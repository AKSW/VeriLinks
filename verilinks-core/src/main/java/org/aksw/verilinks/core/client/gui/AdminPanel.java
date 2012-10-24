package org.aksw.verilinks.core.client.gui;

import org.aksw.verilinks.core.shared.PropertyConstants;
import org.aksw.verilinks.core.shared.msg.Template;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;


public class AdminPanel extends VerticalPanel {
  public FormPanel form;

  private String errorMsg ="\n";
  
  private static final String UPLOAD_ACTION_URL = GWT.getModuleBaseURL()
      + "upload";
  /**
   * The message displayed to the user when the server cannot be reached or
   * returns an error.
   */
  private static final String SERVER_ERROR = "An error occurred while "
      + "attempting to contact the server. Please check your network "
      + "connection and try again.";

  public  final static String LINKSET = "linksetElement";
  public  final static String NAME = "nameElement";
  public  final static String ENDPOINT = "endpointElement";
  public  final static String PROP0 = "prop0Element";
  public  final static String PROP1 = "prop1Element";
  public  final static String PROP2 = "prop2Element";
  public  final static String PROP3 = "prop3Element";
  public  final static String IMAGE = "imageElement";
  public  final static String TYPE = "typeElement";
  
  public  final static String NAME_2 = "nameElement2";
  public  final static String ENDPOINT_2 = "endpointElement2";
  public  final static String PROP0_2 = "prop0Element2";
  public  final static String PROP1_2 = "prop1Element2";
  public  final static String PROP2_2 = "prop2Element2";
  public  final static String PROP3_2 = "prop3Element2";
  public  final static String IMAGE_2 = "imageElement2";
  public  final static String TYPE_2 = "typeElement2";
  
  public  final static String FILE = "fileElement";
  public  final static String DIFFICULTY = "difficultyElement";
  
  public  final static String PREDICATE = "predicateElement";
  public  final static String DESCRIPTION = "descriptionElement";
  
  private TextBox linksetBox;
  
  private TextBox nameBox;
  private String endpoint;
  private TextBox endpointBox;
  private String prop0;
  private TextBox prop0Box;
  private String prop1;
  private TextBox prop1Box;
  private String prop2;
  private TextBox prop2Box;
  private String prop3;
  private TextBox prop3Box;
  private String image;
  private TextBox imageBox;
  private ListBox typeBox;
  private CheckBox cb0_1;
  private CheckBox cb1_1;
  private CheckBox cb2_1;
  private CheckBox cb3_1;
  private CheckBox cbImage_1;
  
//  private TextBox longBox;
//  private TextBox langBox;
  
  
  private TextBox nameBox2;
  private TextBox endpointBox2;
  private TextBox prop0Box2;
  private TextBox prop1Box2;
  private TextBox prop2Box2;
  private TextBox prop3Box2;
  private TextBox imageBox2;
  private ListBox typeBox2;
  private CheckBox cb0_2;
  private CheckBox cb1_2;
  private CheckBox cb2_2;
  private CheckBox cb3_2;
  private CheckBox cbImage_2;
//  private TextBox longBox2;
//  private TextBox langBox2;
//  
  private CheckBox checkBoxTemplate;
  
  private Button addBtn;
  private Button quitBtn;
  private Button showTemplateBtn;
  private FileUpload upload;
  
  private ListBox difficultyBox;
  
  private TextBox predicateBox;
  private TextArea descriptionBox;
  
