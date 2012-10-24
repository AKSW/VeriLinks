package org.aksw.verilinks.core.client;

import org.aksw.verilinks.core.shared.Configuration;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class VeriLinks implements EntryPoint {

	private Configuration config = null;
	private boolean serverRunning = false;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		echo("test");
		this.config = new Configuration();

		// Check if page called from kongregate -> set in configuration
		String urlParam = com.google.gwt.user.client.Window.Location
				.getParameter("kongregate");
//		 Window.alert("urlParam: "+urlParam);
		if (urlParam != null && urlParam.equals("true")) {
			// Window.alert("Welcome Kongregate User! :)");
			config.setKongregate(true);
			DOM.setStyleAttribute(RootPanel.getBodyElement(), "width", "1030px");
			// DOM.setStyleAttribute(RootPanel.getBodyElement(),
			// "-moz-transform", "50%");
			DOM.setStyleAttribute(RootPanel.getBodyElement(), "margin",
					"0px auto");
		} else if (urlParam == null) {
			config.setSimple(true);
			DOM.setStyleAttribute(RootPanel.getBodyElement(), "width", "1030px");
			DOM.setStyleAttribute(RootPanel.getBodyElement(), "margin",
					"0px auto");
			// DOM.setStyleAttribute(RootPanel.getBodyElement(), "borderLeft",
			// "1px solid grey");
			// DOM.setStyleAttribute(RootPanel.getBodyElement(), "borderRight",
			// "1px solid grey");
			config.setKongregate(false);
		}

		connectToServer();

		init();
	}

	private void connectToServer() {
		// TODO Auto-generated method stub

	}
	
	private void init() {
		echo("Init");
		Button b = new Button("rest");
		b.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent arg0) {
				restTest();
				
			}
			
		});
		RootPanel.get().add(b);
	}

	private void restTest(){
		String url = "/Application/rest?service=commitVerifications";
	  	url="/Application/rest?service=getHighscore&game=peas";
	  	url="/Application/rest?service=getLink" +
	  			"&userName=foo&userId=username-login: only name available" +
	  			"&verifiedLinks=55+33+11+1+2" +
	  			"&curLink=2" +
	  			"&nextLink=false" +
	  			"&verification=1" +
	  			"&linkset=dbpedia-linkedgeodata";
	  	url="/Application/rest?service=getLinksets";
	  	String data = "{ "+'"'+"verifiTest"+'"'+":"+'"'+"Test"+'"'+"}";
	  	echo(data);
			
	  	RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url));
//	  	builder.setHeader("Content-Type",
//	    "application/x-www-form-urlencoded");
//	  	builder.setHeader("Access-Control-Allow-Origin", "*");
	  	echo("REST: "+url);
	  	try {
	  	  Request request = builder.sendRequest(data, new RequestCallback() {
	  	    public void onError(Request request, Throwable exception) {
	  	       // Couldn't connect to server (could be timeout, SOP violation, etc.)
	  	    	echo("ERROR rest");
	  	    }

					public void onResponseReceived(Request request, Response response) {
						// TODO Auto-generated method stub
						echo("client SUCCESS rest");
						Window.alert(response.getText());
						
				
					}
	  	  });
	  	} catch (RequestException e) {
	  	  // Couldn't connect to server      
	  		echo("ERROR!");
	  	}
	  
	}

	private void echo(String m){
		System.out.println("[Client]: ");
	}
}
