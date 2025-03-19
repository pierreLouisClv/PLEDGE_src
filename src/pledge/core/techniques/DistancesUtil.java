/*
 * 
 * Author : Christopher Henard (christopher.henard@uni.lu)
 * Date : 01/11/2012
 * Copyright 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
 * All rights reserved
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package pledge.core.techniques;

import java.util.HashSet;
import java.util.Set;
import pledge.core.Product;

/**
 * This class defines some common normalized distances to measure the 
 * distance between two products.
 * 
 * @author Christopher Henard
 */
public class DistancesUtil {

    private static double getSetBasedDistance(Product p1, Product p2, double weight) {
        Set<Integer> intersection = new HashSet<Integer>(p1);
        Set<Integer> union = new HashSet<Integer>(p1);
        intersection.retainAll(p2);
        union.addAll(p2);
        double intersectionSize = intersection.size();
        double unionSize = union.size();

        return 1.0 - (intersectionSize / (intersectionSize + weight * (unionSize - intersectionSize)));
    }

    /**
     * Return the jaccard distance between two products.
     * @param p1 the first product to consider.
     * @param p2 the second product to consider.
     * @return the resulting jaccard distance between p1 and p2
     */
    public static double getJaccardDistance(Product p1, Product p2) {
        return getSetBasedDistance(p1, p2, 1.0);
    }

    /**
     * Return the dice distance between two products.
     * @param p1 the first product to consider.
     * @param p2 the second product to consider.
     * @return the resulting dice distance between p1 and p2
     */
    public static double getDiceDistance(Product p1, Product p2) {
        return getSetBasedDistance(p1, p2, 0.5);
    }

    /**
     * Return the anti dice distance between two products.
     * @param p1 the first product to consider.
     * @param p2 the second product to consider.
     * @return the resulting anti dice distance between p1 and p2
     */
    public static double getAntiDiceDistance(Product p1, Product p2) {
        return getSetBasedDistance(p1, p2, 2.0);
    }

    /**
     * Return the enhanced Jaro-Winkler distance between the products.
     * @return a double representing the distance between two products.
     */
    public static double getEnhancedJaroWinkler(Product p1, Product p2)
    {
        // number of selected features of p1
        int featureSizeP1 = 0;
        // number of selected features of p2
        int featureSizeP2 = 0;
        // number of features that are not selected in both products
        int deselectedFeatures = 0;
        // number of features taht are selected in both products
        int matchingFeatures = 0;
        for (Integer feature : p1)
        {
            // feature > 0 means it is selected
            if (feature > 0)
            {
                featureSizeP1++;
            }
        }
        for (Integer feature : p2)
        {
            if (feature > 0)
            {
                featureSizeP2++;
                if (p1.contains(feature))
                {
                    matchingFeatures++;
                }
            }
            else
            {
                if (p1.contains(feature))
                {
                    deselectedFeatures++;
                }
            }
        }

        double degreeOfDifference = getDegreeOfDifference(featureSizeP1, featureSizeP2);
        double jaroDistance = getJaro(matchingFeatures, deselectedFeatures, featureSizeP1, featureSizeP2, degreeOfDifference);
        return 1.0 - (jaroDistance + (degreeOfDifference * (1.0 - jaroDistance)));
    }

    /**
     * Dice-Jaro-Winkler distance
     * @param p1
     * @param p2
     * @return
     */
    public static double getDiceJaroWinklerDistance(Product p1, Product p2) {
        double alpha = 0.5; // Pondération entre Dice et Jaro-Winkler
    
        // Réutilisation des méthodes existantes
        double diceDistance = getDiceDistance(p1, p2);
        double jaroWinklerDistance = getEnhancedJaroWinkler(p1, p2);
    
        // Combinaison pondérée des distances
        return (alpha * diceDistance) + ((1 - alpha) * jaroWinklerDistance);
    }
    

    /***
     * Jaro distance between two products
     * @param m matching selected features
     * @param n matching not selected features
     * @param T1 product size of first product
     * @param T2 product size of second product
     * @param Df degree of difference between two products
     * @return
     */
    private static double getJaro(int m, int n, int T1, int T2, double Df)
    {
        return (1.0 / 3.0) * ((m / (double) T1) + (m / (double) T2) + ((n - Df) / (double) n));
    }

    /**
     * Degree of difference between two products
     * @param size1
     * @param size2
     * @return
     */
    private static double getDegreeOfDifference(int size1, int size2)
    {
        return ((double) Math.abs(size1 - size2)) / ((double) (size1 + size2));
    }
}
