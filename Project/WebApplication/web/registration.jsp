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
    User user = new User();
    user.email = request.getParameter("emailsignup");
    user.pass = request.getParameter("passwordsignup");
    user.username = request.getParameter("usernamesignup");

    UserListFile ulf = new UserListFile();
    ulf.registerUser(user);
%>
