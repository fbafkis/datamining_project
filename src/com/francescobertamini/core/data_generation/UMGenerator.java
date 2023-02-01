package com.francescobertamini.core.data_generation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static com.francescobertamini.core.utility.FileWriter.writeFile;
import static com.francescobertamini.core.utility.RandomIndexGenerator.getRandomIndexes;

public class UMGenerator {

    /**
     * Generates the utility matrix, in two version: the string composed one, used to print the CSV file, and the
     * numerical one, used inside the program.
     *
     * @param queryIDs           the array containing all the queries' IDs
     * @param UMColumnsDimension width dimension of the utility matrix
     * @param userIDs            the array containing all the users' IDs
     * @return the numerical version of the utility matrix
     */
    public static ArrayList<int[]> generateUM(int queryIDs[], int UMColumnsDimension, int userIDs[]) {
        //The utility matrix with lines as String, to be printed easily.
        String utilityMatrix[] = new String[UMColumnsDimension + 1];
        //The utility matrix only with numbers inside, that will be used into the program.
        ArrayList<int[]> splittedUtilityMatrix = new ArrayList<>();

        //Compose the utility matrix
        //Prepare the first row of the string version containing all the query IDs.
        String umFirstLine = "USER_IDs,Q" + Integer.toString(queryIDs[0]);
        //Populating the first row of the numeric version.
        int splittedUMFirstLine[] = new int[UMColumnsDimension + 1];
        splittedUMFirstLine[0] = -1;
        splittedUMFirstLine[1] = queryIDs[0];
        splittedUtilityMatrix.add(splittedUMFirstLine);
        //Populating the first row of the string version.
        for (int t = 1; t < queryIDs.length; t++) {
            umFirstLine += ",Q" + Integer.toString(queryIDs[t]);
            splittedUMFirstLine[t + 1] = queryIDs[t];
        }
        utilityMatrix[0] = umFirstLine;
        //Prepare all the lines containing the scores referred to the queries for each user.
        for (int u = 0; u < userIDs.length; u++) {
            String umLine = "U" + Integer.toString(userIDs[u]);
            //Numerical version
            int splittedUMLine[] = new int[UMColumnsDimension + 1];
            splittedUMLine[0] = userIDs[u];
            ArrayList<Integer> orderedChoosenQueriesIDs = getRandomIndexes(queryIDs.length, queryIDs);
            for (int q = 0; q < UMColumnsDimension; q++) {
                if (orderedChoosenQueriesIDs.contains(queryIDs[q])) {
                    Random random = new Random();
                    int randomScore = ThreadLocalRandom.current().nextInt(1, 100 + 1);
                    umLine += "," + Integer.toString(randomScore);

                    splittedUMLine[q + 1] = randomScore;
                } else {
                    umLine += ",";
                    splittedUMLine[q + 1] = 0;
                }
            }
            //Adding string version's line.
            utilityMatrix[u + 1] = umLine;
            //Adding numerical version's line.
            splittedUtilityMatrix.add(splittedUMLine);
        }
        //Print the string version utility matrix.
        /* for (String s : utilityMatrix) {
            System.out.println(s);
        }*/

        //Print the utility matrix to file.
        try {
            writeFile("utility_matrix", utilityMatrix);
            //Log
            System.out.println();
            System.out.println("UM Generator - Utility matrix CSV file created.");
        } catch (IOException e) {
            System.err.println();
            System.err.println("UM Generator - Error in writing the utility matrix CSV file.");
        }

        //Print the numerical version utility matrix.
        /* for(int [] l : splittedUtilityMatrix) {
        for (int e : l) {
             System.out.print(e +" ");
         }
         System.out.println();
         }*/

        //Returning only the numerical version of UM.
        return splittedUtilityMatrix;
    }
}



