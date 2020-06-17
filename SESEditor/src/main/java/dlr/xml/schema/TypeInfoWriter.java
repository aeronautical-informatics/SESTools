/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//jdk8x2r jaxp.TypeInfoWriter -xsd11 -i xsd11_datatype_test.xml
package dlr.xml.schema;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.TypeInfoProvider;
import javax.xml.validation.ValidatorHandler;

import org.w3c.dom.TypeInfo;
import org.xml.sax.Attributes;
import org.xml.sax.DTDHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import dlr.ses.core.Console;
import dlr.ses.seseditor.SESEditor;

/**
 * <h1>TypeInfoWriter</h1>
 * <p>
 * Provides a trace of the schema type information for elements and attributes
 * in an XML document. XML instance of the created SES model is validated
 * against an SES XML Schema using this class. Validation result is displayed in
 * the console window of the editor as human readable format.
 * </p>
 *
 * @author Michael Glavassevich, IBM
 *
 * @version $Id: TypeInfoWriter.java 903087 2010-01-26 05:41:00Z mrglavas $
 * 
 * @ModifiedBy Bikash Chandra Karmokar
 */
public class TypeInfoWriter extends DefaultHandler {

	//
	// Constants
	//

	// feature ids

	/**
	 * Schema full checking feature id
	 * (http://apache.org/xml/features/validation/schema-full-checking).
	 */
	protected static final String SCHEMA_FULL_CHECKING_FEATURE_ID = "http://apache.org/xml/features/validation/schema-full-checking";

	/**
	 * Honour all schema locations feature id
	 * (http://apache.org/xml/features/honour-all-schemaLocations).
	 */
	protected static final String HONOUR_ALL_SCHEMA_LOCATIONS_ID = "http://apache.org/xml/features/honour-all-schemaLocations";

	/**
	 * Validate schema annotations feature id
	 * (http://apache.org/xml/features/validate-annotations)
	 */
	protected static final String VALIDATE_ANNOTATIONS_ID = "http://apache.org/xml/features/validate-annotations";

	/**
	 * Generate synthetic schema annotations feature id
	 * (http://apache.org/xml/features/generate-synthetic-annotations).
	 */
	protected static final String GENERATE_SYNTHETIC_ANNOTATIONS_ID = "http://apache.org/xml/features/generate-synthetic-annotations";

	// default settings

	/** Default schema language (http://www.w3.org/2001/XMLSchema). */
	protected static final String DEFAULT_SCHEMA_LANGUAGE = XMLConstants.W3C_XML_SCHEMA_NS_URI;

	/** XSD 1.1 schema language (http://www.w3.org/XML/XMLSchema/v1.1). */
	protected static final String XSD11_SCHEMA_LANGUAGE = "http://www.w3.org/XML/XMLSchema/v1.1";

	/** Default parser name (org.apache.xerces.parsers.SAXParser). */
	protected static final String DEFAULT_PARSER_NAME = "org.apache.xerces.parsers.SAXParser";

	/** Default schema full checking support (false). */
	protected static final boolean DEFAULT_SCHEMA_FULL_CHECKING = false;

	/** Default honour all schema locations (false). */
	protected static final boolean DEFAULT_HONOUR_ALL_SCHEMA_LOCATIONS = false;

	/** Default validate schema annotations (false). */
	protected static final boolean DEFAULT_VALIDATE_ANNOTATIONS = false;

	/** Default generate synthetic schema annotations (false). */
	protected static final boolean DEFAULT_GENERATE_SYNTHETIC_ANNOTATIONS = false;

	//
	// Data
	//

	/** TypeInfo provider. */
	protected TypeInfoProvider fTypeInfoProvider;

	/** Print writer. */
	protected PrintWriter fOut;

	/** Indent level. */
	protected int fIndent;

	//
	// Constructors
	//

	/** Default constructor. */
	public TypeInfoWriter() {
	}

	//
	// ContentHandler and DocumentHandler methods
	//

