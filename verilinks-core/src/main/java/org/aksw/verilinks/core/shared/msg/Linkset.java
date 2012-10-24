package org.aksw.verilinks.core.shared.msg;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Linkset implements IsSerializable{

	private String id;
	private String subject;
	private String object;
	private String predicate;
	private String description;
	private String difficulty;
	/** Should be shown to kongregate user?*/
	private boolean kongregate;
	
	public Linkset(){	}
	
	public Linkset(String id, String subject, String object, String predicate, 
			String description, String difficulty){
		this.id=id;
		this.subject=subject;
		this.predicate=predicate;
		this.object=object;
		this.description=description;
		this.setDifficulty(difficulty);
		kongregate = false;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getPredicate() {
		return predicate;
	}

	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		
		return subject+"-"+object;
	}

	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public String getId() {
		return id;
	}

	public void setId(String string) {
		this.id = string;
	}

	public boolean isKongregate() {
		return kongregate;
	}

	public void setKongregate(boolean kongregate) {
		this.kongregate = kongregate;
	}
	
}
