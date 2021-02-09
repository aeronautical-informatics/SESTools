//help taken from
//https://stackoverflow.com/questions/30191880/how-to-set-jtree-node-icon-dynamically?rq=1
package dlr.ses.core;

/*
 * This code is based on an example provided by Richard Stanford,
 * --Bikash Karmokar
 */

import dlr.ses.peseditor.PESEditor;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.Component;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author BIKASH
 */
class CustomIconRenderer extends DefaultTreeCellRenderer {

    Icon entityIcon;
    Icon specIcon;
    Icon maspIcon;
    Icon aspIcon;

    Path path = Paths.get("").toAbsolutePath();
    String repFslas = path.toString().replace("\\", "/");

    public CustomIconRenderer() { // throws MalformedURLException
        System.err.println(PESEditor.class.getClassLoader().getResource("images/en.png"));
        entityIcon = new ImageIcon(PESEditor.class.getClassLoader().getResource("images/en.png"));
        specIcon = new ImageIcon(PESEditor.class.getClassLoader().getResource("images/sp.png"));
        maspIcon = new ImageIcon(PESEditor.class.getClassLoader().getResource("images/ma.png"));
        aspIcon = new ImageIcon(PESEditor.class.getClassLoader().getResource("images/as16.png")); // as
    }

    @Override
    public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean sel,
                                                  final boolean expanded, final boolean leaf, final int row,
                                                  final boolean hasFocus) {

        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        Object nodeObj = ((DefaultMutableTreeNode) value).getUserObject();
        String nodeName = nodeObj.toString();
        // System.out.println("s: " + s);

        if ((nodeName != null) && (!nodeName.trim().isEmpty())) {

            if (nodeName.endsWith("Spec")) {
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
