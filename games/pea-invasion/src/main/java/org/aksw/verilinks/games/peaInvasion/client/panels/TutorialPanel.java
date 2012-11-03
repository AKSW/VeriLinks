package org.aksw.verilinks.games.peaInvasion.client.panels;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TutorialPanel extends VerticalPanel {

	private Button next;
	private Button back;
	private Button skip;
	private HTML headHTML;
	private HTML topicHTML;
	private Image pic;
	private HTML textHTML;
	private String picURL;
	private int index;
	public final int END = 8;
	private PopupPanel glassPanel;
	
	private VerticalPanel picPanel;
	private Image image0;
	private Image image1;
	private Image image2;
	private Image image3;
	private Image image4;
	private Image image5;
	private Image image6;
	private Image image7;
	private Image image8;

	private boolean isFirst=true;
	
	public TutorialPanel() {
		super();
		this.index = 0;
		this.setStyleName("tutorialPanel");
		initGUI();
	}

	private void initGUI() {
		// Images
		initImages();
		// Button
		next = new Button("Next");
		next.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent arg0) {
				next();

			}
		});
		back = new Button("Back");
		back.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent arg0) {
				back();

			}
		});
		back.setEnabled(false);
		skip = new Button("Skip Tutorial");
		next.addStyleName("myButton");
		back.addStyleName("myButton");
		skip.addStyleName("myButton");

		// Head
		HorizontalPanel headPanel = new HorizontalPanel();
		headHTML = new HTML("Tutorial: How To Play");
		headHTML.setStyleName("tutorialPanel_headText");
		headPanel.setStyleName("tutorialPanel_headPanel");
		headPanel.add(headHTML);
		headPanel.setHorizontalAlignment(ALIGN_RIGHT);
		headPanel.add(skip);
		// Topic
		topicHTML = new HTML("");
		topicHTML.setStyleName("tutorialPanel_topic");
		// Image
		pic = image0;
		picPanel = new VerticalPanel();
