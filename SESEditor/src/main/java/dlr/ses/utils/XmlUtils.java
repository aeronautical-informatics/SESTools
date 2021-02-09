package dlr.ses.utils;

import dlr.ses.core.XMLViewer;

import javax.swing.plaf.basic.BasicInternalFrameUI;
import java.awt.Container;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class XmlUtils {
    public static XMLViewer xmlview;
    public static XMLViewer ontologyview;
    public static XMLViewer sesview;
    public static XMLViewer schemaview;

    public static void initXmlView() {
        XmlUtils.xmlview = new XMLViewer();
        XmlUtils.xmlview.setPreferredSize(new Dimension(200, 200));
        // this is for removing the top-left icon of the internal frame
        BasicInternalFrameUI uixml = (BasicInternalFrameUI) XmlUtils.xmlview.getUI();
        Container northxml = uixml.getNorthPane();
        northxml.remove(0);
        northxml.validate();
        northxml.repaint();
        // end of removing top left icon
        XmlUtils.xmlview.pack();
        XmlUtils.xmlview.setVisible(true);
        XmlUtils.xmlview.setTitle("XML"); // SES Ontology / Schema Viewer
        XmlUtils.xmlview.textArea.setEditable(true);
    }

    public static void initOntologyView() {
        XmlUtils.ontologyview = new XMLViewer();
        XmlUtils.ontologyview.setPreferredSize(new Dimension(200, 200));
        // this is for removing the top-left icon of the internal frame
        BasicInternalFrameUI uiontology = (BasicInternalFrameUI) XmlUtils.ontologyview.getUI();
        Container northontology = uiontology.getNorthPane();
        northontology.remove(0);
        northontology.validate();
        northontology.repaint();
        // end of removing top left icon
        XmlUtils.ontologyview.pack();
        XmlUtils.ontologyview.setVisible(true);
        XmlUtils.ontologyview.setTitle("SES Ontology"); // SES Ontology / Schema Viewer
        XmlUtils.ontologyview.textArea.setEditable(false);
    }

    public static void initSchemaView() {
        XmlUtils.schemaview = new XMLViewer();
        XmlUtils.schemaview.setPreferredSize(new Dimension(200, 200));
        // this is for removing the top-left icon of the internal frame
        BasicInternalFrameUI uischama = (BasicInternalFrameUI) XmlUtils.schemaview.getUI();
        Container northschama = uischama.getNorthPane();
        northschama.remove(0);
        northschama.validate();
        northschama.repaint();
        // end of removing top left icon
        XmlUtils.schemaview.pack();
        XmlUtils.schemaview.setVisible(true);
        XmlUtils.schemaview.setTitle("SES Schema"); // SES Ontology / Schema Viewer
        XmlUtils.schemaview.textArea.setEditable(false);
    }

    public static void initSesView() {
        sesview = new XMLViewer();
        sesview.setPreferredSize(new Dimension(200, 200));
        // this is for removing the top-left icon of the internal frame
        BasicInternalFrameUI uises = (BasicInternalFrameUI) sesview.getUI();
        Container northses = uises.getNorthPane();
        northses.remove(0);
        northses.validate();
        northses.repaint();
        // end of removing top left icon
        sesview.pack();
        sesview.setVisible(true);
        sesview.setTitle("SES XML"); // SES Ontology / Schema Viewer
        sesview.textArea.setEditable(false);
    }

    public static void showSESOntologytoOntologViewer(String fileLocation, String projName) {
        Scanner in = null;
        try {
            in = new Scanner(new File(fileLocation + "/" + projName + "/ses.xsd"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        StringBuilder xmlcontent = new StringBuilder();
        while (in.hasNext()) {
            String line = in.nextLine();
            xmlcontent.append(line);
            xmlcontent.append("\n");
        }
        XmlUtils.ontologyview.textArea.setText(xmlcontent.toString());
        in.close();
    }

    // showing xml to viewer
    public static void showXSDtoXMLViewer(String fileLocation, String projName) {
        Scanner in = null;
        try {
            in = new Scanner(new File(fileLocation + "/" + projName + "/xsdfromxml.xsd"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        StringBuilder xmlcontent = new StringBuilder();
        while (in.hasNext()) {
            String line = in.nextLine();
            xmlcontent.append(line);
            xmlcontent.append("\n");
        }
        XmlUtils.schemaview.textArea.setText(xmlcontent.toString());
        in.close();
    }

    // showing xml to viewer
    public static void showXMLtoXMLViewer(String fileLocation, String projName) {
        Scanner in = null;
        try {
            in = new Scanner(new File(fileLocation + "/" + projName + "/xmlforxsd.xml"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        StringBuilder xmlcontent = new StringBuilder();
        while (in.hasNext()) {
            String line = in.nextLine();
            xmlcontent.append(line);
            xmlcontent.append("\n");
        }
        sesview.textArea.setText(xmlcontent.toString());
        in.close();
    }

    public static void showGeneratedFile(String file, String fileLocation, String projName) {
        Scanner in = null;
        try {
            in = new Scanner(new File(fileLocation + "/" + projName + "/" + file + ".xml"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        StringBuilder xmlcontent = new StringBuilder();
        while (in.hasNext()) {
            String line = in.nextLine();
            xmlcontent.append(line);
            xmlcontent.append("\n");
        }
        XmlUtils.xmlview.textArea.setText(xmlcontent.toString());
        in.close();
    }
}
