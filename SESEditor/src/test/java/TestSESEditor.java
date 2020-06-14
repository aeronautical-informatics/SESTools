import dlr.ses.seseditor.SESEditor;
import edu.cnu.cs.gooey.Gooey;
import edu.cnu.cs.gooey.GooeyFrame;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.assertTrue;
import javax.swing.*;
import java.util.List;

public class TestSESEditor {

    @Test
    public void testMenubar() {
        Gooey.capture(new GooeyFrame() {
            @Override
            public void invoke() {
                SESEditor.main(new String[]{});
            }

            @Override
            public void test(JFrame frame) {
                String actual = frame.getTitle();
                assertTrue(actual.contains("SES Editor"));

                /*
                JMenuBar menubar = Gooey.getMenuBar(frame);
                JMenu file =	Gooey.getMenu(menubar,"File");
                JMenu help =	Gooey.getMenu(menubar,"Help");
                JMenuItem open = Gooey.getMenu(file, "Open");

                List<JMenu> menus =	Gooey.getMenus(menubar);

                assertEquals("Incorrect result",2,menus.size());
                assertTrue(menus.contains(file),"Incorrect result");
                assertTrue(menus.contains(help),"Incorrect result");

                List<JMenuItem> fileItems =	Gooey.getMenus(file);
                assertEquals("Incorrect result",7,fileItems.size());
                assertEquals("Incorrect result",fileItems.contains(open));
                 */
            }
        });
    }
}
