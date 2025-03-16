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
import java.awt.GridLayout;
import java.io.File;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import pledge.core.ModelPLEDGE;
import pledge.gui.controllers.ControllerCloseAbout;
import pledge.gui.controllers.ControllerCloseEditConstraints;
import pledge.gui.controllers.ControllerCoverage;
import pledge.gui.controllers.ControllerDisplayAbout;
import pledge.gui.controllers.ControllerDisplayDocumentation;
import pledge.gui.controllers.ControllerDisplayEditConstraints;
import pledge.gui.controllers.ControllerGenerateProducts;
import pledge.gui.controllers.ControllerGenerationTechnique;
import pledge.gui.controllers.ControllerLoadFeatureModel;
import pledge.gui.controllers.ControllerLoadProducts;
import pledge.gui.controllers.ControllerLoadProductsFM;
import pledge.gui.controllers.ControllerPrioritizationTechnique;
import pledge.gui.controllers.ControllerPrioritizeProducts;
import pledge.gui.controllers.ControllerQuit;
import pledge.gui.controllers.ControllerRemoveConstraint;
import pledge.gui.controllers.ControllerSaveProducts;
import pledge.gui.controllers.ControllerViewConfigurationGeneration;

/**
 *
 * @author Christopher Henard
 */
public class ViewPLEDGE extends JFrame implements Observer {

    public static final Color BLUE_COLOR = new Color(232, 239, 247);
    public static final int D_WIDTH = 900, D_HEIGHT = 600;
    public static final String TITLE = "PLEDGE - A Product Line EDitor and tests GEneration tool";
    private static final String QUIT_MESSAGE = "Quit PLEDGE will interrupt any running task. Do you want to continue ?";
    private static final String QUIT_TITLE = "Quit PLEDGE";
    private static final String FILE_CHOOSER_FEATURE_MODEL_TITLE = "Load a Feature Model";
    private static final String FILE_CHOOSER_PRODUCTS_TITLE = "Load Products";
    private static final String FILE_SAVER_PRODUCTS_TITLE = "Save Products";
    // Update pairwise to t-wise
    private static final String COVERAGE_TITLE = "T-wise coverage";
    private final FileNameExtensionFilter featureModelFileFilter = new FileNameExtensionFilter("SPLOT or DIMACS Feature Models (.xml, .dimacs)", "xml", "dimacs");
    private static final String TAB_FEATURE_MODEL = "Feature Model";
    private final URL BACKGROUND_URL = getClass().getResource("icons/logo.png");

    public enum FileType {

        FEATURE_MODEL, PRODUCTS
    };
    private JFileChooser featureModelChooser, productsChooser, productsSaver;
    // Model
    private ModelPLEDGE model;
    // Controllers
    private ControllerQuit controllerQuit;
    private ControllerLoadFeatureModel controllerLoadFeatureModel;
    private ControllerLoadProducts controllerLoadProducts;
    private ControllerSaveProducts controllerSaveProducts;
    private ControllerGenerationTechnique controllerGenerationTechnique;
    private ControllerPrioritizationTechnique controllerPrioritizationTechnique;
    private ControllerDisplayAbout controllerDisplayAbout;
    private ControllerCloseAbout controllerCloseAbout;
    private ControllerViewConfigurationGeneration controllerViewConfigurationGeneration;
    private ControllerGenerateProducts controllerGenerateProducts;
    private ControllerPrioritizeProducts controllerPrioritizeProducts;
    private ControllerCoverage controllerCoverage;
    private ControllerDisplayDocumentation controllerDisplayDocumentation;
    private ControllerDisplayEditConstraints controllerDisplayEditConstraints;
    private ControllerCloseEditConstraints controllerCloseEditConstraints;
    private ControllerRemoveConstraint controllerRemoveConstraint;
    private ControllerLoadProductsFM controllerLoadProductsFM;
    // Views
    private ViewMenuBar viewMenuBar;
    private ViewConstraints viewConstraints;
    private ViewFeatures viewFeatures;
    private ViewToolBar viewToolBar;
    private ViewStatusBar viewStatusBar;
    private ViewAboutWindow viewAboutWindow;
    private ViewFeatureModelInformation viewFeatureModelInformation;
    private ViewConfigurationGeneration viewConfigurationGeneration;
    private ViewProducts viewProducts;
    private JPanel content, background;
    private ViewDocumentation viewDocumentation;
    private ViewEditConstraints viewEditConstraints;