	/** Set document locator. */
	public void setDocumentLocator(Locator locator) {

		fIndent = 0;
		printIndent();
		Console.addConsoleOutput("setDocumentLocator(");
		Console.addConsoleOutput("systemId=");
		printQuotedString(locator.getSystemId());
		Console.addConsoleOutput(", publicId=");
		printQuotedString(locator.getPublicId());
		Console.addConsoleOutput(")");
		fOut.flush();

	} // setDocumentLocator(Locator)

	/** Start document. */
	public void startDocument() throws SAXException {

		fIndent = 0;
		printIndent();
		Console.addConsoleOutput("startDocument()");
		fOut.flush();
		fIndent++;

	} // startDocument()

	/** Start element. */
	public void startElement(String uri, String localName, String qname, Attributes attributes) throws SAXException {

		TypeInfo type;
		printIndent();
		Console.addConsoleOutput("startElement(");
		Console.addConsoleOutput("name=");
		printQName(uri, localName);
		Console.addConsoleOutput(",");
		Console.addConsoleOutput("type=");
		if (fTypeInfoProvider != null && (type = fTypeInfoProvider.getElementTypeInfo()) != null) {
			printQName(type.getTypeNamespace(), type.getTypeName());
		} else {
			Console.addConsoleOutput("null");
		}
		Console.addConsoleOutput(",");
		Console.addConsoleOutput("attributes=");
		if (attributes == null) {
			Console.addConsoleOutput("null");
		} else {
			Console.addConsoleOutput("{");
			int length = attributes.getLength();
			for (int i = 0; i < length; i++) {
				if (i > 0) {
					Console.addConsoleOutput(",");
				}
				String attrURI = attributes.getURI(i);
				String attrLocalName = attributes.getLocalName(i);
				Console.addConsoleOutput("{");
				Console.addConsoleOutput("name=");
				printQName(attrURI, attrLocalName);
				Console.addConsoleOutput(",");
				Console.addConsoleOutput("type=");
				if (fTypeInfoProvider != null && (type = fTypeInfoProvider.getAttributeTypeInfo(i)) != null) {
					printQName(type.getTypeNamespace(), type.getTypeName());
				} else {
					Console.addConsoleOutput("null");
				}
				Console.addConsoleOutput(",");
				Console.addConsoleOutput("id=");
				Console.addConsoleOutput(
						fTypeInfoProvider != null && fTypeInfoProvider.isIdAttribute(i) ? "\"true\"" : "\"false\"");
				Console.addConsoleOutput(",");
				Console.addConsoleOutput("specified=");
				Console.addConsoleOutput(
						fTypeInfoProvider == null || fTypeInfoProvider.isSpecified(i) ? "\"true\"" : "\"false\"");
				Console.addConsoleOutput("}");
			}
			Console.addConsoleOutput("}");
		}
		Console.addConsoleOutput(")");
		fOut.flush();
		fIndent++;

	} // startElement(String,String,String,Attributes)

	/** End element. */
	public void endElement(String uri, String localName, String qname) throws SAXException {

		fIndent--;
		printIndent();
		Console.addConsoleOutput("endElement(");
		Console.addConsoleOutput("name=");
		printQName(uri, localName);
		Console.addConsoleOutput(")");
		fOut.flush();

	} // endElement(String,String,String)

	/** End document. */
	public void endDocument() throws SAXException {
		fIndent--;
		printIndent();
		Console.addConsoleOutput("endDocument()");
		fOut.flush();
	} // endDocument()

	//
	// ErrorHandler methods
	//

	/** Warning. */
	public void warning(SAXParseException ex) throws SAXException {
		printError("Warning", ex);
	} // warning(SAXParseException)

	/** Error. */
	public void error(SAXParseException ex) throws SAXException {
		printError("Error", ex);
	} // error(SAXParseException)

	/** Fatal error. */
	public void fatalError(SAXParseException ex) throws SAXException {
		printError("Fatal Error", ex);
		throw ex;
	} // fatalError(SAXParseException)

	//
	// Public methods
	//

	/** Sets the output stream for printing. */
	public void setOutput(OutputStream stream, String encoding) throws UnsupportedEncodingException {

		if (encoding == null) {
			encoding = "UTF8";
		}

		java.io.Writer writer = new OutputStreamWriter(stream, encoding);
		fOut = new PrintWriter(writer);

	} // setOutput(OutputStream,String)

