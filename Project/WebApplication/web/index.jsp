<!DOCTYPE html>
<html lang="en" class="no-js"> <!--<![endif]-->
    <head>
        <meta charset="UTF-8" />
        <title>Registration</title>
        <link rel="stylesheet" type="text/css" href="style-sheets/style.css" />
    </head>
    <body>	
        <%@ include file="/WEB-INF/jspf/navigation.jspf" %>
        <%            if (!(session.getAttribute("isLoggedIn") != null
                    && (Boolean) session.getAttribute("isLoggedIn"))) {
        %>
        <div class="container" >
            <div class="wrapper">
                <%@ include file="/WEB-INF/jspf/login.jspf" %>
            </div>
            <div class="wrapper">
                <%@ include file="/WEB-INF/jspf/registration.jspf" %>
            </div>  
        </div>
        <%
        } else {
        %> 
        <div class="container">
            <applet codebase="applet/" code="asw1013.applet.PostTweetApplet"
                    archive="Lib1.jar,Applet1.jar"
                    width=960 height=100>
                <param name="sessionId" value="<%= session.getId()%>">
            </applet>
        </div>
        <%
            }
        %>

        <div class="container">
            <APPLET codebase="applet/" code="asw1013.applet.TweetListApplet" 
                    archive="Lib1.jar,Applet1.jar" 
                    width=960 height=500>
                <param name="sessionId" value="<%= session.getId()%>">

            </APPLET>
        </div> 
    </body>
</html>