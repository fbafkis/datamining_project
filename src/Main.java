
import com.sun.tools.jconsole.JConsoleContext;

import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static void main(String[] args) throws Exception {

        final int UMDimension = 500;
        ArrayList attributesValues[] = new ArrayList[0];
        String attributesNames[] = new String[1];
        int attributesNumber = 0;
        int queryIDs[] = new int[UMDimension];
        int userIDs[] = new int[UMDimension];
        String queries[] = new String[UMDimension];
        String utilityMatrix[] = new String[UMDimension + 1];

        //Create the user IDs.
        for (int i = 0; i < UMDimension; i++) {
            userIDs[i] = i + 1;
        }

        //TODO: print the csv file with user IDs.

        //Get the attributes and their values from the tuples file.
        BufferedReader tuplesReader = new BufferedReader(new FileReader("C:\\Users\\fb\\Desktop\\tuples.csv"));
        List<String> tuplesLines = new ArrayList<>();
        String line = null;
        while ((line = tuplesReader.readLine()) != null) {
            tuplesLines.add(line);
        }
        tuplesReader.close();
        for (int i = 0; i < tuplesLines.size(); i++) {
            String[] splitted = tuplesLines.get(i).split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
            if (i == 0) {
                attributesNumber = splitted.length;
                System.out.println(attributesNumber);
                attributesValues = new ArrayList[attributesNumber];
                for (int w = 0; w < attributesNumber; w++) {
                    attributesValues[w] = new ArrayList<String>();
                }
                attributesNames = new String[attributesNumber];
                attributesNames = splitted;
            } else {
                for (int e = 0; e < splitted.length; e++) {
                    attributesValues[e].add(splitted[e]);
                }
            }
        }

        //Compose the queries.
        for (int i = 0; i < UMDimension; i++) {
            //Add the sequential query ID to the query.
            String query = "Q" + Integer.toString(i + 1);
            queryIDs[i] = i + 1;
            //Call the method to create an ArrayList with a random number of random choosen indexes.
            //Create an array with attributes indexes to pass as parameter to the method.
            int attributesIndexes[] = new int[attributesNumber];
            for (int p = 0; p < attributesNumber; p++) {
                attributesIndexes[p] = p;
            }
            //The ArrayList containing the randomly choosen and sorted indexes.
            ArrayList<Integer> orderedAttributesIndexes = getRandomIndexes(attributesNumber, attributesIndexes);

            //Now the values from the ordered attributes' index list are picked.
            for (int u = 0; u < orderedAttributesIndexes.size(); u++) {
                //All the values for the actual attribute get shuffled.
                Collections.shuffle(attributesValues[orderedAttributesIndexes.get(u)]);
                //It is added to query the current the name of the current attribute and the first value picked from the shuffled list of values.
                query += "," + attributesNames[orderedAttributesIndexes.get(u)] + "=" + attributesValues[orderedAttributesIndexes.get(u)].get(0);
            }
            //The query create just above is added to the query set.
            queries[i] = query;
        }
        //Print the query set
        for (int o = 0; o < UMDimension; o++) {
            System.out.println(queries[o]);
        }
        //TODO: export csv file containing the queries

        //Compose the Utility Matrix
        //Prepare the first row containing all the query IDs.
        String umFirstLine = "USER_IDs, Q" + Integer.toString(queryIDs[0]);
        for (int t = 1; t < queryIDs.length; t++) {
            umFirstLine += ",Q" + Integer.toString(queryIDs[t]);
        }
        utilityMatrix[0] = umFirstLine;
        System.out.println(utilityMatrix[0]);
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
            utilityMatrix[u+1] = umLine;
        }

        for (int v = 0; v < utilityMatrix.length; v++) {
          //  System.out.println(utilityMatrix[v]);
        }

        //TODO: Write out the utility matrix CSV file.
    }

    private static ArrayList<Integer> getRandomIndexes(int maxNum, int[] source) {

        Random random = new Random();
        int numberChoosenToConsider = ThreadLocalRandom.current().nextInt(1, maxNum + 1);
        //Attributes' indexes are added to an ArrayList, so that they can be shuffled .
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int e = 0; e < maxNum; e++) {
            indexes.add(source[e]);
        }
        //All the attributes' indexes get shuffled.
        Collections.shuffle(indexes);
        ArrayList<Integer> orderedIndexes = new ArrayList<>();
        //Add the N randomly picked attributes' indexes to an ArrayList that will get ordered.
        for (int q = 0; q < numberChoosenToConsider; q++) {
            orderedIndexes.add(indexes.get(q));
        }
        //Order the final list of attributes' indexes.
        Collections.sort(orderedIndexes);

        return orderedIndexes;
    }


}