	//
	// Protected methods
	//

	/** Sets the TypeInfoProvider used by this writer. */
	protected void setTypeInfoProvider(TypeInfoProvider provider) {
		fTypeInfoProvider = provider;
	}

	/** Prints the error message. */
	protected void printError(String type, SAXParseException ex) {

		if (type.equals("Error")) {
			SESEditor.errorPresentInSES = 1;
			SESEditor.errorMessageInSES = ex.getMessage();
		}

		Console.addConsoleOutput("[");
		Console.addConsoleOutput(type);
		Console.addConsoleOutput("] ");
		String systemId = ex.getSystemId();
		if (systemId != null) {
			int index = systemId.lastIndexOf('/');
			if (index != -1)
				systemId = systemId.substring(index + 1);
			Console.addConsoleOutput(systemId);
		}
		Console.addConsoleOutput(":");
		Console.addConsoleOutput("" + ex.getLineNumber());
		Console.addConsoleOutput("" + ':');
		Console.addConsoleOutput("" + ex.getColumnNumber());
		Console.addConsoleOutput(": ");
		Console.addConsoleOutput(ex.getMessage());
		Console.addConsoleOutput("");
		// System.err.flush();

	} // printError(String,SAXParseException)

	/** Prints the indent. */
	protected void printIndent() {
		for (int i = 0; i < fIndent; i++) {
			Console.addConsoleOutput(" ");
		}
	} // printIndent()

	protected void printQName(String uri, String localName) {
		if (uri != null && uri.length() > 0) {
			printQuotedString('{' + uri + "}" + localName);
			return;
		}
		printQuotedString(localName);
	}

	/** Print quoted string. */
	protected void printQuotedString(String s) {

		if (s == null) {
			Console.addConsoleOutput("null");
			return;
		}
		Console.addConsoleOutput("\"");
		Console.addConsoleOutput(s);
		Console.addConsoleOutput("\"");

	} // printQuotedString(String)

	//
	// MAIN
	//

