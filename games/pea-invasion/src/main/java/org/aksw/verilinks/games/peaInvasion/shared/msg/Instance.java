package org.aksw.verilinks.games.peaInvasion.shared.msg;

import java.util.HashMap;
import java.util.List;

public class Instance {
	
	private String uri;
//	private String ontology;
//	private List<Property> properties;
	private List<Property> properties;
	private List<String> propertyNames;
	private String type;
		
	public Instance(){}
	
	public Instance(String uri, List<Property> properties){
		this.uri=uri;
		this.properties = properties;
		this.setPropertyNames(propertyNames);
	}
	
	public Instance(String uri, List<Property> properties, List<String> propertyNames){
		this.uri=uri;
//		this.ontology=ontology;
		this.properties = properties;
		this.setPropertyNames(propertyNames);
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

//	public String getOntology() {
//		return ontology;
//	}
//
//	public void setOntology(String ontology) {
//		this.ontology = ontology;
//	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	public void setPropertyNames(List<String> propertyNames) {
		this.propertyNames = propertyNames;
	}

	public List<String> getPropertyNames() {
		return propertyNames;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public String getValue(String property){
		for(int i=0;i<this.properties.size();i++){
			if(this.properties.get(i).getProperty().equals(property)){
				return this.properties.get(i).getValue();
			}
		}
		return null;
	}

	public String getImage(){
		String imgDbpedia = "<http://dbpedia.org/ontology/thumbnail>";
//		String imgFactbook = "<http://dbpedia.org/ontology/thumbnail>";
		
		for(int i=0;i<this.properties.size();i++){
			String prop = this.properties.get(i).getProperty();
			if(prop.equals(imgDbpedia)){
				return this.properties.get(i).getValue();
			}
		}
		return null;
	}
	
	public String getLabel(){
		String lb = "<http://www.w3.org/2000/01/rdf-schema#label>";
		
		for(int i=0;i<this.properties.size();i++){
			String prop = this.properties.get(i).getProperty();
			if(prop.equals(lb)){
				return this.properties.get(i).getValue();
			}
		}
		return null;
	}
	
	public String getOptional(){
		String op = "<http://dbpedia.org/ontology/abstract>";
		
		for(int i=0;i<this.properties.size();i++){
			String prop = this.properties.get(i).getProperty();
			if(prop.equals(op)){
				return this.properties.get(i).getValue();
			}
		}
		return null;
	}
}
