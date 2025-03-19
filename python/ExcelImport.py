import os
import pandas as pd

def calculate_pairwise_coverage(file_path, valid_2_sets):
    """Calcule le pairwise coverage moyen d'un fichier."""
    with open(file_path, 'r') as f:
        pair_sizes = [int(line.strip()) for line in f if line.strip().isdigit()]
    
    if not pair_sizes:
        return None  # Si le fichier est vide ou corrompu
    
    total_pair_sizes = sum(pair_sizes)
    pairwise_coverage = (total_pair_sizes / (valid_2_sets * len(pair_sizes))) * 100
    return pairwise_coverage

def extract_data(results_dir, samples_dir, valid_pairs, output_excel):
    """Extrait les données et les stocke dans un fichier Excel."""
    data = []
    
    for root, _, files in os.walk(results_dir):
        for file in files:
            if file == "pair_sizes.txt":
                file_path = os.path.join(root, file)
                
                # Extraire la technique et le nombre de produits depuis le chemin
                parts = file_path.split(os.sep)
                try:
                    technique = parts[-4]  # Exemple : Evolutionary_Algorithm-Greedy-DJW
                    num_prods = parts[-3]   # Exemple : 10prods, 5prods, etc.
                except IndexError:
                    continue
                
                # Trouver le fichier correspondant dans Samples
                sample_file_path = os.path.join(samples_dir, num_prods, "PairSizes", "pair_sizes.txt")
                
                if os.path.exists(sample_file_path):
                    valid_2_sets = valid_pairs  # Valeur par défaut 1 si manquant
                    
                    result_coverage = calculate_pairwise_coverage(file_path, valid_2_sets)
                    sample_coverage = calculate_pairwise_coverage(sample_file_path, valid_2_sets)
                    
                    data.append([technique, int(num_prods[:-5]), result_coverage, sample_coverage])
    
    # Convertir en DataFrame et sauvegarder en Excel
    data_sorted = sorted(data, key=lambda x: (x[1], -x[2]))
    df = pd.DataFrame(data_sorted, columns=["Technique", "Nombre de Produits", "Pairwise Cov", "Pairwise Cov Sample"])
    df.to_excel(output_excel, index=False)
    print(f"Extraction terminée. Fichier enregistré sous {output_excel}")

# Feature model name
data_set = "E-Shop"
# Enter here the corresponding path to the saving area configured in PLEDGE
root_directory = "PATH" + "\\" + data_set
results_directory = root_directory + "\\Results"
samples_directory = root_directory + "\\Samples"
# Valid pairs must be added manually
valid_pairs =  00
output_file = root_directory + "\\" + data_set + "_pairwise_coverage_results.xlsx"

extract_data(results_directory, samples_directory, valid_pairs, output_file)