package org.aksw.verilinks.server.tools;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.aksw.verilinks.server.msg.Template;
import org.aksw.verilinks.server.msg.TemplateInstance;
import org.aksw.verilinks.server.msg.TemplateLinkset;
import org.aksw.verilinks.server.msg.TemplateProperty;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class XMLTool {

  
  
  private Document document;
  
  private String filePath;
  
  public XMLStreamWriter writer;
  
  public XMLTool(String filePath){
    this.filePath=filePath;
    //readTemplateFile(filePath);
  }
  
  public static String prettyFormat(String input, int indent) {
    try {
        Source xmlInput = new StreamSource(new StringReader(input));
        StringWriter stringWriter = new StringWriter();
        StreamResult xmlOutput = new StreamResult(stringWriter);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute("indent-number", indent);
        Transformer transformer = transformerFactory.newTransformer(); 
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(xmlInput, xmlOutput);
        return xmlOutput.getWriter().toString();
    } catch (Exception e) {
        throw new RuntimeException(e); // simple exception handling, please review it
    }
  }
  
  public static String prettyFormat(String input) {
      return prettyFormat(input, 2);
  }
  
  /**Create original template file, without user-added ontology templates
   * @throws IOException 
   * @throws XMLStreamException */
  public void createTemplateFile() throws XMLStreamException, IOException {
    System.out.println("Create Template File");
    XMLOutputFactory factory = XMLOutputFactory.newInstance();
    
    String uri = "verli:uri";
    String propRdfsLabel = "<http://www.w3.org/2000/01/rdf-schema#label>";
    /** Prop1 */
    String propRdfType = "<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>";
    
     writer =
          factory.createXMLStreamWriter(
              new FileWriter(filePath));
      
      writer.writeStartDocument("UTF-8","1.0");
      writer.writeStartElement("templates");
      
      // Dbpedia - Factbook
      writer.writeStartElement("template");
      writer.writeAttribute("linkset", "dbpedia-factbook");
      writer.writeStartElement("subject");	// subject
      writeOntology("dbpedia");
      writeEndpoint("http://DBpedia.org/sparql");
      writer.writeStartElement("properties"); // properties
      writeProperty(uri, "0", true);
      writePropertyFilter(propRdfsLabel,"1",true,"en");
      writeProperty(propRdfType,"2",true);
      writePropertyFilter("<http://dbpedia.org/ontology/abstract>","3",true,"en"); //filter
      writePropertyLimit("<http://dbpedia.org/ontology/thumbnail>","4",false,"1");
      writer.writeEndElement(); // properties
      writeType(PropertyConstants.TEMPLATE_TYPE_NORMAL);
      writer.writeEndElement();
      writer.writeStartElement("predicate"); // predicate
      writer.writeCharacters("owl:spokenIn");
      writer.writeEndElement();	
      writer.writeStartElement("object");	// object
      writeOntology("factbook");
      writeEndpoint("http://www4.wiwiss.fu-berlin.de/factbook/sparql");
      writer.writeStartElement("properties"); // properties
      writeProperty(uri, "0", true);
      writeProperty(propRdfsLabel,"1",true);
      writeProperty(propRdfType,"2",true);
      writeProperty("<http://www4.wiwiss.fu-berlin.de/factbook/ns#background>","3",true);
//      writePropertyLinkedData("verli:image","4",true);
      writer.writeEndElement(); // properties
      writeType(PropertyConstants.TEMPLATE_TYPE_NORMAL);
      writer.writeEndElement();
      writer.writeEndElement(); // template
      
      // Dbpedia - BBCWildlife
      writer.writeStartElement("template");
      writer.writeAttribute("linkset", "dbpedia-bbcwildlife");
      writer.writeStartElement("subject");	// subject
      writeOntology("dbpedia");
      writeEndpoint("http://DBpedia.org/sparql");
      writer.writeStartElement("properties"); // properties
      writeProperty(uri, "0", true);
      writePropertyFilter(propRdfsLabel,"1",true,"en");
      writeProperty(propRdfType,"2",true);
      writePropertyFilter("<http://dbpedia.org/ontology/abstract>","3",true,"en");
      writePropertyLimit("<http://dbpedia.org/ontology/thumbnail>","4",true,"1");
      writer.writeEndElement(); // properties
      writeType(PropertyConstants.TEMPLATE_TYPE_NORMAL);
      writer.writeEndElement();
      writer.writeStartElement("predicate"); // predicate
      writer.writeCharacters("owl:sameAs");
      writer.writeEndElement();	
      writer.writeStartElement("object");	// object
      writeOntology("bbcwildlife");
      writeEndpoint("http://api.talis.com/stores/bbc-wildlife/services/sparql");
      writer.writeStartElement("properties"); // properties
      writeProperty(uri, "0", true);
      writeProperty(propRdfsLabel,"1",true);
      writeProperty(propRdfType,"2",true);
      writeProperty("<http://purl.org/ontology/wo/adaptation>","3",true);
      writePropertyLinkedData("<http://xmlns.com/foaf/0.1/depicts>","4",true);
      writer.writeEndElement(); // properties
      writeType(PropertyConstants.TEMPLATE_TYPE_NORMAL);
      writer.writeEndElement();
      writer.writeEndElement(); // template
      
      // Dbpedia - LinkedGeoData
      writer.writeStartElement("template");
      writer.writeAttribute("linkset", "dbpedia-linkedgeodata");
      writer.writeStartElement("subject");	// subject
      writeOntology("dbpedia");
      writeEndpoint("http://DBpedia.org/sparql");
      writer.writeStartElement("properties"); // properties
      writeProperty(uri, "0", true);
      writePropertyFilter(propRdfsLabel,"1",true,"en");
      writeProperty(propRdfType,"2",true);
      writePropertyFilter("<http://dbpedia.org/ontology/abstract>","3",true,"en");
      writePropertyLimit("<http://dbpedia.org/ontology/thumbnail>","4",true,"1");
      writer.writeEndElement(); // properties
      writeType(PropertyConstants.TEMPLATE_TYPE_NORMAL);
      writer.writeEndElement();
      writer.writeStartElement("predicate"); // predicate
      writer.writeCharacters("owl:sameAs");
      writer.writeEndElement();	
      writer.writeStartElement("object");	// object
      writeOntology("linkedgeodata");
      writeEndpoint("http://linkedgeodata.org/sparql/");
      writer.writeStartElement("properties"); // properties
      writeProperty(uri, "0", true);
      writeProperty(propRdfsLabel,"1",true);
      writeProperty(propRdfType,"2",true);
      writeProperty("<http://linkedgeodata.org/property/is_in%3Acontinent>","3",false);
      writeProperty("<http://www.w3.org/2003/01/geo/wgs84_pos#lat>","5",true);
      writeProperty("<http://www.w3.org/2003/01/geo/wgs84_pos#long>","6",true);
      //      writePropertyLinkedData("verli:image","4",true);
      writer.writeEndElement(); // properties
      writeType(PropertyConstants.TEMPLATE_TYPE_MAP);
      writer.writeEndElement();
      writer.writeEndElement(); // template
      
      writer.writeEndElement(); // templates
      writer.writeEndDocument();
  
      writer.flush();
      writer.close();

  }

  private void writeOntology(String name) throws XMLStreamException {
    writer.writeStartElement(PropertyConstants.ONTOLOGY);
    writer.writeCharacters(name);
    writer.writeEndElement();
  }
  
  private void writeName(String name) throws XMLStreamException {
    writer.writeStartElement(PropertyConstants.NAME);
    writer.writeCharacters(name);
    writer.writeEndElement();
    
  }

  private void writeEndpoint(String endpoint) throws XMLStreamException {
    writer.writeStartElement(PropertyConstants.ENDPOINT);
    writer.writeCharacters(endpoint);
    writer.writeEndElement();
    
  }

  private void writeProperty(String property, String position, boolean important) throws XMLStreamException {
    writer.writeStartElement("property");
    writer.writeAttribute("position", position);
    // Standart
    String importantProperty =null;
    if (important)
    	importantProperty = "yes";
    else
    	importantProperty = "no";
    writer.writeAttribute(PropertyConstants.ATTR_IMPORTANT, importantProperty);
    writer.writeCharacters(property);
    writer.writeEndElement();
  }
  
  /**
   * With lang filter
   * @param property
   * @param position
   * @param important
   * @param langFilter
   * @throws XMLStreamException
   */
  private void writePropertyFilter(String property, String position, boolean important, String langFilter) throws XMLStreamException {
    writer.writeStartElement("property");
    writer.writeAttribute("position", position);
    // Standart
    String importantProperty =null;
    if (important)
    	importantProperty = "yes";
    else
    	importantProperty = "no";
    writer.writeAttribute(PropertyConstants.ATTR_IMPORTANT, importantProperty);
    writer.writeAttribute(PropertyConstants.ATTR_FILTER, langFilter);
    writer.writeCharacters(property);
    writer.writeEndElement();
    
  }
  
  /**
   * With lang filter
   * @param property
   * @param position
   * @param important
   * @param langFilter
   * @throws XMLStreamException
   */
  private void writePropertyLimit(String property, String position, boolean important, String limit) throws XMLStreamException {
    writer.writeStartElement("property");
    writer.writeAttribute("position", position);
    // Standart
    String importantProperty =null;
    if (important)
    	importantProperty = "yes";
    else
    	importantProperty = "no";
    writer.writeAttribute(PropertyConstants.ATTR_IMPORTANT, importantProperty);
    writer.writeAttribute(PropertyConstants.ATTR_LIMIT, limit);
    writer.writeCharacters(property);
    writer.writeEndElement();
    
  }
  
  private void writePropertyLinkedData(String property, String position, boolean important) throws XMLStreamException {
    writer.writeStartElement("property");
    writer.writeAttribute("position", position);
    // Standart
    String importantProperty =null;
    if (important)
    	importantProperty = "yes";
    else
    	importantProperty = "no";
    writer.writeAttribute(PropertyConstants.ATTR_IMPORTANT, importantProperty);
    writer.writeAttribute(PropertyConstants.ATTR_LINKEDDATA, "true");
    writer.writeCharacters(property);
    writer.writeEndElement();
  }
  
  private void writeProp0(String prop0, String prop0attribute) throws XMLStreamException {
    writer.writeStartElement(PropertyConstants.PROP0);
    writer.writeAttribute("property", prop0attribute);
    // Standart
    writer.writeAttribute(PropertyConstants.ATTR_IMPORTANT, "yes");
    writer.writeCharacters(prop0);
    writer.writeEndElement();
    
  }
  
  private void writeProp0(String prop0, String prop0attribute, String important) throws XMLStreamException {
    writer.writeStartElement(PropertyConstants.PROP0);
    writer.writeAttribute("property", prop0attribute);
    // Standart
    writer.writeAttribute(PropertyConstants.ATTR_IMPORTANT, important);
    writer.writeCharacters(prop0);
    writer.writeEndElement();
    
  }

  private void writeProp1(String prop1, String prop1attribute) throws XMLStreamException {
    writer.writeStartElement(PropertyConstants.PROP1);
    writer.writeAttribute("property", prop1attribute);
    // Standart
    writer.writeAttribute(PropertyConstants.ATTR_IMPORTANT, "yes");
    writer.writeCharacters(prop1);
    writer.writeEndElement();
    
  }

//  private void writeProp2(String prop2, String prop2attribute) throws XMLStreamException {
//    writer.writeStartElement(PropertyConstants.PROP2);
//    if (!prop2attribute.equals(""))
//      writer.writeAttribute("property", prop2attribute);
//    writer.writeCharacters(prop2);
//    writer.writeEndElement();
//    
//  }
  
  /**
   * Write Prop2
   * @param prop2 property
   * @param filter filterValue
   * @param important yes or no
   */
  private void writeProp2(String prop2, String filter, String important) throws XMLStreamException {
    writer.writeStartElement(PropertyConstants.PROP2);
    if (!filter.equals(""))
      writer.writeAttribute(PropertyConstants.ATTR_FILTER, filter);
    if (!important.equals(""))
      writer.writeAttribute(PropertyConstants.ATTR_IMPORTANT, important);
    else
    	writer.writeAttribute(PropertyConstants.ATTR_IMPORTANT, "no");
    
    writer.writeCharacters(prop2);
    writer.writeEndElement();
    
  }

  private void writeImage(String img) throws XMLStreamException {
    writer.writeStartElement(PropertyConstants.IMAGE);
    // Standart
    writer.writeAttribute(PropertyConstants.ATTR_IMPORTANT, "no");
    writer.writeCharacters(img);
    writer.writeEndElement();
    
  }

  // TODO description
  private void writeImage(String img, String linkedData) throws XMLStreamException {
    writer.writeStartElement(PropertyConstants.IMAGE);
    // Standart
    writer.writeAttribute(PropertyConstants.ATTR_IMPORTANT, "yes");
    writer.writeAttribute(PropertyConstants.ATTR_LINKEDDATA, "yes");
    writer.writeCharacters(img);
    writer.writeEndElement();
    
  }
  
  private void writeType(String type) throws XMLStreamException {
    writer.writeStartElement(PropertyConstants.TYPE);
    // Standart
    writer.writeAttribute(PropertyConstants.ATTR_IMPORTANT, "yes");
    writer.writeCharacters(type);
    writer.writeEndElement();
  }
  
  private void writeType(String type, String zoomFactor) throws XMLStreamException {
    writer.writeStartElement(PropertyConstants.TYPE);
    // Standart
    writer.writeAttribute(PropertyConstants.ATTR_IMPORTANT, "yes");
    writer.writeAttribute(PropertyConstants.ATTR_ZOOM, zoomFactor);
    writer.writeCharacters(type);
    writer.writeEndElement();
  }
//  //TODO: difficulty und zoom in adminPnael
//  private void writeDifficulty(String difficulty) throws XMLStreamException {
//    writer.writeStartElement(PropertyConstants.DIFFICULTY);
//    // Standart
//    writer.writeAttribute(PropertyConstants.ATTR_IMPORTANT, "yes");
//    writer.writeCharacters(difficulty);
//    writer.writeEndElement();
//  }
  
  public void readTemplateFile() throws ParserConfigurationException, SAXException, IOException{
    System.out.println("##XML Tool: Read template file...");
    System.out.println("FilePath: "+filePath);
    this.filePath = filePath;
    
    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder;
    
    docBuilder = docFactory.newDocumentBuilder();
    this.document = docBuilder.parse(filePath);
    //Document doc = docBuilder.parse("d://Test.xml");
    echo("Read Done##");
   
    
  }
  
  public void addNewTemplate() {
    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder;
    try {
      docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.parse("d://Templates.xml");
      //Document doc = docBuilder.parse("d://Test.xml");
      
      doc.getDocumentElement().normalize();
      System.out.println("Root element " + doc.getDocumentElement().getNodeName());
      NodeList ontologyList = doc.getElementsByTagName("ontology");
      
      //Ontology
      for (int i =0; i<ontologyList.getLength();i++) {
        Node ontology = ontologyList.item(i);
        System.out.println("#Name# "+ontology.getFirstChild().getTextContent()); //Name
        
        //Apend new child
        Node newNode = doc.createElement("newNode");
        newNode.setTextContent("new Text");
        ontology.appendChild(newNode);
        
        //Child elements
        NodeList childElements = ontology.getChildNodes();
        for (int j=0; j<childElements.getLength();j++) {
          //childElements.item(j).setTextContent("Boyya"); //Change text
        }
        
        if (ontology.getNodeName() == "dbpedia")
          System.out.println("\n\ndbpedia");
        
        System.out.print(ontology.getChildNodes().item(1).getNodeName());
        System.out.println(": "+ontology.getChildNodes().item(1).getTextContent());
        
      }
      
      Node templateChild = doc.getFirstChild();
      Element newElement = doc.createElement("chicken");
      newElement.setAttribute("Boi", "George");
      templateChild.appendChild(newElement);
      NamedNodeMap childAttributes = templateChild.getAttributes();
      Attr newProp = doc.createAttribute("boyya");
      newProp.setValue("milky way");
      childAttributes.setNamedItem(newProp);
      
      TransformerFactory tranFactory = TransformerFactory.newInstance(); 
      Transformer aTransformer = tranFactory.newTransformer(); 

      Source src = new DOMSource(doc); 
      StreamResult dest = new StreamResult(new File("d://TemplatesNew.xml")); 
      //Result dest2 = new StreamResult(System.out); 
      aTransformer.transform(src, dest);
      
      System.out.println("\nPARENT: "+ontologyList.getLength());
      
      
      
    } catch (ParserConfigurationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (SAXException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }  catch (TransformerException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } 
    
    
  }
  
  
  public void addOntology(Template ontology) {
    System.out.println("##XML Tool: Add new ontology to template...");
    
    echo("output Template:");
    echo(ontology.getName());
    echo(ontology.getEndpoint());
    echo(ontology.getProp0());
    echo(ontology.getProp1());
    echo(ontology.getProp2());
    echo(ontology.getProp3());
    echo(ontology.getImage());
    echo(ontology.getType());
    
    
    this.document.getDocumentElement().normalize();
    System.out.println("Root element-> " + document.getDocumentElement().getNodeName()+", FilePath-> "+filePath);
    NodeList ontologyList = document.getElementsByTagName("ontology");
 
    // Check if ontology is already in templates file
    System.out.println("Searching for ontology '"+ontology.getName()+"'");
    String buffer="";
    boolean foundOntology =false;
    // Ontology
    for (int i =0; i<ontologyList.getLength();i++) {
      Node ontologyNode = ontologyList.item(i);
//      System.out.println("ONTO: "+ontologyNode.getFirstChild().getTextContent());
      if (ontologyNode.getFirstChild().getTextContent().equals(ontology.getName())) {
        foundOntology=true;
        break;
      }
    }
    if (foundOntology) {
      System.out.println("Adding Template "+ontology.getName()+" failed. Already exists in template file!");
      System.out.println("##XML Tool: Adding Template Failed\n");
      return;
    }
    else
    	System.out.println("Template not found. Continue adding template.");
    Node ontologyNode = document.getFirstChild().appendChild(document.createElement("ontology"));
    echo("Get ontologyNode 'ontology': "+ontologyNode.toString());
    // Add ontology-template to template file
    Node nameNode = ontologyNode.appendChild(document.createElement(PropertyConstants.NAME));
    nameNode.setTextContent(ontology.getName()); // Name
    echo("Set NameNode");
    Node endpointNode = ontologyNode.appendChild(document.createElement(PropertyConstants.ENDPOINT));
    endpointNode.setTextContent(ontology.getEndpoint()); //Endpoint
    echo("Set epNode");
//    if (!ontology.getProp0().isEmpty()) 
    {
      Node prop0Node = ontologyNode.appendChild(document.createElement(PropertyConstants.PROP0));
      prop0Node.setTextContent(ontology.getProp0());
      echo("Set prop0Node");
    }
//    if (!ontology.getProp1().isEmpty()) 
    {
      Node prop1Node = ontologyNode.appendChild(document.createElement(PropertyConstants.PROP1));
      prop1Node.setTextContent(ontology.getProp1());
      echo("Set prop1Node");
    }
//    if (!ontology.getProp2().isEmpty()) 
    {
      Node prop2Node = ontologyNode.appendChild(document.createElement(PropertyConstants.PROP2));
      prop2Node.setTextContent(ontology.getProp2());
      echo("Set prop2Node");
    }
//    if (!ontology.getProp3().isEmpty()) 
    {
      Node prop3Node = ontologyNode.appendChild(document.createElement(PropertyConstants.PROP3));
      prop3Node.setTextContent(ontology.getProp3());
      echo("Set prop3Node");
    }
//    if (!ontology.getImage().isEmpty()) 
    {
      Node imageNode = ontologyNode.appendChild(document.createElement(PropertyConstants.IMAGE));
      imageNode.setTextContent(ontology.getImage());
      echo("Set imageNode");
    }
    Node typeNode = ontologyNode.appendChild(document.createElement(PropertyConstants.TYPE));
    typeNode.setTextContent(ontology.getType());
    echo("Set typeNode");
    
    System.out.println("Ontology '"+ontology.getName()+"' added to template");
    
    try {
    	echo("Transform");
      TransformerFactory tranFactory = TransformerFactory.newInstance(); 
      Transformer aTransformer = tranFactory.newTransformer(); 

      Source src = new DOMSource(document); 
      StreamResult dest = new StreamResult(new File(filePath)); 
      aTransformer.transform(src, dest);
      
    }  catch (TransformerException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      echo("ERROR");
    } 
    
    System.out.println("##XML Tool: Adding Template Done\n");

    
  }
  
  public void addPropertyToOntology(String tagName, String tagValue, String ontology) {
    System.out.println("##Add new property to an existing ontology of the template...");
    this.document.getDocumentElement().normalize();
    System.out.println("Root element: " + document.getDocumentElement().getNodeName());
    NodeList ontologyList = document.getElementsByTagName("ontology");
    // Ontology
    for (int i =0; i<ontologyList.getLength();i++) {
      Node ontologyNode = ontologyList.item(i);
      if (ontologyNode.getFirstChild().getTextContent().equals(ontology)) {
        // Apend new child
        Node newNode = document.createElement(tagName);
        newNode.setTextContent(tagValue);
        ontologyNode.appendChild(newNode);
        System.out.println("Tag '"+tagName+"' added to ontology '"+ontology+"'.");
        break;
      }
      System.out.println("#Name# "+ontologyNode.getFirstChild().getTextContent()); //Name
    }
    
    try {
      TransformerFactory tranFactory = TransformerFactory.newInstance(); 
      Transformer aTransformer = tranFactory.newTransformer(); 

      Source src = new DOMSource(document); 
      StreamResult dest = new StreamResult(new File(filePath)); 
      aTransformer.transform(src, dest);
      
    }  catch (TransformerException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } 
    
    System.out.println("##Done");
  }
  
  public String getAttribute(String ontology, String property, String attribute){
  	echo("##Find attribute '"+attribute+"' of property '"+property+"'");
    this.document.getDocumentElement().normalize();
//    echo("Root element: " + document.getDocumentElement().getNodeName());
    NodeList ontologyList = document.getElementsByTagName("ontology");
//    echo("Got ontologyList");
    // Error handling
    String msg = "";
    boolean foundOntology = false;
    boolean foundProperty = false;
    
    // Ontology
    for (int i =0; i<ontologyList.getLength();i++) {
      Node ontologyNode = ontologyList.item(i);
//      echo(ontologyNode.getFirstChild().getTextContent());
      if (ontologyNode.getFirstChild().getTextContent().equals(ontology)) {
        foundOntology=true;
//        echo("FOUND ontology "+ontologyNode.getFirstChild().getTextContent());
        // Property
        NodeList propertyList = ontologyNode.getChildNodes();
        for(int j=0; j<propertyList.getLength();j++) {
          Node propertyNode = propertyList.item(j);
//          System.out.println("property "+propertyNode.getNodeName());
          if (propertyNode.getNodeName().equals(property)) {
//          	echo("FOUND property " +propertyNode.getTextContent());
            foundProperty = true;
            NamedNodeMap attributeList = propertyNode.getAttributes();
            for(int k=0; k<attributeList.getLength();k++){
            	Node att = attributeList.item(k);
            	echo(att.getNodeName());
            	if(att.getNodeName().equals(attribute)){
            		 echo("Found attribute " +att);
            		return att.getTextContent();
            	}
            }
           
          }
        }
        return ""; 
      }
    }
    return ""; 
  }
  
  public void modifyPropertyValueOfOntology(String property, String newValue, String ontology) {
    System.out.println("##Modify value of an existing property...");
    this.document.getDocumentElement().normalize();
    System.out.println("Root element: " + document.getDocumentElement().getNodeName());
    NodeList ontologyList = document.getElementsByTagName("ontology");
    
    // Error handling
    String msg = "";
    boolean foundOntology = false;
    boolean foundProperty = false;
    
    // Ontology
    for (int i =0; i<ontologyList.getLength();i++) {
      Node ontologyNode = ontologyList.item(i);
      if (ontologyNode.getFirstChild().getTextContent().equals(ontology)) {
        foundOntology=true;
        //msg = "FOUND ontology "+ontologyNode.getFirstChild().getTextContent();
        // Property
        NodeList propertyList = ontologyNode.getChildNodes();
        for(int j=0; j<propertyList.getLength();j++) {
          Node propertyNode = propertyList.item(j);
          //System.out.println("property "+propertyNode.getNodeName());
          if (propertyNode.getNodeName().equals(property)) {
            foundProperty = true;
            propertyNode.setTextContent(newValue);
            //msg= "FOUND property" +propertyNode.getTextContent();
            break;
          }
        }
        break;
      }
    }
    
    try {
      TransformerFactory tranFactory = TransformerFactory.newInstance(); 
      Transformer aTransformer = tranFactory.newTransformer(); 

      Source src = new DOMSource(document); 
      StreamResult dest = new StreamResult(new File(filePath)); 
      aTransformer.transform(src, dest);
      
    }  catch (TransformerException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } 
    if (foundOntology==true && foundProperty==true)
      msg="SUCCESS: Value of Property '"+property+"' of Ontology '"+ontology +
      "' was changed to '"+newValue+"'.";
    else if (foundOntology==true && foundProperty==false)
      msg="ERROR: Property '"+property+"' doesn't exist.";
    else if (foundOntology==false)
      msg="ERROR: Ontology '"+ontology+"' doesn't exist.";
    
    System.out.println(msg); //Name
    System.out.println("##Done");
  
    
  }
  
  public Template getTemplate(String ontology) {
    System.out.println("##Get template...");
  
    Template template = new Template();

    this.document.getDocumentElement().normalize();
    System.out.println("Root element: " + document.getDocumentElement().getNodeName());
    NodeList ontologyList = document.getElementsByTagName("ontology");
    System.out.println("Searching for ontology '"+ontology+"'");
    String buffer="";
    boolean foundOntology =false;
    // Ontology
    for (int i =0; i<ontologyList.getLength();i++) {
      Node ontologyNode = ontologyList.item(i);
      //System.out.println("ONTO: "+ontologyNode.getFirstChild().getTextContent());
      if (ontologyNode.getFirstChild().getTextContent().equals(ontology)) {
        foundOntology=true;
        //msg = "FOUND ontology "+ontologyNode.getFirstChild().getTextContent();
        // Property
        NodeList propertyList = ontologyNode.getChildNodes();
        for(int j=0; j<propertyList.getLength();j++) {
          buffer =propertyList.item(j).getNodeName();
          //System.out.println("Node Name: "+buffer);
          if (buffer.equals(PropertyConstants.NAME)){
            template.setName(propertyList.item(j).getTextContent());
          }else if (buffer.equals(PropertyConstants.ENDPOINT)){
            template.setEndpoint(propertyList.item(j).getTextContent());
          }else if (buffer.equals(PropertyConstants.PROP0)){
            template.setProp0(propertyList.item(j).getTextContent());
          }else if (buffer.equals(PropertyConstants.PROP1)){
            template.setProp1(propertyList.item(j).getTextContent());
          }else if (buffer.equals(PropertyConstants.PROP2)){
            template.setProp2(propertyList.item(j).getTextContent());
          }else if (buffer.equals(PropertyConstants.PROP3)){
            template.setProp3(propertyList.item(j).getTextContent());
          }else if (buffer.equals(PropertyConstants.IMAGE)){
            template.setImage(propertyList.item(j).getTextContent());
          }else if (buffer.equals(PropertyConstants.TYPE)){
            template.setType(propertyList.item(j).getTextContent());
          }            
        }  
      }
    }
    if(template.getType().equals(PropertyConstants.TEMPLATE_TYPE_MAP)){
	    String zoom = getAttribute(ontology,PropertyConstants.TYPE,PropertyConstants.ATTR_ZOOM);
	    if(!zoom.isEmpty())
	    	template.setZoom(zoom);
    }
    if (foundOntology==false){
      System.out.println("ERROR: Ontology '"+ontology+"' doesn't exist.");
    }
    else {
      System.out.println("SUCCESS: Template '"+ontology+"' was generated succesfully.");
      ///*
      System.out.println(template.getName());
      System.out.println(template.getEndpoint());
      System.out.println(template.getProp0());
      System.out.println(template.getProp1());
      System.out.println(template.getProp2());
      System.out.println(template.getProp3());
      System.out.println(template.getImage());
      System.out.println(template.getType());
      System.out.println(template.getZoom());
      //*/
      System.out.println("##Done");
    }
    
    return template;
    
    
  }
  
  
  public void readDOM() {
    
    
    
  }
  
  public void read (){
    XMLInputFactory factory = XMLInputFactory.newInstance();

    try {

      XMLStreamReader streamReader =
          factory.createXMLStreamReader(
              new FileReader("d:\\Templates.xml"));
      while(streamReader.hasNext()){
        streamReader.next();
        if(streamReader.getEventType() == XMLStreamReader.START_ELEMENT){
            System.out.println(streamReader.getLocalName());
            System.out.println("\t"+streamReader.getAttributeCount());
            if (streamReader.getLocalName()!="template")
              System.out.println(streamReader.getAttributeValue(1));
        }
      }
      
      
      factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);
    } catch (XMLStreamException e) {
      e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
  }
  
  public Document getDocument() {
    return this.document;
  }
  
  
  
  public static void main(String args[]) {
    XMLTool x = new XMLTool("src/main/resources/net/saim/game/resources/LinkTemplates.xml");
    try {
      x.readTemplateFile();

    } catch (ParserConfigurationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (SAXException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
//
    try {
			x.createTemplateFile();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
		x.getLinkset("dbpedia-linkedgeodata");
    //x.addNewTemplate();
    //x.addPropertyToOntology("Tim", "ist cool", "factbook");
//    Template array = new Template();
//    array.setName("name23");
//    array.setEndpoint("endp2");
//    array.setProp0("prop0");
//    array.setProp1("prop1");
//    array.setProp2("");
//    //x.modifyPropertyValueOfOntology(PropertyConstants.PROP3, "Boi", "factbook");
//    x.addOntology(array);
    //x.readDOM();
//    x.getTemplate("factbook");
//    x.getTemplate("climb");
////    String attr = x.getAttribute("dbpedia", PropertyConstants.PROP2, "important");
////    String filter = x.getAttribute("dbpedia", PropertyConstants.PROP2, "filter");
//    String zoom = x.getAttribute("climb",PropertyConstants.TYPE, PropertyConstants.ATTR_ZOOM);
////    echo("ATTRIBUT FINAL: "+attr);
////    echo("FILTER FINAL: "+filter);
//    echo("ZOOM FINAL: "+zoom);
  }
  
  private static void echo(String s){
  	System.out.println("##XMLTool: "+s);
  	
  }

	public TemplateLinkset getLinkset(String linkset) {
		 System.out.println("##Get linkset: "+linkset);
		  
	    TemplateLinkset template = new TemplateLinkset();
	    TemplateInstance subject=null;
	    TemplateInstance object=null;
	    String predicate=null;
	    
	    this.document.getDocumentElement().normalize();
	    System.out.println("Root element: " + document.getDocumentElement().getNodeName());
	    NodeList linksetList = document.getElementsByTagName("template");
	    System.out.println("Searching for linkset '"+linkset+"'");
	    String buffer="";
	    boolean foundOntology =false;
	    // Ontology
	    for (int i =0; i<linksetList.getLength();i++) {
	      Node linksetNode = linksetList.item(i);
	      String linksetBuff = linksetNode.getAttributes().item(0).getTextContent();
	      System.out.println(i+".linkset: "+linksetBuff);
	      if (linksetBuff.equals(linkset)) {
	        foundOntology=true;
	        System.out.println("found linkset: "+linkset);
	        // Tripel
	        NodeList linkList = linksetNode.getChildNodes();
	        for(int j=0; j<linkList.getLength();j++) {
	          buffer =linkList.item(j).getNodeName();
//	          System.out.println("Node Name: "+buffer);
	          if (buffer.equals("subject")){
	          	echo(">>Subject");
	            subject = getTemplateInstance(linkList.item(j));
	          }else if (buffer.equals("predicate")){
	            predicate = linkList.item(j).getTextContent();
	            echo(">>Predicate "+predicate);
	          }else if (buffer.equals("object")){
	          	echo(">>Object");
	          	object = getTemplateInstance(linkList.item(j));
	          }          
	        }
	        template.setSubject(subject);
	        template.setObject(object);
	        template.setPredicate(predicate);
	        template.setLinkset(linkset);
	        
	        break;
	      }
	    }
//	    if(template.getType().equals(PropertyConstants.TEMPLATE_TYPE_MAP)){
//		    String zoom = getAttribute(ontology,PropertyConstants.TYPE,PropertyConstants.ATTR_ZOOM);
//		    if(!zoom.isEmpty())
//		    	template.setZoom(zoom);
//	    }
	    if (foundOntology==false){
	      System.out.println("ERROR: linkset '"+linkset+"' doesn't exist.");
	    }
	    else {
	      System.out.println("SUCCESS: Template '"+linkset+"' was generated succesfully.");
	      /*
	      System.out.println(template.getName());
	      System.out.println(template.getEndpoint());
	      System.out.println(template.getProp0());
	      System.out.println(template.getProp1());
	      System.out.println(template.getProp2());
	      System.out.println(template.getProp3());
	      System.out.println(template.getImage());
	      System.out.println(template.getType());
	      System.out.println(template.getZoom());
	      */
	      System.out.println("##Done");
	    }
	    
	    return template;
	    
		
	}

	private TemplateInstance getTemplateInstance(Node item) {
		TemplateInstance tmp = new TemplateInstance();
		String ontology = null;
		
		Node instance=item;
		NodeList childrenList = instance.getChildNodes();
		String child=null;
		for(int i =0; i<childrenList.getLength();i++){
			echo(i+".children: "+childrenList.item(i).getNodeName());
			child = childrenList.item(i).getNodeName();
			if(child.equals("ontology")){
				ontology=childrenList.item(i).getTextContent();
				tmp.setOntology(ontology);
				echo(">>"+ontology);
			}else if(child.equals("endpoint")){
				tmp.setEndpoint(childrenList.item(i).getTextContent());
			}else if (child.equals("type")){
				tmp.setType(childrenList.item(i).getTextContent());
			}else if(child.equals("properties"))
				tmp.setProperties(parseProperties(childrenList.item(i).getChildNodes()));
		}		
		return tmp;
	}
	
	private List<TemplateProperty> parseProperties(NodeList list) {
		echo("Parse properties:");
		List<TemplateProperty> properties = new ArrayList<TemplateProperty>();
		String property = null;
		for(int i=0;i<list.getLength();i++){
			property=list.item(i).getTextContent();
			echo(i+".property: "+property);
			TemplateProperty prop = new TemplateProperty();
			prop.setProperty(property);
//			echo("important: "+list.item(i).getAttributes().getNamedItem("important").getTextContent());
//			echo("position: "+list.item(i).getAttributes().getNamedItem("position").getTextContent());
			prop.setImportant(list.item(i).getAttributes().getNamedItem("important").getTextContent());
			prop.setPosition(list.item(i).getAttributes().getNamedItem("position").getTextContent());
			if(list.item(i).getAttributes().getNamedItem("filter")!=null)
				prop.setFilter(list.item(i).getAttributes().getNamedItem("filter").getTextContent());
			if(list.item(i).getAttributes().getNamedItem("linkedData")!=null)
				prop.setLinkedData(list.item(i).getAttributes().getNamedItem("linkedData").getTextContent());
			if(list.item(i).getAttributes().getNamedItem("limit")!=null)
				prop.setLimit(list.item(i).getAttributes().getNamedItem("limit").getTextContent());
			
			properties.add(prop);
		}
		echo("Number of Properties: "+properties.size());
		return properties;	
		
	}
}
