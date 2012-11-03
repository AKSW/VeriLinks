package org.aksw.verilinks.games.peaInvasion.shared.msg;

import java.util.HashMap;
import java.util.List;

public class Instance {
	
	private String uri;
//	private String ontology;
//	private List<Property> properties;
	private List<Property> properties;
	private List<String> propertyNames;
	
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

}