  public AdminPanel() {
    // Create a FormPanel and point it at a service.
    form = new FormPanel();
    form.setAction(UPLOAD_ACTION_URL);

    // Because we're going to add a FileUpload widget, we'll need to set the
    // form to use the POST method, and multipart MIME encoding.
    form.setEncoding(FormPanel.ENCODING_MULTIPART);
    form.setMethod(FormPanel.METHOD_POST);

    // Linkset
    HTML linksetHeadLabel = new HTML("<b><i>2.Linkset</i></b>");
    HTML linksetDescrLabel = new HTML("ID of linkset");
    linksetHeadLabel.addStyleName("AdminPanel_Label");
    linksetDescrLabel.addStyleName("AdminPanel_Label");
    linksetBox = new TextBox();
    linksetBox.setStyleName("AdminPanel_Box");
    DOM.setStyleAttribute(linksetBox.getElement(), "width", "310px");
    linksetBox.setName(LINKSET);
    
    VerticalPanel linksetPanel = new VerticalPanel();
    linksetPanel.add(linksetHeadLabel);
    linksetPanel.add(linksetDescrLabel);
    linksetPanel.add(linksetBox);
    DOM.setStyleAttribute(linksetPanel.getElement(), "marginLeft", "40px");
    
    // Create a FileUpload widget.
    HTML uploadDescribtionLabel = new HTML("Upload rdf file");
    //  uploadDescribtionLabel.addStyleName("AdminPanel_Spacing");
    VerticalPanel uploadPanel = new VerticalPanel();
    uploadPanel.setStyleName("adminPanel_upload");
    upload = new FileUpload();
    upload.setName(FILE);
    uploadPanel.add(new HTML("<b><i>1.Resource File</i></b>"));
    uploadPanel.add(uploadDescribtionLabel);
    uploadPanel.add(upload);
//    uploadPanel.addStyleName("adminPanel_margin");
    DOM.setStyleAttribute(uploadPanel.getElement(), "marginLeft", "110px");
    
    
    // Create a panel to hold all of the form widgets.
    checkBoxTemplate = new CheckBox("Template already exists");
    checkBoxTemplate.addClickHandler(new ClickHandler(){

      public void onClick(ClickEvent event) {
        boolean check = ((CheckBox)event.getSource()).isChecked();
        boolean bool = false;
        if (check) {
          bool=false;
        }
        else {
          bool =true;
        }
        //nameBox.setEnabled(bool);
        endpointBox.setEnabled(bool);
        prop0Box.setEnabled(bool);
        prop1Box.setEnabled(bool);
        prop2Box.setEnabled(bool);
        prop3Box.setEnabled(bool);
        imageBox.setEnabled(bool);
        typeBox.setEnabled(bool);
        
        endpointBox2.setEnabled(bool);
        prop0Box2.setEnabled(bool);
        prop1Box2.setEnabled(bool);
        prop2Box2.setEnabled(bool);
        prop3Box2.setEnabled(bool);
        imageBox2.setEnabled(bool);
        typeBox2.setEnabled(bool);
        
        // both selected
//        if(!check || !checkBoxObject.isChecked())
//        	bool=true;
//        else 
//        	bool=false;
//        difficultyBox.setEnabled(bool);
      }
      
    });
    
    showTemplateBtn = new Button("Show template file");
    showTemplateBtn.addStyleName("myButton");
    DOM.setStyleAttribute(showTemplateBtn.getElement(), "marginLeft", "20px");
    VerticalPanel vCheckBoxPanel = new VerticalPanel();
    vCheckBoxPanel.add(checkBoxTemplate);
//    vCheckBoxPanel.add(checkBoxObject);
    
    HorizontalPanel checkBoxPanel = new HorizontalPanel();
    checkBoxPanel.add(vCheckBoxPanel);
    checkBoxPanel.setVerticalAlignment(ALIGN_MIDDLE);
    checkBoxPanel.add(showTemplateBtn);
    checkBoxPanel.addStyleName("adminPanel_checkBoxPanel");
    
    HorizontalPanel upperPanel = new HorizontalPanel();
    upperPanel.add(checkBoxPanel);
    upperPanel.add(uploadPanel);
    upperPanel.add(linksetPanel);
    
    VerticalPanel panelLeft = initLeftGUI();
    VerticalPanel panelRight = initRightGUI();
    VerticalPanel panelMiddle = initMiddleGUI();


    VerticalPanel main = new VerticalPanel();
    HorizontalPanel horizontal = new HorizontalPanel();
    horizontal.add(panelLeft);
    horizontal.setSpacing(15);
    horizontal.add(panelMiddle);    
    horizontal.add(panelRight);
    
    HTML descriptionLabel = new HTML("Add new linkset to Veri-Links:");
    descriptionLabel.addStyleName("adminPanel_headerText");
    
   
    // Add a 'submit' button.
    addBtn = new Button("Add links to database", new ClickHandler() {
      public void onClick(ClickEvent event) {
        form.submit();
      }
    });
    //addBtn.setStyleName("adminPanel_addBtn");
    addBtn.addStyleName("myButton");
    
    quitBtn = new Button("Quit", new ClickHandler() {
      public void onClick(ClickEvent event) {
        
      }
    });
    //quitBtn.setStyleName("adminPanel_quitBtn");
    quitBtn.addStyleName("myButton");
    
    HorizontalPanel headerPanel=new HorizontalPanel();
    headerPanel.addStyleName("adminPanel_header");
    headerPanel.add(descriptionLabel);
    headerPanel.add(quitBtn);
    //main.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    //main.add(quitBtn);
    //main.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_DEFAULT);
    //main.add(describtionLabel);
    
    VerticalPanel btnPanel = new VerticalPanel();
    btnPanel.setHorizontalAlignment(ALIGN_CENTER);
    btnPanel.setSpacing(5);
    btnPanel.add(addBtn);
    main.add(headerPanel);
    main.add(upperPanel);
    //main.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
    main.add(horizontal);
//    main.add(difficultyPanel);
//    main.add(uploadPanel);
//    main.add(bottomPanel);
    main.add(btnPanel);   
    form.setWidget(main);

    initHandler();

    add(form);

  }

  

