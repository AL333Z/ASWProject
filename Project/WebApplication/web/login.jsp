<%-- 
    Document   : login
    Created on : 7-gen-2014, 14.50.23
    Author     : al333z
--%>
<%@ page import="java.util.*" %>
<%
    Enumeration flds = request.getParameterNames();
    String str = "";
    while (flds.hasMoreElements()) {
        String field = (String) flds.nextElement();
        String value = request.getParameter(field);

%>
<li><%= field%> = <%= value%></li>
    <%
        }
    %>
