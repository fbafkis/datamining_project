package com.francescobertamini.core.utility;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class DataNormalizer {


    public static ArrayList<float[]> normalizeUM(ArrayList<int[]> utilityMatrix) {

        ArrayList<float[]> normalizedUM = new ArrayList<>();

        int counter = 0;
        for (int[] l : utilityMatrix) {
            if (counter == 0) {

                float normalizedUMfirstLine [] = new float[utilityMatrix.get(0).length];

                for(int i=0;i< utilityMatrix.get(0).length; i++){
                    normalizedUMfirstLine[i] = (float) utilityMatrix.get(0)[i];
                }

                normalizedUM.add(normalizedUMfirstLine);

            } else {
                int[] values = new int[l.length - 1];

                for (int i = 1; i < l.length; i++) {
                    values[i - 1] = l[i];
                }

                // Allocate storage for the normalized array
                float[] normalized = new float[values.length];

                // Determine the minimum and maximum
                int min = getMinValue(values);
                int max = getMaxValue(values);
                int spread = max - min;

                // If there is no spread, return a flat normalization
                if (spread == 0) {
                    Arrays.fill(normalized, 100f);
                }

                // Normalize each value in the input array
                for (int i = 0; i < values.length; i++) {
                    normalized[i] = ((values[i] - min) / (float) spread)*100;
                }

                float normalizedUMLine[] = new float[l.length];
                normalizedUMLine[0] = l[0];
                for (int i = 0; i < normalized.length; i++) {
                    normalizedUMLine[i + 1] = normalized[i];
                }
                normalizedUM.add(normalizedUMLine);
            }
            counter++;
        }

        for ( int i = 0; i<500; i++){
                float[] l = normalizedUM.get(i);
            for (float e : l) {
                System.out.print(e + " ");
            }
            System.out.println();
        }
        return normalizedUM;
    }


    /**
     * Returns the minimum value in the given array of values, or {@link
     * Integer#MAX_VALUE} if the array is null or zero-length.
     */
    private static int getMinValue(int[] values) {
        int min = Integer.MAX_VALUE;
        int vcount = (values == null) ? 0 : values.length;
        for (int ii = 0; ii < vcount; ii++) {
            if (values[ii] < min) {
                // new min
                min = values[ii];
            }
        }
        return min;
    }

    /**
     * Returns the maximum value in the given array of values, or {@link
     * Integer#MIN_VALUE} if the array is null or zero-length.
     */
    private static int getMaxValue(int[] values) {
        int max = Integer.MIN_VALUE;
        int vcount = (values == null) ? 0 : values.length;
        for (int ii = 0; ii < vcount; ii++) {
            if (values[ii] > max) {
                // new max
                max = values[ii];
            }
        }
        return max;
    }

}
