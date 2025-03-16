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

import java.awt.Dimension;
import java.net.URL;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

/**
 *
 * @author Christopher Henard
 */
public class ViewDocumentation  extends JFrame{
    
private static ViewDocumentation docFrame = null ;

    private static final String TITLE="PLEDGE v1 Documentation";
    private static final String START_PAGE="pledge/doc/index.html";
    private static final int D_WIDTH= 1000;
    private static final int D_HEIGHT=600;
    /** JTextPane placé dans un scrollPane */
    private ScrollHTML display ;

    /**
     * Constructeur privé (singleton).
     * Construit une fenêtre d'aide.
     */
    private ViewDocumentation() {
	super(TITLE);
	display = new ScrollHTML();
	display.setPreferredSize(new Dimension(D_WIDTH,D_HEIGHT));
	setContentPane(display);
	display.setPage(START_PAGE);
	pack();

    } 

    /**
     * Retourne l'instance unique de la fenêtre d'aide.
     *
     * @return l'instance unique de la fenêtre d'aide.
     */
    public static ViewDocumentation getInstance() {
	if (docFrame == null)
            return new ViewDocumentation();
	else
            return docFrame;
    } 
} 




/**
 * Classe interne représentant un JTextPane placé dans un JScrollPane.
 *
 */
class ScrollHTML extends JScrollPane {

    /** Text Pane */
    protected JTextPane textPane ;

    /** Construit le TextPane */
    public ScrollHTML() {
	super();
	textPane = new JTextPane();
	textPane.setEditable(false);
	textPane.setContentType("text/html");
	textPane.addHyperlinkListener(new Hyperactive());
	setViewportView(textPane);
    } 


    /**
     * Charge et affiche une page.
     * @param resourceName le nom de la ressource à utiliser
     */
    public void setPage(String resourceName) {
	try {
	    URL url = ClassLoader.getSystemResource(resourceName);
	    HTMLDocument htmlDocument = new HTMLDocument();
	    htmlDocument.setBase(url);
	    textPane.setPage(url);
	} catch (Exception exc) {

	}
    } 
} 



/**
 * Classe recopiée dans la documentation Sun de javax.swing.JEditorPane
 * Permet de rendre les liens "actifs".
 */
class Hyperactive implements HyperlinkListener {

    /**
     * Met à jour les liens.
     *
     * @param e un événement sur un lien.
     */
    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
	if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
	    JEditorPane pane = (JEditorPane) e.getSource();
		if (e instanceof HTMLFrameHyperlinkEvent) {
		    HTMLFrameHyperlinkEvent  evt = (HTMLFrameHyperlinkEvent)e;
		    HTMLDocument doc = (HTMLDocument)pane.getDocument();
		    doc.processHTMLFrameHyperlinkEvent(evt);
		} else {
		    try {
			pane.setPage(e.getURL());
		    } catch (Throwable t) {
			t.printStackTrace();
		    }
		}
	}
    }
} 
