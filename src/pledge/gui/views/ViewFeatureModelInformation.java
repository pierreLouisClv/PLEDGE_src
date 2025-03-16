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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import pledge.core.ModelPLEDGE;

/**
 *
 * @author Christopher Henard
 */
public class ViewFeatureModelInformation extends JPanel implements Observer {

    private ModelPLEDGE model;
    private static final String TITLE = "Feature Model Information";
    public static final String NAME = "Name:";
    public static final String FORMAT = "Format:";
    public static final String CONSTRAINTS = "Number of CNF constraints:";
    public static final String FEATURES = "Number of features:";
    public static final String CORE = "Number of core features:";
    public static final String DEAD = "Number of dead features:";
    private JLabel nameLabel, name;
    private JLabel formatLabel, format;
    private JLabel constraintsLabel, constraints;
    private JLabel featuresLabel, features;
    private JLabel coreLabel, core;
    private JLabel deadLabel, dead;

    public ViewFeatureModelInformation(ModelPLEDGE model) {
        super(new GridBagLayout());
        this.model = model;
        model.addObserver(this);
        setBorder(BorderFactory.createTitledBorder(TITLE));
        nameLabel = new JLabel(NAME);
        name = new JLabel("-", SwingConstants.LEFT);
        formatLabel = new JLabel(FORMAT);
        format = new JLabel("-", SwingConstants.LEFT);
        constraintsLabel = new JLabel(CONSTRAINTS);
        constraints = new JLabel("-", SwingConstants.LEFT);
        featuresLabel = new JLabel(FEATURES);
        features = new JLabel("-", SwingConstants.LEFT);
        coreLabel = new JLabel(CORE);
        core = new JLabel("-", SwingConstants.LEFT);
        deadLabel = new JLabel(DEAD);
        dead = new JLabel("-", SwingConstants.LEFT);
        GridBagConstraints c = new GridBagConstraints();

        Insets i = new Insets(20, 15, 0, 0);
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = i;
        c.anchor = GridBagConstraints.BASELINE_TRAILING;
        add(nameLabel, c);

        i = new Insets(20, 15, 0, 0);
        c.gridx = 1;
        c.insets = i;
        c.anchor = GridBagConstraints.BASELINE_LEADING;
        add(name, c);

        c.gridx = 2;
        c.anchor = GridBagConstraints.BASELINE_TRAILING;
        add(constraintsLabel, c);

        c.gridx = 3;
        c.anchor = GridBagConstraints.BASELINE_LEADING;
        add(constraints, c);

        c.gridx = 4;
        c.anchor = GridBagConstraints.BASELINE_TRAILING;
        add(coreLabel, c);

        c.gridx = 5;
        c.anchor = GridBagConstraints.BASELINE_LEADING;
        add(core, c);

        i = new Insets(10, 15, 0, 0);
        c.gridx = 0;
        c.gridy = 1;
        c.insets = i;
        c.anchor = GridBagConstraints.BASELINE_TRAILING;
        add(formatLabel, c);

        i = new Insets(10, 15, 10, 0);
        c.gridx = 1;
        c.insets = i;
        c.anchor = GridBagConstraints.BASELINE_LEADING;
        add(format, c);

        c.gridx = 2;
        c.anchor = GridBagConstraints.BASELINE_TRAILING;
        add(featuresLabel, c);

        c.gridx = 3;
        c.anchor = GridBagConstraints.BASELINE_LEADING;
        add(features, c);

        c.gridx = 4;
        c.anchor = GridBagConstraints.BASELINE_TRAILING;
        add(deadLabel, c);

        c.gridx = 5;
        c.anchor = GridBagConstraints.BASELINE_LEADING;
        add(dead, c);
    }

    @Override
    public void update(Observable o, final Object arg) {
        Runnable code = new Runnable() {

            @Override
            public void run() {
                if (arg != null && model.getSolver() != null) {
                    name.setText(model.getFeatureModelName());
                    format.setText(model.getFeatureModelFormat().name());
                    constraints.setText(""+model.getFeatureModelConstraintsString().size());
                    features.setText(""+model.getFeaturesList().size());
                    core.setText("" + model.getCoreFeatures().size());
                    dead.setText(""+model.getDeadFeatures().size());
                    
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
