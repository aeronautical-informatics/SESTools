package dlr.ses.core;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import dlr.ses.peseditor.JtreeToGraph;
import dlr.ses.peseditor.PESEditor;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

public class DynamicTree extends JPanel implements MouseListener {

    public static Multimap<TreePath, String> varMap = ArrayListMultimap.create();
    public static Multimap<TreePath, String> varMapTransfer = ArrayListMultimap.create();
    public static Multimap<TreePath, String> constraintsList = ArrayListMultimap.create();
    public static

    Variable scenarioVariable = new Variable();
    public static String projectFileName; // ="outputgraphxml";
    public DefaultMutableTreeNode rootNode;
    public UndoableTreeModel treeModel; // public DefaultTreeModel treeModel;
    public JTree tree;
    public Toolkit toolkit = Toolkit.getDefaultToolkit();
    public File ssdFile =
            new File(PESEditor.fileLocation + "/" + PESEditor.projName + "/" + projectFileName + ".xml");
    public File ssdFileVar =
            new File(PESEditor.fileLocation + "/" + PESEditor.projName + "/" + projectFileName + ".ssdvar");
    public File ssdFileCon =
            new File(PESEditor.fileLocation + "/" + PESEditor.projName + "/" + projectFileName + ".ssdcon");
    public File ssdFileFlag =
            new File(PESEditor.fileLocation + "/" + PESEditor.projName + "/" + projectFileName + ".ssdflag");
    int clickControl = 0;
    // ssdFileGraph -check this in jtreetograph

