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
 *
 * @author al333z
 */
public class UserListFile {

    private volatile static UserListFile instance = null;

    private final File userFile;
    private JAXBContext context;
    private ManageXML mngXML;

    public static UserListFile getInstance(ServletContext servletContext) throws Exception {
        if(instance == null){
            synchronized(UserListFile.class){
                if(instance == null){
                    instance = new UserListFile(servletContext);
                }
            }
        }
        return instance;
    }

    private UserListFile(ServletContext servletContext) throws Exception {
        context = JAXBContext.newInstance(UserList.class);
        mngXML = new ManageXML();
        userFile = new File(servletContext.getRealPath("/WEB-INF/users.xml")); // this only works with default config of tomcat
    }

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

    public synchronized void deleteUser(String username) throws Exception {
        UserList ul = readFile();

        for (User usr : ul.users) {
            if (username.equals(usr.username)) {
                ul.users.remove(usr);
                writeFile(ul);
                return;
            }
        }
        throw new Exception("User does not exist.");
    }

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

    public synchronized void toggleFollowing(String followerUsername, String followingUsername) throws Exception {
        UserList ul = readFile();
        for(User usr : ul.users){
            if(usr.username.equals(followerUsername)){
                if(usr.following.usernames.contains(followingUsername)){
                    usr.following.usernames.remove(followingUsername);
                } else {
                    usr.following.usernames.add(followingUsername);
                }
                writeFile(ul);
                return;
            }
        }
    }

    public synchronized User loginUser(User user) throws Exception {
        User usr = getUserByUsername(user.username);
        if (usr.pass.equals(user.pass)) {
            return usr;
        } else {
            throw new Exception("Wrong password.");
        }
    }

    public synchronized User getUserByUsername(String username) throws Exception {
        UserList ul = readFile();
        for(User usr : ul.users){
            if(usr.username.equals(username)){
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
