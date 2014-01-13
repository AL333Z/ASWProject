<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<!DOCTYPE html>
<<html lang="en" class="no-js"> <!--<![endif]-->
    <head>
        <meta charset="UTF-8" />
        <title>Search</title>
        <link rel="stylesheet" type="text/css" href="style-sheets/style.css" />
    </head>
    <body>	
        <%@ include file="/WEB-INF/jspf/navigation.jspf" %>
        <div class="container">
            <APPLET codebase="applet/" code="asw1013.UserSearchApplet" 
                    archive="Lib1.jar,Applet1.jar" 
                    width=960 height=500>
                
                <param name="sessionId" value="<%= session.getId()%>">
                <param name="isLoggedIn" value="<%= (session.getAttribute("isLoggedIn") != null && ((Boolean)session.getAttribute("isLoggedIn")) == true) ? "Y" : "N" %>">
                <param name="isAdmin" value="<%= (session.getAttribute("isAdmin") != null && ((Boolean)session.getAttribute("isAdmin")) == true) ? "Y" : "N" %>">
                
            </APPLET>
        </div> 
    </body>
</html>