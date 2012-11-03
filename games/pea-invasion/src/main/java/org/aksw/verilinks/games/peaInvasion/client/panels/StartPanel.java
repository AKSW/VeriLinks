package org.aksw.verilinks.games.peaInvasion.client.panels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.aksw.verilinks.games.peaInvasion.shared.Configuration;
import org.aksw.verilinks.games.peaInvasion.shared.Linkset;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

public class StartPanel extends TabPanel{

//  private ListBox ontoList;
	private Configuration config;
  private CellTable<Linkset>  linksetTable;
  private SingleSelectionModel<Linkset> selectionModel;
  private SimplePager pager;
	private TextBox nameInput;
  private Button startButton;
  private Button loginButton;
  private Button logoutButton;
  private Button addLinksButton;
  private Button processTaskButton;
  private ToggleButton google;
  private ToggleButton fb;
  private String selectedLogin;
  public final String GOOGLE_LOGIN = "google";
  public final String FACEBOOK_LOGIN = "facebook";
  public final String USERNAME_LOGIN = "username";
  private HighscorePanel highscorePanel;
  private VerticalPanel loginPanel;
  //TODO
  public StartPanel(double barHeight, Unit barUnit, Configuration config) {
    super();
    this.config=config;
    this.getDeckPanel().setStyleName("no-border-style"); 
    DOM.setStyleAttribute(this.getElement(), "backgroundColor", "#39538E");
    DOM.setStyleAttribute(this.getElement(), "paddingTop", "5px");
    DOM.setStyleAttribute(this.getElement(), "border", "1px solid black");

    
    // TODO Auto-generated constructor stub
  }
  
  public void setLinkset(ArrayList<Linkset> list) {
  	// Check if linkset should be shown to kongregate user
  	if(config.isKongregate()){
	  	ArrayList<Linkset> listBuffer = new ArrayList<Linkset>();
	  	Linkset lSet;
	  	for(int i = 0; i<list.size(); i++){
	  		lSet = list.get(i);
	  		if(lSet.getName().contains("dbpedia-factbook") || 
	  					lSet.getName().contains("dbpedia-bbcwildlife") || lSet.getName().contains("dbpedia-linkedgeodata"))
	  			listBuffer.add(lSet);
	  	}
	  	list = listBuffer;
  	}
  	 // Linkset
  	this.linksetTable=new CellTable<Linkset>();
  	selectionModel = new SingleSelectionModel<Linkset>();
    linksetTable.setSelectionModel(selectionModel,
        DefaultSelectionEventManager.<Linkset> createCheckboxManager());
    
  	Column<Linkset, Boolean> checkColumn = new Column<Linkset, Boolean>(
        new CheckboxCell(true, false)) {
      @Override
      public Boolean getValue(Linkset object) {
        // Get the value from the selection model.
        return selectionModel.isSelected(object);
      }
    };
  	linksetTable.addColumn(checkColumn);
  	
    Column<Linkset, String> nameColumn = new Column<Linkset, String>(
        new TextCell()) {
      @Override
      public String getValue(Linkset object) {
        return object.getName();
      }
    };
    linksetTable.addColumn(nameColumn,"Linkset");
    
    Column<Linkset, String> predicateColumn = new Column<Linkset, String>(
        new TextCell()) {
      @Override
      public String getValue(Linkset object) {
        return object.getPredicate();
      }
    };
//    linksetTable.addColumn(predicateColumn,"Relation");
    
    Column<Linkset, String> descrColumn = new Column<Linkset, String>(
        new TextCell()) {
      @Override
      public String getValue(Linkset object) {
        return object.getDescription();
      }
    };
    linksetTable.addColumn(descrColumn,"Description");
    
    Column<Linkset, String> diffColumn = new Column<Linkset, String>(
        new TextCell()) {
      @Override
      public String getValue(Linkset object) {
        return object.getDifficulty();
      }
    };
    linksetTable.addColumn(diffColumn,"Question Difficulty");
    
    linksetTable.setStyleName("linksetTable");
//    DOM.setStyleAttribute(linksetTable.getElement(), "border", "1px dashed black");
//    DOM.setStyleAttribute(linksetTable.getElement(), "padding", "5px");
    
//    linksetTable.setColumnWidth(checkColumn, 1, Unit.PX);
    linksetTable.setColumnWidth(nameColumn, 200, Unit.PX);
  	linksetTable.setColumnWidth(predicateColumn, 200, Unit.PX);
  	linksetTable.setColumnWidth(descrColumn, 350, Unit.PX);
  	linksetTable.setColumnWidth(diffColumn, 50, Unit.PX);
  	linksetTable.setHeight("200px");
  	linksetTable.setWidth("630px");
    linksetTable.setRowData(0, list);
    for (int i =0; i<list.size();i++){
    	System.out.println("linkset: "+list.get(i).getName());
    }
    linksetTable.setRowCount(list.size());
    SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
    pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
//    pager = new SimplePager();
    pager.setDisplay(linksetTable);
    pager.setPageSize(4);
    
  	ListDataProvider<Linkset> provider = new ListDataProvider<Linkset>();
  	provider.addDataDisplay(linksetTable);
  	provider.setList(list);

    
//    for (int i =0; i<list.size();i++){
//    	linksetTable.addColumn(nameColumn,"name");
//    }
  	
  }
  
