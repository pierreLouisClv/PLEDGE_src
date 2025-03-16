package pledge.core.techniques.generation;

import java.util.List;
import java.util.Random;

import pledge.core.ModelPLEDGE;
import pledge.core.Product;
import pledge.core.techniques.prioritization.PrioritizationTechnique;

public class DraftAlgorithm implements GenerationTechnique {

    public static final String NAME = "Draft PL Algorithm";

    @Override
    public List<Product> generateProducts(
        ModelPLEDGE model, 
        int nbProducts, 
        long timeAllowed,
        PrioritizationTechnique prioritizationTechnique) throws Exception {
        List<Product> prods = model.getUnpredictableProducts(nbProducts * 3);
        Individual indiv = new Individual(model, prods, prioritizationTechnique);
        indiv.fitnessAndOrdering();
        prods = indiv.getProducts().subList(0, nbProducts - 1);
        return prods;    
    }

    @Override
    public String getName() {
        return NAME;
    }
    
}
