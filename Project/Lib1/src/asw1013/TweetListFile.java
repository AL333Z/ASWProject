package asw1013;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.w3c.dom.Document;

/**
 * Utility class to read and write 
 */
public class TweetListFile {
    
    public static final File TWEET_FILE = new File("/Users/mattia/tweets.xml");
    
    private JAXBContext context;
    private ManageXML mngXML;
    
    public TweetListFile() throws Exception {
        context = JAXBContext.newInstance(TweetList.class);
        mngXML = new ManageXML();
    }
    
    public TweetList readFile() throws Exception{
        if(!TWEET_FILE.exists()){
            createFile();
        }
        InputStream in = new FileInputStream(TWEET_FILE);
        Document tweetsDoc = mngXML.parse(in);
        Unmarshaller u = context.createUnmarshaller();
        TweetList tweets = (TweetList) u.unmarshal(tweetsDoc);
        return tweets;
    }
    
    public void writeFile(TweetList tweetList) throws Exception {
        Marshaller marsh = context.createMarshaller();
        Document doc = mngXML.newDocument();
        marsh.marshal(tweetList, doc);
        OutputStream out = new FileOutputStream(TWEET_FILE);
        mngXML.transform(out, doc);
        out.close();
    }
    
    private void createFile() throws Exception {
        Tweet tweet = new Tweet();
        tweet.message="Hello World!";
        tweet.date=new Date(2014, 01, 01, 00, 00, 00);
        tweet.username="admin";
        TweetList tl = new TweetList();
        tl.tweets.add(tweet);
        writeFile(tl);
    }
    
}
