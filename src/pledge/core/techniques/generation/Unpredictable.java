package pledge.core.techniques.generation;

import java.util.List;
import java.util.Random;

import pledge.core.ModelPLEDGE;
import pledge.core.Product;
import pledge.core.techniques.prioritization.PrioritizationTechnique;

public class Unpredictable implements GenerationTechnique  {
    public static final String NAME = "Unpredictable";
    private static final Random random = new Random();

    @Override
    public List<Product> generateProducts(
        ModelPLEDGE model, 
        int nbProducts, 
        long timeAllowed, 
        PrioritizationTechnique prioritizationTechnique) 
        throws Exception 
    {
        Individual indiv = new Individual(
            model, 
            model.getUnpredictableProducts(nbProducts * 100), 
            prioritizationTechnique);
        indiv.fitnessAndOrdering();
        return indiv.getProducts().subList(0, nbProducts - 1);
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
