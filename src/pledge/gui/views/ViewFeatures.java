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
package pledge.gui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import pledge.core.ModelPLEDGE;
import pledge.gui.AdapterFeatures;

/**
 *
 * @author Christopher Henard
 */
public class ViewFeatures extends JPanel implements Observer {
    
    private static final String TITLE = "Features";
    public static final String[] COLUMN_TITLES = {"Number", "Name", "Type"};
    private ModelPLEDGE model;
    private AdapterFeatures modelAdapter;
    private JTable featuresTable;
    private JScrollPane scrollPane;

    public ViewFeatures(ModelPLEDGE model) {
        super(new BorderLayout());
        this.model = model;
        model.addObserver(this);
        modelAdapter = new AdapterFeatures(model);
        featuresTable = new JTable(modelAdapter);
        featuresTable.setAutoCreateRowSorter(true);
        TableCellRenderer headerRenderer = featuresTable.getTableHeader().getDefaultRenderer();
        ((DefaultTableCellRenderer) headerRenderer).setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        featuresTable.setOpaque(true);
        Cell cell = new Cell();
        for (int i = 0; i < featuresTable.getColumnModel().getColumnCount(); i++) {
            featuresTable.getColumnModel().getColumn(i).setCellRenderer(cell);
        }
        scrollPane = new JScrollPane(featuresTable);
        setBorder(BorderFactory.createTitledBorder(TITLE));
        add(scrollPane, BorderLayout.CENTER);
    }
    
    

    @Override
    public void update(Observable o, final Object arg) {
        Runnable code = new Runnable() {

            @Override
            public void run() {
                if (arg != null) {
                    modelAdapter.update();
                }
            }
        };
        if (SwingUtilities.isEventDispatchThread()) {
            code.run();
        } else {
            SwingUtilities.invokeLater(code);
        }
    }
    
    private class Cell extends DefaultTableCellRenderer {

        /**
         * Construit une cellule.
         */
        public Cell() {
            setOpaque(true);
            setHorizontalAlignment(JLabel.CENTER);
            setBackground(Color.white);
        }

        /**
         * Retourne le composant d'affichage de la cellule.
         * @param table la jtable concernée.
         * @param value la valeur dans la cellule.
         * @param isSelected un booléen indiquant si la cellule est sélectionnée.
         * @param hasFocus un booléen indiquant si la cellule a le focus.
         * @param row la ligne concernée.
         * @param column la colonne concernée.
         * @return le composant d'affichage de la cellule.
         */
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (!isSelected) {

                if (row % 2 == 0) {
                    setBackground(ViewPLEDGE.BLUE_COLOR);
                } else {
                    setBackground(Color.white);
                }
            }
            return this;
        }
    }
}
