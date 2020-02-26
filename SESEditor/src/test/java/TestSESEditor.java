import dlr.ses.seseditor.SESEditor;
import edu.cnu.cs.gooey.Gooey;
import edu.cnu.cs.gooey.GooeyFrame;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.assertTrue;
import javax.swing.*;

public class TestSESEditor {

    @Test
    public void testTitle() {
        Gooey.capture(new GooeyFrame() {
            @Override
            public void invoke() {
                SESEditor.main(new String[]{});
            }

            @Override
            public void test(JFrame frame) {
                String actual = frame.getTitle();
                assertTrue(actual.contains("SES Editor"));
            }
        });
    }
}
