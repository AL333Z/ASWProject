<%@page import="asw1013.entity.User"%>
<%@ page import="asw1013.util.UserListFile" %>

<%
    // redirecting to main page
    String site = getServletContext().getContextPath()+"/index.jsp";
    response.setStatus(response.SC_OK);
    response.sendRedirect(site);
    
%>
