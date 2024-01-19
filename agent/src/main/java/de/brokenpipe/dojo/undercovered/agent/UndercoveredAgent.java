package de.brokenpipe.dojo.undercovered.agent;

import java.lang.instrument.Instrumentation;

import lombok.extern.java.Log;

@Log
public class UndercoveredAgent {

	public static void premain(final String agentArgs, final Instrumentation inst) {

		log.info("[Agent] In premain method");
		inst.addTransformer(new UndercoverTransformer());

		Runtime.getRuntime().addShutdownHook(
				new Thread(() -> System.out.println("In the middle of a shutdown"))
		);
	}
}
