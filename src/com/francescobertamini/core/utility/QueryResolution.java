package com.francescobertamini.core.utility;

import java.util.ArrayList;
import java.util.HashSet;

public class QueryResolution {

    public static HashSet<String[]> getQueryResult(String query, String[] attributesNames, ArrayList<String[]> splittedTuplesLines) {

        HashSet<String[]> result = new HashSet<>();
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
                        result.add(s);
                    }
                }
            } else {
                System.err.println("Error in query syntax.");
            }
        }

        System.out.println("Result:" + result.size());
       for (String[] r : result) {
           System.out.println();
            for (String t : r) {
                System.out.print(t + " ");
            }
        }
       System.out.println();

        return result;

    }

}
