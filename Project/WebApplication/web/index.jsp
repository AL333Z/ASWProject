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
        <div id="container" >
            <div id="wrapper">
                <%@ include file="/WEB-INF/jspf/login.jspf" %>
            </div>
            <div id="wrapper">
                <%@ include file="/WEB-INF/jspf/registration.jspf" %>
            </div>  
        </div>  
        <div id="container">
            <APPLET codebase="applet/" code="asw1013.ListApplet" 
                    archive="Lib1.jar,Applet1.jar" 
                    width=960 height=500>
                <param name="sessionId" value="<%= session.getId()%>">

            </APPLET>
        </div> 
    </body>
</html>