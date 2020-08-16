package dlr.ses.seseditor;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.Serializable;
import javax.swing.*;
/**
 * Einfache Klasse zum erzeugen eines {@link JFrame} zum eingeben des Vornamens
 * sowie des Nachnamens. Beim klicken auf den {@link JButton} wird ein
 * {@link JOptionPane} Dialog angezeigt.
 *
 * @author Stefan Draeger
 *
 */
public class SimpleSwingGui extends JFrame {
    /**
     * Da die Klasse JFrame das Interface {@link HasGetTransferHandler}
     * implementiert und dieses Interface wiederum das Interface
     * {@link Serializable} implementiert sollte man eine eindeutige ID für den
     * Prozess der Serialisierung hinzufügen.
     */
    private static final long serialVersionUID = 3153235363948579984L;
    /** {@link JTextField} für die Eingabe des Vornamens **/
    private JTextField vornameTextField;
    /** {@link JTextField} für die Eingabe des Nachnamens **/
    private JTextField nameTextField;
    /**
     * Konstruktor
     */
    public SimpleSwingGui() {
        init();
    }
    /**
     * Das {@link JFrame} einrichten.
     */
    private void init() {
        this.setTitle("SimpleSwingGui");
        this.setBounds(0, 0, 1200, 2000);
        this.setLayout(new GridLayout(0, 2));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        this.setVisible(true);
    }
    /**
     * Erzeugen der Eingabefelder und des Buttons sowie der {@link ActionListener}
     */
    private void initComponents() {
        this.add(new JLabel(" Vorname"));
        vornameTextField = new JTextField();
        vornameTextField.setName("vornameTextField");
        this.add(vornameTextField);
        this.add(new JLabel(" Name"));
        nameTextField = new JTextField();
        nameTextField.setName("nameTextField");
        this.add(nameTextField);
        JButton sayHelloBtn = new JButton("say Hello");
        sayHelloBtn.setName("sayHelloBtn");
        sayHelloBtn.addActionListener(event -> {
            String message = String.format("Hallo %s %s", vornameTextField.getText(), nameTextField.getText());
            int result = JOptionPane.showConfirmDialog(SimpleSwingGui.this, message, "Hinweis...",
                    JOptionPane.OK_CANCEL_OPTION);
            String resultMsg = String.format("Button %s wurde geklickt", result == JOptionPane.OK_OPTION ? "OK"
                    : result == JOptionPane.CANCEL_OPTION ? "ABBRECHEN" : "-undefined-");
            System.out.println(resultMsg);
        });
        this.add(sayHelloBtn);

    }
}
