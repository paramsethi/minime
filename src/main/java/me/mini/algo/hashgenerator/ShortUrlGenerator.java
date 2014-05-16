package me.mini.algo.hashgenerator;

import me.mini.algo.strategy.BaseUrlShorteningStrategy;
import me.mini.cassandra.query.CassandraUrlQueryUtil;
import me.mini.utils.MinimeException;
import org.apache.log4j.Logger;


public class ShortUrlGenerator {

    private CassandraUrlQueryUtil cassandraUrlQueryUtil;
    private BaseUrlShorteningStrategy urlShorteningStrategy;
    private static final Logger log = Logger.getLogger(ShortUrlGenerator.class);

    public ShortUrlGenerator(BaseUrlShorteningStrategy strategy) {
        this.urlShorteningStrategy = strategy;
		try {
			this.cassandraUrlQueryUtil = CassandraUrlQueryUtil.getInstance();
		} catch (MinimeException mex) {
			mex.printStackTrace();
		}
	}

    public String generateShortUrlHash() throws MinimeException {
        int collisionCount = 0;
        // generated hash as per the strategy implementation
        String shortUrlHash = urlShorteningStrategy.generateUrlHash();
        // check for hash collision; if generated hash already collides, find the next unique
        while (cassandraUrlQueryUtil.queryByUrlHash(shortUrlHash) != null) {
            collisionCount++;
            shortUrlHash = urlShorteningStrategy.generateUrlHash();
        }
        log.info(String.format("Collision count for shortUrlHash: %s = %s", shortUrlHash, collisionCount));
        return shortUrlHash;
    }


}
