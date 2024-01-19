package de.brokenpipe.dojo.undercovered.coverista;

import lombok.extern.java.Log;

@Log
public class Tracker {

	public static void track(final int line) {
		final var callingClass = Thread.currentThread().getStackTrace()[2].getClassName();
		log.finer("hit " + callingClass + ":" + line);
	}
}
