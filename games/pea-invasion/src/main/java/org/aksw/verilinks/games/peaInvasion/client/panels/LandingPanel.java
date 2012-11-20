package org.aksw.verilinks.games.peaInvasion.client.panels;

import java.util.Collection;
import java.util.Date;

import org.aksw.verilinks.games.peaInvasion.client.oauth.Auth;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class LandingPanel extends VerticalPanel{
  
  private Button continueButton;
  private CheckBox newbieCheckBox;
  /**
   * The timeout before a cookie expires, in milliseconds. Current one day.
   */
  private static final int COOKIE_TIMEOUT = 1000 * 60 * 60 * 24;
  
  public LandingPanel(){
    initGUI();
    this.setWidth("750px");
   
  }

  private void initGUI() {
  	Image peaImage = new Image("PeaInvasion/images/landingPage/landingPage_peas.png");
    peaImage.setHeight("200px");
    peaImage.setStyleName("landingPanel_peaImage");
    DOM.setStyleAttribute(peaImage.getElement(), "marginRight", "15px");
    DOM.setStyleAttribute(peaImage.getElement(), "marginTop", "20px");
    DOM.setStyleAttribute(peaImage.getElement(), "border", "1px solid lightgrey");
    
    Image enemyImage = new Image("PeaInvasion/images/landingPage/landingPage_enemy.png");
    enemyImage.setHeight("150px");
    DOM.setStyleAttribute(enemyImage.getElement(), "margin", "10px");
//    DOM.setStyleAttribute(enemyImage.getElement(), "marginLeft", "10px");
    DOM.setStyleAttribute(enemyImage.getElement(), "border", "1px solid black");
    
    HTML head =new HTML("Welcome to Veri-Links");
    head.setStyleName("landingPanel_head");
    DOM.setStyleAttribute(head.getElement(), "borderBottom", "1px dashed black");
    
    HTML info = new HTML("<b>Veri-Links</b> belongs to the <i>Games With A Purpose</i> <a href='http://en.wikipedia.org/wiki/Human-based_computation_game' target='_blank'>(GWAP)</a> genre. " +
    		"Playing Veri-Links means turning this world in to a better place.. well.. kind of.. " +
    		"<br><br>While playing this game, you'll generate useful data as a byproduct - which will be used to improve the semantic web. " +
    		"The data is collected when you answer the questions in the <a href='#' class='infoBlack'><b>quiz</b>" +
    		"<span><b><i>Note:</i><br>You will verify links of the semantic web. Compare 2 instances of 2 different ontologies and decide whether their relation is valid or not.</b></span></a>. " +
    		"You can also skip difficult questions, so please answer them only if you're certain :)" );
    HTML goal = new HTML("<b>Goal</b>");
    goal.setStyleName("landingPanel_goalText");
    DOM.setStyleAttribute(goal.getElement(), "marginTop", "10px");
//    DOM.setStyleAttribute(goal.getElement(), "marginBottom", "0px");
   
    HTML important = new HTML(
        "<a href='#' class='infoLanding'>[1] Fight the enemy troops and increase your score!" +
        "<span><img src='PeaInvasion/images/landingPage/landingPage_screenshot.png' class='imageLanding' alt='screenshot' height='170'/>"  +
        "<br>Use your 'Peas' to hinder the enemy troops from reaching the left side of the game screen. With every defeated enemy, your score will increase." +
        "<br><br>The game mechanics are similiar to a <br>Tower Defense game." +
//        "<br><br>So.. do you have the ability to climb up to the the top of the highscore list?!" 
        "</span></a>"+
        "<br><a href='#' class='infoLanding'>[2] Answer the quiz to gain coins!" +
        "<span><img src='PeaInvasion/images/landingPage/landingPage_screenshot2.png' alt='screenshot' width='500' float='none' border='1px solid black'/>"+
        "<br>Answer the quiz to earn coins, so you can buy new troops. The more coins you have, the better you can defend and achieve a higher score. " +
        "<br><br>Your answers will be compared with the answers of other users. If the majority agrees with your decision you'll get a coin- and score bonus!"+
        "</span></a>"
        );
    important.addStyleName("landingPanel_important");
    
    HTML admin = new HTML("<strong>[Admin Tab]</strong><br>You can verify the semantic web links that are already in our database.<br>" +
    		"But you can also upload your own links! Just add a rdf or a n-triple file containing your links to our server.");
    HTML controlHead = new HTML("<br><strong>[Game Controls]</strong><br><br>");
    HTML controlVerify = new HTML("Verification:" +
    		"<ul>" +
    		"<li>2 x [<strong>1</strong>] = valid" +
    		"<li>2 x [<strong>2</strong>] = not valid" +
    		"<li>2 x [<strong>3</strong>] = not sure" +
    		"</ul>");
    HTML controlGame = new HTML("Game:" +
    		"<ul>" +
    		"<li>[<strong>&larr;</strong>] and [<strong>&rarr;</strong>] = move Pea drop position" +
    		"<li>[<strong>Enter</strong>] = drop Pea" +
    		"<li>[<strong>Esc</strong>] = pause/resume"+
    		"</ul>");
    controlGame.addStyleName("landingPanel_controlGame");
    HorizontalPanel controlPanel = new HorizontalPanel();
    controlPanel.add(controlVerify);
    controlPanel.add(controlGame);
    
    HTML creditsHead = new HTML("<b><br>Credits</b>");
    
    HTML creditsInfo = new HTML(
    		"Game development by the Research Group <b>Agile Knowledge Engineering and Semantic Web </b>" +
    		"<a href='http://aksw.org/About' target='_blank'>(AKSW)</a>."+
    		"<br>Game design and implementation by <i>Quan Nguyen</i>.<br>Project supervised by <i><a href='http://jens-lehmann.org/' target='_blank'>Dr. Jens Lehmann</a></i>.");
    creditsInfo.setStyleName("landingPanel_creditsInfo");
    DOM.setStyleAttribute(creditsInfo.getElement(), "margin", "11px");
    
    // Credits DialogBox
    final DialogBox creditsDialog = new DialogBox();
    creditsDialog.setStyleName("landingPanel_creditsDialog");
    DOM.setStyleAttribute(creditsDialog.getElement(), "border", "2px solid black");

    creditsDialog.setGlassEnabled(true);
    creditsDialog.setAnimationEnabled(true);
    
    HTML creditsDialogHead = new HTML("<b>Credits</b>");
    creditsDialogHead.setStyleName("landingPanel_creditsDialogHead");
//    DOM.setStyleAttribute(creditsDialogHead.getElement(), "backgroundColor", "#39538E");
    Button closeCredits = new Button("Close");
    closeCredits.setStyleName("landingPanel_creditsCialogClose");
    DOM.setStyleAttribute(closeCredits.getElement(), "backgroundColor", "#39538E");
    DOM.setStyleAttribute(closeCredits.getElement(), "border", "1px solid black");
    DOM.setStyleAttribute(closeCredits.getElement(), "margin", "3px");
    DOM.setStyleAttribute(closeCredits.getElement(), "color", "white");
    
    closeCredits.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				creditsDialog.hide();
			}});
    
    HorizontalPanel creditsPanelTop = new HorizontalPanel();
    DOM.setStyleAttribute(creditsPanelTop.getElement(), "padding", "5px");
    creditsPanelTop.add(creditsInfo);
    creditsPanelTop.add(enemyImage);
    
    VerticalPanel creditsPanel = new VerticalPanel();
    creditsPanel.add(creditsDialogHead);
    creditsPanel.add(creditsPanelTop);
    creditsPanel.add(closeCredits);
    
    creditsDialog.setWidget(creditsPanel);
    
    creditsHead.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				creditsDialog.center();
				creditsDialog.show();
			}});
    
