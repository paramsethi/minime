package me.mini.tests;

import java.util.HashSet;

import me.mini.strategy.RandomUrlShorteningStrategy;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author parampreetsethi
 * 
 */
public class RandomUrlShorteningStrategyTest {

	private static final Logger log = Logger
			.getLogger(RandomUrlShorteningStrategyTest.class);
	RandomUrlShorteningStrategy randomHashStrategy;
	static int collisionCount = 0;

	@Before
	public void warmup() {
		randomHashStrategy = new RandomUrlShorteningStrategy();
		long urlNum = 100;
		for (int i = 0; i < urlNum; i++) {
			randomHashStrategy.getUrlHash();
		}
	}

	@Test
	public void verifyCollisionStrategy() {
		HashSet<String> hashSet = new HashSet<String>();

		long urlNum = 100000;
		for (int i = 0; i < urlNum; i++) {
			boolean dataAdded = hashSet.add(randomHashStrategy.getUrlHash());
			if (!dataAdded) {
				collisionCount++;
			}
		}

		log.debug("Collisions per 100000 entries " + collisionCount);

	}

}
