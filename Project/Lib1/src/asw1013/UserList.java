package asw1013;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A list of users
 */
@XmlRootElement
public class UserList {
    
    public List<User> users;
    
}
