import dlr.ses.seseditor.MyPanel;
import dlr.ses.seseditor.SESEditor;
import dlr.ses.seseditor.SimpleSwingGui;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.finder.JOptionPaneFinder;
import org.assertj.swing.fixture.*;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.event.KeyEvent;

import static org.junit.Assert.assertNotNull;

public class TestSimpleSwingGui extends AssertJSwingJUnitTestCase {

    // Eine "Referenz" auf die SwingAnwendung.
    private FrameFixture window;

    @Override
    protected void onSetUp() {
        // Starten der SwingAnwendung
        MyPanel frame = GuiActionRunner.execute(() -> new MyPanel());
        //window = new FrameFixture(robot(), String.valueOf(frame));
        //window = new FrameFixture(robot(), frame);
        window.show();
    }

    @Override
    protected void onTearDown() {
        // Nach dem Beenden des Testfalls, alles Aufräumen.
        // Das heißt, es werden alle Fenster geschlossen,
        // es werden die Maustasten freigegeben,
        // es wird der Bildschirm freigegeben.
        window.cleanUp();
    }
    @Test
    public void shouldBeShowInfodialog1() {

    }
    @Test
    public void shouldBeShowInfodialog() {
        // Das JTextField mit dem Namen "vornameTextField" suchen und merken.
        JTextComponentFixture vornameTextField = window.textBox("vornameTextField");
        // Wenn das Objekt nicht NULL ist dann gehts weiter...
        assertNotNull(vornameTextField);
        //In das JTextField die Buchstaben m,a,x eingeben.
        vornameTextField.pressAndReleaseKeys(KeyEvent.VK_M, KeyEvent.VK_A, KeyEvent.VK_X);
        // Das JTextField mit dem Namen "nameTextField" suchen und merken.
        JTextComponentFixture nachnameTextField = window.textBox("nameTextField");
        // Wenn das Objekt nicht NULL ist dann gehts weiter...
        assertNotNull(nachnameTextField);
        //In das JTextField die Buchstaben m,u,s,t,e,r,m,a,n,n eingeben.
        nachnameTextField.pressAndReleaseKeys(KeyEvent.VK_M, KeyEvent.VK_U, KeyEvent.VK_S, KeyEvent.VK_T, KeyEvent.VK_E,
                KeyEvent.VK_R, KeyEvent.VK_M, KeyEvent.VK_A, KeyEvent.VK_N, KeyEvent.VK_N);
        // Die Schaltfläche mit dem Namen "sayHelloBtn" suchen und merken.
        JButtonFixture sayHelloBtn = window.button("sayHelloBtn");
        // Wenn das Objekt nicht NULL ist dann gehts weiter...
        assertNotNull(sayHelloBtn);
        //Eine Klickaktion auf die Schaltfläche ausführen.
        sayHelloBtn.click();
        JOptionPaneFixture optionPane = JOptionPaneFinder.findOptionPane().withTimeout(10).using(robot());
        // Wenn das Objekt nicht NULL ist dann gehts weiter...
        assertNotNull(optionPane);
        // Wenn das Objekt "optionPane" nicht sichtbar ist dann wird eine Fehlermeldung "geworfen".
        optionPane.requireVisible();

        // Die Schaltfläche mit dem Text "OK" suchen und merken.
        JButtonFixture okBtn = optionPane.buttonWithText("OK");
        // Wenn das Objekt nicht NULL ist dann gehts weiter...
        assertNotNull(okBtn);
        //Eine Klickaktion auf die Schaltfläche ausführen.
        okBtn.click();
        //Eine Klickaktion auf die Schaltfläche ausführen.
        sayHelloBtn.click();
        optionPane = JOptionPaneFinder.findOptionPane().withTimeout(10).using(robot());
        // Wenn das Objekt nicht NULL ist dann gehts weiter...
        assertNotNull(optionPane);
        optionPane.requireVisible();

        // Die Schaltfläche mit dem Text "Abbrechen" suchen und merken.
        JButtonFixture abbrechenBtn = optionPane.buttonWithText("Abbrechen");
        // Wenn das Objekt nicht NULL ist dann gehts weiter...
        assertNotNull(abbrechenBtn);
        //Eine Klickaktion auf die Schaltfläche ausführen.
        abbrechenBtn.click();
    }


}
