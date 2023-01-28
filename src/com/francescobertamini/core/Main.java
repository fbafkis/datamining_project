package com.francescobertamini.core;

import com.francescobertamini.core.utility.QueryResolution;

import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static com.francescobertamini.core.data_generation.TuplesReader.readTuples;
import static com.francescobertamini.core.utility.FileWriter.writeFile;
import static com.francescobertamini.core.utility.QueryResolution.getQueryResult;
import static com.francescobertamini.core.utility.RandomIndexGenerator.getRandomIndexes;

public class Main {
    public static void main(String[] args) throws Exception {
        //The dimension of the square utility matrix.
        final int UMRowsDimension = 500;
        final int UMColumnsDimension = 500;

        //Array of HashSet of strings containing the attributes' unique values.
        HashSet<String> attributesValues[];
        //Array of strings containing the names of the attributes contained in the tuples CSV source file.
        String attributesNames[];
        //The number of the different attributes inside the
        int attributesNumber;
        int queryIDs[] = new int[UMColumnsDimension];
        int userIDs[] = new int[UMRowsDimension];

        ArrayList<String[]> splittedTuplesLines;

        String queries[] = new String[UMColumnsDimension];
        String utilityMatrix[] = new String[UMColumnsDimension + 1];

        ///////////////////////////////////////////////////////////////////////////////////

        //Get the attributes and their values from the tuples file.
        try {

            //Call the method that read the tuples from CSV file.
            Object[] TRintialitedVariables = readTuples();

            splittedTuplesLines = (ArrayList<String[]>) TRintialitedVariables[1];
            attributesNumber = (int) TRintialitedVariables[2];
            attributesValues = (HashSet<String>[]) TRintialitedVariables[3];
            attributesNames = (String[]) TRintialitedVariables[4];


            //Prova con il metodo che da i risultati
            getQueryResult("Q25,name=Mary,lastname=Brown,city=Trento,height=164", attributesNames, splittedTuplesLines);
            getQueryResult("Q30,sex=female", attributesNames, splittedTuplesLines);


            ///////////////////////////////////////////////////////////////////////////////////



            ///////////////////////////////////////////////////////////////////////////////////


            ///////////////////////////////////////////////////////////////////////////////////

            //Compose the utility matrix
            //Prepare the first row containing all the query IDs.
            String umFirstLine = "USER_IDs, Q" + Integer.toString(queryIDs[0]);
            for (int t = 1; t < queryIDs.length; t++) {
                umFirstLine += ",Q" + Integer.toString(queryIDs[t]);
            }
            utilityMatrix[0] = umFirstLine;
            //Prepare all the lines containing the scores referred to the queries for each user.
            for (int u = 0; u < userIDs.length; u++) {
                String umLine = "U" + Integer.toString(userIDs[u]);

                ArrayList<Integer> orderedChoosenQueriesIDs = getRandomIndexes(queryIDs.length, queryIDs);
                for (int q = 0; q < queryIDs.length; q++) {
                    if (orderedChoosenQueriesIDs.contains(queryIDs[q])) {
                        Random random = new Random();
                        int randomScore = ThreadLocalRandom.current().nextInt(1, 100 + 1);
                        umLine += "," + Integer.toString(randomScore);

                    } else {
                        umLine += ",,";
                    }
                }
                utilityMatrix[u + 1] = umLine;
            }
            //Print the utility matrix.
            for (String s : utilityMatrix) {
                //System.out.println(s);
            }
            //Print the utility matrix file
            try {
                writeFile("utility_matrix", utilityMatrix);
                System.out.println("Utility matrix CSV file created.");
            } catch (IOException e) {
                System.err.println("Error in writing the utility matrix CSV file.");
            }
            //Closing the tuples file reading try-catch block.
        } catch (IOException e) {
            System.err.println("It was not possible to open the tuples CSV file.");
        }
    }


}


