package pledge.core.techniques.prioritization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pledge.core.ModelPLEDGE;
import pledge.core.Product;
import pledge.core.techniques.DistancesUtil;

public class SimilarityNS implements PrioritizationTechnique {
    
    public static final String NAME = "Novelty Score";

    private double fitnessSum;


       /**
     * Return the name of this technique.
     * @return the name of this technique.
     */
    @Override
    public String getName() {
        return NAME;
    }

    // @Override
    // public List<Product> prioritize(ModelPLEDGE model, List<Product> products) throws Exception {
    //     int k = products.size(); // Paramètre k pour le calcul du Novelty Score
    //     int size = products.size();
    //     List<Product> prioritizedProducts = new ArrayList<>(size);
    //     List<Product> productsCopy = new ArrayList<>(products);
    //     double[][] distancesMatrix = new double[size][size];
    //     double[] noveltyScores = new double[size];

    //     // Calcul des distances entre tous les produits
    //     for (int i = 0; i < size; i++) {
    //         for (int j = i + 1; j < size; j++) {
    //             double dist = DistancesUtil.getAntiDiceDistance(productsCopy.get(i), productsCopy.get(j));
    //             distancesMatrix[i][j] = dist;
    //             distancesMatrix[j][i] = dist;
    //         }
    //     }

    //     // Calcul des Novelty Scores
    //     for (int i = 0; i < size; i++) {
    //         double[] sortedDistances = Arrays.copyOf(distancesMatrix[i], size);
    //         Arrays.sort(sortedDistances);
    //         double noveltyScore = 0;
    //         for (int j = 0; j < k; j++) {
    //             noveltyScore += sortedDistances[j];
    //         }
    //         noveltyScores[i] = noveltyScore / k; // Moyenne des k plus proches voisins
    //     }

    //     // Priorisation basée sur le Novelty Score (ordre décroissant)
    //     while (!productsCopy.isEmpty()) {
    //         // Trouver le produit avec le Novelty Score maximum
    //         int maxIndex = -1;
    //         double maxScore = Double.NEGATIVE_INFINITY;
    //         for (int i = 0; i < productsCopy.size(); i++) {
    //             if (noveltyScores[i] > maxScore) {
    //                 maxScore = noveltyScores[i];
    //                 maxIndex = i;
    //             }
    //         }

    //         // Ajouter à la liste priorisée
    //         if (maxIndex != -1) {
    //             prioritizedProducts.add(productsCopy.get(maxIndex));
    //             productsCopy.remove(maxIndex);
    //         }
    //     }

    //     return prioritizedProducts;
    // }

    @Override
public List<Product> prioritize(ModelPLEDGE model, List<Product> products) throws Exception {
    int k = products.size(); // Paramètre k pour le calcul du Novelty Score
    int size = products.size();
    List<Product> prioritizedProducts = new ArrayList<>(size);
    // List<Product> productsCopy = new ArrayList<>(products);
    double[][] distancesMatrix = new double[size][size];
    double[] noveltyScores = new double[size];
    
    for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
            if (j > i) {
                distancesMatrix[i][j] = DistancesUtil.getAntiDiceDistance(products.get(i), products.get(j));
                distancesMatrix[j][i] = distancesMatrix[i][j];
            } else if (i == j) {
                distancesMatrix[i][j] = 0.0;
            }
        }
    }

    // Calcul des Novelty Scores
    for (int i = 0; i < size; i++) {
        double[] sortedDistances = Arrays.copyOf(distancesMatrix[i], size);
        Arrays.sort(sortedDistances);
        double noveltyScore = 0;
        for (int j = 0; j < k; j++) {
            noveltyScore += sortedDistances[j];
        }
        noveltyScores[i] = noveltyScore / k; // Moyenne des k plus proches voisins
    }
    
    // Mise à jour de la fitness sum : somme des Novelty Scores
    fitnessSum = 0.0;
    for (double ns : noveltyScores) {
        fitnessSum += ns;
    }

    // Priorisation : on trie les produits selon leur Novelty Score (ordre décroissant)
    List<Integer> indices = new ArrayList<>();
    for (int i = 0; i < size; i++) indices.add(i);
    
    indices.sort((i, j) -> Double.compare(noveltyScores[j], noveltyScores[i])); // Tri décroissant

    // Ajout des produits triés dans la liste finale
    for (int index : indices) {
        prioritizedProducts.add(products.get(index));
    }

    return prioritizedProducts;
}


    @Override
    public double getFitnessSum() {
        return fitnessSum;
    }

}
