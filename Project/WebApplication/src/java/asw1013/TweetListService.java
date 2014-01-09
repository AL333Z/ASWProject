package asw1013;

import asw1013.util.TweetListFile;
import asw1013.entity.TweetList;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.w3c.dom.Document;

/**
 * Service from which a client can get a list of tweets
 */

@WebServlet(urlPatterns = {"/tweets"}, asyncSupported=true)
public class TweetListService extends AbstractXmlServiceServlet{
    
    private LinkedList<AsyncContext> contexts = new LinkedList<AsyncContext>();

    @Override
    protected void operations(Document data, HttpSession session, 
            HttpServletRequest request, HttpServletResponse response, 
            ManageXML mngXML) throws Exception {
        
        String operation = data.getElementsByTagName("operation").item(0).getTextContent();
        
        if(operation.equals("getTweets")){
            
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
            
            
        } else if (operation.equals("postTweet")){
            
            // TODO store the new tweet in the db
            
            
            
            // Notify the event to listeners
            synchronized (this) {
                for (AsyncContext asyncContext : contexts) {
                    OutputStream aos = asyncContext.getResponse().getOutputStream();
                    // don't send anything to the client
                    aos.close();
                    asyncContext.complete();
                }
                contexts.clear();
            }
            
        } else if(operation.equals("waitForUpdate")) {
            
            // This will stop the transmission of the HTTP response until a timeout occurs
            // or somebody will "unlock" this request (when somebody posts a new tweet)

            AsyncContext asyncContext = request.startAsync();

            asyncContext.setTimeout(10 * 1000);
            asyncContext.addListener(new AsyncListener() {
                @Override
                public void onTimeout(AsyncEvent e) throws IOException {
                    AsyncContext asyncContext = e.getAsyncContext();
                    boolean confirm;
                    synchronized (this) {
                        if ((confirm = contexts.contains(asyncContext))) {
                            contexts.remove(asyncContext);
                        }
                    }
                    if (confirm) {
                        OutputStream tos = asyncContext.getResponse().getOutputStream();
                        // don't send anything to the client
                        tos.close();
                        asyncContext.complete();
                    }
                }

                @Override
                public void onComplete(AsyncEvent event) throws IOException {
                }

                @Override
                public void onError(AsyncEvent event) throws IOException {
                }

                @Override
                public void onStartAsync(AsyncEvent event) throws IOException {
                }

            });
            synchronized (this) {
                contexts.add(asyncContext);
            }

        }
        
        
    }
    
}
