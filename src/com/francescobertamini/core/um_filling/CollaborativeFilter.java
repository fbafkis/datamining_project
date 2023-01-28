package com.francescobertamini.core.um_filling;

import com.francescobertamini.core.Main;

import java.util.ArrayList;

import static com.francescobertamini.core.comparison.UsersTastesComparator.findMostSimilarUsers;

public class CollaborativeFilter {

    public int evaluate(int k, int currentUserId, int queryID, int[] userIDs, ArrayList<float[]> normalizedUM) {

        ArrayList<float[]> usersRankingMatrix = findMostSimilarUsers(currentUserId, userIDs, normalizedUM);





        return 0;
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
