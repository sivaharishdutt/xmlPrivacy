package com.example.DM.controller;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.*;

@Path("xmldata")
public class XmldataController {

    @POST
    @Path("/processing")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response xmlresponse(JsonObject data) throws URISyntaxException {
        System.out.println("controller file");

        String xml_data = String.valueOf(data.get("xml_txt"));
        System.out.println(xml_data);
        xml_data = xml_data.substring(1,xml_data.length()-1);
        HashMap<String, Boolean> uniqueElements = new HashMap<String, Boolean>();
        xml_data = xml_data.replaceAll("\\\\n", "");
        xml_data = xml_data.replaceAll("\\\\", "");

        try {
            File myObj = new File("/home/sidan/Downloads/apache-tomcat-9.0.39/bin/input.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data1 = myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        try {
          //InputSource is = new InputSource(new StringReader(xml_data));
            try(PrintWriter out = new PrintWriter("input.txt")) {
                out.println(xml_data);
        }
            final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse("/home/sidan/Downloads/apache-tomcat-9.0.39/bin/input.txt");
            final XPathExpression xpath = XPathFactory.newInstance().newXPath().compile("//*[count(./*) = 0]");
            final NodeList nodeList = (NodeList) xpath.evaluate(doc, XPathConstants.NODESET);
            for(int i = 0; i < nodeList.getLength(); i++) {
                final Element el = (Element) nodeList.item(i);
                uniqueElements.put(el.getNodeName(), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<String> allElements = new ArrayList<String>(uniqueElements.keySet());

        System.out.println(allElements);
        return Response.ok().entity(allElements).build();
    }

    @POST
    @Path("/createspec")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createSpec(JsonObject data) throws URISyntaxException {
        System.out.println("creating spec file");

        JsonObject obj = (JsonObject) data.get("specs");
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            // root element
            Element root = document.createElement("specificationRoot");
            document.appendChild(root);

            for (String key : obj.keySet()) {
                System.out.println(key);
                System.out.println(obj.get(key));

                Element elem = document.createElement("datasource");
                root.appendChild(elem);

                // xpath element
                Element xpath = document.createElement("xpath");
                xpath.appendChild(document.createTextNode(key));
                elem.appendChild(xpath);

                // technique element
                String s = obj.get(key).toString();
                s=s.substring(1,s.length()-1);
                Element mask = document.createElement("FilterType");
                mask.appendChild(document.createTextNode(s));
                elem.appendChild(mask);
        }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File("spec.xml"));

            transformer.transform(domSource, streamResult);

            System.out.println("Done creating XML File");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }
        catch (TransformerException tfe) {
            tfe.printStackTrace();
        }

        return Response.ok(203).build();
    }
}
