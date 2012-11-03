package org.aksw.verilinks.games.peaInvasion.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Model of a semantic web link.
 * Hold information of the triples and their properties and uris.
 */
public class rdfLink implements IsSerializable{

  private int id;
  private rdfInstance subject;
  private String predicate;
  private rdfInstance object;
  private String linkSpecification;
  private int counter;
  
  private String uriSubject;
  private String uriObject;
  
  private String[] array= new String[16];
  
  /**
   * ServiceImpl linksMap method. LinksMap only needs URIs.
   * Property values are saved in instanceMap.
   * @param id
   * @param subject
   * @param predicate
   * @param object
   * @param linkSpecification
   * @param counter
   */
  public rdfLink(int id, String subject, String predicate, 
      String object, String linkSpecification, int counter){
    this.id = id;
    this.uriSubject = subject;
    this.predicate = predicate;
    this.uriObject = object;
    this.linkSpecification = linkSpecification;
    this.counter = counter;
    
    this.array[0]=(new Integer(id)).toString();
    
    
    this.array[13]=predicate;
    
    this.array[14]=linkSpecification;
    this.array[15]=(new Integer(counter)).toString();
    
    
  }
  
  public rdfLink(int id, rdfInstance subject, String predicate, 
      rdfInstance object, String linkSpecification, int counter){
    this.id = id;
    this.subject = subject;
    this.predicate = predicate;
    this.object = object;
    this.linkSpecification = linkSpecification;
    this.counter = counter;
    
    this.array[0]=(new Integer(id)).toString();
    
    this.array[1]=subject.getUri();
    this.array[2]=subject.getLabel();
    this.array[3]=subject.getType();
    this.array[4]=subject.getOntology();
    this.array[5]=subject.getOptional();
    this.array[6]=subject.getImage();
    
    this.array[7]=object.getUri();
    this.array[8]=object.getLabel();
    this.array[9]=object.getType();
    this.array[10]=object.getOntology();
    this.array[11]=object.getOptional();
    this.array[12]=object.getImage();
    
    this.array[13]=predicate;
    
    this.array[14]=linkSpecification;
    this.array[15]=(new Integer(counter)).toString();
   
  }
  
	public rdfLink(String[] stmt) {
	  this.id = Integer.parseInt(stmt[0]);
    this.subject = new rdfInstance(stmt[1],stmt[2],stmt[3],stmt[4],stmt[5],stmt[6]);
    
    this.object = new rdfInstance(stmt[7],stmt[8],stmt[9],stmt[10],stmt[11],stmt[12]);
    this.predicate = stmt[13];
    this.linkSpecification = stmt[14];
    this.counter = Integer.parseInt(stmt[15]);
  }
  /**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public rdfInstance getSubject() {
    return subject;
  }
  public void setSubject(rdfInstance subject) {
    
    
    this.subject = subject;
    
   
    this.array[1]=subject.getUri();
    this.array[2]=subject.getLabel();
    this.array[3]=subject.getType();
    this.array[4]=subject.getOntology();
    this.array[5]=subject.getOptional();
    this.array[6]=subject.getImage();
    
   
    
    
  }
  public String getPredicate() {
    return predicate;
  }
  public void setPredicate(String predicate) {
    this.predicate = predicate;
  }
  public rdfInstance getObject() {
    return object;
  }
  public void setObject(rdfInstance object) {
    this.object = object;
   
    this.array[7]=object.getUri();
    this.array[8]=object.getLabel();
    this.array[9]=object.getType();
    this.array[10]=object.getOntology();
    this.array[11]=object.getOptional();
    this.array[12]=object.getImage();
    
    
    
  }
  public String getLinkSpecification() {
    return linkSpecification;
  }
  public void setLinkSpecification(String linkSpecification) {
    this.linkSpecification = linkSpecification;
  }
  public int getCounter() {
    return counter;
  }
  public void setCounter(int counter) {
    this.counter = counter;
  }

  public String[] getArray(){
    return this.array;
  }

  public String getUriSubject() {
    return uriSubject;
  }

  public String getUriObject() {
    return uriObject;
  }
  
  public void incCounter()  {
    this.counter++;
  }
}