	public VerticalPanel initLeftGUI() {
    VerticalPanel panel = new VerticalPanel();
    
    // Template attributes and uris
    nameBox = new TextBox();
    nameBox.setStyleName("AdminPanel_Box");
    nameBox.setName(NAME);
    
    endpointBox = new TextBox();
    endpointBox.setStyleName("AdminPanel_Box");
    endpointBox.setName(ENDPOINT);
    
    prop0Box = new TextBox();
    prop0Box.setStyleName("AdminPanel_Box");
    prop0Box.setName(PROP0);
    
    prop1Box = new TextBox();
    prop1Box.setStyleName("AdminPanel_Box");
    prop1Box.setName(PROP1);
    
    prop2Box = new TextBox();
    prop2Box.setStyleName("AdminPanel_Box");
    prop2Box.setName(PROP2);
    
    prop3Box = new TextBox();
    prop3Box.setStyleName("AdminPanel_Box");
    prop3Box.setName(PROP3);
    
    imageBox = new TextBox();
    imageBox.setStyleName("AdminPanel_Box");
    imageBox.setName(IMAGE);

    typeBox = new ListBox();
    fillTypeBox(typeBox);
    typeBox.setName(TYPE);
    
    HTML nameLabel = new HTML("Ontology Name");
    nameLabel.addStyleName("AdminPanel_Label");
    HTML endpointLabel = new HTML("SPARQL Endpoint");
    endpointLabel.addStyleName("AdminPanel_Label");
    HTML prop0Label = new HTML("Property #0 URI");
    prop0Label.addStyleName("AdminPanel_Label");
    HTML prop1Label = new HTML("Property #1 URI");
    prop1Label.addStyleName("AdminPanel_Label");
    HTML prop2Label = new HTML("<i>Property #2 URI (Optional)</i>");
    prop2Label.addStyleName("AdminPanel_Label");
    HTML prop3Label = new HTML("<i>Property #3 URI (Optional)</i>");
    prop3Label.addStyleName("AdminPanel_Label");
    HTML imageLabel = new HTML("<i>Image URI (Optional)</i>");
    imageLabel.addStyleName("AdminPanel_Label");
    HTML typeLabel = new HTML("<a href='#' class='infoBlack'>Select type of instance*" +
        "<span>A 'mapType' instance will be displayed as a map.<br>A 'normalType' instance will be displayed as plain text.</span></a>");
    typeLabel.addStyleName("AdminPanel_Label");
    
    // CB
    cb0_1 = new CheckBox();
    cb1_1 = new CheckBox();
    cb2_1 = new CheckBox();
    cb3_1 = new CheckBox();
    cbImage_1 = new CheckBox();
    
    HorizontalPanel p0_1 = new HorizontalPanel();
    HorizontalPanel p1_1 = new HorizontalPanel();
    HorizontalPanel p2_1 = new HorizontalPanel();
    HorizontalPanel p3_1 = new HorizontalPanel();
    HorizontalPanel pImage_1 = new HorizontalPanel();
    
    p0_1.add(prop0Box);
    p0_1.add(cb0_1);
    p1_1.add(prop1Box);
    p1_1.add(cb1_1);
    p2_1.add(prop2Box);
    p2_1.add(cb2_1);
    p3_1.add(prop3Box);
    p3_1.add(cb3_1);
    pImage_1.add(imageBox);
    pImage_1.add(cbImage_1);
    
    
    //panel.add(space);
    panel.add(new HTML("<strong><i>4.Subject</i></strong>"));
    panel.add(nameLabel);
    panel.add(nameBox);
    panel.add(endpointLabel);
    panel.add(endpointBox);
    panel.add(prop0Label);
    panel.add(p0_1);
    
    HorizontalPanel h_1 = new HorizontalPanel();
    h_1.add(new Button("Add Property"));
    h_1.add(new Button("Remove Property"));
    
    panel.add(h_1);
    
//    panel.add(prop1Label);
//    panel.add(p1_1);
//    panel.add(prop2Label);
//    panel.add(p2_1);
//    panel.add(prop3Label);
//    panel.add(p3_1);
//    panel.add(imageLabel);
//    panel.add(pImage_1);
    panel.add(typeLabel);
    panel.add(typeBox);
    //panel.add(boxPanel);
    
    return panel;
    
    
    
  }
  
