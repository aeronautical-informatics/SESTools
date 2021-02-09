package dlr.ses.seseditor;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyPanel extends JPanel {
    private int number, guessCount;
    private int lastDistance;
    private JTextField guessInput;
    private JLabel prompt1, prompt2, message;
    private JButton newGame;
    private Color background;

    // set up GUI and initialize values
    public MyPanel() {


        setLayout(new FlowLayout());
        guessCount = 0;
        background = Color.white;
        // create GUI components
        prompt1 = new JLabel("I have a number between 1 and 1000.");
        this.add(prompt1);
        prompt2 = new JLabel("Can you guess my number? Enter your Guess:");
        add(prompt2);
        guessInput = new JTextField(5);
        guessInput.addActionListener(new GuessHandler());
        add(guessInput);
        message = new JLabel("Guess result appears here.");
        add(message);
        // button starts a new game
        newGame = new JButton("New Game");
        add(newGame);
        newGame.addActionListener(new ActionListener() {
                                      public void actionPerformed(ActionEvent e) {
                                          /*
                                           * A JButton should be provided to allow the user to play the
                                           * game again. When the JButton is clicked, a new random number
                                           * should be generated and the input JTextField changed to be
                                           * editable.
                                           */
                                          message.setText("Guess Result");
                                          guessInput.setText("");
                                          guessInput.setEditable(true);
                                          background = Color.white;
                                          theGame();
                                          repaint();
                                      }
                                  }
        );
        theGame();
    }

    public static void main(String args[]) {
        JFrame myFrame = new JFrame("Guess My Number!!");
        JPanel Guess = new JPanel();
        myFrame.add(Guess);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setSize(400, 400); // set frame size
        myFrame.setVisible(true); // display frame
    }

    // choose a new random number
    public void theGame() {
        number = (int) (Math.random() * 1000 + 1);
        guessCount = 0;
    }

    // change background color
    public void paint(Graphics g) {
        super.paint(g);
        setBackground(background);
    }

    // react to new guess
    public void react(int guess) {
        guessCount++;
        int currentDistance = 1000;
        // first guess
        if (guessCount == 1) {
            lastDistance = Math.abs(guess - number);
            if (guess > number) {
                message.setText("Too High. Try a lower number.");
            } else {
                message.setText("Too Low. Try a higher number.");
            }
        } else {
            currentDistance = Math.abs(guess - number);
            // guess is too high
            if (guess > number) {
                message.setText("Too High. Try a lower number.");
                background = (currentDistance <= lastDistance) ? Color.red
                        : Color.blue;
                lastDistance = currentDistance;
            }
            // guess is too low
            else if (guess < number) {
                message.setText("Too Low. Try a higher number.");
                background = (currentDistance <= lastDistance) ? Color.red
                        : Color.blue;
                lastDistance = currentDistance;
            }
            // guess is correct
            else {
                /*
                 * When the user gets the correct answer, “Correct!” should be
                 * displayed, and the JTextField used for input should be
                 * changed to be uneditable.
                 */
                message.setText("Correct!");
                guessInput.setEditable(false);
                setBackground(Color.white);
            }
            repaint();
        }
    } // end method react

    // inner class acts on user input
    class GuessHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int guess = Integer.parseInt(guessInput.getText());
            react(guess);
            guessInput.selectAll();
        }
    }
}
