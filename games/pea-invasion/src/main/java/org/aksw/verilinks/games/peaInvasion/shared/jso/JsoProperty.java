package org.aksw.verilinks.games.peaInvasion.shared.jso;

import com.google.gwt.core.client.JavaScriptObject;

public class JsoProperty extends JavaScriptObject{
	
	protected JsoProperty(){}
	
	public final native String getProperty() /*-{
		return this.property;
	}-*/;
	
	public final native String getValue() /*-{
		return this.value;
	}-*/;

}