  public VerticalPanel initRightGUI() {
    nameBox2 = new TextBox();
    nameBox2.setStyleName("AdminPanel_Box");
    nameBox2.setName(NAME_2);
    
    endpointBox2 = new TextBox();
    endpointBox2.setStyleName("AdminPanel_Box");
    endpointBox2.setName(ENDPOINT_2);
    
    prop0Box2 = new TextBox();
    prop0Box2.setStyleName("AdminPanel_Box");
    prop0Box2.setName(PROP0_2);
    
    prop1Box2 = new TextBox();
    prop1Box2.setStyleName("AdminPanel_Box");
    prop1Box2.setName(PROP1_2);
    
    prop2Box2 = new TextBox();
    prop2Box2.setStyleName("AdminPanel_Box");
    prop2Box2.setName(PROP2_2);
    
    prop3Box2 = new TextBox();
    prop3Box2.setStyleName("AdminPanel_Box");
    prop3Box2.setName(PROP3_2);
    
    imageBox2 = new TextBox();
    imageBox2.setStyleName("AdminPanel_Box");
    imageBox2.setName(IMAGE_2);
    
    typeBox2= new ListBox();
    fillTypeBox(typeBox2);
    typeBox2.setName(TYPE_2);
    
    HTML nameLabel = new HTML("Ontology Name");
    nameLabel.addStyleName("AdminPanel_Label");
    HTML endpointLabel = new HTML("SPARQL Endpoint");
    endpointLabel.addStyleName("AdminPanel_Label");
    HTML prop0Label = new HTML("Property #0 URI");
    prop0Label.addStyleName("AdminPanel_Label");
    HTML prop1Label = new HTML("Property #1 URI");
    prop1Label.addStyleName("AdminPanel_Label");
    HTML prop2Label = new HTML("<i>Property #2 URI (Optional)</i>");
    prop2Label.addStyleName("AdminPanel_Label");
    HTML prop3Label = new HTML("<i>Property #3 URI (Optional)</i>");
    prop3Label.addStyleName("AdminPanel_Label");
    HTML imageLabel = new HTML("<i>Image URI (Optional)</i>");
    imageLabel.addStyleName("AdminPanel_Label");
    HTML typeLabel = new HTML("<a href='#' class='infoBlack'>Select type of instance*" +
    "<span>A 'mapType' instance will be displayed as a map.<br>A 'normalType' instance will be displayed in plain text.</span></a>");
    typeLabel.addStyleName("AdminPanel_Label");
    
   
    cb0_2 = new CheckBox();
    cb1_2 = new CheckBox();
    cb2_2 = new CheckBox();
    cb3_2 = new CheckBox();
    cbImage_2 = new CheckBox();
    
    HorizontalPanel p0_2 = new HorizontalPanel();
    HorizontalPanel p1_2 = new HorizontalPanel();
    HorizontalPanel p2_2 = new HorizontalPanel();
    HorizontalPanel p3_2 = new HorizontalPanel();
    HorizontalPanel pImage_2 = new HorizontalPanel();
    
    p0_2.add(prop0Box2);
    p0_2.add(cb0_2);
    p1_2.add(prop1Box2);
    p1_2.add(cb1_2);
    p2_2.add(prop2Box2);
    p2_2.add(cb2_2);
    p3_2.add(prop3Box2);
    p3_2.add(cb3_2);
    pImage_2.add(imageBox2);
    pImage_2.add(cbImage_2);
    
    
  
    VerticalPanel panel = new VerticalPanel();
    panel.add(new HTML("<strong><i>5.Object</i></strong>"));
    panel.add(nameLabel);
    panel.add(nameBox2);
    panel.add(endpointLabel);
    panel.add(endpointBox2);
    panel.add(prop0Label);
    panel.add(p0_2);
    
    HorizontalPanel h_2 = new HorizontalPanel();
    h_2.add(new Button("Add Property"));
    h_2.add(new Button("Remove Property"));
    
    panel.add(h_2);
//    panel.add(prop1Label);
//    panel.add(p1_2);
//    panel.add(prop2Label);
//    panel.add(p2_2);
//    panel.add(prop3Label);
//    panel.add(p3_2);
//    panel.add(imageLabel);
//    panel.add(pImage_2);
    panel.add(typeLabel);
    panel.add(typeBox2);
//    panel.add(uploadPanel);
    /*
    //nameBox2.setEnabled(false);
    endpointBox2.setEnabled(false);
    prop0Box2.setEnabled(false);
    prop1Box2.setEnabled(false);
    prop2Box2.setEnabled(false);
    prop3Box2.setEnabled(false);
    imageBox2.setEnabled(false);
    */
    return panel;
    
  }
  
