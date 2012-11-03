package org.aksw.verilinks.games.peaInvasion.client.verify;

import org.aksw.verilinks.games.peaInvasion.shared.Configuration;
import org.aksw.verilinks.games.peaInvasion.shared.PropertyConstants;
import org.aksw.verilinks.games.peaInvasion.shared.Template;
import org.aksw.verilinks.games.peaInvasion.shared.templates.TemplateInstance;

import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapTypeId;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.events.MapEventType;
import com.google.gwt.maps.client.events.MapHandlerRegistration;
import com.google.gwt.maps.client.events.MouseEvent;
import com.google.gwt.maps.client.events.click.ClickMapEvent;
import com.google.gwt.maps.client.events.click.ClickMapHandler;
import com.google.gwt.maps.client.events.closeclick.CloseClickMapEvent;
import com.google.gwt.maps.client.events.closeclick.CloseClickMapHandler;
import com.google.gwt.maps.client.overlays.InfoWindow;
import com.google.gwt.maps.client.overlays.InfoWindowOptions;
import com.google.gwt.maps.client.overlays.Marker;
import com.google.gwt.maps.client.overlays.MarkerOptions;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class InstancePanel extends FlowPanel{

	private Configuration config;
  private HTML uri;
  private HTML uriHTML;
  private HTML label;
  private String rawLabel=null;
  
  private HTML type;
  private String specificUri;
  private String specific;
  private String image;
  private VerticalPanel imagePanel;
  private boolean subject;
  private TemplateInstance template;
  private double latitude;
  private double longitude;
  private VerticalPanel mapInfoPanel;
  //div element subject
	// top: 438px
	// left: 38px
	public final int top = 438;
	public final int leftObject = 718;
	public final int leftSubject = 10;
  
  /**
   * 
   * @param temp template
   * @param subject if instance is subject then true, else false
   */
  public InstancePanel(TemplateInstance temp, boolean subject, Configuration config) {
    super();
    this.template=temp;
    this.config=config;
    this.uri = new HTML("<b>URI</b>");
    this.label = new HTML("<b>"+temp.getProperties().get(1).getProperty()+"</b>");
    this.type = new HTML("<b>"+temp.getProperties().get(2).getProperty()+"</b>");
    this.specificUri = "<b>Specific Uri</b>";
    this.specific = "";
    this.subject=subject;
    this.latitude=0;
    this.longitude=0;
    this.imagePanel=new VerticalPanel();
    echo("label: "+label);
    echo("rawLabel: "+rawLabel);
    echo("tempType: "+temp.getType());
    
    if(temp.getType().equals(PropertyConstants.TEMPLATE_TYPE_MAP))
    	initMapGUI();
    else
    	initGUI();
  }

	private void echo(String string) {
		System.out.println("##InstancePanel: "+string);
	}

	public InstancePanel(String uri, String label, String type, 
      String specificUri, String specific){
    super();
    this.setUri(uri); 
    this.setLabel(label);
    this.setType(type);
    this.setSpecificUri(specificUri);
    this.setSpecific(specific);
    this.imagePanel=new VerticalPanel();
    initGUI();
  }
  
//	native void initializeMap(boolean isSubject, double latitude, double longitude) /*-{
//  var latlng = new $wnd.google.maps.LatLng(latitude, longitude);
//  var myOptions = {
//    zoom: 8,
//    center: latlng,
//    mapTypeId: $wnd.google.maps.MapTypeId.ROADMAP
//  };
//  var subject = isSubject;
//  if( subject)
//  	var part= "subject";
//  else
//  	var part = "object";
//  var div = $doc.getElementById(part);
//  div.style.width="550px";
//  div.style.height="300px";
//  
//  var map = new $wnd.google.maps.Map(div, myOptions);
//}-*/;
//	
	
	private void initializeMap(boolean subject, double latitude, double longitude){
		LatLng CENTER = LatLng.newInstance(latitude, longitude);

		final MapOptions options = MapOptions.newInstance();
    // Zoom level. Required
//    options.setZoom(Integer.parseInt(template.getZoom())); // hurr
		options.setZoom(2); // hurr
    
    // Open a map centered on Cawker City, KS USA. Required
    options.setCenter(CENTER);
    // Map type. Required.
    options.setMapTypeId(MapTypeId.HYBRID);
    
    // Enable maps drag feature. Disabled by default.
    options.setDraggable(true);
    // Enable and add default navigation control. Disabled by default.
//    options.setNavigationControl(true);
    // Enable and add map type control. Disabled by default.
    options.setMapTypeControl(true);
    final MapWidget mapWidget = new MapWidget(options);
    mapWidget.setSize("550px", "300px");
    
    MarkerOptions markerOptions = MarkerOptions.newInstance();
    markerOptions.setPosition(CENTER);
    markerOptions.setTitle(parseLabel(this.rawLabel));
    
    final Marker marker = Marker.newInstance(markerOptions);
    marker.setMap(mapWidget);
    
    marker.addClickHandler(new ClickMapHandler() {
      public void onEvent(ClickMapEvent event) {
        drawInfoWindow(marker, event.getMouseEvent(),mapWidget);
      }
    });
    
    String part=null;
    if(subject)
    	part= "subject";
    else
    	part = "object";
    
    // Panels
    mapInfoPanel = new VerticalPanel();
    mapInfoPanel.add(uri);
    if(config.getKnowledgeMode().equals(Configuration.KNOWLEDGE_EXPERT))
    	mapInfoPanel.add(label);
    else
    	mapInfoPanel.add(type);
    if(uri.getText().length()>85)
    	mapInfoPanel.setWidth("400px");
    mapInfoPanel.setStyleName("instancePanel_mapInfoPanel");
    
    // Add the map to the HTML host page
    RootPanel.get(part).add(mapWidget);
//    int left = RootPanel.get(part).getAbsoluteLeft();
//    int top = RootPanel.get(part).getAbsoluteTop();
    int finalLeft;
//    if(subject)
//    	finalLeft=leftSubject;
//    else
//    	finalLeft=leftObject;
    finalLeft = RootPanel.get(part).getAbsoluteLeft();;
    RootPanel.get(part).add(mapInfoPanel,finalLeft+40,top+30);
    echo("@@@left"+finalLeft);
    echo("@@@top"+top);
    
    // Resize mapWidget
    MapHandlerRegistration.trigger(mapWidget, MapEventType.RESIZE);
    mapWidget.setCenter(CENTER);
	}
	
	 protected void drawInfoWindow(Marker marker, MouseEvent mouseEvent, MapWidget mapWidget) {
	    if (marker == null || mouseEvent == null) {
	      return;
	    }
	    specificUri=parseUri(template.getProperties().get(3).getProperty());
//	    System.out.println("specificUri: "+template.getProp2());
//	    HTML html = new HTML(uri+"<br>"+label+"<br>"+type+"<br>"+"<b><i>"+specificUri+" >></b></i> "+specific+"<br><br>"+"<b><i>coordinates >> </i></b>" + mouseEvent.getLatLng().getToString()+"<br>" +
//	    		"");
	    HTML infoWindowHTML =null;
	    if(config.getKnowledgeMode().equals(Configuration.KNOWLEDGE_EXPERT))
	    		infoWindowHTML = new HTML(""+uri+label+type+"<b><i>"+specificUri+" >></b></i> "+specific+"<br>"+"<b><i>coordinates >> </i></b>" 
	    				+ mouseEvent.getLatLng().getToString()+"<br>" +"");
	    else{
	    	 if(specific.isEmpty())
	    		 infoWindowHTML = new HTML(""+uri+type);
	 	    else
	 	    	infoWindowHTML = new HTML(""+uri+type+"<b><i>"+specificUri+" >></b></i> "+specific+"<br>");
	    }
	    infoWindowHTML.setStyleName("infoWindowHTML");
	    DOM.setStyleAttribute(infoWindowHTML.getElement(), "textAlign", "left");
	    InfoWindowOptions options = InfoWindowOptions.newInstance();
	    options.setContent(infoWindowHTML);
	    InfoWindow iw = InfoWindow.newInstance(options);
	    iw.open(mapWidget, marker);
	    iw.addCloseClickHandler(new CloseClickMapHandler(){

				public void onEvent(CloseClickMapEvent event) {
					mapInfoPanel.setVisible(true);
				}});
	    mapInfoPanel.setVisible(false);
	  }
	
  private String parseLabel(String newLabel) {
  	echo("\n\n\nnewLabel: "+newLabel);
  	echo("rawLabel: "+this.rawLabel);
  	String end="";
		if(newLabel==null)
			return "none";
		if(newLabel.contains("<FONT style='BACKGROUND-COLOR: yellow'>")){
			echo("contains font");
			end=newLabel.replace("<FONT style='BACKGROUND-COLOR: yellow'>", "");
			end=end.replace("</FONT>", "");
		}
		else
			end=newLabel;
		if (end.contains(" | ")){
			echo("contains | ");
			int size= end.split(" | ").length;
			String split[] = new String[size];
			split=end.split(" | ");
			end=split[0];
		}
		
		echo("endLabel: "+end);
		return end;
	}

	private void initMapGUI() {
  	if(subject)
  		RootPanel.get("subject").clear();
  	else 
  		RootPanel.get("object").clear();
  	
	  initializeMap(subject, this.latitude, this.longitude);
  
	}
	
	public void refresh(){
		if(template.getType().equals(PropertyConstants.TEMPLATE_TYPE_MAP))
    	initMapGUI();
    else
    	initGUI();
	}
	
  public void initGUI(){
  	if(subject)
  		RootPanel.get("subject").clear();
  	else
  		RootPanel.get("object").clear();
    // URI
  	FlowPanel uriPanel = new FlowPanel();
    uriPanel.add(uri);
    uriPanel.addStyleName("UriPanel");
    // Label
    FlowPanel labelPanel = new FlowPanel();
    labelPanel.add(label);
    labelPanel.addStyleName("LabelPanel");
    // Type
    FlowPanel typePanel = new FlowPanel();
    typePanel.add(type);
    typePanel.addStyleName("TypePanel");
    // Optional   
    HTML specificText = new HTML(specific);
    specificText.setStyleName("specificText");
    VerticalPanel specificValuePanel = new VerticalPanel();
    specificValuePanel.add(specificText);
    specificValuePanel.setStyleName("specificValuePanel");
    FlowPanel specificPanel = new FlowPanel();
    HTML specificHTML;
    String specificBuffer = parseUri(template.getProperties().get(3).getProperty());
    if(config.getKnowledgeMode().equals(Configuration.KNOWLEDGE_EXPERT))
    	specificHTML = new HTML("<i><b>"+parseUri(template.getProperties().get(3).getProperty())+"</b></i>");
    else{
    	specificBuffer = specificBuffer.substring(specificBuffer.indexOf(':')+1);
    	echo("%%%%%%: 1:"+specificBuffer);
    	specificBuffer = specificBuffer.toUpperCase().subSequence(0, 1)+specificBuffer.substring(1);
    	echo("%%%%%%: 2:"+specificBuffer);
    	specificHTML = new HTML("<i><b>"+specificBuffer+":</b></i>");
    }
    specificHTML.setStyleName("PropertyText");
    specificPanel.add(specificHTML);
    specificPanel.add(specificValuePanel);
    specificPanel.addStyleName("SpecificPanel");
    
    this.imagePanel.setStyleName("ImagePanel");
    
    VerticalPanel sPanel = new VerticalPanel();
    this.setStyleName("verifyComponent_instancePanel");
    sPanel.add(uriPanel);
    if(config.getKnowledgeMode().equals(Configuration.KNOWLEDGE_EXPERT))
    	sPanel.add(labelPanel);
    sPanel.add(typePanel);
    sPanel.add(specificPanel);
    sPanel.add(imagePanel);
    
    // Add to div element
    if(this.subject)
    	RootPanel.get("subject").add(sPanel);
    else 
    	RootPanel.get("object").add(sPanel);
  }

  /**
   * @return the uri
   */
  public HTML getUri() {
    return uri;
  }

  /**
   * @param uri the uri to set
   */
  public void setUri(String uri) {
  	if(config.getKnowledgeMode().equals(Configuration.KNOWLEDGE_EXPERT))
    	this.uri = new HTML("<b>URI</b> >> <a href="+uri+" target="+'"'+"_blank"+'"'+">"+uri+"</a>");
  	else
  		this.uri = new HTML("<b>Label</b>: <a href="+uri+" target="+'"'+"_blank"+'"'+">"+this.rawLabel+"</a>");
  }

  /**
   * @return the label
   */
  public HTML getLabel() {
    return label;
  }

  /**
   * @param label the label to set
   */
  public void setLabel(String label) {
  	if(config.getKnowledgeMode().equals(Configuration.KNOWLEDGE_EXPERT))
  		this.label=new HTML("<i><b>"+parseUri(template.getProperties().get(1).getProperty())+"</b></i>"+
  			" >> "+label);
  	else
  		this.label=new HTML("<i><b>label:</i></b> "+label);
  	this.rawLabel=label;
  }

  /**
   * @return the type
   */
  public HTML getType() {
    return type;
  }

  /**
   * @param type the type to set
   */
  public void setType(String type) {
    //this.type = new HTML(parsePropertyValue(type));
  	if(config.getKnowledgeMode().equals(Configuration.KNOWLEDGE_EXPERT))
  		this.type=new HTML("<i><b>"+parseUri(template.getProperties().get(2).getProperty())+"</b></i>" +
  			" >> "+type);
  	else
  		this.type=new HTML("<i><b>Type:</i></b> "+type);
  }

  /**
   * @return the specificUri
   */
  public String getSpecificUri() {
    return specificUri;
  }

  /**
   * @param specificUri the specificUri to set
   */
  public void setSpecificUri(String specificUri) {
  	if(config.getKnowledgeMode().equals(Configuration.KNOWLEDGE_EXPERT))
  		this.specificUri = specificUri;
  	else
  		this.specificUri=specificUri.substring(specificUri.indexOf(':'));
  	
  }

  /**
   * @return the specificText
   */
  public String getSpecific() {
    return specific;
  }

  /**
   * @param specific the specificValue to set
   */
  public void setSpecific(String specific) {
    if(specific.length() == 0)
    	this.specific = "no value";
    else{
    	this.specific = parseSpecific(specific);
    }
  }
  
  public String parseSpecific(String s){
//  	echo("Parse specific: "+s);
  	if(!s.contains(" | "))
  		return s;
  	String split[] = s.split(" \\| ");
//  	echo("split size: "+split.length);
  	String parsed="";
  	String buffer="";
  
  		for(int i=0;i<split.length;i++){
//  			echo(i+". "+split[i]);
  			buffer = split[i].substring(split[i].lastIndexOf('/')+1);
  			buffer = buffer.replace("#adaptations", "");
  			buffer = buffer.replace(">", "");
//  			echo(i+". "+buffer);
  			parsed +=" - "+buffer+"</br>";
  		}

//  	echo("Parsed: "+parsed);
  	return parsed;
  }
  public void addImage(Image img){
    this.imagePanel.add(img);
  }
  
  public void setTemplate(TemplateInstance tmp) {
    this.template=tmp;
  }
  
  /**
   * Remove '<' and '>' of an image url
   * @param image
   * @return parsed String
   */
  public String parseUri(String s) {
    System.out.print("InstancePanel: parse URI >> ");
    String end =null;
    if (s.isEmpty()){
      System.out.println("URI empty");
      return s;
    } else if ( s.contains("<"))
      end = s.substring(1, s.length()-1);
    else
      end = s;
    System.out.println("Parsed URI: "+end);

    return end;
  }
  
  public String parsePropertyValue(String s) {
    System.out.print("InstancePanel: parse property value >> ");
    if (s.isEmpty()){
      System.out.println("Value empty");
      return s;
    }
    String end = s.replaceAll("<","").replaceAll(">", "");
    
    System.out.println("Parsed value: "+end);

    return end;
  }
  
  public VerticalPanel getImagePanel() {
    return this.imagePanel;
  }

	public void setLatitude(double latitude) {
		this.latitude=latitude;
		
	}
  
	public void setLongitude(double longitude) {
		this.longitude=longitude;
		
	}
	
	public void setConfiguration(Configuration conf){
		this.config=conf;
	}
}
