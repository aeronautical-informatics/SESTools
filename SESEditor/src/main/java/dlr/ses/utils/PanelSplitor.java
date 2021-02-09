package dlr.ses.utils;

import dlr.ses.core.Console;
import dlr.ses.core.Constraint;
import dlr.ses.core.DynamicTree;
import dlr.ses.core.GraphWindow;
import dlr.ses.core.ProjectTree;
import dlr.ses.core.Variable;

import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class PanelSplitor {
    public static int dividerLocation = 0;

    public static JSplitPane addSplitor(ProjectTree projectPanel,
                                        DynamicTree treePanel,
                                        GraphWindow graphWindow,
                                        Console console,
                                        Variable scenarioVariable,
                                        Constraint scenarioConstraint,
                                        JTabbedPane tabbedPane) {
        JSplitPane projectPane =
                new JSplitPane(JSplitPane.VERTICAL_SPLIT, projectPanel,
                        treePanel);
        projectPane.setOneTouchExpandable(true);
        projectPane.setDividerLocation(
                250); // define project Explorer height, so treePanel will be about 450
        // grapConsole.setResizeWeight(.1);
        projectPane
                .setDividerSize(6);// width of the line which split the window
        projectPane.setBorder(null);

        JSplitPane grapConsole =
                new JSplitPane(JSplitPane.VERTICAL_SPLIT, graphWindow, console);
        grapConsole.setOneTouchExpandable(true);
        grapConsole.setDividerLocation(
                750); // define graph window height, so console will be about 150
        // grapConsole.setResizeWeight(.1);
        grapConsole
                .setDividerSize(6);// width of the line which split the window
        grapConsole.setBorder(null);

        JSplitPane graphtree =
                new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, projectPane,
                        grapConsole);
        graphtree.setOneTouchExpandable(true);
        graphtree.setDividerLocation(200);// define tree panel width
        // graphtree.setResizeWeight(.1);
        graphtree.setDividerSize(6);// width of the line which split the window
        graphtree.setBorder(null);
        dividerLocation = graphtree.getDividerLocation();
        graphtree
                .addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY,
                        new PropertyChangeListener() {
                            @Override
                            public void propertyChange(
                                    PropertyChangeEvent evt) {
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        dividerLocation =
                                                graphtree.getDividerLocation();
                                    }
                                });
                            }
                        });

        JSplitPane variableAndCOnstraint =
                new JSplitPane(JSplitPane.VERTICAL_SPLIT, scenarioVariable,
                        scenarioConstraint);
        variableAndCOnstraint.setOneTouchExpandable(true);
        variableAndCOnstraint.setDividerLocation(150);
        // xml.setResizeWeight(.2);
        variableAndCOnstraint
                .setDividerSize(6);// width of the line which split the window
        variableAndCOnstraint.setBorder(null);

        JSplitPane xml =
                new JSplitPane(JSplitPane.VERTICAL_SPLIT, variableAndCOnstraint,
                        tabbedPane);//
        xml.setOneTouchExpandable(true);
        xml.setDividerLocation(300);// 150 for variable and 150 for constraint
        // xml.setResizeWeight(.2);
        xml.setDividerSize(6);// width of the line which split the window
        xml.setBorder(null);

        JSplitPane graphVariable =
                new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, graphtree, xml);
        graphVariable.setOneTouchExpandable(true);
        // graphVariable.setPreferredSize(new Dimension(200, 200));
        graphVariable.setDividerLocation(1400);// width of the graph window
        // graphVariable.setResizeWeight(.2);
        graphVariable
                .setDividerSize(6);// width of the line which split the window
        graphVariable.setBorder(null);

        return graphVariable;
    }
}
