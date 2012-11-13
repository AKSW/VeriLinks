package org.aksw.verilinks.games.peaInvasion.client.panels;

import java.util.ArrayList;

import org.aksw.verilinks.games.peaInvasion.shared.msg.Score;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class HighscorePanel extends VerticalPanel{

	private ListBox highscoreList;
  private HTML scoreHTML;
  private VerticalPanel hsRankPanel;
  private VerticalPanel hsNamePanel;
  private VerticalPanel hsScorePanel;
	private Button closeButton;
	private Button submitButton;
	
	public HighscorePanel(){
		super();
		initGUI();
	}

	public HighscorePanel(boolean b){
		super();
		initGUI();
		if(!b)
			addButtons();
			
	}
	
	private void initGUI() {
	// TextBox
    //final TextBox nameInput= new TextBox();
    scoreHTML = new HTML("0");
   
    // Highscore List
    this.highscoreList = new ListBox();
    highscoreList.setVisibleItemCount(10);
    
    // Highscore panel
    ScrollPanel hsScrollPanel = new ScrollPanel();
    HorizontalPanel hsPanel = new HorizontalPanel();
    hsRankPanel = new VerticalPanel();
    hsRankPanel.setHorizontalAlignment(ALIGN_CENTER);
    hsNamePanel = new VerticalPanel();
    hsNamePanel.setHorizontalAlignment(ALIGN_CENTER);
    hsScorePanel = new VerticalPanel();
    hsScorePanel.setHorizontalAlignment(ALIGN_RIGHT);
    hsPanel.add(hsRankPanel);
    hsPanel.add(hsNamePanel);
    hsPanel.add(hsScorePanel);
    hsScrollPanel.add(hsPanel);
    hsScrollPanel.setStyleName("highscorePanel_scroll");
    // Size
    hsScrollPanel.setSize("270px", "260px");
    
    // Spacing
    hsPanel.setSpacing(7);
    hsRankPanel.setSpacing(7);
    hsNamePanel.setSpacing(7);
    hsScorePanel.setSpacing(7);
    
    // Alignment
    //hsScorePanel.setHorizontalAlignment(HorizontalPanel.ALIGN_LOCALE_END);
    
    HTML head = new HTML("Highscore");
    head.setStyleName("highscorePanel_head");
    // Panel for all the popup widgets
    // highscorePanel = new VerticalPanel();
    this.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
    this.add(head);
    //highscorePanel.add(highscoreList);
    this.add(hsScrollPanel);
    this.add(new HTML("<br><b>Your score:</b>"));
    this.add(scoreHTML);
    //highscorePanel.add(nameInput);
    //highscorePanel.setSize("500","500");
    this.setStyleName("highscorePanel");
//    DOM.setStyleAttribute(this.getElement(), "backgroundPosition", "-50px -50px");
//    DOM.setStyleAttribute(this.getElement(), "backgroundColor", "white");
//    DOM.setStyleAttribute(head.getElement(), "fontSize", "1.7em");
//    DOM.setStyleAttribute(head.getElement(), "paddingTop", "15px");
//    DOM.setStyleAttribute(head.getElement(), "paddingBottom", "10px");
//    DOM.setStyleAttribute(head.getElement(), "borderBottom", "1px dashed grey");
	}

	private void addButtons() {

		 // Button
    closeButton = new Button("Discard");
    submitButton = new Button("Submit Score");
		
    
    // Alignment
    //hsScorePanel.setHorizontalAlignment(HorizontalPanel.ALIGN_LOCALE_END);
    
    // Buttonpanel
    HorizontalPanel buttonPanel = new HorizontalPanel();
    buttonPanel.add(submitButton);
    buttonPanel.add(closeButton);
    buttonPanel.setSpacing(4);
  
    this.add(buttonPanel);
		
	}
	
	public void setSubmitBtnClickHandler(ClickHandler handler) {
		this.submitButton.addClickHandler(handler);
	}
	
	public void setCloseBtnClickHandler(ClickHandler handler) {
		this.closeButton.addClickHandler(handler);
	}

	public void reset() {
		highscoreList.clear();
    hsRankPanel.clear();
    hsNamePanel.clear();
    hsScorePanel.clear();
		
	}

	public void setScore(String score) {
		scoreHTML.setText(score);
		
	}
	
	 /**
   * Update ListBox highscoreList
   * @param highscore
   */
  public void generateHighscoreList(ArrayList<Score> highscore) {
    int j =1;
    String name =null;
    int score = 0;
    for (int i = 0; i<highscore.size();i++) {
     
      	name = highscore.get(i).getName();
      	score = highscore.get(i).getScore();
      	if(name==null){
      		name = "<i>none</i>";
      		hsNamePanel.add(new HTML(name));
      	}
      	else
      		hsNamePanel.add(new HTML("<b>"+name+"</b>"));
      	hsScorePanel.add(new HTML(score+""));
      
      hsRankPanel.add(new Label(Integer.toString(i+1)));

      
    }
  }

  /**
   * Distinguish between startOfLevel(=no Buttons, show at start)
   * and !startOfLevel(=popUp with buttons)
   * @param startOfLevel
   */
	public void setStartOfGame(boolean startOfGame) {
		this.clear();
		initGUI();
		if(startOfGame==false)
			addButtons();
	}
	
	public void echo(String s){
		System.out.println("##HighscorePanel: ");
	}
	
}

