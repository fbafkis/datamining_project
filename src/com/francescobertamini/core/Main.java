package com.francescobertamini.core;

import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static void main(String[] args) throws Exception {
        //The dimension of the square utility matrix.
        final int UMDimension = 500;
        //Array of HashSet of strings containing the attributes' unique values.
        HashSet<String> attributesValues[] = new HashSet[0];
        //Array of strings containing the names of the attributes contained in the tuples CSV source file.
        String attributesNames[] = new String[1];
        //The number of the different attributes inside the
        int attributesNumber = 0;
        int queryIDs[] = new int[UMDimension];
        int userIDs[] = new int[UMDimension];
        String queries[] = new String[UMDimension];
        String utilityMatrix[] = new String[UMDimension + 1];

        ///////////////////////////////////////////////////////////////////////////////////

        //Get the attributes and their values from the tuples file.
        try {
            BufferedReader tuplesReader = new BufferedReader(new FileReader("tuples.csv"));
            //All the lines of the tuples CSV file are placed into an ArrayList.
            List<String> tuplesLines = new ArrayList<>();
            String tuplesLine = null;
            while ((tuplesLine = tuplesReader.readLine()) != null) {
                tuplesLines.add(tuplesLine);
            }
            tuplesReader.close();
            //Every line is analyzed, to retrieve the name of the attributes and their values.
            for (int i = 0; i < tuplesLines.size(); i++) {
                //The CSV file line is splitted.
                String[] splitted = tuplesLines.get(i).split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
                //On the first line are placed the names of the attributes.
                if (i == 0) {
                    attributesNumber = splitted.length;
                    //An array of string HashSets is created. Every HashSet will contain the unique values for a certain attribute.
                    attributesValues = new HashSet[attributesNumber];
                    for (int w = 0; w < attributesNumber; w++) {
                        attributesValues[w] = new HashSet<String>();
                    }
                    //The attributes name are placed in a dedicated string array.
                    attributesNames = new String[attributesNumber];
                    attributesNames = splitted;
                    //On the other lines
                } else {
                    //Every value is placed in the HashSet corresponding to the attribute it refers to.
                    for (int e = 0; e < splitted.length; e++) {
                        attributesValues[e].add(splitted[e]);
                    }
                }
            }
            System.out.println("Tuples CSV file analyzed successful.");

            ///////////////////////////////////////////////////////////////////////////////////

            //Create the user IDs.
            for (int i = 0; i < UMDimension; i++) {
                userIDs[i] = i + 1;
            }
            //Prepare a string array with the 'U' letter before the ID.
            String userIDsStrings[] = new String[userIDs.length];
            for (int i = 0; i < userIDs.length; i++) {
                userIDsStrings[i] = "U" + Integer.toString(userIDs[i]);
            }
            //Print the User IDs.
            for (String s : userIDsStrings) {
                //System.out.println(s);
            }
            System.out.println("Users IDs set created.");
            //Call the method to write out the CSV file.
            try {
                writeFile("users_IDs", userIDsStrings);
                System.out.println("Users IDs CSV file created.");
            } catch (IOException e) {
                System.err.println("Error in writing the users IDs CSV file.");
            }

            ///////////////////////////////////////////////////////////////////////////////////

            //Compose the queries.
            for (int i = 0; i < UMDimension; i++) {
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
            for (int o = 0; o < UMDimension; o++) {
                //System.out.println(queries[o]);
            }
            System.out.println("Queries set created.");
            //Call the method to write out the CSV file.
            try {
                writeFile("queries", queries);
                System.out.println("Queries CSV file created.");
            } catch (IOException e) {
                System.err.println("Error in writing the queries CSV file.");
            }
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

    /**
     * @param maxNum The maximum size of the input integer array.
     * @param source The input array containing the indexes to work on.
     * @return The integer ArrayList containing the randomly chosen indexes sorted in increasing order.
     */
    private static ArrayList<Integer> getRandomIndexes(int maxNum, int[] source) {
        //Generate random number of indexes to pick.
        Random random = new Random();
        int numberChoosenToConsider = ThreadLocalRandom.current().nextInt(1, maxNum + 1);
        //Indexes are added to an ArrayList, so that they can be shuffled .
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int e = 0; e < maxNum; e++) {
            indexes.add(source[e]);
        }
        //All the indexes get shuffled.
        Collections.shuffle(indexes);
        ArrayList<Integer> orderedIndexes = new ArrayList<>();
        //Add the N randomly picked indexes to an ArrayList that will get ordered.
        for (int q = 0; q < numberChoosenToConsider; q++) {
            orderedIndexes.add(indexes.get(q));
        }
        //Order the final list of attributes' indexes.
        Collections.sort(orderedIndexes);
        //Return the final ordered ArrayList.
        return orderedIndexes;
    }

    /**
     * @param fileName The name of the file that is going to be created.
     * @param source   The source string array that is going to be printed into the file.
     */
    private static void writeFile(String fileName, String[] source) throws IOException {
        //Create the file.
        File fout = new File(fileName + ".csv");
        try {
            //Create the FOS and the BW.
            FileOutputStream fos = new FileOutputStream(fout);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            //Print the source's lines.
            for (int i = 0; i < source.length; i++) {
                bw.write(source[i]);
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}


