package org.aksw.verilinks.games.peaInvasion.shared.jso;

import com.google.gwt.core.client.JavaScriptObject;

public class JsoUserstrength extends JavaScriptObject{
	protected JsoUserstrength(){}

	public final native String getUserstrength() /*-{
		return this.userstrength;
	}-*/;

}
