package com.francescobertamini.core.data_generation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

import static com.francescobertamini.core.utility.FileWriter.writeFile;
import static com.francescobertamini.core.utility.RandomIndexGenerator.getRandomIndexes;

public class QueryGenerator {

    /**
     * It generates the queries already posted to the system by the users, using only the possible values present into
     * the tuples. The queries are also printed to a CSV file.
     *
     * @param UMColumnsDimension width dimension of the utility matrix
     * @param attributesNumber   number of attributes that compose the tuples
     * @param attributesNames    names of all the possible attributes
     * @param attributesValues   array of HasSet containing all the (unique) possible values present in the tuples for each attribute
     * @return an array of object, with the array of String containing the queries and the array of int containing the generated query IDs.
     */
    public static Object[] generateQueries(int UMColumnsDimension, int attributesNumber, String attributesNames[], HashSet<String> attributesValues[]) {

        //Array containing all the query IDs.
        int queryIDs[] = new int[UMColumnsDimension];
        //Array of String containing the queries.
        String queries[] = new String[UMColumnsDimension];

        //Compose the queries.
        for (int i = 0; i < UMColumnsDimension; i++) {
            //Add the sequential query ID to the query.
            String query = "Q" + Integer.toString(i + 1);
            queryIDs[i] = i + 1;
            //Call the method to create an ArrayList with a random number of random chosen indexes.
            //Create an array with attributes indexes to pass as parameter to the method.
            int attributesIndexes[] = new int[attributesNumber];
            for (int p = 0; p < attributesNumber; p++) {
                attributesIndexes[p] = p;
            }
            //The ArrayList containing the randomly chosen and sorted indexes.
            ArrayList<Integer> orderedAttributesIndexes = getRandomIndexes(attributesNumber, attributesIndexes);
            //Now the values from the ordered attributes' index list are picked.
            for (int u = 0; u < orderedAttributesIndexes.size(); u++) {
                //Convert the HashSet containing the attribute's values to an ArrayList to shuffle it.
                ArrayList<String> attributesValuesArrayList = new ArrayList<>();
                Iterator<String> attributesValuesIterator = attributesValues[orderedAttributesIndexes.get(u)].iterator();
                while (attributesValuesIterator.hasNext()) {
                    attributesValuesArrayList.add(attributesValuesIterator.next());
                }
                //All the values for the actual attribute get shuffled.
                Collections.shuffle(attributesValuesArrayList);
                //It is added to query the current the name of the current attribute and the first value picked from the shuffled list of values.
                query += "," + attributesNames[orderedAttributesIndexes.get(u)] + "=" + attributesValuesArrayList.get(0);
            }
            //The query create just above is added to the query set.
            queries[i] = query;
        }

        //Print the query set
          /*for (int o = 0; o < UMColumnsDimension; o++) {
          System.out.println(queries[o]);
        }*/

        //Log
        System.out.println();
        System.out.println("Query Generator - Queries set created.");
        //Call the method to write out the CSV file.
        try {
            writeFile("queries", queries);
            //Log
            System.out.println();
            System.out.println("Query Generator - Queries CSV file created.");
        } catch (IOException e) {
            System.err.println();
            System.err.println("Query Generator - Error in writing the queries CSV file.");
        }
        //Array of objects containing the two variables created.
        Object[] result = new Object[2];
        result[0] = queries;
        result[1] = queryIDs;
        //Returning the array of objects.
        return result;
    }
}
