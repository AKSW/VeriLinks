package org.aksw.verilinks.server.msg;


public class TemplateProperty{
	
	private String property;
	private String important;
	private String position;
	private String filter = null;
	private String linkedData= null;
	private String limit = null;
	
	public TemplateProperty(){
		
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getImportant() {
		return important;
	}

	public void setImportant(String important) {
		this.important = important;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getFilter() {
		return filter;
	}

	public void setLinkedData(String linkedData) {
		this.linkedData = linkedData;
	}

	public String getLinkedData() {
		return linkedData;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public String getLimit() {
		return limit;
	}

}
