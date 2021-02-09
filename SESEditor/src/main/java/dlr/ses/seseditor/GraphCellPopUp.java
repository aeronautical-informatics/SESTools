/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dlr.ses.seseditor;

import com.mxgraph.model.mxCell;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <h1>GraphCellPopUp</h1>
 * <p>
 * This class implements right click action of the mouse on the node in the
 * graphical editor. It initiates all the mouse actions such as add variable,
 * delete variable, rename, delete node etc which are used in the graphical
 * editor for modifying SES nodes.
 * </p>
 *
 * @author Bikash Chandra Karmokar
 * @version 1.0
 */
class GraphCellPopUp extends JPopupMenu {

    public GraphCellPopUp(int x, int y, Object pos) {
        JMenuItem itemVar = new JMenuItem("Add Variable");
        JMenuItem itemVarDel = new JMenuItem("Delete Variable");
        JMenuItem itemVarDelAll = new JMenuItem("Delete All Variables");
        JMenuItem itemRename = new JMenuItem("Rename");
        JMenuItem itemDel = new JMenuItem("Delete");
        JMenuItem itemAddModule = new JMenuItem("Add Module");
        JMenuItem itemSaveModule = new JMenuItem("Save Module");
        JMenuItem itemConstraint = new JMenuItem("Add Constraint");
        JMenuItem itemConstraintDelAll = new JMenuItem("Delete All Constraint");
        JMenuItem itemDelEdge = new JMenuItem("Delete Edge");

        itemVar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                SESEditor.jtreeTograph.addVariableFromGraphPopup(pos);

            }
        });

        itemVarDel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                SESEditor.jtreeTograph.deleteVariableFromGraphPopup(pos);

            }
        });

        itemVarDelAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                SESEditor.jtreeTograph.deleteAllVariablesFromGraphPopup(pos);

            }
        });

        itemRename.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                SESEditor.jtreeTograph.renameCell(pos);

            }
        });

        itemDel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                SESEditor.jtreeTograph.deleteNodeFromGraphPopup(pos);

            }
        });

        itemDelEdge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SESEditor.jtreeTograph.deleteEdgeFromGraphPopup(pos);

            }
        });

        itemAddModule.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                SESEditor.jtreeTograph.addModuleFromOtherModelAsXML(pos);

            }
        });

        itemSaveModule.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                SESEditor.jtreeTograph.writeSaveModuleToFileAsXML(pos);

            }
        });

        itemConstraint.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                SESEditor.jtreeTograph.addConstraintFromGraphPopup(pos);

            }
        });

        itemConstraintDelAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                SESEditor.jtreeTograph.deleteAllConstraintFromGraphPopup(pos);

            }
        });

        mxCell cell = (mxCell) pos;
        String cellName = (String) cell.getValue();
        boolean connected = SESEditor.jtreeTograph.isConnectedToRoot(cell);
        JtreeToGraph.connectedToRoot = false; // have to assign false because isConnectedToRoot() function
        // assign true during calling

        if (cell.isVertex()) {
            if (cell.getId().startsWith("uniformity") && connected) {
                if (cell.getId().endsWith("RefNode")) {
                    add(itemDel);
                } else {
                    // nothing
                }

            } else {
                add(itemVar);
                add(new JSeparator());
                add(itemRename);
                add(new JSeparator());
                add(itemVarDel);
                add(new JSeparator());
                add(itemVarDelAll);
                add(new JSeparator());
                add(itemDel);
                add(new JSeparator());
                add(itemAddModule);
                add(new JSeparator());
                add(itemSaveModule);
                if (cellName.endsWith("Dec")) {
                    add(new JSeparator());
                    add(itemConstraint);
                    add(new JSeparator());
                    add(itemConstraintDelAll);
                }
            }
        } else {
            add(itemDelEdge);
        }

    }
}