//		picPanel.setStyleName("tutorialPanel_image");
		picPanel.add(pic);
		// Text
		textHTML = new HTML();
		String text = "Welcome to the Veri-Links Tutorial! "
				+ "Click 'Next' to start the short tutorial or click 'Skip Tutorial' to start the game";
		textHTML.setHTML(text);
		textHTML.setStyleName("tutorialPanel_text");
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setStyleName("tutorialPanel_buttonPanel");
		buttonPanel.add(back);
		buttonPanel.add(next);
		this.add(headPanel);
		// add(topicHTML);
		this.setHorizontalAlignment(ALIGN_CENTER);
		add(picPanel);
		// this.setHorizontalAlignment(ALIGN_DEFAULT);
		add(textHTML);
		this.setHorizontalAlignment(ALIGN_RIGHT);
		add(buttonPanel);
	}

	private void initImages(){
		image0 = new Image("Application/images/tutorial/tutorialIntro.png");
		image1 = new Image("Application/images/tutorial/tutorialControls.png");
		image2 = new Image("Application/images/tutorial/tutorialControls2.png");
		image3 = new Image("Application/images/tutorial/tutorialControls3.png");
		image4 = new Image("Application/images/tutorial/tutorialGoal.png");
		image5 = new Image("Application/images/tutorial/tutorialGoal2.png");
		image6 = new Image("Application/images/tutorial/tutorialGoal3.png");
		image7 = new Image("Application/images/tutorial/tutorialUnits.png");
		image8 = new Image("Application/images/tutorial/tutorialVerify2.png");
		
		image0.setStyleName("tutorialPanel_image");
		image1.setStyleName("tutorialPanel_image");
		image2.setStyleName("tutorialPanel_image");
		image3.setStyleName("tutorialPanel_image");
		image4.setStyleName("tutorialPanel_image");
		image5.setStyleName("tutorialPanel_image");
		image6.setStyleName("tutorialPanel_image");
		image7.setStyleName("tutorialPanel_image");
		image8.setStyleName("tutorialPanel_image");
		DOM.setStyleAttribute(image8.getElement(), "height", "200px");
		
	}
	
	public void setNextClickHandler(ClickHandler handler) {
		next.addClickHandler(handler);
	}

	public void setBackClickHandler(ClickHandler handler) {
		back.addClickHandler(handler);
	}

	public void setSkipClickHandler(ClickHandler handler) {
		skip.addClickHandler(handler);
	}

	/** Change to next tutorial page */
	public void next() {
		echo("" + index);
		index++;
		switch (index) {
		case 1:
			controls();
			break;
		case 2:
			controls2();
			break;
		case 3:
			controls3();
			break;
		case 4:
			goal();
			break;
		case 5:
			goal2();
			break;
		case 6:
			goal3();
			break;
		// case 7:
		// units();
		// break;
		case 7:
			verify();
			break;
		case END:
			clickSkip();
			break;
		}
		echo("" + index);
		if (index != END)
			this.glassPanel.center();
	}

	/** Change to next tutorial page */
	public void back() {
		echo("" + index);
		--index;
		switch (index) {
		case 0:
			break;
		case 1:
			controls();
			break;
		case 2:
			controls2();
			break;
		case 3:
			controls3();
			break;
		case 4:
			goal();
			break;
		case 5:
			goal2();
			break;
		case 6:
			goal3();
			break;
		// case 7:
		// units();
		// break;
		case 7:
			verify();
			break;
		}
		echo("" + index);
		this.glassPanel.center();
	}

	public void controls() {
		back.setEnabled(false);
		// Head
		headHTML.setHTML("Tutorial: Controls");
		// Topic
		topicHTML.setHTML("Change drop position");
		// Image
		picPanel.clear();
		picPanel.add(image1);
		// Text
		textHTML
				.setHTML("Use the <b>LEFT</b> and <b>RIGHT</b> arrow key to switch "
						+ "between drop positions.");
	}

	public void controls2() {
		back.setEnabled(true);
		// Head
		headHTML.setHTML("Tutorial: Controls");
		// Topic
		topicHTML.setHTML("Change drop position");
		// Image
		picPanel.clear();
		picPanel.add(image2);
		// Text
		textHTML
				.setHTML("Each level has a <b>fixed</b> amount of <b>drop positions</b>.");
	}

	public void controls3() {
		back.setEnabled(true);
		// Head
		headHTML.setHTML("Tutorial: Controls");
		// Topic
		topicHTML.setHTML("Drop Pea");
		// Image
		picPanel.clear();
		picPanel.add(image3);
		// Text
		textHTML.setHTML("Use the <b>ENTER</b> key to drop your pea. "
				+ "Each dropped pea will <b>cost coins</b>.");
	}

	public void goal() {
		back.setEnabled(true);
		// Head
		headHTML.setHTML("Tutorial: Goal");
		// Topic
		topicHTML.setHTML("Hit Enemy Peas");
		// Image
		picPanel.clear();
		picPanel.add(image4);
		// Text
		textHTML
				.setHTML("Try to <b>hit</b> the <b>enemy peas</b> with your own pea. ");
	}

	public void goal2() {
		next.setHTML("Next");
		back.setEnabled(true);
		// Head
		headHTML.setHTML("Tutorial: Goal");
		// Topic
		topicHTML.setHTML("Hit Enemy Peas");
		// Image
		picPanel.clear();
		picPanel.add(image5);
		// Text
		textHTML
				.setHTML("Weakened peas will change their color. "
						+ "Your <b>score</b> increases</b> with every <b>eliminated enemy pea</b>.");
	}

	public void goal3() {
		next.setHTML("Next");
		back.setEnabled(true);
		// Head
		headHTML.setHTML("Tutorial: Goal");
		// Topic
		topicHTML.setHTML("Defend");
		// Image
		picPanel.clear();
		picPanel.add(image6);
		// Text
		textHTML
				.setHTML("Enemy peas <b>must not</b> reach your side! Defend your village!");
	}

	public void units() {
		back.setEnabled(true);

		next.setHTML("Next");
		// Head
		headHTML.setHTML("Tutorial: Units");
		// Topic
		topicHTML.setHTML("Units");
		// Image
		picPanel.clear();
		picPanel.add(image7);
		// Text
		textHTML.setHTML("Do this and do that");
	}

	public void verify() {
		if(isFirst)
			next.setHTML("Start Game");
		else
			next.setHTML("Resume Game");
		// Head
		headHTML.setHTML("Tutorial: Verification");
		// Topic
		topicHTML.setHTML("Verification");
		// Image
		picPanel.clear();
		picPanel.add(image8);
		// Text
		textHTML
				.setHTML("Decide whether this statement is <b>True</b> or <b>Not False</b>. "
						+ "If you don't have a clue, choose <b>Not Sure</b>. <br>For each verified statement, you'll be rewarded with coins - which you can use to buy new units."
						+ "<br><br><b>Shortcut-Keys:</b> Push key <b>[1]</b> for <b>Valid</b>, key <b>[2]</b> for <b>Not Valid</b> and key <b>[3]</b> for <b>Not Sure</b>. "
						+ "<br><br>Please mark a statement as valid or not valid <b>only</b> if you're <b>secure</b> in your decision."
						+ "<br>If your verification <b>corresponds</b> to the verification of the majority of the players, you will get a <b>bonus</b>."
						+ "<br>But watch out: False verifications will be <b>punished</b>! So avoid making random decisions."
						+"<br>The actually displayed quiz may vary from game to game."
//						+ "<br>and if you have the slightest doubt, better choose 'Not Sure' or click the <b>http-links</b> for further information."
						+ "<br><br>Thanks for your attention. Now have some fun and start playing! :)<br>");

//		textHTML
//		.setHTML("Decide whether this statement is True <img src='Application/images/verification/true.png'/> or False <img src='Application/images/verification/false.png'/>. If you don't have a clue, choose Unsure <img src='Application/images/verification/unsure.png'/>." +
//				"</br></br>You earn coins for answering questions correctly, which you can use to buy new units. " +
//				"</br>The more difficult the questions are, the higher your reward." +
//				"</br>If you incorrectly answer a question, you get a penalty." +
//				"</br>If the player community has no agreement on the answer of a question," +
//				"</br>you get only few coins as bonus for answering the questions (we do not know the correct answer - it is all up to the players). " +
//				"</br></br>Shortcut-Keys: Push <img src='Application/images/verification/key1.png'/> for True, <img src='Application/images/verification/key1.png'/> for False and <img src='Application/images/verification/key1.png'/> for Unsure. </br>Thanks for your attention. Now have some fun and start playing! :)");
		textHTML
		.setHTML("Decide whether this statement is True <img src='Application/images/verification/true.png'/> or False <img src='Application/images/verification/false.png'/>. If you don't have a clue, choose Unsure <img src='Application/images/verification/unsure.png'/>." +
				"</br></br>You earn coins for answering questions correctly, which you can use to buy new units. " +
				"</br>The more difficult the questions are, the higher your reward. " +
				"If you incorrectly answer a question, you get a penalty. " +
				"</br>If the player community has no agreement on the answer of a question, " +
				"you get only few coins as bonus for answering the questions (we do not know the correct answer - it is all up to the players). " +
				"</br></br><b>Shortcut-Keys:</b> Push <img src='Application/images/verification/key1.png'/> for True, <img src='Application/images/verification/key2.png'/> for False and <img src='Application/images/verification/key3.png'/> for Unsure. " +
				"</br></br>Thanks for your attention. Now have some fun and start playing! :)");
		
		DOM.setStyleAttribute(textHTML.getElement(), "width", "850px");
		DOM.setStyleAttribute(textHTML.getElement(), "padding", "6px");
	}

	public void verify2() {
		next.setHTML("Start Game");
		// Head
		headHTML.setHTML("Tutorial: Verification");
		// Topic
		topicHTML.setHTML("Verification");
		// Image
		picURL = "Application/images/tutorial/tutorialVerify2.png";
		pic.setUrl(picURL);
		
		// Text
		textHTML
				.setHTML("Decide whether this statement is <b>Valid</b> or <b>Not Valid</b>. "
						+ "If you don't have a clue, choose <b>Not Sure</b>. "
						+ "<br><b>Shortcut-Keys:</b> Push key <b>[1]</b> for <b>Valid</b>, key <b>[2]</b> for <b>Not Valid</b> and key <b>[3]</b> for <b>Not Sure</b>."
						+ "<br>Each verification will increase your coins."
						+ "<br><br>Please mark a statement as valid or not valid only if you're secure in your decision. Thanks :)"
						+ "<br>Now have some fun and start playing!");
	}

	private void clickSkip() {
		skip.click();
	}

	private void echo(String s) {
		System.out.println(">>TutorialPanel: " + s);
	}

	private void refresh() {

	}

	public void setGlassPanel(PopupPanel pop) {
		this.glassPanel = pop;

	}
	
	public void setNextText(){
		isFirst = false;
	}
}
