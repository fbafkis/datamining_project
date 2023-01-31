package com.francescobertamini.core.utility;

import java.util.ArrayList;
import java.util.HashSet;

public class QueryResolution {

    /**
     * It resolves a given query, returning the set of tuples that represents its result.
     *
     * @param query               the given query in string format that has to be solved
     * @param attributesNames     names of all the possible attributes
     * @param splittedTuplesLines tuples set in ArrayList of arrays of strings format
     * @return the HashSet containing the resulting tuples as strings
     */
    public static HashSet<String> getQueryResult(String query, String[] attributesNames, ArrayList<String[]> splittedTuplesLines) {
        //The HashSet (without duplicates, since the same tuple can be matching the query over multiple attributes)
        // contining the resulting set of tuples.
        HashSet<String> result = new HashSet<>();
        //The query is splitted from the CSV-line format.
        String[] queryComponents = query.split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
        //Cycling over the components of the query.
        for (int q = 1; q < queryComponents.length; q++) {
            //The query component is splitted on the = symbol.
            String[] queryTerms = queryComponents[q].split("=");
            int attributeIndex = -1;
            //Search for the name of the attribute specified in the query component into the set of possible attributes
            //names saving its index (position into the tuple in array format).
            for (int a = 0; a < attributesNames.length; a++) {
                if (queryTerms[0].equals(attributesNames[a])) {
                    attributeIndex = a;
                }
            }
            //If the attribute name is found.
            if (attributeIndex != -1) {
                //Cycling over the tuples.
                for (String[] s : splittedTuplesLines) {
                    //If the tuple's attribute value matches the query's one.
                    if (s[attributeIndex].equals(queryTerms[1])) {
                        //A string version of the tuple is created.
                        String stringedTuple = new String();
                        stringedTuple = s[0];
                        for (int i = 1; i < s.length; i++) {
                            stringedTuple += "," + s[i];
                        }
                        //And added to the result set.
                        result.add(stringedTuple);
                    }
                }
            } else {
                //It means that the attribute name is misspelled.
                System.err.println("Query Solver - Error in query syntax.");
            }
        }
        //Log
        //System.out.println();
        //System.out.println("Query Solver - Query solved.");

        //Prints the resulting set of tuples.
        /*System.out.println("Query Solver - Result of size "+ result.size() +":");
        for (String r : result) {
            System.out.println(r);
        }
        System.out.println();
        */

        //Return the array of String format tuples.
        return result;
    }

    /**
     * Given the ID of a query and the utility matrix's first line, it finds the column index of that query.
     *
     * @param queryId     the ID of the query we want to look the column index for
     * @param firstUMLine the first line (containing the query IDs) of the utility matrix
     * @return the column index for the query
     */
    public static int findQueryColumnIndex(int queryId, float[] firstUMLine) {
        int index = -1;
        //Cycling over the only numerical query IDs that are in the UM's first line.
        for (int i = 1; i < firstUMLine.length; i++) {
            if ((int) firstUMLine[i] == queryId) {
                index = i;
            }
        }
        //If the query ID is not found.
        if (index == -1) {
            System.err.println();
            System.err.println("Error in finding query column index.");
        }
        //Return the column index for the given query.
        return index;
    }
}
