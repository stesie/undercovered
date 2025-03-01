package de.brokenpipe.dojo.undercovered.demo;

public class Demo3 {
	public static void main(final String[] argv) {
		final Stuff stuff = new Stuff(
				!getBoolean());
		bla("value: " + stuff.boolValue());
	}

	public static boolean getBoolean() {
		return true;
	}

	private static void bla(final String greeting) {
		System.out.println(greeting);
	}

	record Stuff(boolean boolValue) {
	}
}
