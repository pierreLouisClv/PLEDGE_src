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

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import pledge.core.ModelPLEDGE;
import pledge.core.techniques.generation.GenerationTechnique;
import pledge.core.techniques.prioritization.PrioritizationTechnique;

/**
 *
 * @author Christopher Henard
 */
public class ViewMenuBar extends JMenuBar implements Observer {

    private static final String FILE = "File";
    private static final String CONFIGURATION = "Configuration";
    private static final String EXECUTE = "Execute";
    private static final String HELP = "Help";
    private static final String LOAD_FEATURE_MODEL = "Load a Feature Model...";
    private static final String LOAD_PRODUCTS = "Load Products...";
    private static final String LOAD_PRODUCTS_FM = "Load Products after Feature Model...";
    private static final String QUIT = "Quit";
    public static final String GENERATE = "Generate products";
    public static final String PRIORITIZE = "Prioritize products";
    public static final String STOP = "Stop the execution";
    public static final String SAVE_PRODUCTS = "Save the products";
    public static final String COMPUTE_PAIRWISE = "Compute the pairwise coverage";
    public static final String DOC = "Documentation";
    public static final String ABOUT = "About PLEDGE...";
    public static final String PRIORITIZATION_TECHNIQUE = "Prioritization technique";
    public static final String GENERATION_TECHNIQUE = "Generation technique";
    private ModelPLEDGE model;
    private JMenu file, execute, configuration, help, generationTechnique, prioritizationTechnique;
    private JMenuItem loadFeatureModel, loadProducts, loadProductsFM, quit, generate, prioritize, coverage, stop, saveProducts, about, doc;
    private ImageIcon loadFMIcon, loadProductsIcon, quitIcon, saveProductsIcon,
            stopIcon, generateIcon, prioritizeIcon;
    private List<JRadioButtonMenuItem> generationTechniqueButtons = new ArrayList<JRadioButtonMenuItem>();
    private List<JRadioButtonMenuItem> prioritizationTechniqueButtons = new ArrayList<JRadioButtonMenuItem>();
    private final URL urlLoadFM = getClass().getResource("icons/load_fm.png");
    private final URL urlLoadProducts = getClass().getResource("icons/load_products.png");
    private final URL urlQuit = getClass().getResource("icons/exit.png");
    private final URL urlSaveProducts = getClass().getResource("icons/save_products.png");
    private final URL urlGenerate = getClass().getResource("icons/generate.png");
    private final URL urlPrioritize = getClass().getResource("icons/prioritize.png");
    private final URL urlStop = getClass().getResource("icons/stop.png");

