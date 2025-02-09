package de.brokenpipe.dojo.undercovered.coverista;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import lombok.Getter;

@Getter
public class JumpLabelCollector implements Consumer<Integer> {

	private Set<Integer> jumpLabels = new HashSet<>();

	@Override
	public void accept(final Integer lineNumber) {
		jumpLabels.add(lineNumber);
	}
}
