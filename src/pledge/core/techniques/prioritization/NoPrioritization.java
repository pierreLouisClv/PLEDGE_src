package pledge.core.techniques.prioritization;

import java.util.ArrayList;
import java.util.List;

import pledge.core.ModelPLEDGE;
import pledge.core.Product;
import pledge.core.techniques.DistancesUtil;

public class NoPrioritization implements PrioritizationTechnique {
    public static final String NAME = "No one";

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
        
        return products;
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
