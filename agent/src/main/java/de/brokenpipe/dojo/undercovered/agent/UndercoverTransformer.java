package de.brokenpipe.dojo.undercovered.agent;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

import lombok.extern.java.Log;

import de.brokenpipe.dojo.undercovered.coverista.Instrumenter;

@Log
public class UndercoverTransformer implements ClassFileTransformer {
	@Override
	public byte[] transform(final Module module, final ClassLoader loader, final String className,
			final Class<?> classBeingRedefined, final ProtectionDomain protectionDomain, final byte[] classfileBuffer) {
		if (className.startsWith("java/") || className.startsWith("sun/") || className.startsWith("jdk/internal")
				|| className.startsWith("de/brokenpipe/dojo/undercovered/coverista/")) {
			log.fine("*not* instrumenting class " + className);
			return classfileBuffer;
		}

		log.info("transforming " + className);

		log.fine("source path is " + protectionDomain.getCodeSource().getLocation().getPath());

		try {
			return new Instrumenter().instrumentClass(classfileBuffer);
		} catch (final IOException e) {
			throw new RuntimeException("instrumentation of '" + className + "' failed", e);
		}
	}
}
