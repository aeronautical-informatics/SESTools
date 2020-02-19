import dlr.ses.seseditor.SESEditor;
import edu.cnu.cs.gooey.Gooey;
import edu.cnu.cs.gooey.GooeyFrame;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import javax.swing.*;

public class TestSimpleSwingGui {
    private static class EmptyJFrame extends JFrame {
        public EmptyJFrame() {
            setSize(200, 100);
        }

        public static void main(final String[] args) {
            JFrame frame = new EmptyJFrame();
            frame.setTitle(args[0]); // for testing purposes (to identify if a window was left open after a test)
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }

    @Test
    public void testEmptyTitle() {
        Gooey.capture(new GooeyFrame() {
            @Override
            public void invoke() {
                SESEditor.main(new String[]{"a"});
            }

            @Override
            public void test(JFrame frame) {
                String actual = frame.getTitle();
                assertTrue(actual.contains("SES Editor"));
            }
        });
    }
}
