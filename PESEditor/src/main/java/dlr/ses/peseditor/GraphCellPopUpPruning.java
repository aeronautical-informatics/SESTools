package dlr.ses.peseditor;


import com.mxgraph.model.mxCell;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * <h1>GraphCellPopUpPruning</h1>
 * <p>
 * This class initiates all the pruning operation of the editor. By right
 * clicking on the selected node prune option can be found. Based on the node
 * type automatically node specific prune operation will be executed.
 * </p>
 *
 * @author Bikash Chandra Karmokar
 * @version 1.0
 */
public class GraphCellPopUpPruning extends JPopupMenu {

    public GraphCellPopUpPruning(int x, int y, Object pos) {
        JMenuItem itemPruneSpec = new JMenuItem("Prune It");
        JMenuItem itemPruneDec = new JMenuItem("Prune It");
        JMenuItem itemPruneMAsp = new JMenuItem("Prune It");
        JMenuItem itemPruneSiblings = new JMenuItem("Prune It");

        itemPruneSpec.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                PESEditor.jtreeTograph.pruneNodeFromGraphPopup(pos);

            }
        });

        itemPruneDec.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                //DynamicTreeDemo.jtreeTograph.pruneNodeFromGraphPopup(pos);

            }
        });

        itemPruneMAsp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                PESEditor.jtreeTograph.pruneMAspNodeFromGraphPopup(pos);

            }
        });

        itemPruneSiblings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                PESEditor.jtreeTograph.pruneSiblingsFromGraphPopup(pos);

            }
        });


        mxCell cell = (mxCell) pos;
        String cellName = (String) cell.getValue();

        if (cellName.endsWith("Dec")) {
            //add(itemPruneDec);

        } else if (cellName.endsWith("MAsp")) {
            add(itemPruneMAsp);

        } else if (cellName.endsWith("Spec")) {
            add(itemPruneSpec);
        } else {
            add(itemPruneSiblings);
        }
    }

}
