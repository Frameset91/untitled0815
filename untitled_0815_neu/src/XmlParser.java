import javax.xml.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
public class XmlParser {

	public static void readXML (File file) {
		try {
		DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbfactory.newDocumentBuilder();
		Document doc = dBuilder.parse(file);
		doc.getDocumentElement().normalize();
		
//		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		NodeList nList = doc.getElementsByTagName(doc.getDocumentElement().getNodeName());
		System.out.println("-----------------------");
 
		for (int temp = 0; temp < nList.getLength(); temp++) {
 
		   Node nNode = nList.item(temp);
		   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
 
		      Element eElement = (Element) nNode;
 
		      System.out.println("freigabe : " + getTagValue("freigabe", eElement));
		      System.out.println("satzstatus : " + getTagValue("satzstatus", eElement));
	              System.out.println("gegnerzug : " + getTagValue("gegnerzug", eElement));
		      System.out.println("sieger : " + getTagValue("sieger", eElement));
 
		
		
		   }
		   }
		}
		catch (Exception e){
			e.printStackTrace();
		}
		}
		 private static String getTagValue(String sTag, Element eElement) {
				NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
			 
			        Node nValue = (Node) nlList.item(0);
			 
				return nValue.getNodeValue();
			  }
		   
	
	
}
		
		
		
		
		
		
		
