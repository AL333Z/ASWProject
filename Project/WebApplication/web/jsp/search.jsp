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
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/style-sheets/style.css" />
    </head>
    <body>	
        <%@ include file="/WEB-INF/jspf/navigation.jspf" %>
        <div class="container">
            <APPLET codebase="../applet/" code="asw1013.applet.UserSearchApplet" 
                    archive="Lib1.jar,Lib2.jar,Applet3.jar" 
                    width=1000 height=500>
                
                <param name="sessionId" value="<%= session.getId()%>">
                <param name="isLoggedIn" value="<%= (session.getAttribute("isLoggedIn") != null && ((Boolean)session.getAttribute("isLoggedIn")) == true) ? "Y" : "N" %>">
                <param name="isAdmin" value="<%= (session.getAttribute("isAdmin") != null && ((Boolean)session.getAttribute("isAdmin")) == true) ? "Y" : "N" %>">
                Applet failed to run. No Java plug-in was found.
            </APPLET>
        </div> 
    </body>
</html>