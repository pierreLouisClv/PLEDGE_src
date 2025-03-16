/*
 * Author : Christopher Henard (christopher.henard@uni.lu)
 * Date : 20/08/2013
 * Copyright 2013 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import pledge.core.ModelPLEDGE;

/**
 *
 * @author Christopher Henard
 */
public class ViewEditConstraints extends JDialog {

    private JPanel content, close;
    private JLabel icon, authors;
    private JButton closeButton;
    private static final int D_WIDTH = 550, D_HEIGHT = 250;

    private static final String TITLE = "Insert a constraint";


    public ViewEditConstraints(ModelPLEDGE model, ViewPLEDGE viewPLEDGE) {
        super(viewPLEDGE, TITLE);
        content = new JPanel(new BorderLayout());

        closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        close = new JPanel();
        close.add(closeButton);
        content.add(close, BorderLayout.SOUTH);
        add(content);
        setSize(new Dimension(D_WIDTH, D_HEIGHT));
        setResizable(false);
        setLocationRelativeTo(getParent());
    }

    public JButton getCloseButton() {
        return closeButton;
    }
}

