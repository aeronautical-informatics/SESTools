/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dlr.ses.seseditor;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTree;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <h1>TreePopup</h1>
 * <p>
 * This class implements the JTree node pop up action of the SES JTree displayed
 * in the left side of the editor. Only few actions are implemented here because
 * all the actions are implemented in graphical panel right click option. Node
 * addition and deletion and variable deletion options are added in the pop pup
 * action of the SES JTree.
 * </p>
 *
 * @author Bikash Chandra Karmokar
 * @version 1.0
 */
public class TreePopup extends JPopupMenu {

    public TreePopup(JTree tree) {
        JMenuItem itemAdd = new JMenuItem("Add Node");
        JMenuItem itemVar = new JMenuItem("Add Variable");
        JMenuItem itemVarDel = new JMenuItem("Delete Variable");
        JMenuItem itemVarDelAll = new JMenuItem("Delete All Variable");
        JMenuItem itemDelete = new JMenuItem("Delete Node");

        itemAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                SESEditor.popUpActionAdd();

            }
        });

        itemVar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                SESEditor.popUpActionAddVariable();

            }
        });

        itemVarDel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                SESEditor.popUpActionDeleteVariable();

            }
        });

        itemVarDelAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                SESEditor.popUpActionDeleteAllVariables();

            }
        });

        itemDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                SESEditor.popUpActionDelete();
            }
        });

        add(itemAdd);
        add(new JSeparator());
        // add(itemVar);
        // add(new JSeparator());
        // add(itemVarDel);
        // add(new JSeparator());
        add(itemVarDelAll);
        add(new JSeparator());
        add(itemDelete);
        // add(new JSeparator());
        // add(itemDeleteAll);

    }
}