    public ViewMenuBar(ModelPLEDGE model) {
        super();
        this.model = model;
        model.addObserver(this);
        loadFMIcon = new ImageIcon(urlLoadFM);
        loadProductsIcon = new ImageIcon(urlLoadProducts);
        quitIcon = new ImageIcon(urlQuit);
        saveProductsIcon = new ImageIcon(urlSaveProducts);
        generateIcon = new ImageIcon(urlGenerate);
        prioritizeIcon = new ImageIcon(urlPrioritize);
        stopIcon = new ImageIcon(urlStop);
        loadFeatureModel = new JMenuItem(LOAD_FEATURE_MODEL, loadFMIcon);
        loadFeatureModel.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
        loadProducts = new JMenuItem(LOAD_PRODUCTS, loadProductsIcon);
        loadProducts.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        loadProductsFM = new JMenuItem(LOAD_PRODUCTS_FM);
        loadProductsFM.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK));
        quit = new JMenuItem(QUIT, quitIcon);
        quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        generate = new JMenuItem(GENERATE, generateIcon);
        generate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.ALT_MASK));
        prioritize = new JMenuItem(PRIORITIZE, prioritizeIcon);
        prioritize.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.ALT_MASK));
        coverage = new JMenuItem(COMPUTE_PAIRWISE);
        stop = new JMenuItem(STOP, stopIcon);
        stop.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, ActionEvent.ALT_MASK));
        saveProducts = new JMenuItem(SAVE_PRODUCTS, saveProductsIcon);
        saveProducts.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
        doc = new JMenuItem(DOC);
        doc.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.ALT_MASK));
        about = new JMenuItem(ABOUT);
        about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
        generationTechnique = new JMenu(GENERATION_TECHNIQUE);
        ButtonGroup generationGroup = new ButtonGroup();
        for (GenerationTechnique gt : model.getGenerationTechniques()) {
            final JRadioButtonMenuItem button = new JRadioButtonMenuItem(gt.getName());
            generationTechniqueButtons.add(button);
            if (gt.getName().equals(model.getGenerationTechnique().getName())) {
                button.setSelected(true);
            }
            generationGroup.add(button);
            generationTechnique.add(button);
        }
        prioritizationTechnique = new JMenu(PRIORITIZATION_TECHNIQUE);
        ButtonGroup prioritizationGroup = new ButtonGroup();
        for (PrioritizationTechnique pt : model.getPrioritizationTechniques()) {
            final JRadioButtonMenuItem button = new JRadioButtonMenuItem(pt.getName());
            prioritizationTechniqueButtons.add(button);
            if (pt.getName().equals(model.getPrioritizationTechnique().getName())) {
                button.setSelected(true);
            }
            prioritizationGroup.add(button);
            prioritizationTechnique.add(button);
        }
        file = new JMenu(FILE);
        file.setMnemonic(KeyEvent.VK_F);
        file.add(loadFeatureModel);
        file.add(loadProducts);
        file.add(loadProductsFM);
        file.addSeparator();
        file.add(quit);
        add(file);
        execute = new JMenu(EXECUTE);
        execute.setMnemonic(KeyEvent.VK_E);
        execute.add(generate);
        execute.add(prioritize);
        execute.add(coverage);
        execute.add(stop);
        execute.addSeparator();
        execute.add(saveProducts);
        add(execute);
        configuration = new JMenu(CONFIGURATION);
        configuration.setMnemonic(KeyEvent.VK_C);
        configuration.add(generationTechnique);
        configuration.add(prioritizationTechnique);
        add(configuration);
        help = new JMenu(HELP);
        help.setMnemonic(KeyEvent.VK_H);
        help.add(doc);
        help.addSeparator();
        help.add(about);
        add(help);
        setEnabled();
    }

    public JMenuItem getLoadFeatureModel() {
        return loadFeatureModel;
    }

    public JMenuItem getQuit() {
        return quit;
    }

    public JMenuItem getLoadProducts() {
        return loadProducts;
    }
    
    public JMenuItem getLoadProductsFM() {
        return loadProductsFM;
    }

    public List<JRadioButtonMenuItem> getGenerationTechniqueButtons() {
        return generationTechniqueButtons;
    }

    public List<JRadioButtonMenuItem> getPrioritizationTechniqueButtons() {
        return prioritizationTechniqueButtons;
    }

    public JMenuItem getAbout() {
        return about;
    }

    public JMenuItem getGenerate() {
        return generate;
    }

    public JMenuItem getPrioritize() {
        return prioritize;
    }

    public JMenuItem getDoc() {
        return doc;
    }

    public JMenuItem getCoverage() {
        return coverage;
    }
    
    

    public JMenuItem getSaveProducts() {
        return saveProducts;
    }
    
    
    
    

    @Override
    public void update(Observable o, Object arg) {
        setEnabled();
    }

    public String getSelectedGenerationTechnique() {
        for (JRadioButtonMenuItem button : generationTechniqueButtons) {
            if (button.isSelected()) {
                return button.getText();
            }
        }
        return null;
    }

    public String getSelectedPrioritizationTechnique() {
        for (JRadioButtonMenuItem button : prioritizationTechniqueButtons) {
            if (button.isSelected()) {
                return button.getText();
            }
        }
        return null;
    }

    private void setEnabled() {
        Runnable code = new Runnable() {

            @Override
            public void run() {
                generate.setEnabled(model.getSolver() != null &&! model.isRunning());
                prioritize.setEnabled(model.getProducts() != null && ! model.isRunning());
                coverage.setEnabled(model.getProducts() != null && ! model.isRunning());
                saveProducts.setEnabled(model.getProducts() != null);
                stop.setEnabled(model.isRunning());
                loadFeatureModel.setEnabled(!model.isRunning());
                loadProducts.setEnabled(!model.isRunning());
                execute.setEnabled(generate.isEnabled() || prioritize.isEnabled() || stop.isEnabled());
            }
        };
        if (SwingUtilities.isEventDispatchThread()) {
            code.run();
        } else {
            SwingUtilities.invokeLater(code);
        }
    }
}
