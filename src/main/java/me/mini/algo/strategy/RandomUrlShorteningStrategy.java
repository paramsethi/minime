package me.mini.algo.strategy;

import me.mini.utils.GlobalUtils;
import me.mini.utils.PropertyBag;

/**
 * This class generates random 6 character hash.
 *
 * @author parampreetsethi
 *  
 * Algorithm
 * ----------
 * The basic strategy is to have a array of allowed characters (char[] allowedHashCharacters) Here it's A-Z, a-z and 0-9 characters
 * which is in total 62 characters. Using random function generate 6 different integers between 0-61 range. These are then used as
 * indexes of array - to pick random chars (from char[] allowedHashCharacters) and create a 6 digit hash.
 *  
 * For example
 * -----------
 * If random function returns [0,5,3,29,17,33]. We pick the 0th, 5th, 3rd, etc index values from char[] allowedHashCharacters.
 * The corresponding hash will be "aed3p7".
 *  
 * Using 6 hash chars, with each place can be chosen in 62 ways, the total number of possible hashes are 62^6 ~ 56 Billion.
 *  
 * To scale the algorithm more than 56 Billion
 * --------------------------------------------
 * The functionality can scale either by adding one extra character to hash which will increase the number of urls,
 * or by adding more url supported ascii characters.
 *  
 * 0 char = 1 URLs
 * 1 char = 62 URLs
 * 2 chars = 3,844 URLs
 * 3 chars = 238,328 URLs
 * 4 chars = 14,776,336 URLs
 * 5 chars = 916,132,832 URLs
 * 6 chars ~ 56,800,235,580 URLs
 * 7 chars ~ 3,521,614,606,000 URLs
 * 8 chars ~ 218,340,105,600,000 URLs
 * 9 chars ~ 13,537,708,655,000,000 URLs
 * 10 chars ~ 839,299,365,900,000,000 URLs
 * 11 chars ~ 52,036,560,680,000,000,000 URLs
 */
public class RandomUrlShorteningStrategy implements BaseUrlShorteningStrategy {

    /**
     * @return
     */
    public String generateUrlHash() {
        return generateRandomHash();
    }

    /**
     * @return
     */
    private String generateRandomHash() {
        // size of the generated hash
        int allowedHashLength = hashLength();
        // characters than can used to represent a hash
        char[] allowedHashCharacters = hashChars();
        // set of random indexes (in range of 0 -> allowedHashCharacters.length)
        int[] randomIndexes = generateRandomIndexes(allowedHashLength, allowedHashCharacters.length);
        // generate the randomIndex value; an pick a character from allowedHashCharacters[randomIndex] to build the hash
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < randomIndexes.length; i++) {
            int randomIndex = randomIndexes[i];
            sb.append(allowedHashCharacters[randomIndex]);
        }
        return sb.toString();
    }

    /**
     * generate n random indexes - from within 0 to m range
     *
     * @param valuesRequired
     * @param rangeMax
     * @return
     */
    private int[] generateRandomIndexes(int valuesRequired, int rangeMax) {
        int[] randomIndexes = new int[valuesRequired];
        for (int i = 0; i < valuesRequired; i++) {
            randomIndexes[i] = (int) (Math.random() * rangeMax - 1);
        }
        return randomIndexes;
    }

    /**
     * @return
     */
    private int hashLength() {
        int length = PropertyBag.getIntProperty("RandomUrlShorteningStrategy.hash.length", DEFAULT_HASH_LENGTH);
        return length;
    }

    /**
     * @return
     */
    private char[] hashChars() {
        String value = PropertyBag.getProperty("RandomUrlShorteningStrategy.hash.chars");
        if (GlobalUtils.isStringNullOrEmpty(value)) {
            return DEFAULT_HASH_CHARACTERS;
        }
        return value.toCharArray();
    }


}