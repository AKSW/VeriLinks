package org.aksw.verilinks.core.shared.jso;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class JsoLinksetArray extends JavaScriptObject{

	 protected JsoLinksetArray() {}
	
	 public final native JsArray<JsoLinkset> getLinkset() /*-{
  		return this.linksets;
		}-*/;
}
