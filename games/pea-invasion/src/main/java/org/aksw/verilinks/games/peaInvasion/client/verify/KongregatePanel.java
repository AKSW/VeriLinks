package org.aksw.verilinks.games.peaInvasion.client.verify;

import org.aksw.verilinks.games.peaInvasion.client.panels.DetailsPanel;
import org.aksw.verilinks.games.peaInvasion.shared.Balancing;
import org.aksw.verilinks.games.peaInvasion.shared.Configuration;
import org.aksw.verilinks.games.peaInvasion.shared.Template;
import org.aksw.verilinks.games.peaInvasion.shared.rdfInstance;
import org.aksw.verilinks.games.peaInvasion.shared.rdfStatement;
import org.aksw.verilinks.games.peaInvasion.shared.templates.TemplateInstance;
import org.aksw.verilinks.games.peaInvasion.shared.templates.TemplateLinkset;

import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapTypeId;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.events.MapEventType;
import com.google.gwt.maps.client.events.MapHandlerRegistration;
import com.google.gwt.maps.client.events.click.ClickMapEvent;
import com.google.gwt.maps.client.events.click.ClickMapHandler;
import com.google.gwt.maps.client.overlays.Marker;
import com.google.gwt.maps.client.overlays.MarkerOptions;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class KongregatePanel extends VerifyPanel {

	private HorizontalPanel question;
	private HorizontalPanel wholeQuestionPanel;
	private rdfStatement link;
	private HTML subject;
	private HTML predicate;
	private HTML object;

	private HTML sUri;
	private HTML oUri;
	private HTML sLabel;
	private HTML oLabel;

	private DetailsPanel sDetails;
	private DetailsPanel oDetails;

	private Image imgSub;
	private Image imgOb;
	// div element subject
	// top: 438px
	// left: 38px
	public final int top = 438;
	public final int left = 38;

	private VerticalPanel mapInfoPanel;
	private MapWidget mapWidget;

	private HTML linkDifficulty;
	
	public KongregatePanel(TemplateLinkset template,
			Configuration config) {
		super(template, config);

		initGUI();

	}

	private void initGUI() {
		echo("##Konngregate init gui");
		DOM.setStyleAttribute(RootPanel.get("predicate").getElement(),
				"borderLeft", "1px outset black");
		this.subject = new HTML("Subject");
		this.object = new HTML("Object");
		this.predicate = new HTML("Predicate");

		this.subject.setStyleName("kongregateSubject");
		this.object.setStyleName("kongregateObject");
		this.predicate.setStyleName("kongregatePredicate");

		this.imgSub = new Image();
		this.imgOb = new Image();

		this.subject.addMouseOverHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent event) {
				drawDetails(subjectInstance, true);
			}
		});
		this.subject.addMouseOutHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				drawDetails(subjectInstance, false);
			}
		});
		this.object.addMouseOverHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent event) {
				drawDetails(objectInstance, true);
			}
		});
		this.object.addMouseOutHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				drawDetails(objectInstance, false);
			}
		});

		this.imgSub.addMouseOverHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent event) {
				drawDetailsImage(subjectInstance, true);
			}
		});
		this.imgSub.addMouseOutHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				drawDetailsImage(subjectInstance, false);
			}
		});
		this.imgOb.addMouseOverHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent event) {
				drawDetailsImage(objectInstance, true);
			}
		});
		this.imgOb.addMouseOutHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				drawDetailsImage(objectInstance, false);
			}
		});

		// DOM.setStyleAttribute(subject.getElement(), "fontStyle", "italic");
		// DOM.setStyleAttribute(subject.getElement(), "font", "20px MisoRegular");
		// DOM.setStyleAttribute(subject.getElement(), "margin", "20px");
		// DOM.setStyleAttribute(subject.getElement(), "backgroundColor", "white");
		// DOM.setStyleAttribute(predicate.getElement(), "font",
		// "20px MisoRegular");
		// DOM.setStyleAttribute(predicate.getElement(), "margin", "20px");
		// DOM.setStyleAttribute(object.getElement(), "fontSize", "2em");
		// DOM.setStyleAttribute(object.getElement(), "font", "20px MisoRegular");
		// DOM.setStyleAttribute(object.getElement(), "margin", "20px");
		// DOM.setStyleAttribute(object.getElement(), "color", "#7D1B7E");
		this.question = new HorizontalPanel();
		question.setStyleName("kongregateQuestion");
		question.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
		question.setVerticalAlignment(ALIGN_MIDDLE);
		question.add(subject);
		question.add(predicate);
		question.add(object);
		 DOM.setStyleAttribute(question.getElement(), "margin", "auto");

		// DOM.setStyleAttribute(question.getElement(), "border",
		// "1px solid lightgrey");
		DOM.setStyleAttribute(DOM.getElementById("subject"), "border", "none");
		// DOM.setStyleAttribute(DOM.getElementById("subject"), "marginTop",
		// "10px");

		// DOM.setStyleAttribute(DOM.getElementById("subject"), "-moz-box-shadow",
		// "1px 3px 2px 0px lightgrey");
		// DOM.setStyleAttribute(DOM.getElementById("subject"),
		// "-webkit-box-shadow", "1px 3px 2px 0px lightgrey");
		// DOM.setStyleAttribute(DOM.getElementById("subject"), "box-shadow",
		// "1px 3px 2px 0px lightgrey");
		//
		HTML is = new HTML("Is");
		is.setStyleName("kongregateWholeQuestion");
		HTML qMark = new HTML("?");
		qMark.setStyleName("kongregateWholeQuestion");
		wholeQuestionPanel = new HorizontalPanel();
		wholeQuestionPanel.setVerticalAlignment(ALIGN_MIDDLE);
