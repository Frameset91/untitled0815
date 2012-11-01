package utilities.communication;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import org.w3c.dom.*;


/**
 * Klasse zum Parsen des Serverfiles und zur Erstellung eines ServerMessage
 * Objektes
 * 
 * @author Bjoern List
 * 
 */
public class XmlParser {

	private static XmlParser singleton = null;

	/**
	 * privater Konstruktor
	 */
	private XmlParser() {

	}
	
	/**
	 * Liefert das Singleton Objekt zurück und erstellt es wenn notwendig
	 * @return XmlParser singleton
	 */
	public static XmlParser getInstance() {
		if (singleton == null) {
			singleton = new XmlParser();
		}
		return singleton;

	}

	/**
	 * Die Methode parst eine XML und speichert alle Elemente in einem ServerMessage Objekt
	 * 
	 * @param file XML Datei, die geparst werden soll
	 * @return ServerMessage
	 * @throws Exception
	 */
	public ServerMessage readXML(File file) throws Exception {
		ServerMessage msg = null;
		try {
			DocumentBuilderFactory dbfactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbfactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();

			// alle Elemente des XML Files in NodeList einfügen
			NodeList nList = doc.getElementsByTagName(doc.getDocumentElement()
					.getNodeName());

			// neue ServerMessage erzeugen
			msg = new ServerMessage();

			// jedes Node Element behandeln
			for (int temp = 0; temp < nList.getLength(); temp++) {
				// Node Element aus Liste erzeugen
				Node nNode = nList.item(temp);

				// Wenn ein ELEMENT und kein KOPF Node
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					msg.setRelease(getTagValue("freigabe", eElement));
					msg.setSetstatus(getTagValue("satzstatus", eElement));
					msg.setOppmove(getTagValue("gegnerzug", eElement));
					msg.setWinner(getTagValue("sieger", eElement));

				}
			}

		} catch (Exception e) {
			System.out.println("Keine Datei vorhanden");
		}

		return msg;
	}

	/**
	 * Diese Methode liefert den Wert eines Tags im XML File
	 * 
	 * @param sTag
	 *            String Name des Tags
	 * @param eElement
	 * @return String Wert des Tags
	 */
	private String getTagValue(String sTag, Element eElement) throws Exception {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
				.getChildNodes();

		Node nValue = (Node) nlList.item(0);

		return nValue.getNodeValue();
	}

}
