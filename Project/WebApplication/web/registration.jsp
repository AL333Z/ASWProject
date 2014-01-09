<%@page import="asw1013.entity.User"%>
<%@ page import="asw1013.util.UserListFile" %>

<%
    User user = new User();
    user.email = request.getParameter("emailsignup");
    user.pass = request.getParameter("passwordsignup");
    user.username = request.getParameter("usernamesignup");

    UserListFile ulf = new UserListFile();
    ulf.registerUser(user);

    // setting user as logged in
    session.setAttribute("isLoggedIn", true);
    session.setAttribute("username", user.username);
    session.setAttribute("email", user.email);
    session.setAttribute("isAdmin", new Boolean(false));
    
    // redirecting to main page
    String site = new String("index.jsp");
    response.setStatus(response.SC_OK);
    response.sendRedirect(site);
    
%>
