package me.mini.strategy;

import me.mini.entity.StatsEntity;

public class RandomUrlShorteningStrategy implements UrlShorteningStrategy {

	@Override
	public String getUrlHash() {
		return generateRandomHash();
	}

	/**
	 * Generate random hash
	 * 
	 * @return
	 */
	private String generateRandomHash() {
		StringBuilder sb = new StringBuilder();
		int[] randomHashes = generateRandomIndexes(HASH_SIZE, HASH_CHARS.length);

		for (int i = 0; i < randomHashes.length; i++) {
			sb.append(HASH_CHARS[randomHashes[i]]);
		}

		return sb.toString();
	}

	public StatsEntity systemStats() {
		StatsEntity entity = new StatsEntity();

		entity.setTotalUrls((long) Math.pow(HASH_CHARS.length, HASH_SIZE));
		long startTime = System.currentTimeMillis();
		long urlNum = 5000000;
		for (int i = 0; i < urlNum; i++) {
			getUrlHash();
		}
		long totalTime = (System.currentTimeMillis() - startTime);

		entity.setTestUrlRan(urlNum);
		entity.setTimeTakenInMS(totalTime);

		entity.setTotalMemory(Runtime.getRuntime().totalMemory());

		entity.setFreeMemory(Runtime.getRuntime().freeMemory());

		return entity;
	}

	/**
	 * generate n random indexes from 0 to m ranges
	 * 
	 * @param n
	 * @param m
	 * @return
	 */
	private int[] generateRandomIndexes(int n, int m) {
		int[] randomIndexes = new int[n];
		for (int i = 0; i < n; i++) {
			randomIndexes[i] = (int) (Math.random() * m - 1);
		}
		return randomIndexes;
	}

}