package org.aksw.verilinks.games.peaInvasion.client.panels;


import org.aksw.verilinks.games.peaInvasion.client.core.info.Statistics;
import org.aksw.verilinks.games.peaInvasion.shared.User;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class StatisticsPanel extends VerticalPanel{

//	private int score;
//	private int money;
//	private int level;
//	private int time;
//	private int numVerifications;
//	private int numAgreedVerifications;
//	private int numDisagreedVerifications;
	private Statistics stats;
	
	
	public StatisticsPanel(Statistics statistics){
		super();
		stats= statistics;
		//this.setStyleName("statisticsPanel");
		initGUI();
	}

	private void initGUI() {
		HTML nameDesc = new HTML("<b>Player:</b>");
		HTML name = new HTML(stats.getUser().getName());
		name.addStyleName("font_italic");
		HTML levelDesc = new HTML("<b>Level:</b>");
		HTML level = new HTML(""+stats.getLevel());
		level.addStyleName("font_italic");
		HTML scoreDesc = new HTML("<b>Score:</b>");
		HTML score = new HTML(""+stats.getScore());
		score.addStyleName("font_italic");
		HTML countDesc = new HTML("<b>#Verifications:</b>");
		HTML count = new HTML(""+stats.getVerification().getList().size());
		count.addStyleName("font_italic");
		HTML agreeDesc = new HTML("<b>#Agreements:</b>");
		HTML agree = new HTML(""+stats.getVerification().getCountAgreed());
		agree.addStyleName("font_italic");
		HTML disagreeDesc = new HTML("<b>#Disagreements:</b>");
		HTML disagree = new HTML(""+stats.getVerification().getCountDisagreed());
		disagree.addStyleName("font_italic");
		HTML penaltyDesc = new HTML("<b>#Penalties</b>");
		HTML penalty = new HTML(""+stats.getVerification().getCountPenalty());
		penalty.addStyleName("font_italic");
		VerticalPanel descPanel = new VerticalPanel();
		descPanel.add(nameDesc);
		descPanel.add(levelDesc);
		descPanel.add(scoreDesc);
		descPanel.add(countDesc);
		descPanel.add(agreeDesc);
		descPanel.add(disagreeDesc);
		descPanel.add(penaltyDesc);
		
		
		VerticalPanel valuePanel = new VerticalPanel();
		valuePanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		valuePanel.add(name);
		valuePanel.add(level);
		valuePanel.add(score);
		valuePanel.add(count);
		valuePanel.add(agree);
		valuePanel.add(disagree);
		valuePanel.add(penalty);
		//valuePanel.setStyleName("statisticsPanel_value");
		
		HorizontalPanel statsPanel = new HorizontalPanel();
		statsPanel.add(descPanel);
		statsPanel.add(valuePanel);
		statsPanel.setStyleName("statisticsPanel_stats");
		
		HTML desc= new HTML("Game Statistics");
		desc.setStyleName("statisticsPanel_desc");
		
		add(desc);
		add(statsPanel);
	}
}