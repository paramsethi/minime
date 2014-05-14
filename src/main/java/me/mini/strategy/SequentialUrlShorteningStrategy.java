package me.mini.strategy;

/**
 * @author parampreetsethi
 * 
 *         Pick a sequential id and convert it to base 36 number
 */

public class SequentialUrlShorteningStrategy implements UrlShorteningStrategy {

	private static final String CHARS = "abcdefghijklmnopqrstuvwxyz0123456789";
	private static final int BASE = 36;

	// This should be generated in distributed environment.
	private static int counter = 1;

	public static String encode(int num) {
		StringBuilder sb = new StringBuilder();

		while (num > 0) {
			sb.append(CHARS.charAt(num % BASE));
			num /= BASE;
		}

		return sb.reverse().toString();
	}

	public static int decode(String str) {
		int num = 0;

		for (int i = 0, len = str.length(); i < len; i++) {
			num = num * BASE + CHARS.indexOf(str.charAt(i));
		}

		return num;
	}

	@Override
	public String getUrlHash() {
		return encode(counter++);
	}
}
