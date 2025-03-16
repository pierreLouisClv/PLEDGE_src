package pledge.core.techniques.generation.NoveltyScore;

import java.util.ArrayList;
import java.util.List;

import pledge.core.ModelPLEDGE;
import pledge.core.Product;
import pledge.core.techniques.DistancesUtil;
import pledge.core.techniques.generation.GenerationTechnique;
import pledge.core.techniques.prioritization.PrioritizationTechnique;
import java.util.Random;

public class NoveltyScoreAlgorithm implements GenerationTechnique{

    public static final String NAME = "Novelty Score Algorithm";

    private Random random = new Random();

    @Override
    public List<Product> generateProducts(ModelPLEDGE model, int nbProducts, long timeAllowed,
            PrioritizationTechnique prioritizationTechnique) throws Exception {
        long startTimeMS = System.currentTimeMillis();
        //Init the archive by generating N configs
        NSIndividual indiv = new NSIndividual(
                model, 
                model.getUnpredictableProducts(nbProducts));
        ProbSATSolver probSATSolver = new ProbSATSolver(model);
        int nbIter = 0;     
        while (System.currentTimeMillis() - startTimeMS < timeAllowed) {
            model.setCurrentAction("Iteration number " + (nbIter + 1));
            Binary[] offspringsBin = indiv.generateOffspringChilds();
            for (Binary bin : offspringsBin)
            {
                Binary repairedBin = probSATSolver.execute(bin);

                indiv.updateProducts(binToProd(repairedBin));
            }
            nbIter++;
            model.setProgress((int) ((System.currentTimeMillis() - startTimeMS) / (double) timeAllowed * 100.0));
        }
       
        return indiv.products;
    }


    private Product binToProd(Binary bin)
    {
        Product product = new Product();
        
        for (int i = 0; i < bin.getNumberOfBits();i++) {
        	
        	if (bin.getIth(i) == true){
        		product.add(i + 1);
        	} else {
        		product.add(-(i + 1));
        	}
        		
        } // for i
        return product;
    }

    @Override
    public String getName() {
        return NAME;
    }

    class NSIndividual
    {
        // Parameters
        private int k_; 	
        			// k_ is Nb in the paper
        private double theta_; 	

        boolean isMatrixInit;

        double[][] distancesMatrix;

        Random random = new Random();

        ModelPLEDGE fm;

        List<Product> products;

        double[] noveltyScores;

        public NSIndividual(ModelPLEDGE fm, List<Product> products)
        {
            this.fm = fm;
            this.products = products;
            this.k_ = products.size(); // Param _k as the product size
            this.theta_ = 0;
            distancesMatrix = provideDistanceMatrice(products.size());
            noveltyScores = provideNoveltyScores(products.size(), distancesMatrix);
        }

        public Binary[] generateOffspringChilds()
        {
            Product [] parents = selectParents();
            // On créé des enfants (proba = 1): chaque enfant aura pour chaque val, la valeur d'un des deux
            return binaryCrossover(parents[0], parents[1]);
        }

        private boolean updateProducts(Product product) 
        {
            // Pas d'ajout si produit existe déjà 
            // for (Product prod : products) {
            //     boolean isDifferent = false;
            //     int featureIndex = 0;
            //     while (!isDifferent)
            //     {
            //         if product.
            //     }
            //     for (int i = 0; i < product.size(); i++)
            //     {
                    
            //     }
            // }
            if (products.contains(product)) {
                return false;
            }
            
            // // Si liste incomplète on ajoute direct
            // if (products.size() < archiveSize_) {
                
            //     products.add(product);
            //     return true;
                
            // } else { // archive.size() == archiveSize_
            // Initialisation de la matrice des distances
            int nbProducts = products.size(); 		            
                
            distancesMatrix[nbProducts][nbProducts] = 0.0;
            // Distance du nouveau produit
            for (int i = 0; i < nbProducts; i++) {
                distancesMatrix[nbProducts][i] = DistancesUtil.getAntiDiceDistance(product, products.get(i));
                distancesMatrix[i][nbProducts] = distancesMatrix[nbProducts][i];
            }
            
            noveltyScores = provideNoveltyScores(nbProducts + 1, distancesMatrix);

            //Find the solution with worst (smallest) novelty score
            double minScore = Double.MAX_VALUE;
            int minID  = -1;
            
            // double maxScore = - Double.MAX_VALUE;
            // int maxID  = -1;
            
            for (int i = 0; i < nbProducts; i++) {          
                    if (noveltyScores[i] < minScore) {
                        minScore = noveltyScores[i];
                        minID = i;
                    }
                    
                    // if (noveltyScores[i] > maxScore) {
                    //     maxScore = noveltyScores[i];
                    //     maxID = i;
                    // } 
            }
              
                    
            // Try to replace the worst member        
            if (noveltyScores[nbProducts]  > theta_ && (noveltyScores[nbProducts] > minScore)) {
                products.set(minID, product); // replace
                
    //			System.out.println("noveltyScores[archiveSize_]" + noveltyScores[archiveSize_] +",minScore"  + minScore);
                // Update the distance matrix
                for (int j=0;j < nbProducts; j++) {
                    distancesMatrix[minID][j] = distancesMatrix[nbProducts][j];
                    distancesMatrix[j][minID] = distancesMatrix[j][nbProducts];
                    // reset
                    distancesMatrix[nbProducts][j] = 0;
                    distancesMatrix[j][nbProducts] = 0;
                }
                
                distancesMatrix[minID][minID] = 0.0;
                noveltyScores[minID] = noveltyScores[nbProducts];
                // reset
                noveltyScores[nbProducts] = 0;
                return true;
            }    		
            
            return false;
        }

