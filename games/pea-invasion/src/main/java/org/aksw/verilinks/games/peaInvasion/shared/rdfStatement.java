package org.aksw.verilinks.games.peaInvasion.shared;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.IsSerializable;

public class rdfStatement implements IsSerializable{
//	/**
//	  * HashMap that will always contain strings for both keys and values
//	  * @gwt.typeArgs <java.lang.String, java.lang.String>
//	  */
//	  private HashMap<String, String> template;
	  private int id;
	  
	  /**
	   * @gwt.typeArgs <net.saim.game.shared.rdfInstance>
	   */
	  private rdfInstance subject;
	  private String predicate;
	  /**
	   * @gwt.typeArgs <net.saim.game.shared.rdfInstance>
	   */
	  private rdfInstance object;
	  private String linkSpecification;
	  private int counter;
	  private int notSure;
	  

		private String uriSubject;
	  private String uriObject;
	  
	  private Bonus bonus;
	  private double difficulty;
	  
	  private boolean first=false;
	  public rdfStatement(){
//	    template= new HashMap<String, String>();
//	    template.put(PropertyConstants.NAME,"");
//	    template.put(PropertyConstants.ENDPOINT,"");
//	    template.put(PropertyConstants.PROP0,"");
//	    template.put(PropertyConstants.PROP1,"");
//	    template.put(PropertyConstants.PROP2,"");
//	    template.put(PropertyConstants.PROP3,"");
//	    template.put(PropertyConstants.IMAGE,"");
//	    template.put(PropertyConstants.TYPE,"");
//	    template.put(PropertyConstants.LATITUDE,"");
//	    template.put(PropertyConstants.LONGITUDE,"");
	  	
	  }
	  
	  /**
	   * Server
	   * @param id
	   * @param subject
	   * @param predicate
	   * @param object
	   * @param linkSpecification
	   * @param counter
	   */
	  public rdfStatement(int id, String uriSubject, String predicate, 
	      String uriObject, String linkSpecification, int counter,int notSure){
	  	this.id = id;
	    this.uriSubject = uriSubject;
	    this.predicate = predicate;
	    this.uriObject = uriObject;
	    this.linkSpecification = linkSpecification;
	    this.counter = counter;
	    this.notSure=notSure;
	    this.difficulty=0;
	  }
	  
	  public rdfStatement(int id, rdfInstance instanceSubject, String predicate, 
      rdfInstance instanceObject, String linkSpecification, int counter, int notSure){
	  	 this.id = id;
	     this.subject = instanceSubject;
	     this.uriSubject= subject.getUri();
	     this.predicate = predicate;
	     this.object = instanceObject;
	     this.uriObject= object.getUri();
	     this.linkSpecification = linkSpecification;
	     this.counter = counter;
	     this.notSure=notSure;
	     this.difficulty=0;
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

		public String getUriSubject() {
			return uriSubject;
		}

		public void setUriSubject(String uriSubject) {
			this.uriSubject = uriSubject;
		}

		public String getUriObject() {
			return uriObject;
		}

		public void setUriObject(String uriObject) {
			this.uriObject = uriObject;
		}

		public void incCounter() {
			this.counter++;
			
		}

		public void setBonus(Bonus bonus) {
			this.bonus = bonus;
		}

		public Bonus getBonus() {
			return bonus;
		}

	  public int getNotSure() {
			return notSure;
		}

		public void incNotSure() {
			notSure++;
		}

		public double getDifficulty() {
			return difficulty;
		}

		public void setDifficulty(double difficulty) {
			this.difficulty = difficulty;
		}
		
//		public void setFirst(boolean b){
//			this.first=b;
//		}
//		public boolean getFirst(){
//			return this.first;
//		}
	  
}
