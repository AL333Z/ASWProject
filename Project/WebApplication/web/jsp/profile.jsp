<!DOCTYPE html>

<!DOCTYPE html>
<html lang="en" class="no-js"> <!--<![endif]-->
    <head>
        <meta charset="UTF-8" />
        <title>Registration</title>
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/style-sheets/style.css" />
    </head>
    <body>	
        <%@ include file="/WEB-INF/jspf/navigation.jspf" %>
        
        <div class="container">
            <h1>Tweets posted by <%= request.getParameter("username") %></h1>
            <APPLET codebase="../applet/" code="asw1013.applet.TweetListApplet" 
                    archive="Lib1.jar,Applet1.jar" 
                    width=960 height=500>
                <param name="sessionId" value="<%= session.getId()%>">
                <param name="tweetsOfUsername" value="<%= request.getParameter("username") %>">
                Applet failed to run. No Java plug-in was found.
            </APPLET>
        </div> 
    </body>
</html>