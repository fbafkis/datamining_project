package com.francescobertamini.core;

import com.francescobertamini.core.data_generation.UsersIDGenerator;
import com.francescobertamini.core.utility.QueryResolution;

import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static com.francescobertamini.core.comparison.UsersTastesComparator.findMostSimilarUsers;
import static com.francescobertamini.core.data_generation.QueryGenerator.generateQueries;
import static com.francescobertamini.core.data_generation.TuplesReader.readTuples;
import static com.francescobertamini.core.data_generation.UMGenerator.generateUM;
import static com.francescobertamini.core.data_generation.UsersIDGenerator.generateUserIDs;
import static com.francescobertamini.core.utility.DataNormalizer.normalizeUM;
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
        int queryIDs[];
        int userIDs[];

        ArrayList<String[]> splittedTuplesLines;

        String queries[] = new String[UMColumnsDimension];
        String utilityMatrix[] = new String[UMColumnsDimension + 1];

        ArrayList<int[]> splittedUM = new ArrayList<>();
        ArrayList<float[]> normalizedUM = new ArrayList<>();

        ///////////////////////////////////////////////////////////////////////////////////

        //Get the attributes and their values from the tuples file.
        try {

            //Call the method that read the tuples from CSV file.
            Object[] TRintializedVariables = readTuples();

            splittedTuplesLines = (ArrayList<String[]>) TRintializedVariables[1];
            attributesNumber = (int) TRintializedVariables[2];
            attributesValues = (HashSet<String>[]) TRintializedVariables[3];
            attributesNames = (String[]) TRintializedVariables[4];


            //Prova con il metodo che da i risultati
            getQueryResult("Q25,name=Mary,lastname=Brown,city=Trento,height=164", attributesNames, splittedTuplesLines);
            getQueryResult("Q30,sex=female", attributesNames, splittedTuplesLines);

            ///////////////////////////////////////////////////////////////////////////////////

            Object[] QGinitializedVariables = generateQueries(UMColumnsDimension, attributesNumber, attributesNames, attributesValues);

            queries = (String[]) QGinitializedVariables[0];
            queryIDs = (int[]) QGinitializedVariables[1];

            ///////////////////////////////////////////////////////////////////////////////////

            userIDs = generateUserIDs(UMRowsDimension);

            ///////////////////////////////////////////////////////////////////////////////////

            Object[] GUMinitializedVariables = generateUM(queryIDs, UMColumnsDimension, userIDs);
            utilityMatrix = (String[]) GUMinitializedVariables[0];
            splittedUM = (ArrayList<int[]>) GUMinitializedVariables[1];

            ///////////////////////////////////////////////////////////////////////////////////

            normalizedUM = normalizeUM(splittedUM);

            ///////////////////////////////////////////////////////////////////////////////////



            //Closing the tuples file reading try-catch block.
        } catch (IOException e) {
            System.err.println("It was not possible to open the tuples CSV file.");
        }
    }


}


