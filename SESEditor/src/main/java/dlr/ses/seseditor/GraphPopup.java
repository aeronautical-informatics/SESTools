/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dlr.ses.seseditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

/**
 * <h1>GraphPopup</h1>
 * <p>
 * This class implements right click action of the mouse on the graphical editor
 * panel. It initiates mouse actions related to various elements node addition
 * on the panel.
 * </p>
 * 
 * @author Bikash Chandra Karmokar
 * @version 1.0
 *
 */
class GraphPopup extends JPopupMenu {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GraphPopup(int x, int y) {
		JMenuItem itemEntity = new JMenuItem("Add Entity");
		JMenuItem itemSpec = new JMenuItem("Add Specialization");
		JMenuItem itemAspect = new JMenuItem("Add Aspect");
		JMenuItem itemMultiAspect = new JMenuItem("Add MultiAspect");

		itemEntity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				SESEditor.jtreeTograph.addNodeFromGraphPopup("Entity", x, y);

			}
		});

		itemSpec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				SESEditor.jtreeTograph.addNodeFromGraphPopup("Spec", x, y);

			}
		});

		itemAspect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				SESEditor.jtreeTograph.addNodeFromGraphPopup("Dec", x, y);

			}
		});

		itemMultiAspect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				SESEditor.jtreeTograph.addNodeFromGraphPopup("MAsp", x, y);

			}
		});

		add(itemEntity);
		add(new JSeparator());
		add(itemSpec);
		add(new JSeparator());
		add(itemAspect);
		add(new JSeparator());
		add(itemMultiAspect);

	}
}
