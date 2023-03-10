package com.francescobertamini.core.utility;

import java.io.*;

public class FileWriter {

    /**
     * Given an array of strings representing the file's lines, it writes the csv file with the specified name.
     *
     * @param fileName the name of the file that is going to be created
     * @param source   the source string array that is going to be printed into the file
     */
    public static void writeFile(String fileName, String[] source) throws IOException {
        //Create the file.
        File fout = new File(fileName + ".csv");
        try {
            //Create the FOS and the BW.
            FileOutputStream fos = new FileOutputStream(fout);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            //Print the source's lines.
            for (int i = 0; i < source.length; i++) {
                bw.write(source[i]);
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
