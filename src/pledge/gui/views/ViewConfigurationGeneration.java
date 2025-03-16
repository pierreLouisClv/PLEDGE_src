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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import pledge.core.ModelPLEDGE;

/**
 *
 * @author Christopher Henard
 */
public class ViewConfigurationGeneration extends JDialog implements Observer {

    private static final String CONFIG_GENERATION = "Generation parameters";
    private static final String NB_PRODUCTS = "Number of products to generate:";
    private static final String TIME = "Time in seconds allowed for the generation:";
    private static final String OK = "Ok";
    private static final int D_WIDTH = 500, D_HEIGHT = 150;
    private ModelPLEDGE model;
    private JLabel nbProductsLabel, timeLabel;
    private JSpinner nbProducts, time;
    private JButton ok;
    private JPanel content, options, valid;
    private ViewPLEDGE viewPLEDGE;

    public ViewConfigurationGeneration(ModelPLEDGE model, ViewPLEDGE viewPLEDGE) {
        super(viewPLEDGE, CONFIG_GENERATION);
        this.viewPLEDGE = viewPLEDGE;
        this.model = model;
        content = new JPanel(new BorderLayout());
        valid = new JPanel();
        options = new JPanel(new GridBagLayout());
        nbProductsLabel = new JLabel(NB_PRODUCTS, SwingConstants.LEFT);
        timeLabel = new JLabel(TIME, SwingConstants.LEFT);
        nbProducts = new JSpinner(new SpinnerNumberModel(model.getNbProductsToGenerate(), 1, 1000000, 1));
        time = new JSpinner(new SpinnerNumberModel(model.getGenerationTimeMSAllowed()/1000, 1, 1000000, 1));
        GridBagConstraints c = new GridBagConstraints();
        c.insets= new Insets(20, 15, 0, 0);
        c.gridx = c.gridy = 0;
        c.anchor = GridBagConstraints.BASELINE_LEADING;
        options.add(nbProductsLabel, c);
        c.gridx = 1;
        c.anchor = GridBagConstraints.BASELINE_TRAILING;
        options.add(nbProducts, c);
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.BASELINE_LEADING;
        options.add(timeLabel, c);
        c.gridx = 1;
        c.anchor = GridBagConstraints.BASELINE_TRAILING;
        options.add(time,c);
        ok = new JButton(OK);
        valid.add(ok);
        content.add(options, BorderLayout.CENTER);
        content.add(valid, BorderLayout.SOUTH);
        add(content);
        setSize(new Dimension(D_WIDTH, D_HEIGHT));
        setResizable(false);
        setLocationRelativeTo(getParent());

    }

    @Override
    public void update(Observable o, Object arg) {
        Runnable code = new Runnable() {

            @Override
            public void run() {
                nbProducts.setValue(model.getNbProductsToGenerate());
                time.setValue(model.getGenerationTimeMSAllowed()/1000);

            }
        };
        if (SwingUtilities.isEventDispatchThread()) {
            code.run();
        } else {
            SwingUtilities.invokeLater(code);
        }
    }

    public JButton getOk() {
        return ok;
    }

    public JSpinner getNbProducts() {
        return nbProducts;
    }

    public JSpinner getTime() {
        return time;
    }
}
