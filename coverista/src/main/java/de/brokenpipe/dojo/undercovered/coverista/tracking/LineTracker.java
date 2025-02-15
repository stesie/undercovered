package de.brokenpipe.dojo.undercovered.coverista.tracking;

import lombok.Getter;

@Getter
public class LineTracker {

	private final int line;
	private final String methodSignature;

	private int hitCount = 0;

	public LineTracker(final int line, final String methodName, final String descriptor) {
		this.line = line;
		this.methodSignature = "%s%s".formatted(methodName, descriptor);
	}

	public void hit() {
		hitCount ++;
	}
}
