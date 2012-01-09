import java.util.HashMap;
import java.util.Map;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Settings {
	public Map<String, String> settings;
	
	public Settings(String settingsLocation) {

		settings = new HashMap<String, String>();

		try {
			
			File file = new File(settingsLocation);	
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			NodeList nodeLst = doc.getElementsByTagName("machine");

			for (int s = 0; s < nodeLst.getLength(); s++) {
				Node fstNode = nodeLst.item(s);
				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
					get_value_by_key("image_directory", settings, fstNode);
					get_value_by_key("csv_file", settings, fstNode);
					get_value_by_key("log_file", settings, fstNode);					
				}	
			}

		} catch (Exception e) {
			JFrame frame = new JFrame();
			JOptionPane.showMessageDialog(frame, e.getMessage());
			e.printStackTrace();
		}
	}

	private static void get_value_by_key(String key, Map<String, String> settings,
			Node fstNode) {
		Element fstElmnt = (Element) fstNode;
		NodeList fstNmElmntLst = fstElmnt.getElementsByTagName(key);
		Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
		NodeList fstNm = fstNmElmnt.getChildNodes();

		settings.put(key, ((Node) fstNm.item(0)).getNodeValue());
	}
}