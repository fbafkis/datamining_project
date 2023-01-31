package com.francescobertamini.core.data_generation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class UMReader {

    /**
     * It reads a standardized utility matrix from a CSV file.
     *
     * @param fileName the name of the CSV file containing the utility matrix
     * @return the numerical version of the utility matrix
     * @throws IOException
     */
    public static ArrayList<int[]> readUM(String fileName) throws IOException {
        //The UM saved as an array of strings.
        ArrayList<String> UMStringLines = new ArrayList<>();
        //The numerical version of the UM.
        ArrayList<int[]> splittedUtilityMatrix = new ArrayList<>();
        //Read the file.
        BufferedReader UMReader = new BufferedReader(new FileReader(fileName));
        //All the lines of the utility matrix CSV file are placed into an ArrayList.
        String stringUMLine = null;
        try {
            while ((stringUMLine = UMReader.readLine()) != null) {
                UMStringLines.add(stringUMLine);
            }
            UMReader.close();
        } catch (IOException e) {
            System.err.println();
            System.err.println("Utility Matrix Reader - Unable to open the specified file \"" + fileName + "\".");
            System.exit(1);
        }
        //Every line is analyzed, to retrieve the query IDs, the user IDs and the scores.
        for (int i = 0; i < UMStringLines.size(); i++) {
            //The CSV file line is splitted.
            String[] splitted = UMStringLines.get(i).split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)", UMStringLines.get(0).length());
            //On the first line are placed the IDs of the queries.
            if (i == 0) {
                int UMLine[] = new int[splitted.length];
                //Place -1 value on the
                UMLine[0] = -1;
                for (int w = 1; w < splitted.length; w++) {
                    //Remove the Q letter and save the query ID.
                    int queryID = Integer.parseInt(splitted[w].substring(1));
                    UMLine[w] = queryID;
                }
                splittedUtilityMatrix.add(UMLine);
                //On the other lines
            } else {
                int UMLine[] = new int[splitted.length];
                for (int e = 0; e < splitted.length; e++) {
                    //Analyzing the User ID.
                    if (e == 0) {
                        //Remove U letter from the UID.
                        UMLine[0] = Integer.parseInt(splitted[0].substring(1));
                    } else {
                        //If there is a score.
                        if (!splitted[e].equals("")) {
                            UMLine[e] = Integer.parseInt(splitted[e]);
                            //If there is not, prints 0.
                        } else {
                            UMLine[e] = 0;
                        }
                    }
                }
                splittedUtilityMatrix.add(UMLine);
            }
        }

        //Print the numerical version utility matrix.
        /*for (int[] l : splittedUtilityMatrix) {
            for (int e : l) {
                System.out.print(e + " ");
            }
            System.out.println();
        }*/

        //Log
        System.out.println();
        System.out.println("Utility Matrix Reader - CSV file was read correctly.");

        //Return the numerical file-read utility matrix.
        return splittedUtilityMatrix;
    }
}
