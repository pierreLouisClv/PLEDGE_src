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
import org.sat4j.specs.IConstr;
import pledge.core.ModelPLEDGE;
import pledge.gui.views.ViewFeatures;

/**
 *
 * @author Christopher Henard
 */
public class AdapterConstraints extends AbstractListModel {

    private ModelPLEDGE model;

    public AdapterConstraints(ModelPLEDGE model) {
        this.model = model;
    }

    @Override
    public int getSize() {
        return model.getFeatureModelConstraintsString().size();
    }

    @Override
    public Object getElementAt(int index) {
        return model.getFeatureModelConstraintsString().get(index);

    }

    public void update() {
        fireContentsChanged(this, 0, getSize() - 1);
    }
}