	/*public static void main(String[] args) throws UnsupportedEncodingException {
		TypeInfoWriter writer = new TypeInfoWriter();
		writer.setOutput(System.out, "UTF8");

		// Create SchemaFactory and configure
		SchemaFactory factory = SchemaFactory.newInstance(DEFAULT_SCHEMA_LANGUAGE);
		//factory.setProperty("http://saxon.sf.net/feature/xsd-version", "1.1");

	}*/
	/** Main program entry point. */
	// public static void main(String[] argv) {
	public static void validateXML() {
		// jdk8x2r jaxp.TypeInfoWriter -xsd11 -i xsd11_datatype_test.xml

		// is there anything to do?
		/*
		 * if (argv.length == 0) { printUsage(); System.exit(1); }
		 */

		// variables
		XMLReader parser = null;
		Vector schemas = null;
		Vector instances = null;
		String schemaLanguage = DEFAULT_SCHEMA_LANGUAGE;
		boolean schemaFullChecking = DEFAULT_SCHEMA_FULL_CHECKING;
		boolean honourAllSchemaLocations = DEFAULT_HONOUR_ALL_SCHEMA_LOCATIONS;
		boolean validateAnnotations = DEFAULT_VALIDATE_ANNOTATIONS;
		boolean generateSyntheticAnnotations = DEFAULT_GENERATE_SYNTHETIC_ANNOTATIONS;

		schemaLanguage = XSD11_SCHEMA_LANGUAGE;

		// process -i: instance documents
		if (instances == null) {
			instances = new Vector();
		}

		if (SESEditor.sesValidationControl == 1) {
			instances.add(SESEditor.fileLocation + "/" + SESEditor.projName + "/xmlforxsd.xml");
			SESEditor.sesValidationControl = 0;
		} else {
			String rootNodeName = SESEditor.jtreeTograph.rootNodeName();
			instances.add(SESEditor.fileLocation + "/" + SESEditor.projName + "/" + rootNodeName + ".xml");
		}

		/*
		 * //---------------------------------------------------------------------------
		 * -start of argument matching and
		 * assignment------------------------------------- //-xsd11 -i
		 * xsd11_datatype_test.xml //----------------------------------------- for (int
		 * i = 0; i < argv.length; ++i) { String arg = argv[i]; if (arg.startsWith("-"))
		 * { String option = arg.substring(1); if (option.equals("l")) { // get schema
		 * language name if (++i == argv.length) {
		 * Console.addConsoleOutputln("error: Missing argument to -l option."); } else {
		 * schemaLanguage = argv[i]; } continue; }
		 * //-------------------------------------------------- if
		 * (option.equals("xsd11")) { schemaLanguage = XSD11_SCHEMA_LANGUAGE; continue;
		 * } //-------------------------------------------------- if
		 * (option.equals("p")) { // get parser name if (++i == argv.length) {
		 * Console.addConsoleOutputln("error: Missing argument to -p option.");
		 * continue; } String parserName = argv[i];
		 * 
		 * // create parser try { parser = XMLReaderFactory.createXMLReader(parserName);
		 * } catch (Exception e) { try { Parser sax1Parser =
		 * ParserFactory.makeParser(parserName); parser = new ParserAdapter(sax1Parser);
		 * Console.
		 * addConsoleOutputln("warning: Features and properties not supported on SAX1 parsers."
		 * ); } catch (Exception ex) { parser = null;
		 * Console.addConsoleOutputln("error: Unable to instantiate parser (" +
		 * parserName + ")"); e.printStackTrace(System.err); System.exit(1); } }
		 * continue; } if (arg.equals("-a")) { // process -a: schema documents if
		 * (schemas == null) { schemas = new Vector(); } while (i + 1 < argv.length &&
		 * !(arg = argv[i + 1]).startsWith("-")) { schemas.add(arg); ++i; } continue; }
		 * //-------------------------------------------------- if (arg.equals("-i")) {
		 * // process -i: instance documents if (instances == null) { instances = new
		 * Vector(); } while (i + 1 < argv.length && !(arg = argv[i +
		 * 1]).startsWith("-")) { instances.add(arg); ++i; } continue; }
		 * //-------------------------------------------------- if
		 * (option.equalsIgnoreCase("f")) { schemaFullChecking = option.equals("f");
		 * continue; } if (option.equalsIgnoreCase("hs")) { honourAllSchemaLocations =
		 * option.equals("hs"); continue; } if (option.equalsIgnoreCase("va")) {
		 * validateAnnotations = option.equals("va"); continue; } if
		 * (option.equalsIgnoreCase("ga")) { generateSyntheticAnnotations =
		 * option.equals("ga"); continue; } if (option.equals("h")) { printUsage();
		 * continue; } Console.addConsoleOutputln("error: unknown option (" + option +
		 * ")."); continue; } }
		 * //---------------------------------------------------------------------------
		 * -end of argument matching and assignment-------------------------------------
		 */
		// use default parser?
		if (parser == null) {
			// create parser
			try {
				parser = XMLReaderFactory.createXMLReader(DEFAULT_PARSER_NAME);
			} catch (Exception e) {
				Console.addConsoleOutput("error: Unable to instantiate parser (" + DEFAULT_PARSER_NAME + ")");
				e.printStackTrace(System.err);
				System.exit(1);
			}
		}

		try {
			// Create writer
			TypeInfoWriter writer = new TypeInfoWriter();
			writer.setOutput(System.out, "UTF8");

			// Create SchemaFactory and configure
			//factory.setProperty("http://saxon.sf.net/feature/xsd-version", "1.1");
			SchemaFactory factory = SchemaFactory.newInstance(DEFAULT_SCHEMA_LANGUAGE);
			//factory.setProperty("http://saxon.sf.net/feature/xsd-version", "1.1");
			factory.setErrorHandler(writer);

			try {
				factory.setFeature(SCHEMA_FULL_CHECKING_FEATURE_ID, schemaFullChecking);
			} catch (SAXNotRecognizedException e) {
				Console.addConsoleOutput(
						"warning: SchemaFactory does not recognize feature (" + SCHEMA_FULL_CHECKING_FEATURE_ID + ")");
			} catch (SAXNotSupportedException e) {
				Console.addConsoleOutput(
						"warning: SchemaFactory does not support feature (" + SCHEMA_FULL_CHECKING_FEATURE_ID + ")");
			}
			try {
				factory.setFeature(HONOUR_ALL_SCHEMA_LOCATIONS_ID, honourAllSchemaLocations);
			} catch (SAXNotRecognizedException e) {
				Console.addConsoleOutput(
						"warning: SchemaFactory does not recognize feature (" + HONOUR_ALL_SCHEMA_LOCATIONS_ID + ")");
			} catch (SAXNotSupportedException e) {
				Console.addConsoleOutput(
						"warning: SchemaFactory does not support feature (" + HONOUR_ALL_SCHEMA_LOCATIONS_ID + ")");
			}
			try {
				factory.setFeature(VALIDATE_ANNOTATIONS_ID, validateAnnotations);
			} catch (SAXNotRecognizedException e) {
				System.err
						.println("warning: SchemaFactory does not recognize feature (" + VALIDATE_ANNOTATIONS_ID + ")");
			} catch (SAXNotSupportedException e) {
				Console.addConsoleOutput(
						"warning: SchemaFactory does not support feature (" + VALIDATE_ANNOTATIONS_ID + ")");
			}
			try {
				factory.setFeature(GENERATE_SYNTHETIC_ANNOTATIONS_ID, generateSyntheticAnnotations);
			} catch (SAXNotRecognizedException e) {
				Console.addConsoleOutput("warning: SchemaFactory does not recognize feature ("
						+ GENERATE_SYNTHETIC_ANNOTATIONS_ID + ")");
			} catch (SAXNotSupportedException e) {
				Console.addConsoleOutput(
						"warning: SchemaFactory does not support feature (" + GENERATE_SYNTHETIC_ANNOTATIONS_ID + ")");
			}

			// Build Schema from sources
			Schema schema;
			if (schemas != null && schemas.size() > 0) {
				final int length = schemas.size();
				StreamSource[] sources = new StreamSource[length];
				for (int j = 0; j < length; ++j) {
					sources[j] = new StreamSource((String) schemas.elementAt(j));
				}
				schema = factory.newSchema(sources);
			} else {
				schema = factory.newSchema();
			}

			// Setup validator and parser
			ValidatorHandler validator = schema.newValidatorHandler();
			parser.setContentHandler(validator);
			if (validator instanceof DTDHandler) {
				parser.setDTDHandler((DTDHandler) validator);
			}
			parser.setErrorHandler(writer);
			validator.setContentHandler(writer);
			validator.setErrorHandler(writer);
			writer.setTypeInfoProvider(validator.getTypeInfoProvider());

			try {
				validator.setFeature(SCHEMA_FULL_CHECKING_FEATURE_ID, schemaFullChecking);
			} catch (SAXNotRecognizedException e) {
				Console.addConsoleOutput(
						"warning: Validator does not recognize feature (" + SCHEMA_FULL_CHECKING_FEATURE_ID + ")");
			} catch (SAXNotSupportedException e) {
				Console.addConsoleOutput(
						"warning: Validator does not support feature (" + SCHEMA_FULL_CHECKING_FEATURE_ID + ")");
			}
			try {
				validator.setFeature(HONOUR_ALL_SCHEMA_LOCATIONS_ID, honourAllSchemaLocations);
			} catch (SAXNotRecognizedException e) {
				Console.addConsoleOutput(
						"warning: Validator does not recognize feature (" + HONOUR_ALL_SCHEMA_LOCATIONS_ID + ")");
			} catch (SAXNotSupportedException e) {
				Console.addConsoleOutput(
						"warning: Validator does not support feature (" + HONOUR_ALL_SCHEMA_LOCATIONS_ID + ")");
			}
			try {
				validator.setFeature(VALIDATE_ANNOTATIONS_ID, validateAnnotations);
			} catch (SAXNotRecognizedException e) {
				Console.addConsoleOutput(
						"warning: Validator does not recognize feature (" + VALIDATE_ANNOTATIONS_ID + ")");
			} catch (SAXNotSupportedException e) {
				Console.addConsoleOutput(
						"warning: Validator does not support feature (" + VALIDATE_ANNOTATIONS_ID + ")");
			}
			try {
				validator.setFeature(GENERATE_SYNTHETIC_ANNOTATIONS_ID, generateSyntheticAnnotations);
			} catch (SAXNotRecognizedException e) {
				Console.addConsoleOutput(
						"warning: Validator does not recognize feature (" + GENERATE_SYNTHETIC_ANNOTATIONS_ID + ")");
			} catch (SAXNotSupportedException e) {
				Console.addConsoleOutput(
						"warning: Validator does not support feature (" + GENERATE_SYNTHETIC_ANNOTATIONS_ID + ")");
			}

			// Validate instance documents and print type information
			if (instances != null && instances.size() > 0) {
				final int length = instances.size();
				for (int j = 0; j < length; ++j) {
					parser.parse((String) instances.elementAt(j));
				}
			}
		} catch (SAXParseException e) {
			// ignore
		} catch (Exception e) {
			Console.addConsoleOutput("error: Parse error occurred - " + e.getMessage());
			if (e instanceof SAXException) {
				Exception nested = ((SAXException) e).getException();
				if (nested != null) {
					e = nested;
				}
			}
			// e.printStackTrace(System.err);//mark

			// these 3 lines are alternative of the above one line marked //mark
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			Console.addConsoleOutput(errors.toString());
		}
	} // main(String[])

