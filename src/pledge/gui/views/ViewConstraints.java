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

import java.awt.Component;
import pledge.gui.AdapterConstraints;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import pledge.core.ModelPLEDGE;

/**
 *
 * @author Christopher Henard
 */
public class ViewConstraints extends JPanel implements Observer {
    
    private static final String TITLE = "CNF Constraints";
    private static final int D_WIDTH = 300;
    private static final Color GRAY = new Color(220, 220, 220);
    private ModelPLEDGE model;
    private AdapterConstraints modelAdapter;
    private JList constraintsList;
    private JScrollPane scrollPane;
    
    public ViewConstraints(final ModelPLEDGE model) {
        super(new BorderLayout());
        this.model = model;
        model.addObserver(this);
        modelAdapter = new AdapterConstraints(model);
        constraintsList = new JList(modelAdapter);
        scrollPane = new JScrollPane(constraintsList);
        scrollPane.setPreferredSize(new Dimension(D_WIDTH, getHeight()));
        constraintsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        constraintsList.addListSelectionListener(new ListSelectionListener() {
            
            @Override
            public void valueChanged(ListSelectionEvent arg) {
                if (!arg.getValueIsAdjusting()) {
                   model.setCurrentConstraint(constraintsList.getSelectedIndex());
                }
            }
        });


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
}
