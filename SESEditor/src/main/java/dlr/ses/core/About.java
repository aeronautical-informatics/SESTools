package dlr.ses.core;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

/**
 * <h1>About</h1>
 * <p>
 * This class implements the about window of the editor. Information about the
 * editor such as editor name, its version, developer name and the supervisor
 * name are displayed on the window.
 * </p>
 * 
 * @author Bikash Chandra Karmokar
 * @version 1.0
 *
 */
public class About extends JPanel {

	private static final long serialVersionUID = 1L;
	public String editorName;
	JFrame frame = new JFrame();

	public About(String name) {
		super(new BorderLayout());
		this.editorName = name;
		aboutGUI();

	}

	/**
	 * Shows the information about the editor and the developer and his supervisor
	 */
	public void aboutGUI() {

		JLabel editorNameLabel = new JLabel("Name:");
		editorNameLabel.setBounds(20, 30, 120, 30);

		JLabel editorName = new JLabel(this.editorName);
		editorName.setBounds(130, 30, 300, 30);

		JLabel versionLabel = new JLabel("Version:");
		versionLabel.setBounds(20, 50, 120, 50);

		JLabel versionNumver = new JLabel("2019.01");
		versionNumver.setBounds(130, 50, 300, 50);

		JLabel developerNameLabel = new JLabel("<html>Developer:<br></html>");
		developerNameLabel.setBounds(20, 70, 120, 70);

		JLabel developerName = new JLabel(
				"<html>Bikash Chandra Karmokar, <br>Student Assistant, German Aerospace Center (DLR)</html>");
		developerName.setBounds(130, 75, 300, 75);

		JLabel supervisorNameLabel = new JLabel("<html>Supervisor:<br></html>");
		supervisorNameLabel.setBounds(20, 100, 120, 100);

		JLabel supervisorName = new JLabel(
				"<html>Dr. Umut Durak, <br>Research Scientist, German Aerospace Center (DLR)</html>");
		supervisorName.setBounds(130, 105, 300, 105);

		JPanel panelTop = new JPanel();
		JPanel panelCenter = new JPanel();
		panelCenter.setLayout(null);
		JPanel panelBottom = new JPanel();

		panelCenter.add(editorNameLabel);
		panelCenter.add(editorName);

		panelCenter.add(versionLabel);
		panelCenter.add(versionNumver);

		panelCenter.add(developerNameLabel);
		panelCenter.add(developerName);

		panelCenter.add(supervisorNameLabel);
		panelCenter.add(supervisorName);

		panelTop.setBorder(new EtchedBorder());
		panelCenter.setBorder(new EtchedBorder());
		panelBottom.setBorder(new EtchedBorder());

		int width = 450;
		int height = 300;
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - width) / 2;
		int y = (screen.height - height) / 2;

		frame.setBounds(x, y, width, height);
		frame.setTitle("About Editor");
		frame.setResizable(false);
		frame.pack();
		frame.setSize(width, height);
		frame.setVisible(true);
		frame.add(panelTop, BorderLayout.NORTH);
		frame.add(panelCenter, BorderLayout.CENTER);
		frame.add(panelBottom, BorderLayout.SOUTH);

	}

}
