import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Currency {
    public static void main(String[] args) throws Exception {
        String[][] rates = getRates();

        for (String[] rate : rates) {
            System.out.println(rate[0] + " " + rate[1]);
        }
    }

    private static String[][] getRates() throws Exception {
        HashMap<String, NodeList> result = new HashMap<>();
        String[][] rates;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String url = "http://www.cbr.ru/scripts/xml_daily.asp?date_req=" + simpleDateFormat.format(date);
        Document doc = loadDocument(url);
        NodeList n1 = doc.getElementsByTagName("Valute");

        for (int i = 0; i < n1.getLength(); i++) {
            Node c = n1.item(i);
            NodeList nodeChilds = c.getChildNodes();

            for (int j = 0; j < nodeChilds.getLength(); j++){
                if(nodeChilds.item(j).getNodeName().equals("CharCode")){
                    result.put(nodeChilds.item(j).getTextContent(), nodeChilds);
                }
            }
        }

        int k = 0;
        rates = new String[result.size()][2];

        for (Map.Entry<String, NodeList> entry : result.entrySet()) {
            NodeList temp = entry.getValue();
            double value = 0;
            int nominal = 0;

            for (int i =0; i < temp.getLength(); i++) {
                if(temp.item(i).getNodeName().equals("Value")){
                    value = Double.parseDouble((temp.item(i).getTextContent()).replace(',', '.'));
                } else if(temp.item(i).getNodeName().equals("Nominal")){
                    nominal = Integer.parseInt(temp.item(i).getTextContent());
                }
            }
            rates[k][0] = entry.getKey();
            rates[k][1] = ((double)Math.round(value / nominal * 10000)) / 10000 + " рублей";
            k++;
        }
        return rates;
    }

    private static Document loadDocument(String url) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        return factory.newDocumentBuilder().parse(new URL(url).openStream());
    }
}
