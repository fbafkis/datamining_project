package com.francescobertamini.core.comparison;

import java.util.ArrayList;
import java.util.Comparator;

public class UsersTastesComparator {

    public static ArrayList<float[]> findMostSimilarUsers(int currentUserId, int[] userIDs, ArrayList<float[]> normalizedUM) {

        ArrayList<float[]> rankingMatrix = new ArrayList<>();
        float[] currentUserScores = new float[normalizedUM.get(0).length];
        for (float[] line : normalizedUM) {
            if ((int) line[0] == currentUserId) {
                currentUserScores = line;
            }
        }

        int matrixLineCounter = 0;
        for (float[] matrixLine : normalizedUM) {

            if (matrixLineCounter != 0) {

                int userID = (int) matrixLine[0];

                if (userID != currentUserId) {

                    float userScore = 0f;
                    int matchingQueries = 0;

                    for (int i = 1; i < matrixLine.length; i++) {
                        if (currentUserScores[i] != -101f && matrixLine[i] != -101f) {
                            userScore += Math.abs(currentUserScores[i] - matrixLine[i]);
                            matchingQueries++;
                        }
                    }

                    userScore = userScore / (float) matchingQueries;


                    float[] rankingMatrixLine = new float[2];

                    rankingMatrixLine[0] = userID;
                    rankingMatrixLine[1] = userScore;

                    rankingMatrix.add(rankingMatrixLine);
                }
            }


            matrixLineCounter++;

        }

        Comparator<float[]> arrayComparator = new Comparator<float[]>() {
            @Override
            public int compare(float[] array1, float[] array2) {
                return Float.compare(array1[1], array2[1]);
            }
        };

        rankingMatrix.sort(arrayComparator);

       /* for (float[] l : rankingMatrix) {
            for (float f : l) {
                System.out.print(f + " ");
            }
            System.out.println();
        }*/

        return rankingMatrix;
    }

}
