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
package pledge.gui;

import javax.swing.AbstractListModel;
import pledge.core.ModelPLEDGE;
import pledge.core.Product;

/**
 *
 * @author Christopher Henard
 */
public class AdapterProducts extends AbstractListModel {

    private ModelPLEDGE model;

    public AdapterProducts(ModelPLEDGE model) {
        this.model = model;
    }

    @Override
    public int getSize() {
        if (model.getProducts() != null) {
            return model.getProducts().size();

        }
        return 0;
    }

    @Override
    public Object getElementAt(int index) {
        if (model.getProducts() != null) {
            if (model.getSolver() != null) {
                Product p = model.getProducts().get(index);
                String s = "P" + (index + 1) + ":";
                for (Integer i : p) {
                    if (i > 0) {
                        s += "    " + model.getFeaturesList().get(i - 1);
                    }
                }
                return s;
            } else {
                return "P" + (index + 1) + "      " + model.getProducts().get(index);
            }
        }
        return null;

    }

    public void update() {
        fireContentsChanged(this, 0, getSize() - 1);
    }
}
