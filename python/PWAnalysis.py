import os
import matplotlib.pyplot as plt

def read_execution_file(file_path):
    pairs = []
    with open(file_path, 'r') as f:
        for line in f:
            parts = line.strip().split(';')
            if len(parts) >= 1:
                pairs.append(int(parts[0]))
    return pairs

def load_data(root_dir):
    # data[nbProds][execution][technique] = (iterations, pairs)
    data = {}
    for technique in os.listdir(root_dir):
        tech_path = os.path.join(root_dir, technique)
        if not os.path.isdir(tech_path):
            continue
        for nbprods in os.listdir(tech_path):
            nbprods_path = os.path.join(tech_path, nbprods)
            if not os.path.isdir(nbprods_path):
                continue
            data.setdefault(nbprods, {})
            for i in range(5):  # execution_0.txt ... execution_4.txt
                exec_file = os.path.join(nbprods_path, f"execution_{i}.txt")
                if os.path.exists(exec_file):
                    pairs = read_execution_file(exec_file)
                    iterations = list(range(1, len(pairs) + 1))
                    data[nbprods].setdefault(i, {})
                    data[nbprods][i][technique] = (iterations, pairs)
    return data

def plot_graphs(data, output_dir="plotsWP8"):
    os.makedirs(output_dir, exist_ok=True)

    # One figure per nbProds per execution
    for nbprods, exec_dict in data.items():
        for exec_i, tech_dict in exec_dict.items():
            plt.figure(figsize=(10, 6))
            plt.title(f"WebPortal - nbProds {nbprods} - Execution {exec_i + 1}")
            plt.xlabel("Iteration")
            plt.ylabel("Number of pairs")

            for technique, (iterations, pairs) in tech_dict.items():
                plt.plot(iterations, pairs, label=technique)

            plt.legend()
            plt.grid(True)
            plt.tight_layout()

            # File name
            base_filename = f"nbProds_{nbprods}_Exec_{exec_i}"
            svg_path = os.path.join(output_dir, f"{base_filename}.svg")
            png_path = os.path.join(output_dir, f"{base_filename}.png")

            # Save vector and raster versions
            plt.savefig(svg_path, format="svg")
            plt.savefig(png_path, format="png", dpi=300)

            plt.close()  # Close the figure to save memory

    print(f"Plots saved in '{output_dir}' as SVG (vector) and PNG (raster).")

if __name__ == "__main__":
    root_dir = "PATH"  # Replace with your folder path
    data = load_data(root_dir)
    plot_graphs(data)
