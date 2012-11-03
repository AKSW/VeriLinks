/**
 * 
 */
package org.aksw.verilinks.games.peaInvasion.client.verify;
//
//import com.google.gwt.maps.client.MapOptions;
//import com.google.gwt.maps.client.MapTypeId;
//import com.google.gwt.maps.client.MapWidget;
//import com.google.gwt.maps.client.base.LatLng;
import java.util.ArrayList;

import org.aksw.verilinks.games.peaInvasion.shared.Template;
import org.aksw.verilinks.games.peaInvasion.shared.templates.TemplateInstance;

import com.google.gwt.ajaxloader.client.ArrayHelper;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.maps.client.LoadApi;
import com.google.gwt.maps.client.LoadApi.LoadLibrary;
import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapTypeId;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.base.LatLng;
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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;



/**
 * @author q1
 *
 */
public class MapsPanel extends HorizontalPanel{
	 
//	private MapWidget mapWidget;

  private HTML htmlDistanceMatrixService = new HTML("&nbsp;");
	
	public MapsPanel(){
		super();
		echo("InitMaps");
//		add(new Button("test"));
//		initializeMap();
//		initInstance();
		loadMapApi();
//		drawMap();
		
		echo("InitMaps Done");
	}
//	   
//	private void drawMap() {
//		echo("DRAW START");
//    LatLng center = LatLng.newInstance(49.496675,-102.65625);
//    MapOptions opts = MapOptions.newInstance();
//    opts.setZoom(4);
//    opts.setCenter(center);
//    opts.setMapTypeId(MapTypeId.HYBRID);
//    
//    mapWidget = new MapWidget(opts);
//    add(mapWidget);
//    mapWidget.setSize("750px", "500px");
//    echo("DRAW END");
//  }
//
	
	
	private void initMap() {
		// TODO Auto-generated method stub
		
	}


	private void initInstance(){
		InstancePanel in = new InstancePanel(new TemplateInstance(),true,null);
		RootPanel.get("subject").add(in);
		
	}
	native void initializeMap() /*-{
  var latlng = new $wnd.google.maps.LatLng(31.4000476, 34.3832624);
  var myOptions = {
    zoom: 8,
    center: latlng,
    mapTypeId: $wnd.google.maps.MapTypeId.ROADMAP
  };
  var div = $doc.getElementById("object");
  div.style.width="500px";
  div.style.height="300px";
  
  var map = new $wnd.google.maps.Map(div, myOptions);
}-*/;

//	private void initializeMap(){
//		 final MapOptions options = new MapOptions();
//	    // Zoom level. Required
//	    options.setZoom(8);
//	    // Open a map centered on Cawker City, KS USA. Required
//	    options.setCenter(new LatLng(39.509, -98.434));
//	    // Map type. Required.
//	    options.setMapTypeId(new MapTypeId().getRoadmap());
//	    
//	    // Enable maps drag feature. Disabled by default.
//	    options.setDraggable(true);
//	    // Enable and add default navigation control. Disabled by default.
//	    options.setNavigationControl(true);
//	    // Enable and add map type control. Disabled by default.
//	    options.setMapTypeControl(true);
//	    mapWidget = new MapWidget(options);
//	    mapWidget.setSize("800px", "600px");
//	    
//	    
//	    // Add the map to the HTML host page
//	    RootPanel.get("verifyComponent_maps").add(mapWidget);
//
//	}
	
	
	private void echo(String string) {
		System.out.println("##Client: MapsPanel>> "+string);
		
	}

	private void loadMapApi() {
    boolean sensor = true;
    
    // load all the libs for use
    ArrayList<LoadLibrary> loadLibraries = new ArrayList<LoadApi.LoadLibrary>();
    loadLibraries.add(LoadLibrary.ADSENSE);
    loadLibraries.add(LoadLibrary.DRAWING);
    loadLibraries.add(LoadLibrary.GEOMETRY);
    loadLibraries.add(LoadLibrary.PANORAMIO);
    loadLibraries.add(LoadLibrary.PLACES);
    
    Runnable onLoad = new Runnable() {
      public void run() {
        System.out.println( "DUde");
        draw();
      }
    };
    
    LoadApi.go(onLoad, loadLibraries, sensor);
  }
	
	private void draw(){
		LatLng center = LatLng.newInstance(49.496675,-102.65625);
    
	  VerticalPanel panel = new VerticalPanel();
	  MapOptions op = MapOptions.newInstance();
	  op.setZoom(4);
	  op.setCenter(center);
	  op.setMapTypeId(MapTypeId.HYBRID);
	  
	  MapWidget mw = new MapWidget(op);
	  panel.add(mw);
	  mw.setSize("600px", "600px");
	  RootPanel.get().add(panel);
	  direction(mw);
	  System.out.println("maptest!");
	}
	
	private void direction(MapWidget mapWidget){
	  DirectionsRendererOptions options = DirectionsRendererOptions.newInstance();
	    final DirectionsRenderer directionsDisplay = DirectionsRenderer.newInstance(options);
	    directionsDisplay.setMap(mapWidget);
	    
	    final LatLng origin = LatLng.newInstance(37.7699298, -122.4469157);
	    final LatLng destination = LatLng.newInstance(37.7683909618184, -122.51089453697205);
	    
//	    String origin = "Arlington, WA";
//	    String destination = "Seattle, WA";
	    
	    DirectionsRequest request = DirectionsRequest.newInstance();
	    request.setOrigin(origin);
	    request.setDestination(destination);
	    request.setTravelMode(TravelMode.DRIVING);
	    
	    DirectionsService o = DirectionsService.newInstance();    
	    o.route(request, new DirectionsResultHandler() {
	      public void onCallback(DirectionsResult result, DirectionsStatus status) {
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

	  
  }
	
	private void getDistance(LatLng origin, LatLng destination) {
//    
//    String origin = "Arlington, WA";
//    String destination = "Seattle, WA";
    
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
      public void onCallback(DistanceMatrixResponse response, DistanceMatrixStatus status) {
        
        System.out.println("status=" + status.value());
        
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
          for (int i=0; i < elements.length(); i++) {
            DistanceMatrixResponseElement e = elements.get(i);
            Distance distance = e.getDistance();
            Duration duration = e.getDuration();
            DistanceMatrixElementStatus st = e.getStatus();
            System.out.println("distance=" + distance.getText() + " value=" + distance.getValue());
            System.out.println("duration=" + duration.getText() + " value=" + duration.getValue());
            
            String html = "&nbsp;&nbsp;Distance=" + distance.getText() + " Duration=" + duration.getText() + " ";
            htmlDistanceMatrixService.setHTML(html);
            RootPanel.get().add(new HTML(html));
          }
          
        } else if (status == DistanceMatrixStatus.OVER_QUERY_LIMIT) {
          
        } else if (status == DistanceMatrixStatus.REQUEST_DENIED) {
          
        } else if (status == DistanceMatrixStatus.UNKNOWN_ERROR) {
          
        }
        
      }
    });
    
    
  }
	
}
