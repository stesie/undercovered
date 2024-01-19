package de.brokenpipe.dojo.undercovered.coverista;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import lombok.extern.java.Log;

@Log
public class Coverista {

	public static void main(final String[] argv) {
		if (argv.length != 2) {
			System.err.println("expected two command arguments, path to input file + path to output file");
			System.exit(1);
		}

		final var result = new Instrumenter().instrumentClass(argv[0]);

		try (final var outputStream = new FileOutputStream(argv[1])) {
			outputStream.write(result);
		} catch (final IOException ex) {
			throw new RuntimeException("unable to write to file: " + argv[1], ex);
		}
	}

}
