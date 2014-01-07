<%@page import="asw1013.entity.User"%>
<%@page import="javax.xml.bind.Marshaller"%>
<%@page import="asw1013.entity.UserList"%>
<%@page import="javax.xml.bind.JAXBContext"%>
<%@page import="java.net.URL"%>
<%@page import="asw1013.HTTPClient"%>
<%@page import="org.w3c.dom.Element"%>
<%@page import="asw1013.ManageXML"%>
<%@page import="org.w3c.dom.Document"%>
<%@ page import="java.util.*" %>
<%@ page import="asw1013.util.UserListFile" %>


<%

    ManageXML mngXML = new ManageXML();
    HTTPClient hc = new HTTPClient();
    JAXBContext jc = JAXBContext.newInstance(UserList.class);
    Marshaller marsh = jc.createMarshaller();
    Document doc = mngXML.newDocument();
    
    //TODO remove hardcoded url
    hc.setBase(new URL("http://localhost:8080/WebApplication/"));
//    hc.setBase(new URL(getServletContext().getContextPath()));

    Document data = mngXML.newDocument();
    Element rootReq = data.createElement("registration");
    
    User user = new User();
    user.email = request.getParameter("emailsignup");
    user.pass = request.getParameter("passwordsignup");
    user.username = request.getParameter("usernamesignup");

    UserList ul = new UserList();
    ul.users.add(user);
    
    marsh.marshal(ul, doc);
    log("doc "+doc.toString());
    
//    rootReq.appendChild(doc);
//    data.appendChild(rootReq);
//
//    log("sending registration: "+data.toString());
//    
//    Document answer = hc.execute("users", data);
%>
