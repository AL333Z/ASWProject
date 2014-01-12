package asw1013;

import asw1013.entity.Tweet;
import asw1013.util.TweetListFile;
import asw1013.entity.TweetList;
import asw1013.util.UserListFile;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
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
            
            TweetListFile tweetFile = TweetListFile.getInstance();
            TweetList tweetList = tweetFile.readFile();

            TweetList tweetListToSend;
            String myUsername = (String) session.getAttribute("username");
            if(myUsername == null){
                // Return tweets of all users
                tweetListToSend = tweetList;
            } else if(data.getElementsByTagName("tweetsOfUsername").getLength()==0){
                // Return tweets of users I'm following
                UserListFile ufile = UserListFile.getInstance();
                List<String> followingUsernames = ufile.getUserByUsername(myUsername).following.usernames;
                tweetListToSend = new TweetList();
                for(Tweet tweet : tweetList.tweets){
                    if(followingUsernames.contains(tweet.username) || tweet.username.equals(myUsername)){
                        tweetListToSend.tweets.add(tweet);
                    }
                }
            } else {
                // Return tweets of an user specified by the request
                tweetListToSend = new TweetList();
                String username = data.getElementsByTagName("tweetsOfUsername").item(0).getTextContent();
                for(Tweet tweet : tweetList.tweets){
                    if(tweet.username.equals(username)){
                        tweetListToSend.tweets.add(tweet);
                    }
                }
            }
            
            JAXBContext jc = JAXBContext.newInstance(TweetList.class);
            Marshaller marsh = jc.createMarshaller();
            Document doc = mngXML.newDocument();
            marsh.marshal(tweetListToSend, doc);

            OutputStream os = response.getOutputStream();
            mngXML.transform(os, doc);
            os.close();
            
            
        } else if (operation.equals("postTweet")){
            
            Tweet tweet = new Tweet();
            tweet.message = data.getElementsByTagName("tweetText").item(0).getTextContent();
            tweet.date = new Date();
            tweet.username = (String) session.getAttribute("username");
            
            TweetListFile tweetFile = TweetListFile.getInstance();
            tweetFile.addTweet(tweet);
            
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
            
            // Close the output stream of this connection
            response.getOutputStream().close();
            
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
