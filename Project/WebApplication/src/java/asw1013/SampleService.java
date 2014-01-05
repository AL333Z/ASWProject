package asw1013;

import java.io.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.w3c.dom.*;

@WebServlet(urlPatterns = {"/sampleservice"})
public class SampleService extends AbstractXmlServiceServlet {
    
    protected void operations(Document data, HttpSession session, HttpServletResponse response, ManageXML mngXML) throws Exception {
                
        //name of operation is message root
        Element root = data.getDocumentElement();
        String operation = root.getTagName();
        switch (operation) {
            case "op1":
                System.out.println("push received");  

                Document answer = mngXML.newDocument();
                answer.appendChild(answer.createElement("ok"));                
                OutputStream os = response.getOutputStream();
                mngXML.transform(os, answer);
                os.close();                
                
                break;
            case "op2":
                System.out.println("pop received");  
                break;
        }
    }

}

