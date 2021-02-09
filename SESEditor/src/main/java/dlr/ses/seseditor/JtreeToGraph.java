package dlr.ses.seseditor;

import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxUndoManager;
import com.mxgraph.util.mxUndoableEdit;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
import dlr.ses.core.DynamicTree;
import dlr.ses.core.FileConvertion;
import dlr.ses.core.Variable;
import dlr.ses.utils.PanelSplitor;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <h1>JtreeToGraph</h1>
 * <p>
 * This class is the base of graphical drawing editor panel. JGraphX is used
 * here to implement the edge-vertex connection. Functionalities of node
 * addition, deletion, variable addition, deletion, constraint addition deletion
 * etc all are implemented in this class. Also synchronization of the drawn tree
 * in the graphic panel and the JTree are also done here in this class.
 * </p>
 *
 * @author Bikash Chandra Karmokar
 * @version 1.0
 */
public class JtreeToGraph {

    public static int nodeNumber = 1;
    public static int addNodeClick = 0;
    public static int deleteNodeClick = 0;
    public static int checkEdgeNodeClick = 0;
    public static mxGraph graph;

    // ProjectPane Related
    public static String newFileName = SESEditor.projName;
    public static String projectFileNameGraph = newFileName;
    public static int nodeAddCollitionRemove = 0;
    public static mxCell selectedNodeCellForVariableUpdate = null;
    public static mxCell lastSelectedNodeCellTreeAndGraph = null;
    /**
     * To add node automatically from console window using text command (add node
     * Scenario) <code>currentSelectedCell</code> variable keep track of the last
     * selected cell using mouse.
     */
    public static mxCell currentSelectedCell = null;
    public static int uniformityNodeNumber = 0;
    public static boolean uniformityNodeSynControl = false;
    public static ArrayList<String> pathForRefNode = new ArrayList<String>();
    // checkRootConnectivity
    public static boolean connectedToRoot = false;
    // for testing of adding multiple node at the same time
    // for variable
    public static ArrayList<String> pathToRoot = new ArrayList<String>();
    // for subtree
    public static ArrayList<String> pathToRootSubTree = new ArrayList<String>();
    public static mxCell firstAddedCellForSubTreeDeletion = null;
    public static int undoForSubTreeCount = 0;
    static Object parent;
    // below block of saveModuleFromCurrentModel is using for drawn subtree
    // synchronisation with
    // jtree
    // ----------------------------------------------------------------------------------------------------------------------------------------
    static String parentName = "";
    static int treeSyncNodeCount = 0;
    static int firstAddedCellForSubTree = 0;
    static String subtreeCheckLabel = null;

    // for variable addition start----------------------------------
    static mxCell subtreeCheckCell = null;
    static mxCell subtreeSyncCell = null;
    static String addedCellNameSync = null;
    public File ssdFileGraph = new File(
            SESEditor.fileLocation + "/" + SESEditor.projName + "/" + projectFileNameGraph + "Graph.xml");
    // for variable addition ----------------------------------end
    public mxGraphComponent graphComponent = null;
    // for constraint addition ----------------------------------end
    public mxUndoManager undoManager;
    // for constraint addition ----------------------------------end
    public mxCell lastAddedCell = null;
    public Variable scenarioVariable = new Variable();
    public Object rootNode;
    Path pathForRuntimeFiles = Paths.get("").toAbsolutePath();
    String repFslas = pathForRuntimeFiles.toString().replace("\\", "/");
    ArrayList<String> path = new ArrayList<String>();
    // selectedType is using in below function: addVariableFromGraphPopup
    String selectedType = "byte";

    // public boolean isConnectedToRoot(mxCell cell) {
    // Object[] incoming = graph.getIncomingEdges(cell);
    // if (incoming.length != 0) {
    // Object source = graph.getModel().getTerminal(incoming[incoming.length - 1],
    // true);
    // mxCell sourceCell = (mxCell) source;
    //
    // if (sourceCell.getId().equals("rootnode")) {
    // return true;
    // }
    // isConnectedToRoot(sourceCell);
    // }
    //
    // return false;
    // }
    String selectedVariable = "";
    // used in next function
    String[] nodesToSelectedNode;
    int totalNodes;
    int nodeReached;
    mxCell lastNodeInPath;
    String[] nodeNamesForGraphSync = new String[100];
    ArrayList<mxCell> deletableChildNodes = new ArrayList<mxCell>();

    public static void nodeToRootPathVar(mxCell cell) {
        Object[] incoming = graph.getIncomingEdges(cell);
        // getTerminal(java.lang.Object edge, boolean isSource)
        if (incoming.length != 0) {
            Object source = graph.getModel().getTerminal(incoming[incoming.length - 1], true);
            mxCell sourceCell = (mxCell) source;
            // System.out.println(sourceCell.getValue());
            pathToRoot.add((String) sourceCell.getValue());
            nodeToRootPathVar(sourceCell);
        }
    }

