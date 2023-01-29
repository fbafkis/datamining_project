package com.francescobertamini.core.um_filling;
import java.util.ArrayList;

import static com.francescobertamini.core.utility.ExponentialDistributor.getExponentialPercentages;
import static com.francescobertamini.core.utility.QueryResolution.findQueryColumnIndex;
import static com.francescobertamini.core.utility.UsersTastesComparator.findMostSimilarUsers;
public class CollaborativeFilter {
    public static float collEvaluate(int k, int currentUserId, int queryID, int[] userIDs, ArrayList<float[]> normalizedUM) {
        ArrayList<float[]> usersRankingMatrix = findMostSimilarUsers(currentUserId, userIDs, normalizedUM);
        ArrayList<float[]> usersRankingScores = new ArrayList<>();
        int queryColumnIndex = findQueryColumnIndex(queryID, normalizedUM.get(0));
        float score = -101f;
        int usefulUsersCounter = 0;

        for (float[] user : usersRankingMatrix) {
            if (usefulUsersCounter < k) {
                for (float[] line : normalizedUM) {
                    if (line[0] == user[0] && line[queryColumnIndex] != -101f) {
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

        if (usefulUsersCounter == k) {
            System.out.println("The K most similar users with the scores referred to query " + queryID + ":");

            for (float[] line : usersRankingScores) {
                System.out.println("User ID:" + (int) line[0] + " Similarity index: " + line[1] + " Score assigned to the query: " + line[2]);
            }

            int percentages[] = getExponentialPercentages(k);

            float[] weightedScores = new float[k];

            for (int i = 0; i < k; i++) {
                float weightedScore = (percentages[k - 1 - i] / 100f) * (usersRankingScores.get(i)[2]);
                weightedScores[i] = weightedScore;
            }

            for (float ws : weightedScores) {
                score += ws;
            }
            score = score / k;

            System.out.println();
            System.out.println("Final score produced by the collaborative filter for query Q" + queryID + ": " + score);

        } else {
            System.err.println("Not enough suitable users were found. Try with a lower k value.");
        }
        return score;
    }




}