    public ViewPLEDGE() {
        super(TITLE);
        model = new ModelPLEDGE();
        model.addObserver(this);
        new Thread(new Runnable(){

            @Override
            public void run() {
                viewDocumentation = ViewDocumentation.getInstance();
                viewDocumentation.setLocationRelativeTo(ViewPLEDGE.this);
            }
            
        }).start();
        viewAboutWindow = new ViewAboutWindow(this);
        viewMenuBar = new ViewMenuBar(model);
        viewConstraints = new ViewConstraints(model);
        viewFeatures = new ViewFeatures(model);
        viewStatusBar = new ViewStatusBar(model);
        viewFeatureModelInformation = new ViewFeatureModelInformation(model);
        viewConfigurationGeneration = new ViewConfigurationGeneration(model, this);
        viewProducts = new ViewProducts(model);
        viewToolBar = new ViewToolBar(model);
        viewEditConstraints = new ViewEditConstraints(model, this);
        controllerQuit = new ControllerQuit(model, this);
        controllerLoadFeatureModel = new ControllerLoadFeatureModel(model, this);
        controllerLoadProducts = new ControllerLoadProducts(model, this);
        controllerLoadProductsFM = new ControllerLoadProductsFM(model, this);
        controllerDisplayAbout = new ControllerDisplayAbout(this);
        controllerCloseAbout = new ControllerCloseAbout(this);
        controllerViewConfigurationGeneration = new ControllerViewConfigurationGeneration(this);
        controllerGenerateProducts = new ControllerGenerateProducts(model, this, viewConfigurationGeneration);
        controllerCoverage = new ControllerCoverage(model, this);
        controllerSaveProducts = new ControllerSaveProducts(model, this);
        controllerPrioritizeProducts = new ControllerPrioritizeProducts(model, this);
        controllerDisplayDocumentation = new ControllerDisplayDocumentation(this);
        controllerDisplayEditConstraints = new ControllerDisplayEditConstraints(this);
        controllerCloseEditConstraints =  new ControllerCloseEditConstraints(this);
        controllerRemoveConstraint = new ControllerRemoveConstraint(model);
        viewEditConstraints.getCloseButton().addActionListener(controllerCloseEditConstraints );
        viewAboutWindow.getCloseButton().addActionListener(controllerCloseAbout);
        viewMenuBar.getQuit().addActionListener(controllerQuit);
        viewMenuBar.getLoadFeatureModel().addActionListener(controllerLoadFeatureModel);
        viewMenuBar.getLoadProducts().addActionListener(controllerLoadProducts);
        viewMenuBar.getLoadProductsFM().addActionListener(controllerLoadProductsFM);
        viewMenuBar.getAbout().addActionListener(controllerDisplayAbout);
        viewMenuBar.getGenerate().addActionListener(controllerViewConfigurationGeneration);
        viewMenuBar.getSaveProducts().addActionListener(controllerSaveProducts);
        viewMenuBar.getPrioritize().addActionListener(controllerPrioritizeProducts);
        viewMenuBar.getDoc().addActionListener(controllerDisplayDocumentation);
        viewMenuBar.getCoverage().addActionListener(controllerCoverage);
        viewConfigurationGeneration.getOk().addActionListener(controllerGenerateProducts);
        controllerGenerationTechnique = new ControllerGenerationTechnique(model, viewMenuBar);
        for (JRadioButtonMenuItem button : viewMenuBar.getGenerationTechniqueButtons()) {
            button.addActionListener(controllerGenerationTechnique);
        }
        controllerPrioritizationTechnique = new ControllerPrioritizationTechnique(model, viewMenuBar);
        for (JRadioButtonMenuItem button : viewMenuBar.getPrioritizationTechniqueButtons()) {
            button.addActionListener(controllerPrioritizationTechnique);
        }
        viewToolBar.getQuit().addActionListener(controllerQuit);
        viewToolBar.getLoadFM().addActionListener(controllerLoadFeatureModel);
        viewToolBar.getLoadProducts().addActionListener(controllerLoadProducts);
        viewToolBar.getGenerate().addActionListener(controllerViewConfigurationGeneration);
        viewToolBar.getSaveProducts().addActionListener(controllerSaveProducts);
        viewToolBar.getPrioritize().addActionListener(controllerPrioritizeProducts);
        viewToolBar.getAddConstraint().addActionListener(controllerDisplayEditConstraints);
        viewToolBar.getRemoveConstraint().addActionListener(controllerRemoveConstraint);
        setJMenuBar(viewMenuBar);
        featureModelChooser = new JFileChooser();
        featureModelChooser.setMultiSelectionEnabled(false);
        featureModelChooser.setAcceptAllFileFilterUsed(false);
        featureModelChooser.addChoosableFileFilter(featureModelFileFilter);
        featureModelChooser.setDialogTitle(FILE_CHOOSER_FEATURE_MODEL_TITLE);
        productsChooser = new JFileChooser();
        productsChooser.setMultiSelectionEnabled(false);
        productsChooser.setAcceptAllFileFilterUsed(false);
        productsChooser.setDialogTitle(FILE_CHOOSER_PRODUCTS_TITLE);
        productsSaver = new JFileChooser();
        productsChooser.setMultiSelectionEnabled(false);
        productsChooser.setAcceptAllFileFilterUsed(false);
        productsSaver.setDialogTitle(FILE_SAVER_PRODUCTS_TITLE);
        ViewPLEDGE.this.add(viewStatusBar, BorderLayout.SOUTH);
        content = new JPanel(new BorderLayout());
//        content.add(viewConstraints, BorderLayout.EAST);
//        content.add(viewFeatures, BorderLayout.CENTER);
//        content.add(viewFeatureModelInformation, BorderLayout.NORTH);
//        content.add(viewProducts, BorderLayout.SOUTH);
        viewProducts.setVisible(false);
        add(viewToolBar, BorderLayout.PAGE_START);
        background = new JPanel(new GridLayout(1, 1));
        background.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(172, 168, 153)));
        background.setBackground(Color.white);
        background.add(new JLabel(new ImageIcon(BACKGROUND_URL)), BorderLayout.CENTER);
        add(background, BorderLayout.CENTER);
