/*
 * 
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
package pledge.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.sat4j.specs.TimeoutException;

/**
 * This class represents a product (i.e. a list of features, selected or not).
 * 
 * @author Christopher Henard
 */
public class Product extends HashSet<Integer> implements Serializable {

    /* Relative coverage of this product. This value depends on the number of pairs 
     * covered by the previous products when we evaluate the coverage of a set of 
     * products.     */
    private double coverage;
    static final long serialVersionUID = -6618469841127325812L;

    /**
     * Create a product.
     */
    public Product() {
        super();
        coverage = 0;
    }

    /**
     * Returns the pairs covered by the product
     */
    public Set<TSet> getCoveredPairs() throws TimeoutException {

        List<Integer> pl = new ArrayList<Integer>(this);
        int size = size();
        Set<TSet> pairs = new HashSet<TSet>(size * (size - 1) / 2);
        Util.nCk(size, 2 , pairs, pl, false, null);
        return pairs;
    }
    
}