    @SuppressWarnings("unchecked")
    public DynamicTree() { // throws MalformedURLException
        super(new GridLayout(1, 0));

        // rootNode = new DefaultMutableTreeNode("Scenario");
        // treeModel = new DefaultTreeModel(rootNode);
        // treeModel.addTreeModelListener(new MyTreeModelListener());
        // --------------------

        // projectFileName=DynamicTreeDemo.newFileName;
        System.out.println("projectFileName: " + projectFileName);

        if (ssdFile.exists()) {
            try {
                if (PESEditor.openClicked == 1) {
                    File ssdFile = new File(PESEditor.openFileName + ".ssd");
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ssdFile));
                    // treeModel = (DefaultTreeModel) ois.readObject();
                    ois.close();

                    // for variable
                    // File ssdFileVar = new File(DynamicTreeDemo.openFileName + ".ssdvar");
                    // ObjectInputStream oisvar = new ObjectInputStream(new
                    // FileInputStream(ssdFileVar));
                    // varMap = (Multimap<String, String>) oisvar.readObject();
                    // oisvar.close();

                } else {
                    /*
                     * ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ssdFile));
                     * treeModel = (DefaultTreeModel) ois.readObject();
                     *
                     * ois.close();
                     */
                    // for variable
                    if (ssdFileVar.exists() && ssdFileCon.exists() && ssdFileFlag.exists()) {
                        ObjectInputStream oisvar = new ObjectInputStream(new FileInputStream(ssdFileVar));
                        varMap = (Multimap<TreePath, String>) oisvar.readObject();
                        oisvar.close();

                        ObjectInputStream oiscon = new ObjectInputStream(new FileInputStream(ssdFileCon));
                        constraintsList = (Multimap<TreePath, String>) oiscon.readObject();
                        oiscon.close();

                    }

                    if (ssdFileFlag.exists()) {

                        ObjectInputStream oisflag = new ObjectInputStream(new FileInputStream(ssdFileFlag));
                        FlagVariables flags;
                        flags = (FlagVariables) oisflag.readObject();
                        JtreeToGraph.nodeNumber = flags.nodeNumber;
                        System.out.println("flags.nodeNumber:" + flags.nodeNumber);
                        JtreeToGraph.uniformityNodeNumber = flags.uniformityNodeNumber;
                        oisflag.close();

                    }

                    // restoring jtree from xml
                    XmlJTree myTree = new XmlJTree(
                            PESEditor.fileLocation + "/" + PESEditor.projName + "/" + projectFileName
                            + ".xml");
                    treeModel = myTree.dtModel;
                    treeModel.addTreeModelListener(new MyTreeModelListener());
                }

            } catch (IOException err) {
                err.printStackTrace();
            } catch (ClassNotFoundException err) {
                err.printStackTrace();
            }
        } else {
            rootNode = new DefaultMutableTreeNode("Thing");
            treeModel = new UndoableTreeModel(rootNode); // treeModel = new DefaultTreeModel(rootNode);
            treeModel.addTreeModelListener(new MyTreeModelListener());

        }
        // --------------------

        tree = new JTree(treeModel);
        tree.setEditable(true);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);
        tree.addMouseListener(this);

        // for expanding the tree on starting
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }

        // //for close-open icon
        // DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer)
        // tree.getCellRenderer();
        // Icon closedIcon = new ImageIcon("images/delete.png");
        // Icon openIcon = new ImageIcon("images/delete.png");
        // Icon leafIcon = new ImageIcon("images/add.png");
        // renderer.setClosedIcon(closedIcon);
        // renderer.setOpenIcon(openIcon);
        // renderer.setLeafIcon(leafIcon);
        // default icon
        Path path = Paths.get("").toAbsolutePath();
        String repFslas = path.toString().replace("\\", "/");

        Icon entityIcon = new ImageIcon(repFslas + "/dlr/resources/images/en.png");

        CustomIconRenderer customIconRenderer = new CustomIconRenderer();
        tree.setCellRenderer(customIconRenderer);
        customIconRenderer.setLeafIcon(entityIcon);
        tree.setCellRenderer(new CustomIconRenderer());
        tree.setCellRenderer(customIconRenderer);

        // for cursor change
        tree.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int x = (int) e.getPoint().getX();
                int y = (int) e.getPoint().getY();
                TreePath path = tree.getPathForLocation(x, y);
                if (path == null) {
                    tree.setCursor(Cursor.getDefaultCursor());
                    clickControl = 0;
                } else {
                    tree.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    clickControl = 1;

                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tree);
        add(scrollPane);

    }

    public void importExistingProject(String filename, String path) {// not using have to delete
        // restoring jtree from xml
        XmlJTree myTree = new XmlJTree(path + "/" + "TestMain.xml");
        treeModel = myTree.dtModel;
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
        treeModel.addTreeModelListener(new MyTreeModelListener());
        treeModel.reload(root);

        // tree.setModel(new DefaultTreeModel(root)); //because of this line after
        // opening only child node is allowing insertion
        tree.setModel(treeModel);

        // tree.setModel(new DefaultTreeModel(root));

        // for expanding the tree on starting
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }

        System.out.println(treeModel.getRoot());
    }

    public void openExistingProject(String filename, String oldProjectTreeProjectName) {
        // restoring jtree from xml
        XmlJTree myTree =
                new XmlJTree(PESEditor.fileLocation + "/" + PESEditor.projName + "/" + filename + ".xml");
        treeModel = myTree.dtModel;
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
        treeModel.addTreeModelListener(new MyTreeModelListener());
        treeModel.reload(root);

        // tree.setModel(new DefaultTreeModel(root)); //because of this line after
        // opening only child node is allowing insertion
        tree.setModel(treeModel);

        // tree.setModel(new DefaultTreeModel(root));

        // for expanding the tree on starting
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }

        System.out.println(treeModel.getRoot());

        // System.out.println("oldProjectTreeProjectName: "+oldProjectTreeProjectName);

        String newProjectName = filename;
        // this is similar to new-------------------------------------------------
        PESEditor.projName = newProjectName;
        JtreeToGraph.newFileName = newProjectName;
        JtreeToGraph.projectFileNameGraph = newProjectName;

        PESEditor.jtreeTograph.ssdFileGraph = new File(
                PESEditor.fileLocation + "/" + PESEditor.projName + "/" + newProjectName + "Graph.xml");
        PESEditor.treePanel.ssdFile =
                new File(PESEditor.fileLocation + "/" + PESEditor.projName + "/" + newProjectName + ".xml");
        // DynamicTreeDemo.treePanel.ssdFileVar = new
        // File(DynamicTreeDemo.fileLocation+"/"+DynamicTreeDemo.projName+"/" +
        // newProjectName + ".ssdvar");
        // DynamicTreeDemo.treePanel.ssdFileCon = new
        // File(DynamicTreeDemo.fileLocation+"/"+DynamicTreeDemo.projName+"/" +
        // newProjectName + ".ssdcon");

        try {

            ObjectInputStream oisvar;

            oisvar = new ObjectInputStream(new FileInputStream(
                    PESEditor.fileLocation + "/" + PESEditor.projName + "/" + newProjectName + ".ssdvar"));
            varMap = (Multimap<TreePath, String>) oisvar.readObject();
            oisvar.close();

            ObjectInputStream oiscon = new ObjectInputStream(new FileInputStream(
                    PESEditor.fileLocation + "/" + PESEditor.projName + "/" + newProjectName + ".ssdcon"));
            constraintsList = (Multimap<TreePath, String>) oiscon.readObject();
            oiscon.close();

            if (ssdFileFlag.exists()) {
                ObjectInputStream oisflag = new ObjectInputStream(new FileInputStream(
                        PESEditor.fileLocation + "/" + PESEditor.projName + "/" + newProjectName
                        + ".ssdflag"));
                FlagVariables flags = new FlagVariables();
                flags = (FlagVariables) oisflag.readObject();
                System.out.println("flags.nodeNumber:" + flags.nodeNumber);
                JtreeToGraph.uniformityNodeNumber = flags.uniformityNodeNumber;
                oisflag.close();
            }

            // }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ProjectTree.projectName = newProjectName;

        PESEditor.projectPanel.changeCurrentProjectFileName(newProjectName, oldProjectTreeProjectName);
        PESEditor.treePanel.addUndoableEditListener(new EditorUndoableEditListener());

        // this is similar to new end------------------------------------------------

        Variable.setNullToAllRows();
        Constraint.setNullToAllRows();

    }

    // for expanding the tree after changes
    public void expandTree() {
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
    }

    public void clear() {
        rootNode.removeAllChildren();
        treeModel.reload();
    }

    // -----------------------------------------------------------------------------
    // for undo redo
    public void addUndoableEditListener(UndoableEditListener l) {
        treeModel.addUndoableEditListener(l);
    }

    public void removeUndoableEditListener(UndoableEditListener l) {
        treeModel.removeUndoableEditListener(l);
    }

    // -----------------------------------------------------------------------------

    public void removeCurrentNode() {
        TreePath currentSelection = tree.getSelectionPath();
        if (currentSelection != null) {
            DefaultMutableTreeNode currentNode =
                    (DefaultMutableTreeNode) (currentSelection.getLastPathComponent());
            MutableTreeNode parent = (MutableTreeNode) (currentNode.getParent());
            if (parent != null) {

                // ---------------------------------------------------------------------
                // have to call function to delete node from graph here
                TreeNode[] nodes = currentNode.getPath();
                String[] nodesToSelectedNode = new String[nodes.length];

                for (int i = 0; i < nodes.length; i++) {

                    nodesToSelectedNode[i] = (nodes[i].toString());

                }

                treeModel.removeNodeFromParent(
                        currentNode); // if this line is above the currentNode.getPath() then it
                // will not work because before cullect path nodes the
                // variable is deleted then

                PESEditor.jtreeTograph.deleteNodeWithTree(nodesToSelectedNode);

                // ---------------------------------------------------------------------

                return;
            }
        }

        // Either there was no selection, or the root was selected.
        toolkit.beep();
    }

    public TreePath getTreeNodePath(String[] nodePath) {
        TreePath parentPath;
        FindByName obj = new FindByName(tree, nodePath);
        parentPath = FindByName.path;
        return parentPath;
    }

    public void removeCurrentNodeWithGraphDelete(TreePath currentSelection) {
        if (currentSelection != null) {
            DefaultMutableTreeNode currentNode =
                    (DefaultMutableTreeNode) (currentSelection.getLastPathComponent());
            MutableTreeNode parent = (MutableTreeNode) (currentNode.getParent());
            if (parent != null) {
                treeModel.removeNodeFromParent(currentNode);
                return;
            }
        }
        // Either there was no selection, or the root was selected.
        toolkit.beep();

    }

    public DefaultMutableTreeNode addObjectWIthGraphAddition(Object child, String[] nodePath) {

        DefaultMutableTreeNode parentNode = null;
        TreePath parentPath;

        // String[] modPath = {"Scenario","ScenarioDec","Events"};
        FindByName obj = new FindByName(tree, nodePath);
        parentPath = FindByName.path;

        if (parentPath == null) {
            parentNode = rootNode;
        } else {
            parentNode = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
            // System.out.println(parentNode.toString());
        }

        return addObject(parentNode, child, true);
    }

    /**
     * Add child to the currently selected node.
     */
    public DefaultMutableTreeNode addObject(Object child) {
        DefaultMutableTreeNode parentNode = null;
        TreePath parentPath = tree.getSelectionPath();
        // for testing
        // String[] modPath = {"Scenario","ScenarioDec","Events"};
        // FindByName obj = new FindByName(tree,modPath);
        // parentPath = FindByName.path;
        // end of testing
        if (parentPath == null) {
            parentNode = rootNode;
        } else {
            parentNode = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
            // System.out.println(parentNode.toString());
        }

        return addObject(parentNode, child, true);
    }

    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, Object child) {
        return addObject(parent, child, false);
    }

    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, Object child,
                                            boolean shouldBeVisible) {
        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);

        if (parent == null) {
            parent = rootNode;
        }

        // It is key to invoke this on the TreeModel, and NOT
        // DefaultMutableTreeNode
        treeModel.insertNodeInto(childNode, parent, parent.getChildCount());
        // System.out.println(childNode.toString());
        // System.out.println(parent.toString());
        // System.out.println(parent.getChildCount());

        // Make sure the user can see the lovely new node.
        if (shouldBeVisible) {

            tree.scrollPathToVisible(new TreePath(childNode.getPath()));
            // tree.expandPath(new TreePath(parent.getPath()));

        }

        return childNode;
    }

    public void saveTreeModel() {
        try {
            // ObjectOutputStream oos = new ObjectOutputStream(new
            // FileOutputStream(ssdFile));
            // oos.writeObject(treeModel);
            // oos.close();

            // for variable
            ObjectOutputStream oosvar = new ObjectOutputStream(new FileOutputStream(ssdFileVar));
            oosvar.writeObject(varMap);
            oosvar.close();

            // for constraint
            ObjectOutputStream ooscons = new ObjectOutputStream(new FileOutputStream(ssdFileCon));
            ooscons.writeObject(constraintsList);
            ooscons.close();

            // for constraint
            ObjectOutputStream oosflag = new ObjectOutputStream(new FileOutputStream(ssdFileFlag));
            FlagVariables flags = new FlagVariables();
            flags.nodeNumber = JtreeToGraph.nodeNumber;
            System.out.println("flags.nodeNumber:save:" + flags.nodeNumber);
            flags.uniformityNodeNumber = JtreeToGraph.uniformityNodeNumber;
            oosflag.writeObject(flags);
            oosflag.close();

            // for graph
            // ObjectOutputStream oosgraph = new ObjectOutputStream(new
            // FileOutputStream(JtreeToGraph.ssdFileGraph));
            // oosgraph.writeObject(JtreeToGraph.graph);
            // oosgraph.close();

            PESEditor.jtreeTograph.saveGraph();

        } catch (IOException err) {

            err.printStackTrace();
        }
    }

    public void saveTreeModelAs(String fileName) {
        fileName = fileName + ".ssd";
        try {
            // ObjectOutputStream oos = new ObjectOutputStream(new
            // FileOutputStream(fileName));
            // oos.writeObject(treeModel);
            // oos.close();

            // for variable
            fileName = fileName + "var";
            ObjectOutputStream oosvar = new ObjectOutputStream(new FileOutputStream(fileName));
            oosvar.writeObject(varMap);
            oosvar.close();

            // for graph
            // fileName = fileName + "graph";
            // ObjectOutputStream oosgraph = new ObjectOutputStream(new
            // FileOutputStream(fileName));
            // oosgraph.writeObject(JtreeToGraph.graph);
            // oosgraph.close();

        } catch (IOException err) {

            err.printStackTrace();
        }
    }

    public void openTreeModel(File fileName) {
        if (fileName.exists()) {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
                // treeModel = (DefaultTreeModel) ois.readObject();
                treeModel.reload();
                ois.close();
            } catch (IOException err) {
                err.printStackTrace();
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); //To
        // change body of generated methods, choose Tools | Templates.
    }

    public void mousePressed(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); //To
        // change body of generated methods, choose Tools | Templates.
    }

    public void mouseReleased(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); //To
        // change body of generated methods, choose Tools | Templates.
        // JOptionPane.showMessageDialog(tree, "Test");
        //final TreePopup treePopup = new TreePopup(tree);

        // if (clickControl == 1) {
        // if (e.isPopupTrigger()) {
        // treePopup.show(e.getComponent(), e.getX(), e.getY());
        // }
        // }
        if (e.getButton() == MouseEvent.BUTTON1) {
            TreePath currentSelection = tree.getSelectionPath();
            if (currentSelection != null) {
                DefaultMutableTreeNode currentNode =
                        (DefaultMutableTreeNode) (currentSelection.getLastPathComponent());
                // System.out.println("mouseReleased(MouseEvent e):" + currentSelection);
                // scenarioVariable.name.setText(currentNode.toString());
                // scenarioVariable.setEntityVariables(currentNode.toString());
                // scenarioVariable.setEntityName("bks");
                // System.out.println("Left button clicked");

                // -------------------------------------------------------
                TreeNode[] nodes = currentNode.getPath();
                // int len1 = nodes.length;

                Collection<String> nodeVariables = DynamicTree.varMap.get(currentSelection);
                // int len2 = nodeVariables.size();

                // int size = len1 + len2;
                String[] nodesToSelectedNode = new String[100];

                int c = 0;
                String variables = "";
                int b = 0;

                for (TreePath key : varMap.keySet()) {
                    int a = 0;

                    for (String value : varMap.get(key)) {
                        // System.out.printf("%s %s\n", key, value);
                        DefaultMutableTreeNode currentNode2 =
                                (DefaultMutableTreeNode) (key.getLastPathComponent());
                        // System.out.println(currentNode2.toString());
                        TreeNode[] nodes2 = currentNode2.getPath();

                        if (nodes.length == nodes2.length) {
                            for (int i = 0; i < nodes.length; i++) {

                                if (nodes[i].toString().equals(nodes2[i].toString())) {
                                    a = 1;

                                } else {
                                    a = 0;
                                }

                            }
                        }

                        if (a == 1) {
                            nodesToSelectedNode[b] = value;
                            b++;
                        }
                    }

                }

                // for (String value : nodeVariables) {
                // nodesToSelectedNode[c] = value;
                // b++;
                // }

                PESEditor.jtreeTograph.scenarioVariable
                        .showNodeValuesInTable(currentNode.toString(), nodesToSelectedNode);
                // -------------------------------------------------------

            }

        } else if (e.getButton() == MouseEvent.BUTTON3) {
            // System.out.println("Right button clicked");
            if (clickControl == 1) {
                if (e.isPopupTrigger()) {
                    //treePopup.show(e.getComponent(), e.getX(), e.getY());
                }
            }

        }

    }

    public void refreshVariableTable(TreePath treePathForVariable) {
        DefaultMutableTreeNode currentNode =
                (DefaultMutableTreeNode) (treePathForVariable.getLastPathComponent());
        // TreeNode[] nodes2 = currentNode.getPath();

        // Collection<String> nodeVariables =
        // DynamicTree.varMap.get(treePathForVariable);

        // String variables = "";
        // for (String value : nodeVariables) {
        // variables = variables + value + "\n";
        // }
        // System.out.println(variables);

        // -------------------------------------------------------
        TreeNode[] nodes = currentNode.getPath();
        // int len1 = nodes.length;

        Collection<String> nodeVariables = DynamicTree.varMap.get(treePathForVariable);
        // int len2 = nodeVariables.size();

        // int size = len1 + len2;
        String[] nodesToSelectedNode = new String[100];
        int c = 0;
        String variables = "";
        int b = 0;

        for (TreePath key : DynamicTree.varMap.keySet()) {
            int a = 0;

            for (String value : DynamicTree.varMap.get(key)) {
                // System.out.printf("%s %s\n", key, value);
                DefaultMutableTreeNode currentNode2 = (DefaultMutableTreeNode) (key.getLastPathComponent());
                // System.out.println(currentNode2.toString());
                TreeNode[] nodes2 = currentNode2.getPath();

                if (nodes.length == nodes2.length) {
                    for (int i = 0; i < nodes.length; i++) {

                        if (nodes[i].toString().equals(nodes2[i].toString())) {
                            a = 1;

                        } else {
                            a = 0;
                        }

                    }
                }

                if (a == 1) {
                    nodesToSelectedNode[b] = value;
                    b++;
                }
            }

        }

        // for(String value : nodeVariables) {
        // nodesToSelectedNode[c]=value;
        // b++;
        // }

        PESEditor.jtreeTograph.scenarioVariable
                .showNodeValuesInTable(currentNode.toString(), nodesToSelectedNode);
    }

    public void showConstraintsInTable(TreePath treePathForVariable) {

        DefaultMutableTreeNode currentNode =
                (DefaultMutableTreeNode) (treePathForVariable.getLastPathComponent());

        // -------------------------------------------------------
        TreeNode[] nodes = currentNode.getPath();

        // int size = len1 + len2;
        String[] nodesToSelectedNode = new String[100];

        int b = 0;

        for (TreePath key : DynamicTree.constraintsList.keySet()) {
            int a = 0;

            for (String value : DynamicTree.constraintsList.get(key)) {
                // System.out.printf("%s %s\n", key, value);
                DefaultMutableTreeNode currentNode2 = (DefaultMutableTreeNode) (key.getLastPathComponent());
                // System.out.println(currentNode2.toString());
                TreeNode[] nodes2 = currentNode2.getPath();

                if (nodes.length == nodes2.length) {
                    for (int i = 0; i < nodes.length; i++) {

                        if (nodes[i].toString().equals(nodes2[i].toString())) {
                            a = 1;

                        } else {
                            a = 0;
                        }

                    }
                }

                if (a == 1) {
                    nodesToSelectedNode[b] = value;
                    b++;
                }
            }

        }

        // for(String value : nodeVariables) {
        // nodesToSelectedNode[c]=value;
        // b++;
        // }

        PESEditor.scenarioConstraint.showConstraintsInTable(nodesToSelectedNode);
    }

    public void mouseEntered(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); //To
        // change body of generated methods, choose Tools | Templates.
    }

    public void mouseExited(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); //To
        // change body of generated methods, choose Tools | Templates.
    }

    class MyTreeModelListener implements TreeModelListener {

        public void treeNodesChanged(TreeModelEvent e) {
            DefaultMutableTreeNode node;
            node = (DefaultMutableTreeNode) (e.getTreePath().getLastPathComponent());

            /*
             * If the event lists children, then the changed node is the child of the node
             * we've already gotten. Otherwise, the changed node and the specified node are
             * the same.
             */
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
