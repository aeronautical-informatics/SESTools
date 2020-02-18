package dlr.ses.core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;

/**
 * <h1>GraphWindow</h1>
 * <p>
 * This class is used as the drawing panel. SES Model is drawn in this window
 * panel. This class inherits JInternalFrame to display drawing panel within
 * another window.
 * </p>
 * 
 * @author Bikash Chandra Karmokar
 * @version 1.0
 *
 */
public class GraphWindow extends JInternalFrame {

	public GraphWindow() {
		super("Graphical View", false, // resizable
				false, // closable
				false, // maximizable
				false // iconifiable
		);

	}
}
