package org.aksw.verilinks.games.peaInvasion.shared.jso;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class JsoHighscoreArray extends JavaScriptObject{

	 protected JsoHighscoreArray() {}
	
	 public final native JsArray<JsoScore> getHighscore() /*-{
   return this.highscore;
 	}-*/;
	 
}
