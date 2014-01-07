package asw1013.entity;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A list of tweets
 */
@XmlRootElement
public class TweetList {
    
    public List<Tweet> tweets;
       
    public TweetList(){
        this.tweets = new ArrayList<Tweet>();
    }
    
}
