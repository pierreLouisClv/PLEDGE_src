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
package pledge.core.techniques.prioritization;

import java.util.List;
import pledge.core.ModelPLEDGE;
import pledge.core.Product;



/**
 * This is the interface for the products' prioritization techniques.
 *
 * @author Christopher Henard
 */
public interface PrioritizationTechnique {
    
    /**
     * Returns the prioritized list of products.
     * @param model the model of the application.
     * @param products the list of products to prioritize.
     * @return the prioritized list of products.
     * @throws Exception if an error occurs during the prioritization.
     */
    public List<Product> prioritize(ModelPLEDGE model, List<Product> products) throws Exception;
    
    /**
     * Return the name of this technique.
     * @return the name of this technique.
     */
    public String getName();
    
    /**
     * Return the fitness sum associated to the products.
     * @return a double representing the fitness sum associated to the products.
     */
    public double getFitnessSum();

}
