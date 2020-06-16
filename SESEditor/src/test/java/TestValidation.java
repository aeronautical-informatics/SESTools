import dlr.ses.core.Console;
import dlr.ses.seseditor.SESEditor;
import dlr.xml.schema.TypeInfoWriter;
import edu.cnu.cs.gooey.Gooey;
import edu.cnu.cs.gooey.GooeyFrame;
import org.junit.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class TestValidation {

    @Test
    public void testWarningValidation() {
        Gooey.capture(new GooeyFrame() {
            @Override
            public void invoke() {
                SESEditor.main(new String[]{""});
            }

            @Override
            public void test(JFrame frame) {
                SESEditor.sesValidationControl = 1;
                TypeInfoWriter.validateXML();
                String console=Console.getText();

                assertFalse(console.contains("warning:"), "Incorrect result");
            }
        });
    }
    @Test
    public void testErrorValidation() {
        Gooey.capture(new GooeyFrame() {
            @Override
            public void invoke() {
                SESEditor.main(new String[]{""});
            }

            @Override
            public void test(JFrame frame) {
                SESEditor.sesValidationControl = 1;
                TypeInfoWriter.validateXML();
                String console=Console.getText();

                assertFalse(console.contains("error:"), "Incorrect result");
            }
        });
    }

}
