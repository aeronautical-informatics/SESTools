package dlr.ses.seseditor;

import dlr.ses.core.Constraint;
import dlr.ses.core.ProjectTree;
import dlr.ses.core.Variable;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

/**
 * <h1>New Project</h1>
 * <p>
 * This class implements New Project Window for creating new SES project with
 * configuration like project name, root name and project location.
 * </p>
 *
 * @author Bikash Chandra Karmokar
 * @version 1.0
 */

public class NewProject extends JPanel {

    private static final long serialVersionUID = 1L;
    JFrame frame = new JFrame();

    public NewProject() {
        super(new BorderLayout());
    }

    /**
     * Create new SES project taking input from user. Here, user inputs are project
     * name, root name and project location. <b>Thing</b> is default root name and
     * default location is jar file location. Check the project name and root name
     * and make sure that they are not same showing error while user type root name.
     * Also check the duplicate project name for the default location.
     */
    public void createNewProjectWindow() {

        JLabel projectNameLabel = new JLabel("Project Name:");
        projectNameLabel.setBounds(20, 30, 120, 30);
        JTextField newProjectNameField = new JTextField(30);
        newProjectNameField.setBounds(150, 30, 410, 30);

        JCheckBox defaultRootNameChecker = new JCheckBox("Use Default Root:");
        defaultRootNameChecker.setBounds(20, 70, 120, 30);
        defaultRootNameChecker.setSelected(true);
        JTextField newRootNameField = new JTextField();
        newRootNameField.setBounds(150, 70, 410, 30);
        newRootNameField.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        newRootNameField.setText("Thing");
        newRootNameField.setEnabled(false);

        JCheckBox defaultProjectLocationChecker = new JCheckBox("Use Default Location:");
        defaultProjectLocationChecker.setBounds(20, 110, 140, 30);
        defaultProjectLocationChecker.setSelected(true);

        JLabel projectLocationLabel = new JLabel("Location:");
        projectLocationLabel.setBounds(20, 250, 120, 30);
        projectLocationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        projectLocationLabel.setEnabled(false);
        JTextField projectLocationField = new JTextField();
        projectLocationField.setBounds(20, 150, 430, 30);
        projectLocationField.setEnabled(false);
        projectLocationField.setText(SESEditor.fileLocation);
        JButton selectProjectLocation = new JButton("Browse...");
        selectProjectLocation.setBounds(460, 150, 100, 30);
        selectProjectLocation.setEnabled(false);

        JLabel errorLabelField = new JLabel();
        errorLabelField.setForeground(Color.RED);
        errorLabelField.setBounds(20, 210, 320, 30);
        errorLabelField.setVisible(true);

        JButton create = new JButton("Create");
        create.setBounds(350, 250, 100, 30);
        JButton cancel = new JButton("Cancel");
        cancel.setBounds(460, 250, 100, 30);

        newProjectNameField.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                String name = newProjectNameField.getText().trim();
                File fileName = new File(name);
                if (fileName.exists() && fileName.isDirectory()) {
                    errorLabelField.setVisible(true);
                    errorLabelField.setText("There is a file with the same name. It will be overwritten.");
                    System.out.println("Yes");
                } else {
                    errorLabelField.setVisible(false);
                    System.out.println("No");
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }
        });

        defaultProjectLocationChecker.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    projectLocationField.setText(SESEditor.fileLocation);
                    projectLocationField.setEnabled(false);
                    projectLocationLabel.setEnabled(false);
                    selectProjectLocation.setEnabled(false);
                } else {
                    projectLocationField.setEnabled(true);
                    projectLocationLabel.setEnabled(true);
                    selectProjectLocation.setEnabled(true);
                    projectLocationField.setText("");
                }

            }
        });

        selectProjectLocation.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setAcceptAllFileFilterUsed(false);
                fileChooser.setCurrentDirectory(new File(SESEditor.repFslas));
                int result = fileChooser.showOpenDialog(SESEditor.framew);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    SESEditor.fileLocation = selectedFile.getAbsolutePath();
                    projectLocationField.setText(SESEditor.fileLocation);
                }
            }
        });

        newRootNameField.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent arg0) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

                if (newRootNameField.getText().trim().equals(newProjectNameField.getText().trim())) {
                    errorLabelField.setVisible(true);
                    errorLabelField.setText("Root name shold be different from project name.");
                } else {
                    errorLabelField.setText("");
                    errorLabelField.setVisible(false);
                }

            }

            @Override
            public void keyTyped(KeyEvent e) {

            }

        });

        defaultRootNameChecker.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    newRootNameField.setEnabled(false);
                    newRootNameField.setText("Thing");

                } else {
                    newRootNameField.setEnabled(true);

                    newRootNameField.setText("");
                }

            }
        });

        JPanel panelTop = new JPanel();
        JPanel panelCenter = new JPanel();
        panelCenter.setLayout(null);
        JPanel panelBottom = new JPanel();

        panelCenter.add(projectNameLabel);
        panelCenter.add(newProjectNameField);

        panelCenter.add(defaultRootNameChecker);
        panelCenter.add(newRootNameField);

        panelCenter.add(defaultProjectLocationChecker);
        panelCenter.add(projectLocationField);
        panelCenter.add(selectProjectLocation);

        panelCenter.add(errorLabelField);
        panelCenter.add(create);
        panelCenter.add(cancel);

        panelTop.setBorder(new EtchedBorder());
        panelCenter.setBorder(new EtchedBorder());
        panelBottom.setBorder(new EtchedBorder());

        create.setToolTipText("Create Project");
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newProjectName = newProjectNameField.getText();
                String newRootName = null;
                if (defaultRootNameChecker.isSelected()) {
                    newRootName = "Thing";
                } else {
                    newRootName = newRootNameField.getText();
                }

                String oldProjectTreeProjectName = SESEditor.projName;

                SESEditor.projName = newProjectName;
                JtreeToGraph.newFileName = newProjectName;
                JtreeToGraph.projectFileNameGraph = newProjectName;
                JtreeToGraph.nodeNumber = 1;

                SESEditor.jtreeTograph.ssdFileGraph = new File(
                        SESEditor.fileLocation + "/" + SESEditor.projName + "/" + newProjectName
                        + "Graph.xml");
                SESEditor.treePanel.ssdFile = new File(
                        SESEditor.fileLocation + "/" + SESEditor.projName + "/" + newProjectName + ".xml");
                SESEditor.treePanel.ssdFileVar = new File(
                        SESEditor.fileLocation + "/" + SESEditor.projName + "/" + newProjectName + ".ssdvar");
                SESEditor.treePanel.ssdFileCon = new File(
                        SESEditor.fileLocation + "/" + SESEditor.projName + "/" + newProjectName + ".ssdcon");
                SESEditor.treePanel.ssdFileFlag = new File(
                        SESEditor.fileLocation + "/" + SESEditor.projName + "/" + newProjectName
                        + ".ssdflag");

                ProjectTree.projectName = newProjectName;


                SESEditor.jtreeTograph.deleteAllNodesFromGraphWindow(newRootName);
                SESEditor.jtreeTograph.addPageLengthNodes();

                DefaultMutableTreeNode rootNodeNew = new DefaultMutableTreeNode(newRootName);
                SESEditor.treePanel.treeModel.setRoot(rootNodeNew);
                SESEditor.treePanel.treeModel.reload();
                SESEditor.treePanel.tree.setModel(SESEditor.treePanel.treeModel);

                SESEditor.projectPanel
                        .changeCurrentProjectFileName(newProjectName, oldProjectTreeProjectName);

                Variable.setNullToAllRows();
                Constraint.setNullToAllRows();

                System.out.println(newProjectName);
                System.out.println(newRootName);

                SESEditor.newProjectFolderCreation();
                frame.dispose();

            }
        });

        cancel.setToolTipText("Cancel");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        int width = 600;
        int height = 360;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - width) / 2;
        int y = (screen.height - height) / 2;

        frame.setBounds(x, y, width, height);
        frame.setTitle("New SES Project");
        frame.pack();
        frame.setSize(width, height);
        frame.setVisible(true);
        frame.add(panelTop, BorderLayout.NORTH);
        frame.add(panelCenter, BorderLayout.CENTER);
        frame.add(panelBottom, BorderLayout.SOUTH);

    }

}
