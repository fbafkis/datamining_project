package com.francescobertamini.core.data_generation;

import java.io.IOException;

import static com.francescobertamini.core.utility.FileWriter.writeFile;

public class UsersIDGenerator {

    /**
     * It generates a series of n= vertical utility matrix dimension incremental int IDs that will be the IDs of the users.
     *
     * @param UMRowsDimension height dimension of the utility matrix
     * @return the array of int containing the users' IDs
     */
    public static int[] generateUserIDs(int UMRowsDimension) {
        int userIDs[] = new int[UMRowsDimension];

        //Create the user IDs.
        for (int i = 0; i < UMRowsDimension; i++) {
            userIDs[i] = i + 1;
        }
        //Prepare a string array with the 'U' letter before the ID.
        String userIDsStrings[] = new String[userIDs.length];
        for (int i = 0; i < userIDs.length; i++) {
            userIDsStrings[i] = "U" + Integer.toString(userIDs[i]);
        }

        //Print the User IDs.
        /*for (String s : userIDsStrings) {
            System.out.println(s);
        }*/

        //Log
        System.out.println();
        System.out.println("UID Generator - Users IDs set created.");
        //Call the method to write out the CSV file.
        try {
            writeFile("users_IDs", userIDsStrings);
            //Log
            System.out.println();
            System.out.println("UID Generator - Users IDs CSV file created.");
        } catch (IOException e) {
            System.err.println();
            System.err.println("UID Generator - Error in writing the users IDs CSV file.");
        }
        //Returns the array with the users' IDs.
        return userIDs;
    }
}
