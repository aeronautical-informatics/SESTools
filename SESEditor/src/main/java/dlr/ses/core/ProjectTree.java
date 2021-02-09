package dlr.ses.core;

import com.google.common.io.Files;
import dlr.ses.seseditor.JtreeToGraph;
import dlr.ses.seseditor.ProjectTreePopup;
import dlr.ses.seseditor.SESEditor;
import dlr.ses.utils.XmlUtils;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Enumeration;
import java.util.Scanner;

/**
 * <h1>ProjectTree</h1>
 * <p>
 * This class is used to implements to show project file structure as JTree
 * format. Current project name and its added modules are displayed with
 * different icon in this tree.
 * </p>
 *
 * @author Bikash Chandra Karmokar
 * @version 1.0
 */
public class ProjectTree extends JPanel implements MouseListener {

    public static String projectName;// = "Project"; //ProjectTree.projectName

    public DefaultMutableTreeNode projectRoot, mainModule, addedModule;
    public DefaultMutableTreeNode projectXmlFile;
    public DefaultTreeModel projectTreeModel;
    public JTree projectTree;
    public Toolkit toolkit = Toolkit.getDefaultToolkit();
    int clickControl = 0;
    File ssdFileProject = new File(
            SESEditor.projName + "/" + JtreeToGraph.newFileName +
                    "Project.xml");

