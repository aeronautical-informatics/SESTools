package dlr.ses.seseditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTree;

/**
 * <h1>ProjectTreePopup</h1>
 * <p>
 * This class implements the JTree node pop up action of the project tree. Only
 * delete action is implemented here. The main module can't be deleted. But
 * added module file name can be deleted using this delete functionality.
 * </p>
 * 
 * @author Bikash Chandra Karmokar
 * @version 1.0
 *
 */
public class ProjectTreePopup extends JPopupMenu {

	public ProjectTreePopup(JTree tree) {
		JMenuItem itemDelete = new JMenuItem("Delete Node");

		itemDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// System.out.println("Node Deleted");
				SESEditor.popUpActionDeleteProjectTree();
			}
		});

		add(itemDelete);
		// add(new JSeparator());
		// add(itemDeleteAll);

	}
}
