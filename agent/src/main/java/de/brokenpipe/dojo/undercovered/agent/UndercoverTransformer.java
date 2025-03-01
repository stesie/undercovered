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
		if (className.startsWith("java/")
				|| className.startsWith("javax/")
				|| className.startsWith("jdk/")
				|| className.startsWith("sun/")
				|| className.startsWith("de/brokenpipe/dojo/undercovered/coverista/")) {
			log.fine("*not* instrumenting class " + className);
			return classfileBuffer;
		}

		if (loader == null) {
			log.warning("class loader is null, not instrumenting " + className);
			return classfileBuffer;
		}

		try {
			loader.loadClass("de.brokenpipe.dojo.undercovered.coverista.tracking.Tracker");
		} catch (final ClassNotFoundException e) {
			log.warning("class loader does not know about our Tracker, not instrumenting " + className);
			return classfileBuffer;
		}

		log.fine("transforming " + className + ", module: " + module + ", loader: " + loader);

		try {
			return new Instrumenter(collector).instrumentClass(classfileBuffer);
		} catch (final Throwable e) {
			log.warning("instrumentation of '" + className + "' failed: " + e);
			throw new RuntimeException("instrumentation of '" + className + "' failed", e);
		}
	}
}
