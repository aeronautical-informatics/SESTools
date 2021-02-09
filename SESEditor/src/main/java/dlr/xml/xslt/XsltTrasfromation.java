package dlr.xml.xslt;

import dlr.ses.core.DynamicTree;
import dlr.ses.seseditor.SESEditor;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Tut:
 * https://examples.javacodegeeks.com/core-java/xml/xpath/xpath-xslt-tutorial/
 */
public class XsltTrasfromation {

    private static Document document;

    public static void main(String[] args) throws Exception {
        XsltTrasfromation.executeXSLT();
        XsltTrasfromation.convertXMLtoXHTML();
    }

    public static void executeXSLT() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        //File xml = new File("src/xslt/Scenario.xml");
        File xsl = new File("src/dlr/xml/xslt/Scenario.xsl");

        File xml = new File(SESEditor.fileLocation + "/" + SESEditor.projName + "/xmlforxsd.xml");

        DocumentBuilder builder = factory.newDocumentBuilder();
        document = builder.parse(xml);

        // Use a Transformer for output
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        StreamSource style = new StreamSource(xsl);
        Transformer transformer = transformerFactory.newTransformer(style);

        DOMSource source = new DOMSource(document);
        //StreamResult result = new StreamResult(new File("src/xslt/Scenario.html"));
        StreamResult result = new StreamResult(
                new File(SESEditor.fileLocation + "/" + SESEditor.projName + "/xmlforxsdXSTL.html"));
        transformer.transform(source, result);
        XsltTrasfromation.convertXMLtoXHTML();
    }

    public static void convertXMLtoXHTML() {
        // System.out.println("Modify called");

        PrintWriter f0 = null;
        try {
            f0 = new PrintWriter(new FileWriter(
                    SESEditor.fileLocation + "/" + SESEditor.projName + "/" + DynamicTree.projectFileName
                    + ".obj"));
            // System.out.println("output file generated");
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        Scanner in = null;
        try {
            in = new Scanner(
                    new File(SESEditor.fileLocation + "/" + SESEditor.projName + "/xmlforxsdXSTL.html"));
            //in = new Scanner(new File("src/xslt/xmlforxsdXSTL.html"));
            // System.out.println("my read complete");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        while (in.hasNext()) { // Iterates each line in the file

            String line = in.nextLine();
            String result1 = line.trim(); // line.replaceAll("\\s+", "");
            line = result1;

            if (line.startsWith("<html>") || line.startsWith("<body>") || line.startsWith("<table") || line
                    .startsWith("<tr>") || line.startsWith("<td>") || line.startsWith("<h2>") || line
                        .startsWith("</html>") || line.startsWith("</body>") || line.startsWith("</table>")
                || line.startsWith("</tr>") || line.startsWith("</td>") || line.startsWith("</h2>")) {

                continue;
            } else {

                f0.println(line);
            }
        }

        in.close();
        f0.close();
    }
}
