package de.brokenpipe.dojo.undercovered.demo;

public class Demo {

	public static void main(final String[] argv) {
		final String greeting = "Hello World";

		bla(greeting);
		bla("to the blarg");

	}

	private static void bla(final String greeting) {
		System.out.println(greeting);
	}
}
