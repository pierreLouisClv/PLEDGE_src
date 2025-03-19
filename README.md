# PLEDGE evolution with Novelty Score

This repository contains an adaptation of PLEDGE made by Pierre-Louis Clavel for executing experiment of following pre-printed paper : Comparative Evaluation of Similarity-Based Prioritization Techniques in Search-Based Test Case Generation for Software Product Lines.

All the features of the Tool were developped by Christophe Henard. The original user guide can be in pledge_guide/.

This version provides prioritization techniques Novelty Score, Dice-Jaro-Winkler and a similarity distance Enhanced Jaro-Winkler

To reproduce the experiment, some prerequistes are needed :

- Store .dimacs files into a filesystem path and put this path into loadExperimentBenchmark() method in ModelPLEDGE
- Set a saving path area where samples and test suites will be generated into SAVING_PATH_AREA in ModelPLEDGE
- Uncomment "List<Product> products = model.getStartingTestSuite();" line in Evolutionary Algorithm Class and comment the unpredictable generation
- Configure benchmarks parameters with feature model name (corresponding to .dimacs file name), test suite sizes and time allowed into benchmarks variable in ModelPLEDGE class

Then, executes the experiment with method runCompleteExperiment() in class ModelPLEDGE
