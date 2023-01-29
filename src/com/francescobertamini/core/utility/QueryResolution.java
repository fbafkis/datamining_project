package com.francescobertamini.core.utility;

import java.util.ArrayList;
import java.util.HashSet;

public class QueryResolution {

    public static HashSet<String> getQueryResult(String query, String[] attributesNames, ArrayList<String[]> splittedTuplesLines) {

        HashSet<String> result = new HashSet<>();
        String[] queryComponents = query.split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");

        for (int q = 1; q < queryComponents.length; q++) {

            String[] queryTerms = queryComponents[q].split("=");

            int attributeIndex = -1;

            for (int a = 0; a < attributesNames.length; a++) {

                if (queryTerms[0].equals(attributesNames[a])) {
                    attributeIndex = a;
                }
            }

            if (attributeIndex != -1) {
                for (String[] s : splittedTuplesLines) {
                    if (s[attributeIndex].equals(queryTerms[1])) {
                        String stringedTupled = new String();
                        stringedTupled = s[0];
                        for (int i = 1; i < s.length; i++) {
                            stringedTupled += "," + s[i];
                        }
                        result.add(stringedTupled);
                    }
                }
            } else {
                System.err.println("Error in query syntax.");
            }
        }

        System.out.println("Result of size "+ result.size() +":");
        for (String r : result) {
            System.out.println(r);
        }
        System.out.println();

        return result;

    }

    public static int findQueryColumnIndex(int queryId, float[] firstUMLine) {
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


}
