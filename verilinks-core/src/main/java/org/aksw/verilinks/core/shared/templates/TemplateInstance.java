package org.aksw.verilinks.core.shared.templates;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TemplateInstance implements IsSerializable{

//	private Node instance;
	private String ontology;
	private String endpoint;
	private String type;
	private List<TemplateProperty> properties = new ArrayList<TemplateProperty>();
	
	public TemplateInstance() {

	}

	

	private void echo(String string) {
//		System.out.println("TemplateInstance: "+string);
	}

	public List<TemplateProperty> getProperties() {
		return properties;
	}

	public void setProperties(List<TemplateProperty> properties) {
		this.properties = properties;
	}

	public String getOntology() {
		return ontology;
	}

	public void setOntology(String ontology) {
		this.ontology = ontology;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
