package com.francescobertamini.core.um_filling;

import java.io.IOException;
import java.util.ArrayList;

import static com.francescobertamini.core.um_filling.CollaborativeFilter.collEvaluate;
import static com.francescobertamini.core.um_filling.ContentBasedFilter.cBEvaluate;
import static com.francescobertamini.core.utility.FileWriter.writeFile;

public class UMFiller {

    /**
     * Given two k parameters (one for the collaborative filter and one for the content based filter), a normalized
     * utility matrix and a set of queries and tuples, it fills the utility matrix giving to the queries not yet
     * evaluated by a specific user a score guess that is the average of the two guess scores produced by a collaborative
     * and a content based filter.
     *
     * @param k1                  the number of user with the most similar tastes to consider (collaborative filter)
     * @param k2                  the number of the most similar queries already evaluated to consider (content based filter)
     * @param normalizedUM        the normalized utility matrix
     * @param queries             the set of all the queries posted to the system
     * @param attributesNames     names of all the possible attributes
     * @param splittedTuplesLines tuples set in ArrayList of arrays of strings format
     * @param UMColumnsDimension  width dimension of the utility matrix
     * @param UMRowsDimension     height dimension of the utility matrix
     * @return normalized and dense version of the utility matrix
     * @throws IOException
     */
    public static ArrayList<float[]> fillUtilityMatrix(int k1, int k2, ArrayList<float[]> normalizedUM, String queries[],
                                                       String[] attributesNames, ArrayList<String[]> splittedTuplesLines,
                                                       int UMColumnsDimension, int UMRowsDimension) throws IOException {

        //The output normalized and filled utility matrix.
        ArrayList<float[]> filledNormalizedUM = new ArrayList<>();
        //The first line is the same (it contains the query IDs).
        filledNormalizedUM.add(normalizedUM.get(0));
        //Cycling over UM lines.
        for (int i = 1; i < UMRowsDimension; i++) {
            //Creating the filled UM's new line.
            float[] filledUMLine = new float[UMColumnsDimension + 1];
            //Copy the UID (it remains the same).
            filledUMLine[0] = normalizedUM.get(i)[0];
            //Cycling over the query scores in the UM's line.
            for (int j = 1; j < UMColumnsDimension; j++) {
                //If the score for the currently analyzed query (column, j index) has not been assigned yet by the
                // currently analyzed user (line, i index).
                if (normalizedUM.get(i)[j] == -101f) {
                    //Retrieve the currently analyzed UID.
                    int userID = (int) normalizedUM.get(i)[0];
                    //Retrieve the currently analyzed query ID from the UM's first line.
                    int queryID = (int) normalizedUM.get(0)[j];
                    //Retrieve the score guess from the collaborative filter.
                    float collFilterScore = collEvaluate(k1, userID, queryID, normalizedUM);
                    //Retrive the score guess from the content based filter.
                    float cBFilterScore = cBEvaluate(k2, userID, queries, queryID, normalizedUM, attributesNames, splittedTuplesLines);
                    //Calculate the average of the two score guesses, that will be the final score.
                    float finalScore = (collFilterScore + cBFilterScore) / 2;
                    //Log
                    System.out.println();
                    System.out.println("UM Filler - The final score for the query Q" + queryID + " is: " + finalScore + ".");
                    //Assign the final score to currently analyzed query (column) for the currently analyzed user (line).
                    filledUMLine[j] = finalScore;
                } else {
                    //If the currently analyzed query has already been evaluated by the currently analyzed user,
                    // copy the original score.
                    filledUMLine[j] = normalizedUM.get(i)[j];
                }
            }
        }
        //Log
        System.out.println();
        System.out.println("UM Filler - The utility matrix has been filled.");
        //Prepare the printable (array of String) version that will be printed on a CSV file.
        String[] printableUM = preparePrintableFilledUM(UMColumnsDimension, UMRowsDimension, filledNormalizedUM);
        //Call the method to write out the CSV file.
        writeFile("filled_utility_matrix", printableUM);

        //Printing the dense normalized utility matrix.
        /*for(float [] l : filledNormalizedUM) {
            for (float e : l) {
                System.out.print(e +" ");
            }
            System.out.println();
        }*/

        //Return the normalize dense utility matrix.
        return filledNormalizedUM;
    }

    /**
     * Given a utility matrix in its numerical version and its dimensions, it converts the utility matrix to the array
     * of String version, suitable for easy printing on a CSV file.
     *
     * @param UMColumnsDimension width dimension of the utility matrix
     * @param UMRowsDimension    height dimension of the utility matrix
     * @param filledNormalizedUM the utility matrix
     * @return the utility matrix in the printable (array of String) format
     */
    private static String[] preparePrintableFilledUM(int UMColumnsDimension, int UMRowsDimension, ArrayList<float[]> filledNormalizedUM) {
        //The resulting printable UM.
        String[] printableUM = new String[UMRowsDimension + 1];
        //Printable UM's first line.
        String firstLinePrintableUM;
        firstLinePrintableUM = "USER_IDs, Q" + filledNormalizedUM.get(0)[1];
        //Preparing the printable UM's first line.
        for (int i = 2; i < UMColumnsDimension; i++) {
            firstLinePrintableUM += ",Q" + filledNormalizedUM.get(0)[i];
        }
        printableUM[0] = firstLinePrintableUM;
        //Cycling over the numerical UM.
        for (int i = 1; i < UMRowsDimension; i++) {
            //Composing a printable UM's new line.
            String line = new String();
            for (int j = 0; j < UMColumnsDimension + 1; j++) {
                //Adding the UID in U123 format.
                if (j == 0) {
                    line = "U" + filledNormalizedUM.get(i)[0];
                } else {
                    //Adding the query scores.
                    line += "," + filledNormalizedUM.get(i)[j];
                }
            }
        }
        //Return the printable version of the UM.
        return printableUM;
    }
}
