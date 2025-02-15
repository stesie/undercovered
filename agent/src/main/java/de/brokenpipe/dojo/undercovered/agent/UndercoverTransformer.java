package de.brokenpipe.dojo.undercovered.agent;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

import de.brokenpipe.dojo.undercovered.coverista.tracking.TrackingCollector;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import de.brokenpipe.dojo.undercovered.coverista.Instrumenter;

@Log
@RequiredArgsConstructor
public class UndercoverTransformer implements ClassFileTransformer {

	private final TrackingCollector collector;

	@Override
	public byte[] transform(final Module module, final ClassLoader loader, final String className,
			final Class<?> classBeingRedefined, final ProtectionDomain protectionDomain, final byte[] classfileBuffer) {
		if (className.startsWith("java/") || className.startsWith("sun/") || className.startsWith("jdk/internal")
				|| className.startsWith("de/brokenpipe/dojo/undercovered/coverista/")) {
			log.fine("*not* instrumenting class " + className);
			return classfileBuffer;
		}

		log.info("transforming " + className);

		try {
			return new Instrumenter(collector).instrumentClass(classfileBuffer);
		} catch (final Throwable e) {
			log.warning("instrumentation of '" + className + "' failed: " + e);
			throw new RuntimeException("instrumentation of '" + className + "' failed", e);
		}
	}
}