  private VerticalPanel initMiddleGUI(){

    VerticalPanel difficultyPanel = new VerticalPanel();
    difficultyBox = new ListBox();
    difficultyBox.setName(DIFFICULTY);
    fillDifficultyBox(difficultyBox);
    HTML difficultyLabel = new HTML("Choose game difficulty for this linkset");
    difficultyLabel.addStyleName("adminPanel_label");
    difficultyPanel.add(new HTML("<b><i>7.Difficulty</i></b>"));
    difficultyPanel.add(difficultyLabel);
    difficultyPanel.add(difficultyBox);
//    difficultyPanel.addStyleName("adminPanel_margin");
    
    
    // Predicate
    this.predicateBox=new TextBox();
    predicateBox.setName(PREDICATE);
    predicateBox.setWidth("250px");
    VerticalPanel predicatePanel = new VerticalPanel();
    predicatePanel.add(new HTML("<b><i>3.Predicate</i><b>"));
    predicatePanel.add(new HTML("Name of the relation"));
    predicatePanel.add(predicateBox);
    
    // Description
    this.descriptionBox=new TextArea();
    this.descriptionBox.setName(DESCRIPTION);
    this.descriptionBox.setWidth("250px");
    this.descriptionBox.setHeight("100px");
    VerticalPanel descPanel = new VerticalPanel();
    descPanel.add(new HTML("<b><i>6.Description</i></b>"));
    descPanel.add(new HTML("Short comment on the linkset"));
    descPanel.add(descriptionBox);
    DOM.setStyleAttribute(descPanel.getElement(), "margin", "20px 0px");
    
    VerticalPanel panel= new VerticalPanel();
    panel.add(predicatePanel);
    panel.add(descPanel);
    panel.add(difficultyPanel);
    
    
    
    return panel;
  }
  
