package com.francescobertamini.core;

import java.awt.desktop.SystemSleepEvent;
import java.io.*;
import java.util.*;

import static com.francescobertamini.core.data_generation.QueriesReader.readQueries;
import static com.francescobertamini.core.data_generation.QueryGenerator.generateQueries;
import static com.francescobertamini.core.data_generation.TuplesReader.readTuples;
import static com.francescobertamini.core.data_generation.UMGenerator.generateUM;
import static com.francescobertamini.core.data_generation.UMReader.readUM;
import static com.francescobertamini.core.data_generation.UsersIDGenerator.generateUserIDs;
import static com.francescobertamini.core.um_filling.UMFiller.fillUtilityMatrix;
import static com.francescobertamini.core.utility.DataNormalizer.normalizeUM;

public class Main {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        //User-modifiable variables.
        //////////////////////////////////////////////////////////////////////////////
        //The mode ("g" for generating queries and utility matrix, "r" for read from file.
        char mode = 'g';
        //The input CSV file names:
        String tuplesFileName = "tuples.csv";
        String queriesFileName = "queries.csv";
        String UMFileName = "utility_matrix.csv";
        //The k parameter for the collaborative filter.
        int k1 = 2;
        //The k parameter for the content based filter.
        int k2 = 2;
        //The dimensions of the square utility matrix.
        final int UMRowsDimension = 50;
        final int UMColumnsDimension = 50;

        // End of user-modifiable variables.
        //////////////////////////////////////////////////////////////////////////////
        //Array of HashSet of strings containing the attributes' unique values.
        HashSet<String> attributesValues[];
        //Array of strings containing the names of the attributes contained in the tuples CSV source file.
        String attributesNames[];
        //The number of the different attributes inside the tuples set.
        int attributesNumber;
        //The array containing all the query IDs.
        int queryIDs[];
        //The array containing all the user IDs.
        int userIDs[];
        //The divided in string array version of the tuples set.
        ArrayList<String[]> splittedTuplesLines;
        //The set of all the queries already posted to the system.
        String queries[] = new String[0];
        //The numerical version of the original utility matrix.
        ArrayList<int[]> splittedUM = null;
        //The numerical and normalized version of the original utility matrix.
        ArrayList<float[]> normalizedUM = new ArrayList<>();
        //The dense version of the numerical and normalized utility matrix.
        ArrayList<float[]> denseUM;

        //Get the attributes and their values from the tuples file.
        try {
            //Call the method that read the tuples from CSV file.
            Object[] TRintializedVariables = readTuples(tuplesFileName);

            splittedTuplesLines = (ArrayList<String[]>) TRintializedVariables[1];
            attributesNumber = (int) TRintializedVariables[2];
            attributesValues = (HashSet<String>[]) TRintializedVariables[3];
            attributesNames = (String[]) TRintializedVariables[4];


            if (mode == 'g') {

                //Generating the query set and the utility matrix.
                //Generate the queries.
                Object[] qGinitializedVariables = generateQueries(UMColumnsDimension, attributesNumber, attributesNames, attributesValues);
                queries = (String[]) qGinitializedVariables[0];
                queryIDs = (int[]) qGinitializedVariables[1];
                //Generate the users IDs.
                userIDs = generateUserIDs(UMRowsDimension);
                //Generate the utility matrix.
                splittedUM = generateUM(queryIDs, UMColumnsDimension, userIDs);

                System.exit(0);

            } else if (mode == 'r') {
                //Using already existing query set and utility matrix from file.

                //Read UM CSV file
                splittedUM = readUM(UMFileName);
                //Read the queries file
                queries = readQueries(queriesFileName);


            } else {
                System.err.println();
                System.err.println("Choose a valid mode for the execution.");
                System.exit(1);
            }

            //Keeping trace of the time.
            long timeBefore, timeAfter;
            timeBefore = System.nanoTime();

            //Normalize the utility matrix.
            normalizedUM = normalizeUM(splittedUM);
            System.out.println();
            System.out.println("Running the filling algorithm...");
            //Fill the sparse utility matrix.
            denseUM = fillUtilityMatrix(k1, k2, normalizedUM, queries, attributesNames,
                    splittedTuplesLines, UMColumnsDimension, UMRowsDimension);

            //Print the time it took.
            timeAfter = System.nanoTime();
            System.out.println();
            System.out.println("The hybrid raccomandation system took: " + (timeAfter - timeBefore) / 1000000000 + " seconds (" + (timeAfter - timeBefore) + "  nanonseconds) with k1=" + k1 + " and k2=" + k2 + ".");

            //Closing the tuples file reading try-catch block.
        } catch (IOException e) {
            System.err.println();
            System.err.println("It was not possible to open the some CSV file.");
            System.exit(1);
        }
    }
}


