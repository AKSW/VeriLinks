package org.aksw.verilinks.server.msg;

public class TemplateLinkset {
	
	private TemplateInstance subject;
	private TemplateInstance object;
	private String predicate;
	private String linkset;
	
//	
//	public TemplateLinkset(XMLTool xml, String linkset){
//		// Parse template file
//		parseTemplate(xml,linkset);
//		
//	}


	public TemplateLinkset() {
		// TODO Auto-generated constructor stub
	}


//	private void parseTemplate(XMLTool xml, String linkset) {
//		echo("Parse XMLTool");
//		xml.getLinkset(linkset);
//	}


	public TemplateInstance getSubject() {
		return subject;
	}


	public void setSubject(TemplateInstance subject) {
		this.subject = subject;
	}


	public TemplateInstance getObject() {
		return object;
	}


	public void setObject(TemplateInstance object) {
		this.object = object;
	}


	public String getPredicate() {
		return predicate;
	}


	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}


	public void setLinkset(String linkset) {
		this.linkset=linkset;
		
	}


	public String getLinkset() {
		return linkset;
	}

}
