package de.brokenpipe.dojo.undercovered.coverista.tracking;

import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClassTracker {

	private final String className;
	private final Map<Integer, LineTracker> lines = new HashMap<>();

	public LineTracker trackLine(final int line, final String methodName, final String descriptor) {
		return lines.computeIfAbsent(Integer.valueOf(line), k -> new LineTracker(line, methodName, descriptor));
	}

	public LineTracker line(final int line) {
		return lines.get(Integer.valueOf(line));
	}

}
