<%@page import="asw1013.entity.User"%>
<%@ page import="asw1013.util.UserListFile" %>

<%
    User user = new User();
    user.pass = request.getParameter("password");
    user.username = request.getParameter("username");

    UserListFile ulf = UserListFile.getInstance(getServletContext());
    user = ulf.loginUser(user);
    
    // setting user as logged in
    session.setAttribute("isLoggedIn", true);
    session.setAttribute("username", user.username);
    session.setAttribute("email", user.email);
    session.setAttribute("isAdmin", user.isAdmin);

    // redirecting to main page
    String site = new String("index.jsp");
    response.setStatus(response.SC_OK);
    response.sendRedirect(site);
    
%>
