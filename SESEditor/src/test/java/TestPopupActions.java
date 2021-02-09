import dlr.ses.seseditor.SESEditor;
import edu.cnu.cs.gooey.Gooey;
import edu.cnu.cs.gooey.GooeyDialog;
import edu.cnu.cs.gooey.GooeyFrame;
import org.junit.Test;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestPopupActions {
    @Test
    public void testMenubar() {
        Gooey.capture(new GooeyFrame() {
            @Override
            public void invoke() {
                SESEditor.main(new String[]{""});
            }

            @Override
            public void test(JFrame frame) {
                JMenuBar menubar = Gooey.getMenuBar(frame);
                JMenu library = Gooey.getMenu(menubar, "SESEditor");
                JMenuItem exit = Gooey.getMenu(library, "Exit");

                assertTrue(frame.isShowing(), "JFrame should be displayed");
                Gooey.capture(new GooeyDialog() {
                    @Override
                    public void invoke() {
                        exit.doClick();
                    }

                    @Override
                    public void test(JDialog dialog) {
                        Gooey.getLabel(dialog, "Do you want to exit?");
                        JButton yes = Gooey.getButton(dialog, "Yes");
                        yes.doClick();
                        assertFalse(dialog.isShowing(), "JDialog should be hidden");
                    }
                });

            }
        });
    }

    @Test
    public void tryCloseProgram() {
        Gooey.capture(new GooeyFrame() {
            @Override
            public void invoke() {
                SESEditor.main(new String[]{});
            }

            @Override
            public void test(JFrame frame) {
                JMenuBar menubar = Gooey.getMenuBar(frame);
                JMenu library = Gooey.getMenu(menubar, "SongLibrary");
                JMenuItem exit = Gooey.getMenu(library, "Exit");

                assertTrue(frame.isShowing(), "JFrame should be displayed");
                Gooey.capture(new GooeyDialog() {
                    @Override
                    public void invoke() {
                        exit.doClick();
                    }

                    @Override
                    public void test(JDialog dialog) {
                        Gooey.getLabel(dialog, "Do you want to exit?");
                        JButton no = Gooey.getButton(dialog, "No");
                        no.doClick();
                        assertFalse(dialog.isShowing(), "JDialog should be hidden");
                        assertTrue(frame.isShowing(), "Jframe should be showing");
                    }
                });

            }
        });
    }

    @Test
    public void saveThetable() {
        Gooey.capture(new GooeyFrame() {
            @Override
            public void invoke() {
                SESEditor.main(new String[]{});
            }

            @Override
            public void test(JFrame frame) {
                JMenuBar menubar = Gooey.getMenuBar(frame);
                JMenu table = Gooey.getMenu(menubar, "Table");
                JMenuItem save = Gooey.getMenu(table, "Save As...");
                File file = new File("readme");
                String line = "hot, dog, for, lunch";

                try {
                    BufferedWriter wrote = new BufferedWriter(
                            new FileWriter(file.getAbsolutePath()));

                    wrote.write(line);
                    wrote.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Gooey.capture(new GooeyDialog() {
                    @Override
                    public void invoke() {
                        save.doClick();
                    }

                    @Override
                    public void test(JDialog d) {
                        JFileChooser choose = Gooey.getComponent(d,
                                JFileChooser.class);
                        choose.setSelectedFile(file);
                        choose.approveSelection();
                    }
                });
            }
        });
    }
}
