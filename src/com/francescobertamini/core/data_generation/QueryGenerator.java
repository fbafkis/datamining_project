package com.francescobertamini.core.data_generation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

import static com.francescobertamini.core.utility.FileWriter.writeFile;
import static com.francescobertamini.core.utility.RandomIndexGenerator.getRandomIndexes;

public class QueryGenerator {

    public static void generateQueries (int UMColumnsDimension,int [] queryIDs, int attributesNumber, String attributesNames[], HashSet<String> attributesValues[]){

        String queries[] = new String[UMColumnsDimension];
        String utilityMatrix[] = new String[UMColumnsDimension + 1];

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
        //for (int o = 0; o < UMColumnsDimension; o++) {
        //System.out.println(queries[o]);
        //}
        System.out.println("Queries set created.");
        //Call the method to write out the CSV file.
        try {
            writeFile("queries", queries);
            System.out.println("Queries CSV file created.");
        } catch (IOException e) {
            System.err.println("Error in writing the queries CSV file.");
        }
    }

}