  public String getName(){
    return this.nameInput.getText();
  }
  
  public Linkset getLinkset() {
//    return this.ontoList.getItemText(ontoList.getSelectedIndex());
//  	return "dbpedia-factbook";
  	Linkset linkset = null;
  	System.out.println("StartPanel: Get selected linkset: "+selectionModel.getSelectedObject());;
  	if(this.selectionModel.getSelectedObject() != null)
  		linkset = this.selectionModel.getSelectedObject();
  	return linkset;
  }
  
  public void setStartBtnClickHandler(ClickHandler c) {
    startButton.addClickHandler(c);
  }
  
  public void setLoginBtnClickHandler(ClickHandler c) {
    loginButton.addClickHandler(c);
  }

  public void setLogoutBtnClickHandler(ClickHandler c) {
    logoutButton.addClickHandler(c);
  }

  
  public void setAddLinksBtnClickHandler(ClickHandler c) {
    addLinksButton.addClickHandler(c);
    
  }
  
  public void setProcessTaskBtnClickHandler(ClickHandler c) {
    processTaskButton.addClickHandler(c);
  }
  
  public void initGUI() {
    // Tab 1
    HTML infoStartGame = new HTML("<b>Start Playing</b>");
    HTML ontoInfo = new HTML("<b>Choose a quiz type:</b>");
//    HTML ontoInfo = new HTML("<a href='#' class='infoBlack'><b>Choose a quiz typ:</b>" +
//		"<span>Choose a linkset</span></a>");
    HTML loginInfoHead = new HTML("<b>Choose login method:<b><br>");
    HTML loginInfoSocial = new HTML("<a href='#' class='infoBlack'><i>[1.Option]</i> Login with your Google or Facebook account.*" +
    		"<span>No information will be posted on your Google<br>or Facebook pages.</span></a>");
    
    startButton = new Button("Start Veri-Links");
    startButton.addStyleName("myButton");
    startButton.setEnabled(false);
    
    loginButton = new Button("Select login");
    loginButton.addStyleName("myButton");
    
    logoutButton = new Button("Logout");
    logoutButton.addStyleName("myButton");
    logoutButton.setEnabled(false);
    logoutButton.setVisible(false);
    
    Image googleImage = new Image("http://www.supportnet.de/articleimage/2398219/01-Google-Logo.jpg");
    googleImage.setStyleName("startPanel_loginImage");
    google = new ToggleButton(googleImage);
    google.setStyleName("image-ToggleButton");
    google.addClickHandler(new ClickHandler(){

      public void onClick(ClickEvent arg0) {
        if(google.isDown()){
          loginButton.setText("Log in with Google account");
          fb.setDown(false);
          selectedLogin=GOOGLE_LOGIN;
        }else {
          loginButton.setText("Select login");
        }
        nameInput.setReadOnly(true);
        nameInput.addStyleName("startPanel_nameInput-disabled");
      }});
    Image fbImage = new Image("Application/images/login/facebook-login.png");
    fbImage.setStyleName("startPanel_loginImage");
    fb = new ToggleButton(fbImage);
    fb.setStyleName("image-ToggleButton");
    fb.addClickHandler(new ClickHandler(){

      public void onClick(ClickEvent arg0) {
        if(fb.isDown()){
          loginButton.setText("Log in with Facebook account");
          google.setDown(false);
          selectedLogin=FACEBOOK_LOGIN;
        }else {
          loginButton.setText("Select login");
        }
        nameInput.setReadOnly(true);
        nameInput.setStyleName("startPanel_nameInput-disabled");
      }});
    
    HorizontalPanel imagePanel = new HorizontalPanel();
    imagePanel.add(google);
    imagePanel.add(fb);
    
    HTML infoName = new HTML("<a href='#' class='infoBlack'><i>[2.Option]</i> Enter your name:*" +
    		"<span>Name has to be at least 3 characters long.</span></a>");
    nameInput= new TextBox();
    nameInput.setReadOnly(true);
    nameInput.setStyleName("startPanel_nameInput-disabled");
    nameInput.addMouseDownHandler(new MouseDownHandler(){

			public void onMouseDown(MouseDownEvent arg0) {
				fb.setDown(false);
				google.setDown(false);
				nameInput.setReadOnly(false);
				nameInput.setStyleName("startPanel_nameInput");
				loginButton.setText("Enter your username");
				selectedLogin=USERNAME_LOGIN;
			}});
    nameInput.addKeyPressHandler(new KeyPressHandler(){

			public void onKeyPress(KeyPressEvent event) {
				System.out.println("keyyy: "+nameInput.getText().length());
				if(nameInput.getText().length()>1)
					loginButton.setText("Login");
				else
					loginButton.setText("Enter your username");
			}});
    
    
    // LoginPanel
    VerticalPanel socialLoginPanel = new VerticalPanel();
    socialLoginPanel.add(loginInfoSocial);
    socialLoginPanel.add(imagePanel);
    
    VerticalPanel normalLoginPanel = new VerticalPanel();
    normalLoginPanel.add(infoName);
    normalLoginPanel.add(nameInput);
    normalLoginPanel.setStyleName("normalLoginPanel");
    DOM.setStyleAttribute(normalLoginPanel.getElement(), "marginLeft", "100px");
    
    HorizontalPanel hLoginPanel = new HorizontalPanel();
    hLoginPanel.add(socialLoginPanel);
    hLoginPanel.add(normalLoginPanel);
    hLoginPanel.addStyleName("startPanel_hLoginPanel");
    DOM.setStyleAttribute(hLoginPanel.getElement(), "border", "1px solid grey");
    DOM.setStyleAttribute(hLoginPanel.getElement(), "padding", "10px");
    DOM.setStyleAttribute(hLoginPanel.getElement(), "marginBottom", "5px");
    DOM.setStyleAttribute(hLoginPanel.getElement(), "marginTop", "8px");
//    DOM.setStyleAttribute(hLoginPanel.getElement(), "backgroundColor", "#F1F1F2");

   
    loginPanel = new VerticalPanel();
    loginPanel.add(loginInfoHead);
    loginPanel.add(hLoginPanel);
    
    HorizontalPanel loginButtonPanel = new HorizontalPanel();
    loginButtonPanel.add(loginButton);
    loginButtonPanel.add(logoutButton);
    
    // Linkset
    VerticalPanel linksetPanel = new VerticalPanel();
    linksetPanel.setStyleName("startPanel_linksetPanel");
    linksetPanel.add(linksetTable);
    linksetPanel.add(pager);
    DOM.setStyleAttribute(linksetPanel.getElement(), "border", "1px solid grey");
    DOM.setStyleAttribute(linksetPanel.getElement(), "padding", "5px");
    DOM.setStyleAttribute(linksetPanel.getElement(), "backgroundColor", "white");
    
    VerticalPanel infoPanelGame = new VerticalPanel();
    infoPanelGame.setSpacing(8);
    infoPanelGame.add(ontoInfo);
    infoPanelGame.add(linksetPanel);
//    infoPanelGame.add(ontoList);
//    infoPanelGame.add(linksetTable);
//    infoPanelGame.add(pager);
    //infoPanelGame.add(infoName);
    //infoPanelGame.add(nameInput);
//    infoPanelGame.add(loginInfo);
//    infoPanelGame.add(imagePanel);
//    infoPanelGame.add(normalLoginPanel);
    if(config.getLoginNeeded()){
	    infoPanelGame.add(loginPanel);
	    infoPanelGame.add(loginButtonPanel);
    }
    infoPanelGame.add(startButton);
    
    VerticalPanel vPanel = new VerticalPanel();
    //vPanel.add(infoStartGame);
    vPanel.add(infoPanelGame);
    
    HorizontalPanel gamePanel = new HorizontalPanel();
    gamePanel.add(vPanel);
    gamePanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
    gamePanel.add(highscorePanel);

    
    
    // Tab 2
    HTML head = new HTML("<b>Start Admin Panel</b>");
    addLinksButton = new Button("Add Links");
    addLinksButton.addStyleName("myButton");
    processTaskButton = new Button("Process Link Tasks");
    processTaskButton.addStyleName("myButton");
    VerticalPanel infoPanel = new VerticalPanel();
    HTML summary = new HTML("You can verify the semantic web links that are already in our database. " +
    		"But you can also upload your own links! Just add a rdf or a n-triple file containing your links to our server");
    summary.setStyleName("startPanel_summary");
    DOM.setStyleAttribute(summary.getElement(), "padding", "3px");
    DOM.setStyleAttribute(summary.getElement(), "border", "1px dashed lightgray");
    DOM.setStyleAttribute(summary.getElement(), "border", "1px dashed gray");
    
    HTML infoAddLinks = new HTML("[Add Link Task]<br>" +
    		"Add a new link task to the Veri-Links database. " +
    		"Upload a rdf or n-triple file containing the semantic web links. " +
    		"And specify the data of the interlinked ontologies");
    HTML infoProcessTasks = new HTML("[Process Link Tasks]<br>" +
        "Process the pending links tasks, new links will be added to the Veri-Links database.");
    infoPanel.add(summary);
    infoPanel.add(infoAddLinks);
    infoPanel.add(addLinksButton);
    
    infoPanel.add(infoProcessTasks);
    infoPanel.add(processTaskButton);
    infoPanel.setSpacing(8);
    VerticalPanel adminPanel = new VerticalPanel();
    DOM.setStyleAttribute(adminPanel.getElement(), "backgroundColor", "white");
    DOM.setStyleAttribute(adminPanel.getElement(), "padding", "10px");
    
    //adminPanel.setStyleName("Menu_AdminPanel");
    adminPanel.add(head);
    adminPanel.add(infoPanel);
    adminPanel.setWidth("400px");
    
    // TabPanel
//    TabPanel tabPanel = this;
    
    DOM.setStyleAttribute(gamePanel.getElement(), "backgroundColor", "white");
    DOM.setStyleAttribute(highscorePanel.getElement(), "border", "1px solid grey");
    DOM.setStyleAttribute(gamePanel.getElement(), "padding", "5px");
    this.add(gamePanel, "Game");
    if(config.getKnowledgeMode().equals(Configuration.KNOWLEDGE_EXPERT))
    	this.add(adminPanel, "Admin");
    
//    tabPanel.addStyleName("noBorder");
//    tabPanel.setWidth("900px");
//    tabPanel.setHeight("500px");
    this.selectTab(0);
  }


