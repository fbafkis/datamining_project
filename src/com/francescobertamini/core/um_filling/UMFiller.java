package com.francescobertamini.core.um_filling;

import java.io.IOException;
import java.util.ArrayList;

import static com.francescobertamini.core.um_filling.CollaborativeFilter.collEvaluate;
import static com.francescobertamini.core.um_filling.ContentBasedFilter.cBEvaluate;
import static com.francescobertamini.core.utility.FileWriter.writeFile;

public class UMFiller {
    public static ArrayList<float[]> fillUtilityMatrix(int k1, int k2, int[] userIDs, ArrayList<float[]> normalizedUM, String queries[],
                                                       String[] attributesNames, ArrayList<String[]> splittedTuplesLines,
                                                       int UMColumnsDimension, int UMRowsDimension) throws IOException {

        ArrayList<float[]> filledNormalizedUM = new ArrayList<>();
        filledNormalizedUM.add(normalizedUM.get(0));

        for (int i = 1; i < UMRowsDimension; i++) {

            float[] filledUMLine = new float[UMColumnsDimension + 1];
            filledUMLine[0] = normalizedUM.get(i)[0];
            for (int j = 1; j < UMColumnsDimension; j++) {
                if (normalizedUM.get(i)[j] == -101f) {
                    int userID = (int) normalizedUM.get(i)[0];
                    int queryID = (int) normalizedUM.get(0)[j];

                    float collFilterScore = collEvaluate(k1, userID, queryID, userIDs, normalizedUM);
                    float cBFilterScore = cBEvaluate(k2, userID, queries, queryID, normalizedUM, attributesNames, splittedTuplesLines);

                    float finalScore = (collFilterScore + cBFilterScore) / 2;

                    System.out.println();
                    System.out.println("UM Filler - The final score for the query Q" + queryID + " is: " + finalScore + ".");
                    filledUMLine[j] = finalScore;
                } else {
                    filledUMLine[j] = normalizedUM.get(i)[j];
                }
            }
        }

        ////// Print the csv file

        String[] printableUM = preparePrintableFilledUM(UMColumnsDimension, UMRowsDimension, filledNormalizedUM);
        writeFile("filled_utility_matrix", printableUM);

        ArrayList<float[]> result = filledNormalizedUM;

        return result;
    }


    private static String[] preparePrintableFilledUM(int UMColumnsDimension, int UMRowsDimension, ArrayList<float[]> filledNormalizedUM) {

        String[] printableUM = new String[UMRowsDimension + 1];
        String firstLinePrintableUM;
        firstLinePrintableUM = "USER_IDs, Q" + filledNormalizedUM.get(0)[1];

        for (int i = 2; i < UMColumnsDimension; i++) {
            firstLinePrintableUM += ",Q" + filledNormalizedUM.get(0)[i];
        }
        printableUM[0] = firstLinePrintableUM;

        for (int i = 1; i < UMRowsDimension; i++) {
            String line = new String();
            for (int j = 0; j < UMColumnsDimension + 1; j++) {
                if (j == 0) {
                    line = "U" + filledNormalizedUM.get(i)[0];
                } else {
                    line += "," + filledNormalizedUM.get(i)[j];
                }
            }
        }
        return printableUM;
    }

}
