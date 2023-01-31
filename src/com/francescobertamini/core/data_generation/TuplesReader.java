package com.francescobertamini.core.data_generation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class TuplesReader {

    /**
     * It reads the CSV file containing the tuples and it creates the different variables neede by the program to run.
     *
     * @param fileName the name of the CSV file containing the tuples
     * @return the object array containing the variables
     * @throws IOException
     */

    public static Object[] readTuples(String fileName) throws IOException {
        //The set of all the possible (unique) values for each attribute.
        HashSet<String> attributesValues[] = new HashSet[0];
        //The set of all the attribute/s names.
        String attributesNames[] = new String[1];
        int attributesNumber = 0;
        //The tuples saved as an array of strings.
        ArrayList<String> tuplesLines = new ArrayList<>();
        //The splitted version of the tuples set.
        ArrayList<String[]> splittedTuplesLines = new ArrayList<>();
        //Read the file.
        BufferedReader tuplesReader = new BufferedReader(new FileReader(fileName));
        //All the lines of the tuples CSV file are placed into an ArrayList.
        String tuplesLine = null;
        while ((tuplesLine = tuplesReader.readLine()) != null) {
            tuplesLines.add(tuplesLine);
        }
        tuplesReader.close();
        //Every line is analyzed, to retrieve the name of the attributes and their values.
        for (int i = 0; i < tuplesLines.size(); i++) {
            //The CSV file line is splitted.
            String[] splitted = tuplesLines.get(i).split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
            //Add the splitted line to the splitted version of the tuple.
            splittedTuplesLines.add(splitted);
            //On the first line are placed the names of the attributes.
            if (i == 0) {
                attributesNumber = splitted.length;
                //An array of string HashSets is created. Every HashSet will contain the unique values for a certain attribute.
                attributesValues = new HashSet[attributesNumber];
                for (int w = 0; w < attributesNumber; w++) {
                    attributesValues[w] = new HashSet<String>();
                }
                //The attributes names are placed in a dedicated string array.
                attributesNames = splitted;
                //On the other lines
            } else {
                //Every value is placed in the HashSet corresponding to the attribute it refers to.
                for (int e = 0; e < splitted.length; e++) {
                    attributesValues[e].add(splitted[e]);
                }
            }
        }
        //Log
        System.out.println();
        System.out.println("Tuples CSV file analyzed successful.");

        //Return the object array containing the
        Object[] result = new Object[5];
        result[0] = tuplesLines;
        result[1] = splittedTuplesLines;
        result[2] = attributesNumber;
        result[3] = attributesValues;
        result[4] = attributesNames;

        return result;

    }

}
