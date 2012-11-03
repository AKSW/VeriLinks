package org.aksw.verilinks.games.peaInvasion.client.panels;

import org.aksw.verilinks.games.peaInvasion.shared.rdfInstance;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DetailsPanel extends Animation{

	private rdfInstance instance;
	private VerticalPanel panel;
	private double opacity;
	private int x;
	private int y;
	
	public DetailsPanel(rdfInstance instance, int posX, int posY){
		this.instance=instance;
		this.panel = new VerticalPanel();
		panel.setStyleName("detailsPanel");
		DOM.setStyleAttribute(panel.getElement(), "padding", "15px");
		DOM.setStyleAttribute(panel.getElement(), "border", "1px solid black");
		x = posX;
		y = posY;
		if(instance != null)
			initGUI();
		DOM.setStyleAttribute(panel.getElement(), "opacity", "0");
	}
	
	private void initGUI(){
		String iLabel = instance.getLabel();
		HTML label = new HTML("<b>Label:</b> "+iLabel);
		
		String iType = instance.getType();
		HTML type = new HTML("<b>Type:</b> "+iType);
		
		String iCom = instance.getOptional();
		HTML comment = new HTML("<b>Description:</b> "+iCom);
		
		panel.add(label);
		panel.add(type);
		panel.add(comment);
//		panel.setVisible(true);
		RootPanel.get().add(panel);
		if(x<0)
			x= 10;
		int height = panel.getOffsetHeight();

		y = y-height-20;
		RootPanel.get().add(panel,x,y);
		
	}
	
	@Override
	protected void onUpdate(double progress) {
		opacity+=0.2;
		String op = Double.toString(opacity);
		DOM.setStyleAttribute(panel.getElement(), "opacity", op);
		
	}

	public void destroy() {
		panel.removeFromParent();
	}

}
