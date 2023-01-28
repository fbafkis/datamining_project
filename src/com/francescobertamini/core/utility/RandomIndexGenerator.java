package com.francescobertamini.core.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomIndexGenerator {

    /**
     * @param maxNum The maximum size of the input integer array.
     * @param source The input array containing the indexes to work on.
     * @return The integer ArrayList containing the randomly chosen indexes sorted in increasing order.
     */
    public static ArrayList<Integer> getRandomIndexes(int maxNum, int[] source) {
        //Generate random number of indexes to pick.
        Random random = new Random();
        int numberChoosenToConsider = ThreadLocalRandom.current().nextInt(1, maxNum + 1);
        //Indexes are added to an ArrayList, so that they can be shuffled .
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int e = 0; e < maxNum; e++) {
            indexes.add(source[e]);
        }
        //All the indexes get shuffled.
        Collections.shuffle(indexes);
        ArrayList<Integer> orderedIndexes = new ArrayList<>();
        //Add the N randomly picked indexes to an ArrayList that will get ordered.
        for (int q = 0; q < numberChoosenToConsider; q++) {
            orderedIndexes.add(indexes.get(q));
        }
        //Order the final list of attributes' indexes.
        Collections.sort(orderedIndexes);
        //Return the final ordered ArrayList.
        return orderedIndexes;
    }
}
