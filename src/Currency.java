import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Currency {
    public static void main(String[] args) throws Exception {
        String[][] rates = getRates();
    }

    private static String[][] getRates() throws Exception {
        String[][] rates = null;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String url = "http://www.cbr.ru/scripts/xml_daily.asp?date_req=" + simpleDateFormat.format(date);
        Document doc = loadDocument(url);

        System.out.println(doc.getXmlVersion());
        
        return rates;
    }

    private static Document loadDocument(String url) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        return factory.newDocumentBuilder().parse(new URL(url).openStream());
    }
}
