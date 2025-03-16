import os
import pandas as pd

def calculate_pairwise_coverage(base_dir, total_valid_pairs, output_file):
    results = []
    
    # Parcourir les sous-dossiers dans Amazon/Samples
    for sample_dir in os.listdir(base_dir):
        if "prods" in sample_dir:
            try:
                num_prods = int(sample_dir.replace("prods", ""))  # Extraire le nombre de produits
                sample_path = os.path.join(base_dir, sample_dir, "PairSizes", "pair_sizes.txt")
                
                if os.path.isfile(sample_path):
                    # Lire les valeurs depuis le fichier
                    with open(sample_path, 'r') as f:
                        pair_sizes = [int(line.strip()) for line in f.readlines()]
                        
                    # Calcul du pairwise coverage
                    total_pair_size = sum(pair_sizes)
                    pairwise_coverage = (total_pair_size / (total_valid_pairs * len(pair_sizes))) * 100
                    
                    # Ajouter les résultats
                    results.append([num_prods, pairwise_coverage])
            except ValueError:
                continue
    
    # Trier les résultats par nombre de produits
    results.sort(key=lambda x: x[0])
    
    # Sauvegarde des résultats dans un fichier Excel
    df = pd.DataFrame(results, columns=["Number of Products", "Pairwise Coverage (%)"])
    df.to_excel(output_file, index=False)
    print(f"Résultats enregistrés dans {output_file}")

# Paramètres
data_set = "e-shop"
base_directory = "C:\\Users\\USER\\Desktop\\M2-IKSEM\\THESIS\\Results\\SuiteSizes_study\\" 
total_valid_pairs = 149723  # Remplacez par la valeur correcte
output_excel = base_directory + data_set + "\\" + data_set + "_pairwise_coverage_results.xlsx"

calculate_pairwise_coverage(base_directory + data_set + "\\Samples", total_valid_pairs, output_excel)