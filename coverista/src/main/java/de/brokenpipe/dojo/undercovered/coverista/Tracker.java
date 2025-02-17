package de.brokenpipe.dojo.undercovered.coverista;

import lombok.extern.java.Log;

@Log
public class Tracker {

	public static void track() {
		final StackTraceElement element = Thread.currentThread().getStackTrace()[2];
		final String callingClass = element.getClassName();
		log.finer("hit " + callingClass + ":" + element.getLineNumber());
	}
}
