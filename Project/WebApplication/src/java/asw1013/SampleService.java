package asw1013;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.w3c.dom.*;

@WebServlet(urlPatterns = {"/sampleservice"})
public class SampleService extends HttpServlet {
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {        
        
        InputStream is = request.getInputStream();
        response.setContentType("text/xml;charset=UTF-8");
        
        try {
        ManageXML mngXML = new ManageXML();
        Document data = mngXML.parse(is);
        is.close();
        
        operations(data,request,response,mngXML);

        }
        catch (Exception ex){ 
            System.out.println(ex);
        }
    }
    
    private void operations(Document data, HttpServletRequest request, HttpServletResponse response, ManageXML mngXML) throws Exception {
                
        HttpSession session = request.getSession();
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

