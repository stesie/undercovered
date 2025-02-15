package de.brokenpipe.dojo.undercovered.coverista.tracking;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LineTracker {

	private final int line;
	private final String methodName;
	private final String descriptor;

	private int hitCount = 0;

	public void hit() {
		hitCount ++;
	}
}
