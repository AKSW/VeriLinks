package org.aksw.verilinks.games.peaInvasion.shared.jso;

import com.google.gwt.core.client.JavaScriptObject;

public class JsoLinkset extends JavaScriptObject {

	protected JsoLinkset() {
	}

	public final native String getId() /*-{
		return this.id;
	}-*/;

	public final native String getSubject() /*-{
		return this.subject;
	}-*/;

	public final native String getObject() /*-{
		return this.object;
	}-*/;

	public final native String getPredicate() /*-{
		return this.predicate;
	}-*/;

	public final native String getDescription() /*-{
		return this.description;
	}-*/;

	public final native String getDifficulty() /*-{
		return this.difficulty;
	}-*/;
	
	public final native String getTemplate() /*-{
	return this.template;
}-*/;
}
