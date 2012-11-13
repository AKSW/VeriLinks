package org.aksw.verilinks.games.peaInvasion.shared.jso;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class JsoTemplateArray extends JavaScriptObject{
	 protected JsoTemplateArray() {}
		
	 public final native JsArray<JsoTemplate> getTemplates() /*-{
   return this.templates;
 	}-*/;
}
