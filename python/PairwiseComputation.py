import os
import itertools

def read_products(file_path):
    """Lit un fichier contenant les produits et retourne une liste de listes d'entiers."""
    products = []
    with open(file_path, 'r') as file:
        for line in file:
            numbers = [x.strip() for x in line.strip().split(';') if x.strip()]
            try:
                products.append([int(x) for x in numbers])
            except ValueError:
                print(f"Erreur de conversion dans le fichier {file_path}, ligne ignorée: {line.strip()}")
    return products

def generate_twise_sets(products):
    """Génère les 2-wise sets pour une liste de produits et retourne leur nombre."""
    t_sets = set()
    for product in products:
        t_sets.update(itertools.combinations(product, 2))
    return len(t_sets)

def process_directory(root_path, samples=True):
    """Parcourt les répertoires et calcule les 2-wise interactions."""
    base_dir = os.path.join(root_path, "Samples" if samples else "Results")
    process_level = 1 if samples else 2
    for root, dirs, files in os.walk(base_dir):
        # Limite la profondeur de la recherche à 3 niveaux
        level = root[len(base_dir):].count(os.sep)
        if level == process_level:
            process_products(root)

def process_products(dir_path):
    pair_sizes = []
    
    for file in os.listdir(dir_path):
        file_path = os.path.join(dir_path, file)
        if os.path.isfile(file_path):
            products = read_products(file_path)
            pair_count = generate_twise_sets(products)
            pair_sizes.append(str(pair_count))
    
    if pair_sizes:
        # Correction : Assurer que PairSizes est bien placé
        output_dir = os.path.join(dir_path, "PairSizes")  # Utiliser root au lieu de sub_path
        os.makedirs(output_dir, exist_ok=True)
        output_file = os.path.join(output_dir, "pair_sizes.txt")
        
        with open(output_file, 'w') as f:
            f.write("\n".join(pair_sizes))
        print(f"Fichier généré : {output_file}")

if __name__ == "__main__":
    # Enter the saving area configured in PLEDGE followed by the feature model name
    root_directory = "PATH\\e-shop" 
    process_directory(root_directory, samples=True)
    process_directory(root_directory, samples=False)
