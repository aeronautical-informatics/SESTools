package dlr.ses.core;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;

public class XMLViewer extends JInternalFrame {

    public RSyntaxTextArea textArea;

    public XMLViewer() {

        JPanel cp = new JPanel(new BorderLayout());

        textArea = new RSyntaxTextArea(20, 60);
        //textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
        textArea.setCodeFoldingEnabled(true);
        RTextScrollPane sp = new RTextScrollPane(textArea);
        cp.add(sp);
        setContentPane(cp);
        setTitle("XML Viewer");

    }

}
