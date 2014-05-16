package me.mini.tests;

import junit.framework.Assert;
import me.mini.algo.strategy.RandomUrlShorteningStrategy;
import me.mini.algo.strategy.SequentialUrlShorteningStrategy;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author parampreetsethi
 * 
 */
public class UrlShorteningStrategyTest {
	RandomUrlShorteningStrategy randomHashStrategy;

	@Before
	public void init() {
		randomHashStrategy = new RandomUrlShorteningStrategy();
	}

	@Test
	public void verifyRandomHashStrategy() {

		String hash = randomHashStrategy.generateUrlHash();
		Assert.assertNotNull(hash);
		Assert.assertEquals(hash.length(),
				RandomUrlShorteningStrategy.DEFAULT_HASH_LENGTH);
	}

	@Test
	public void verifySeuentialStrategy() {
		long num = 100000000000l;
		String hash = SequentialUrlShorteningStrategy.encode(num);
		Assert.assertEquals(hash, "bLj9OjO");
	}

}
