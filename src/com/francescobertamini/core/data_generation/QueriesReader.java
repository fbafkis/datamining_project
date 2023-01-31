package com.francescobertamini.core.data_generation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class QueriesReader {

    /**
     * Reads the queries from an input CSV file.
     *
     * @param fileName the name of the CSV file containing the utility matrix
     * @return the String array containing the queries
     * @throws IOException
     */
    public static String[] readQueries (String fileName) throws IOException {

            BufferedReader queriesReader = new BufferedReader(new FileReader(fileName));
            //All the lines of the queries CSV file are placed into an ArrayList.
            ArrayList<String> queriesAR = new ArrayList<>();
            String query = null;
              try {
            while ((query = queriesReader.readLine()) != null) {
                queriesAR.add(query);
            }
            queriesReader.close();
              } catch (IOException e){
                  System.err.println();
                  System.err.println("Queries Reader - Unable to open the specified file \"" + fileName + "\".");
                  System.exit(1);
              }

        String [] queries = new String[queriesAR.size()];

        for (int i = 0; i < queries.length; i++) {
            queries[i] = queriesAR.get(i);
        }

        //Log
        System.out.println();
        System.out.println("Utility Matrix Reader - CSV file was read correctly.");

        return queries;
    }

}
