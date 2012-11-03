package org.aksw.verilinks.games.peaInvasion.client.verify;

import org.aksw.verilinks.games.peaInvasion.shared.Configuration;
import org.aksw.verilinks.games.peaInvasion.shared.Template;
import org.aksw.verilinks.games.peaInvasion.shared.rdfInstance;
import org.aksw.verilinks.games.peaInvasion.shared.rdfStatement;
import org.aksw.verilinks.games.peaInvasion.shared.templates.TemplateInstance;
import org.aksw.verilinks.games.peaInvasion.shared.templates.TemplateLinkset;

import com.google.gwt.ajaxloader.client.ArrayHelper;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
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
import com.google.gwt.maps.client.events.tiles.TilesLoadedMapEvent;
import com.google.gwt.maps.client.events.tiles.TilesLoadedMapHandler;
import com.google.gwt.maps.client.overlays.InfoWindow;
import com.google.gwt.maps.client.overlays.InfoWindowOptions;
import com.google.gwt.maps.client.overlays.Marker;
import com.google.gwt.maps.client.overlays.MarkerOptions;
import com.google.gwt.maps.client.services.DirectionsRenderer;
import com.google.gwt.maps.client.services.DirectionsRendererOptions;
import com.google.gwt.maps.client.services.DirectionsRequest;
import com.google.gwt.maps.client.services.DirectionsResult;
import com.google.gwt.maps.client.services.DirectionsResultHandler;
import com.google.gwt.maps.client.services.DirectionsService;
import com.google.gwt.maps.client.services.DirectionsStatus;
import com.google.gwt.maps.client.services.Distance;
import com.google.gwt.maps.client.services.DistanceMatrixElementStatus;
import com.google.gwt.maps.client.services.DistanceMatrixRequest;
import com.google.gwt.maps.client.services.DistanceMatrixRequestHandler;
import com.google.gwt.maps.client.services.DistanceMatrixResponse;
import com.google.gwt.maps.client.services.DistanceMatrixResponseElement;
import com.google.gwt.maps.client.services.DistanceMatrixResponseRow;
import com.google.gwt.maps.client.services.DistanceMatrixService;
import com.google.gwt.maps.client.services.DistanceMatrixStatus;
import com.google.gwt.maps.client.services.Duration;
import com.google.gwt.maps.client.services.TravelMode;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class BothMapsPanel extends VerifyPanel {


	private HorizontalPanel sPanel;
	private HorizontalPanel oPanel;
	private VerticalPanel sInfoPanel;
	private VerticalPanel oInfoPanel;

	private VerticalPanel routePanel;
	private VerticalPanel routeInfoPanel;

//	private VerticalPanel markerInfoPanel;
	
	private MapWidget mapWidget;
	private double lat;
	private double longi;
	private String mapWidth = "1000px";
	private String mapHeight = "320px";
	private HTML sUri;
	private HTML oUri;
	private HTML sLabel;
	private HTML oLabel;

	// div element subject
	// top: 438px
	// left: 38px
	public final int top = 438;
	public final int left = 38;

	public BothMapsPanel(TemplateLinkset template, Configuration config) {
		super(template, config);
		this.lat = 0;
		this.longi = 0;


		// routeInfoPanel
		routePanel = new VerticalPanel();
		routePanel.setStyleName("bothMapsPanel_routePanel");
		routePanel.add(new HTML("<b>Route Information</b>"));
		routeInfoPanel = new VerticalPanel();
		routeInfoPanel.setStyleName("bothMapsPanel_routeInfoPanel");
		routePanel.add(routeInfoPanel);

		// RootPanel.get().add(routePanel,left+400,top+150);
		RootPanel.get().add(routePanel, 10, top + 140);
		drawMap();

	}

	private void drawMap() {
		echo("drawMap");
		if (subjectInstance != null) {
			lat = subjectInstance.getLatitude();
			longi = subjectInstance.getLongitude();
		}
		LatLng center = LatLng.newInstance(lat, longi);
		MapOptions opts = MapOptions.newInstance();
		opts.setZoom(4);
		opts.setCenter(center);
		opts.setMapTypeId(MapTypeId.HYBRID);

		mapWidget = new MapWidget(opts);
		this.add(mapWidget);
		mapWidget.setSize(mapWidth, mapHeight);

		mapWidget.addClickHandler(new ClickMapHandler() {
			public void onEvent(ClickMapEvent event) {
				// TODO fix the event getting, getting ....
				System.out.println("clicked on latlng="
						+ event.getMouseEvent().getLatLng());
			}
		});

		mapWidget.addTilesLoadedHandler(new TilesLoadedMapHandler() {
			public void onEvent(TilesLoadedMapEvent event) {

			}
		});
	}

	private void drawRoute() {
		echo("drawRoute");
		DirectionsRendererOptions options = DirectionsRendererOptions.newInstance();
		final DirectionsRenderer directionsDisplay = DirectionsRenderer
				.newInstance(options);
		directionsDisplay.setMap(mapWidget);

		final LatLng origin = LatLng.newInstance(subjectInstance.getLatitude(),
				subjectInstance.getLongitude());
		final LatLng destination = LatLng.newInstance(objectInstance.getLatitude(),
				objectInstance.getLongitude());

		DirectionsRequest request = DirectionsRequest.newInstance();
		request.setOrigin(origin);
		request.setDestination(destination);
		request.setTravelMode(TravelMode.DRIVING);

		DirectionsService o = DirectionsService.newInstance();
		o.route(request, new DirectionsResultHandler() {
			public void onCallback(DirectionsResult result, DirectionsStatus status) {
				echo(">>>DirectionsService Status: "+status.toString());
				if (status == DirectionsStatus.OK) {
					directionsDisplay.setDirections(result);
					getDistance(origin, destination);
				} else if (status == DirectionsStatus.INVALID_REQUEST) {

				} else if (status == DirectionsStatus.MAX_WAYPOINTS_EXCEEDED) {

				} else if (status == DirectionsStatus.NOT_FOUND) {

				} else if (status == DirectionsStatus.OVER_QUERY_LIMIT) {

				} else if (status == DirectionsStatus.REQUEST_DENIED) {

				} else if (status == DirectionsStatus.UNKNOWN_ERROR) {

				} else if (status == DirectionsStatus.ZERO_RESULTS) {

				}

			}
		});

		echo("drawRoute done");
	}

	private void getDistance(LatLng origin, LatLng destination) {

		String[] ao = new String[1];
		ao[0] = origin.getToString();
		JsArrayString origins = ArrayHelper.toJsArrayString(ao);

		String[] ad = new String[1];
		ad[0] = destination.getToString();
		JsArrayString destinations = ArrayHelper.toJsArrayString(ad);

		DistanceMatrixRequest request = DistanceMatrixRequest.newInstance();
		request.setOrigins(origins);
		request.setDestinations(destinations);
		request.setTravelMode(TravelMode.DRIVING);

		DistanceMatrixService o = DistanceMatrixService.newInstance();
		o.getDistanceMatrix(request, new DistanceMatrixRequestHandler() {
			public void onCallback(DistanceMatrixResponse response,
					DistanceMatrixStatus status) {

				if (status == DistanceMatrixStatus.INVALID_REQUEST) {

				} else if (status == DistanceMatrixStatus.MAX_DIMENSIONS_EXCEEDED) {

				} else if (status == DistanceMatrixStatus.MAX_ELEMENTS_EXCEEDED) {

				} else if (status == DistanceMatrixStatus.OK) {

					JsArrayString dest = response.getDestinationAddresses();
					JsArrayString org = response.getOriginAddresses();
					JsArray<DistanceMatrixResponseRow> rows = response.getRows();

					System.out.println("rows.length=" + rows.length());
					DistanceMatrixResponseRow d = rows.get(0);
					JsArray<DistanceMatrixResponseElement> elements = d.getElements();
					for (int i = 0; i < elements.length(); i++) {
						DistanceMatrixResponseElement e = elements.get(i);
						Distance distance = e.getDistance();
						Duration duration = e.getDuration();
						DistanceMatrixElementStatus st = e.getStatus();

						System.out.println("##getDistance Link: s="
								+ subjectInstance.getLabel() + " o="
								+ objectInstance.getLabel());
						System.out.println(" elements.length(): " + elements.length());
						System.out.println("distance=" + distance.getText() + " value="
								+ distance.getValue());
						System.out.println("duration=" + duration.getText() + " value="
								+ duration.getValue());
						System.out.println("status: " + st.toString());

						// Display route information
						HTML dist = new HTML("<i>Distance >></i> <b>" + distance.getText()
								+ "</b>");
						HTML dur = new HTML("<i>Duration >></i> <b>" + duration.getText()
								+ "</b>");

						routeInfoPanel.clear();
						routeInfoPanel.add(dist);
						routeInfoPanel.add(dur);
					}

				} else if (status == DistanceMatrixStatus.OVER_QUERY_LIMIT) {

				} else if (status == DistanceMatrixStatus.REQUEST_DENIED) {

				} else if (status == DistanceMatrixStatus.UNKNOWN_ERROR) {

				}

			}
		});

	}

	public void echo(String s) {
		System.out.println("##BothMapsPanel: " + s);
	}

	public void update(rdfStatement link) {
		this.subjectInstance = link.getSubject();
		this.objectInstance = link.getObject();

		RootPanel.get("subject").clear();
		this.clear();
		RootPanel.get("subject").add(this);
		drawMap();
		drawRoute();
		drawMarker();
		drawInfo();
		MapHandlerRegistration.trigger(mapWidget, MapEventType.RESIZE);
	}

	private void drawMarker() {

		LatLng subject = LatLng.newInstance(subjectInstance.getLatitude(),
				subjectInstance.getLongitude());
		LatLng object = LatLng.newInstance(objectInstance.getLatitude(),
				objectInstance.getLongitude());
		MarkerOptions optionsSubject = MarkerOptions.newInstance();
		optionsSubject.setPosition(subject);
//		optionsSubject.setTitle("(A) subject: "+subjectInstance.getLabel());
		optionsSubject.setTitle("(A) subject");

		final Marker markerSubject = Marker.newInstance(optionsSubject);
		markerSubject.setMap(mapWidget);

		markerSubject.addClickHandler(new ClickMapHandler() {
			public void onEvent(ClickMapEvent event) {
				 drawInfoWindowSubject(markerSubject, event.getMouseEvent(),mapWidget);
			}
		});
		
		MarkerOptions optionsObject = MarkerOptions.newInstance();
		optionsObject.setPosition(object);
//		optionsObject.setTitle("(B) object: "+objectInstance.getLabel());
		optionsObject.setTitle("(B) object");

		final Marker markerObject = Marker.newInstance(optionsObject);
		markerObject.setMap(mapWidget);

		markerObject.addClickHandler(new ClickMapHandler() {
			public void onEvent(ClickMapEvent event) {
				 drawInfoWindowObject(markerObject, event.getMouseEvent(),mapWidget);
			}
		});
	}

	protected void drawInfoWindowSubject(Marker marker, MouseEvent mouseEvent, MapWidget mapWidget) {
    if (marker == null || mouseEvent == null) {
      return;
    }
    
    HTML instanceText = parseInstance(subjectTemplate,subjectInstance);
  
//    System.out.println("specificUri: "+template.getProp2());
//    HTML html = new HTML(uri+"<br>"+label+"<br>"+type+"<br>"+"<b><i>"+specificUri+" >></b></i> "+specific+"<br><br>"+"<b><i>coordinates >> </i></b>" + mouseEvent.getLatLng().getToString()+"<br>" +
//    		"");
    HTML infoWindowHTML = instanceText;
    infoWindowHTML.setStyleName("infoWindowHTML");
//    DOM.setStyleAttribute(html.getElement(), "color", "red");
    DOM.setStyleAttribute(infoWindowHTML.getElement(), "textAlign", "left");
    InfoWindowOptions options = InfoWindowOptions.newInstance();
    options.setContent(infoWindowHTML);
    InfoWindow iw = InfoWindow.newInstance(options);
    iw.addCloseClickHandler(new CloseClickMapHandler(){

			public void onEvent(CloseClickMapEvent event) {
				sPanel.setVisible(true);
			}});
    iw.open(mapWidget, marker);
    
    sPanel.setVisible(false);
  }
	
	protected void drawInfoWindowObject(Marker marker, MouseEvent mouseEvent, MapWidget mapWidget) {
    if (marker == null || mouseEvent == null) {
      return;
    }
    
    HTML instanceText = parseInstance(objectTemplate,objectInstance);
  
//    System.out.println("specificUri: "+template.getProp2());
//    HTML html = new HTML(uri+"<br>"+label+"<br>"+type+"<br>"+"<b><i>"+specificUri+" >></b></i> "+specific+"<br><br>"+"<b><i>coordinates >> </i></b>" + mouseEvent.getLatLng().getToString()+"<br>" +
//    		"");
    HTML infoWindowHTML = instanceText;
    infoWindowHTML.setStyleName("infoWindowHTML");
//    DOM.setStyleAttribute(html.getElement(), "color", "red");
    DOM.setStyleAttribute(infoWindowHTML.getElement(), "textAlign", "left");
    InfoWindowOptions options = InfoWindowOptions.newInstance();
    options.setContent(infoWindowHTML);
    InfoWindow iw = InfoWindow.newInstance(options);
    iw.addCloseClickHandler(new CloseClickMapHandler(){

			public void onEvent(CloseClickMapEvent event) {
				oPanel.setVisible(true);
			}});
    iw.open(mapWidget, marker);
    oPanel.setVisible(false);
  }
	
	
	private HTML parseInstance(TemplateInstance template,
			rdfInstance instance) {
		String uri=null;
		String label=null;
		String type=null;
		String specific=null;
		HTML parsed = null;
	  if(config.getKnowledgeMode().equals(Configuration.KNOWLEDGE_EXPERT)){
	  	uri = new String("<b>URI</b> >> <a href="+instance.getUri()+" target="+'"'+"_blank"+'"'+">"+instance.getUri()+"</a>");
		  
	    label = new String("<i><b>"+template.getProperties().get(1).getProperty()+"</b></i>"+
	  			" >> "+instance.getLabel());
	    type = new String("<i><b>"+template.getProperties().get(2).getProperty()+"</b></i>" +
	  			" >> "+instance.getType());
	    if (instance.getOptional().length() >0)
	    	specific = new String("<i><b>"+template.getProperties().get(3).getProperty()+">> </b></i>"+instance.getOptional());
	    else
	    	specific = new String("<i><b>"+template.getProperties().get(3).getProperty()+">> </b></i>"+"no value");
	    parsed=new HTML(uri+"<br>"+label+"<br>"+type+"<br>"+specific);
	  }else{
	  	uri = new String("<b>Label:</b> <a href="+instance.getUri()+" target="+'"'+"_blank"+'"'+">"+instance.getLabel()+"</a>");

	    type = new String("<i><b>Type:</b></i> "+instance.getType());
	    if(!instance.getOptional().isEmpty()){
	    	specific = new String("<i><b>"+template.getProperties().get(3).getProperty()+": </b></i>"+instance.getOptional());
	    	parsed=new HTML(uri+"<br>"+type+"<br>"+specific);
	    }
	    else
	    	parsed=new HTML(uri+"<br>"+type);
	    
	  }
		return parsed;
	}

	private void drawInfo() {
		if(config.getKnowledgeMode().equals(Configuration.KNOWLEDGE_EXPERT)){
			sUri = new HTML("<b>URI</b> >> <a href=" + subjectInstance.getUri()
					+ " target=" + '"' + "_blank" + '"' + ">" + subjectInstance.getUri()
					+ "</a>");
			sLabel = new HTML("<i><b>" + subjectTemplate.getProperties().get(1).getProperty() + "</b></i>"
					+ " >> " + subjectInstance.getLabel());
			oUri = new HTML("<b>URI</b> >> <a href=" + objectInstance.getUri()
					+ " target=" + '"' + "_blank" + '"' + ">" + objectInstance.getUri()
					+ "</a>");
			oLabel = new HTML("<i><b>" + objectTemplate.getProperties().get(1).getProperty() + "</b></i>"
					+ " >> " + objectInstance.getLabel());
		}
		else{
			sUri = new HTML("<b>Label:</b> <a href=" + subjectInstance.getUri()
					+ " target=" + '"' + "_blank" + '"' + ">" + subjectInstance.getLabel()
					+ "</a>");
			sLabel = new HTML("<i><b>" + subjectTemplate.getProperties().get(1).getProperty() + "</b></i>"
					+ " >> " + subjectInstance.getLabel());
			oUri = new HTML("<b>Label:</b> <a href=" + objectInstance.getUri()
					+ " target=" + '"' + "_blank" + '"' + ">" + objectInstance.getLabel()
					+ "</a>");
			oLabel = new HTML("<i><b>" + objectTemplate.getProperties().get(1).getProperty() + "</b></i>"
					+ " >> " + objectInstance.getLabel());
			
		}
			
		// Panels
		sInfoPanel = new VerticalPanel();
		sInfoPanel.add(sUri);
		if(config.getKnowledgeMode().equals(Configuration.KNOWLEDGE_EXPERT))
			sInfoPanel.add(sLabel);
		else
			sInfoPanel.add(new HTML("<b><i>Type:</i></b> "+subjectInstance.getType()));
		// sInfoPanel.setBorderWidth(1);
		if (sUri.getText().length() > 85)
			sInfoPanel.setWidth("400px");
		sPanel = new HorizontalPanel();
		sPanel.setVerticalAlignment(ALIGN_MIDDLE);
		sPanel.add(new HTML("A"));
		sPanel.add(sInfoPanel);

		oInfoPanel = new VerticalPanel();
		oInfoPanel.add(oUri);
		if(config.getKnowledgeMode().equals(Configuration.KNOWLEDGE_EXPERT))
			oInfoPanel.add(oLabel);
		else
			oInfoPanel.add(new HTML("<b><i>Type:</i></b> "+objectInstance.getType()));
		
		// oInfoPanel.setBorderWidth(1);
		if (sUri.getText().length() > 85)
			oInfoPanel.setWidth("400px");
		oPanel = new HorizontalPanel();
		oPanel.setVerticalAlignment(ALIGN_MIDDLE);

		oPanel.add(new HTML("B"));
		oPanel.add(oInfoPanel);

		// int left = RootPanel.get("subject").getAbsoluteLeft();
		// int top = RootPanel.get("subject").getAbsoluteTop();

		RootPanel.get("subject").add(sPanel, left + 80, top + 10);
		RootPanel.get("subject").add(oPanel, left + 600, top + 268);
		sInfoPanel.setStyleName("bothMapsPanel_infoPanel_right");
		oInfoPanel.setStyleName("bothMapsPanel_infoPanel_right");
		sPanel.setStyleName("bothMapsPanel_infoPanel");
		oPanel.setStyleName("bothMapsPanel_infoPanel");

	}

}
