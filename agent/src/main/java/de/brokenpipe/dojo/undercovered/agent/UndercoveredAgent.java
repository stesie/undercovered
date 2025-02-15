package de.brokenpipe.dojo.undercovered.agent;

import java.lang.instrument.Instrumentation;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.brokenpipe.dojo.undercovered.coverista.tracking.Tracker;
import de.brokenpipe.dojo.undercovered.coverista.tracking.TrackingCollector;
import lombok.extern.java.Log;

@Log
public class UndercoveredAgent {

	public static void premain(final String agentArgs, final Instrumentation inst) {

		log.info("[Agent] In premain method, agentArgs: " + agentArgs);
		final var args = AgentArgs.fromString(agentArgs);

		final var collector = Tracker.createCollector(args.includes, args.excludes);

		final UndercoverTransformer transformer = new UndercoverTransformer(collector);
		inst.addTransformer(transformer);

		Runtime.getRuntime().addShutdownHook(
				new Thread(() -> {
					System.out.println("In the middle of a shutdown");
					inst.removeTransformer(transformer);

					if (args.destfile != null) {
						serializeCollectorToJson(collector, args.destfile);
					}
				})
		);
	}

	private static void serializeCollectorToJson(final TrackingCollector collector, final String destfile) {
		final var objectMapper = new ObjectMapper();

		try (final var writer = new java.io.FileWriter(destfile)) {
			objectMapper.writeValue(writer, collector);
		} catch (final Exception e) {
			log.severe("Unable to serialize collector to JSON:" + e);
		}
	}

	private record AgentArgs(String destfile, Set<String> includes, Set<String> excludes) {
		public static AgentArgs fromString(final String agentArgs) {
			final Map<String, String> argsMap = Arrays.stream(agentArgs.split(","))
					.map(s -> s.split("="))
					.collect(Collectors.toMap(a -> a[0], a -> a[1]));

			final String destfile = argsMap.get("destfile");
			final Set<String> includes = argsMap.containsKey("includes") ?
					Set.of(argsMap.get("includes").split(":")) : Set.of();
			final Set<String> excludes = argsMap.containsKey("excludes") ?
					Set.of(argsMap.get("excludes").split(":")) : Set.of();

			return new AgentArgs(destfile, includes, excludes);
		}
	}
}
