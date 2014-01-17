package asw1013;

import java.io.*;
import java.net.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class HTTPClient {

    private URL base = null;
    private String sessionId = null;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setBase(URL base) {
        this.base = base;
    }

    public URL getBase() {
        return base;
    }

    public Document execute(String address, Document data) throws TransformerException, ParserConfigurationException, SAXException, IOException, MalformedURLException {
        ManageXML manageXML = new ManageXML();

        HttpURLConnection connection = (HttpURLConnection) new URL(base.toString()+"/"+address).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        if (sessionId != null) {
            connection.setRequestProperty("Cookie", "JSESSIONID=" + sessionId);
        }
        connection.setRequestProperty("Accept", "text/xml");
        connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        connection.connect();

        OutputStream out = connection.getOutputStream();
        manageXML.transform(out, data);
        out.close();

        InputStream in = connection.getInputStream();
        Document answer = manageXML.parse(in);
        in.close();

        String setCookie = connection.getHeaderField("Set-Cookie");
        if (setCookie != null && !setCookie.equals("") && (setCookie.substring(0, setCookie.indexOf("=")).equals("JSESSIONID"))) {
            sessionId = setCookie.substring(setCookie.indexOf("=") + 1, setCookie.indexOf(";"));
        }
        //System.out.println(connection.getHeaderFields());

        connection.disconnect();
        return answer;
    }

}
