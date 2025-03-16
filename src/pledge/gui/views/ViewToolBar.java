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

import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import pledge.core.ModelPLEDGE;

/**
 *
 * @author Christopher Henard
 */
public class ViewToolBar extends JToolBar implements Observer {

    public static final String LOAD_FM = "Load a Feature Model";
    public static final String LOAD_PRODUCTS = "Load Products";
    public static final String QUIT = "Quit PLEDGE";
    public static final String SAVE_PRODUCTS = "Save the products";
    public static final String GENERATE = "Generate products";
    public static final String PRIORITIZE = "Prioritize products";
    public static final String STOP = "Stop the execution";
    public static final String ADD_CONSTRAINT = "Add a constraint to the Feature Model";
    public static final String REMOVE_CONSTRAINT = "Remove a constraint from the Feature Model";
    private ModelPLEDGE model;
    private ImageIcon loadFMIcon, loadProductsIcon, quitIcon, saveProductsIcon,
            stopIcon, generateIcon, prioritizeIcon, addConstraintIcon, removeConstraintIcon;
    private JButton loadFM, loadProducts, quit, saveProducts, generate, prioritize, stop, addConstraint, removeConstraint;
    private final URL urlLoadFM = getClass().getResource("icons/load_fm.png");
    private final URL urlLoadProducts = getClass().getResource("icons/load_products.png");
    private final URL urlQUIT = getClass().getResource("icons/exit.png");
    private final URL urlSaveProducts = getClass().getResource("icons/save_products.png");
    private final URL urlGenerate = getClass().getResource("icons/generate.png");
    private final URL urlPrioritize = getClass().getResource("icons/prioritize.png");
    private final URL urlStop = getClass().getResource("icons/stop.png");
    private final URL urlAddConstraint = getClass().getResource("icons/add.png");
    private final URL urlRemoveConstraint = getClass().getResource("icons/remove.png");

    public ViewToolBar(ModelPLEDGE model) {
        super();
        this.model = model;
        model.addObserver(this);
        setFloatable(false);
        setBorderPainted(false);
        loadFMIcon = new ImageIcon(urlLoadFM);
        loadProductsIcon = new ImageIcon(urlLoadProducts);
        quitIcon = new ImageIcon(urlQUIT);
        saveProductsIcon = new ImageIcon(urlSaveProducts);
        generateIcon = new ImageIcon(urlGenerate);
        prioritizeIcon = new ImageIcon(urlPrioritize);
        addConstraintIcon = new ImageIcon(urlAddConstraint);
        removeConstraintIcon = new ImageIcon(urlRemoveConstraint);
        stopIcon = new ImageIcon(urlStop);
        loadFM = new JButton(loadFMIcon);
        loadFM.setToolTipText(LOAD_FM);
        loadFM.setFocusPainted(false);
        loadProducts = new JButton(loadProductsIcon);
        loadProducts.setToolTipText(LOAD_PRODUCTS);
        loadProducts.setFocusPainted(false);
        quit = new JButton(quitIcon);
        quit.setToolTipText(QUIT);
        quit.setFocusPainted(false);
        saveProducts = new JButton(saveProductsIcon);
        saveProducts.setToolTipText(SAVE_PRODUCTS);
        saveProducts.setFocusPainted(false);
        generate = new JButton(generateIcon);
        generate.setToolTipText(GENERATE);
        generate.setFocusPainted(false);
        prioritize = new JButton(prioritizeIcon);
        prioritize.setToolTipText(PRIORITIZE);
        prioritize.setFocusPainted(false);
        stop = new JButton(stopIcon);
        stop.setToolTipText(STOP);
        stop.setFocusPainted(false);
        addConstraint = new JButton(addConstraintIcon);
        addConstraint.setToolTipText(ADD_CONSTRAINT);
        addConstraint.setFocusPainted(false);
        removeConstraint = new JButton(removeConstraintIcon);
        removeConstraint.setToolTipText(REMOVE_CONSTRAINT);
        removeConstraint.setFocusPainted(false);
        add(loadFM);
        add(loadProducts);
        add(quit);
        addSeparator();
        add(generate);
        add(prioritize);
        add(stop);
        add(saveProducts);
        addSeparator();
        add(addConstraint);
        add(removeConstraint);
        setEnabled();
    }

    public JButton getLoadFM() {
        return loadFM;
    }

    public JButton getLoadProducts() {
        return loadProducts;
    }

    public JButton getQuit() {
        return quit;
    }

    public JButton getGenerate() {
        return generate;
    }

    public JButton getPrioritize() {
        return prioritize;
    }

    public JButton getSaveProducts() {
        return saveProducts;
    }

    public JButton getStop() {
        return stop;
    }

    public JButton getAddConstraint() {
        return addConstraint;
    }

    public JButton getRemoveConstraint() {
        return removeConstraint;
    }
    
    private void setEnabled() {
        Runnable code = new Runnable() {

            @Override
            public void run() {
                generate.setEnabled(model.getSolver() != null && !model.isRunning());
                prioritize.setEnabled(model.getProducts() != null && !model.isRunning());
                saveProducts.setEnabled(model.getProducts() != null);
                loadFM.setEnabled(!model.isRunning());
                loadProducts.setEnabled(!model.isRunning());
                stop.setEnabled(model.isRunning());
                addConstraint.setEnabled(model.getSolver() != null && !model.isRunning());
                removeConstraint.setEnabled(model.getSolver() != null && !model.isRunning() && model.getCurrentConstraint() >= 0);
            }
        };
        if (SwingUtilities.isEventDispatchThread()) {
            code.run();
        } else {
            SwingUtilities.invokeLater(code);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        setEnabled();
    }
}
