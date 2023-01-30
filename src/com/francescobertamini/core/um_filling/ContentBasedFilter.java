package com.francescobertamini.core.um_filling;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

import static com.francescobertamini.core.utility.ExponentialDistributor.getExponentialPercentages;
import static com.francescobertamini.core.utility.QueryResolution.findQueryColumnIndex;
import static com.francescobertamini.core.utility.QueryResolution.getQueryResult;
import static com.francescobertamini.core.utility.JaccardComparator.*;
import static com.francescobertamini.core.utility.RankingArrayCompartor.customArrayComparator;

public class ContentBasedFilter {

    /**
     * Given a query ID, a reference user ID and the normalized utility matrix, it produces a score guess for the query
     * that is not yet evaluated by the reference user. It is a content based filter, since it consider the scores of
     * k most similar queries respect to the given query that have already evaluated by the reference user,giving the
     * highest weight to the score of the most similar query, following an exponential distribution for the weights. The
     * similarities between queries is calculated using the Jaccard similarity over their result (the set of tuples).
     *
     * @param k                   the number of the most similar queries already evaluated to consider
     * @param currentUserId       the UID of the reference user
     * @param queries             the set of all the queries posted to the system
     * @param queryID             the ID of the query to evaluate (guess the score)
     * @param normalizedUM        the normalized utility matrix
     * @param attributesNames     names of all the possible attributes
     * @param splittedTuplesLines tuples set in ArrayList of arrays of strings format
     * @return the float representing the guess of the score produced by the content based filter
     */
    public static float cBEvaluate(int k, int currentUserId, String queries[], int queryID, ArrayList<float[]> normalizedUM,
                                   String[] attributesNames, ArrayList<String[]> splittedTuplesLines) {
        //The 'Q' char before the query IDs is removed.
        String[] cleanedQueries = removeQChar(queries);
        //The current query in string format.
        String query = null;
        //Query score's guess is initialized to the default (absent) value.
        float score = -101f;
        //Finding the current query.
        for (String q : cleanedQueries) {
            //Removing the first part of the query, the UID.
            String splitted[] = q.split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
            splitted[0] = splitted[0].substring(1);
            int id = Integer.parseInt(splitted[0]);
            if (id == queryID) {
                query = q;
            }
        }
        //If the reference query has been found.
        if (query != null) {
            //Retrieving the result of the reference query (the set of tuples).
            HashSet<String> queryResult = getQueryResult(query, attributesNames, splittedTuplesLines);
            //The ranking matrix containing queries' similarity scores.
            ArrayList<float[]> querySimilarityRanking = new ArrayList<>();
            //Cycling over the queries.
            for (String q : queries) {
                //Retrieving the result of the currently analyzed query (the set of tuples).
                HashSet<String> currentQueryResult = getQueryResult(q, attributesNames, splittedTuplesLines);
                //Calculating the Jaccard similarity between the rerference query and the currently analyzed.
                float similarityScore = getJaccardScore(queryResult, currentQueryResult);
                //Split the currently analyzed query to get its ID.
                String splitted[] = q.split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
                splitted[0] = splitted[0].substring(1);
                int id = Integer.parseInt(splitted[0]);
                //Compose the queries similarity scores' matrix's line.
                float[] QSMLine = new float[2];
                QSMLine[0] = id;
                QSMLine[1] = similarityScore;
                //Add the matrix's line.
                querySimilarityRanking.add(QSMLine);
            }
            //Sorting the ranking matrix using the custom comparator in the reversed way (higher ranking
            // at the beginning of the array).
            querySimilarityRanking.sort(customArrayComparator.reversed());
            //Log
            System.out.println();
            System.out.println("The similarity ranking matrix for query Q" + queryID + ":");

            //Printing the queries' similarity score ranking matrix.
            /*for (float[] l : querySimilarityRanking) {
                System.out.println(l[0] + " " + l[1]);
            }*/

            //The UM's line of the reference user.
            float[] userUMLine = null;
            //Retrieving the UM's line containing the scores of the reference user.
            for (int i = 1; i < normalizedUM.size(); i++) {
                if (normalizedUM.get(i)[0] == currentUserId) {
                    userUMLine = normalizedUM.get(i);
                }
            }
            //If the reference users' line is not found inside the UM.
            if (userUMLine == null) {
                System.err.println();
                System.err.println("Error in finding the user line into utility matrix.");
                System.exit(1);
            }
            //The counter of the valid queries found.
            int usefulQueriesCounter = 0;
            //The array containing the scores assigned by the reference user to the most similar queries.
            ArrayList<float[]> queryRankingScores = new ArrayList<>();
            //Cycling order the most similar queries.
            for (float[] l : querySimilarityRanking) {
                //Stop when k valid queries are found.
                if (usefulQueriesCounter < k) {
                    //Retrieving the currently analyzed query's ID.
                    int index = findQueryColumnIndex((int) l[0], normalizedUM.get(0));
                    //If the reference user has already evaluated the currently analyzed query.
                    if (userUMLine[index] != -101f) {
                        //Add the query to the queries' score matrix.
                        float[] QRSLine = new float[3];
                        QRSLine[0] = l[0];
                        QRSLine[1] = l[1];
                        QRSLine[2] = userUMLine[index];
                        queryRankingScores.add(QRSLine);
                        usefulQueriesCounter++;
                    }
                }
            }
            //If k valid queries have been found successfully.
            if (usefulQueriesCounter == k) {
                //Log
                System.out.println();
                System.out.println("CB Filter - The K most similar queries with the scores referred to query " + queryID + ":");

                //Printing queries' IDs, similarity scores and queries scores.
                /*for (float[] line : queryRankingScores) {
                    System.out.println("Query ID:" + (int) line[0] + " Similarity index: " + line[1] + " Score assigned to the query: " + line[2]);
                }*/

                //Calculating the k percentages distributed over the exponential distrobution.
                int percentages[] = getExponentialPercentages(k);
                //The array containing the weighted query scores of the k users.
                float[] weightedScores = new float[k];
                //Calculating the weighted query scores of the k users.
                for (int i = 0; i < k; i++) {
                    float weightedScore = (percentages[k - 1 - i] / 100f) * (queryRankingScores.get(i)[2]);
                    weightedScores[i] = weightedScore;
                }
                //Calculating the final guess for the query score.
                for (float ws : weightedScores) {
                    score += ws;
                }
                score = score / k;
                //Log
                System.out.println();
                System.out.println("CB Filter - Final score produced by the content based filter for query Q" + queryID + ": " + score);
            } else {
                //If it is impossible to find k valid queries.
                System.err.println();
                System.err.println("CB Filter - Not enough suitable queries were found. Try with a lower k value.");
                score = 0f;
            }
        } else {
            //If the reference query is not found.
            System.err.println();
            System.err.println("CB Filter - Error in retrieving the current query.");
        }
        return score;
    }

    /**
     * Given the array of queries in string format, it removes the 'Q' char at the beginning of the ID of every query.
     * @param queries
     * @return
     */
    private static String[] removeQChar(String[] queries) {
        //Cycling over the query set.
        for (String q : queries) {
            //Removing the first character 'Q'.
            q = q.substring(1);
        }
        //Return the cleaned query set.
        return queries;
    }
}
