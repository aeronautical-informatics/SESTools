package dlr.ses.core;

import dlr.ses.peseditor.PESEditor;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.Component;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CustomIconRendererProject extends DefaultTreeCellRenderer {
    Icon moduleIcon;
    Icon xmlIcon;

    Path path = Paths.get("").toAbsolutePath();
    String repFslas = path.toString().replace("\\", "/");

    public CustomIconRendererProject() { // throws MalformedURLException
        moduleIcon = new ImageIcon(
                PESEditor.class.getClassLoader().getResource("images/folder164.png")); //164 is perfect
        xmlIcon = new ImageIcon(PESEditor.class.getClassLoader().getResource("images/projtreeleaf.png"));
    }

    public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean sel,
                                                  final boolean expanded, final boolean leaf, final int row,
                                                  final boolean hasFocus) {

        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        Object nodeObj = ((DefaultMutableTreeNode) value).getUserObject();
        String nodeName = nodeObj.toString();
        // System.out.println("s: " + s);

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
