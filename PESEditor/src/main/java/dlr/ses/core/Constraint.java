/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dlr.ses.core;

import dlr.ses.peseditor.JtreeToGraph;
import dlr.ses.peseditor.PESEditor;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author BIKASH
 */
public class Constraint extends JPanel {

    public static JTable table;
    public static DefaultTableModel model;

    String[][] data;
    JScrollPane jp;

    public Constraint() {
        // super(new BorderLayout());
        setLayout(new GridLayout(1, 0)); // rows,cols

        String[] columnNames = {"Constraints"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable();
        table.setModel(model);

        table.setPreferredScrollableViewportSize(new Dimension());
        table.setFillsViewportHeight(true);
        table.setShowVerticalLines(true);
        table.setEnabled(false);
        // row listener
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable target = (JTable) e.getSource();

                    Point point = e.getPoint();
                    int row = table.rowAtPoint(point);

                    String constraints = (String) target.getModel().getValueAt(row, 0);

                    updateTableData(constraints);
                    // updateTableData("constraints");

                }
            }
        });

        // Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        // Add the scroll pane to this panel.
        add(scrollPane);
        // add(name);
        // add(name2);

        setNullRowsToVariableTable();

    }

    // public void showNodeValuesInTable(String selectedNode, Collection<String>
    // nodeVariables) {

    public static void setNullRowsToVariableTable() {
        for (int i = 0; i < 100; i++) {
            model.addRow(new Object[] {""});
        }

    }

    // public void showConstraintsInTable2() {
    // // try {
    //
    // DefaultTableModel dtm = (DefaultTableModel) table.getModel();
    // dtm.setRowCount(0); // for deleting previous table content
    //
    // for (String value : DynamicTreeDemo.treePanel.constraintsList) {
    //
    // if (value == null) {
    // model.addRow(new Object[] { "" });
    // } else {
    // model.addRow(new Object[] { value });
    // }
    //
    // }
    //
    // setNullRowsToVariableTable();
    //
    // }

    public static void setNullToAllRows() {
        DefaultTableModel dtm = (DefaultTableModel) table.getModel();
        dtm.setRowCount(0); // for deleting previous table content

        for (int i = 0; i < 100; i++) {
            model.addRow(new Object[] {""});
        }

    }

    public void showConstraintsInTable(String[] nodesToSelectedNode) {

        DefaultTableModel dtm = (DefaultTableModel) table.getModel();
        dtm.setRowCount(0); // for deleting previous table content

        for (String value : nodesToSelectedNode) {
            if (value == null) {
                model.addRow(new Object[] {""});
            } else {
                model.addRow(new Object[] {value});
                //System.out.println(value);
            }

        }

        setNullRowsToVariableTable();

    }

    public void updateTableData(String constraints) {

        // multiple input for variable---------------------------------
        JTextArea constraintsField = new JTextArea(10, 30);
        constraintsField.setLineWrap(true);
        constraintsField.setWrapStyleWord(true);

        constraintsField.setText(constraints);

        String constraintsOld = constraints;

        Object[] message = {"Constraints:", constraintsField};

        int option = JOptionPane
                .showConfirmDialog(PESEditor.framew, message, "Please Update", JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            constraints = constraintsField.getText();

            // boolean validInput = (constraintsField.getText() != null) &&
            // (!constraintsField.getText().trim().isEmpty());

            // if (validInput) {

            // DynamicTreeDemo.treePanel.constraintsList.remove(constraintsOld);
            // DynamicTreeDemo.treePanel.constraintsList.add(constraints);
            // showConstraintsInTable();
            // }


            PESEditor.jtreeTograph.deleteConstraintFromScenarioTableForUpdate(
                    JtreeToGraph.selectedNodeCellForVariableUpdate, constraintsOld, constraints);

        }

    }

}
