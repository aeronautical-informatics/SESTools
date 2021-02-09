import dlr.ses.seseditor.SESEditor;
import edu.cnu.cs.gooey.Gooey;
import edu.cnu.cs.gooey.GooeyFrame;
import org.junit.Test;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestSimpleSwingGui {

    @Test
    public void testToolbar() {
        Gooey.capture(new GooeyFrame() {
            @Override
            public void invoke() {
                SESEditor.main(new String[] {""});
            }

            @Override
            public void test(JFrame frame) {
                JToolBar toolbar = Gooey.getComponent(frame, JToolBar.class);

                JButton selector = Gooey.getComponent(toolbar, JButton.class, "Selector");
                JButton addEntity = Gooey.getComponent(toolbar, JButton.class, "Add Entity");
                JButton addAspect = Gooey.getComponent(toolbar, JButton.class, "Add Aspect");
                JButton addSpecialization = Gooey.getComponent(toolbar, JButton.class, "Add Specialization");
                JButton addMultiaspect = Gooey.getComponent(toolbar, JButton.class, "Add Multi-Aspect");
                JButton deleteNode = Gooey.getComponent(toolbar, JButton.class, "Delete Node From Graph");
                JButton saveGraph = Gooey.getComponent(toolbar, JButton.class, "Save Graph");
                JButton undo = Gooey.getComponent(toolbar, JButton.class, "Undo");
                JButton redo = Gooey.getComponent(toolbar, JButton.class, "Redo");
                JButton zoomIn = Gooey.getComponent(toolbar, JButton.class, "Zoom In");
                JButton zoomOut = Gooey.getComponent(toolbar, JButton.class, "Zoom Out");
                JButton validation = Gooey.getComponent(toolbar, JButton.class, "Validation");

                List<JButton> toolbarButtons = Gooey.getComponents(toolbar, JButton.class);

                assertEquals(12, toolbarButtons.size(), "Incorrect result");
                assertTrue(toolbarButtons.contains(selector), "Incorrect result");
                assertTrue(toolbarButtons.contains(addEntity), "Incorrect result");
                assertTrue(toolbarButtons.contains(addAspect), "Incorrect result");
                assertTrue(toolbarButtons.contains(addSpecialization), "Incorrect result");
                assertTrue(toolbarButtons.contains(addMultiaspect), "Incorrect result");
                assertTrue(toolbarButtons.contains(deleteNode), "Incorrect result");
                assertTrue(toolbarButtons.contains(saveGraph), "Incorrect result");
                assertTrue(toolbarButtons.contains(undo), "Incorrect result");
                assertTrue(toolbarButtons.contains(redo), "Incorrect result");
                assertTrue(toolbarButtons.contains(zoomIn), "Incorrect result");
                assertTrue(toolbarButtons.contains(zoomOut), "Incorrect result");
                assertTrue(toolbarButtons.contains(validation), "Incorrect result");

            }
        });
    }

    @Test
    public void testMenubar() {
        Gooey.capture(new GooeyFrame() {
            @Override
            public void invoke() {
                SESEditor.main(new String[] {""});
            }

            @Override
            public void test(JFrame frame) {
                String actual = frame.getTitle();
                assertTrue(actual.contains("SES Editor"));

                JMenuBar menubar = Gooey.getMenuBar(frame);

                JMenu file = Gooey.getMenu(menubar, "File");
                JMenuItem newItem = Gooey.getMenu(file, "New");
                JMenuItem openItem = Gooey.getMenu(file, "Open");
                JMenuItem saveItem = Gooey.getMenu(file, "Save");
                JMenuItem saveAsItem = Gooey.getMenu(file, "Save As...");
                JMenuItem importItem = Gooey.getMenu(file, "Import");
                JMenuItem exportItem = Gooey.getMenu(file, "Export");
                JMenuItem exitItem = Gooey.getMenu(file, "Exit");

                JMenu help = Gooey.getMenu(menubar, "Help");
                JMenuItem manualItem = Gooey.getMenu(help, "Manual");
                JMenuItem aboutItem = Gooey.getMenu(help, "About");


                List<JMenu> menus = Gooey.getMenus(menubar);
                assertEquals(2, menus.size(), "Incorrect result");
                assertTrue(menus.contains(file), "Incorrect result");
                assertTrue(menus.contains(help), "Incorrect result");

                List<JMenuItem> fileItems = Gooey.getMenus(file);
                assertEquals(7, fileItems.size(), "Incorrect result");
                assertTrue(fileItems.contains(newItem), "Incorrect result");
                assertTrue(fileItems.contains(openItem), "Incorrect result");
                assertTrue(fileItems.contains(saveItem), "Incorrect result");
                assertTrue(fileItems.contains(saveAsItem), "Incorrect result");
                assertTrue(fileItems.contains(importItem), "Incorrect result");
                assertTrue(fileItems.contains(exportItem), "Incorrect result");
                assertTrue(fileItems.contains(exitItem), "Incorrect result");

                List<JMenuItem> helpItems = Gooey.getMenus(help);
                assertEquals(2, helpItems.size(), "Incorrect result");
                assertTrue(helpItems.contains(manualItem), "Incorrect result");
                assertTrue(helpItems.contains(aboutItem), "Incorrect result");
            }
        });
    }
}
