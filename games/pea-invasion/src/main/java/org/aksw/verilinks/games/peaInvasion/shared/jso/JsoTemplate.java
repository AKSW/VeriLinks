package org.aksw.verilinks.games.peaInvasion.shared.jso;

import com.google.gwt.core.client.JavaScriptObject;

public class JsoTemplate extends JavaScriptObject {
	protected JsoTemplate(){}

	public final native String getName() /*-{
		return this.name;
	}-*/;

	public final native JsoTemplateInstance getSubject() /*-{
		return this.subject;
	}-*/;

	public final native JsoTemplateInstance getObject() /*-{
		return this.score;
	}-*/;
}
