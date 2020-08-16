/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dlr.ses.core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import dlr.ses.peseditor.PESEditor;

/**
 *
 * @author BIKASH
 */
public class Variable extends JPanel {

	public static JTable table;
	public static DefaultTableModel model;

	public static JTextField name = new JTextField();
	public static JTextField name2 = new JTextField();

	String[][] data;
	JScrollPane jp;

	public Variable() {
		// super(new BorderLayout());
		setLayout(new GridLayout(1, 0));// rows,cols

		String[] columnNames = { "Node Name", "Variables", "Type", "Default Value", "Lower Bound", "Upper Bound" };
		model = new DefaultTableModel(columnNames, 0);
		table = new JTable();
		table.setModel(model);

		table.setPreferredScrollableViewportSize(new Dimension());
		table.setFillsViewportHeight(true);
		table.setShowVerticalLines(true);
		table.setEnabled(false);
		// row listener
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		table.addMouseListener((MouseListener) new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					JTable target = (JTable) e.getSource();

					Point point = e.getPoint();
					int row = table.rowAtPoint(point);

					String nodeName = (String) target.getModel().getValueAt(0, 0);
					String variableName = (String) target.getModel().getValueAt(row, 1);
					String variableType = (String) target.getModel().getValueAt(row, 2);
					String variableValue = (String) target.getModel().getValueAt(row, 3);
					String lowerBound = (String) target.getModel().getValueAt(row, 4);
					String uperBound = (String) target.getModel().getValueAt(row, 5);

					String rowValues = nodeName + " " + variableName + " " + variableType + " " + variableValue + " "
							+ lowerBound + " " + uperBound + " ";
					// Console.addConsoleOutput(rowValues);

					updateTableData(nodeName, variableName, variableType, variableValue, lowerBound, uperBound);

				}
			}
		});

		// Create the scroll pane and add the table to it.
		JScrollPane scrollPane = new JScrollPane(table);

		// Add the scroll pane to this panel.
		add(scrollPane);
		// add(name);
		// add(name2);

	}

	// public void showNodeValuesInTable(String selectedNode, Collection<String>
	// nodeVariables) {
	public void showNodeValuesInTable(String selectedNode, String[] nodeVariables) {
		// try {

		DefaultTableModel dtm = (DefaultTableModel) table.getModel();
		dtm.setRowCount(0);// for deleting previous table content
		String[] properties = null;
		int a = 0;
		String valueFirst = "";

		for (String value : nodeVariables) {
			if (a == 0) {

				if (value == null) {
					model.addRow(new Object[] { selectedNode, "", "", "", "", "" });
				} else {
					properties = value.split(",");

					if (properties[1].toString().equals("string") || properties[1].toString().equals("boolean")) {
						model.addRow(new Object[] { selectedNode, properties[0], properties[1], properties[2] });
					} else {
						model.addRow(new Object[] { selectedNode, properties[0], properties[1], properties[2],
								properties[3], properties[4] });
					}
				}

				valueFirst = value;
				a = 1;
			}
		}
		// "every $x in .//* satisfies empty($x//*[@name = $x/@name]) "}
		// if (properties.length > 4) {
		// model.addRow(new Object[] { selectedNode, properties[0], properties[1],
		// properties[2], properties[3],properties[4] });
		// }
		// model.addRow(new Object[] { selectedNode,valueFirst});
		// System.out.println("so far ok");

		for (String value : nodeVariables) {
			if (a == 1) {
				a = 0;
				continue;
			}
			// model.addRow(new Object[]{"",value,""});
			if (value == null) {
				// do nothing
			} else {
				properties = value.split(",");

				if (properties[1].toString().equals("string") || properties[1].toString().equals("boolean")) {
					model.addRow(new Object[] { selectedNode, properties[0], properties[1], properties[2] });
				} else {
					model.addRow(new Object[] { selectedNode, properties[0], properties[1], properties[2],
							properties[3], properties[4] });
				}
			}

		}

		setNullRowsToVariableTable();

	}

	public static void setNullRowsToVariableTable() {
		for (int i = 0; i < 100; i++) {
			model.addRow(new Object[] { "", "", "", "", "", "" });
		}

	}

	public static void setNullToAllRows() {
		DefaultTableModel dtm = (DefaultTableModel) table.getModel();
		dtm.setRowCount(0);// for deleting previous table content

		for (int i = 0; i < 100; i++) {
			model.addRow(new Object[] { "" });
		}

	}

	// selectedType is using in below function: addVariableFromGraphPopup
	String selectedType = "boolean";

	public void updateTableData(String nodeName, String variableName, String variableType, String variableValue,
			String variableLowerBound, String variableUpperBound) {

		// multiple input for variable---------------------------------
		JTextField nodeNameleField = new JTextField();
		nodeNameleField.setEditable(false);
		JTextField variableField = new JTextField();
		// JTextField variableTypeField = new JTextField();
		JTextField valueField = new JTextField();
		JTextField lowerBoundField = new JTextField();
		JTextField upperBoundField = new JTextField();

		// for validation of input
		JLabel errorLabelField = new JLabel();
		errorLabelField.setForeground(Color.RED);
		errorLabelField.setVisible(false);

		String[] typeList = { "boolean", "int", "float", "double", "string" };

		String variableFieldRegEx = "[a-zA-Z_][a-zA-Z0-9_]*";

		JComboBox variableTypeField = new JComboBox(typeList);

		variableTypeField.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					selectedType = variableTypeField.getSelectedItem().toString();

					if (selectedType.equals("string") || selectedType.equals("boolean")) {
						lowerBoundField.setText(null);
						upperBoundField.setText(null);
						lowerBoundField.setEnabled(false);
						upperBoundField.setEnabled(false);
						// System.out.println(selectedType);
					} else {
						lowerBoundField.setEnabled(true);
						upperBoundField.setEnabled(true);

					}

					// --------------
					if (selectedType.equals("boolean")) {
						if (variableField.getText().trim().matches(variableFieldRegEx)
								&& (valueField.getText().trim().equals("true")
										|| variableField.getText().trim().equals("false"))) {
							errorLabelField.setVisible(false);
						} else {
							errorLabelField.setVisible(true);
							errorLabelField.setText("Invalid Input. Check All Values.");

						}
					} else if (selectedType.equals("string")) {
						if (variableField.getText().trim().matches(variableFieldRegEx)
								&& valueField.getText().trim().matches(variableFieldRegEx)) {
							errorLabelField.setVisible(false);
						} else {
							errorLabelField.setVisible(true);
							errorLabelField.setText("Invalid Input. Check All Values.");

						}
					} else if (selectedType.equals("double")) {
						if (valueField.getText().trim().matches("^\\d*\\.\\d+")
								&& variableField.getText().trim().matches(variableFieldRegEx)
								&& lowerBoundField.getText().trim().matches("^\\d*\\.\\d+")
								&& upperBoundField.getText().trim().matches("^\\d*\\.\\d+")) {
							errorLabelField.setVisible(false);
						} else {
							errorLabelField.setVisible(true);
							errorLabelField.setText("Invalid Input. Check All Values.");

						}
					} else {
						if (variableField.getText().trim().matches(variableFieldRegEx)
								&& valueField.getText().trim().matches("^[0-9]+")
								&& lowerBoundField.getText().trim().matches("^[0-9]+")
								&& upperBoundField.getText().trim().matches("^[0-9]+")) {
							errorLabelField.setVisible(false);
						} else {
							errorLabelField.setVisible(true);
							errorLabelField.setText("Invalid Input. Check All Values.");

						}
					}
					// ---

				}
			}
		});

		nodeNameleField.setText(nodeName);
		variableField.setText(variableName);
		variableTypeField.setSelectedItem(variableType);
		valueField.setText(variableValue);
		lowerBoundField.setText(variableLowerBound);
		upperBoundField.setText(variableUpperBound);

		String variableNameOld = null;

		if (selectedType.equals("string") || selectedType.equals("boolean")) {
			lowerBoundField.setText(null);
			upperBoundField.setText(null);
			lowerBoundField.setEnabled(false);
			upperBoundField.setEnabled(false);

			errorLabelField.setVisible(false);
			// have to check why without this gives error during opening this form even
			// value is correct.during debugging i saw if condition is not working and make
			// the error label visible but values was correct
		} else {
			lowerBoundField.setEnabled(true);
			upperBoundField.setEnabled(true);

		}

		if (selectedType.equals("string") || selectedType.equals("boolean")) {
			variableNameOld = variableName + "," + variableType + "," + variableValue;
		} else {
			variableNameOld = variableName + "," + variableType + "," + variableValue + "," + variableLowerBound + ","
					+ variableUpperBound;
		}

		variableField.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {

				if (selectedType.equals("string")) {
					if (variableField.getText().trim().matches(variableFieldRegEx)
							&& valueField.getText().trim().matches(variableFieldRegEx)) {
						errorLabelField.setVisible(false);
					} else {
						errorLabelField.setVisible(true);
						errorLabelField.setText("Invalid Input. Check All Values.");

					}
				} else if (selectedType.equals("boolean")) {

					if ((valueField.getText().trim().equals("false") || valueField.getText().trim().equals("true"))
							&& variableField.getText().trim().matches(variableFieldRegEx)) {
						errorLabelField.setVisible(false);
					} else {
						errorLabelField.setVisible(true);
						errorLabelField.setText("Invalid Input. Check All Values.");

					}

				} else if (selectedType.equals("double")) {
					if (valueField.getText().trim().matches("^\\d*\\.\\d+")
							&& variableField.getText().trim().matches(variableFieldRegEx)
							&& lowerBoundField.getText().trim().matches("^\\d*\\.\\d+")
							&& upperBoundField.getText().trim().matches("^\\d*\\.\\d+")) {
						errorLabelField.setVisible(false);
					} else {
						errorLabelField.setVisible(true);
						errorLabelField.setText("Invalid Input. Check All Values.");

					}
				} else {
					if (variableField.getText().trim().matches(variableFieldRegEx)
							&& valueField.getText().trim().matches("^[0-9]+")
							&& lowerBoundField.getText().trim().matches("^[0-9]+")
							&& upperBoundField.getText().trim().matches("^[0-9]+")) {
						errorLabelField.setVisible(false);
					} else {
						errorLabelField.setVisible(true);
						errorLabelField.setText("Invalid Input. Check All Values.");

					}
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

					if ((valueField.getText().trim().equals("false") || valueField.getText().trim().equals("true"))
							&& variableField.getText().trim().matches(variableFieldRegEx)) {
						errorLabelField.setVisible(false);
					} else {
						errorLabelField.setVisible(true);
						errorLabelField.setText("Invalid Input. Check All Values.");

					}

				} else if (selectedType.equals("int")) {

					if (valueField.getText().trim().matches("^[0-9]+")
							&& variableField.getText().trim().matches(variableFieldRegEx)
							&& lowerBoundField.getText().trim().matches("^[0-9]+")
							&& upperBoundField.getText().trim().matches("^[0-9]+")) {
						errorLabelField.setVisible(false);
					} else {
						errorLabelField.setVisible(true);
						errorLabelField.setText("Invalid Input. Check All Values.");

					}

				} else if (selectedType.equals("float")) {

					if (valueField.getText().trim().matches("^\\d*\\.\\d+")
							&& variableField.getText().trim().matches(variableFieldRegEx)
							&& lowerBoundField.getText().trim().matches("^\\d*\\.\\d+")
							&& upperBoundField.getText().trim().matches("^\\d*\\.\\d+")) {
						errorLabelField.setVisible(false);
					} else {
						errorLabelField.setVisible(true);
						errorLabelField.setText("Invalid Input. Check All Values.");

					}

				} else if (selectedType.equals("double")) {

					if (valueField.getText().trim().matches("^\\d*\\.\\d+")
							&& variableField.getText().trim().matches(variableFieldRegEx)
							&& lowerBoundField.getText().trim().matches("^\\d*\\.\\d+")
							&& upperBoundField.getText().trim().matches("^\\d*\\.\\d+")) {
						errorLabelField.setVisible(false);
					} else {
						errorLabelField.setVisible(true);
						errorLabelField.setText("Invalid Input. Check All Values.");

					}

				} else if (selectedType.equals("string")) {

					if (valueField.getText().trim().matches(variableFieldRegEx)
							&& variableField.getText().trim().matches(variableFieldRegEx)) {
						errorLabelField.setVisible(false);
					} else {
						errorLabelField.setVisible(true);
						errorLabelField.setText("Invalid Input. Check All Values.");

					}
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

					if (valueField.getText().trim().matches("^[0-9]+")
							&& variableField.getText().trim().matches(variableFieldRegEx)
							&& lowerBoundField.getText().trim().matches("^[0-9]+")
							&& upperBoundField.getText().trim().matches("^[0-9]+")) {
						errorLabelField.setVisible(false);
					} else {
						errorLabelField.setVisible(true);
						errorLabelField.setText("Invalid Input. Check All Values.");

					}

				} else if (selectedType.equals("int")) {

					if (valueField.getText().trim().matches("^[0-9]+")
							&& variableField.getText().trim().matches(variableFieldRegEx)
							&& lowerBoundField.getText().trim().matches("^[0-9]+")
							&& upperBoundField.getText().trim().matches("^[0-9]+")) {
						errorLabelField.setVisible(false);
					} else {
						errorLabelField.setVisible(true);
						errorLabelField.setText("Invalid Input. Check All Values.");

					}

				} else if (selectedType.equals("double")) {

					if (valueField.getText().trim().matches("^\\d*\\.\\d+")
							&& variableField.getText().trim().matches(variableFieldRegEx)
							&& lowerBoundField.getText().trim().matches("^\\d*\\.\\d+")
							&& upperBoundField.getText().trim().matches("^\\d*\\.\\d+")) {
						errorLabelField.setVisible(false);
					} else {
						errorLabelField.setVisible(true);
						errorLabelField.setText("Invalid Input. Check All Values.");

					}

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

					if (valueField.getText().trim().matches("^[0-9]+")
							&& variableField.getText().trim().matches(variableFieldRegEx)
							&& lowerBoundField.getText().trim().matches("^[0-9]+")
							&& upperBoundField.getText().trim().matches("^[0-9]+")) {
						errorLabelField.setVisible(false);
					} else {
						errorLabelField.setVisible(true);
						errorLabelField.setText("Invalid Input. Check All Values.");

					}

				} else if (selectedType.equals("int")) {

					if (valueField.getText().trim().matches("^[0-9]+")
							&& variableField.getText().trim().matches(variableFieldRegEx)
							&& lowerBoundField.getText().trim().matches("^[0-9]+")
							&& upperBoundField.getText().trim().matches("^[0-9]+")) {
						errorLabelField.setVisible(false);
					} else {
						errorLabelField.setVisible(true);
						errorLabelField.setText("Invalid Input. Check All Values.");

					}

				} else if (selectedType.equals("double")) {

					if (valueField.getText().trim().matches("^\\d*\\.\\d+")
							&& variableField.getText().trim().matches(variableFieldRegEx)
							&& lowerBoundField.getText().trim().matches("^\\d*\\.\\d+")
							&& upperBoundField.getText().trim().matches("^\\d*\\.\\d+")) {
						errorLabelField.setVisible(false);
					} else {
						errorLabelField.setVisible(true);
						errorLabelField.setText("Invalid Input. Check All Values.");

					}

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

		Object[] message = { "Node Name:", nodeNameleField, "Variable Name:", variableField, "Variable Type:",
				variableTypeField, "Value:", valueField, "Lower Bound:", lowerBoundField, "Upper Bound:",
				upperBoundField, " ", errorLabelField };

		int option = JOptionPane.showConfirmDialog(PESEditor.framew, message, "Please Update",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		if (option == JOptionPane.OK_OPTION) {
			variableName = variableField.getText();
			variableType = (String) variableTypeField.getSelectedItem();
			variableValue = valueField.getText();
			variableLowerBound = lowerBoundField.getText();
			variableUpperBound = upperBoundField.getText();
			
			if(variableType.equals("")){
				variableType="none";
			}
			
			if(variableValue.equals("")){
				variableValue="none";
			}
			
			if(variableLowerBound.equals("")){
				variableLowerBound="none";
			}
			
			if(variableUpperBound.equals("")){
				variableUpperBound="none";
			}

			boolean validInput = (variableField.getText() != null) && (!variableField.getText().trim().isEmpty())
					&& (variableTypeField.getSelectedItem() != null)
					&& (!variableTypeField.getSelectedItem().toString().trim().isEmpty());
					//&& (valueField.getText() != null) && (!valueField.getText().trim().isEmpty());
			// && (lowerBoundField.getText() != null) &&
			// (!lowerBoundField.getText().trim().isEmpty())
			// && (upperBoundField.getText() != null) &&
			// (!upperBoundField.getText().trim().isEmpty());

			if (validInput) {

				if (variableTypeField.getSelectedItem().toString().trim().equals("string")
						|| variableTypeField.getSelectedItem().toString().trim().equals("boolean")) {
					variableName = variableName + "," + variableType + "," + variableValue;
				} else {
					variableName = variableName + "," + variableType + "," + variableValue + "," + variableLowerBound
							+ "," + variableUpperBound;
				}

				// have to send rowVals for updating the content and position pos of the
				// nodeObject.
				// pos have to bring here during calling this class function
				// System.out.println("variableNameOld: " + variableNameOld);
				// System.out.println("variableNameNew: " + variableName);
				// System.out.println("nodeName: " +
				// DynamicTreeDemo.jtreeTograph.selectedNodeCellForVariableUpdate.getValue().toString());

				PESEditor.jtreeTograph.deleteVariableFromScenarioTableForUpdate(
						PESEditor.jtreeTograph.selectedNodeCellForVariableUpdate, variableNameOld, variableName);
			}

		}

	}

}
