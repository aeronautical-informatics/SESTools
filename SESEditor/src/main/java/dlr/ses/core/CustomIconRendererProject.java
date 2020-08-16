package dlr.ses.core;

import java.awt.Component;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import dlr.ses.seseditor.SESEditor;

/**
 * <h1>CustomIconRendererProject</h1>
 * <p>
 * To change the icon of the project window tree specific icons are used. This
 * class is used to set the folder icon and xml file icon for the project files.
 * </p>
 * 
 * @author Bikash Chandra Karmokar
 * @version 1.0
 *
 */
public class CustomIconRendererProject extends DefaultTreeCellRenderer {
	Icon moduleIcon;
	Icon xmlIcon;

	Path path = Paths.get("").toAbsolutePath();
	String repFslas = path.toString().replace("\\", "/");

	public CustomIconRendererProject() {// throws MalformedURLException
		moduleIcon = new ImageIcon(SESEditor.class.getClassLoader().getResource("images/folder164.png"));// 164 is perfect
		xmlIcon = new ImageIcon(SESEditor.class.getClassLoader().getResource("images/projtreeleaf.png"));
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf,
			int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		Object nodeObj = ((DefaultMutableTreeNode) value).getUserObject();
		String nodeName = nodeObj.toString();
		if ((nodeName != null) && (!nodeName.trim().isEmpty())) {
			if (nodeName.endsWith("Module")) {
				setIcon(moduleIcon);
			} else if (nodeName.endsWith("xml")) {
				setIcon(xmlIcon);
			} else {
				setIcon(moduleIcon);
			}
		}
		return this;
	}

}
