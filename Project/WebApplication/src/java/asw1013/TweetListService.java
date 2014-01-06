package asw1013;

import java.io.OutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Service from which a client can get a list of tweets
 */

@WebServlet(urlPatterns = {"/tweets"})
public class TweetListService extends AbstractXmlServiceServlet{

    @Override
    protected void operations(Document data, HttpSession session, HttpServletResponse response, ManageXML mngXML) throws Exception {
        
        Element recvRoot = data.getDocumentElement();
        // TODO get from XML the start and stop numbers of tweets to sent (pagination)
        
        // TODO read the XML file of tweets and marshal them (it's better to do this in a separate library class)
        
        // Example root element to send, replace this with something useful!!
        Element sendRoot = mngXML.newDocument().createElement("boh");
        
        JAXBContext jc = JAXBContext.newInstance(TweetList.class);
        Marshaller marsh = jc.createMarshaller();
        Document doc = mngXML.newDocument();
        marsh.marshal(sendRoot, doc);
        
        OutputStream os = response.getOutputStream();
        mngXML.transform(os, doc);
        os.close();
        
    }
    
}
