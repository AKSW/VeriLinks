package org.aksw.verilinks.core.shared.jso;

import com.google.gwt.core.client.JavaScriptObject;

public class JsoTask extends JavaScriptObject {

	protected JsoTask(){}

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
	
	public final native String getDate() /*-{
	return this.date;
}-*/;
	
	public final native String getFile() /*-{
	return this.file;
}-*/;
	
	public final native String getLinkset() /*-{
	return this.linkset;
}-*/;
	
}