//		wholeQuestionPanel.add(is);
		wholeQuestionPanel.add(question);
		wholeQuestionPanel.add(qMark);
		
		HTML head = new HTML("Quiz:");
		if(subjectTemplate.getOntology().equals("dbpedia") && objectTemplate.getOntology().equals("linkedgeodata"))
			head.setHTML("Does this flag/image belong to this country?");
		if(subjectTemplate.getOntology().equals("dbpedia") && objectTemplate.getOntology().equals("factbook"))
			head.setHTML("Is this language spoken in this country?");
		if(subjectTemplate.getOntology().equals("dbpedia") && objectTemplate.getOntology().equals("bbcwildlife"))
			head.setHTML("Are these the same animals?");
		head.setStyleName("kongregateHead");
		DOM.setStyleAttribute(head.getElement(), "marginBottom", "8px");
		
		linkDifficulty = new HTML("Link Difficulty");
//		DOM.setStyleAttribute(linkDifficulty.getElement(), "fontStyle", "italic");
		DOM.setStyleAttribute(linkDifficulty.getElement(), "margin", "5px 20px");
//		DOM.setStyleAttribute(linkDifficulty.getElement(), "fontWeight", "bold");
		
		HorizontalPanel questionHeadPanel = new HorizontalPanel();
		questionHeadPanel.add(head);
		questionHeadPanel.add(linkDifficulty);
		
		add(questionHeadPanel);
		add(wholeQuestionPanel);
		// DOM.setStyleAttribute(qMark.getElement(), "paddingTop", "15px");
		// DOM.setStyleAttribute(is.getElement(), "paddingTop", "15px");
		// DOM.setStyleAttribute(head.getElement(), "font", "30px MisoRegular");
		// DOM.setStyleAttribute(head.getElement(), "color", "black");
		// DOM.setStyleAttribute(head.getElement(), "fontWeight", "bold");
	}

	public void drawDetails(rdfInstance instance, boolean run) {
		if (instance == subjectInstance) {
			if (run) {
				sDetails = new DetailsPanel(instance, subject.getAbsoluteLeft(),
						subject.getAbsoluteTop());
				sDetails.run(5000);
			} else
				sDetails.destroy();

		} else {
			if (run) {
				oDetails = new DetailsPanel(instance, object.getAbsoluteLeft(),
						object.getAbsoluteTop());
				oDetails.run(5000);
			} else
				oDetails.destroy();
		}
	}

	public void drawDetailsImage(rdfInstance instance, boolean run) {
		if (instance == subjectInstance
				&& link.getLinkSpecification().contains("dbpedia-linkedgeodata") == false) {
			if (run) {
				sDetails = new DetailsPanel(instance, imgSub.getAbsoluteLeft(),
						imgSub.getAbsoluteTop());
				sDetails.run(5000);
			} else
				sDetails.destroy();

		} else {
			if (run) {
				oDetails = new DetailsPanel(instance, imgOb.getAbsoluteLeft(),
						imgOb.getAbsoluteTop());
				oDetails.run(5000);
			} else
				oDetails.destroy();
		}
	}

	public void echo(String s) {
		System.out.println("##KongregatePanel: " + s);
	}

	public void update(rdfStatement link) {
		// Remove DetailsPanel
		if (sDetails != null) {
			sDetails.destroy();
		}
		if (oDetails != null) {
			oDetails.destroy();
		}

		this.link = link;
		this.subjectInstance = link.getSubject();
		this.objectInstance = link.getObject();

		// update Question
		updateQuestion();
	}

	private void updateQuestion() {
		if (link.getLinkSpecification().contains("dbpedia-bbcwildlife")) {
			imgSub.setUrl(parseImage(subjectInstance.getImage()));
			imgOb.setUrl(parseImage(objectInstance.getImage()));
			imgSub.addStyleName("kongregateImage");
			imgOb.addStyleName("kongregateImage");
			DOM.setStyleAttribute(imgSub.getElement(), "border", "1px solid black");
			DOM.setStyleAttribute(imgOb.getElement(), "border", "1px solid black");
			// imgSub.setHeight("130px");
			// imgOb.setHeight("130px");

			imgSub.addErrorHandler(new ErrorHandler() {
				public void onError(ErrorEvent event) {
					imgSub.setUrl("Application/images/noImage.png");
					imgSub.setTitle("Error while loading image!");
				}
			});

			imgOb.addErrorHandler(new ErrorHandler() {
				public void onError(ErrorEvent event) {
					imgOb.setUrl("Application/images/noImage.png");
					imgOb.setTitle("Error while loading image!");
				}
			});

			question.clear();
			question.add(imgSub);
			question.add(predicate);
			question.add(imgOb);

		} else if (link.getLinkSpecification().contains("dbpedia-factbook")) { // dbpedia-factbook
			this.subject.setHTML(subjectInstance.getLabel());
			this.object.setHTML(objectInstance.getLabel());
		} else if (link.getLinkSpecification().contains("dbpedia-linkedgeodata")) { // dbpedia-factbook
			imgSub.setUrl(parseImage(subjectInstance.getImage()));

			imgSub.addStyleName("kongregateImage");

			DOM.setStyleAttribute(imgSub.getElement(), "border", "1px solid black");
			imgSub.setWidth("210px");

			imgSub.addErrorHandler(new ErrorHandler() {
				public void onError(ErrorEvent event) {
					imgSub.setUrl("Application/images/noImage.png");
					imgSub.setTitle("Error while loading image!");
				}
			});

			initializeMap(objectTemplate, objectInstance.getLatitude(),
					objectInstance.getLongitude());
			question.clear();
			question.add(imgSub);
			question.add(predicate);
			question.add(mapWidget);
			// Resize mapWidget
			LatLng CENTER = LatLng.newInstance(objectInstance.getLatitude(),
					objectInstance.getLongitude());
			MapHandlerRegistration.trigger(mapWidget, MapEventType.RESIZE);
			mapWidget.setCenter(CENTER);
		}

		this.predicate.setHTML(parsePredicate(link.getPredicate()));
		
		this.linkDifficulty.setHTML("<b>Difficulty: <i>"+Balancing.getStringLinkDifficulty(link.getDifficulty())+"</i></b>");
		// question.clear();
		// question.add(subject);
		// question.add(predicate);
		// question.add(object);
		//
		// RootPanel.get("subject").clear();
		// RootPanel.get("subject").add(this);
	}

	private String parseImage(String image) {
		if(image == null)
			return image;
		image = image.replaceAll("<", "");
		image = image.replaceAll(">", "");
		System.out.println("parseImage q: " + image);
		return image;
	}

	private HTML parseInstance(Template template, rdfInstance instance) {
		String uri = null;
		String label = null;
		String type = null;
		String specific = null;
		HTML parsed = null;
		if (config.getKnowledgeMode().equals(Configuration.KNOWLEDGE_EXPERT)) {
			uri = new String("<b>URI</b> >> <a href=" + instance.getUri()
					+ " target=" + '"' + "_blank" + '"' + ">" + instance.getUri()
					+ "</a>");

			label = new String("<i><b>" + template.getProp0() + "</b></i>" + " >> "
					+ instance.getLabel());
			type = new String("<i><b>" + template.getProp1() + "</b></i>" + " >> "
					+ instance.getType());
			if (instance.getOptional().length() > 0)
				specific = new String("<i><b>" + template.getProp2() + ">> </b></i>"
						+ instance.getOptional());
			else
				specific = new String("<i><b>" + template.getProp2() + ">> </b></i>"
						+ "no value");
			parsed = new HTML(uri + "<br>" + label + "<br>" + type + "<br>"
					+ specific);
		} else {
			uri = new String("<b>Label:</b> <a href=" + instance.getUri()
					+ " target=" + '"' + "_blank" + '"' + ">" + instance.getLabel()
					+ "</a>");

			type = new String("<i><b>Type:</b></i> " + instance.getType());
			if (!instance.getOptional().isEmpty()) {
				specific = new String("<i><b>" + template.getProp2() + ": </b></i>"
						+ instance.getOptional());
				parsed = new HTML(uri + "<br>" + type + "<br>" + specific);
			} else
				parsed = new HTML(uri + "<br>" + type);

		}
		return parsed;
	}

	public String parsePredicate(String pred) {
		String parsed = pred.substring(pred.lastIndexOf('/') + 1);
		// TODO here
		if (parsed.equals("sameAs"))
			parsed = "same as";
		else if (parsed.equals("owl:sameAs"))
			parsed = "same as";
		else if (parsed.equals("spokenIn"))
			parsed = "spoken in";
		else if (parsed.equals("owl#sameAs"))
			parsed = "same as";
		else if (parsed.equals("spatial#P"))
			parsed = "is part of";
		return ("<b>" + parsed + "</b>");
	}

	private void initializeMap(TemplateInstance template, double latitude,
			double longitude) {
		LatLng CENTER = LatLng.newInstance(latitude, longitude);

		final MapOptions options = MapOptions.newInstance();
		// Zoom level. Required
//		options.setZoom(Integer.parseInt(template.getZoom())); hurr
		options.setZoom(6);
		// Open a map centered on Cawker City, KS USA. Required
		options.setCenter(CENTER);
		// Map type. Required.
		options.setMapTypeId(MapTypeId.TERRAIN);

		// Enable maps drag feature. Disabled by default.
		options.setDraggable(true);
		// Enable and add default navigation control. Disabled by default.
		// options.setNavigationControl(true);
		// Enable and add map type control. Disabled by default.
		options.setMapTypeControl(true);
		this.mapWidget = new MapWidget(options);
		mapWidget.setStyleName("kongregateMap");
		mapWidget.setSize("460px", "270px");
		DOM.setStyleAttribute(mapWidget.getElement(), "border", "1px solid black");
		DOM.setStyleAttribute(mapWidget.getElement(), "margin", "10px");
		MarkerOptions markerOptions = MarkerOptions.newInstance();
		markerOptions.setPosition(CENTER);
		markerOptions.setTitle(parseLabel(link.getObject().getLabel()));

		final Marker marker = Marker.newInstance(markerOptions);
		marker.setMap(mapWidget);

		marker.addClickHandler(new ClickMapHandler() {
			public void onEvent(ClickMapEvent event) {
				// drawInfoWindow(marker, event.getMouseEvent(),mapWidget);
			}
		});

		// Panels
		mapInfoPanel = new VerticalPanel();
		mapInfoPanel.add(new HTML(link.getObject().getUri()));
		if (config.getKnowledgeMode().equals(Configuration.KNOWLEDGE_EXPERT))
			mapInfoPanel.add(new HTML(link.getObject().getLabel()));
		else
			mapInfoPanel.add(new HTML(link.getObject().getType()));
		if (link.getObject().getUri().length() > 85)
			mapInfoPanel.setWidth("400px");
		mapInfoPanel.setStyleName("instancePanel_mapInfoPanel");

		int finalLeft;
		echo("@@@top" + top);

		// Resize mapWidget
		MapHandlerRegistration.trigger(mapWidget, MapEventType.RESIZE);
		echo("######resize!!!!");
		mapWidget.setCenter(CENTER);

	}

	private String parseLabel(String newLabel) {
		echo("\n\n\nnewLabel: " + newLabel);
		String end = "booya";
		if (newLabel == null)
			return "none";
		if (newLabel.contains("<FONT style='BACKGROUND-COLOR: yellow'>")) {
			echo("contains font");
			end = newLabel.replace("<FONT style='BACKGROUND-COLOR: yellow'>", "");
			end = end.replace("</FONT>", "");
		} else
			end = newLabel;
		if (end.contains(" | ")) {
			echo("contains | ");
			int size = end.split(" | ").length;
			String split[] = new String[size];
			split = end.split(" | ");
			end = split[0];
		}

		echo("endLabel: " + end);
		String test = "<FONT style='BACKGROUND-COLOR: yellow'><FONT style='BACKGROUND-COLOR: yellow'><FONT style='BACKGROUND-COLOR: yellow'><FONT style='BACKGROUND-COLOR: yellow'><FONT style='BACKGROUND-COLOR: yellow'><FONT style='BACKGROUND-COLOR: yellow'><FONT style='BACKGROUND-COLOR: yellow'><FONT style='BACKGROUND-COLOR: yellow'><FONT style='BACKGROUND-COLOR: yellow'><FONT style='BACKGROUND-COLOR: yellow'>Republic of Chile</FONT></FONT></FONT></FONT></FONT></FONT></FONT></FONT></FONT></FONT>";
		echo("\n\n\n1.: " + test);

		test = test.replace("<FONT style='BACKGROUND-COLOR: yellow'>", "back");
		echo("2: " + test + "\n\n");
		return end;
	}

}
