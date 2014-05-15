package me.mini.algo.strategy;

import me.mini.utils.GlobalUtils;
import me.mini.utils.PropertyBag;

/**
 * This class generates sequential hash using characters a-z, A-Z, and 0-9.
 *
 * @author parampreetsethi
 *
 * Algorithm
 * ----------
 * The basic strategy is to have a sequential counter and convert the generated number to corresponding base 62 number using A-Z, a-z and 0-9 characters.
 * The counter starts from 10,000 to ensure the numbers are not single digit.
 *
 * For example
 * -----------
 * If counter = 10,000, the corresponding base 62 number is "cCs".
 * If counter = 12324324, the corresponding base 62 value is "QJh1".
 *
 * To scale the algorithm
 * ----------------------
 * As the numbers are generated sequentialy, there is no possibility of collisions. The algorithm can scale with no limitation.
 * For example: 100,000,000,000'th url will have hash = bLj9OjO
 *
 * To scale in a distributed application
 * --------------------------------------
 * In a distributed application, the challenge is to maintain counters thread safe way. The counter READ and INCREMENT has to be one atomic process.
 * This can *NOT* be done via -
 * 1) JVM in-memory (because multple instances hitting the same counter)
 * 2) Cassandra 'counter' column, or memcache (because read and increment of counter are not atomic)
 *
 * Solution:
 * Use transaction based solution (mysql/oracle's auto_increment columns) to maintain counter value - ensuring read and increment as atomic.
 *
 */
public class SequentialUrlShorteningStrategy implements BaseUrlShorteningStrategy {

    private static final int BASE = 62;
    private static String allowedHashCharString = null;

    static {
        allowedHashCharString = hashChars();
    }

    // This should be generated in distributed environment.
    private static long counter = 1;

    public String generateUrlHash() {
        long sequenceValue = getNextCounter();
        return encode(sequenceValue);
    }

    public static String encode(long num) {
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            int index = (int) (num % BASE);
            if (index < 0) {
                System.out.println(index + " " + num);
            }
            char c = allowedHashCharString.charAt(index);
            sb.append(c);
            num /= BASE;
        }
        return sb.reverse().toString();
    }

    private long getNextCounter() {
        counter++;
        return counter;
    }

    private static String hashChars() {
        String value = PropertyBag.getProperty("RandomUrlShorteningStrategy.hash.chars");
        if (GlobalUtils.isStringNullOrEmpty(value)) {
            return new String(DEFAULT_HASH_CHARACTERS);
        }
        return value;
    }

    public static void main(String[] args) {
        long num = 100000000000l;
        System.out.println("The encoded number is ** " + encode(num));
    }

}