package asw1013;

import asw1013.util.TweetListFile;
import asw1013.entity.TweetList;
import java.io.OutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Service from which a client can get a list of tweets
 */

@WebServlet(urlPatterns = {"/tweets"})
public class TweetListService extends AbstractXmlServiceServlet{

    @Override
    protected void operations(Document data, HttpSession session, 
            HttpServletRequest request, HttpServletResponse response, 
            ManageXML mngXML) throws Exception {
       
        Element recvRoot = data.getDocumentElement();
        // TODO get from XML the start and stop numbers of tweets to sent (pagination)

        TweetListFile tweetFile = new TweetListFile();
        TweetList tweetList = tweetFile.readFile();
       
        // TODO filter from the tweetlist only the tweets that we want to send to the client
        TweetList tweetListToSend = tweetList; //only for example
        
        JAXBContext jc = JAXBContext.newInstance(TweetList.class);
        Marshaller marsh = jc.createMarshaller();
        Document doc = mngXML.newDocument();
        marsh.marshal(tweetListToSend, doc);
       
        
        OutputStream os = response.getOutputStream();
        mngXML.transform(os, doc);
        os.close();
        
    }
    
}
