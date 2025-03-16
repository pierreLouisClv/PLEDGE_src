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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import pledge.gui.views.ViewPLEDGE;

/**
 *
 * @author Christopher Henard
 */
public class ViewAboutWindow extends JDialog {

    private JPanel content, close;
    private JLabel icon, authors;
    private JButton closeButton;
    private static final int D_WIDTH = 550, D_HEIGHT = 250;
    private final URL AUTHORS_URL = getClass().getResource("icons/authors.png");
    private static final String TITLE = "About PLEDGE...";
    private static final String AUTHORS = "<html><center><font size=+1><b>" + ViewPLEDGE.TITLE + "<b></font><br/><br/> <font size =-1>Christopher Henard<br/><br/> version 1.1 - August 2013<font size =-1></center></html>";


    public ViewAboutWindow(ViewPLEDGE viewPLEDGE) {
        super(viewPLEDGE, TITLE);
        content = new JPanel(new BorderLayout());
        if (AUTHORS_URL != null) {
            icon = new JLabel(new ImageIcon(AUTHORS_URL));
        }
        authors = new JLabel(AUTHORS, SwingConstants.RIGHT);
        closeButton = new JButton("Close");
        content.setBackground(Color.white);
        content.add(icon, BorderLayout.WEST);
        content.add(authors, BorderLayout.CENTER);
        close = new JPanel();
        close.setBackground(Color.white);
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
