package com.francescobertamini.core.utility;

import java.util.ArrayList;

public class DataNormalizer {

    /**
     * It normalizes the data inside the utility matrix. On each row, it subtracts to every element the average of all
     * the elements on that row.
     *
     * @param utilityMatrix the utility matrix to normalize
     * @return the normalized version of the utility matrix
     */
    public static ArrayList<float[]> normalizeUM(ArrayList<int[]> utilityMatrix) {
        //The normalized UM made by arrays of float.
        ArrayList<float[]> normalizedUM = new ArrayList<>();
        //Cycling over the UM lines.
        int counter = 0;
        for (int[] l : utilityMatrix) {
            //Managing the first line case, containing the queries' IDs.
            if (counter == 0) {
                float normalizedUMfirstLine[] = new float[utilityMatrix.get(0).length];
                for (int i = 0; i < utilityMatrix.get(0).length; i++) {
                    normalizedUMfirstLine[i] = (float) utilityMatrix.get(0)[i];
                }
                normalizedUM.add(normalizedUMfirstLine);
                //Normalize the lines.
            } else {
                //Place only the values without the UID into a variable.
                int[] values = new int[l.length - 1];
                for (int i = 1; i < l.length; i++) {
                    values[i - 1] = l[i];
                }
                // Allocate storage for the normalized array.
                float[] normalizedArray = new float[values.length];
                //Calculating the average.
                int sum = 0;
                int elementCounter = 0;
                for (int e : values) {
                    sum += e;
                    if (e != 0) {
                        elementCounter++;
                    }
                }
                float average = (float) sum / (float) elementCounter;
                // Normalize each value in the input array.
                for (int i = 0; i < values.length; i++) {
                    if (values[i] != 0) {
                        normalizedArray[i] = (float) values[i] - average;
                    } else {
                        //If the original int score is 0 it means that it was absent. It is replaced with -101f value.
                        normalizedArray[i] = -101f;
                    }
                }
                //Prepare the new line for the normalized UM.
                float normalizedUMLine[] = new float[l.length];
                normalizedUMLine[0] = l[0];
                for (int i = 0; i < normalizedArray.length; i++) {
                    normalizedUMLine[i + 1] = normalizedArray[i];
                }
                //Add the new line to the normalized UM.
                normalizedUM.add(normalizedUMLine);
            }
            counter++;
        }
        //Log
        System.out.println();
        System.out.println("Data Normalizer - Data normalized.");

        //Print the normalized UM.
         /*for (int i = 0; i < normalizedUM.size(); i++) {
            float[] l = normalizedUM.get(i);
        for (float e : l) {
        System.out.print(e + " ");
        }
        System.out.println();
        }*/

        //Return the normalized utility matrix.
        return normalizedUM;
    }
}
