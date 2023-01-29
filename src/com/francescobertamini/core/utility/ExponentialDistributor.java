package com.francescobertamini.core.utility;

public class ExponentialDistributor {

    public static int[] getExponentialPercentages(int k) {

        float b = (float) Math.pow(100f, (1.0 / (float) (k - 1)));

        int percentages[] = new int[k];

        for (int t = 0; t < k; t++) {
            float percentage = (float) ((Math.pow(b, t)) - (Math.pow(b, t - 1)));
            percentages[t] = Math.round(percentage);
        }

        System.out.println();
        System.out.println("Percentages exponentially distributed with value k=" + k + ":");
        for (int e : percentages) {
            System.out.println(e + "%");
        }

        return percentages;
    }

}
