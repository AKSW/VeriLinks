package org.aksw.verilinks.core.shared.jso;

import com.google.gwt.core.client.JavaScriptObject;

public class JsoPropertyName extends JavaScriptObject{
	
	protected JsoPropertyName(){}
	
	public final native String getName() /*-{
		return this.name;
	}-*/;

}
