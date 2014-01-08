<%-- 
    Document   : login
    Created on : 7-gen-2014, 14.50.23
    Author     : al333z
--%>
<%
    // setting user as logged in
    session.removeAttribute("isLoggedIn");
    session.removeAttribute("username");
    session.removeAttribute("email");
    
    // redirecting to main page
    String site = new String("index.jsp");
    response.setStatus(response.SC_OK);
    response.sendRedirect(site);
    
%>