//  
//    VerticalPanel credits = new VerticalPanel();
//    credits.add(creditsHead);
//    credits.add(creditsInfo);
    
    HTML copyright = new HTML("<i>© 2011-2012, Quan Nguyen, All Rights Reserved</i>");
    copyright.setStyleName("landingPanel_browser");
    DOM.setStyleAttribute(copyright.getElement(), "margin", "5px");
    HTML browser = new HTML("[Recommended browsers]: Google Chrome, Internet Explorer");
    browser.addStyleName("landingPanel_browser");
    continueButton = new Button("CONTINUE");
//    continueButton.addStyleName("myButton");
    continueButton.setStyleName("myButton2");
    DOM.setStyleAttribute(continueButton.getElement(), "marginTop", "6px");
    DOM.setStyleAttribute(continueButton.getElement(), "border", "1px solid black");
    DOM.setStyleAttribute(continueButton.getElement(), "backgroundColor", "#39538E");
    DOM.setStyleAttribute(continueButton.getElement(), "color", "white");
    DOM.setStyleAttribute(continueButton.getElement(), "padding", "2px 4px");
    
    newbieCheckBox = new CheckBox("I'm a semantic web pro, show me all the details");
    
    // Style
//    info.addStyleName("landingPanel_fontSize");
    info.setStyleName("landingPanel_info");
    DOM.setStyleAttribute(info.getElement(), "backgroundColor", "white");
    DOM.setStyleAttribute(info.getElement(), "border", "1px solid grey");
    DOM.setStyleAttribute(info.getElement(), "padding", "20px");
    DOM.setStyleAttribute(info.getElement(), "marginTop", "15px");
    DOM.setStyleAttribute(info.getElement(), "letterSpacing", "normal");
    DOM.setStyleAttribute(info.getElement(), "textAlign", "center");

    DOM.setStyleAttribute(important.getElement(), "border", "1px solid black");
    DOM.setStyleAttribute(important.getElement(), "marginBottom", "0px");
    info.addStyleName("landingPanel_fontSize");
    goal.addStyleName("landingPanel_fontSize");
    important.addStyleName("landingPanel_fontSize");
    
    admin.addStyleName("landingPanel_fontSizeSmall");
    controlHead.addStyleName("landingPanel_fontSizeSmall");
    controlGame.addStyleName("landingPanel_fontSizeSmall");
    controlVerify.addStyleName("landingPanel_fontSizeSmall");
    creditsHead.addStyleName("landingPanel_fontSize");
    creditsInfo.addStyleName("landingPanel_fontSize");
    
    HorizontalPanel first = new HorizontalPanel();
    first.add(peaImage);
    first.add(info);
