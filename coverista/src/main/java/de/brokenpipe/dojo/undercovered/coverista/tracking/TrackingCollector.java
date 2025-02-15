package de.brokenpipe.dojo.undercovered.coverista.tracking;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TrackingCollector {

	private final Set<String> includePatterns;
	private final Set<String> excludePatterns;

	private final Map<String, ClassTracker> classes = new HashMap<>();

	public ClassTracker trackClass(final String className) {
		return classes.computeIfAbsent(className, k -> new ClassTracker(className));
	}
}
