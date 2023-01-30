package com.francescobertamini.core.um_filling;

import java.util.ArrayList;
import static com.francescobertamini.core.utility.ExponentialDistributor.getExponentialPercentages;
import static com.francescobertamini.core.utility.QueryResolution.findQueryColumnIndex;
import static com.francescobertamini.core.utility.UsersTastesComparator.findMostSimilarUsers;
public class CollaborativeFilter {

    /**
     * Given a query ID, a reference user ID and the normalized utility matrix, it produces a score guess for the query
     * that is not yet evaluated by the reference user. It is a collaborative filter, since it consider the scores of
     * k users that have the most similar tastes respect to the reference user and that have already evaluated the query,
     * giving the highest weight to the score of the most similar user, following an exponential distribution for
     * the weights.
     *
     * @param k             the number of user with the most similar tastes to consider
     * @param currentUserId the UID of the reference user
     * @param queryID       the ID of the query to evaluate (guess the score)
     * @param normalizedUM  the normalized utility matrix
     * @return the float representing the guess of the score produced by the collaborative filter
     */
    public static float collEvaluate(int k, int currentUserId, int queryID, ArrayList<float[]> normalizedUM) {
        //The ranking matrix for the users' tastes similarity respect to the reference user is produced.
        ArrayList<float[]> usersRankingMatrix = findMostSimilarUsers(currentUserId, normalizedUM);
        //The array containing the scores assigned by the users to the query.
        ArrayList<float[]> usersRankingScores = new ArrayList<>();
        //Retrieve the query column index inside the UM.
        int queryColumnIndex = findQueryColumnIndex(queryID, normalizedUM.get(0));
        //Initialize the score to the default (absent) value.
        float score = -101f;
        int usefulUsersCounter = 0;
        //Cylce over the users' tastes similarity ranking matrix.
        for (float[] user : usersRankingMatrix) {
            //Stop when k valid users are found.
            if (usefulUsersCounter < k) {
                //Cycling over UM's lines.
                for (float[] line : normalizedUM) {
                    //If the current user in the tastes' similarity matrix already evaluated the query.
                    if (line[0] == user[0] && line[queryColumnIndex] != -101f) {
                        //Add its UID, its tastes' similarity score and its query score to the line of the scores' matrix.
                        float[] URSLine = new float[3];
                        URSLine[0] = user[0];
                        URSLine[1] = user[1];
                        URSLine[2] = line[queryColumnIndex];
                        usersRankingScores.add(URSLine);
                        usefulUsersCounter++;
                    }
                }
            }
        }
        //If k valid users have been found successfully.
        if (usefulUsersCounter == k) {
            //Log
            System.out.println();
            System.out.println("Coll Filter - The K most similar users with the scores referred to query " + queryID + ":");

            //Printing users' UIDs, similarity scores and query scores.
            /*for (float[] line : usersRankingScores) {
                System.out.println("User ID:" + (int) line[0] + " Similarity index: " + line[1] + " Score assigned to the query: " + line[2]);
            }*/

            //Calculating the k percentages distributed over the exponential distrobution.
            int percentages[] = getExponentialPercentages(k);
            //The array containing the weighted query scores of the k users.
            float[] weightedScores = new float[k];
            //Calculating the weighted query scores of the k users.
            for (int i = 0; i < k; i++) {
                float weightedScore = (percentages[k - 1 - i] / 100f) * (usersRankingScores.get(i)[2]);
                weightedScores[i] = weightedScore;
            }
            //Calculating the final guess for the query score.
            for (float ws : weightedScores) {
                score += ws;
            }
            score = score / k;
            //Log
            System.out.println();
            System.out.println("Coll Filter - Final score produced by the collaborative filter for query Q" + queryID + ": " + score);

        } else {
            //If it is impossible to find k valid users.
            System.err.println();
            System.err.println("Coll Filter - Not enough suitable users were found. Try with a lower k value. " +
                    "The value from the collaborative filter will not be considered.");
            score = -101f;
        }
        //Returning the collaborative filter final guess for the query's score.
        return score;
    }
}
