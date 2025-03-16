/*
 * Author : Christopher Henard (christopher.henard@uni.lu)
 * Date : 01/11/2012
 * Copyright 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
 * All rights reserved
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package pledge;

import java.util.logging.Level;
import java.util.logging.Logger;
import pledge.core.ModelPLEDGE;
import pledge.gui.GUI;

/**
 *
 * @author Christopher Henard
 * 
 * This is the main class of the application.
 */
public class Main {

    /**
     * Entry point of the application.
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                new GUI();
            } else {
                CommandLineParser parser = new CommandLineParser(args, "PLEDGE");
                try {
                    
                    parser.parseArgs();
                    if (parser.getCommandName().equals(CommandLineParser.GENERATE)) {
                        ModelPLEDGE model = new ModelPLEDGE();
                        if (parser.getCommandGenerate().dimacs) {
                            model.loadFeatureModel(parser.getCommandGenerate().fmFile, model.getFeatureModelFormat().DIMACS);
                        } else {
                            model.loadFeatureModel(parser.getCommandGenerate().fmFile, model.getFeatureModelFormat().SPLOT);
                        }
                        model.setNbProductsToGenerate(parser.getCommandGenerate().nbProds);
                        model.setGenerationTimeMSAllowed(parser.getCommandGenerate().timeAllowed);
                        model.generateProducts();
                        model.saveProducts(parser.getCommandGenerate().outputFile);
                    }
                } catch (Exception e) {
                    parser.printUsage();
                }
            }



        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
