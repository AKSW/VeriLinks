package org.aksw.verilinks.games.peaInvasion.shared.jso;

import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class JsoInstance extends JavaScriptObject{

	protected JsoInstance(){}
	
	public final native String getUri() /*-{
		return this.uri;
	}-*/;

	public final native String getOntology() /*-{
		return this.ontology;
	}-*/;

	
	public final native JsArray<JsoProperty> getProperties() /*-{
 	return this.properties;
	}-*/;
	 
	public final native JsArray<JsoPropertyName> getPropertyNames() /*-{
 		return this.propertyNames;
	}-*/;

	public final native String get(String name) /*-{
		 return $wnd.name;
	}-*/;
 
	public final native String get() /*-{
	 return $wnd["verli:ontology"];
}-*/;
	
}
