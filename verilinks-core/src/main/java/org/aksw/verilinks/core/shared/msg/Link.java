package org.aksw.verilinks.core.shared.msg;


public class Link {
  private int id;
  private String subjectUri;
  private String objectUri;
  private Instance subject;
  private Instance object;
  private String predicate;
  private double confidence;
  private int counter;
  private double difficulty;
 
  public Link(){
  	this.id=0;
  	this.subject=null;
  	this.object=null;
  	this.predicate=null;
  	this.confidence=0;
  	this.counter=0;
  }
  
  public Link(int id, Instance subject, Instance object, String predicate, 
  		double confidence, int counter){
  	this.id=id;
  	this.subject=subject;
  	this.object=object;
  	this.predicate=predicate;
  	this.confidence=confidence;
  	this.counter=counter;
  }

	public Link(int id, String subjectUri, String predicate, String objectUri,
			String linkset, int counter, int notSure) {
		this.subjectUri=subjectUri;
		this.objectUri=objectUri;
		this.predicate=predicate;
		this.counter=counter;
		
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Instance getSubject() {
		return subject;
	}

	public void setSubject(Instance subject) {
		this.subject = subject;
	}

	public Instance getObject() {
		return object;
	}

	public void setObject(Instance object) {
		this.object = object;
	}

	public String getPredicate() {
		return predicate;
	}

	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	public double getConfidence() {
		return confidence;
	}

	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public void setDifficulty(double difficulty) {
		this.difficulty = difficulty;
	}

	public double getDifficulty() {
		return difficulty;
	}

	public String getSubjectUri() {
		return subjectUri;
	}

	public void setSubjectUri(String subjectUri) {
		this.subjectUri = subjectUri;
	}

	public String getObjectUri() {
		return objectUri;
	}

	public void setObjectUri(String objectUri) {
		this.objectUri = objectUri;
	}
  
  
}
