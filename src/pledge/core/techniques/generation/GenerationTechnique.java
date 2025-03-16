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
package pledge.core.techniques.generation;

import java.util.List;
import pledge.core.ModelPLEDGE;
import pledge.core.Product;
import pledge.core.techniques.prioritization.PrioritizationTechnique;

/**
 * This is the interface for the products generation technique.
 * 
 * @author Christopher Henard
 */
public interface GenerationTechnique {
    
    /**
     * Generate products.
     * @param model the application's modeL
     * @param nbProducts the number of products to generate.
     * @param timeAllowed the time allowed in seconds to generate products.
     * @param prioritizationTechnique the prioritization technique to use.
     * @return a list containing the generated products.
     * @throws Exception if a problem occurs during the generation.
     */
    public List<Product> generateProducts(ModelPLEDGE model, int nbProducts, long timeAllowed, PrioritizationTechnique prioritizationTechnique) throws Exception;
    
    /**
     * Returns the name of this technique.
     * @return a String representing the name of this technique.
     */
    public String getName();
}
