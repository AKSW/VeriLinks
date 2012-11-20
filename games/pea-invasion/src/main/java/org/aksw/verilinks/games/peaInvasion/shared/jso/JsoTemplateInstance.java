package org.aksw.verilinks.games.peaInvasion.shared.jso;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class JsoTemplateInstance extends JavaScriptObject {

	protected JsoTemplateInstance() {
	}

	public final native String getType() /*-{
		return this.type;
	}-*/;

	public final native JsArray<JsoTemplateProperty> getProperties() /*-{
		return this.properties;
	}-*/;
}
