package com.francescobertamini.core.data_generation;

import java.io.IOException;

import static com.francescobertamini.core.utility.FileWriter.writeFile;

public class UsersIDGenerator {

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
        for (String s : userIDsStrings) {
            //System.out.println(s);
        }
        System.out.println("Users IDs set created.");
        //Call the method to write out the CSV file.
        try {
            writeFile("users_IDs", userIDsStrings);
            System.out.println("Users IDs CSV file created.");
        } catch (IOException e) {
            System.err.println("Error in writing the users IDs CSV file.");
        }

        return userIDs;

    }

}