  private void initHandler(){
    
 // Add an event handler to the form.
    form.addSubmitHandler(new FormPanel.SubmitHandler() {
      public void onSubmit(SubmitEvent event) {
        // This event is fired just before the form is submitted. We can
        // take this opportunity to perform validation.
        if (okay()==false) 
        {
          Window.alert("Please correct/fill in the following field/s: "+ errorMsg);
          errorMsg="\n";
          event.cancel();
        } else {
          Window.alert("Client: Sending information to server.");
          //disable();
        }
      }
    });

    form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
      public void onSubmitComplete(SubmitCompleteEvent event) {
        // When the form submission is successfully completed, this
        // event is fired. Assuming the service returned a response of type
        // text/html, we can get the result text here (see the FormPanel
        // documentation for further explanation).
        Window.alert(event.getResults());
        addBtn.setText("Done");
        addBtn.setEnabled(false);
      }
    });
  }
  
  public boolean okay() {
    boolean status = true;
     
    if(nameBox.getText().isEmpty()) {
      status =false;
      errorMsg+="\nName";
    }
    if(!checkBoxTemplate.getValue()) {
      if(endpointBox.getText().isEmpty()) {
        status =false;
        errorMsg+="\nEndpoint";
      }
      if(prop0Box.getText().isEmpty()) {
        status =false;
        errorMsg+="\nProperty0";
      }
      if(prop1Box.getText().isEmpty()) {
        status =false;
        errorMsg+="\nProperty1";
      }
      int selected = typeBox.getSelectedIndex();
      if(typeBox.getItemText(selected).isEmpty()) {
        status =false;
        errorMsg+="\nType[1]";
      }
      if(endpointBox2.getText().isEmpty()) {
        status =false;
        errorMsg+="\nEndpoint[2]";
      }
      if(prop0Box2.getText().isEmpty()) {
        status =false;
        errorMsg+="\nProperty1[2]";
      }
      if(prop1Box2.getText().isEmpty()) {
        status =false;
        errorMsg+="\nProperty1[2]";
      }
      selected = typeBox2.getSelectedIndex();
      if(typeBox2.getItemText(selected).isEmpty()) {
        status =false;
        errorMsg+="\nType[2]";
      }
    }
    if(nameBox2.getText().isEmpty()) {
      status =false;
      errorMsg+="\nName[2]";
    }
    
    if(!( upload.getFilename().endsWith(".xml") || upload.getFilename().endsWith(".nt")) ) {
      status=false;
      errorMsg+="\n\nInvalid file format. ";
      System.out.println("name: "+upload.getFilename());
    }
    return status;
  }
  
  private void disable() {
    this.nameBox.setReadOnly(true);
    this.endpointBox.setReadOnly(true);
    this.endpointBox.setStyleName("AdminPanel_Box-readOnly");
    this.prop0Box.setReadOnly(true);
    this.prop0Box.setStyleName("AdminPanel_Box-readOnly");
    this.prop1Box.setReadOnly(true);
    this.prop1Box.setStyleName("AdminPanel_Box-readOnly");
    this.prop2Box.setReadOnly(true);
    this.prop2Box.setStyleName("AdminPanel_Box-readOnly");
    this.prop3Box.setReadOnly(true);
    this.prop3Box.setStyleName("AdminPanel_Box-readOnly");
    this.imageBox.setReadOnly(true);
    this.imageBox.setStyleName("AdminPanel_Box-readOnly");
    this.addBtn.setEnabled(false);
    //this.addBtn.setText("Done");
    this.upload.setEnabled(false);
    this.predicateBox.setReadOnly(true);
    this.predicateBox.setStyleName("AdminPanel_Box-readOnly");
    this.descriptionBox.setReadOnly(true);
    this.descriptionBox.setStyleName("AdminPanel_Box-readOnly");
    
  }
  
  public void setQuitClickHandler(ClickHandler handler) {
    this.quitBtn.addClickHandler(handler);
  }
  
  public Template getTemplate(){
    Template temp = new Template();
    temp.setName(nameBox.getText());
    temp.setEndpoint(endpointBox.getText());
    temp.setProp0(prop0Box.getText());
    if (!prop1Box.getText().isEmpty())
      temp.setProp1(prop1Box.getText());
    if (!prop2Box.getText().isEmpty())
      temp.setProp2(prop2Box.getText());
    if (!prop3Box.getText().isEmpty())
      temp.setProp3(prop3Box.getText());
    if (!imageBox.getText().isEmpty())
      temp.setImage(imageBox.getText());
    
    return temp;
    
  }
  
  private void fillTypeBox(ListBox list) {
  	list.addItem("");
  	list.addItem(PropertyConstants.TEMPLATE_TYPE_NORMAL);
  	list.addItem(PropertyConstants.TEMPLATE_TYPE_MAP);
  }
  
  private void fillDifficultyBox(ListBox list) {
		list.addItem(PropertyConstants.DIFFICULTY_EASY);
		list.addItem(PropertyConstants.DIFFICULTY_MEDIUM);
		list.addItem(PropertyConstants.DIFFICULTY_HARD);
	}
}