	/** Prints the usage. */
	private static void printUsage() {

		Console.addConsoleOutput("usage: java jaxp.TypeInfoWriter (options) ...");
		Console.addConsoleOutput("");

		Console.addConsoleOutput("options:");
		Console.addConsoleOutput("  -l name     Select schema language by name.");
		Console.addConsoleOutput("  -p name     Select parser by name.");
		Console.addConsoleOutput("  -a uri ...  Provide a list of schema documents");
		Console.addConsoleOutput("  -i uri ...  Provide a list of instance documents to validate");
		Console.addConsoleOutput("  -f  | -F    Turn on/off Schema full checking.");
		Console.addConsoleOutput("              NOTE: Not supported by all schema factories and validators.");
		Console.addConsoleOutput("  -hs | -HS   Turn on/off honouring of all schema locations.");
		Console.addConsoleOutput("              NOTE: Not supported by all schema factories and validators.");
		Console.addConsoleOutput("  -va | -VA   Turn on/off validation of schema annotations.");
		Console.addConsoleOutput("              NOTE: Not supported by all schema factories and validators.");
		Console.addConsoleOutput("  -ga | -GA   Turn on/off generation of synthetic schema annotations.");
		Console.addConsoleOutput("              NOTE: Not supported by all schema factories and validators.");
		Console.addConsoleOutput("  -xsd11      Turn on XSD 1.1 support.");
		Console.addConsoleOutput("  -h          This help screen.");

		Console.addConsoleOutput("");
		Console.addConsoleOutput("defaults:");
		Console.addConsoleOutput("  Schema language:                 " + DEFAULT_SCHEMA_LANGUAGE);
		Console.addConsoleOutput("  Parser:                          " + DEFAULT_PARSER_NAME);
		Console.addConsoleOutput("  Schema full checking:            ");
		Console.addConsoleOutput(DEFAULT_SCHEMA_FULL_CHECKING ? "on" : "off");
		Console.addConsoleOutput("  Honour all schema locations:     ");
		Console.addConsoleOutput(DEFAULT_HONOUR_ALL_SCHEMA_LOCATIONS ? "on" : "off");
		Console.addConsoleOutput("  Validate annotations:            ");
		Console.addConsoleOutput(DEFAULT_VALIDATE_ANNOTATIONS ? "on" : "off");
		Console.addConsoleOutput("  Generate synthetic annotations:  ");
		Console.addConsoleOutput(DEFAULT_GENERATE_SYNTHETIC_ANNOTATIONS ? "on" : "off");

	} // printUsage()

} // class TypeInfoWriter
