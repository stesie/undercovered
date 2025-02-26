package de.brokenpipe.dojo.undercovered.demo;

import java.util.function.Supplier;

public class Demo2 {
	public static void main(final String[] argv) {
		final String greeting = "Hello World";
		bla(greeting);

		for (int i = 0; i < 3; i++) {
			bla("to the blarg");
		}

		final Supplier<Integer> numberSupplier = () -> Integer.valueOf(42);
		bla("the value: " + numberSupplier.get());
	}

	private static void bla(final String greeting) {
		System.out.println(greeting);
	}
}