//    
//    HorizontalPanel last = new HorizontalPanel();
//    last.add(credits);
    
    
    VerticalPanel body = new VerticalPanel();
    body.add(first);
    body.add(goal);
    body.add(important);
//    body.add(admin);
//    body.add(controlHead);
//    body.add(controlPanel);
//    body.add(last);
    body.add(creditsHead);
    body.setHorizontalAlignment(ALIGN_CENTER);

//    body.add(newbieCheckBox);
    body.add(continueButton);
    body.addStyleName("landingPanel_body");
    DOM.setStyleAttribute(body.getElement(), "padding", "10px 15px");
    DOM.setStyleAttribute(body.getElement(), "paddingTop", "0px");
    DOM.setStyleAttribute(body.getElement(), "marginTop", "0px");
    DOM.setStyleAttribute(head.getElement(), "marginBottom", "0px");
    add(head);
    add(body);
    this.setHorizontalAlignment(ALIGN_RIGHT);
//    add(browser);
    add(copyright);
    setStyleName("landingPanel");
//    
////    Cookies cookie;
//    final TextBox cookieName = new TextBox();
//    final TextBox cookieValue = new TextBox();
//    ListBox existingCookiesBox = new ListBox();
//    Button setCookieButton = new Button("set Cookie");
//    setCookieButton.addClickHandler(new ClickHandler() {
//      public void onClick(ClickEvent event) {
//        String name = cookieName.getText();
//        String value = cookieValue.getText();
//        Date expires = new Date((new Date()).getTime() + COOKIE_TIMEOUT);
//
//        // Verify the name is valid
//        if (name.length() < 1) {
//          Window.alert("Invalid cookie!");
//          return;
//        }
//
//        // Set the cookie value
//        Cookies.setCookie(name, value, expires);
////        refreshExistingCookies(name);
//      }
//    });
//
//    
//    int selectedIndex = 0;
////    Collection<String> cookies = Cookies.getCookieNames();
////    for (String cookie : cookies) {
////      existingCookiesBox.addItem(cookie);
////      if (cookie.equals(null)) {
////        selectedIndex = existingCookiesBox.getItemCount() - 1;
////      }
////    }
//
//    Storage.getLocalStorageIfSupported();
//    
//    add(existingCookiesBox);
//    add(cookieName);
//    add(cookieValue);
//    add(setCookieButton);
//    
//    
//
//      Button button = new Button("Clear stored tokens");
//      button.addClickHandler(new ClickHandler() {
//        public void onClick(ClickEvent event) {
//          Auth.get().clearAllTokens();
//          Window.alert("All tokens cleared");
//        }
//      });
//     add(button);
    
  }
  
  public void setNextClickHandler(ClickHandler clickHandler) {
    this.continueButton.addClickHandler(clickHandler);
    
  }
  
  private class SubmitListener extends KeyboardListenerAdapter {
    public void onKeyPress(Widget sender, char key, int mods) {
      if (KeyboardListener.KEY_ENTER == key){
        continueButton.click();
        System.out.println("ENTER");
      }
    }
  }
  
  /** Focus continue button for keyListener*/
  public void focus(){
  	continueButton.setFocus(true);
  }
  
  public boolean getNewbie(){
//  	return !this.newbieCheckBox.getValue();
  	String urlParam = com.google.gwt.user.client.Window.Location.getParameter("pro");
//    Window.alert("urlParam: "+urlParam);
    if(urlParam!=null && urlParam.equals("true")){
    	return false;
    }
    else
    	return true;
  }
  
}
