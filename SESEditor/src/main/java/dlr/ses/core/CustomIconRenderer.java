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
 * <h1>CustomIconRenderer</h1>
 * <p>
 * To identify System Entity Structure elements uniquely different icons are
 * used. CustomIconRenderer class is used to set the icon of the elements based
 * on the name of the elements.
 * </p>
 * 
 * @author Bikash Chandra Karmokar
 * @version 1.0
 *
 */
class CustomIconRenderer extends DefaultTreeCellRenderer {

	Icon entityIcon;
	Icon specIcon;
	Icon maspIcon;
	Icon aspIcon;

	Path path = Paths.get("").toAbsolutePath();
	String repFslas = path.toString().replace("\\", "/");

	public CustomIconRenderer() {// throws MalformedURLException
		System.err.println(SESEditor.class.getClassLoader().getResource("."));
		entityIcon = new ImageIcon(SESEditor.class.getClassLoader().getResource("images/en.png"));
		specIcon = new ImageIcon(SESEditor.class.getClassLoader().getResource("images/sp.png"));
		maspIcon = new ImageIcon(SESEditor.class.getClassLoader().getResource("images/ma.png"));
		aspIcon = new ImageIcon(SESEditor.class.getClassLoader().getResource("images/as16.png"));// as
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf,
			int row, boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

		Object nodeObj = ((DefaultMutableTreeNode) value).getUserObject();
		String nodeName = nodeObj.toString();
		// System.out.println("s: " + s);

		if ((nodeName != null) && (!nodeName.trim().isEmpty())) {

			if (nodeName.startsWith("~")) {
				setIcon(null);

			} else if (nodeName.endsWith("Spec")) {
				setIcon(specIcon);

			} else if (nodeName.endsWith("MAsp")) {
				setIcon(maspIcon);

			} else if (nodeName.endsWith("Dec")) {
				setIcon(aspIcon);

			} else {
				setIcon(entityIcon);
			}

		}
		return this;
	}
}
