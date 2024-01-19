package de.brokenpipe.dojo.undercovered.coverista;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

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

	public byte[] instrumentClass(final FileInputStream inputStream) throws IOException {
		return instrumentClass(new ClassReader(inputStream));
	}

	public byte[] instrumentClass(final byte[] classBytes) throws IOException {
		return instrumentClass(new ClassReader(classBytes));
	}

	private byte[] instrumentClass(final ClassReader reader) {
		final var writer = new ClassWriter(0);

		final var classVisitor = new CoveristaClassVisitor(writer);
		reader.accept(classVisitor, 0);

		return writer.toByteArray();
	}
}
