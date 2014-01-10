<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6 lt8"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7 lt8"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8 lt8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"> <!--<![endif]-->
    <head>
        <meta charset="UTF-8" />
        <title>Registration</title>
        <link rel="stylesheet" type="text/css" href="style-sheets/style.css" />
    </head>
    <body>	
        <%@ include file="/WEB-INF/jspf/navigation.jspf" %>
        <%
            if (!(session.getAttribute("isLoggedIn") != null
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
            <applet codebase="applet/" code="asw1013.PostTweetApplet"
                    archive="Lib1.jar,Applet1.jar"
                    width=960 height=100>
                <param name="sessionId" value="<%= session.getId()%>">
            </applet>
        </div>
        <%
            }
        %>
        
        <div class="container">
            <APPLET codebase="applet/" code="asw1013.ListApplet" 
                    archive="Lib1.jar,Applet1.jar" 
                    width=960 height=500>
                <param name="sessionId" value="<%= session.getId()%>">

            </APPLET>
        </div> 
    </body>
</html>