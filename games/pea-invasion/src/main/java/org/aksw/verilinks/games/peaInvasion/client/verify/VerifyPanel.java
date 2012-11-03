package org.aksw.verilinks.games.peaInvasion.client.verify;

import org.aksw.verilinks.games.peaInvasion.shared.Configuration;
import org.aksw.verilinks.games.peaInvasion.shared.Template;
import org.aksw.verilinks.games.peaInvasion.shared.rdfInstance;
import org.aksw.verilinks.games.peaInvasion.shared.rdfStatement;
import org.aksw.verilinks.games.peaInvasion.shared.templates.TemplateInstance;
import org.aksw.verilinks.games.peaInvasion.shared.templates.TemplateLinkset;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public abstract class VerifyPanel extends VerticalPanel{
	protected Configuration config;
	protected TemplateInstance subjectTemplate;
	protected TemplateInstance objectTemplate;

	protected rdfInstance subjectInstance;
	protected rdfInstance objectInstance;

	protected HorizontalPanel sPanel;
	protected HorizontalPanel oPanel;
	protected VerticalPanel sInfoPanel;
	protected VerticalPanel oInfoPanel;
	
	public VerifyPanel(TemplateLinkset template, Configuration config){
		super();
		this.subjectTemplate = template.getSubject();
		this.objectTemplate = template.getObject();
		this.config=config;
		RootPanel.get("subject").add(this);
	}
	
	public abstract void update(rdfStatement link);
	

}