    public static boolean saveToXMLFileWithoutIndent(Document doc, String filePath)
            throws TransformerException {
        if (doc != null) {
            try {
                javax.xml.transform.TransformerFactory tFactory =
                        javax.xml.transform.TransformerFactory.newInstance();
                // javax.xml.transform.stream.StreamSource();
                javax.xml.transform.Transformer transformer = tFactory.newTransformer();
                javax.xml.transform.dom.DOMSource source = new javax.xml.transform.dom.DOMSource(doc);
                javax.xml.transform.stream.StreamResult result =
                        new javax.xml.transform.stream.StreamResult(new File(filePath));
                // transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                // transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
                // "yes");
                transformer.setOutputProperty(OutputKeys.INDENT,
                        "yes"); // if no then output will be one line. so indent
                // With 0 spacing is best option
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount",
                        "0"); // indent-amount now 0
                // so it will remain in
                // one line

                transformer.transform(source, result);
                return true;

            } catch (TransformerFactoryConfigurationError ex) {
                return false;
            }

        } else {
            return false;
        }
    }

    public static boolean saveToXMLFile(Document doc, String filePath) throws TransformerException {
        if (doc != null) {
            try {
                javax.xml.transform.TransformerFactory tFactory =
                        javax.xml.transform.TransformerFactory.newInstance();
                // javax.xml.transform.stream.StreamSource();
                javax.xml.transform.Transformer transformer = tFactory.newTransformer();
                javax.xml.transform.dom.DOMSource source = new javax.xml.transform.dom.DOMSource(doc);
                javax.xml.transform.stream.StreamResult result =
                        new javax.xml.transform.stream.StreamResult(new File(filePath));
                // transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                // transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
                // "yes");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "0");

                transformer.transform(source, result);
                return true;

            } catch (TransformerFactoryConfigurationError ex) {
                return false;
            }

        } else {
            return false;
        }
    }

    /**
     * Modifying the generated graphxml.xml output by removing <start> and </start>
     * tag from the file and making single line element <node1/> two double lines
     * <node1> </node1>
     */
    public static void modifyXmlOutput() {
        // System.out.println("Modify called");

        PrintWriter f0 = null;
        try {
            // f0 = new PrintWriter(new FileWriter("runtimefiles/outputgraphxml.xml"));
            f0 = new PrintWriter(new FileWriter(
                    SESEditor.fileLocation + "/" + SESEditor.projName + "/" + projectFileNameGraph + ".xml"));
            // System.out.println("output file generated");
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        Scanner in = null;
        try {
            in = new Scanner(new File(SESEditor.fileLocation + "/" + SESEditor.projName + "/graphxml.xml"));
            // System.out.println("my read complete");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        while (in.hasNext()) { // Iterates each line in the file
            // System.out.println("inside while");
            String line = in.nextLine();
            // System.out.println("Line: " + line);

            if (line.endsWith("start>")) {
                continue;
            } else if (line.endsWith("/>")) {
                // System.out.println(line);
                String result = line.replaceAll("[</>]", "");
                // String result = line.replaceAll("[\\<\\/\\>]","");
                result = result.replaceAll("\\s+", "");
                String line1 = "<" + result + ">";
                String line2 = "</" + result + ">";
                f0.println(line1);
                f0.println(line2);

            } else {
                f0.println(line);
            }
        }

        in.close();
        f0.close();
    }

    /**
     * Under the MVC model, the JGraph class is a controller, GraphModel is a model,
     * and GraphUI is a view.
     *
     * @param frame
     */
    public void createGraph(JInternalFrame frame) {

        graph = new mxGraph();
        undoManager = new mxUndoManager();

        // setting default edge color
        graph.getStylesheet().getDefaultEdgeStyle()
                .put(mxConstants.STYLE_STROKECOLOR, mxUtils.getHexColorString(Color.BLACK));
        graph.getStylesheet().getDefaultEdgeStyle()
                .put(mxConstants.STYLE_FONTCOLOR, mxUtils.getHexColorString(Color.BLACK));
        graph.setCellsEditable(false);

        // creating new style
        mxStylesheet stylesheet = graph.getStylesheet();
        Hashtable<String, Object> style = new Hashtable<String, Object>();
        style.put(mxConstants.STYLE_IMAGE, mxConstants.SHAPE_IMAGE);
        style.put(mxConstants.STYLE_FONTCOLOR, "#774400");
        stylesheet.putCellStyle("Aspect", style);

        // problem: xml can not save this styles.
        // Solution: have to add it after graph initialization.

        Hashtable<String, Object> styleEdge = new Hashtable<String, Object>();
        styleEdge.put(mxConstants.STYLE_EDGE, mxConstants.SHAPE_LINE);
        styleEdge.put(mxConstants.STYLE_FONTCOLOR, "#774400");
        stylesheet.putCellStyle("Edge", styleEdge);

        Hashtable<String, Object> entity = new Hashtable<String, Object>();
        entity.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
        entity.put(mxConstants.STYLE_FONTCOLOR, mxUtils.getHexColorString(Color.BLACK));
        entity.put(mxConstants.STYLE_FILLCOLOR, "#FFFFFF");
        entity.put(mxConstants.STYLE_ROUNDED, true);
        entity.put(mxConstants.STYLE_LABEL_POSITION, mxConstants.ALIGN_BOTTOM);
        entity.put(mxConstants.STYLE_STROKECOLOR, mxUtils.getHexColorString(Color.BLACK));
        entity.put(mxConstants.STYLE_STROKEWIDTH, 2);
        entity.put(mxConstants.STYLE_EDGE, mxUtils.getHexColorString(Color.BLACK));
        stylesheet.putCellStyle("Entity", entity);

        Hashtable<String, Object> multiaspect = new Hashtable<String, Object>();
        multiaspect.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_IMAGE);
        multiaspect.put(mxConstants.STYLE_VERTICAL_LABEL_POSITION, mxConstants.ALIGN_TOP);
        multiaspect.put(mxConstants.STYLE_FONTCOLOR, mxUtils.getHexColorString(Color.BLACK));
        multiaspect.put(mxConstants.STYLE_IMAGE,
                SESEditor.class.getClassLoader().getResource("images/multi.png"));
        stylesheet.putCellStyle("Multiaspect", multiaspect);

        Hashtable<String, Object> aspect = new Hashtable<String, Object>();
        aspect.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_IMAGE);
        aspect.put(mxConstants.STYLE_VERTICAL_LABEL_POSITION, mxConstants.ALIGN_TOP);
        aspect.put(mxConstants.STYLE_FONTCOLOR, mxUtils.getHexColorString(Color.BLACK));
        aspect.put(mxConstants.STYLE_IMAGE,
                SESEditor.class.getClassLoader().getResource("images/aspect.png"));
        stylesheet.putCellStyle("Aspect", aspect);

        Hashtable<String, Object> specialization = new Hashtable<String, Object>();
        specialization.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_IMAGE);
        specialization.put(mxConstants.STYLE_VERTICAL_LABEL_POSITION, mxConstants.ALIGN_TOP);
        specialization.put(mxConstants.STYLE_FONTCOLOR, mxUtils.getHexColorString(Color.BLACK));
        specialization.put(mxConstants.STYLE_IMAGE,
                SESEditor.class.getClassLoader().getResource("images/spec.png"));
        specialization.put(mxConstants.STYLE_EDGE, mxUtils.getHexColorString(Color.BLACK));
        stylesheet.putCellStyle("Specialization", specialization);

        parent = graph.getDefaultParent();

        if (ssdFileGraph.exists()) {
            graph.getModel().beginUpdate();
            try {
                // use "org.w3c.dom.Document" not swing Document
                System.err.println(
                        SESEditor.fileLocation + "/" + SESEditor.projName + "/" + projectFileNameGraph
                        + "Graph.xml");
                Document xml = mxXmlUtils.parseXml(mxUtils.readFile(
                        SESEditor.fileLocation + "/" + SESEditor.projName + "/" + projectFileNameGraph
                        + "Graph.xml"));

                mxCodec codec = new mxCodec(xml);
                codec.decode(xml.getDocumentElement(), graph.getModel());
                parent = graph.getDefaultParent();

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                graph.getModel().endUpdate();
            }

        } else {
            graph.getModel().beginUpdate();
            try {
                rootNode = graph.insertVertex(parent, "rootnode", "Thing", 515, 20, 80, 30, "Entity");

                Object hideV =
                        graph.insertVertex(parent, "hideV", "End of Canvas", 0, 50000, 80, 30, "Entity");
                mxCell hidenodeV = (mxCell) hideV;
                // hidenode.setVisible(false);

                Object hideH =
                        graph.insertVertex(parent, "hideH", "End of Canvas", 50000, 0, 80, 30, "Entity");
                mxCell hidenodeH = (mxCell) hideH;
                // hidenode.setVisible(false);

            } finally {
                graph.getModel().endUpdate();
            }
        }

        graphComponent = new mxGraphComponent(graph);
        // graphComponent.getViewport().setOpaque(true);
        graphComponent.getViewport().setBackground(Color.WHITE);

        // undo redo settings
        mxEventSource.mxIEventListener listener = new mxEventSource.mxIEventListener() {

            @Override
            public void invoke(Object sender, mxEventObject evt) {
                // this condition is added to control subtree undo addition
                if (!SESEditor.undoControlForSubTree == true) {
                    undoManager.undoableEditHappened((mxUndoableEdit) evt.getProperty("edit"));
                }

            }
        };

        // for undo redo
        graph.getModel().addListener(mxEvent.UNDO, listener);
        graph.getView().addListener(mxEvent.UNDO, listener);

        frame.getContentPane().add(graphComponent);

        // for edge connection event
        graph.addListener(mxEvent.CELL_CONNECTED, new mxIEventListener() {
            @Override
            public void invoke(Object sender, mxEventObject evt) {
                mxCell connectionCell = (mxCell) evt.getProperty("edge");
                boolean source = (Boolean) evt.getProperty("source");

                // System.out.println(source);
                // System.out.println("Source Node: " + connectionCell.getSource().getValue());

                lastAddedCell = (mxCell) connectionCell
                        .getSource(); // if there is no terminal cell then have to handle

            }
        });

        // for keyboard delete action
        /*
         * new mxKeyboardHandler( graphComponent); //it is out of control now
         *
         * graph.addListener(mxEvent.CELLS_REMOVED, new mxIEventListener() {
         *
         * @Override public void invoke(Object sender, mxEventObject evt) {
         *
         *
         * } });
         */

        graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent e) {

                Object cell = graphComponent.getCellAt(e.getX(), e.getY());

                currentSelectedCell = (mxCell) cell; // this for console use

                if (e.getButton() == MouseEvent.BUTTON1) {

                    if (cell == null) {

                        // if (addNodeClick == 1) {
                        String ob = Integer.toString(nodeNumber);
                        String nodeName = "node" + ob;
                        int a = e.getX();
                        int b = e.getY();
                        // System.out.println(a + ", " + b);

                        graph.getModel().beginUpdate();
                        try {
                            if (SESEditor.nodeAddDetector.equals("entity")) {
                                Object obj =
                                        graph.insertVertex(parent, null, nodeName, e.getX() - 40, e.getY(),
                                                80, 30, "Entity");
                                nodeNumber++;

                            } else if (SESEditor.nodeAddDetector.equals("aspect")) {
                                Object obj = graph.insertVertex(parent, null, nodeName + "Dec", e.getX() - 15,
                                        e.getY(), 30, 30, "Aspect");
                                nodeNumber++;

                            } else if (SESEditor.nodeAddDetector.equals("multiaspect")) {
                                Object obj =
                                        graph.insertVertex(parent, null, nodeName + "MAsp", e.getX() - 15,
                                                e.getY(), 30, 30, "Multiaspect");
                                nodeNumber++;

                            } else if (SESEditor.nodeAddDetector.equals("specialization")) {

                                Object obj =
                                        graph.insertVertex(parent, null, nodeName + "Spec", e.getX() - 15,
                                                e.getY(), 30, 30, "Specialization");
                                nodeNumber++;

                            }

                            // System.out.println(obj);

                        } finally {
                            graph.getModel().endUpdate();
                            // De-Selecting mouse selection from menu items
                            SESEditor.nodeAddDetector = "";
                        }

                    } else {
                        // this section is for showing variables of the selected node to the variable
                        // table
                        if (!SESEditor.nodeAddDetector.equals("delete")) {

                            Object showvar = graphComponent.getCellAt(e.getX(), e.getY());
                            mxCell varCell = (mxCell) showvar;
                            selectedNodeCellForVariableUpdate = varCell;

                            System.out.println(varCell.getId());

                            Object[] incom = graph.getIncomingEdges(varCell);

                            if (varCell.isVertex()) {

                                String selectedNode = (String) varCell.getValue();
                                pathToRoot.add((String) varCell.getValue());
                                nodeToRootPathVar(varCell);

                                String[] stringArray = pathToRoot.toArray(new String[0]);
                                ArrayList<String> pathToRootRev = new ArrayList<String>();

                                for (int i = stringArray.length - 1; i >= 0; i--) {
                                    pathToRootRev.add(stringArray[i]);
                                }

                                String[] stringArrayRev = pathToRootRev.toArray(new String[0]);

                                TreePath treePathForVariable = SESEditor.getTreeNodePath(stringArrayRev);
                                if (!(treePathForVariable == null)) {
                                    DefaultMutableTreeNode currentNode =
                                            (DefaultMutableTreeNode) (treePathForVariable
                                                                              .getLastPathComponent()); // if mouse not released then some condition

                                    // -------------------------------------------------------
                                    TreeNode[] nodes = currentNode.getPath();

                                    Collection<String> nodeVariables =
                                            DynamicTree.varMap.get(treePathForVariable);

                                    String[] nodesToSelectedNode = new String[100];
                                    int c = 0;
                                    String variables = "";
                                    int b = 0;

                                    for (TreePath key : DynamicTree.varMap.keySet()) {
                                        int a = 0;

                                        for (String value : DynamicTree.varMap.get(key)) {

                                            DefaultMutableTreeNode currentNode2 =
                                                    (DefaultMutableTreeNode) (key.getLastPathComponent());

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

                                    nodesToSelectedNode = Arrays.stream(nodesToSelectedNode)
                                            .filter(s -> (s != null && s.length() > 0))
                                            .toArray(String[]::new);

                                    Arrays.parallelSort(nodesToSelectedNode);

                                    SESEditor.jtreeTograph.scenarioVariable
                                            .showNodeValuesInTable(currentNode.toString(),
                                                    nodesToSelectedNode);
                                    // -------------------------------------------------------

                                    pathToRoot.clear();

                                    // for showing constraints in the table when selecting any node from graph;
                                    SESEditor.treePanel.showConstraintsInTable(treePathForVariable);

                                } else {
                                    pathToRoot.clear();
                                }

                            }
                        }

                        // double click handling
                        if (e.getClickCount() == 2) {
                            mxCell clikedCell = (mxCell) cell;
                            if (clikedCell.isVertex()) {

                            }
                        }

                    } // end of else from if cell==null

                    if (SESEditor.nodeAddDetector.equals("delete")) {
                        // this delete will not delete all the child node. becuase sometimes we need to
                        // delete
                        // only one node that time we can use this and for that we have to add another
                        // button for not synchronizing with jtree

                        Object delcell = graphComponent.getCellAt(e.getX(), e.getY());
                        final Toolkit toolkit = Toolkit.getDefaultToolkit();

                        if (delcell != null) {

                            mxCell deleteCell = (mxCell) delcell;

                            if (deleteCell.isEdge()) {
                                deleteEdgeFromGraphPopup(delcell);
                            } else {
                                deleteNodeFromGraphPopup(delcell);
                            }

                        }

                        // De-Selecting mouse selection from menu items
                        SESEditor.nodeAddDetector = "";

                    }

                } // button 1 end

                // right click events using pop up menu
                if (e.getButton() == MouseEvent.BUTTON3) {
                    // for fixing popup window while page is more thant monitor height
                    Point p = MouseInfo.getPointerInfo().getLocation();
                    int y = p.y;
                    int x = p.x;
                    int screenWidth = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;

                    if (x > screenWidth) {
                        x = x - PanelSplitor.dividerLocation - 10 - screenWidth;
                    } else {
                        x = x - PanelSplitor.dividerLocation - 10;
                    }
                    // end of that

                    // here position will be edge or vertex on that place
                    Object position = graphComponent.getCellAt(e.getX(), e.getY());

                    // checking if there is any vertex or edge
                    if (position != null) {
                        GraphCellPopUp graphCellPopup = new GraphCellPopUp(e.getX(), e.getY(), position);
                        if (e.isPopupTrigger()) {
                            graphCellPopup.show(graphComponent, x, y - 104);
                        }

                    } else {
                        GraphPopup graphPopup = new GraphPopup(e.getX(), e.getY());

                        if (e.isPopupTrigger()) {
                            graphPopup.show(graphComponent, x, y - 104);
                        }

                    }

                }

                // for the last added node
                callAfterEdgeConnectionComplete();

            }

            // mouse event 2

        });

    }

    public String rootNodeName() {
        Object[] cells = graph.getChildVertices(graph.getDefaultParent()); // getSelectionCells();

        mxCell rootcell = null;

        for (Object c : cells) {
            mxCell cell2 = (mxCell) c; // casting
            if (cell2.isVertex()) {
                String val = cell2.getId();
                // System.out.println(val);
                if (val.equals("rootnode")) {
                    rootcell = cell2;
                }

            }
        } // end of for

        return rootcell.getValue().toString();
    }

    public void rootToEndNodeVariable() {

        Object[] cells = graph.getChildVertices(graph.getDefaultParent()); // getSelectionCells();

        mxCell rootcell = null;

        for (Object c : cells) {
            mxCell cell2 = (mxCell) c;
            if (cell2.isVertex()) {
                String val = cell2.getId();
                if (val.equals("rootnode")) {
                    rootcell = cell2;
                }

            }
        }

        if (rootcell != null) {
            nextChildNodeForVariable(rootcell);
            rootToEndVariableAddition(rootcell);

        }

    }

    public void nextChildNodeForVariable(mxCell cell) {
        Object[] outgoing = graph.getOutgoingEdges(cell);

        if (outgoing.length > 0) {
            for (int i = 0; i < outgoing.length; i++) {
                Object targetCell = graph.getModel().getTerminal(outgoing[i], false);

                // for next call
                mxCell targetCell2 = (mxCell) targetCell;

                if (targetCell2.getId().startsWith("uniformity")) {
                    if (targetCell2.getId().endsWith("RefNode")) {
                        // finding and saving root to this node path
                        nodeToRootPath(targetCell2);

                        String[] stringArray = path.toArray(new String[0]);
                        ArrayList<String> pathRev = new ArrayList<String>();

                        for (int j = stringArray.length - 1; j >= 0; j--) {
                            pathRev.add(stringArray[j]);
                        }

                        String[] stringArrayRev = pathRev.toArray(new String[0]);

                        for (String x : stringArrayRev) {
                            System.out.println(x);
                        }

                        String cellName = targetCell2.getValue().toString();

                        FileConvertion fileConvertion = new FileConvertion();

                        fileConvertion.addingUniformityRefNodeToXML(stringArrayRev, cellName);

                        path.clear();

                    } else {
                        continue;
                    }

                } else {
                    rootToEndVariableAddition(targetCell2); // variable addition
                    rootToEndConstraintAddition(targetCell2); // constraint addition
                    nextChildNodeForVariable(targetCell2);
                }

            }
        }

    }

    public void rootToEndVariableAddition(mxCell varCell) {

        if (varCell.isVertex()) {

            pathToRoot.add((String) varCell.getValue());
            nodeToRootPathVar(varCell);

            String[] stringArray = pathToRoot.toArray(new String[0]);
            ArrayList<String> pathToRootRev = new ArrayList<String>();

            for (int i = stringArray.length - 1; i >= 0; i--) {
                pathToRootRev.add(stringArray[i]);
            }

            String[] stringArrayRev = pathToRootRev.toArray(new String[0]);

            TreePath treePathForVariable = SESEditor.getTreeNodePath(stringArrayRev);
            DefaultMutableTreeNode currentNode =
                    (DefaultMutableTreeNode) (treePathForVariable.getLastPathComponent());

            // -------------------------------------------------------
            TreeNode[] nodes = currentNode.getPath();

            String[] nodesToSelectedNode = new String[100];
            // int c = 0;
            String variables = "";
            int b = 0;

            for (TreePath key : DynamicTree.varMap.keySet()) {
                int a = 0;

                for (String value : DynamicTree.varMap.get(key)) {

                    DefaultMutableTreeNode currentNode2 =
                            (DefaultMutableTreeNode) (key.getLastPathComponent());

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

            // java 8 way null removal
            nodesToSelectedNode =
                    Arrays.stream(nodesToSelectedNode).filter(s -> (s != null && s.length() > 0))
                            .toArray(String[]::new);

            for (String value : nodesToSelectedNode) {
                variables = variables + "<" + value + "Var/>" + "\n";
            }

            // treePathForVariable
            FileConvertion fileConversion = new FileConvertion();
            fileConversion.variableAdditionToNode(treePathForVariable, variables);

            pathToRoot.clear();
        }
    }

    // start of constraint addition
    public void rootToEndConstraintAddition(mxCell varCell) {

        if (varCell.isVertex()) {

            String selectedNode = (String) varCell.getValue();

            pathToRoot.add((String) varCell.getValue());
            nodeToRootPathVar(varCell);

            String[] stringArray = pathToRoot.toArray(new String[0]);
            ArrayList<String> pathToRootRev = new ArrayList<String>();

            for (int i = stringArray.length - 1; i >= 0; i--) {
                pathToRootRev.add(stringArray[i]);
            }

            String[] stringArrayRev = pathToRootRev.toArray(new String[0]);

            TreePath treePathForVariable = SESEditor.getTreeNodePath(stringArrayRev);
            DefaultMutableTreeNode currentNode =
                    (DefaultMutableTreeNode) (treePathForVariable.getLastPathComponent());

            // -------------------------------------------------------
            TreeNode[] nodes = currentNode.getPath();

            String[] nodesToSelectedNode = new String[100];

            String variables = "";
            int b = 0;

            for (TreePath key : DynamicTree.constraintsList.keySet()) {
                int a = 0;

                for (String value : DynamicTree.constraintsList.get(key)) {

                    DefaultMutableTreeNode currentNode2 =
                            (DefaultMutableTreeNode) (key.getLastPathComponent());

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

            // java 8 way null removal
            nodesToSelectedNode =
                    Arrays.stream(nodesToSelectedNode).filter(s -> (s != null && s.length() > 0))
                            .toArray(String[]::new);

            for (String value : nodesToSelectedNode) {
                variables = variables + "<" + value + "Con/>" + "\n";
            }

            // treePathForVariable
            if (variables.length() > 8) {
                FileConvertion fileConversion = new FileConvertion();
                // fileConversion.constraintAdditionToNode(treePathForVariable, variables);
                fileConversion.constraintAdditionToNode(selectedNode,
                        variables); // sending treePath is correct way, the
                // above one. have to check.
            }

            pathToRoot.clear();
        }
    }
    // end of solving sequence problem--------------------------------------

    // start of constraint addition in SES
    public void rootToEndConstraintAdditionInSES(mxCell varCell) {

        if (varCell.isVertex()) {

            String selectedNode = (String) varCell.getValue();

            pathToRoot.add((String) varCell.getValue());
            nodeToRootPathVar(varCell);

            String[] stringArray = pathToRoot.toArray(new String[0]);
            ArrayList<String> pathToRootRev = new ArrayList<String>();

            for (int i = stringArray.length - 1; i >= 0; i--) {
                pathToRootRev.add(stringArray[i]);
            }

            String[] stringArrayRev = pathToRootRev.toArray(new String[0]);

            // for (int i = 0; i <stringArrayRev.length; i++) {
            // System.out.println(stringArrayRev[i]);
            // }

            TreePath treePathForVariable = SESEditor.getTreeNodePath(stringArrayRev);
            DefaultMutableTreeNode currentNode =
                    (DefaultMutableTreeNode) (treePathForVariable.getLastPathComponent());

            // -------------------------------------------------------
            TreeNode[] nodes = currentNode.getPath();

            Collection<String> nodeVariables = DynamicTree.constraintsList.get(treePathForVariable);

            String[] nodesToSelectedNode = new String[100];
            int c = 0;
            String variables = "";
            int b = 0;

            for (TreePath key : DynamicTree.constraintsList.keySet()) {
                int a = 0;

                for (String value : DynamicTree.constraintsList.get(key)) {
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

            // java 8 way null removal
            nodesToSelectedNode =
                    Arrays.stream(nodesToSelectedNode).filter(s -> (s != null && s.length() > 0))
                            .toArray(String[]::new);

            for (String value : nodesToSelectedNode) {
                variables = variables + "<" + value + "Con/>" + "\n";
            }
            // System.out.println(selectedNode + ": \n" + variables);
            // System.out.println("--------------------------------");

            // treePathForVariable
            if (variables.length() > 8) {
                FileConvertion fileConversion = new FileConvertion();
                fileConversion.constraintAdditionToNode(treePathForVariable, variables);

            }

            pathToRoot.clear();
        }
    }

    public void callAfterEdgeConnectionComplete() {
        mxCell addedCell = null;

        if (lastAddedCell != null && graph.getOutgoingEdges(lastAddedCell).length > 0) {

            Object[] outgoing = graph.getOutgoingEdges(lastAddedCell);
            Object targetCell = graph.getModel().getTerminal(outgoing[outgoing.length - 1], false);
            addedCell = (mxCell) targetCell;
            Object[] incoming = graph.getIncomingEdges(addedCell);

            Object lastAddedEdge = outgoing[outgoing.length - 1];
            mxCell lastEdge = (mxCell) lastAddedEdge;
            if (lastEdge.isEdge()) {
                try {
                    lastEdge.getTarget().getValue();
                } catch (Exception e) {
                    graph.removeCells(new Object[] {outgoing[outgoing.length - 1]});
                }
            }

            if (lastAddedCell.getId().startsWith("uniformity")) {
                graph.removeCells(new Object[] {outgoing[outgoing.length - 1]});

                // System.out.println("MaintainingUniformity");
                JOptionPane.showMessageDialog(SESEditor.framew,
                        "You can not add here. Add to the reference node.");

                String nameOfReferenceNode = lastAddedCell.getValue().toString();

                addedCell = null;
                lastAddedCell = null;
            }

            if (incoming.length > 1) {
                // System.out.println(addedCell.getValue() + " already has a parent node.");
                graph.removeCells(new Object[] {outgoing[outgoing.length - 1]});
                lastAddedCell = null;
            } else {

                if (addedCell != null) {
                    // System.out.println("lastAddedCell: " + lastAddedCell.getValue());

                    nodeToRootPath(addedCell);

                    mxCell cellParentCheck = lastAddedCell;
                    lastAddedCell = null;

                    // update jtree
                    String[] stringArray = path.toArray(new String[0]);
                    ArrayList<String> pathRev = new ArrayList<String>();

                    for (int i = stringArray.length - 1; i >= 0; i--) {
                        pathRev.add(stringArray[i]);
                    }

                    String[] stringArrayRev = pathRev.toArray(new String[0]);

                    // have to check if the source node is connected with any parent or not.
                    /*
                     * for (int i = stringArrayRev.length - 1; i >= 0; i--) {
                     * System.out.println(stringArrayRev[i]); }
                     */

                    if (cellParentCheck != null) {
                        if (cellParentCheck.getId()
                                .equals("rootnode")) {// by using checkRootConnectivity() i can omit
                            // this section and make it only on if else.
                            // have to check later
                            if (graph.getOutgoingEdges(addedCell).length > 0) {
                                // for cell who has child elements and added to some other parent as a child
                                // node. subtree addition
                                saveModuleFromCurrentModel(cellParentCheck);
                                SESEditor.undoControlForSubTree = true;
                                addModuleFromSubgraph(cellParentCheck);
                                deleteNodeFromGraphPopupForSubTree(firstAddedCellForSubTreeDeletion);
                                SESEditor.undoControlForSubTree = false;
                                firstAddedCellForSubTreeDeletion = null;
                                firstAddedCellForSubTree = 0;

                            } else {
                                SESEditor.addNodeWIthGraphAddition(addedCell.getValue().toString(),
                                        stringArrayRev);

                                // have to check subtree here
                            }
                        } else {// if not root node

                            checkRootConnectivity(cellParentCheck);

                            if (connectedToRoot) {
                                Object[] forParentCheck = graph.getIncomingEdges(cellParentCheck);
                                if (forParentCheck.length == 1) {

                                    if (graph.getOutgoingEdges(addedCell).length > 0) {
                                        // for cell who has child elements and added to some other parent as a child
                                        // node. subtree creation
                                        saveModuleFromCurrentModel(cellParentCheck);
                                        SESEditor.undoControlForSubTree = true;
                                        addModuleFromSubgraph(cellParentCheck);
                                        deleteNodeFromGraphPopupForSubTree(firstAddedCellForSubTreeDeletion);
                                        SESEditor.undoControlForSubTree = false;
                                        firstAddedCellForSubTreeDeletion = null;
                                        firstAddedCellForSubTree = 0;

                                    } else {
                                        SESEditor.addNodeWIthGraphAddition(addedCell.getValue().toString(),
                                                stringArrayRev);

                                        System.out.println("Tested syn now");
                                        System.out.println("cellParentCheck:" + cellParentCheck.getValue());
                                        System.out.println("addedCell:" + addedCell.getValue());
                                        // Synchronization with its child node while adding into main reference node
                                        checkSubtreeNodeForSync(cellParentCheck, addedCell);
                                        // have to check subtree here for adding node with same node
                                        checkSubtreeNode(addedCell);
                                    }
                                }
                            }

                            connectedToRoot = false;

                        }

                        cellParentCheck = null;
                    }

                    path.clear();
                }
            }

        }

        // System.out.println("connection completed");

    }

    public void addNodeIntoJtreeWithNewModuleAddition(mxCell lastAddedCell) {
        mxCell addedCell = null;
        // System.out.println("test called");
        if (lastAddedCell != null) {
            Object[] outgoing = graph.getOutgoingEdges(lastAddedCell);
            Object targetCell = graph.getModel().getTerminal(outgoing[outgoing.length - 1], false);
            addedCell = (mxCell) targetCell;

            if (addedCell != null) {
                // System.out.println(addedCell.getValue());

                nodeToRootPath(addedCell);
                lastAddedCell = null;

                // update jtree
                String[] stringArray = path.toArray(new String[0]);
                ArrayList<String> pathRev = new ArrayList<String>();

                for (int i = stringArray.length - 1; i >= 0; i--) {
                    pathRev.add(stringArray[i]);
                }

                String[] stringArrayRev = pathRev.toArray(new String[0]);

                SESEditor.addNodeWIthGraphAddition(addedCell.getValue().toString(), stringArrayRev);

                path.clear();
            }

        }

    }

    public void addNodeIntoJtreeForSubTree(mxCell lastAddedCell) {
        path.clear();
        mxCell addedCell = null;
        // System.out.println("test called");
        if (lastAddedCell != null) {
            Object[] outgoing = graph.getOutgoingEdges(lastAddedCell);
            Object targetCell = graph.getModel().getTerminal(outgoing[outgoing.length - 1], false);
            addedCell = (mxCell) targetCell;

            if (addedCell != null) {
                // System.out.println(addedCell.getValue());

                nodeToRootPath(addedCell);
                lastAddedCell = null;

                // update jtree
                String[] stringArray = path.toArray(new String[0]);
                ArrayList<String> pathRev = new ArrayList<String>();

                for (int i = stringArray.length - 1; i >= 0; i--) {
                    pathRev.add(stringArray[i]);
                }

                String[] stringArrayRev = pathRev.toArray(new String[0]);

                SESEditor.addNodeWIthGraphAddition(addedCell.getValue().toString(), stringArrayRev);

                path.clear();
            }

        }

    }

    public void checkRootConnectivity(mxCell cell) {

        Object[] incoming = graph.getIncomingEdges(cell);
        // getTerminal(java.lang.Object edge, boolean isSource)
        if (incoming.length != 0) {
            Object source = graph.getModel().getTerminal(incoming[incoming.length - 1], true);
            mxCell sourceCell = (mxCell) source;
            // System.out.println(sourceCell.getValue());
            if (sourceCell.getId()
                    .equals("rootnode")) { // "rootnode".equals(((mxCell) delcell).getId().toString())
                connectedToRoot = true;
            }
            checkRootConnectivity(sourceCell);
        }
    }

    public boolean isConnectedToRoot(mxCell cell) {

        checkRootConnectivity(cell);

        return connectedToRoot;
    }

    public void nodeToRootPath(mxCell cell) {
        Object[] incoming = graph.getIncomingEdges(cell);
        // getTerminal(java.lang.Object edge, boolean isSource)
        if (incoming.length != 0) {
            Object source = graph.getModel().getTerminal(incoming[incoming.length - 1], true);
            mxCell sourceCell = (mxCell) source;
            // System.out.println(sourceCell.getValue());
            path.add((String) sourceCell.getValue());
            nodeToRootPath(sourceCell);
        }
    }

    public void nodeToRootPathSubTree(mxCell cell) {
        Object[] incoming = graph.getIncomingEdges(cell);
        // getTerminal(java.lang.Object edge, boolean isSource)
        if (incoming.length != 0) {
            Object source = graph.getModel().getTerminal(incoming[incoming.length - 1], true);
            mxCell sourceCell = (mxCell) source;
            // System.out.println(sourceCell.getValue());
            pathToRootSubTree.add((String) sourceCell.getValue());
            nodeToRootPathSubTree(sourceCell);
        }
    }

    public void addVariableFromGraphPopup(Object pos) {
        // String variableName = JOptionPane.showInputDialog(DynamicTreeDemo.framew,
        // "Variable Name:", "New Variable",
        // JOptionPane.INFORMATION_MESSAGE);

        mxCell varCell = (mxCell) pos;
        // System.out.println("Cell-Name" + varCell.getValue());
        selectedNodeCellForVariableUpdate = varCell;

        String variableName = null;
        String variableType = null;
        String variableValue = null;
        String variableLowerBound = null;
        String variableUpperBound = null;

        // multiple input for variable---------------------------------
        JTextField variableField = new JTextField();
        // JTextField variableTypeField = new JTextField();
        JTextField valueField = new JTextField();
        JTextField lowerBoundField = new JTextField();
        JTextField upperBoundField = new JTextField();
        // for validation of input
        JLabel errorLabelField = new JLabel();
        errorLabelField.setForeground(Color.RED);
        errorLabelField.setVisible(true);

        lowerBoundField.setEnabled(false);
        upperBoundField.setEnabled(false);

        String[] typeList = {"Select Type:", "boolean", "int", "float", "double", "string"};

        String variableFieldRegEx = "[a-zA-Z_][a-zA-Z0-9_]*"; // alphanumeric but not start with number

        JComboBox variableTypeField = new JComboBox(typeList);
        variableTypeField.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    selectedType = variableTypeField.getSelectedItem().toString();

                    if (selectedType.equals("string") || selectedType.equals("boolean")) {
                        lowerBoundField.setEnabled(false);
                        upperBoundField.setEnabled(false);
                        // System.out.println(selectedType);
                    } else {
                        lowerBoundField.setEnabled(true);
                        upperBoundField.setEnabled(true);
                    }

                    // --------------
                    if (selectedType.equals("boolean")) {
                        // errorLabelField.setText("Invalid Input. Check All Values.");
                        errorLabelField.setVisible(
                                !variableField.getText().trim().matches(variableFieldRegEx) || (
                                        !valueField.getText().trim().equals("true") && !variableField
                                                .getText().trim().equals("false")));
                    } else if (selectedType.equals("string")) {
                        // errorLabelField.setText("Invalid Input. Check All Values.");
                        errorLabelField.setVisible(
                                !variableField.getText().trim().matches(variableFieldRegEx) || !valueField
                                        .getText().trim().matches(variableFieldRegEx));
                    } else if (selectedType.equals("double")) {
                        // errorLabelField.setText("Invalid Input. Check All Values.");
                        errorLabelField.setVisible(
                                !valueField.getText().trim().matches("^\\d*\\.\\d+") || !variableField
                                        .getText().trim().matches(variableFieldRegEx) || !lowerBoundField
                                        .getText().trim().matches("^\\d*\\.\\d+") || !upperBoundField
                                        .getText().trim().matches("^\\d*\\.\\d+"));
                    } else {
                        // errorLabelField.setText("Invalid Input. Check All Values.");
                        errorLabelField.setVisible(
                                !variableField.getText().trim().matches(variableFieldRegEx) || !valueField
                                        .getText().trim().matches("^[0-9]+") || !lowerBoundField.getText()
                                        .trim().matches("^[0-9]+") || !upperBoundField.getText().trim()
                                        .matches("^[0-9]+"));
                    }
                    // ---

                }
            }
        });

        variableField.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyReleased(KeyEvent e) {

                if (selectedType.equals("string")) {
                    // errorLabelField.setText("Invalid Input. Check All Values.");
                    errorLabelField.setVisible(
                            !variableField.getText().trim().matches(variableFieldRegEx) || !valueField
                                    .getText().trim().matches(variableFieldRegEx));
                } else if (selectedType.equals("boolean")) {

                    // errorLabelField.setText("Invalid Input. Check All Values.");
                    errorLabelField.setVisible(
                            (!valueField.getText().trim().equals("false") && !valueField.getText().trim()
                                    .equals("true")) || !variableField.getText().trim()
                                    .matches(variableFieldRegEx));

                } else if (selectedType.equals("double")) {
                    // errorLabelField.setText("Invalid Input. Check All Values.");
                    errorLabelField.setVisible(
                            !valueField.getText().trim().matches("^\\d*\\.\\d+") || !variableField.getText()
                                    .trim().matches(variableFieldRegEx) || !lowerBoundField.getText().trim()
                                    .matches("^\\d*\\.\\d+") || !upperBoundField.getText().trim()
                                    .matches("^\\d*\\.\\d+"));
                } else {
                    // errorLabelField.setText("Invalid Input. Check All Values.");
                    errorLabelField.setVisible(
                            !variableField.getText().trim().matches(variableFieldRegEx) || !valueField
                                    .getText().trim().matches("^[0-9]+") || !lowerBoundField.getText().trim()
                                    .matches("^[0-9]+") || !upperBoundField.getText().trim()
                                    .matches("^[0-9]+"));
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub

            }
        });

        valueField.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyReleased(KeyEvent e) {

                if (selectedType.equals("boolean")) {

                    // errorLabelField.setText("Invalid Input. Check All Values.");
                    errorLabelField.setVisible(
                            (!valueField.getText().trim().equals("false") && !valueField.getText().trim()
                                    .equals("true")) || !variableField.getText().trim()
                                    .matches(variableFieldRegEx));

                } else if (selectedType.equals("int")) {

                    // errorLabelField.setText("Invalid Input. Check All Values.");
                    errorLabelField.setVisible(
                            !valueField.getText().trim().matches("^[0-9]+") || !variableField.getText().trim()
                                    .matches(variableFieldRegEx) || !lowerBoundField.getText().trim()
                                    .matches("^[0-9]+") || !upperBoundField.getText().trim()
                                    .matches("^[0-9]+"));

                } else if (selectedType.equals("float")) {

                    // errorLabelField.setText("Invalid Input. Check All Values.");
                    errorLabelField.setVisible(
                            !valueField.getText().trim().matches("^\\d*\\.\\d+") || !variableField.getText()
                                    .trim().matches(variableFieldRegEx)
                            // "^[a-z,A-Z]+"
                            || !lowerBoundField.getText().trim().matches("^\\d*\\.\\d+") || !upperBoundField
                                    .getText().trim().matches("^\\d*\\.\\d+"));

                } else if (selectedType.equals("double")) {

                    // errorLabelField.setText("Invalid Input. Check All Values.");
                    errorLabelField.setVisible(
                            !valueField.getText().trim().matches("^\\d*\\.\\d+") || !variableField.getText()
                                    .trim().matches(variableFieldRegEx) || !lowerBoundField.getText().trim()
                                    .matches("^\\d*\\.\\d+") || !upperBoundField.getText().trim()
                                    .matches("^\\d*\\.\\d+"));

                } else if (selectedType.equals("string")) {

                    // errorLabelField.setText("Invalid Input. Check All Values.");
                    errorLabelField.setVisible(
                            !valueField.getText().trim().matches(variableFieldRegEx) || !variableField
                                    .getText().trim().matches(variableFieldRegEx));
                }

            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub

            }
        });

        lowerBoundField.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyReleased(KeyEvent e) {

                if (selectedType.equals("float")) {

                    // errorLabelField.setText("Invalid Input. Check All Values.");
                    errorLabelField.setVisible(
                            !valueField.getText().trim().matches("^[0-9]+") || !variableField.getText().trim()
                                    .matches(variableFieldRegEx) || !lowerBoundField.getText().trim()
                                    .matches("^[0-9]+") || !upperBoundField.getText().trim()
                                    .matches("^[0-9]+"));

                } else if (selectedType.equals("int")) {

                    // errorLabelField.setText("Invalid Input. Check All Values.");
                    errorLabelField.setVisible(
                            !valueField.getText().trim().matches("^[0-9]+") || !variableField.getText().trim()
                                    .matches(variableFieldRegEx) || !lowerBoundField.getText().trim()
                                    .matches("^[0-9]+") || !upperBoundField.getText().trim()
                                    .matches("^[0-9]+"));

                } else if (selectedType.equals("double")) {

                    // errorLabelField.setText("Invalid Input. Check All Values.");
                    errorLabelField.setVisible(
                            !valueField.getText().trim().matches("^\\d*\\.\\d+") || !variableField.getText()
                                    .trim().matches(variableFieldRegEx) || !lowerBoundField.getText().trim()
                                    .matches("^\\d*\\.\\d+") || !upperBoundField.getText().trim()
                                    .matches("^\\d*\\.\\d+"));

                }
                // else if (selectedType.equals("string")) {
                //
                // if (valueField.getText().trim().matches("^[a-z,A-Z]+")
                // && variableField.getText().trim().matches("^[a-z,A-Z]+")) {
                // errorLabelField.setVisible(false);
                // } else {
                // errorLabelField.setVisible(true);
                // errorLabelField.setText("Invalid Input. Check All Values.");
                //
                // }
                // }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub

            }
        });

        upperBoundField.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyReleased(KeyEvent e) {

                if (selectedType.equals("float")) {

                    // errorLabelField.setText("Invalid Input. Check All Values.");
                    errorLabelField.setVisible(
                            !valueField.getText().trim().matches("^[0-9]+") || !variableField.getText().trim()
                                    .matches(variableFieldRegEx) || !lowerBoundField.getText().trim()
                                    .matches("^[0-9]+") || !upperBoundField.getText().trim()
                                    .matches("^[0-9]+"));

                } else if (selectedType.equals("int")) {

                    // errorLabelField.setText("Invalid Input. Check All Values.");
                    errorLabelField.setVisible(
                            !valueField.getText().trim().matches("^[0-9]+") || !variableField.getText().trim()
                                    .matches(variableFieldRegEx) || !lowerBoundField.getText().trim()
                                    .matches("^[0-9]+") || !upperBoundField.getText().trim()
                                    .matches("^[0-9]+"));

                } else if (selectedType.equals("double")) {

                    // errorLabelField.setText("Invalid Input. Check All Values.");
                    errorLabelField.setVisible(
                            !valueField.getText().trim().matches("^\\d*\\.\\d+") || !variableField.getText()
                                    .trim().matches(variableFieldRegEx) || !lowerBoundField.getText().trim()
                                    .matches("^\\d*\\.\\d+") || !upperBoundField.getText().trim()
                                    .matches("^\\d*\\.\\d+"));

                }
                // else if (selectedType.equals("string")) {
                //
                // if (valueField.getText().trim().matches("^[a-z,A-Z]+")
                // && variableField.getText().trim().matches("^[a-z,A-Z]+")) {
                // errorLabelField.setVisible(false);
                // } else {
                // errorLabelField.setVisible(true);
                // errorLabelField.setText("Invalid Input. Check All Values.");
                //
                // }
                // }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub

            }
        });

        Object[] message =
                {"Variable Name:", variableField, "Variable Type:", variableTypeField, "Default Value:",
                        valueField, "Lower Bound:", lowerBoundField, "Upper Bound:", upperBoundField, " ",
                        errorLabelField};

        int option = JOptionPane
                .showConfirmDialog(SESEditor.framew, message, "Please Enter", JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            variableName = variableField.getText();
            variableType = (String) variableTypeField.getSelectedItem();
            variableValue = valueField.getText();
            variableLowerBound = lowerBoundField.getText();
            variableUpperBound = upperBoundField.getText();

            if (variableType.equals("")) {
                variableType = "none";
            }

            if (variableValue.equals("")) {
                variableValue = "none";
            }

            if (variableLowerBound.equals("")) {
                variableLowerBound = "none";
            }

            if (variableUpperBound.equals("")) {
                variableUpperBound = "none";
            }
            //...........................

            // added inside IF block so that if variable window closed without adding then
            // nothing will happen.
            if (variableTypeField.getSelectedItem().toString().trim().equals("string") || variableTypeField
                    .getSelectedItem().toString().trim().equals("boolean")) {
                variableName = variableName + "," + variableType + "," + variableValue;
            } else {
                variableName =
                        variableName + "," + variableType + "," + variableValue + "," + variableLowerBound
                        + "," + variableUpperBound;
            }


            boolean validInput =
                    (variableField.getText() != null) && (!variableField.getText().trim().isEmpty()) && (
                            variableTypeField.getSelectedItem() != null) && (!variableTypeField
                            .getSelectedItem().toString().trim().isEmpty());
            //&& (valueField.getText() != null) && (!valueField.getText().trim().isEmpty());
            // && (lowerBoundField.getText() != null) &&
            // (!lowerBoundField.getText().trim().isEmpty())
            // && (upperBoundField.getText() != null) &&
            // (!upperBoundField.getText().trim().isEmpty());

            if (!validInput) {
                JOptionPane.showMessageDialog(SESEditor.framew, "Please input all values correctly.");
            }

            // end of multiple input for variable----------------------------
            if (validInput) {
                mxCell cellForAddingVariable = (mxCell) pos;
                pathToRoot.add((String) cellForAddingVariable.getValue());
                nodeToRootPathVar(cellForAddingVariable);

                String[] stringArray = pathToRoot.toArray(new String[0]);
                ArrayList<String> pathToRootRev = new ArrayList<String>();

                for (int i = stringArray.length - 1; i >= 0; i--) {
                    pathToRootRev.add(stringArray[i]);
                }

                String[] stringArrayRev = pathToRootRev.toArray(new String[0]);

                TreePath treePathForVariable = SESEditor.getTreeNodePath(stringArrayRev);

                DynamicTree.varMap.put(treePathForVariable, variableName);

                pathToRoot.clear();

                // have to call a function to refresh the table view
                SESEditor.treePanel.refreshVariableTable(treePathForVariable);
            }
        }

    }

    public void addVariableFromScenarioTableForUpdate(mxCell cellForAddingVariable, String variableName) {

        // end of multiple input for variable----------------------------

        pathToRoot.add((String) cellForAddingVariable.getValue());
        nodeToRootPathVar(cellForAddingVariable);

        String[] stringArray = pathToRoot.toArray(new String[0]);
        ArrayList<String> pathToRootRev = new ArrayList<String>();

        for (int i = stringArray.length - 1; i >= 0; i--) {
            pathToRootRev.add(stringArray[i]);
        }

        String[] stringArrayRev = pathToRootRev.toArray(new String[0]);

        // for (int i = 0; i < stringArrayRev.length; i++) {
        // System.out.println(stringArrayRev[i]);
        // }

        TreePath treePathForVariable = SESEditor.getTreeNodePath(stringArrayRev);

        DynamicTree.varMap.put(treePathForVariable, variableName);
        /*
         * DynamicTree.varMapValue.put(treePathForVariable, variableValue);
         * DynamicTree.varMapValueMinRange.put(treePathForVariable, variableLowerBound);
         * DynamicTree.varMapValueMaxRange.put(treePathForVariable, variableUpperBound);
         */
        pathToRoot.clear();

        // //just testing setting as attributes
        // //not working have to delete later
        // cellForAddingVariable.setAttribute("variable", variableName);
        // String st = cellForAddingVariable.getAttribute("variable");
        // System.out.println(st);

        // have to call a function to refresh the table view
        SESEditor.treePanel.refreshVariableTable(treePathForVariable);

    }

    public void addConstraintFromGraphPopup(Object pos) {

        JTextArea constraintsField = new JTextArea(10, 30);
        constraintsField.setLineWrap(true);
        constraintsField.setWrapStyleWord(true);

        Object[] message = {"Constraint:", constraintsField};

        int option = JOptionPane
                .showConfirmDialog(SESEditor.framew, message, "Please Enter", JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            // DynamicTreeDemo.treePanel.constraintsList.add(constraintsField.getText());
            // DynamicTreeDemo.scenarioConstraint.showConstraintsInTable();
            String constraints = constraintsField.getText();

            boolean validInput =
                    (constraintsField.getText() != null) && (!constraintsField.getText().trim().isEmpty());

            if (validInput) {
                mxCell cellForAddingVariable = (mxCell) pos;
                pathToRoot.add((String) cellForAddingVariable.getValue());
                nodeToRootPathVar(cellForAddingVariable);

                String[] stringArray = pathToRoot.toArray(new String[0]);
                ArrayList<String> pathToRootRev = new ArrayList<String>();

                for (int i = stringArray.length - 1; i >= 0; i--) {
                    pathToRootRev.add(stringArray[i]);
                }

                String[] stringArrayRev = pathToRootRev.toArray(new String[0]);

                TreePath treePathForVariable = SESEditor.getTreeNodePath(stringArrayRev);

                DynamicTree.constraintsList.put(treePathForVariable, constraints);

                pathToRoot.clear();

                // have to call a function to refresh the table view
                // DynamicTree.constraintsList.put(treePathForVariable, constraints);
                SESEditor.treePanel.showConstraintsInTable(treePathForVariable);
            }
        }
    }

    public void deleteConstraintFromScenarioTableForUpdate(mxCell cellForAddingVariable, String variableName,
                                                           String variableNameNew) {
        // String variableName = JOptionPane.showInputDialog(DynamicTreeDemo.framew,
        // "Variable Name:", "New Variable",JOptionPane.INFORMATION_MESSAGE);

        if ((variableName != null) && (!variableName.trim().isEmpty())) {
            // Console.addConsoleOutput(cellForAddingVariable.getValue().toString()+","+variableName);

            // mxCell cellForAddingVariable = (mxCell) pos;
            pathToRoot.add((String) cellForAddingVariable.getValue());
            nodeToRootPathVar(cellForAddingVariable);

            String[] stringArray = pathToRoot.toArray(new String[0]);
            ArrayList<String> pathToRootRev = new ArrayList<String>();

            for (int i = stringArray.length - 1; i >= 0; i--) {
                pathToRootRev.add(stringArray[i]);
            }

            String[] stringArrayRev = pathToRootRev.toArray(new String[0]);

            // for (int i = 0; i < stringArrayRev.length; i++) {
            // System.out.println(stringArrayRev[i]);
            // }

            TreePath treePathForVariable = SESEditor.getTreeNodePath(stringArrayRev);

            DefaultMutableTreeNode currentNode =
                    (DefaultMutableTreeNode) (treePathForVariable.getLastPathComponent());
            TreeNode[] nodes = currentNode.getPath();

            // DynamicTree.varMap.put(treePathForVariable, variableName);
            // have to delete that variable here
            // ---------------------------------------------------start
            int yv = 0;
            TreePath keyDel = null;
            for (TreePath key : DynamicTree.constraintsList.keySet()) {
                int a = 0;
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
                    for (String value : DynamicTree.constraintsList.get(key)) {
                        if (value.equals(variableName)) {
                            yv = 1;
                            keyDel = key; // to avoid java.util.ConcurrentModificationException
                        }
                    }
                }

            }
            if (yv == 1) {
                // DynamicTree.varMap.asMap().remove(keyDel);
                DynamicTree.constraintsList.remove(keyDel, variableName); // for removing only one values
                yv = 0;
            }

            // have to call a function to refresh the table view
            DynamicTree.constraintsList.put(treePathForVariable, variableNameNew);
            // ---------------------------------------------------end

            pathToRoot.clear();

            SESEditor.treePanel.showConstraintsInTable(treePathForVariable);

        }
    }

    // recursive function for finding child nodes

    public void deleteVariableFromScenarioTableForUpdate(mxCell cellForAddingVariable, String variableName,
                                                         String variableNameNew) {
        // String variableName = JOptionPane.showInputDialog(DynamicTreeDemo.framew,
        // "Variable Name:", "New Variable",JOptionPane.INFORMATION_MESSAGE);

        if ((variableName != null) && (!variableName.trim().isEmpty())) {
            // Console.addConsoleOutput(cellForAddingVariable.getValue().toString()+","+variableName);

            // mxCell cellForAddingVariable = (mxCell) pos;
            pathToRoot.add((String) cellForAddingVariable.getValue());
            nodeToRootPathVar(cellForAddingVariable);

            String[] stringArray = pathToRoot.toArray(new String[0]);
            ArrayList<String> pathToRootRev = new ArrayList<String>();

            for (int i = stringArray.length - 1; i >= 0; i--) {
                pathToRootRev.add(stringArray[i]);
            }

            String[] stringArrayRev = pathToRootRev.toArray(new String[0]);

            // for (int i = 0; i < stringArrayRev.length; i++) {
            // System.out.println(stringArrayRev[i]);
            // }

            TreePath treePathForVariable = SESEditor.getTreeNodePath(stringArrayRev);

            DefaultMutableTreeNode currentNode =
                    (DefaultMutableTreeNode) (treePathForVariable.getLastPathComponent());
            TreeNode[] nodes = currentNode.getPath();

            // DynamicTree.varMap.put(treePathForVariable, variableName);
            // have to delete that variable here
            // ---------------------------------------------------start
            int yv = 0;
            TreePath keyDel = null;
            for (TreePath key : DynamicTree.varMap.keySet()) {
                int a = 0;
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
                    for (String value : DynamicTree.varMap.get(key)) {
                        if (value.equals(variableName)) {
                            yv = 1;
                            keyDel = key; // to avoid java.util.ConcurrentModificationException
                        }
                    }
                }

            }
            if (yv == 1) {
                // DynamicTree.varMap.asMap().remove(keyDel);
                DynamicTree.varMap.remove(keyDel, variableName); // for removing only one values
                yv = 0;
            }

            // have to call a function to refresh the table view
            SESEditor.treePanel.refreshVariableTable(treePathForVariable);
            // ---------------------------------------------------end

            pathToRoot.clear();

            addVariableFromScenarioTableForUpdate(cellForAddingVariable, variableNameNew);

        }
    }

    public void deleteVariableFromGraphPopup(Object pos) {
        String variableName = null;

        mxCell cell = (mxCell) pos;

        // all variables

        pathToRoot.add((String) cell.getValue());
        nodeToRootPathVar(cell);

        String[] stringArray = pathToRoot.toArray(new String[0]);
        ArrayList<String> pathToRootRev = new ArrayList<String>();

        for (int i = stringArray.length - 1; i >= 0; i--) {
            pathToRootRev.add(stringArray[i]);
        }

        String[] stringArrayRev = pathToRootRev.toArray(new String[0]);

        TreePath treePathForVariable = SESEditor.getTreeNodePath(stringArrayRev);

        int b = 0;
        String[] nodesToSelectedNode = new String[100];
        nodesToSelectedNode[b] = "Select Variable:";
        b++;

        if (!(treePathForVariable == null)) {
            DefaultMutableTreeNode currentNode =
                    (DefaultMutableTreeNode) (treePathForVariable.getLastPathComponent());

            TreeNode[] nodes = currentNode.getPath();

            for (TreePath key : DynamicTree.varMap.keySet()) {
                int a = 0;

                for (String value : DynamicTree.varMap.get(key)) {

                    DefaultMutableTreeNode currentNode2 =
                            (DefaultMutableTreeNode) (key.getLastPathComponent());

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

        }

        // java 8 way null removal
        nodesToSelectedNode = Arrays.stream(nodesToSelectedNode).filter(s -> (s != null && s.length() > 0))
                .toArray(String[]::new);

        JComboBox variableList = new JComboBox(nodesToSelectedNode);
        variableList.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                selectedVariable = variableList.getSelectedItem().toString().trim();

            }
        });

        Object[] message = {"Variable:", variableList};

        int option = JOptionPane
                .showConfirmDialog(SESEditor.framew, message, "Please Select", JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            variableName = selectedVariable;
            // System.out.println(variableName);

            // end of all variables

            if ((variableName != null) && (!variableName.trim().isEmpty())) {

                DefaultMutableTreeNode currentNode =
                        (DefaultMutableTreeNode) (treePathForVariable.getLastPathComponent());
                TreeNode[] nodes = currentNode.getPath();

                int yv = 0;
                TreePath keyDel = null;
                for (TreePath key : DynamicTree.varMap.keySet()) {
                    int a = 0;
                    DefaultMutableTreeNode currentNode2 =
                            (DefaultMutableTreeNode) (key.getLastPathComponent());

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
                        for (String value : DynamicTree.varMap.get(key)) {
                            if (value.equals(variableName)) {
                                yv = 1;
                                keyDel = key; // to avoid java.util.ConcurrentModificationException
                            }
                        }
                    }

                }
                if (yv == 1) {
                    // DynamicTree.varMap.asMap().remove(keyDel);
                    DynamicTree.varMap.remove(keyDel, variableName); // for removing only one values
                    yv = 0;
                }

                // have to call a function to refresh the table view
                SESEditor.treePanel.refreshVariableTable(treePathForVariable);
                // ---------------------------------------------------end

                pathToRoot.clear();

            }
        }
        selectedVariable = "";
    }

    public void deleteAllVariablesFromGraphPopup(Object pos) {
        // deleting all the variables of a node

        mxCell cellForAddingVariable = (mxCell) pos;
        pathToRoot.add((String) cellForAddingVariable.getValue());
        nodeToRootPathVar(cellForAddingVariable);

        String[] stringArray = pathToRoot.toArray(new String[0]);
        ArrayList<String> pathToRootRev = new ArrayList<String>();

        for (int i = stringArray.length - 1; i >= 0; i--) {
            pathToRootRev.add(stringArray[i]);
        }

        String[] stringArrayRev = pathToRootRev.toArray(new String[0]);

        // for (int i = 0; i < stringArrayRev.length; i++) {
        // System.out.println(stringArrayRev[i]);
        // }

        TreePath treePathForVariable = SESEditor.getTreeNodePath(stringArrayRev);

        DefaultMutableTreeNode currentNode =
                (DefaultMutableTreeNode) (treePathForVariable.getLastPathComponent());
        TreeNode[] nodes = currentNode.getPath();

        // DynamicTree.varMap.put(treePathForVariable, variableName);
        // have to delete that variable here
        // ---------------------------------------------------start

        List<TreePath> delKeys = new ArrayList<TreePath>();

        int yv = 0;
        for (TreePath key : DynamicTree.varMap.keySet()) {

            int a = 0;
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
                yv = 1;
                delKeys.add(key);
            }

        }
        if (yv == 1) {
            for (TreePath k : delKeys) {
                DynamicTree.varMap.asMap().remove(k);
                yv = 0;
            }
        }

        // have to call a function to refresh the table view
        SESEditor.treePanel.refreshVariableTable(treePathForVariable);
        // ---------------------------------------------------end

        pathToRoot.clear();

    }

    public void deleteAllConstraintFromGraphPopup(Object pos) {
        // deleting all the variables of a node

        mxCell cellForAddingVariable = (mxCell) pos;
        pathToRoot.add((String) cellForAddingVariable.getValue());
        nodeToRootPathVar(cellForAddingVariable);

        String[] stringArray = pathToRoot.toArray(new String[0]);
        ArrayList<String> pathToRootRev = new ArrayList<String>();

        for (int i = stringArray.length - 1; i >= 0; i--) {
            pathToRootRev.add(stringArray[i]);
        }

        String[] stringArrayRev = pathToRootRev.toArray(new String[0]);

        // for (int i = 0; i < stringArrayRev.length; i++) {
        // System.out.println(stringArrayRev[i]);
        // }

        TreePath treePathForVariable = SESEditor.getTreeNodePath(stringArrayRev);

        DefaultMutableTreeNode currentNode =
                (DefaultMutableTreeNode) (treePathForVariable.getLastPathComponent());
        TreeNode[] nodes = currentNode.getPath();

        // DynamicTree.varMap.put(treePathForVariable, variableName);
        // have to delete that variable here
        // ---------------------------------------------------start

        List<TreePath> delKeys = new ArrayList<TreePath>();

        int yv = 0;
        for (TreePath key : DynamicTree.constraintsList.keySet()) {

            int a = 0;
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
                yv = 1;
                delKeys.add(key);
            }

        }
        if (yv == 1) {
            for (TreePath k : delKeys) {
                DynamicTree.constraintsList.asMap().remove(k);
                yv = 0;
            }
        }

        // have to call a function to refresh the table view
        SESEditor.treePanel.showConstraintsInTable(treePathForVariable);
        // ---------------------------------------------------end

        pathToRoot.clear();

    }

    // for solving sequence problem--------------------------------------
    public void rootToEndNodeSequenceSolve() {

        Object[] cells = graph.getChildVertices(graph.getDefaultParent()); // getSelectionCells();

        mxCell rootcell = null;

        for (Object c : cells) {
            mxCell cell2 = (mxCell) c; // casting
            if (cell2.isVertex()) {
                String val = cell2.getId();
                // System.out.println(val);
                if (val.equals("rootnode")) {
                    rootcell = cell2;
                }

            }
        } // end of for

        if (rootcell != null) {
            nextChild(rootcell);
        }

    }

    // if i want to create less xml file i can store data in buffer an write only
    // one xml

    public void nextChild(mxCell cell) {
        int specDecCount = 0;
        Object[] outgoing = graph.getOutgoingEdges(cell);

        for (int j = 0; j < outgoing.length; j++) {
            Object targetCellTT = graph.getModel().getTerminal(outgoing[j], false);
            mxCell targetCell2TT = (mxCell) targetCellTT;
            // System.out.println(targetCell2TT.getValue());
            if (targetCell2TT.getValue().toString().endsWith("Spec") || targetCell2TT.getValue().toString()
                    .endsWith("Dec")) {
                specDecCount++;
            }
        }

        if (specDecCount > 1) {
            // System.out.println("Have to insert one sequence here:" + cell.getValue());
            path.add((String) cell.getValue());
            nodeToRootPath(cell);

            // update jtree
            String[] stringArray = path.toArray(new String[0]);
            ArrayList<String> pathRev = new ArrayList<String>();

            for (int k = stringArray.length - 1; k >= 0; k--) {
                pathRev.add(stringArray[k]);
            }

            String[] stringArrayRev = pathRev.toArray(new String[0]);

            // for (int k = 0; k < stringArrayRev.length; k++) {
            // System.out.println(stringArrayRev[k]);
            // }

            path.clear();
            specDecCount = 0;

            FileConvertion obj = new FileConvertion();
            obj.fixingSequenceProblem(stringArrayRev);

        }

        if (outgoing.length > 0) {
            for (int i = 0; i < outgoing.length; i++) {
                Object targetCell = graph.getModel().getTerminal(outgoing[i], false);
                mxCell targetCell2 = (mxCell) targetCell;
                nextChild(targetCell2);

            }

            //
        }

    }

    // this function is used by other caller for inserting node in specific position
    public mxCell rootToSelectedNode(String[] nodesToSelectedNodeOrg) {
        nodesToSelectedNode = nodesToSelectedNodeOrg;
        totalNodes = nodesToSelectedNode.length;
        nodeReached = 1; // 1 because rootnode is reached by default

        // for (int i = 0; i < totalNodes; i++) {
        // System.out.println(nodesToSelectedNode[i]);
        //
        // }

        // for sub tree generation i can use that
        // graph.getChildVertices(graph.getDefaultParent()) later

        Object[] cells = graph.getChildVertices(graph.getDefaultParent()); // getSelectionCells();

        mxCell rootcell = null;

        for (Object c : cells) {
            mxCell cell2 = (mxCell) c; // casting
            if (cell2.isVertex()) {
                String val = cell2.getId();
                // System.out.println(val);
                if (val.equals("rootnode")) {
                    rootcell = cell2;
                }

            }
        } // end of for

        if (rootcell != null) {
            nextChildNodeInPath(rootcell); // important func call
        }

        if (totalNodes == 1) {
            // System.out.println(rootcell.getValue());
            return rootcell;
        } else {
            // System.out.println("lastNodeInPathCall:" + lastNodeInPath.getValue());
            // System.out.println(lastNodeInPath.getGeometry().getX());
            // System.out.println(lastNodeInPath.getGeometry().getY());
            return lastNodeInPath;
        }

    }

    public void nextChildNodeInPath(mxCell cell) {
        Object[] outgoing = graph.getOutgoingEdges(cell);

        if (outgoing.length > 0) {
            for (int i = 0; i < outgoing.length && nodeReached != totalNodes; i++) {
                Object targetCell = graph.getModel().getTerminal(outgoing[i], false);

                // for next call
                mxCell targetCell2 = (mxCell) targetCell;
                // System.out.println(targetCell2.getValue());
                // System.out.println(nodesToSelectedNode[nodeReached]);
                if (targetCell2.getValue().equals(nodesToSelectedNode[nodeReached])) {
                    // call to find next match child node on the path
                    nodeReached++;
                    // System.out.println("lastNodeInPath:" + targetCell2.getValue());
                    lastNodeInPath = targetCell2;

                    if (nodeReached < totalNodes) {

                        nextChildNodeInPath(targetCell2);

                    }

                }
            }
        }

    }

    public void zoomIn() {
        graphComponent.zoomIn();
    }

    public void zoomOut() {
        graphComponent.zoomOut();
    }

    public void redo() {
        if (undoManager.canRedo()) {
            undoManager.redo();
        }
    }

    public void undo() {
        if (undoManager.canUndo()) {
            undoManager.undo();
        }
    }

    public void saveGraph() {
        try {
            // ObjectOutputStream oosgraph = new ObjectOutputStream(new
            // FileOutputStream(ssdFileGraph));
            // oosgraph.writeObject(graph);
            // oosgraph.close();

            // saving drawn graph as a png
            // BufferedImage image = mxCellRenderer.createBufferedImage(graph, null, 1,
            // Color.WHITE, true, null);
            // ImageIO.write(image, "PNG", new File(DynamicTreeDemo.fileLocation + "/" +
            // DynamicTreeDemo.projName + "/graph.png"));

            // saving into xml
            mxCodec codec = new mxCodec();
            String xml = mxXmlUtils.getXml(codec.encode(graph.getModel()));
            // java.io.FileWriter fw = new
            // java.io.FileWriter("runtimefiles\\scenariograph.xml");
            java.io.FileWriter fw = new java.io.FileWriter(ssdFileGraph);
            fw.write(xml);
            fw.close();
            // System.out.println("During save:"+graph);

        } catch (Exception e) {
            System.out.println("Error:" + e);
        }

    }

    public void graphToXML() {

        // for sub tree generation i can use that
        // graph.getChildVertices(graph.getDefaultParent()) later

        Object[] cells = graph.getChildVertices(graph.getDefaultParent()); // getSelectionCells();

        mxCell rootcell = null;

        for (Object c : cells) {
            mxCell cell2 = (mxCell) c; // casting
            if (cell2.isVertex()) {
                String val = cell2.getId();
                // System.out.println(val);
                if (val.equals("rootnode")) {
                    rootcell = cell2;
                }

            }
        }

        Document calendarDOMDoc = null;
        try {
            DOMImplementation domImpl =
                    DocumentBuilderFactory.newInstance().newDocumentBuilder().getDOMImplementation();

            calendarDOMDoc = domImpl.createDocument(null, "start", null);

        } catch (ParserConfigurationException e1) {
            e1.printStackTrace(System.err);
        } catch (DOMException e2) {
            e2.printStackTrace(System.err);
        }

        calendarDOMDoc.getDocumentElement().appendChild(childNodes(calendarDOMDoc, rootcell));

        try {
            saveToXMLFile(calendarDOMDoc,
                    SESEditor.fileLocation + "/" + SESEditor.projName + "/graphxml.xml");
            // modifyXmlOutput(); // to make it perfect so that it can transer to
            // JTree again
            // JOptionPane.showMessageDialog(DynamicTreeDemo.framew, "Scenario Converted
            // Successfully.", "Jtree to XML",
            // JOptionPane.INFORMATION_MESSAGE);
            // JOptionPane.showMessageDialog(frame, "Problem Occured",
            // "Error",JOptionPane.INFORMATION_MESSAGE);
        } catch (TransformerException ex) {
            Logger.getLogger(SESEditor.class.getName()).log(Level.SEVERE, null, ex);
        }

        modifyXmlOutput();

    }

    public Element childNodes(Document thisDoc, mxCell cell) {

        Element thisElement = null;

        String nodeName = graph.getModel().getValue(cell).toString();
        thisElement = thisDoc.createElement(nodeName);

        Object[] outgoing = graph.getOutgoingEdges(cell);
        // Object[] values = new Object[outgoing.length];

        if (outgoing.length > 0) {
            for (int i = 0; i < outgoing.length; i++) {
                Object targetCell = graph.getModel().getTerminal(outgoing[i], false);
                // values[i] = graph.getModel().getValue(targetCell);
                // System.out.println("Parent:"+cell.getValue()+"<--->Child:"+graph.getModel().getValue(targetCell));
                // System.out.println("</" +
                // graph.getModel().getValue(targetCell) + ">");

                // for next call

                mxCell targetCell2 = (mxCell) targetCell;
                thisElement.appendChild(childNodes(thisDoc, targetCell2));
            }
        }

        return thisElement;

    }

    public void graphToXMLWithUniformity() {

        // for sub tree generation i can use that
        // graph.getChildVertices(graph.getDefaultParent()) later

        Object[] cells = graph.getChildVertices(graph.getDefaultParent()); // getSelectionCells();

        mxCell rootcell = null;

        for (Object c : cells) {
            mxCell cell2 = (mxCell) c; // casting
            if (cell2.isVertex()) {
                String val = cell2.getId();
                // System.out.println(val);
                if (val.equals("rootnode")) {
                    rootcell = cell2;
                }

            }
        }

        Document calendarDOMDoc = null;
        try {
            DOMImplementation domImpl =
                    DocumentBuilderFactory.newInstance().newDocumentBuilder().getDOMImplementation();

            calendarDOMDoc = domImpl.createDocument(null, "start", null);

        } catch (ParserConfigurationException e1) {
            e1.printStackTrace(System.err);
        } catch (DOMException e2) {
            e2.printStackTrace(System.err);
        }

        calendarDOMDoc.getDocumentElement().appendChild(childNodesWithUniformity(calendarDOMDoc, rootcell));

        try {
            saveToXMLFile(calendarDOMDoc,
                    SESEditor.fileLocation + "/" + SESEditor.projName + "/graphxmluniformity.xml");
            // modifyXmlOutput(); // to make it perfect so that it can transer to
            // JTree again
            // JOptionPane.showMessageDialog(DynamicTreeDemo.framew, "Scenario Converted
            // Successfully.", "Jtree to XML",
            // JOptionPane.INFORMATION_MESSAGE);
            // JOptionPane.showMessageDialog(frame, "Problem Occured",
            // "Error",JOptionPane.INFORMATION_MESSAGE);
        } catch (TransformerException ex) {
            Logger.getLogger(SESEditor.class.getName()).log(Level.SEVERE, null, ex);
        }

        modifyXmlOutput();

    }

    public Element childNodesWithUniformity(Document thisDoc, mxCell cell) {

        Element thisElement = null;

        String nodeName = graph.getModel().getValue(cell).toString();
        thisElement = thisDoc.createElement(nodeName);

        Object[] outgoing = graph.getOutgoingEdges(cell);
        // Object[] values = new Object[outgoing.length];

        if (outgoing.length > 0) {
            for (int i = 0; i < outgoing.length; i++) {
                Object targetCell = graph.getModel().getTerminal(outgoing[i], false);

                mxCell targetCell2 = (mxCell) targetCell;

                if (targetCell2.getId().startsWith("uniformity")) {
                    if (targetCell2.getId().endsWith("RefNode")) {

                    } else {
                        continue;
                    }

                } else {
                    thisElement.appendChild(childNodesWithUniformity(thisDoc, targetCell2));
                }
            }
        }

        return thisElement;

    }

    /**
     * Modifying the generated output by making single line element <node/> two
     * double lines <node> </node>
     */
    public void modifyXmlOutputFixForSameNameNode() {
        // System.out.println("Modify called");

        PrintWriter f0 = null;
        try {
            // f0 = new PrintWriter(new FileWriter("runtimefiles/outputgraphxml.xml"));
            f0 = new PrintWriter(new FileWriter(
                    SESEditor.fileLocation + "/" + SESEditor.projName + "/outputgraphxmlforxsdvar.xml"));
            // System.out.println("output file generated");
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        Scanner in = null;
        try {
            in = new Scanner(new File(
                    SESEditor.fileLocation + "/" + SESEditor.projName + "/outputgraphxmlforxsd.xml"));
            // System.out.println("my read complete");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        while (in.hasNext()) { // Iterates each line in the file
            // System.out.println("inside while");
            String line = in.nextLine();
            // System.out.println("Line: " + line);

            if (line.endsWith("/>")) {
                // System.out.println(line);
                String result = line.replaceAll("[</>]", "");
                // String result = line.replaceAll("[\\<\\/\\>]","");
                if (result.endsWith("RefNode")) {
                    f0.println(line);

                } else if (result.endsWith("Var")) {
                    f0.println(line);

                } else if (result.endsWith("Con")) {
                    f0.println(line);

                } else {
                    result = result.replaceAll("\\s+", "");
                    String line1 = "<" + result + ">";
                    String line2 = "</" + result + ">";
                    f0.println(line1);
                    f0.println(line2);
                }

            } else {
                f0.println(line);
            }
        }

        in.close();
        f0.close();

        copyFixForSameNameNodeToOther();
    }

    public void copyFixForSameNameNodeToOther() {
        // System.out.println("Modify called");

        PrintWriter f0 = null;
        try {
            f0 = new PrintWriter(new FileWriter(
                    SESEditor.fileLocation + "/" + SESEditor.projName + "/outputgraphxmlforxsd.xml"));
            // System.out.println("output file generated");
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        Scanner in = null;
        try {
            in = new Scanner(new File(
                    SESEditor.fileLocation + "/" + SESEditor.projName + "/outputgraphxmlforxsdvar.xml"));
            // System.out.println("my read complete");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        while (in.hasNext()) { // Iterates each line in the file

            String line = in.nextLine();

            f0.println(line);
        }

        in.close();
        f0.close();
    }

    // for modifying the generated xml output
    public void xmlOutputForXSD() {

        PrintWriter f0 = null;
        try {
            f0 = new PrintWriter(
                    new FileWriter(SESEditor.fileLocation + "/" + SESEditor.projName + "/xmlforxsd.xml"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        Scanner in = null;
        try {
            in = new Scanner(new File(
                    SESEditor.fileLocation + "/" + SESEditor.projName + "/outputgraphxmlforxsd.xml"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int first = 0;

        while (in.hasNext()) { // Iterates each line in the file

            String mod = null;
            String line = in.nextLine();
            // String result1 = line.replaceAll("\\s+", ""); // for deleting space before
            // each entity because of xml
            // indentation

            // line = result1;

            if (line.startsWith("<?")) { // have to solve space problem for this line
                f0.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");

            } else if (line.startsWith("</")) {
                String result = line.replaceAll("[</>]", "");

                if (result.endsWith("Dec")) {
                    mod = "</aspect>";
                } else if (result.endsWith("MAsp")) {
                    mod = "</multiAspect>";
                } else if (result.endsWith("Spec")) {
                    mod = "</specialization>";
                } else {
                    if (result.endsWith("Seq")) {
                        continue;
                    }
                    mod = "</entity>";
                }

                f0.println(mod);

            } else if (line.startsWith("<")) {
                if (line.endsWith("/>")) {
                    String result = line.replaceAll("[</>]", "");

                    if (result.endsWith("Var")) {
                        String novarresult = result.replace("Var", "");

                        // f0.println("<var name=\"" + novarresult + "\"></var>");

                        // System.out.println(novarresult);

                        String[] properties = novarresult.split(",");
                        if (properties[1].equals("string") || properties[1].equals("boolean")) {

                            f0.println("<var name=\"" + properties[0] + "\" " + "default=\"" + properties[2]
                                       + "\"></var>");
                        } else {

                            f0.println("<var name=\"" + properties[0] + "\" " + "default=\"" + properties[2]
                                       + "\" " + "lower=\"" + properties[3] + "\" " + "upper=\""
                                       + properties[4] + "\" " + "></var>");

                        }

                    } else if (result.endsWith("RefNode")) {
                        String noRefNoderesult = result.replace("RefNode", "");

                        if (noRefNoderesult.endsWith("Dec")) {
                            f0.println("<aspect name=\"" + noRefNoderesult + "\" ref=\"" + noRefNoderesult
                                       + "\"/>");
                        } else if (noRefNoderesult.endsWith("MAsp")) {
                            f0.println(
                                    "<multiAspect name=\"" + noRefNoderesult + "\" ref=\"" + noRefNoderesult
                                    + "\"/>");
                        } else if (noRefNoderesult.endsWith("Spec")) {
                            f0.println("<specialization name=\"" + noRefNoderesult + "\" ref=\""
                                       + noRefNoderesult + "\"/>");
                        } else {
                            f0.println("<entity name=\"" + noRefNoderesult + "\" ref=\"" + noRefNoderesult
                                       + "\"/>");
                        }

                    } else {

                    }

                } else {

                    String result = line.replaceAll("[</>]", "");

                    if (result.endsWith("Dec")) {
                        mod = "<aspect name=\"" + result + "\">";
                    } else if (result.endsWith("MAsp")) {
                        mod = "<multiAspect name=\"" + result + "\">";
                    } else if (result.endsWith("Spec")) {
                        mod = "<specialization name=\"" + result + "\">";
                    } else {
                        if (first == 0) {
                            mod = "<entity xmlns:vc=\"http://www.w3.org/2007/XMLSchema-versioning\""
                                  + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
                                  //+ " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema\""
                                  + " xsi:noNamespaceSchemaLocation=\"ses.xsd\" name=\"" + result + "\">";
                            first = 1;
                        } else {
                            if (result.endsWith("Seq")) {
                                continue;
                            }

                            mod = "<entity name=\"" + result + "\">";

                        }

                    }

                    f0.println(mod);

                }
            }

        }

        in.close();
        f0.close();
    }

    public void addNodeWithJtreeAddition(String nodeName, String[] nodesToSelectedNode) {

        mxCell selectedCell = rootToSelectedNode(nodesToSelectedNode);

        Object[] outgoing = graph.getOutgoingEdges(selectedCell);

        mxCell selectedCellNew = null;
        int len = outgoing.length;

        double x;
        double y;

        if (len > 0) {

            Object targetCell = graph.getModel().getTerminal(outgoing[len - 1], false);
            selectedCellNew = (mxCell) targetCell;
            x = selectedCellNew.getGeometry().getX();
            y = selectedCellNew.getGeometry().getY();
            x = x + 100;

            // if the added node manually changed to other position then overlap may happen.
            // for removing that bug have to compare all the child nodes x positions and
            // have to
            // add after the most right one. didn't implement it yet. will do later after
            // first
            // prototype

        } else {
            x = selectedCell.getGeometry().getX();
            y = selectedCell.getGeometry().getY();
            y = y + 100;
        }

        graph.getModel().beginUpdate();
        try {
            if (nodeName.endsWith("Dec")) {
                Object obj = graph.insertVertex(parent, null, nodeName, x + 25, y, 30, 30, "Aspect");
                graph.insertEdge(parent, null, "", selectedCell, obj);
                lastAddedCell = null; // so that it will not cause duplicate addition in tree
                nodeNumber++;

            } else if (nodeName.endsWith("MAsp")) {
                Object obj = graph.insertVertex(parent, null, nodeName, x + 25, y, 30, 30, "Multiaspect");
                graph.insertEdge(parent, null, "", selectedCell, obj);
                lastAddedCell = null; // so that it will not cause duplicate addition in tree
                nodeNumber++;

            } else if (nodeName.endsWith("Spec")) {
                Object obj = graph.insertVertex(parent, null, nodeName, x + 25, y, 30, 30, "Specialization");
                graph.insertEdge(parent, null, "", selectedCell, obj);
                lastAddedCell = null; // so that it will not cause duplicate addition in tree
                nodeNumber++;

            } else /* if (DynamicTreeDemo.nodeAddDetector.equals("entity")) */ {
                Object obj = graph.insertVertex(parent, null, nodeName, x, y, 80, 30,
                        "Entity"); // "Aspect;fillColor=#0759cf;strokeColor=white;");
                graph.insertEdge(parent, null, "", selectedCell, obj);
                lastAddedCell = null; // so that it will not cause duplicate addition in tree
                nodeNumber++;

            }

        } finally {
            graph.getModel().endUpdate();

        }
    }

    public void addNodeFromConsole(String nodeName) {

        if (currentSelectedCell != null) {
            mxCell selectedCell = currentSelectedCell;

            Object[] outgoing = graph.getOutgoingEdges(selectedCell);

            mxCell selectedCellNew = null;
            int len = outgoing.length;

            double x;
            double y;

            if (len > 0) {

                Object targetCell = graph.getModel().getTerminal(outgoing[len - 1], false);
                selectedCellNew = (mxCell) targetCell;
                x = selectedCellNew.getGeometry().getX();
                y = selectedCellNew.getGeometry().getY();
                x = x + 100;

                // if the added node manually changed to other position then overlap may happen.
                // for removing that bug have to compare all the child nodes x positions and
                // have to
                // add after the most right one. didn't implement it yet. will do later after
                // first
                // prototype

            } else {
                x = selectedCell.getGeometry().getX();
                y = selectedCell.getGeometry().getY();
                y = y + 100;
            }

            graph.getModel().beginUpdate();
            try {
                if (nodeName.endsWith("Dec")) {
                    Object obj = graph.insertVertex(parent, null, nodeName, x + 25, y, 30, 30, "Aspect");
                    graph.insertEdge(parent, null, "", selectedCell, obj);
                    lastAddedCell = null; // so that it will not cause duplicate addition in tree
                    nodeNumber++;

                } else if (nodeName.endsWith("MAsp")) {
                    Object obj = graph.insertVertex(parent, null, nodeName, x + 25, y, 30, 30, "Multiaspect");
                    graph.insertEdge(parent, null, "", selectedCell, obj);
                    lastAddedCell = null; // so that it will not cause duplicate addition in tree
                    nodeNumber++;

                } else if (nodeName.endsWith("Spec")) {
                    Object obj =
                            graph.insertVertex(parent, null, nodeName, x + 25, y, 30, 30, "Specialization");
                    graph.insertEdge(parent, null, "", selectedCell, obj);
                    lastAddedCell = null; // so that it will not cause duplicate addition in tree
                    nodeNumber++;

                } else /* if (DynamicTreeDemo.nodeAddDetector.equals("entity")) */ {
                    Object obj = graph.insertVertex(parent, null, nodeName, x, y, 80, 30,
                            "Entity"); // "Aspect;fillColor=#0759cf;strokeColor=white;");
                    graph.insertEdge(parent, null, "", selectedCell, obj);
                    lastAddedCell = null; // so that it will not cause duplicate addition in tree
                    nodeNumber++;

                }

            } finally {
                graph.getModel().endUpdate();

            }
        }
    }

    public void addModuleFromOtherModelAsXML(Object obj) {
        // a small tree will be added dynamically to the selected node.

        String fileName = "";
        // choosing file from a directory
        Path currentDirectory = Paths.get("").toAbsolutePath();
        String repFslas = currentDirectory.toString().replace("\\", "/");

        JFileChooser fileChooser = new JFileChooser();

        FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter("xml files (*.xml)", "xml");
        fileChooser.setFileFilter(xmlfilter);

        fileChooser.setCurrentDirectory(new File(SESEditor.fileLocation + "/" + SESEditor.projName));
        int result = fileChooser.showOpenDialog(SESEditor.framew);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            fileName = selectedFile.getName();

            Object[] addedNodes = new Object[100];
            int addedNodeCount = 0;
            String[] nodeNameSplits = null;
            int nodeCount = 0;

            String[] parentNames = new String[100];
            int parentCount = 0;

            String[] nodeNames = new String[100];

            try {
                FileReader reader = new FileReader(selectedFile.getAbsolutePath());
                BufferedReader bufferedReader = new BufferedReader(reader);

                String line;

                while ((line = bufferedReader.readLine()) != null) {

                    if (line.startsWith("<?")) {
                        continue;
                    } else if (line.startsWith("<start") || line.startsWith("</start")) {
                        continue;
                    } else if (line.startsWith("</")) {
                        parentCount--;
                    } else if (line.startsWith("<")) {
                        if (line.endsWith("/>")) {
                            String ln = line.replaceAll("[</>]", "");
                            nodeNames[nodeCount] = ln + "-" + parentNames[parentCount - 1] + "-hasparent";
                            nodeCount++;
                        } else {
                            String ln = line.replaceAll("[</>]", "");

                            if (parentCount > 0) {
                                nodeNames[nodeCount] = ln + "-" + parentNames[parentCount - 1] + "-hasparent";
                                nodeCount++;
                            } else {
                                nodeNames[nodeCount] = ln;
                                nodeCount++;
                            }
                            parentNames[parentCount] = ln;
                            parentCount++;
                        }
                    }

                }

                // for (int i = 0; i < nodeCount; i++) {
                // System.out.println(nodeNames[i]);
                // }

                reader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < nodeCount; i++) {
                String nodeName = nodeNames[i];

                if (nodeName.endsWith("hasparent")) {
                    nodeNameSplits = nodeName.split("-");
                    nodeName = nodeNameSplits[0];

                    String parentNode = nodeNameSplits[1];
                    for (Object node : addedNodes) {
                        if (!(node == null)) {
                            mxCell cell = (mxCell) node;
                            if (cell.getValue().equals(parentNode)) {
                                obj = cell;
                            }
                        }
                    }
                }

                mxCell selectedCell = (mxCell) obj;

                Object[] outgoing = graph.getOutgoingEdges(selectedCell);

                mxCell selectedCellNew = null;
                int len = outgoing.length;

                double x;
                double y;

                if (len > 0) {

                    Object targetCell = graph.getModel().getTerminal(outgoing[len - 1], false);
                    selectedCellNew = (mxCell) targetCell;
                    x = selectedCellNew.getGeometry().getX();
                    y = selectedCellNew.getGeometry().getY();
                    x = x + 100;

                    // if the added node manually changed to other position then overlap may happen.
                    // for removing that bug have to compare all the child nodes x positions and
                    // have to
                    // add after the most right one. didn't implement it yet. will do later after
                    // first
                    // prototype

                } else {
                    x = selectedCell.getGeometry().getX();
                    y = selectedCell.getGeometry().getY();
                    y = y + 100;
                }

                graph.getModel().beginUpdate();
                try {
                    if (nodeName.endsWith("Dec")) {
                        obj = graph.insertVertex(parent, null, nodeName, x + 25, y, 30, 30, "Aspect");
                        graph.insertEdge(parent, null, "", selectedCell, obj);
                        lastAddedCell = null; // so that it will not cause duplicate addition in tree
                        nodeNumber++;

                        addNodeIntoJtreeWithNewModuleAddition(selectedCell);

                    } else if (nodeName.endsWith("MAsp")) {

                        obj = graph.insertVertex(parent, null, nodeName, x + 25, y, 30, 30, "Multiaspect");
                        graph.insertEdge(parent, null, "", selectedCell, obj);
                        lastAddedCell = null; // so that it will not cause duplicate addition in tree
                        nodeNumber++;

                        addNodeIntoJtreeWithNewModuleAddition(selectedCell);

                    } else if (nodeName.endsWith("Spec")) {

                        obj = graph.insertVertex(parent, null, nodeName, x + 25, y, 30, 30, "Specialization");
                        graph.insertEdge(parent, null, "", selectedCell, obj);
                        lastAddedCell = null; // so that it will not cause duplicate addition in tree
                        nodeNumber++;

                        addNodeIntoJtreeWithNewModuleAddition(selectedCell);

                    } else /* if (DynamicTreeDemo.nodeAddDetector.equals("entity")) */ {
                        obj = graph.insertVertex(parent, null, nodeName, x, y, 80, 30,
                                "Entity"); // "Aspect;fillColor=#0759cf;strokeColor=white;");
                        graph.insertEdge(parent, null, "", selectedCell, obj);
                        lastAddedCell = null; // so that it will not cause duplicate addition in tree
                        nodeNumber++;

                        addNodeIntoJtreeWithNewModuleAddition(selectedCell);

                    }

                    // added nodes in the array
                    addedNodes[addedNodeCount] = obj;
                    addedNodeCount++;

                } finally {
                    graph.getModel().endUpdate();

                }

            }

            // have to add xml file to project here
            SESEditor.projectPanel.addModueFile(fileName);

        }
    }

    public void importExistingProjectIntoGraph(String importFileLocation) {
        mxCell root = getRootNode();

        Object obj = root;

        String fileName = "";
        // choosing file from a directory
        Path currentDirectory = Paths.get("").toAbsolutePath();
        String repFslas = currentDirectory.toString().replace("\\", "/");

        Object[] addedNodes = new Object[100];
        int addedNodeCount = 0;
        String[] nodeNameSplits = null;
        int nodeCount = 0;

        String[] parentNames = new String[100];
        int parentCount = 0;

        String[] nodeNames = new String[100];

        try {
            FileReader reader = new FileReader(
                    (SESEditor.fileLocation + "/" + SESEditor.projName + "/" + SESEditor.projName + ".xml"));
            BufferedReader bufferedReader = new BufferedReader(reader);

            String line;

            while ((line = bufferedReader.readLine()) != null) {

                if (line.startsWith("<?")) {
                    continue;
                } else if (line.startsWith("<start") || line.startsWith("</start")) {
                    continue;
                } else if (line.startsWith("</")) {
                    parentCount--;
                } else if (line.startsWith("<")) {
                    if (line.endsWith("/>")) {
                        String ln = line.replaceAll("[</>]", "");
                        nodeNames[nodeCount] = ln + "-" + parentNames[parentCount - 1] + "-hasparent";
                        nodeCount++;
                    } else {
                        String ln = line.replaceAll("[</>]", "");

                        if (parentCount > 0) {
                            nodeNames[nodeCount] = ln + "-" + parentNames[parentCount - 1] + "-hasparent";
                            nodeCount++;
                        } else {
                            nodeNames[nodeCount] = ln;
                            nodeCount++;
                        }
                        parentNames[parentCount] = ln;
                        parentCount++;
                    }
                }

            }

            // for (int i = 0; i < nodeCount; i++) {
            // System.out.println(nodeNames[i]);
            // }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        String rootName = nodeNames[0];

        // graph.getModel().beginUpdate();
        // try {
        // graph.getModel().setValue(root, rootName);
        // } finally {
        // graph.getModel().endUpdate();
        // }

        SESEditor.jtreeTograph.deleteAllNodesFromGraphWindow(rootName);

        DefaultMutableTreeNode rootNode2 = new DefaultMutableTreeNode(rootName);
        SESEditor.treePanel.treeModel.setRoot(rootNode2);
        SESEditor.treePanel.treeModel.reload();

        for (int i = 1; i < nodeCount; i++) {
            String nodeName = nodeNames[i];

            if (nodeName.endsWith("hasparent")) {
                nodeNameSplits = nodeName.split("-");
                nodeName = nodeNameSplits[0];

                String parentNode = nodeNameSplits[1];
                for (Object node : addedNodes) {
                    if (!(node == null)) {
                        mxCell cell = (mxCell) node;
                        if (cell.getValue().equals(parentNode)) {
                            obj = cell;
                        }
                    }
                }
            }

            mxCell selectedCell = (mxCell) obj;

            Object[] outgoing = graph.getOutgoingEdges(selectedCell);

            mxCell selectedCellNew = null;
            int len = outgoing.length;

            double x;
            double y;

            if (len > 0) {

                Object targetCell = graph.getModel().getTerminal(outgoing[len - 1], false);
                selectedCellNew = (mxCell) targetCell;
                x = selectedCellNew.getGeometry().getX();
                y = selectedCellNew.getGeometry().getY();
                x = x + 100;

                // if the added node manually changed to other position then overlap may happen.
                // for removing that bug have to compare all the child nodes x positions and
                // have to
                // add after the most right one. didn't implement it yet. will do later after
                // first
                // prototype

            } else {
                x = selectedCell.getGeometry().getX();
                y = selectedCell.getGeometry().getY();
                y = y + 100;
            }

            graph.getModel().beginUpdate();
            try {
                if (nodeName.endsWith("Dec")) {
                    obj = graph.insertVertex(parent, null, nodeName, x + 25, y, 30, 30, "Aspect");
                    graph.insertEdge(parent, null, "", selectedCell, obj);
                    lastAddedCell = null; // so that it will not cause duplicate addition in tree
                    nodeNumber++;

                    addNodeIntoJtreeWithNewModuleAddition(selectedCell);

                } else if (nodeName.endsWith("MAsp")) {

                    obj = graph.insertVertex(parent, null, nodeName, x + 25, y, 30, 30, "Multiaspect");
                    graph.insertEdge(parent, null, "", selectedCell, obj);
                    lastAddedCell = null; // so that it will not cause duplicate addition in tree
                    nodeNumber++;

                    addNodeIntoJtreeWithNewModuleAddition(selectedCell);

                } else if (nodeName.endsWith("Spec")) {

                    obj = graph.insertVertex(parent, null, nodeName, x + 25, y, 30, 30, "Specialization");
                    graph.insertEdge(parent, null, "", selectedCell, obj);
                    lastAddedCell = null; // so that it will not cause duplicate addition in tree
                    nodeNumber++;

                    addNodeIntoJtreeWithNewModuleAddition(selectedCell);

                } else /* if (DynamicTreeDemo.nodeAddDetector.equals("entity")) */ {
                    obj = graph.insertVertex(parent, null, nodeName, x, y, 80, 30,
                            "Entity"); // "Aspect;fillColor=#0759cf;strokeColor=white;");
                    graph.insertEdge(parent, null, "", selectedCell, obj);
                    lastAddedCell = null; // so that it will not cause duplicate addition in tree
                    nodeNumber++;

                    addNodeIntoJtreeWithNewModuleAddition(selectedCell);

                }

                // added nodes in the array
                addedNodes[addedNodeCount] = obj;
                addedNodeCount++;

            } finally {
                graph.getModel().endUpdate();

            }

        }

        // have to add xml file to project here
        // DynamicTreeDemo.projectPanel.addModueFile(fileName);

    }

    // ----------------------------------------------------------------------------------------------------------------------------------------

    public void addModuleFromOtherModel(Object obj) {
        // a small tree will be added dynamically to the selected node.

        String fileName = "";
        // choosing file from a directory
        Path currentDirectory = Paths.get("").toAbsolutePath();
        String repFslas = currentDirectory.toString().replace("\\", "/");

        JFileChooser fileChooser = new JFileChooser();

        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
        fileChooser.setFileFilter(filter);

        fileChooser.setCurrentDirectory(new File(SESEditor.fileLocation + "/" + SESEditor.projName));
        int result = fileChooser.showOpenDialog(SESEditor.framew);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            fileName = selectedFile.getName();

            Object[] addedNodes = new Object[100];
            int addedNodeCount = 0;
            String[] nodeNameSplits = null;
            int nodeCount = 0;

            String[] nodeNames = new String[100];

            try {
                FileReader reader = new FileReader(selectedFile.getAbsolutePath());
                BufferedReader bufferedReader = new BufferedReader(reader);

                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    nodeNames[nodeCount] = line;
                    nodeCount++;
                }
                reader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < nodeCount; i++) {
                String nodeName = nodeNames[i];

                if (nodeName.endsWith("hasparent")) {
                    nodeNameSplits = nodeName.split("-");
                    nodeName = nodeNameSplits[0];

                    String parentNode = nodeNameSplits[1];
                    for (Object node : addedNodes) {
                        if (!(node == null)) {
                            mxCell cell = (mxCell) node;
                            if (cell.getValue().equals(parentNode)) {
                                obj = cell;
                            }
                        }
                    }
                }

                mxCell selectedCell = (mxCell) obj;

                Object[] outgoing = graph.getOutgoingEdges(selectedCell);

                mxCell selectedCellNew = null;
                int len = outgoing.length;

                double x;
                double y;

                if (len > 0) {

                    Object targetCell = graph.getModel().getTerminal(outgoing[len - 1], false);
                    selectedCellNew = (mxCell) targetCell;
                    x = selectedCellNew.getGeometry().getX();
                    y = selectedCellNew.getGeometry().getY();
                    x = x + 100;

                    // if the added node manually changed to other position then overlap may happen.
                    // for removing that bug have to compare all the child nodes x positions and
                    // have to
                    // add after the most right one. didn't implement it yet. will do later after
                    // first
                    // prototype

                } else {
                    x = selectedCell.getGeometry().getX();
                    y = selectedCell.getGeometry().getY();
                    y = y + 100;
                }

                graph.getModel().beginUpdate();
                try {
                    if (nodeName.endsWith("Dec")) {
                        obj = graph.insertVertex(parent, null, nodeName, x + 25, y, 30, 30, "Aspect");
                        graph.insertEdge(parent, null, "", selectedCell, obj);
                        lastAddedCell = null; // so that it will not cause duplicate addition in tree
                        nodeNumber++;

                        addNodeIntoJtreeWithNewModuleAddition(selectedCell);

                    } else if (nodeName.endsWith("MAsp")) {

                        obj = graph.insertVertex(parent, null, nodeName, x + 25, y, 30, 30, "Multiaspect");
                        graph.insertEdge(parent, null, "", selectedCell, obj);
                        lastAddedCell = null; // so that it will not cause duplicate addition in tree
                        nodeNumber++;

                        addNodeIntoJtreeWithNewModuleAddition(selectedCell);

                    } else if (nodeName.endsWith("Spec")) {
                        obj = graph.insertVertex(parent, null, nodeName, x + 25, y, 30, 30, "Specialization");
                        graph.insertEdge(parent, null, "", selectedCell, obj);
                        lastAddedCell = null; // so that it will not cause duplicate addition in tree
                        nodeNumber++;

                        addNodeIntoJtreeWithNewModuleAddition(selectedCell);

                    } else /* if (DynamicTreeDemo.nodeAddDetector.equals("entity")) */ {
                        obj = graph.insertVertex(parent, null, nodeName, x, y, 80, 30,
                                "Entity"); // "Aspect;fillColor=#0759cf;strokeColor=white;");
                        graph.insertEdge(parent, null, "", selectedCell, obj);
                        lastAddedCell = null; // so that it will not cause duplicate addition in tree
                        nodeNumber++;

                        addNodeIntoJtreeWithNewModuleAddition(selectedCell);

                    }

                    // added nodes in the array
                    addedNodes[addedNodeCount] = obj;
                    addedNodeCount++;

                } finally {
                    graph.getModel().endUpdate();

                }

            }

            // have to add xml file to project here
            SESEditor.projectPanel.addModueFile(fileName);

        }

    }

    public void saveModuleFromCurrentModel(Object obj) {

        mxCell cell = (mxCell) obj;
        Object[] outgoing = graph.getOutgoingEdges(cell);
        parentName = (String) cell.getValue();

        if (outgoing.length > 0) {

            Object targetCell = graph.getModel().getTerminal(outgoing[outgoing.length - 1], false);
            mxCell targetCell2 = (mxCell) targetCell;

            Object sourceCell = graph.getModel().getTerminal(outgoing[outgoing.length - 1], true);
            mxCell sourceCell2 = (mxCell) sourceCell;

            nodeNamesForGraphSync[treeSyncNodeCount] = (String) targetCell2.getValue();
            treeSyncNodeCount++;

            saveModuleFromCurrentModelSecondStep(targetCell);

        }

    }

    public void saveModuleFromCurrentModelSecondStep(Object obj) {

        mxCell cell = (mxCell) obj;
        Object[] outgoing = graph.getOutgoingEdges(cell);
        parentName = (String) cell.getValue();

        System.out.println(cell.getId());

        if (outgoing.length > 0) {
            for (int i = 0; i < outgoing.length; i++) {
                Object targetCell = graph.getModel().getTerminal(outgoing[i], false);
                mxCell targetCell2 = (mxCell) targetCell;

                Object sourceCell = graph.getModel().getTerminal(outgoing[i], true);
                mxCell sourceCell2 = (mxCell) sourceCell;

                nodeNamesForGraphSync[treeSyncNodeCount] =
                        targetCell2.getValue() + "-" + sourceCell2.getValue() + "-" + "hasparent";
                treeSyncNodeCount++;

                saveModuleFromCurrentModelSecondStep(targetCell);

            }
        }

    }

    public void addModuleFromSubgraph(Object obj) {
        // a small tree will be added dynamically to the jtree which is drawn in graph
        // editor and connected later to other node

        Object[] addedNodes = new Object[100];
        int addedNodeCount = 0;
        String[] nodeNameSplits = null;

        String[] nodeNames = new String[100];
        int nodeCount = 0;
        nodeCount = treeSyncNodeCount;
        nodeNames = nodeNamesForGraphSync;

        for (int i = 0; i < nodeCount; i++) {
            String nodeName = nodeNames[i];

            if (nodeName.endsWith("hasparent")) {
                nodeNameSplits = nodeName.split("-");
                nodeName = nodeNameSplits[0];

                String parentNode = nodeNameSplits[1];
                for (Object node : addedNodes) {
                    if (!(node == null)) {
                        mxCell cell = (mxCell) node;
                        if (cell.getValue().equals(parentNode)) {
                            obj = cell;
                        }
                    }
                }
            }

            mxCell selectedCell = (mxCell) obj;

            Object[] outgoing = graph.getOutgoingEdges(selectedCell);

            mxCell selectedCellNew = null;
            int len = outgoing.length;

            double x;
            double y;

            if (len > 0) {

                Object targetCell = graph.getModel().getTerminal(outgoing[len - 1], false);
                selectedCellNew = (mxCell) targetCell;

                x = selectedCellNew.getGeometry().getX();
                y = selectedCellNew.getGeometry().getY();
                x = x + 100;

                // if the added node manually changed to other position then overlap may happen.
                // for removing that bug have to compare all the child nodes x positions and
                // have to
                // add after the most right one. didn't implement it yet. will do later after
                // first
                // prototype

            } else {
                x = selectedCell.getGeometry().getX();
                y = selectedCell.getGeometry().getY();
                y = y + 100;
            }

            graph.getModel().beginUpdate();
            try {
                if (nodeName.endsWith("Dec")) {
                    obj = graph.insertVertex(parent, null, nodeName, x + 25, y, 30, 30, "Aspect");

                    graph.insertEdge(parent, null, "", selectedCell, obj);
                    lastAddedCell = null; // so that it will not cause duplicate addition in tree

                    // mxCell cell = (mxCell) obj; cell.setVisible(false);

                    // have to send tree path and node name
                    // i have node name- nodeName
                    // have to find tree path for that nodeName

                    addNodeIntoJtreeForSubTree(selectedCell);

                    if (firstAddedCellForSubTree == 0) {
                        firstAddedCellForSubTreeDeletion = (mxCell) obj;
                        firstAddedCellForSubTree = 1;
                    }

                } else if (nodeName.endsWith("MAsp")) {
                    obj = graph.insertVertex(parent, null, nodeName, x + 25, y, 30, 30, "Multiaspect");
                    graph.insertEdge(parent, null, "", selectedCell, obj);
                    lastAddedCell = null;

                    addNodeIntoJtreeForSubTree(selectedCell);

                    if (firstAddedCellForSubTree == 0) {
                        firstAddedCellForSubTreeDeletion = (mxCell) obj;
                        firstAddedCellForSubTree = 1;
                    }

                } else if (nodeName.endsWith("Spec")) {
                    obj = graph.insertVertex(parent, null, nodeName, x + 25, y, 30, 30, "Specialization");
                    graph.insertEdge(parent, null, "", selectedCell, obj);
                    lastAddedCell = null;

                    addNodeIntoJtreeForSubTree(selectedCell);

                    if (firstAddedCellForSubTree == 0) {
                        firstAddedCellForSubTreeDeletion = (mxCell) obj;
                        firstAddedCellForSubTree = 1;
                    }

                } else {
                    obj = graph.insertVertex(parent, null, nodeName, x, y, 80, 30, "Entity");
                    graph.insertEdge(parent, null, "", selectedCell, obj);

                    lastAddedCell = null;

                    addNodeIntoJtreeForSubTree(selectedCell);

                    if (firstAddedCellForSubTree == 0) {
                        firstAddedCellForSubTreeDeletion = (mxCell) obj;
                        firstAddedCellForSubTree = 1;
                    }

                }

                // added nodes in the array
                addedNodes[addedNodeCount] = obj;
                addedNodeCount++;

            } finally {
                graph.getModel().endUpdate();

            }

        }

        // have to make used array null and count 0 here
        nodeNamesForGraphSync = new String[100]; // or have to delete the added nodes in other way
        undoForSubTreeCount = treeSyncNodeCount; // not using have to check carefully then delete
        treeSyncNodeCount = 0;

    }

    public void addModuleFromSubgraphUniformity(Object obj) {
        // a small tree will be added dynamically to the jtree which is drawn in graph
        // editor and connected later to other node

        Object[] addedNodes = new Object[100];
        int addedNodeCount = 0;
        String[] nodeNameSplits = null;

        String[] nodeNames = new String[100];
        int nodeCount = 0;
        nodeCount = treeSyncNodeCount;
        nodeNames = nodeNamesForGraphSync;

        // deleting null nodes;
        nodeNames =
                Arrays.stream(nodeNames).filter(s -> (s != null && s.length() > 0)).toArray(String[]::new);

        // adding uniformity node to the array
        addedNodes[addedNodeCount] = obj;
        addedNodeCount++;

        for (int i = 0; i < nodeCount; i++) {
            String nodeName = nodeNames[i];

            if (nodeName.endsWith("hasparent")) {
                nodeNameSplits = nodeName.split("-");
                nodeName = nodeNameSplits[0];

                String parentNode = nodeNameSplits[1];
                for (Object node : addedNodes) {
                    if (!(node == null)) {
                        mxCell cell = (mxCell) node;
                        if (cell.getValue().equals(parentNode)) {
                            obj = cell;
                        }
                    }
                }
            }

            mxCell selectedCell = (mxCell) obj;

            Object[] outgoing = graph.getOutgoingEdges(selectedCell);

            mxCell selectedCellNew = null;
            int len = outgoing.length;

            double x;
            double y;

            if (len > 0) {

                Object targetCell = graph.getModel().getTerminal(outgoing[len - 1], false);
                selectedCellNew = (mxCell) targetCell;

                x = selectedCellNew.getGeometry().getX();
                y = selectedCellNew.getGeometry().getY();
                x = x + 100;

                // if the added node manually changed to other position then overlap may happen.
                // for removing that bug have to compare all the child nodes x positions and
                // have to
                // add after the most right one. didn't implement it yet. will do later after
                // first
                // prototype

            } else {
                x = selectedCell.getGeometry().getX();
                y = selectedCell.getGeometry().getY();
                y = y + 100;
            }

            String nodeId = "uniformity" + uniformityNodeNumber;

            graph.getModel().beginUpdate();
            try {
                if (nodeName.endsWith("Dec")) {
                    obj = graph.insertVertex(parent, nodeId, nodeName, x + 25, y, 30, 30, "Aspect");

                    graph.insertEdge(parent, null, "", selectedCell, obj);
                    lastAddedCell = null; // so that it will not cause duplicate addition in tree

                    uniformityNodeNumber++;

                    addNodeIntoJtreeForSubTree(selectedCell);

                } else if (nodeName.endsWith("MAsp")) {
                    obj = graph.insertVertex(parent, nodeId, nodeName, x + 25, y, 30, 30, "Multiaspect");
                    graph.insertEdge(parent, null, "", selectedCell, obj);
                    lastAddedCell = null;

                    uniformityNodeNumber++;

                    addNodeIntoJtreeForSubTree(selectedCell);

                } else if (nodeName.endsWith("Spec")) {
                    obj = graph.insertVertex(parent, nodeId, nodeName, x + 25, y, 30, 30, "Specialization");
                    graph.insertEdge(parent, null, "", selectedCell, obj);
                    lastAddedCell = null;

                    uniformityNodeNumber++;

                    addNodeIntoJtreeForSubTree(selectedCell);

                } else {
                    obj = graph.insertVertex(parent, nodeId, nodeName, x, y, 80, 30, "Entity");
                    graph.insertEdge(parent, null, "", selectedCell, obj);

                    lastAddedCell = null;

                    uniformityNodeNumber++;

                    addNodeIntoJtreeForSubTree(selectedCell);

                }

                // added nodes in the array
                addedNodes[addedNodeCount] = obj;
                addedNodeCount++;

            } finally {
                graph.getModel().endUpdate();

            }

        }

        // have to make used array null and count 0 here
        nodeNamesForGraphSync = new String[100]; // or have to delete the added nodes in other way
        undoForSubTreeCount = treeSyncNodeCount; // not using have to check carefully then delete
        treeSyncNodeCount = 0;

    }

    public void saveModuleFromCurrentModelAsXML(Object obj, String fileName) {

        mxCell cell = (mxCell) obj;
        Document calendarDOMDoc = null;
        try {
            DOMImplementation domImpl =
                    DocumentBuilderFactory.newInstance().newDocumentBuilder().getDOMImplementation();

            calendarDOMDoc = domImpl.createDocument(null, "start", null);

        } catch (ParserConfigurationException e1) {
            e1.printStackTrace(System.err);
        } catch (DOMException e2) {
            e2.printStackTrace(System.err);
        }

        calendarDOMDoc.getDocumentElement().appendChild(childNodes(calendarDOMDoc, cell));

        try {
            saveToXMLFileWithoutIndent(calendarDOMDoc, fileName + ".xml");

        } catch (TransformerException ex) {
            Logger.getLogger(SESEditor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void writeSaveModuleToFileAsXML(Object obj) {

        mxCell cell = (mxCell) obj;
        String fileName = cell.getValue().toString();
        // choosing file from a directory
        Path currentDirectory = Paths.get("").toAbsolutePath();
        String repFslas = currentDirectory.toString().replace("\\", "/");

        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter("xml files (*.xml)", "xml");
        fileChooser.setFileFilter(xmlfilter);
        fileChooser.setSelectedFile(new File(fileName));
        fileChooser.setCurrentDirectory(new File(SESEditor.fileLocation + "/" + SESEditor.projName));
        int result = fileChooser.showSaveDialog(SESEditor.framew);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            saveModuleFromCurrentModelAsXML(obj, selectedFile.getAbsolutePath());

        }

    }

    public void addNodeFromGraphPopup(String node, int x, int y) {

        String variableName = JOptionPane
                .showInputDialog(SESEditor.framew, "Node Name:", "New Node", JOptionPane.INFORMATION_MESSAGE);

        if (variableName != null) {
            variableName = variableName.replaceAll("\\s+", "");
        }

        Object obj;

        if ((variableName != null) && (!variableName.trim().isEmpty())) {
            graph.getModel().beginUpdate();
            try {
                if (node.endsWith("Dec")) {
                    if (!variableName.endsWith("Dec")) {
                        variableName = variableName + "Dec";
                    }
                    obj = graph.insertVertex(parent, null, variableName, x - 15, y, 30, 30, "Aspect");
                    nodeNumber++;

                } else if (node.endsWith("MAsp")) {
                    if (!variableName.endsWith("MAsp")) {
                        variableName = variableName + "MAsp";
                    }
                    obj = graph.insertVertex(parent, null, variableName, x - 15, y, 30, 30, "Multiaspect");
                    nodeNumber++;

                } else if (node.endsWith("Spec")) {
                    if (!variableName.endsWith("Spec")) {
                        variableName = variableName + "Spec";
                    }
                    obj = graph.insertVertex(parent, null, variableName, x - 15, y, 30, 30, "Specialization");
                    nodeNumber++;

                } else /* if (DynamicTreeDemo.nodeAddDetector.equals("entity")) */ {
                    obj = graph.insertVertex(parent, null, variableName, x - 40, y, 80, 30,
                            "Entity"); // "Aspect;fillColor=#0759cf;strokeColor=white;");
                    nodeNumber++;

                }

            } finally {
                graph.getModel().endUpdate();

            }
        }
    }

    public mxCell getRootNode() {
        Object[] cells = graph.getChildVertices(graph.getDefaultParent()); // getSelectionCells();

        mxCell rootcell = null;

        for (Object c : cells) {
            mxCell cell2 = (mxCell) c;
            if (cell2.isVertex()) {
                String val = cell2.getId();
                if (val.equals("rootnode")) {
                    rootcell = cell2;
                }

            }
        }

        return rootcell;
    }

    public void checkSubtreeNode(mxCell cell) {

        subtreeCheckLabel = cell.getValue().toString();
        subtreeCheckCell = cell;

        nextChildNodeForcheckSubtreeNode(getRootNode());

        subtreeCheckLabel = null;
        subtreeCheckCell = null;

    }

    public void nextChildNodeForcheckSubtreeNode(mxCell cell) {
        Object[] outgoing = graph.getOutgoingEdges(cell);

        if (outgoing.length > 0) {
            for (int i = 0; i < outgoing.length; i++) {
                Object targetCell = graph.getModel().getTerminal(outgoing[i], false);

                // for next call
                mxCell targetCell2 = (mxCell) targetCell;
                // System.out.println("targetCell2:"+targetCell2.getValue()+" and
                // subtreeCheckLabel:" + subtreeCheckLabel );

                if (targetCell2.getValue().toString().equals(subtreeCheckLabel)
                    && targetCell2.getId() != subtreeCheckCell.getId() && !(targetCell2.getId().startsWith(
                        "uniformity"))) {
                    // targetCell2.getId()!=subtreeCheckCell.getId()
                    // added the above check because search is depth first and to ignore the current
                    // added cell
                    // System.out.println("subtree needed here");

                    if (graph.getOutgoingEdges(targetCell2).length > 1) {

                        subtreeCheckCell.setId("uniformity" + uniformityNodeNumber + "RefNode");
                        uniformityNodeNumber++;

                        saveModuleFromCurrentModelSecondStep(targetCell2);
                        addModuleFromSubgraphUniformity(subtreeCheckCell);

                    } else {

                        subtreeCheckCell.setId("uniformity" + uniformityNodeNumber + "RefNode");
                        uniformityNodeNumber++;

                        saveModuleFromCurrentModel(targetCell2);
                        addModuleFromSubgraphUniformity(subtreeCheckCell);

                    }

                    break;
                } else {
                    nextChildNodeForcheckSubtreeNode(targetCell2);
                }
            }
        }

    }

    public void checkSubtreeNodeForSync(mxCell addedCellParent, mxCell addedCell) {

        subtreeSyncCell = addedCellParent;
        addedCellNameSync = addedCell.getValue().toString();
        nextChildNodeForcheckSubtreeNodeSync(getRootNode());

    }

    public void nextChildNodeForcheckSubtreeNodeSync(mxCell cell) {
        Object[] outgoing = graph.getOutgoingEdges(cell);

        if (outgoing.length > 0) {
            for (int i = 0; i < outgoing.length; i++) {
                Object targetCell = graph.getModel().getTerminal(outgoing[i], false);

                // for next call
                mxCell targetCell2 = (mxCell) targetCell;
                // System.out.println("targetCell2:"+targetCell2.getValue()+" and
                // subtreeSyncCell:" + subtreeSyncCell.getValue() );

                if (targetCell2.getValue().toString().equals(subtreeSyncCell.getValue())
                    && targetCell2.getId() != subtreeSyncCell.getId() && targetCell2.getId()
                            .startsWith("uniformity")) {
                    // targetCell2.getId()!=subtreeCheckCell.getId()
                    // added the above check because search is depth first and to ignore the current
                    // added cell
                    // System.out.println("have to sync subtree");

                    currentSelectedCell = targetCell2;
                    addNodeFromConsole(addedCellNameSync);
                    currentSelectedCell = null;

                    break;
                } else {
                    // System.out.println("--------------start-----------------");
                    // System.out.println(targetCell2.getValue().toString());
                    // System.out.println(targetCell2.getId().toString());
                    // System.out.println(subtreeSyncCell.getValue().toString());
                    // System.out.println(subtreeSyncCell.getId().toString());
                    // System.out.println("----------------end---------------");

                    nextChildNodeForcheckSubtreeNodeSync(targetCell2);
                }
            }
        }

    }

    /**
     * Check whether the deleting node contains any reference subtree or not. If it
     * has some reference nodes then it will also delete those at the same time.
     *
     * @param deleteCell
     */
    public void checkSubtreeNodeForSyncDelete(mxCell deleteCell) {

        subtreeSyncCell = deleteCell;
        nextChildNodeForcheckSubtreeNodeSyncDelete(getRootNode());

    }

    /**
     * Find next node in the path for deleting.
     *
     * @param deleteCell
     */
    public void nextChildNodeForcheckSubtreeNodeSyncDelete(mxCell deleteCell) {
        Object[] outgoing = graph.getOutgoingEdges(deleteCell);

        if (outgoing.length > 0) {
            for (int i = 0; i < outgoing.length; i++) {
                Object targetCell = graph.getModel().getTerminal(outgoing[i], false);

                // for next call
                mxCell targetCell2 = (mxCell) targetCell;

                System.out.println(targetCell2.getValue());

                if (targetCell2.getValue().toString().equals(subtreeSyncCell.getValue())
                    && targetCell2.getId() != subtreeSyncCell.getId() && targetCell2.getId()
                            .startsWith("uniformity")) {
                    // targetCell2.getId()!=subtreeCheckCell.getId()
                    // added the above check because search is depth first and to ignore the current
                    // added cell
                    // System.out.println("have to delete for sync");

                    // for deleting subtree reference
                    deleteNodeFromGraphPopupReferenceDeleteSync(targetCell2);

                    break;
                } else {

                    nextChildNodeForcheckSubtreeNodeSyncDelete(targetCell2);
                }
            }
        }

    }

    /**
     * Check whether the renaming node contains any reference node or not. If it has
     * any reference node then it will also rename that at the same time.
     *
     * @param renameCell
     * @param newName
     */
    public void checkSubtreeNodeForSyncRename(mxCell renameCell, String newName) {

        subtreeSyncCell = renameCell;
        nextChildNodeForcheckSubtreeNodeSyncRename(getRootNode(), newName);

    }

    /**
     * Find next node in the path for renaming.
     *
     * @param renameCell
     * @param newName
     */
    public void nextChildNodeForcheckSubtreeNodeSyncRename(mxCell renameCell, String newName) {
        Object[] outgoing = graph.getOutgoingEdges(renameCell);

        if (outgoing.length > 0) {
            for (int i = 0; i < outgoing.length; i++) {
                Object targetCell = graph.getModel().getTerminal(outgoing[i], false);

                // for next call
                mxCell targetCell2 = (mxCell) targetCell;

                System.out.println(targetCell2.getValue());

                if (targetCell2.getValue().toString().equals(subtreeSyncCell.getValue())
                    && targetCell2.getId() != subtreeSyncCell.getId() && targetCell2.getId()
                            .startsWith("uniformity")) {
                    // targetCell2.getId()!=subtreeCheckCell.getId()
                    // added the above check because search is depth first and to ignore the current
                    // added cell
                    // System.out.println("have to delete for sync");

                    // for deleting subtree reference
                    graph.getModel().beginUpdate();
                    try {
                        graph.getModel().setValue(targetCell2, newName);
                    } finally {
                        graph.getModel().endUpdate();
                    }

                    break;
                } else {

                    nextChildNodeForcheckSubtreeNodeSyncDelete(targetCell2);
                }
            }
        }

    }

    public void deleteNodeFromGraphPopup(Object pos) {

        // for deleting from tree at the same time
        mxCell cellForAddingVariable = (mxCell) pos;

        if (cellForAddingVariable.getId().startsWith("uniformity") && !cellForAddingVariable.getId()
                .endsWith("RefNode")) {

            boolean connected = SESEditor.jtreeTograph.isConnectedToRoot(cellForAddingVariable);
            connectedToRoot = false;
            if (!connected) {

                graph.getModel().beginUpdate();
                try {
                    graph.removeCells(new Object[] {cellForAddingVariable});
                } finally {
                    graph.getModel().endUpdate();
                }
            } else {
                JOptionPane.showMessageDialog(SESEditor.framew,
                        "You can not delete from here. Delete from the reference node.");
                String nameOfReferenceNode = cellForAddingVariable.getValue().toString();
            }

        } else {

            pathToRoot.add((String) cellForAddingVariable.getValue());
            nodeToRootPathVar(cellForAddingVariable);

            String[] stringArray = pathToRoot.toArray(new String[0]);
            ArrayList<String> pathToRootRev = new ArrayList<String>();

            for (int i = stringArray.length - 1; i >= 0; i--) {
                pathToRootRev.add(stringArray[i]);
            }

            String[] stringArrayRev = pathToRootRev.toArray(new String[0]);

            TreePath treePathForVariable = SESEditor.getTreeNodePath(stringArrayRev);

            // calling function for deleting the node
            // System.out.println(treePathForVariable);
            SESEditor.treePanel.removeCurrentNodeWithGraphDelete(treePathForVariable);

            pathToRoot.clear();

            // reference delete syn
            if (!cellForAddingVariable.getId().endsWith("RefNode")) {
                checkSubtreeNodeForSyncDelete(cellForAddingVariable);
            }

            // if i put these delete section above tree node delete then it will not work
            // because before detecting it is deleting the node and could not find tree path

            // Object delcell = pos;
            final Toolkit toolkit = Toolkit.getDefaultToolkit();

            if (cellForAddingVariable != null) {

                graph.getModel().beginUpdate();
                try {
                    if ("rootnode".equals(cellForAddingVariable.getId())) {
                        toolkit.beep();

                    } else {
                        deletableChildNodes.add(cellForAddingVariable);
                        deleteAllChildNode(cellForAddingVariable);
                        mxCell[] allnodes = deletableChildNodes.toArray(new mxCell[0]);
                        for (int i = 0; i < allnodes.length; i++) {
                            mxCell a = allnodes[i];
                            graph.removeCells(new Object[] {a});

                            deletableChildNodes.clear();
                        }

                    }

                } finally {
                    graph.getModel().endUpdate();
                }

            }

        }

    }

    public void deleteNodeFromGraphPopupReferenceDeleteSync(Object pos) {

        // for deleting from tree at the same time
        mxCell cellForAddingVariable = (mxCell) pos;

        pathToRoot.add((String) cellForAddingVariable.getValue());
        nodeToRootPathVar(cellForAddingVariable);

        String[] stringArray = pathToRoot.toArray(new String[0]);
        ArrayList<String> pathToRootRev = new ArrayList<String>();

        for (int i = stringArray.length - 1; i >= 0; i--) {
            pathToRootRev.add(stringArray[i]);
        }

        String[] stringArrayRev = pathToRootRev.toArray(new String[0]);

        TreePath treePathForVariable = SESEditor.getTreeNodePath(stringArrayRev);

        // calling function for deleting the node
        // System.out.println(treePathForVariable);
        SESEditor.treePanel.removeCurrentNodeWithGraphDelete(treePathForVariable);

        pathToRoot.clear();

        // if i put these delete section above tree node delete then it will not work
        // because before detecting it is deleting the node and could not find tree path

        // Object delcell = pos;
        final Toolkit toolkit = Toolkit.getDefaultToolkit();

        if (cellForAddingVariable != null) {

            graph.getModel().beginUpdate();
            try {
                if ("rootnode".equals(cellForAddingVariable.getId())) {
                    toolkit.beep();

                } else {
                    deletableChildNodes.add(cellForAddingVariable);
                    deleteAllChildNode(cellForAddingVariable);
                    mxCell[] allnodes = deletableChildNodes.toArray(new mxCell[0]);
                    for (int i = 0; i < allnodes.length; i++) {
                        mxCell a = allnodes[i];
                        graph.removeCells(new Object[] {a});

                        deletableChildNodes.clear();
                    }

                }

            } finally {
                graph.getModel().endUpdate();
            }

        }

    }

    public void deleteEdgeFromGraphPopup(Object pos) {

        Object cell = graph.getModel().getTerminal(pos, false);
        mxCell targetCell = (mxCell) cell;

        // for deleting from tree at the same time
        pathToRoot.add((String) targetCell.getValue());
        nodeToRootPathVar(targetCell);

        String[] stringArray = pathToRoot.toArray(new String[0]);
        ArrayList<String> pathToRootRev = new ArrayList<String>();

        for (int i = stringArray.length - 1; i >= 0; i--) {
            pathToRootRev.add(stringArray[i]);
        }

        String[] stringArrayRev = pathToRootRev.toArray(new String[0]);

        TreePath treePathForVariable = SESEditor.getTreeNodePath(stringArrayRev);

        // calling function for deleting the node
        SESEditor.treePanel.removeCurrentNodeWithGraphDelete(treePathForVariable);

        pathToRoot.clear();

        graph.getModel().beginUpdate();
        try {
            graph.removeCells(new Object[] {pos});
        } finally {
            graph.getModel().endUpdate();
        }

    }

    public void deleteAllChildNode(mxCell cell) {
        Object[] outgoing = graph.getOutgoingEdges(cell);

        if (outgoing.length > 0) {
            for (int i = 0; i < outgoing.length; i++) {
                Object targetCell = graph.getModel().getTerminal(outgoing[i], false);
                mxCell targetCell2 = (mxCell) targetCell;
                deletableChildNodes.add(targetCell2);
                deleteAllChildNode(targetCell2);

            }
        }

    }

    public void deleteNodeFromGraphPopupForSubTree(Object pos) {

        // for deleting from tree at the same time
        mxCell cellForAddingVariable = (mxCell) pos;

        // Object delcell = pos;
        final Toolkit toolkit = Toolkit.getDefaultToolkit();

        if (cellForAddingVariable != null) {

            graph.getModel().beginUpdate();
            try {
                if ("rootnode".equals(cellForAddingVariable.getId())) {
                    toolkit.beep();

                } else {
                    deletableChildNodes.add(cellForAddingVariable);
                    deleteAllChildNode(cellForAddingVariable);
                    mxCell[] allnodes = deletableChildNodes.toArray(new mxCell[0]);
                    for (int i = 0; i < allnodes.length; i++) {
                        mxCell a = allnodes[i];
                        graph.removeCells(new Object[] {a});

                        deletableChildNodes.clear();
                    }

                }

            } finally {
                graph.getModel().endUpdate();
            }

        }

    }

    public void deleteNodeWithTree(String[] nodesToSelectedNode) {

        rootToSelectedNode(nodesToSelectedNode);

        // below codes also used in another function. i can make a separate function
        // using this to make the code more understandable and organized.
        final Toolkit toolkit = Toolkit.getDefaultToolkit();

        if (lastNodeInPath != null) {

            graph.getModel().beginUpdate();
            try {
                if ("rootnode".equals(lastNodeInPath.getId())) {
                    toolkit.beep();

                } else {
                    deletableChildNodes.add(lastNodeInPath);
                    deleteAllChildNode(lastNodeInPath);
                    mxCell[] allnodes = deletableChildNodes.toArray(new mxCell[0]);
                    for (int i = 0; i < allnodes.length; i++) {
                        mxCell a = allnodes[i];
                        System.err.println(a.getValue());
                        graph.removeCells(new Object[] {a});

                        deletableChildNodes.clear();
                    }

                }

            } finally {
                graph.getModel().endUpdate();
            }

        }

        // this variable is using by many functions. so have to make it null here so
        // that it will not affect others
        lastNodeInPath = null;
    }

    /**
     * Rename node name of both graph node and jtree node. If the graph root has
     * child then it will show an message that node having child can't be renamed.
     * It take selected mxCell object as a parameter for changing the name.
     *
     * @param pos
     */
    public void renameCell(Object pos) {

        mxCell cell = (mxCell) pos;

        if (cell.getId().equals("rootnode")) {

            Object[] outgoing = graph.getOutgoingEdges(cell);

            if (outgoing.length == 0) {

                String newName = JOptionPane.showInputDialog(SESEditor.framew, "New Name", "Rename Node",
                        JOptionPane.PLAIN_MESSAGE);
                newName = newName.replaceAll("\\s+", "");

                if ((newName != null) && (!newName.trim().isEmpty())) {

                    // jtree
                    if (cell.getId().equals("rootnode")) {
                        // assigning new projectname and root to tree
                        DefaultMutableTreeNode rootNode2 = new DefaultMutableTreeNode(newName);
                        SESEditor.treePanel.treeModel.setRoot(rootNode2);
                        SESEditor.treePanel.treeModel.reload();
                    }

                    // for graph

                    graph.getModel().beginUpdate();
                    try {
                        graph.getModel().setValue(cell, newName);
                    } finally {
                        graph.getModel().endUpdate();
                    }

                }

                checkSubtreeNodeForSyncRename(cell, newName);

            } else {
                JOptionPane.showMessageDialog(SESEditor.framew,
                        "You can't rename a root node having child node.");
            }
        } else {

            String newName = JOptionPane
                    .showInputDialog(SESEditor.framew, "New Name", "Rename Node", JOptionPane.PLAIN_MESSAGE);

            if (newName != null) {
                newName = newName.replaceAll("\\s+", "");
            }

            if ((newName != null) && (!newName.trim().isEmpty())) {

                // if the new name is not correct then change it according to cell type.
                String currentName = (String) cell.getValue();

                if (currentName.endsWith("Dec")) {
                    if (!newName.endsWith("Dec")) {
                        newName = newName + "Dec";
                    }
                } else if (currentName.endsWith("Spec")) {
                    if (!newName.endsWith("Spec")) {
                        newName = newName + "Spec";
                    }
                } else if (currentName.endsWith("MAsp")) {
                    if (!newName.endsWith("MAsp")) {
                        newName = newName + "MAsp";
                    }
                }

                // for jtree

                // if a node is not connected to root then it will not try to change
                // corresponding node name from tree
                checkRootConnectivity(cell);

                if (connectedToRoot) {

                    pathToRoot.add((String) cell.getValue());
                    nodeToRootPathVar(cell);

                    String[] stringArray = pathToRoot.toArray(new String[0]);
                    ArrayList<String> pathToRootRev = new ArrayList<String>();

                    for (int i = stringArray.length - 1; i >= 0; i--) {
                        pathToRootRev.add(stringArray[i]);
                    }

                    String[] stringArrayRev = pathToRootRev.toArray(new String[0]);

                    TreePath treePathForRename = SESEditor.getTreeNodePath(stringArrayRev);

                    SESEditor.treePanel.treeModel.valueForPathChanged(treePathForRename, newName);

                    pathToRoot.clear();
                }

                connectedToRoot = false;

                // for graph

                graph.getModel().beginUpdate();
                try {
                    graph.getModel().setValue(cell, newName);

                } finally {
                    graph.getModel().endUpdate();
                }

            }

        }
    }

    /**
     * Used to rename the current root of the graph. It takes new root name as an
     * argument and change the name accordingly.
     *
     * @param newRootName
     */
    public void renameRootNode(String newRootName) {

        Object[] cells = graph.getChildVertices(graph.getDefaultParent()); // getSelectionCells();

        mxCell rootcell = null;

        for (Object c : cells) {
            mxCell cell2 = (mxCell) c; // casting
            if (cell2.isVertex()) {
                String val = cell2.getId();
                if (val.equals("rootnode")) {
                    rootcell = cell2;
                }

            }
        }

        if (rootcell != null) {
            graph.getModel().beginUpdate();
            try {
                graph.getModel().setValue(rootcell, newRootName);
            } finally {
                graph.getModel().endUpdate();
            }

        }

    }

    /**
     * This function deletes all the nodes except root node during new project
     * creation. Also changes the root name with new root name which is coming as an
     * argument.
     *
     * @param newRootName
     */

    public void deleteAllNodesFromGraphWindow(String newRootName) {

        graph.getModel().beginUpdate();
        try {
            Object[] cells = graph.getChildVertices(graph.getDefaultParent());
            for (Object x : cells) {
                mxCell cell = (mxCell) x;

                if (cell.getId().equals("rootnode")) {
                    graph.getModel().setValue(cell, newRootName);

                } else {
                    graph.removeCells(new Object[] {x});

                }
            }

        } finally {
            graph.getModel().endUpdate();
        }
    }

    /**
     * This function add two nodes at Top-Right and Bottom-Left corner to make the
     * page big enough during new project creation.
     */

    public void addPageLengthNodes() {

        graph.getModel().beginUpdate();
        try {
            Object hideV = graph.insertVertex(parent, "hideV", "End of Canvas", 0, 50000, 80, 30, "Entity");
            mxCell hidenodeV = (mxCell) hideV;
            // hidenode.setVisible(false);

            Object hideH = graph.insertVertex(parent, "hideH", "End of Canvas", 50000, 0, 80, 30, "Entity");
            mxCell hidenodeH = (mxCell) hideH;
            // hidenode.setVisible(false);

        } finally {
            graph.getModel().endUpdate();
        }
    }

    /**
     * Open existing project from disk. Read the XML file from the specified
     * location with the provided file name then according to XML file changes the
     * parent and update the graph after accordingly.
     *
     * @param filename
     * @param oldProjectTreeProjectName
     */
    public void openExistingProject(String filename, String oldProjectTreeProjectName) {

        parent = graph.getDefaultParent();

        ssdFileGraph =
                new File(SESEditor.fileLocation + "/" + SESEditor.projName + "/" + filename + "Graph.xml");

        if (ssdFileGraph.exists()) {
            graph.getModel().beginUpdate();
            try {

                Document xml = mxXmlUtils.parseXml(mxUtils.readFile(
                        SESEditor.fileLocation + "/" + SESEditor.projName + "/" + filename + "Graph.xml"));
                mxCodec codec = new mxCodec(xml);
                codec.decode(xml.getDocumentElement(), graph.getModel());
                parent = graph.getDefaultParent();

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                graph.getModel().endUpdate();
            }

            SESEditor.treePanel.openExistingProject(filename, oldProjectTreeProjectName);

        }

    }

    /**
     * Add constraint to the aspect node in the SES XML structure.
     */
    public void addconstraintToSESStructure() {
        TreePath keyPath = null;
        String constraint = "";

        for (TreePath keyPath2 : DynamicTree.constraintsList.keySet()) {
            keyPath = keyPath2;

        }

        if (keyPath != null) {

            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) (keyPath.getLastPathComponent());
            TreeNode[] nodes = currentNode.getPath();
            int len = nodes.length;
            String[] sesNodesInPath = new String[len];

            String[] nodesToSelectedNode = new String[100];

            int b = 0;

            for (TreePath key : DynamicTree.constraintsList.keySet()) {
                int a = 0;

                for (String value : DynamicTree.constraintsList.get(key)) {

                    DefaultMutableTreeNode currentNode2 =
                            (DefaultMutableTreeNode) (key.getLastPathComponent());

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

            nodesToSelectedNode =
                    Arrays.stream(nodesToSelectedNode).filter(s -> (s != null && s.length() > 0))
                            .toArray(String[]::new);

            for (String con : nodesToSelectedNode) {
                if (constraint.equals("")) {
                    constraint = con;
                } else {
                    constraint = constraint + ", " + con;
                }

            }

            for (int i = 0; i < len; i++) {
                if (nodes[i].toString().endsWith("Dec")) {
                    sesNodesInPath[i] = "aspect";
                } else if (nodes[i].toString().endsWith("MAsp")) {
                    sesNodesInPath[i] = "multiAspect";
                } else if (nodes[i].toString().endsWith("Spec")) {
                    sesNodesInPath[i] = "specialization";
                } else {
                    sesNodesInPath[i] = "entity";
                }
            }
            FileConvertion fileConvertion = new FileConvertion();
            fileConvertion.addConstraintToSESStructure(sesNodesInPath, constraint);

        }
    }

}
