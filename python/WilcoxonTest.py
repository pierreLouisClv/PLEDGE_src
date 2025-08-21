import os
import pandas as pd
from scipy.stats import wilcoxon
from itertools import combinations
import numpy as np

def read_pair_sizes(file_path):
    """Lit les tailles de paires d'un fichier texte."""
    with open(file_path, 'r') as f:
        return [int(line.strip()) for line in f if line.strip().isdigit()]

def collect_all_data(results_dir):
    """Collecte toutes les données de pair_sizes par technique et taille."""
    data = {}

    for root, _, files in os.walk(results_dir):
        for file in files:
            if file == "pair_sizes.txt":
                file_path = os.path.join(root, file)
                parts = file_path.split(os.sep)

                try:
                    technique = parts[-4]
                    num_prods = int(parts[-3].replace("prods", ""))
                except (IndexError, ValueError):
                    continue

                sizes = read_pair_sizes(file_path)

                if num_prods not in data:
                    data[num_prods] = {}
                if technique not in data[num_prods]:
                    data[num_prods][technique] = []

                data[num_prods][technique].append(sizes)

    return data

def flatten_runs(runs):
    """Fusionne plusieurs runs d’une technique en une seule liste."""
    return [val for run in runs for val in run]

def compute_confidence_interval(values):
    """Retourne la moyenne, l'écart-type et l'intervalle de confiance à 95%."""
    arr = np.array(values)
    n = len(arr)
    if n == 0:
        return (np.nan, np.nan, np.nan, np.nan)
    mean = np.mean(arr)
    std = np.std(arr, ddof=1)
    ci95 = 1.96 * std / np.sqrt(n)
    return mean, std, mean - ci95, mean + ci95

def apply_wilcoxon_tests(data, dataset_name, output_dir):
    """Applique le test de Wilcoxon et calcule les intervalles de confiance."""
    all_results = []
    ci_results = []

    os.makedirs(output_dir, exist_ok=True)

    for nb_prods, techniques_data in sorted(data.items()):
        techniques = list(techniques_data.keys())

        # Statistiques descriptives avec IC
        for tech in techniques:
            values = flatten_runs(techniques_data[tech])
            mean, std, ci_low, ci_high = compute_confidence_interval(values)

            ci_results.append({
                "Dataset": dataset_name,
                "Num Products": nb_prods,
                "Technique": tech,
                "Mean": mean,
                "Std Dev": std,
                "CI 95% Low": ci_low,
                "CI 95% High": ci_high,
                "N": len(values)
            })

        # Comparaison pairwise
        for tech1, tech2 in combinations(techniques, 2):
            values1 = flatten_runs(techniques_data[tech1])
            values2 = flatten_runs(techniques_data[tech2])

            min_len = min(len(values1), len(values2))
            if min_len < 10:
                continue

            stat, p_value = wilcoxon(values1[:min_len], values2[:min_len])

            all_results.append({
                "Dataset": dataset_name,
                "Num Products": nb_prods,
                "Technique 1": tech1,
                "Technique 2": tech2,
                "Wilcoxon p-value": p_value,
                "Significant (p<0.05)": "Yes" if p_value < 0.05 else "No"
            })

    return pd.DataFrame(all_results), pd.DataFrame(ci_results)

def main():
    # Put here Path of results indicated in PLEDGE tool
    base_path = "PATH"
    # Put here dataset used in the experiment
    dataset_list = [
        "DSSample", "Drupal", "WebPortal", "Amazon", "E-Shop", 
        "Printers", "CocheEcologico", 
        "SPLOT-3CNF-FM-1000-200-0,50-SAT-1", 
        "SPLOT-3CNF-FM-1000-200-0,50-SAT-7"
    ]

    for dataset in dataset_list:
        print(f"Processing dataset: {dataset}")
        root_dir = os.path.join(base_path, dataset)
        os.makedirs(root_dir, exist_ok=True)

        results_dir = os.path.join(root_dir, "Results")
        output_file = os.path.join(root_dir, f"{dataset}_wilcoxon_results_2.xlsx")

        data = collect_all_data(results_dir)
        df_wilcoxon, df_stats = apply_wilcoxon_tests(data, dataset, root_dir)

        # Sauvegarde dans deux feuilles Excel
        with pd.ExcelWriter(output_file) as writer:
            df_wilcoxon.to_excel(writer, sheet_name="Wilcoxon Results", index=False)
            df_stats.to_excel(writer, sheet_name="Confidence Intervals", index=False)

        print(f"✔ Saved results for {dataset} in {output_file}")

if __name__ == "__main__":
    main()