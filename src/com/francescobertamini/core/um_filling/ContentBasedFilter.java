package com.francescobertamini.core.um_filling;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

import static com.francescobertamini.core.utility.ExponentialDistributor.getExponentialPercentages;
import static com.francescobertamini.core.utility.QueryResolution.findQueryColumnIndex;
import static com.francescobertamini.core.utility.QueryResolution.getQueryResult;
import static com.francescobertamini.core.utility.JaccardComparator.*;

public class ContentBasedFilter {

    public static float cBEvaluate(int k, int currentUserId, String queries[], int queryID, ArrayList<float[]> normalizedUM,
                                 String[] attributesNames, ArrayList<String[]> splittedTuplesLines) {

        String[] cleanedQueries = removeQChar(queries);
        String query = null;

        for (String q : cleanedQueries) {
            String splitted[] = q.split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
            splitted[0] = splitted[0].substring(1);
            int id = Integer.parseInt(splitted[0]);
            if (id == queryID) {
                query = q;
            }
        }
        if (query != null) {
            HashSet<String> queryResult = getQueryResult(query, attributesNames, splittedTuplesLines);
            ArrayList<float[]> querySimilarityRanking = new ArrayList<>();
            float score = -101f;

            for (String q : queries) {
                HashSet<String> currentQueryResult = getQueryResult(q, attributesNames, splittedTuplesLines);
                float similarityScore = getJaccardScore(queryResult, currentQueryResult);
                String splitted[] = q.split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
                splitted[0] = splitted[0].substring(1);
                int id = Integer.parseInt(splitted[0]);
                float[] QSMLine = new float[2];
                QSMLine[0] = id;
                QSMLine[1] = similarityScore;
                querySimilarityRanking.add(QSMLine);
            }

            Comparator<float[]> arrayComparator = new Comparator<float[]>() {
                @Override
                public int compare(float[] array1, float[] array2) {
                    return Float.compare(array1[1], array2[1]);
                }
            };

            querySimilarityRanking.sort(arrayComparator);

            System.out.println();
            System.out.println("The similarity ranking matrix for query Q" + queryID + ":");
            for (float[] l : querySimilarityRanking) {
                //System.out.println(l[0] + " " + l[1]);
            }

            float[] userUMLine = null;

            for (int i = 1; i < normalizedUM.size(); i++) {
                if (normalizedUM.get(i)[0] == currentUserId) {
                    userUMLine = normalizedUM.get(i);
                }
            }

            if (userUMLine == null) {
                System.err.println("Error in finding the user line into utility matrix.");
                System.exit(1);
            }

            int usefulQueriesCounter = 0;
            ArrayList<float[]> queryRankingScores = new ArrayList<>();
            for (float[] l : querySimilarityRanking) {
                if (usefulQueriesCounter < k) {
                    int index = findQueryColumnIndex((int) l[0], normalizedUM.get(0));
                    if (userUMLine[index] != -101f) {
                        float[] QRSLine = new float[3];
                        QRSLine[0] = l[0];
                        QRSLine[1] = l[1];
                        QRSLine[2] = userUMLine[index];
                        queryRankingScores.add(QRSLine);
                        usefulQueriesCounter++;
                    }
                }
            }
            if (usefulQueriesCounter == k ) {
                System.out.println("CB Filter - The K most similar queries with the scores referred to query " + queryID + ":");
                for (float[] line : queryRankingScores) {
                    System.out.println("Query ID:" + (int) line[0] + " Similarity index: " + line[1] + " Score assigned to the query: " + line[2]);
                }
                int percentages[] = getExponentialPercentages(k);

                float[] weightedScores = new float[k];

                for (int i = 0; i < k; i++) {
                    float weightedScore = (percentages[k - 1 - i] / 100f) * (queryRankingScores.get(i)[2]);
                    weightedScores[i] = weightedScore;
                }

                for (float ws : weightedScores) {
                    score += ws;
                }
                score = score / k;

                System.out.println();
                System.out.println("CB Filter - Final score produced by the content based filter for query Q" + queryID + ": " + score);

            } else {
                System.err.println("CB Filter - Not enough suitable queries were found. Try with a lower k value.");
            }
        } else {
            System.err.println("CB Filter - Error in retrieving the current query.");
        }
        return 0;
    }

    private static String[] removeQChar(String[] queries) {

        for (String q : queries) {
            q = q.substring(1);
        }
        for (String q : queries) {
            System.out.println(q);
        }
        return queries;
    }


}
