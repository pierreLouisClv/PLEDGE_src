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

import javax.swing.table.AbstractTableModel;
import pledge.core.ModelPLEDGE;
import pledge.gui.views.ViewFeatures;

/**
 *
 * @author Christopher Henard
 */
public class AdapterFeatures extends AbstractTableModel {

    private ModelPLEDGE model;
    public static final int COLUMNS_COUNT = 3;

    public AdapterFeatures(ModelPLEDGE model) {
        this.model = model;
    }

    @Override
    public int getRowCount() {
        return model.getFeaturesList().size();
    }

    @Override
    public int getColumnCount() {
        return ViewFeatures.COLUMN_TITLES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return rowIndex + 1;
        } else if (columnIndex == 1) {
            return model.getFeaturesList().get(rowIndex);
        } else {
           return model.getFeatureType(model.getFeaturesList().get(rowIndex));
        }
    }

    @Override
    public String getColumnName(int col) {
        return ViewFeatures.COLUMN_TITLES[col];
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public void update(){
        fireTableDataChanged() ;
    }
}