    public ProjectTree() {
        super(new GridLayout(1, 0));

        if (ssdFileProject.exists()) {
            // restoring jtree from xml
            XmlJTree myTree = new XmlJTree(
                    SESEditor.projName + "/" + JtreeToGraph.newFileName +
                            "Project.xml");
            projectTreeModel = myTree.dtModel;
            projectTreeModel
                    .addTreeModelListener(new ProjectTreeModelListener());
            projectTree = new JTree(projectTreeModel);

            // have to initialize addedModule here. //BUG fixed :)
            projectRoot =
                    (DefaultMutableTreeNode) projectTree.getModel().getRoot();
            Enumeration enumeration = projectRoot.breadthFirstEnumeration();
            while (enumeration.hasMoreElements()) {

                DefaultMutableTreeNode node =
                        (DefaultMutableTreeNode) enumeration.nextElement();
                if ("AddedModule".equals(node.getUserObject().toString())) {

                    addedModule = node;
                }
            }

        } else {
            projectName = JtreeToGraph.newFileName + ".xml";
            projectRoot = new DefaultMutableTreeNode("Project");
            mainModule = new DefaultMutableTreeNode("MainModule");
            addedModule = new DefaultMutableTreeNode("AddedModule");
            projectXmlFile = new DefaultMutableTreeNode(projectName);
            projectTreeModel = new DefaultTreeModel(projectRoot);
            projectTreeModel
                    .addTreeModelListener(new ProjectTreeModelListener());
            projectTree = new JTree(projectTreeModel);
            projectTreeModel.insertNodeInto(mainModule, projectRoot,
                    projectRoot.getChildCount());
            projectTreeModel.insertNodeInto(addedModule, projectRoot,
                    projectRoot.getChildCount());
            projectTreeModel.insertNodeInto(projectXmlFile, mainModule,
                    mainModule.getChildCount());

        }

        projectTree.setEditable(true);
        projectTree.getSelectionModel()
                .setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        projectTree.setShowsRootHandles(true);
        projectTree.addMouseListener(this);

        // for cursor change
        projectTree.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int x = (int) e.getPoint().getX();
                int y = (int) e.getPoint().getY();
                TreePath path = projectTree.getPathForLocation(x, y);
                if (path == null) {
                    projectTree.setCursor(Cursor.getDefaultCursor());
                    clickControl = 0;
                } else {
                    projectTree.setCursor(
                            Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    clickControl = 1;

                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(projectTree);
        add(scrollPane);

        // for project icon
        // DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer)
        // projectTree.getCellRenderer();
        /*
         * Icon closedIcon = new ImageIcon("images/delete.png"); Icon openIcon = new
         * ImageIcon("images/delete.png");
         */
        // Icon xmlIcon = new ImageIcon("images/projtreeleaf.png");
        // renderer.setClosedIcon(closedIcon);
        // renderer.setOpenIcon(openIcon);
        // renderer.setLeafIcon(xmlIcon);
        // renderer.setIcon(openIcon);
        // default icon

        // Path path = Paths.get("").toAbsolutePath();
        // String repFslas = path.toString().replace("\\", "/");

        // Icon xmlIcon = new ImageIcon("images/projtreeleaf.png");

        CustomIconRendererProject customIconRenderer =
                new CustomIconRendererProject();
        // projectTree.setCellRenderer(customIconRenderer);
        // customIconRenderer.setLeafIcon(xmlIcon);
        projectTree.setCellRenderer(new CustomIconRendererProject());
        projectTree.setCellRenderer(customIconRenderer);

        // for expanding the tree in the beginning
        expandTree();

    }

    public static void showXSDtoXMLViewer(String fileName) {

        Scanner in = null;
        try {
            in = new Scanner(new File(SESEditor.projName + "/" + fileName));

        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }

        StringBuilder xmlcontent = new StringBuilder();

        while (in.hasNext()) {

            String line = in.nextLine();

            xmlcontent.append(line);
            xmlcontent.append("\n");

        }

        XmlUtils.xmlview.textArea.setText(xmlcontent.toString());
        // System.out.println(xmlcontent);

        in.close();

    }

    public void expandTree() {
        for (int i = 0; i < projectTree.getRowCount(); i++) {
            projectTree.expandRow(i);
        }
    }

    public void addModueFile(String fileName) {
        // fileName = fileName.replaceFirst("[.][^.]+$", ""); //regular expression
        fileName = Files.getNameWithoutExtension(
                fileName);// using google guava deleting file extension
        projectXmlFile = new DefaultMutableTreeNode(fileName + ".xml");
        projectTreeModel.insertNodeInto(projectXmlFile, addedModule,
                addedModule.getChildCount());

        // no need to delete and again add .xml. its just for learning purpose i added
        // this. have to remove later.

        // BUG
        // after opening a saved project this function is not working have to check.
        // :) already fixed it addding some code in constructor.

        // for expanding the tree in the beginning
        expandTree();
    }

    public void removeCurrentNode() {
        TreePath currentSelection = projectTree.getSelectionPath();
        if (currentSelection != null) {
            DefaultMutableTreeNode currentNode =
                    (DefaultMutableTreeNode) (currentSelection
                            .getLastPathComponent());
            MutableTreeNode parent =
                    (MutableTreeNode) (currentNode.getParent());
            if (parent != null) {

                if (currentNode.toString().equals("MainModule") ||
                        currentNode.toString().equals("AddedModule")
                        || currentNode.toString()
                        .equals(SESEditor.projName + ".xml")) {
                    toolkit.beep();
                } else {
                    projectTreeModel.removeNodeFromParent(currentNode);
                }

                return;
            }
        }

    }

    public void changeCurrentProjectFileName(String fileName,
                                             String oldProjectTreeProjectName) {

        // have to initialize addedModule here. //BUG fixed :)
        projectRoot = (DefaultMutableTreeNode) projectTree.getModel().getRoot();
        Enumeration enumeration = projectRoot.breadthFirstEnumeration();

        while (enumeration.hasMoreElements()) {

            DefaultMutableTreeNode node =
                    (DefaultMutableTreeNode) enumeration.nextElement();
            if ((oldProjectTreeProjectName + ".xml")
                    .equals(node.getUserObject().toString())) {
                projectTreeModel.removeNodeFromParent(node);
            }
        }

        // have to initialize addedModule here. //BUG fixed :)
        projectRoot = (DefaultMutableTreeNode) projectTree.getModel().getRoot();
        enumeration = projectRoot.breadthFirstEnumeration();

        while (enumeration.hasMoreElements()) {

            DefaultMutableTreeNode node =
                    (DefaultMutableTreeNode) enumeration.nextElement();
            if ("MainModule".equals(node.getUserObject().toString())) {
                mainModule = node;
            }
        }

        projectXmlFile = new DefaultMutableTreeNode(fileName + ".xml");
        projectTreeModel.insertNodeInto(projectXmlFile, mainModule,
                mainModule.getChildCount());
        expandTree();

    }

    @Override
    public void mouseClicked(MouseEvent arg0) {

        if (arg0.getClickCount() == 2) // double click
        {
            String name = projectTree.getSelectionPath().getLastPathComponent()
                    .toString();
            // System.out.println(name);
            showXSDtoXMLViewer(name);
            XmlUtils.xmlview.setTitle(name);

        }
        // System.out.println("Test tree click");
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

        final ProjectTreePopup treePopup = new ProjectTreePopup(projectTree);

        if (e.getButton() == MouseEvent.BUTTON3) {
            if (clickControl == 1) {
                if (e.isPopupTrigger()) {
                    treePopup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }

    }

    // Internal Class for Project Tree Handling
    class ProjectTreeModelListener implements TreeModelListener {

        public void treeNodesChanged(TreeModelEvent e) {
            DefaultMutableTreeNode node;
            node = (DefaultMutableTreeNode) (e.getTreePath()
                    .getLastPathComponent());

            int index = e.getChildIndices()[0];
            node = (DefaultMutableTreeNode) (node.getChildAt(index));

            // System.out.println("The user has finished editing the node.");
            // System.out.println("New value: " + node.getUserObject());
        }

        public void treeNodesInserted(TreeModelEvent e) {
            // System.out.println("New Node inserted");
        }

        public void treeNodesRemoved(TreeModelEvent e) {
            // System.out.println("One Node removed");
        }

        public void treeStructureChanged(TreeModelEvent e) {
        }
    }

}
