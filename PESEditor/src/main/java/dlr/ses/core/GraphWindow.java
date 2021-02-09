package dlr.ses.core;

import javax.swing.JInternalFrame;


public class GraphWindow extends JInternalFrame {

    public GraphWindow() {
        super("Graphical View", false, // resizable
                false, // closable
                false, // maximizable
                false // iconifiable
        );

        //this.add(createTabbedPane());
    }

//	private JTabbedPane createTabbedPane() {
//		JTabbedPane jtp = new JTabbedPane();
//		createTab(jtp, "One");
//		createTab(jtp, "Two");
//		return jtp;
//	}
//
//	private void createTab(JTabbedPane jtp, String s) {
//		jtp.add(s, new JLabel("TabbedPane " + s, JLabel.CENTER));
//	}	


}
