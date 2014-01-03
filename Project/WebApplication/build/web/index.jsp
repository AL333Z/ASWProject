<%-- 
    Document   : index.jsp
    Created on : 3-gen-2014, 15.13.35
    Author     : al333z
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    
    <body>
    
        <h1>Hello World!</h1>
        
        <APPLET codebase="applet/" code="asw1013.ListApplet" 
                archive="Lib1.jar,Applet1.jar" 
                width=500 height=500>
            <param name="sessionId" value="<%= session.getId()%>">

        </APPLET>
        
    </body>
</html>
