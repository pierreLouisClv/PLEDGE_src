package pledge.core.techniques.prioritization;

import java.util.ArrayList;
import java.util.List;

import pledge.core.ModelPLEDGE;
import pledge.core.Product;
import pledge.core.techniques.DistancesUtil;


/**
 * Dice-Jaro-Winkler Local Maximum Distance Enhancement prioritization technique coming from Sulaiman et al.'s work : A Dissimilarity with Dice-Jaro-Winkler Test Case Prioritization Approach for Model Based Testing in Software Product Line  
 */
public class SimilarityDJW implements PrioritizationTechnique {

public static final String NAME = "DJW";

private double fitnessSum;

@Override
public List<Product> prioritize(ModelPLEDGE model, List<Product> products) throws Exception {
    int size = products.size();
    List<Product> prioritizedProducts = new ArrayList<>();
    List<Product> productsCopy = new ArrayList<>(products);
    
    double[][] distancesMatrix = new double[size][size];
    fitnessSum = 0.0; // Réinitialisation de la fitness sum

    // Calcul des distances entre produits avec Dice-Jaro-Winkler
    for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
            if (j > i) {
                distancesMatrix[i][j] = DistancesUtil.getDiceJaroWinklerDistance(products.get(i), products.get(j));
                distancesMatrix[j][i] = distancesMatrix[i][j]; // Matrice symétrique
            } else if (i == j) {
                distancesMatrix[i][j] = 0.0;
            }
        }
    }

    // Sélection initiale : choisir les deux produits les plus dissemblables
    int firstIndex = 0, secondIndex = 0;
    double maxDist = -1;

    for (int i = 0; i < size; i++) {
        for (int j = i + 1; j < size; j++) {
            if (distancesMatrix[i][j] > maxDist) {
                maxDist = distancesMatrix[i][j];
                firstIndex = i;
                secondIndex = j;
            }
        }
    }

    // Ajout des deux premiers produits
    prioritizedProducts.add(productsCopy.get(firstIndex));
    prioritizedProducts.add(productsCopy.get(secondIndex));
    fitnessSum += distancesMatrix[firstIndex][secondIndex]; // Mise à jour de la fitness sum

    productsCopy.remove(products.get(firstIndex));
    productsCopy.remove(products.get(secondIndex));

    // Sélection des produits restants en maximisant la dissimilarité avec le dernier ajouté
    while (!productsCopy.isEmpty()) {
        double maxDissimilarity = -1;
        int bestIndex = -1;

        Product lastAdded = prioritizedProducts.get(prioritizedProducts.size() - 1);

        for (int i = 0; i < productsCopy.size(); i++) {
            double dissimilarity = DistancesUtil.getDiceJaroWinklerDistance(lastAdded, productsCopy.get(i));

            if (dissimilarity > maxDissimilarity) {
                maxDissimilarity = dissimilarity;
                bestIndex = i;
            }
        }

        // bestIndex = -1 dans de rares cas pour Drupal
        Product bestProduct = productsCopy.get(bestIndex);
        prioritizedProducts.add(bestProduct);
        fitnessSum += maxDissimilarity; // Mise à jour de la fitness sum

        productsCopy.remove(bestProduct);
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
