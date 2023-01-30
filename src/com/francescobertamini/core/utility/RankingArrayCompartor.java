package com.francescobertamini.core.utility;

import java.util.Comparator;

public class RankingArrayCompartor {

    //Custom comparator to compare the ranking matrix lines on the matching score (the float in position 1 in every
    // line).
    public static Comparator<float[]> customArrayComparator = new Comparator<float[]>() {
        @Override
        public int compare(float[] array1, float[] array2) {
            return Float.compare(array1[1], array2[1]);
        }
    };
}
