package com.francescobertamini.core.utility;

public class ExponentialDistributor {

    /**
     * Given a parameter k, it returns an arry of integer containing the k percentages resulting from dividing
     * 100% out of k parts increasing following the exponential distribution.
     *
     * @param k the parameter that indicates in how many parts 100% has to be divided
     * @return the array of int containing the percentages
     */
    public static int[] getExponentialPercentages(int k) {
        //The parameter b of the formula.
        float b = (float) Math.pow(100f, (1.0 / (float) (k - 1)));
        //The array containing the percentages.
        int percentages[] = new int[k];
        //Calculating f(x) - f(x-1) to calculate the value of each part x.
        for (int t = 0; t < k; t++) {
            float percentage = (float) ((Math.pow(b, t)) - (Math.pow(b, t - 1)));
            percentages[t] = Math.round(percentage);
        }
        //Log
        System.out.println();
        System.out.println("Exp Distributor - Percentages exponentially distributed with value k=" + k + ":");
        for (int e : percentages) {
            System.out.println(e + "%");
        }

        //Returning the array containing the percentages.
        return percentages;
    }
}
