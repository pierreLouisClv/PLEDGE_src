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
package pledge.gui.controllers;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import pledge.core.ModelPLEDGE;
import pledge.gui.views.ViewPLEDGE;

/**
 *
 * @author Christopher Henard
 */
public class ControllerRemoveConstraint extends AbstractAction {

    private ModelPLEDGE model;

    public ControllerRemoveConstraint(ModelPLEDGE model) {
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final Thread t = new Thread(new Runnable() {

            public void run() {
                try {
                    model.removeConstraint(model.getCurrentConstraint());
                } catch (Exception ex) {
                    Logger.getLogger(ControllerRemoveConstraint.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
        t.start();
    }
}
