package de.brokenpipe.dojo.undercovered.coverista;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import de.brokenpipe.dojo.undercovered.coverista.tracking.TrackingCollector;
import lombok.RequiredArgsConstructor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

@RequiredArgsConstructor
public class Instrumenter {

	private final TrackingCollector trackingCollector;

	public Instrumenter() {
		trackingCollector = null;
	}

	public byte[] instrumentClass(final String fileName) {
		try (final var inputStream = new FileInputStream(fileName)) {
			return instrumentClass(inputStream);
		} catch (final FileNotFoundException e) {
			throw new RuntimeException("file does not exist: " + fileName, e);
		} catch (final IOException e) {
			throw new RuntimeException("unable to read file: " + fileName, e);
		}
	}

	public byte[] instrumentClass(final InputStream inputStream) throws IOException {
		return instrumentClass(inputStream.readAllBytes());
	}

	public byte[] instrumentClass(final byte[] classBytes) {
		// first pass: gather label information
		final JumpLabelCollector jumpLabelCollector = collectJumpLabels(
				classBytes);

		// second pass: instrument
		return instrumentClass(classBytes, jumpLabelCollector).toByteArray();
	}

	private JumpLabelCollector collectJumpLabels(final byte[] classBytes) {
		final JumpLabelCollector jumpLabelCollector = new JumpLabelCollector();
		final CoveristaClassVisitor labelCollVisitor = new CoveristaClassVisitor(null,
				(className, methodName, descriptor, mv)
						-> new LabelCollectingMethodVisitor(mv, jumpLabelCollector));
		new ClassReader(classBytes).accept(labelCollVisitor, 0);
		return jumpLabelCollector;
	}

	private ClassWriter instrumentClass(final byte[] classBytes, final JumpLabelCollector jumpLabelCollector) {
		final ClassReader reader = new ClassReader(classBytes);
		final var writer = new ClassWriter(0);

		final var classVisitor = new CoveristaClassVisitor(writer, (clazz, method, descriptor, mv)
				-> {
			final var lineRegisteringVisitor = trackingCollector == null
					? mv
					: new LineRegisteringMethodVisitor(mv, trackingCollector.trackClass(clazz), method, descriptor);

			return new InstrumentingMethodVisitor(lineRegisteringVisitor, jumpLabelCollector.getJumpLabels(), clazz);
		});
		reader.accept(classVisitor, 0);
		return writer;
	}

}
