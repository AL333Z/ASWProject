package asw1013;

import asw1013.entity.TweetList;
import asw1013.entity.UserList;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;


public class XmlSchemaGenerator {
    
    public static void main(String[] args){
        try {
            writeXmlSchema(TweetList.class);
            writeXmlSchema(UserList.class);
        } catch (JAXBException ex) {
            Logger.getLogger(XmlSchemaGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XmlSchemaGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void writeXmlSchema(Class xmlRoot) throws JAXBException, IOException {
        JAXBContext context = JAXBContext.newInstance(xmlRoot);
        context.generateSchema(new SchemaOutputResolver() {
            @Override
            public Result createOutput(String namespaceUri, String suggestedFileName) throws IOException {
                return new StreamResult(new File("/Users/mattia",suggestedFileName));
            }
        });
    }
    
}
