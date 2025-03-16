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
package pledge.core.techniques.generation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.sat4j.specs.TimeoutException;

import pledge.core.ModelPLEDGE;
import pledge.core.Product;
import pledge.core.TSet;
import pledge.core.techniques.prioritization.PrioritizationTechnique;

/**
 *
 * @author Christopher Henard
 * 
 * This class represents a search-based approach to generate products.
 */
public class EvolutionaryAlgorithm1Plus1 implements GenerationTechnique {

    public static final String NAME = "Evolutionary Algorithm";
    private static final Random random = new Random();

    @Override
    public List<Product> generateProducts(
        ModelPLEDGE model, 
        int nbProducts, 
        long timeAllowed, 
        PrioritizationTechnique prioritizationTechnique) 
        throws Exception 
    {
        List<Product> products = model.getStartingTestSuite(); 
        long startTimeMS = System.currentTimeMillis();
        Individual indiv = new Individual(
            model, 
            // model.getUnpredictableProducts(nbProducts),
            products, 
            prioritizationTechnique);
        indiv.fitnessAndOrdering();
        int nbIter = 0;
        // model.saveProducts(indiv.getProducts(), false);

        // model.setCurrentAction("First twise coverage computation");
        // model.initTWiseCoverage(3, indiv.getProducts());

        while (System.currentTimeMillis() - startTimeMS < timeAllowed) {
            model.setCurrentAction("Iteration number " + (nbIter + 1));
            Individual newIndiv = new Individual(model, indiv, prioritizationTechnique);
            newIndiv.mutate(Individual.MUTATE_WORST, model);
            newIndiv.fitnessAndOrdering();
            if (newIndiv.getFitness() > indiv.getFitness()) {
                indiv = newIndiv;
            }

            nbIter++;
            model.setProgress((int) ((System.currentTimeMillis() - startTimeMS) / (double) timeAllowed * 100.0));
        }

        // model.setCurrentAction("Final twise coverage computation");
        // model.exportTWiseCoverage(indiv.getProducts());
        // model.saveProducts(indiv.getProducts(), true);
        model.saveProductsExp2(indiv.getProducts());
        return indiv.getProducts();
    }

    /**
     * Returns the name of this technique.
     * @return a String representing the name of this technique.
     */
    @Override
    public String getName() {
        return NAME;
    }

}
