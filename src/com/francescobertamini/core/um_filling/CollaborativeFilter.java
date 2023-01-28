package com.francescobertamini.core.um_filling;

import com.francescobertamini.core.Main;

import java.util.ArrayList;

import static com.francescobertamini.core.comparison.UsersTastesComparator.findMostSimilarUsers;

public class CollaborativeFilter {

    public static int evaluate(int k, int currentUserId, int queryID, int[] userIDs, ArrayList<float[]> normalizedUM) {

        ArrayList<float[]> usersRankingMatrix = findMostSimilarUsers(currentUserId, userIDs, normalizedUM);

        ArrayList<float[]> usersRankingScores = new ArrayList<>();

        int queryColumnIndex = findQueryColumnIndex(queryID, normalizedUM.get(0));

        int usefulUsersCounter = 0;

        for (float[] user : usersRankingMatrix) {
            if (usefulUsersCounter < k) {
                for (float[] line : normalizedUM) {
                    if (line[0] == user[0] && line[queryColumnIndex] != -101f) {
                        float[] URMLine = new float[3];
                        URMLine[0] = user[0];
                        URMLine[1] = user[1];
                        URMLine[2] = line[queryColumnIndex];
                        usersRankingScores.add(URMLine);
                        usefulUsersCounter++;
                    }
                }
            }
        }

        System.out.println("The K most similar users with the scores referred to query " + queryID + ":");

        for (float[] line : usersRankingScores) {
            System.out.println("User ID:" + (int) line[0] + " Similarity index: " + line[1] + " Score assigned to the query: " + line[2]);
        }



        return 0;
    }

    private static int findQueryColumnIndex(int queryId, float[] firstUMLine) {
        int index = -1;
        for (int i = 1; i < firstUMLine.length; i++) {
            if ((int) firstUMLine[i] == queryId) {
                index = i;
            }
        }
        if (index == -1) {
            System.err.println("Error in finding query column index.");
        }
        return index;
    }

    private int[] getExponentialPercentages(int k) {

        float b = (float) Math.pow(100f, (1.0 / (float) (k - 1)));

        int percentages[] = new int[k];

        for (int t = 0; t < k; t++) {
            float percentage = (float) ((Math.pow(b, t)) - (Math.pow(b, t - 1)));
            // System.out.println(percentage);
            percentages[t] = Math.round(percentage);
        }

        for (int e : percentages) {
            System.out.println(e);
        }

        return percentages;
    }


}
