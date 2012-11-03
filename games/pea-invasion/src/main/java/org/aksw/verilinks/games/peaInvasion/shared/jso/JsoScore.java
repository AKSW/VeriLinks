package org.aksw.verilinks.games.peaInvasion.shared.jso;

import com.google.gwt.core.client.JavaScriptObject;


public class JsoScore extends JavaScriptObject{

	protected JsoScore(){}

	public final native String getName() /*-{
		return this.name;
	}-*/;

	public final native String getId() /*-{
		return this.id;
	}-*/;

	public final native int getScore() /*-{
		return this.score;
	}-*/;

}
