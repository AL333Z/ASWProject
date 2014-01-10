package asw1013.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * An user of the website
 */
public class User {
    
    public String username;
    public String pass;
    public String email;
    public boolean isAdmin;
    public Following following;
    
    public User(){
        following = new Following();
    }
        
}
