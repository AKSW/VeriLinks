package org.aksw.verilinks.core.shared.jso;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class JsoHighscore extends JavaScriptObject{

	 protected JsoHighscore() {}
	
	 public final native JsArray<JsoScore> getHighscore() /*-{
   return this.highscore;
 	}-*/;
	 
}
