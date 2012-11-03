package org.aksw.verilinks.games.peaInvasion.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Model of an instance of a semantic web link.
 */
public class rdfInstance implements IsSerializable {

	private String uri;
	
	private String label;
	
	private String type;
	
	private String ontology;
	
	/** Ontology specific **/
	private String optional;

	private String image = null;
	
	private String instanceType;
	private double latitude;
	
	private double longitude;
	
	public rdfInstance(){
		
	}
	public rdfInstance(String uri, String label, String type, String ontology,
			String optional, String image) {
		this.uri = uri;
		this.label = label;
		this.type = type;
		this.ontology = ontology;
		this.optional = optional;
    if( image.length() > 1)
      this.image = image;
    else
      this.image = "none";
	}

	public rdfInstance(String uri, String label, String type, String ontology,
			String optional, String image, String instanceType, double latitude, double longitude) {
		this.uri = uri;
		this.label = label;
		this.type = type;
		this.ontology = ontology;
		this.optional = optional;
    if( image.length() > 1)
      this.image = image;
    else
      this.image = "none";
    this.instanceType = instanceType;
    this.latitude=latitude;
    this.longitude= longitude;
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOptional() {
		return optional;
	}

	public void setOptional(String optional) {
		this.optional = optional;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getOntology() {
		return ontology;
	}

	public void setOntology(String ontology) {
		this.ontology = ontology;
	}
	
	public void setImage(String img){

    this.image = img;
}
	
	public String getImage(){

	    return this.image;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public void setInstanceType(String instanceType) {
		this.instanceType = instanceType;
	}
	public String getInstanceType() {
		return instanceType;
	}
	
}