//        add(content, BorderLayout.CENTER);
//        viewFeatureModelInformation.setVisible(false);
//        viewConstraints.setVisible(false);
//        viewFeatures.setVisible(false);
        setSize(D_WIDTH, D_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(getParent());
        setVisible(true);


    }
    
    public void displayCoverage(String cov){
        JOptionPane.showMessageDialog(this, cov, COVERAGE_TITLE, JOptionPane.INFORMATION_MESSAGE);
    }

    public void displayDocumentation() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                viewDocumentation.setVisible(true);
            }
        });
    }

    public boolean getQuitConfirmation() {
        return JOptionPane.showConfirmDialog(this, QUIT_MESSAGE, QUIT_TITLE, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    public File displayFileChooser(FileType fileType) {

        switch (fileType) {
            case FEATURE_MODEL:
                if (featureModelChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    return featureModelChooser.getSelectedFile();
                }
                break;
            case PRODUCTS:
                if (productsChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    return productsChooser.getSelectedFile();
                }
                break;
        }


        return null;
    }

    public File displayFileSaver() {


        if (productsSaver.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            return productsSaver.getSelectedFile();
        }


        return null;
    }

    @Override
    public void update(Observable o, final Object arg) {
        Runnable code = new Runnable() {

            @Override
            public void run() {
                if (arg != null) {
                    remove(background);
                    add(content, BorderLayout.CENTER);
                    viewStatusBar.addSeparator();
                    if (model.getSolver() == null) {
                        viewFeatureModelInformation.setVisible(false);
                        viewConstraints.setVisible(false);
                        viewFeatures.setVisible(false);
                        content.add(viewProducts, BorderLayout.CENTER);
                    } else {
                        content.add(viewConstraints, BorderLayout.EAST);
                        content.add(viewFeatures, BorderLayout.CENTER);
                        content.add(viewFeatureModelInformation, BorderLayout.NORTH);
                        content.add(viewProducts, BorderLayout.SOUTH);
                        viewFeatureModelInformation.setVisible(true);
                        viewConstraints.setVisible(true);
                        viewFeatures.setVisible(true);
                    }

                    validate();
                    SwingUtilities.updateComponentTreeUI(ViewPLEDGE.this);

                }

                viewProducts.setVisible(model.getProducts() != null);
            }
        };
        if (SwingUtilities.isEventDispatchThread()) {
            code.run();
        } else {
            SwingUtilities.invokeLater(code);
        }
    }

    public void displayViewAboutWindow(final boolean display) {
        Runnable code = new Runnable() {

            @Override
            public void run() {
                viewAboutWindow.setVisible(display);
            }
        };
        if (SwingUtilities.isEventDispatchThread()) {
            code.run();
        } else {
            SwingUtilities.invokeLater(code);
        }
    }
    
    public void displayViewEditConstraints(final boolean display) {
        Runnable code = new Runnable() {

            @Override
            public void run() {
                viewEditConstraints.setVisible(display);
            }
        };
        if (SwingUtilities.isEventDispatchThread()) {
            code.run();
        } else {
            SwingUtilities.invokeLater(code);
        }
    }

    public void displayViewConfigurationGeneration(final boolean display) {
        Runnable code = new Runnable() {

            @Override
            public void run() {
                viewConfigurationGeneration.setVisible(display);
            }
        };
        if (SwingUtilities.isEventDispatchThread()) {
            code.run();
        } else {
            SwingUtilities.invokeLater(code);
        }
    }

    public void displayError(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }
}