  public boolean isEmpty() {
//    if (ontoList.getItemCount()==0)
//      return true;
//    else
//      return false;
  	System.out.println("linkset count: "+linksetTable.getRowCount());
  	if (linksetTable.getRowCount()==0)
      return true;
    else
      return false;
  }
  
  public Button getLoginButton(){
    return this.loginButton;
  }
  
  public Button getLogoutButton(){
    return this.logoutButton;
  }
  
  public void disableLogin(String userName){
    loginButton.setText("Logged in as '"+userName+"'");
    loginButton.setEnabled(false);
    logoutButton.setEnabled(true);
    logoutButton.setVisible(true);
    logoutButton.setText("Logout '"+userName+"'");
    google.setEnabled(false);
    fb.setEnabled(false);
    nameInput.setText("");
    nameInput.setEnabled(false);
    nameInput.addStyleName("startPanel_nameInput-disabled");
    loginPanel.setStyleName("startPanel_loginPanel-disabled");
  }
  
  public void enableLogin(){
  	startButton.setEnabled(false);
    loginButton.setText("Select Login");
    loginButton.setEnabled(true);
    logoutButton.setEnabled(false);
    logoutButton.setVisible(false);
    logoutButton.setText("Logout");
    google.setEnabled(true);
    fb.setEnabled(true);
    nameInput.setText("");
    nameInput.setEnabled(true);
    nameInput.addStyleName("startPanel_nameInput");
    loginPanel.setStyleName("startPanel_loginPanel");
  }
  
  
  public void enableStart(){
    this.startButton.setEnabled(true);
  }

  public String getSelectedLogin() {
    return this.selectedLogin;
  }
  
  public boolean loginSelected(){
  	if (this.fb.isDown() || this.google.isDown() )
  		return true;
  	else if( this.nameInput.isReadOnly()==false){
  		if(this.nameInput.getText().length()<3){
	  		Window.setStatus("Invalid UserName");
	      Window.alert("UserName has to be at least 3 characters long!");
	  		return false;
  		}else
  			return true;
  	}else 
  		return false;
  		
  }

  public String getEnteredName(){
  	return this.nameInput.getText();
  }
	public void setHighscorePanel(HighscorePanel highscorePanel) {
		this.highscorePanel=highscorePanel;
	}
}
