package org.aksw.verilinks.games.peaInvasion.shared.jso;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class JsoTemplateProperty extends JavaScriptObject{
	protected JsoTemplateProperty() {
	}

	public final native String getProperty() /*-{
		return this.property;
	}-*/;

}
