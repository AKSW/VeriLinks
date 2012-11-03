package org.aksw.verilinks.games.peaInvasion.shared.jso;


import com.google.gwt.core.client.JavaScriptObject;

public class JsoLink extends JavaScriptObject{

	protected JsoLink(){}
	
	public final native int getId() /*-{
		return this.id;
	}-*/;
	
	public final native double getConfidence() /*-{
		return this.confidence;
	}-*/;
	
	public final native double getDifficulty() /*-{
		return this.difficulty;
	}-*/;

	public final native int getCounter() /*-{
		return this.counter;
	}-*/;

	public final native JsoInstance getSubject() /*-{
		return this.subject;
	}-*/;

	public final native JsoInstance getObject() /*-{
		return this.object;
	}-*/;
	
	public final native String getPredicate() /*-{
		return this.predicate;
	}-*/;
		
}
