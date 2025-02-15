package de.brokenpipe.dojo.undercovered.coverista.tracking;

import java.util.Set;

import lombok.extern.java.Log;

@Log
public class Tracker {

	static TrackingCollector currentCollector;

	public static void track(final String callingClass, final int line) {
		log.finer("hit " + callingClass + ":" + line);
		currentCollector.trackClass(callingClass).line(line).hit();
	}

	public static TrackingCollector createCollector(final Set<String> includePatterns,
			final Set<String> excludePatterns) {
		currentCollector = new TrackingCollector(includePatterns, excludePatterns);
		return currentCollector;
	}
}
