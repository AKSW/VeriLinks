package org.aksw.verilinks.games.peaInvasion.shared.jso;

import com.google.gwt.core.client.JavaScriptObject;

public class JsoUserdata extends JavaScriptObject{
	protected JsoUserdata(){}

	public final native String getName() /*-{
		return this.name;
	}-*/;

	public final native String getId() /*-{
		return this.id;
	}-*/;

	public final native int getHighscore() /*-{
		return this.highscore;
	}-*/;

	public final native String getStrength() /*-{
	return this.strength;
}-*/;
}
