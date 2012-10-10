package utilities;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import org.w3c.dom.*;

public class XmlParser {

	public static ServerMessage readXML(File file) throws Exception {
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

			// create ServerMessage instance
			msg = new ServerMessage();

			// jedes Node Element behandeln
			for (int temp = 0; temp < nList.getLength(); temp++) {
				// Node Element aus Liste erzeugen
				Node nNode = nList.item(temp);
				
				//Wenn ein ELEMENT und kein KOPF Node
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					msg.setFreigabe(getTagValue("freigabe", eElement));
					msg.setSatzstatus(getTagValue("satzstatus", eElement));
					msg.setGegnerzug(getTagValue("gegnerzug", eElement));
					msg.setSieger(getTagValue("sieger", eElement));

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return msg;
	}

	/**
	 * Diese Methode liefert den Wert eines Tags im XML File
	 * @param sTag String Name des Tags
	 * @param eElement
	 * @return String Wert des Tags
	 */
	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
				.getChildNodes();

		Node nValue = (Node) nlList.item(0);

		return nValue.getNodeValue();
	}

}
