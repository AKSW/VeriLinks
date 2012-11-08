package org.aksw.verilinks.core.shared.jso;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class JsoTaskArray extends JavaScriptObject {

	 protected JsoTaskArray() {}
		
	 public final native JsArray<JsoTask> getTasks() /*-{
  		return this.tasks;
		}-*/;
}
