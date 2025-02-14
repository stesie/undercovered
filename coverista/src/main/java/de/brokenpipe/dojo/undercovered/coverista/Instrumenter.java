package de.brokenpipe.dojo.undercovered.coverista;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class Instrumenter {
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
		final JumpLabelCollector jumpLabelCollector = new JumpLabelCollector();
		final CoveristaClassVisitor labelCollVisitor = new CoveristaClassVisitor(null,
				(className, methodName, mv) -> new CoveristaLabelCollectingMethodVisitor(mv, jumpLabelCollector));
		new ClassReader(classBytes).accept(labelCollVisitor, 0);

		// second pass: instrument
		final ClassReader reader = new ClassReader(classBytes);
		final var writer = new ClassWriter(0);

		final var classVisitor = new CoveristaClassVisitor(writer,
				(className, methodName, mv)
						-> new CoveristaInstrumentingMethodVisitor(mv, jumpLabelCollector.getJumpLabels(), className));
		reader.accept(classVisitor, 0);

		return writer.toByteArray();
	}

}
