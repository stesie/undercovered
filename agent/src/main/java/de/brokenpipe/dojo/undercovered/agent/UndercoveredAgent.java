package de.brokenpipe.dojo.undercovered.agent;

import java.lang.instrument.Instrumentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.brokenpipe.dojo.undercovered.coverista.tracking.Tracker;
import de.brokenpipe.dojo.undercovered.coverista.tracking.TrackingCollector;
import lombok.extern.java.Log;

@Log
public class UndercoveredAgent {

	public static void premain(final String agentArgs, final Instrumentation inst) {

		log.info("[Agent] In premain method");
		final var collector = Tracker.createCollector(null, null);

		inst.addTransformer(new UndercoverTransformer(collector));

		Runtime.getRuntime().addShutdownHook(
				new Thread(() -> {
					System.out.println("In the middle of a shutdown");
					serializeCollectorToJson(collector);
				})
		);
	}

	private static void serializeCollectorToJson(final TrackingCollector collector) {
		final var objectMapper = new ObjectMapper();
		try {
			final String json = objectMapper.writeValueAsString(collector);
			System.out.println("Serialized collector to JSON: " + json);
		} catch (final Exception e) {
			log.severe("Unable to serialize collector to JSON:" + e);
		}
	}
}
