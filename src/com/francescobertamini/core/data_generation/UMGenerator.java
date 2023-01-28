package com.francescobertamini.core.data_generation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static com.francescobertamini.core.utility.FileWriter.writeFile;
import static com.francescobertamini.core.utility.RandomIndexGenerator.getRandomIndexes;

public class UMGenerator {

    public  static Object [] generateUM (int queryIDs[], int UMColumnsDimension, int userIDs[] ){
        String utilityMatrix[] = new String[UMColumnsDimension + 1];
        ArrayList<int []> splittedUtilityMatrix = new ArrayList<>();

        //Compose the utility matrix
        //Prepare the first row containing all the query IDs.
        String umFirstLine = "USER_IDs, Q" + Integer.toString(queryIDs[0]);

        int splittedUMFirstLine [] = new int [UMColumnsDimension+1];
        splittedUMFirstLine [0] = -1;
        splittedUMFirstLine [1] = queryIDs[0];

        for (int t = 1; t < queryIDs.length; t++) {
            umFirstLine += ",Q" + Integer.toString(queryIDs[t]);
            splittedUMFirstLine [t+1] = queryIDs[t];
        }
        utilityMatrix[0] = umFirstLine;
        splittedUtilityMatrix.add(splittedUMFirstLine);
        //Prepare all the lines containing the scores referred to the queries for each user.
        for (int u = 0; u < userIDs.length; u++) {
            String umLine = "U" + Integer.toString(userIDs[u]);

            int splittedUMLine [] = new int[UMColumnsDimension +1 ];
            splittedUMLine[0] = userIDs[u];

            ArrayList<Integer> orderedChoosenQueriesIDs = getRandomIndexes(queryIDs.length, queryIDs);
            for (int q = 0; q < UMColumnsDimension; q++) {
                if (orderedChoosenQueriesIDs.contains(queryIDs[q])) {
                    Random random = new Random();
                    int randomScore = ThreadLocalRandom.current().nextInt(1, 100 + 1);
                    umLine += "," + Integer.toString(randomScore);

                    splittedUMLine[q+1] = randomScore;

                } else {
                    umLine += ",,";

                    splittedUMLine[q+1] = 0;
                }
            }
            utilityMatrix[u + 1] = umLine;

           // System.out.println(splittedUMLine [0]);

            splittedUtilityMatrix.add(splittedUMLine);
        }
        //Print the string made utility matrix.
       // for (String s : utilityMatrix) {
           //  System.out.println(s);
        //}
        //Print the utility matrix file
        try {
            writeFile("utility_matrix", utilityMatrix);
            System.out.println("Utility matrix CSV file created.");
        } catch (IOException e) {
            System.err.println("Error in writing the utility matrix CSV file.");
        }

        //Print the UM variable version

       // for(int [] l : splittedUtilityMatrix) {
            //for (int e : l) {
           //     System.out.print(e +" ");
          // }
          // System.out.println();
       // }

        Object[] result = new Object[2];

        result[0] = utilityMatrix;
        result[1] = splittedUtilityMatrix;
        return result;
    }
}
