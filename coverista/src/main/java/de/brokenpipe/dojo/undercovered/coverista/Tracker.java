package de.brokenpipe.dojo.undercovered.coverista;

import lombok.extern.java.Log;

@Log
public class Tracker {

	public static void track(final String callingClass, final int line) {
		log.finer("hit " + callingClass + ":" + line);
	}
}
