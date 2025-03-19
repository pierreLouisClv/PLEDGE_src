# PLEDGE Evolution with Novelty Score

This repository contains an adaptation of PLEDGE by Pierre-Louis Clavel for executing experiments related to the following pre-published paper: Comparative Evaluation of Similarity-Based Prioritization Techniques in Search-Based Test Case Generation for Software Product Lines.

All tool features were originally developed by Christophe Henard. The original user guide can be found in the pledge_guide/ directory.

This version introduces prioritization techniques such as Novelty Score, Dice-Jaro-Winkler, and an enhanced similarity distance metric: Enhanced Jaro-Winkler.

## Prerequisites
Before running the experiment, ensure the following setup:

- Store .dimacs files in a designated file system path and specify this path in the loadExperimentBenchmark() method in ModelPLEDGE.
- Define a save directory for generated samples and test suites by setting SAVING_PATH_AREA in ModelPLEDGE.
- Uncomment the line List<Product> products = model.getStartingTestSuite(); in the Evolutionary Algorithm class and comment out the unpredictable generation logic.
- Configure benchmark parameters, including the feature model name (matching the .dimacs file name), test suite sizes, and time limits in the benchmarks variable within ModelPLEDGE.

## Running the Experiment
To execute the experiment, call the runCompleteExperiment() method in the ModelPLEDGE class.
