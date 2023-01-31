package com.francescobertamini.core.utility;

import java.util.ArrayList;
import java.util.Comparator;

import static com.francescobertamini.core.utility.RankingArrayCompartor.customArrayComparator;

public class UsersTastesComparator {

    /**
     * Given a current user ID that acts as point of reference and the normalized utility matrix, it produces the
     * ranking of the users with the most similar tastes to the reference user. It calculates a score basing on the
     * absolute distances between the scores assigned to the queries evaluated by both the reference user and the
     * one being currently analyzed (the lower the most similar tastes).
     *
     * @param currentUserId the UID of the reference user
     * @param normalizedUM  the normalized utility matrix
     * @return the ArrayList containing on each line the UID of the compared user and its similarity of tastes' score,
     * in float format.
     */
    public static ArrayList<float[]> findMostSimilarUsers(int currentUserId, ArrayList<float[]> normalizedUM) {
        //The resulting ranking matrix.
        ArrayList<float[]> rankingMatrix = new ArrayList<>();
        //The array containing the reference user's scores.
        float[] currentUserScores = new float[normalizedUM.get(0).length];
        //Cycling over the lines of the normalized UM.
        for (float[] line : normalizedUM) {
            //Search for reference user's scores.
            if ((int) line[0] == currentUserId) {
                currentUserScores = line;
            }
        }
        int matrixLineCounter = 0;
        for (float[] matrixLine : normalizedUM) {
            //Skips the UM's first line containing the query IDs.
            if (matrixLineCounter >0) {
                int userID = (int) matrixLine[0];
                //Skipping the reference user.
                if (userID != currentUserId) {
                    float userScore = 0f;
                    int matchingQueries = 0;
                    //Cycling over the user's scores.
                    //System.err.println(normalizedUM.get(0).length);
                    for (int i = 1; i < matrixLine.length; i++) {
                        //Consider the score only if evaluated by both the reference user and the current user.
                        if (currentUserScores[i] != -101f && matrixLine[i] != -101f) {
                            //Calculating the pure sum of distances.
                            userScore += Math.abs(currentUserScores[i] - matrixLine[i]);
                            matchingQueries++;
                        }
                    }
                    //Calculating the final similarity of tastes' score considering the number of matching queries
                    //(the most query matching the lower score)
                    userScore = userScore / (float) matchingQueries;
                    //Composing the result ranking matrix line.
                    float[] rankingMatrixLine = new float[2];
                    rankingMatrixLine[0] = userID;
                    rankingMatrixLine[1] = userScore;
                    //Adding the line to the ranking matrix.
                    rankingMatrix.add(rankingMatrixLine);
                }
            }
            matrixLineCounter++;
        }
        //Sorting the ranking matrix using the custom comparator (basing on ranking).
        rankingMatrix.sort(customArrayComparator);

        //Log
        //System.out.println();
        //System.out.println("User tastes' comparator - Similarity ranking matrix produced for user U" + currentUserId + ".");

        //Printing the ranking matrix.
       /* for (float[] l : rankingMatrix) {
            for (float f : l) {
                System.out.print(f + " ");
            }
            System.out.println();
        }*/

        //Returning the ranking matrix.
        return rankingMatrix;
    }
}
