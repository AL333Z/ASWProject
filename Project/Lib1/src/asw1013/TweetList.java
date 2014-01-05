package asw1013;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A list of tweets
 */
@XmlRootElement
public class TweetList {
    
    public List<Tweet> tweets;
    
}
