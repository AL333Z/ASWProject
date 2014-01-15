/*
 * Copyright 2014 al333z.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package asw1013.util;

import asw1013.ManageXML;
import asw1013.entity.User;
import asw1013.entity.UserList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.ServletContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.w3c.dom.Document;

/**
 * Utility class to read and write data from the XML file of users.
 *
 * This class is a singleton (only one instance per JVM is allowed) to solve
 * concurrency problems.
 */
public class UserListFile {

    private volatile static UserListFile instance = null;

    private final File userFile;
    private JAXBContext context;
    private ManageXML mngXML;

    /**
     * Return a singleton object of UserListFile
     *
     * @param servletContext
     * @return
     * @throws Exception
     */
    public static UserListFile getInstance(ServletContext servletContext) throws Exception {
        if (instance == null) {
            synchronized (UserListFile.class) {
                if (instance == null) {
                    instance = new UserListFile(servletContext);
                }
            }
        }
        return instance;
    }

    private UserListFile(ServletContext servletContext) throws Exception {
        context = JAXBContext.newInstance(UserList.class);
        mngXML = new ManageXML();
        userFile = new File(servletContext.getRealPath("/WEB-INF/xml/users.xml")); // this only works with default config of tomcat
    }

    /**
     * Read the xml db
     * @return the list of users
     * @throws Exception 
     */
    public synchronized UserList readFile() throws Exception {
        if (!userFile.exists()) {
            createFile();
        }
        InputStream in = new FileInputStream(userFile);
        Document tweetsDoc = mngXML.parse(in);
        Unmarshaller u = context.createUnmarshaller();
        UserList users = (UserList) u.unmarshal(tweetsDoc);
        return users;
    }

    /**
     * Register a new user, adding a new entry in the xml db
     * @param user
     * @throws Exception 
     */
    public synchronized void registerUser(User user) throws Exception {
        UserList ul = readFile();
        if (!isUserRegistered(user, ul)) {
            user.isAdmin = false;
            ul.users.add(user);
            writeFile(ul);
        } else {
            throw new Exception("User already registered.");
        }
    }

    /**
     * Delete a user, removing its entry from the xml db
     * @param username
     * @throws Exception 
     */
    public synchronized void deleteUser(String username) throws Exception {
        UserList ul = readFile();

        for (User usr : ul.users) {
            usr.following.usernames.remove(username);
        }
        for (User usr : ul.users) {
            if (username.equals(usr.username)) {
                ul.users.remove(usr);
                writeFile(ul);
                return;
            }
        }
        throw new Exception("User does not exist.");
    }

    /**
     * Search a user in the xml db
     * @param str the string to match
     * @return the list of users that match str
     * @throws Exception 
     */
    public synchronized UserList searchUsers(String str) throws Exception {
        UserList ul = readFile();
        UserList returnList = new UserList();
        for (User usr : ul.users) {
            if (usr.username.contains(str) || usr.email.contains(str)) {
                returnList.users.add(usr);
            }
        }
        return returnList;
    }

    /**
     * Add/remove the following relationship beetween two users
     * @param followerUsername the follower username
     * @param followingUsername the followed username
     * @throws Exception 
     */
    public synchronized void toggleFollowing(String followerUsername, String followingUsername) throws Exception {
        UserList ul = readFile();
        for (User usr : ul.users) {
            if (usr.username.equals(followerUsername)) {
                if (usr.following.usernames.contains(followingUsername)) {
                    usr.following.usernames.remove(followingUsername);
                } else {
                    usr.following.usernames.add(followingUsername);
                }
                writeFile(ul);
                return;
            }
        }
    }

    /**
     * Check user credentials
     * @param user
     * @return the user, if credentials are ok
     * @throws Exception 
     */
    public synchronized User loginUser(User user) throws Exception {
        User usr = getUserByUsername(user.username);
        if (usr.pass.equals(user.pass)) {
            return usr;
        } else {
            throw new Exception("Wrong password.");
        }
    }

    /**
     * @param username
     * @return user information
     * @throws Exception 
     */
    public synchronized User getUserByUsername(String username) throws Exception {
        UserList ul = readFile();
        for (User usr : ul.users) {
            if (usr.username.equals(username)) {
                return usr;
            }
        }
        throw new Exception("User does not exist.");
    }

    private synchronized void writeFile(UserList userList) throws Exception {
        Marshaller marsh = context.createMarshaller();
        Document doc = mngXML.newDocument();
        marsh.marshal(userList, doc);
        OutputStream out = new FileOutputStream(userFile);
        mngXML.transform(out, doc);
        out.close();
    }

    private boolean isUserRegistered(User user, UserList ul) {
        for (User usr : ul.users) {
            if (usr.username.equals(user.username)) {
                return true;
            }
        }
        return false;
    }

    private void createFile() throws Exception {
        
        
        User admin = new User();
        admin.username = "admin";
        admin.pass = "password";
        admin.email = "admin@share2me.com";
        admin.isAdmin = true;

        UserList ul = new UserList();
        ul.users.add(admin);
        writeFile(ul);
    }

}