        private double[][] provideDistanceMatrice(int nbProducts)
        {
            distancesMatrix = new double[nbProducts + 1][nbProducts + 1];
            for (int i = 0; i < nbProducts; i++) {
                distancesMatrix[i][i] = 0.0;
                for (int j = i + 1; j < nbProducts; j++) {   
                    double dist = DistancesUtil.getAntiDiceDistance(products.get(i), products.get(j));             	
                    distancesMatrix[i][j] = dist;       
                    distancesMatrix[j][i] = dist;  
                }
            } 
            isMatrixInit = true;
            return distancesMatrix;
        }

        private double[] provideNoveltyScores(int nbProducts, double[][] distancesMatrix)
        {
            noveltyScores = new double[nbProducts];
            // Obtain the novelty scores    	
            // reset novelty scores
            for (int i = 0; i < nbProducts;i++) {
                noveltyScores[i] = 0.0;
            }
            
            double [] dist = new double [nbProducts]; 
            int []    idx =  new int [nbProducts]; 
                    
            for (int i = 0; i < nbProducts;i++) {
                
                for (int j = 0; j < nbProducts; j++) {
                    dist[j] = distancesMatrix[i][j];
                    idx[j] = j;
                }
                
                /* Find the k-nearest neighbors*/
                for (int u = 0; u < k_ - 1; u++) {
                    for (int v = u + 1; v < nbProducts; v++) {
                        if (dist[u] > dist[v]) {
                            double temp = dist[u];
                            dist[u]   = dist[v];
                            dist[v]   = temp;
                            int id = idx[u];
                            idx[u] = idx[v];
                            idx[v] = id;
                        } // if
                    }
                } // for

                /* Find the k-nearest neighbors*/
                // DistancesUtil.minFastSort(dist, idx, nbProducts + 1, k_);
                
                noveltyScores[i] = 0.0;    		
                for (int k = 0; k < k_; k++) {
                    noveltyScores[i] = noveltyScores[i] + dist[k];
                }
                
                noveltyScores[i] = noveltyScores[i]/k_; // the average value
                
            } // for i
            return noveltyScores;
        }

        private Product [] selectParents() {
            Product [] parents = new Product [2];                
            int p1 = binarySelection();
            int p2 = -1;              
            do {
                p2 = binarySelection(); 
            } while (p2==p1);                      
            parents[0] = products.get(p1);
            parents[1] = products.get(p2);
            return parents;	   
        } 

        private int binarySelection() {
            int r1 = random.nextInt(0, products.size());
            int r2 = -1;
            do {
                r2 = random.nextInt(0, products.size()); 
            } while (r2==r1);
            if (noveltyScores[r1] != noveltyScores[r2])
            {
                return noveltyScores[r1] > noveltyScores[r2] ? r1 : r2;
            }
            else {
                return random.nextDouble() > 0.5 ? r1 : r2;
            }
        }

        private Binary[] binaryCrossover(Product prod1, Product prod2) {
            // Tableau booléan
           Binary parent1 = Product2Bin(prod1);	   
           Binary parent2 = Product2Bin(prod2);	
           Binary [] offSpringBin = new Binary[2];
           
           // Tableaux randoms
           offSpringBin[0] = new Binary(prod1.size());
           offSpringBin[1] = new Binary(prod2.size());
                   
           Product [] offSpring = new  Product [2];

            for (int i = 0;i < parent1.getNumberOfBits();i++){ 
                // Pour chaque valeur boolénne, les enfants prennent la valeur d'un des deux parents
                // Enfants C prend 
                if (random.nextDouble() < 0.5) {
                    boolean value = parent1.getIth(i);	        
                    offSpringBin[0].setIth(i, value);	        		
                    
                    value = parent2.getIth(i);
                    offSpringBin[1].setIth(i, value);
                
                    } else {
                    boolean value = parent2.getIth(i);
                    offSpringBin[0].setIth(i, value);
                    value = parent1.getIth(i);	        
                    offSpringBin[1].setIth(i, value);

                    }
            }        
           return offSpringBin;
        }

        private Binary Product2Bin(Product prod) {

            Binary bin = new Binary(prod.size());    	    
            
            for (Integer i:prod) {
                
                if (i > 0){
                    bin.setIth(i-1, true);
                } else {
                    bin.setIth(Math.abs(i)-1, false);
                }
                    
            } // for i
            return bin;
        }
    }
    
}
