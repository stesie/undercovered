package de.brokenpipe.dojo.undercovered.agent;

import java.lang.instrument.Instrumentation;

import de.brokenpipe.dojo.undercovered.coverista.tracking.Tracker;
import lombok.extern.java.Log;

@Log
public class UndercoveredAgent {

	public static void premain(final String agentArgs, final Instrumentation inst) {

		log.info("[Agent] In premain method");
		final var collector = Tracker.createCollector(null, null);

		inst.addTransformer(new UndercoverTransformer(collector));

		Runtime.getRuntime().addShutdownHook(
				new Thread(() -> System.out.println("In the middle of a shutdown"))
		);
	}
}
