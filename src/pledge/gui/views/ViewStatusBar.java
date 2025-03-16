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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import pledge.core.ModelPLEDGE;

/**
 *
 * @author Christopher Henard
 */
public class ViewStatusBar extends JPanel implements Observer {

    private static final String INIT_MESSAGE = "No file loaded";
    private static final String END_MESSAGE = "Completed";
    private ModelPLEDGE model;
    private JLabel currentAction, globalAction;
    private JProgressBar progressBar;
    private JPanel action, status;
    private JSeparator separator;

    public ViewStatusBar(ModelPLEDGE model) {
        super(new BorderLayout());
        this.model = model;
        model.addObserver(this);
        globalAction = new JLabel(INIT_MESSAGE);
        currentAction = new JLabel();
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        action = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.LINE_END;
        c.insets = new Insets(2, 5, 2, 5);
        action.add(currentAction, c);
        c.gridx = 1;
        action.add(progressBar, c);
        status = new JPanel(new GridBagLayout());
        c.gridx = c.gridy = 0;
        c.insets = new Insets(5, 5, 4, 5);
        status.add(globalAction, c);
        separator = new JSeparator();
        action.setVisible(false);
        
        add(action, BorderLayout.EAST);
        add(status, BorderLayout.WEST);

    }
    
    public void addSeparator(){
        add(separator, BorderLayout.NORTH);
    }
    
    public void removeSeparator(){
        remove(separator);
    }

    @Override
    public void update(Observable o, Object arg) {
        Runnable code = new Runnable() {

            @Override
            public void run() {

                if (model.isRunning()) {
                    action.setVisible(true);
                    globalAction.setText(model.getGlobalAction());
                    currentAction.setText(model.getCurrentAction());
                    if (!model.isIndeterminate()) {
                        progressBar.setIndeterminate(false);
                        progressBar.setStringPainted(true);
                        int progress = model.getProgress();
                        progressBar.setValue(progress);
                    }
                    else
                    {
                        progressBar.setIndeterminate(true);
                        progressBar.setStringPainted(false);
                    }


                } else {
                    action.setVisible(false);
                    globalAction.setText(END_MESSAGE);
                    progressBar.setIndeterminate(true);
                    progressBar.setStringPainted(false);
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
