package com.francescobertamini.core.utility;

import java.util.HashSet;

public class JaccardComparator {

    //TODO: add the documentation

    /**
     * Checks if bit is set at a given position
     *
     * @param pos the position
     * @param ID the ID
     * @return
     */
    public static byte getBitAtPosition(int pos, byte ID) {
        return (byte) ((ID >> pos) & 1);
    }

    /**
     * This function finds the Jaccard similarity of the tuples having 2 HashSet of strings as input the function
     * returns the Jaccard similarity of the 2 strings
     *
     * @param in1 the first HashSet to compare
     * @param in2 the first HashSet to compare
     * @return the Jaccard similarity index
     */
    public static float getJaccardScore(HashSet<String> in1, HashSet<String> in2) {
        //Sets the variables
        byte[] string1 = null;
        byte[] string2 = null;
        float jaccard = 0;
        int size = 0;
        int byteSize = 0;
        //Converts the HashSet to string
        String[] arr1 = in1.toArray(new String[in1.size()]);
        String[] arr2 = in2.toArray(new String[in2.size()]);
        //Finds the longest string
        if (arr1.length < arr2.length) {
            size = arr1.length;
        } else {
            size = arr2.length;
        }
        for (int i = 0; i < size; i++) {
            //Gets the bytes of each char
            string1 = arr1[i].getBytes();
            string2 = arr2[i].getBytes();
            if (string1.length > string2.length) {
                //Finds the length of the string in bits
                byteSize = byteSize + (string2.length * 8);
                for (int j = string2.length - 1; j >= 0; j--) {
                    for (int k = 0; k < 8; k++) {
                        //If bit is set or not in both strings, the Jaccard similarity is increased
                        if (getBitAtPosition(k, string2[j]) == getBitAtPosition(k, string1[j])) {
                            jaccard++;
                        }
                    }
                }
            } else {
                //Finds the length of the string in bits
                byteSize = byteSize + (string1.length * 8);
                for (int j = string1.length - 1; j >= 0; j--) {
                    for (int k = 0; k < 8; k++) {
                        //If bit is set or not in both strings, the Jaccard similarity is increased
                        if (getBitAtPosition(k, string1[j]) == getBitAtPosition(k, string2[j])) {
                            jaccard++;
                        }
                    }
                }
            }
        }
        //Computes the similarity
        jaccard = jaccard / byteSize;
        return jaccard;
    }
}


