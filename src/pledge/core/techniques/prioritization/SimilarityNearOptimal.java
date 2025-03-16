/*
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
package pledge.core.techniques.prioritization;

import java.util.ArrayList;
import java.util.List;
import pledge.core.ModelPLEDGE;
import pledge.core.Product;
import pledge.core.techniques.DistancesUtil;

/**
 *
 * @author Christopher Henard
 */
public class SimilarityNearOptimal implements PrioritizationTechnique {

    public static final String NAME = "NO"; 

    private double fitnessSum;

    /**
     * Returns the prioritized list of products.
     * @param model the model of the application.
     * @param products the list of products to prioritize.
     * @return the prioritized list of products.
     * @throws Exception if an error occurs during the prioritization.
     */
    @Override
    public List<Product> prioritize(ModelPLEDGE model, List<Product> products) throws Exception {
        int size = products.size();
        List<Product> prioritizedProducts = new ArrayList<Product>(size);
        List<Product> productsCopy = new ArrayList<Product>(products);
        double[][] distancesMatrix = new double[size][size];
        fitnessSum = 0;

        //model.setCurrentAction("Computing the distances...");
        // Computation of the distances
        double total = products.size() * (products.size() - 1) / 2;
        double done = 0;
        for (int i = 0; i < distancesMatrix.length; i++) {
            for (int j = 0; j < distancesMatrix.length; j++) {
                distancesMatrix[i][j] = -1;
                if (j > i) {
                    double dist;
                    if (model.getDistanceTechnique() == ModelPLEDGE.JACCARD)
                    {
                        dist = DistancesUtil.getJaccardDistance(productsCopy.get(i), productsCopy.get(j));
                    }
                    else
                    {
                        dist = DistancesUtil.getEnhancedJaroWinkler(productsCopy.get(i), productsCopy.get(j));
                    }
                    distancesMatrix[i][j] = dist;
                    fitnessSum += dist;
                    done++;
                    //model.setProgress((int) (done / total * 100.0));
                }

            }

        }


        //model.setCurrentAction("Ordering the products...");
        //model.setProgress(0);
        List<Integer> possibleIndices = new ArrayList<Integer>();
        List<Integer> doneIndices = new ArrayList<Integer>();
        for (int i = 0; i < size; i++) {
            possibleIndices.add(i);

        }
        double maxDistance = -1;
        int toAddIIndex = -1;
        int toAddJIndex = -1;
        for (int i = 0; i < distancesMatrix.length; i++) {
            for (int j = 0; j < distancesMatrix.length; j++) {
                if (j > i) {
                    if (distancesMatrix[i][j] > maxDistance) {
                        maxDistance = distancesMatrix[i][j];
                        toAddIIndex = i;
                        toAddJIndex = j;
                    }
                }
            }
        }
        Product pi = products.get(toAddIIndex);
        Product pj = products.get(toAddJIndex);

        prioritizedProducts.add(pi);
        prioritizedProducts.add(pj);
        productsCopy.remove(pi);
        productsCopy.remove(pj);
        possibleIndices.remove((Integer) toAddIIndex);
        possibleIndices.remove((Integer) toAddJIndex);
        doneIndices.add(toAddIIndex);
        doneIndices.add(toAddJIndex);


        while (!productsCopy.isEmpty()) {

            if (possibleIndices.size() > 1) {
                double maxDist = -1;
                int toAdd = -1;
                for (Integer i : possibleIndices) {

                    double distance = 0;
                    for (Integer j : doneIndices) {
                        distance += (j > i) ? distancesMatrix[i][j] : distancesMatrix[j][i];
                    }
                    if (distance > maxDist) {
                        maxDist = distance;
                        toAdd = i;
                    }
                }
                Product p = products.get(toAdd);

                prioritizedProducts.add(p);
                productsCopy.remove(p);
                possibleIndices.remove((Integer) toAdd);
                doneIndices.add(toAdd);

            } else {
                prioritizedProducts.add(products.get(possibleIndices.get(0)));
                productsCopy.clear();
            }
            //model.setProgress((int) ((double) prioritizedProducts.size() / products.size() * 100.0));
        }

        return prioritizedProducts;
    }

    /**
     * Return the name of this technique.
     * @return the name of this technique.
     */
    @Override
    public String getName() {
        return NAME;
    }

    /**
     * Return the fitness sum associated to the products.
     * @return a double representing the fitness sum associated to the products.
     */
    @Override
    public double getFitnessSum() {
        return fitnessSum;
    }
}