package com.francescobertamini.core;

import java.io.*;
import java.util.*;

import projPack.MatrixReader;
import projPack.SVDAlgorithm;
import projPack.UMUtil;

import static com.francescobertamini.core.data_generation.QueryGenerator.generateQueries;
import static com.francescobertamini.core.data_generation.TuplesReader.readTuples;
import static com.francescobertamini.core.data_generation.UMGenerator.generateUM;
import static com.francescobertamini.core.data_generation.UsersIDGenerator.generateUserIDs;
import static com.francescobertamini.core.um_filling.UMFiller.fillUtilityMatrix;
import static com.francescobertamini.core.utility.DataNormalizer.normalizeUM;

public class Main {
    public static void main(String[] args) throws Exception {
    	
		long timeBefore, timeAfter;
		
		timeBefore = System.nanoTime();
		//Reads the utility matrix
		MatrixReader reader = new MatrixReader("utility_matrix.csv");	
	
		//Normalizes the data for the utility matrix
		UMUtil UMUtil = new UMUtil(reader.getUMAsDouble(), reader.getSize());		
		UMUtil.normalizeData();

		SVDAlgorithm svd = new SVDAlgorithm(UMUtil.getUM(), reader.getSize());
		svd.sparseSVD();
		timeAfter = System.nanoTime();
		System.out.println("SVD took: " + (timeAfter-timeBefore) + " ns");
    	
        //The k parameter for the collaborative filter.
        int k1 = 4;
        //The k parameter for the content based filter.
        int k2 = 5;
        //The dimensions of the square utility matrix.
        final int UMRowsDimension = 500;
        final int UMColumnsDimension = 500;
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
        String queries[];
        //The numerical version of the original utility matrix.
        ArrayList<int[]> splittedUM;
        //The numerical and normalized version of the original utility matrix.
        ArrayList<float[]> normalizedUM;
        //The dense version of the numerical and normalized utility matrix.
        ArrayList<float[]> denseUM;

        //Get the attributes and their values from the tuples file.
        //TODO: remove because of tuple reading from memory.
        try {
            //TODO: change because of tuple reading from memory.
            //Call the method that read the tuples from CSV file.
            Object[] TRintializedVariables = readTuples();

            splittedTuplesLines = (ArrayList<String[]>) TRintializedVariables[1];
            attributesNumber = (int) TRintializedVariables[2];
            attributesValues = (HashSet<String>[]) TRintializedVariables[3];
            attributesNames = (String[]) TRintializedVariables[4];

            //Generate the queries.
            Object[] QGinitializedVariables = generateQueries(UMColumnsDimension, attributesNumber, attributesNames, attributesValues);
            queries = (String[]) QGinitializedVariables[0];
            queryIDs = (int[]) QGinitializedVariables[1];
            //Generate the users IDs.
            userIDs = generateUserIDs(UMRowsDimension);
            //Generate the utility matrix.
            splittedUM = generateUM(queryIDs, UMColumnsDimension, userIDs);
            //Normalize the utility matrix.
            normalizedUM = normalizeUM(splittedUM);
            //Fill the sparse utility matrix.
            denseUM = fillUtilityMatrix(k1, k2, normalizedUM, queries, attributesNames,
                    splittedTuplesLines, UMColumnsDimension, UMRowsDimension);
            //Closing the tuples file reading try-catch block.
        } catch (IOException e) {
            //TODO: remove because of tuple reading from memory.
            System.err.println();
            System.err.println("It was not possible to open the tuples CSV file.");
        }
    }
}


