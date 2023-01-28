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

                float normalizedUMfirstLine[] = new float[utilityMatrix.get(0).length];

                for (int i = 0; i < utilityMatrix.get(0).length; i++) {
                    normalizedUMfirstLine[i] = (float) utilityMatrix.get(0)[i];
                }

                normalizedUM.add(normalizedUMfirstLine);

            } else {
                int[] values = new int[l.length - 1];

                for (int i = 1; i < l.length; i++) {
                    values[i - 1] = l[i];
                }

                // Allocate storage for the normalized array
                float[] normalizedArray = new float[values.length];

                int sum = 0;
                int elementCounter = 0;
                for (int e : values) {
                    sum += e;
                    if (e != 0) {
                        elementCounter++;
                    }
                }

                float average = (float) sum / (float) elementCounter;


                // Normalize each value in the input array
                for (int i = 0; i < values.length; i++) {
                    if (values[i] != 0) {
                        normalizedArray[i] = (float) values[i] - average;
                    } else {
                        normalizedArray[i] = -101f;
                    }
                }

                float normalizedUMLine[] = new float[l.length];
                normalizedUMLine[0] = l[0];
                for (int i = 0; i < normalizedArray.length; i++) {
                    normalizedUMLine[i + 1] = normalizedArray[i];
                }
                normalizedUM.add(normalizedUMLine);
            }
            counter++;
        }

        // for (int i = 0; i < 500; i++) {
        //    float[] l = normalizedUM.get(i);
            //for (float e : l) {
                //System.out.print(e + " ");
            //}
            //System.out.println();
        //}
        return normalizedUM;
    }
}
