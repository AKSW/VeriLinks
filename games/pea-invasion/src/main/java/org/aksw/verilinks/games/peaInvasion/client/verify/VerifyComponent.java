package org.aksw.verilinks.games.peaInvasion.client.verify;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import org.aksw.verilinks.games.peaInvasion.shared.Configuration;
import org.aksw.verilinks.games.peaInvasion.shared.PropertyConstants;
//import org.aksw.verilinks.games.peaInvasion.shared.Template;
import org.aksw.verilinks.games.peaInvasion.shared.rdfInstance;
import org.aksw.verilinks.games.peaInvasion.shared.rdfStatement;
import org.aksw.verilinks.games.peaInvasion.shared.msg.Instance;
import org.aksw.verilinks.games.peaInvasion.shared.msg.Link;
import org.aksw.verilinks.games.peaInvasion.shared.templates.TemplateInstance;
import org.aksw.verilinks.games.peaInvasion.shared.templates.TemplateLinkset;

public class VerifyComponent extends HorizontalPanel {

	private Configuration config;
	private Button verifyButton;

	private PushButton trueButton;
	private PushButton falseButton;
	private PushButton unsureButton;

	private static final CharSequence SEPERATOR = PropertyConstants.SEPERATOR_PROPERTY_VALUE;

	private HTML predicateHTML;

	// Panel
	private VerticalPanel subPanel;
	private VerticalPanel obPanel;
	private VerticalPanel predicatePanel;

	private VerticalPanel subjectImagePanel;
	private VerticalPanel objectImagePanel;
	private final String noImage = "PeaInvasion/images/noImage.png";

	private TemplateInstance subjectTemplate;
	private TemplateInstance objectTemplate;
	private TemplateLinkset template;

	private boolean isBothMapInstances = false;
	private BothMapsPanel bothMapsPanel;

	private KongregatePanel kongregate;

	/**
	 * Constructor
	 * 
	 * @param model
	 */
	public VerifyComponent(TemplateLinkset template, Configuration config) {

		super();

		this.template = template;
		this.subjectTemplate = template.getSubject();
		this.objectTemplate = template.getObject();
		this.config = config;
		init();

		this.setStyleName("verifyComponent");

	}

	public void updateStatement(Link stmt, TemplateLinkset temp) {

		System.out.println("\n##VerifyComponent: Update Table##");

		System.out.println("Using templates: "
				+ temp.getSubject().getOntology() + "-"
				+ temp.getObject().getOntology() + "'");

		Instance instanceSubject = stmt.getSubject();
		Instance instanceObject = stmt.getObject();

		// here
		// if(config.isKongregate() || config.isSimple())
		// updateKongregate(stmt);
		// else if (!isBothMapInstances)
		// updateNormal(stmt);
		// else
		// updateBothMaps(stmt);
		updateNormal(stmt);

		// Predicate panel
		DOM.getElementById("predicate").setInnerHTML(
				parsePredicate(stmt.getPredicate()));

		// Image panel
		// Subject image
		subjectImagePanel.clear();
		String buffer = null;
		String sParsedImage = parseImage(instanceSubject.getImage());
		System.out
				.println("Image Size: " + instanceSubject.getImage().length());

		// TODO length of NO_DECLARATION ?
		if (sParsedImage == null)
			// No image available
			subjectImagePanel.add(new HTML("<img class='image' src='" + noImage
					+ "' title='no image available' />"));
		else if (instanceSubject.getImage().length() > 10
				&& !sParsedImage.equals(PropertyConstants.NO_DECLARATION)) // Image
																			// available
		{
			buffer = "<a class='small' href='#nogo'>" + "<img src='"
					+ sParsedImage + "' title='subject image' />"
					+ "<img class='large' src='" + sParsedImage + "'  /></a>";
			subjectImagePanel.add(new HTML(buffer));
			System.out.println("CMON: " + buffer);
		} else
			// No image available
			subjectImagePanel.add(new HTML("<img class='image' src='" + noImage
					+ "' title='no image available' />"));

		// Object image
		objectImagePanel.clear();
		String oParsedImage = parseImage(instanceObject.getImage());
		// Image available
		if (oParsedImage == null) {
			// No image available
			objectImagePanel.add(new HTML("<img class='image' src='" + noImage
					+ "' title='no image available' />"));
		} else if (oParsedImage.length() > 10
				&& !oParsedImage.equals(PropertyConstants.NO_DECLARATION)) {
			buffer = "<a class='small' href='#nogo' title='small object image'>"
					+ "<img src='"
					+ oParsedImage
					+ "' title='object image' />"
					+ "<img class='large' src='" + oParsedImage + "'  /></a>";
			objectImagePanel.add(new HTML(buffer));
		} else
			// No image available
			objectImagePanel.add(new HTML("<img class='image' src='" + noImage
					+ "' title='no image available' />"));

		System.out.println("##VerifyCOmponent: Update Table done ##");

	}

	/**
	 * Update statement in VerifyComponent for text-type instances. Or 1
	 * text-type and 1 map-type instance.
	 * 
	 * @param newLink
	 */
	private void updateNormal(Link stmt) {
		echo("Update normal");
		Instance instanceSubject = stmt.getSubject();
		Instance instanceObject = stmt.getObject();
		System.out.println("Set subject panel");
		subPanel.clear();
		subPanel.add(new HTML(removeDuplicate(instanceSubject.getLabel())));
		// subPanel.add(new HTML(instanceSubject.getUri()));
		// subPanel.add(new HTML(instanceSubject.getOptional()));

		echo("subjectTemplate type: " + subjectTemplate.getType());
		if (subjectTemplate.getType().equals("map")) {
			subPanel.add(new HTML("MAPTYPE"));
			// subjectPanel.setLatitude(instanceSubject.getLatitude());
			// subjectPanel.setLongitude(instanceSubject.getLongitude());
		}
		System.out.println("Subject Uri: " + instanceSubject.getUri());
		System.out.println("Set subject panel done");

		System.out.println("Set object panel");
		obPanel.clear();
		obPanel.add(new HTML(removeDuplicate(instanceObject.getLabel())));
		// obPanel.add(new HTML(instanceObject.getUri()));
		// obPanel.add(new HTML(instanceObject.getOptional()));
		echo("objectTemplate.getType(): " + objectTemplate.getType());
		if (objectTemplate.getType().equals("map")) {
			// obPanel.add(new HTML("map"));
			// objectPanel.setLatitude(instanceObject.getLatitude());
			// objectPanel.setLongitude(instanceObject.getLongitude());
		}
		System.out.println("Object Uri: " + instanceObject.getUri());
		System.out.println("Set object panel done");

	}

	/**
	 * Update statement in VerifyComponent for both map-type instances
	 * 
	 * @param link
	 */
	private void updateBothMaps(rdfStatement link) {
		echo("Update both");
		if (config.equals(Configuration.KNOWLEDGE_EXPERT))
			this.bothMapsPanel.update(link);
		else {
			rdfInstance sub = link.getSubject();
			rdfInstance ob = link.getObject();

			sub.setType(removeNamespace(sub.getType()));
			ob.setType(removeNamespace(ob.getType()));

			rdfStatement stmt = link;
			link.setSubject(sub);
			link.setObject(ob);
			this.bothMapsPanel.update(link);
		}
	}

	/**
	 * Update statement in VerifyComponent for both map-type instances
	 * 
	 * @param link
	 */
	private void updateKongregate(rdfStatement link) {
		echo("Update Kongregate");
		rdfInstance sub = link.getSubject();
		rdfInstance ob = link.getObject();

		sub.setType(removeNamespace(sub.getType()));
		ob.setType(removeNamespace(ob.getType()));

		rdfStatement stmt = link;
		link.setSubject(sub);
		link.setObject(ob);

		this.kongregate.update(stmt);
	}

	private String removeDuplicate(String value) {
		echo("Remove duplicate: " + value);
		String delimiter = " \\; ";
		String seperator = " ; ";
		String dummy = "";
		String parsed = "";
		int size = value.split(delimiter).length;
		// echo("VerifyComponent: removeDuplicate size: " + size);

		String[] split = new String[size];
		split = value.split(delimiter);
		// 0
		parsed = findDuplicate(split[0], "");
		// 1
		for (int i = 1; i < size; i++) {
			dummy = findDuplicate(split[i], parsed);
			if (!dummy.isEmpty()) {
				if (parsed.isEmpty())
					parsed += dummy;
				else
					parsed += seperator + dummy;
			}
		}
		echo("Duplicate removed: " + parsed);
		return parsed;
	}

	private String findDuplicate(String value, String endString) {
		// echo("FindDuplicate value: " + value+" , end: "+endString);
		String parsed = value;
		if (endString.contains(parsed) || parsed.contains("??"))
			return "";
		return parsed;
	}

	private String removeNamespace(String value) {
		echo("Remove Namespace: " + value);
		String delimiter = " \\; ";
		String seperator = " ; ";
		String dummy = "";
		String parsed = "";
		int size = value.split(delimiter).length;
		// echo("VerifyComponent: removeNamespace size: " + size);

		String[] split = new String[size];
		split = value.split(delimiter);
		// 0
		parsed = findNamespace(split[0], "");
		// 1
		for (int i = 1; i < size; i++) {
			dummy = findNamespace(split[i], parsed);
			if (!dummy.isEmpty())
				parsed += seperator + dummy;

		}
		echo("namepsace removed: " + parsed);
		return parsed;
	}

	private String findNamespace(String value, String endString) {
		echo("FindNamespace value: " + value);
		String parsed;
		if (value.contains(":"))
			parsed = value.substring(value.indexOf(":") + 1);
		else
			parsed = value;
		echo("FindNamespace parsed: " + parsed);
		if (endString.contains(parsed))
			return "";
		return parsed;
	}

	// TODO server does highlight. but client should do that. hosted and web
	// mode
	// compatibility
	/**
	 * Highlight similarities between subject and object
	 * 
	 * @param tempSubject
	 * @param tempObject
	 * @return
	 */
	private rdfStatement highlight(rdfStatement link) {
		System.out.println("Client: highlight matches");
		rdfInstance subject = link.getSubject();
		rdfInstance object = link.getObject();

		// Label
		String sLabel = subject.getLabel();
		String sLabelLowerCase = subject.getLabel().toLowerCase();
		String oLabel = object.getLabel();
		// oLabel+=SEPERATOR+"Harpyija (lintu)"+SEPERATOR+"Harpia";
		String oLabelLowerCase = object.getLabel().toLowerCase();
		System.out.println("Label sub: " + sLabel);
		System.out.println("Label ob: " + oLabel);

		// Type
		String sType = subject.getType();
		String sTypeLowerCase = subject.getType().toLowerCase();
		String oType = object.getType();
		// oType+=SEPERATOR+"Harpyija (lintu)"+SEPERATOR+"Harpia";
		String oTypeLowerCase = object.getType().toLowerCase();
		System.out.println("Type sub: " + sType);
		System.out.println("Type ob: " + oType);

		// Specific
		String sSpecific = subject.getOptional();
		String sSpecificLowerCase = sSpecific.toLowerCase();
		String oSpecific = object.getOptional();
		String oSpecificLowerCase = oSpecific.toLowerCase();
		System.out.println("Optional sub: " + sSpecific);
		System.out.println("Optional ob: " + oSpecific);

		// count properties
		// if (sLabel.contains(SEPERATOR) || oLabel.contains(SEPERATOR))
		{
			System.out.println("sLabel and o Label got SEP - SPLIT\n");
			System.out.println("Sep:.." + SEPERATOR + "..\n\n\n");
			// count properties

			// Split oLabel
			String[] properties = oLabel
					.split(PropertyConstants.SEPERATOR_PROPERTY_VALUE);
			for (int i = 0; i < properties.length; i++) {
				System.out.println("Split o " + i + ": " + properties[i]);
				// label
				if (sLabel.toLowerCase().contains(properties[i].toLowerCase())) {
					sLabel = highlightText(sLabel, properties[i]);
					oLabel = highlightText(oLabel, properties[i]);
				}
				// subject specific
				if (sSpecific.toLowerCase().contains(
						properties[i].toLowerCase())) {
					sSpecific = highlightText(sSpecific, properties[i]);
					// oSpecific = highlightText(oSpecific,properties[i]);
					oLabel = highlightText(oLabel, properties[i]);
				}

			}
			// Split sLabel
			properties = sLabel
					.split(PropertyConstants.SEPERATOR_PROPERTY_VALUE);
			for (int i = 0; i < properties.length; i++) {
				System.out.println("Split s " + i + ": " + properties[i]);

				// subject specific
				if (oSpecific.toLowerCase().contains(
						properties[i].toLowerCase())) {
					// sSpecific = highlightText(sSpecific,properties[i]);
					oSpecific = highlightText(oSpecific, properties[i]);
					sLabel = highlightText(sLabel, properties[i]);
				}

			}

		}

		// Label
		sLabel = sLabel.replace(SEPERATOR, " | ");
		oLabel = oLabel.replace(SEPERATOR, " | ");

		link.getSubject().setLabel(sLabel);
		link.getObject().setLabel(oLabel);

		// Type
		sType = sType.replace(SEPERATOR, " | ");
		oType = oType.replace(SEPERATOR, " | ");

		link.getSubject().setType(sType);
		link.getObject().setType(oType);

		// Specific
		sSpecific = sSpecific.replace(SEPERATOR, " | ");
		oSpecific = oSpecific.replace(SEPERATOR, " | ");

		link.getSubject().setOptional(sSpecific);
		link.getObject().setOptional(oSpecific);

		System.out.println("-------------------------\n\n");
		return link;
	}

	private void echo(String string) {
		System.out.println(string);
	}

	private String highlightText(String label, String replace) {
		System.out.println("Highlight label: " + label);
		System.out.println("Highlight replace: " + replace);
		// search position
		int pos = label.indexOf(replace);
		// add tag
		String buffer = null;

		if (replace.contains("(")) {
			// split
			buffer = label.replace(replace,
					"<FONT style='BACKGROUND-COLOR: yellow'>" + replace
							+ "</FONT>");
		} else {
			String pattern = "(?i)(" + replace + ")";
			System.out.println("pattern: " + pattern);
			buffer = label.replaceAll(pattern,
					"<FONT style='BACKGROUND-COLOR: yellow'>" + replace
							+ "</FONT>");
		}
		System.out.println("buffer highlighText: " + buffer);

		return buffer;
	}

	private String highlightText(String label) {
		return "<FONT style='BACKGROUND-COLOR: yellow'>" + label + "</FONT>";
	}

	// Init checkboxes and queries
	private void init() {
		echo("##VerifyComponent: Init VerifyComponent");
		echo("subjectTemplate type: " + subjectTemplate.getType());
		echo("objectTemplate type: " + objectTemplate.getType());
		// Subject and object Both map-type instances
		// if (config.isKongregate() || config.isSimple()){
		// initKongregate();
		// }
		// else{
		// if
		// (subjectTemplate.getType().equals(PropertyConstants.TEMPLATE_TYPE_MAP)
		// == true
		// &&
		// objectTemplate.getType().equals(PropertyConstants.TEMPLATE_TYPE_MAP)
		// == true) {
		// this.isBothMapInstances = true;
		// initBothMapInstances();
		// }
		// else { // at least 1 text-type instance
		// this.isBothMapInstances = false;
		// initNormal();
		// }
		// }
		initNormal();
		echo("##VerifyComponent: Init VerifyComponent End");
	}

	/**
	 * Init method for at least 1 text-type instance
	 */
	private void initNormal() {
		echo("Init Normal");
		this.setSpacing(2);

		this.subPanel = new VerticalPanel();
		DOM.getElementById("subject").setInnerHTML("");
		RootPanel.get("subject").add(subPanel);

		this.obPanel = new VerticalPanel();
		DOM.getElementById("object").setInnerHTML("");
		RootPanel.get("object").add(obPanel);

		// predicate (middlePanel)
		initMiddlePanel();

		// image Panels
		initImagePanel();
	}

	/**
	 * Init method for at least 1 text-type instance
	 */
	private void initKongregate() {
		echo("Init Kongregate");
		this.setSpacing(2);

		// Kongregate Panel
		kongregate = new KongregatePanel(template, config);

		// predicate (middlePanel)
		initMiddlePanel();

		// image Panels
		initImagePanel();

	}

	/**
	 * Init method for 2 map-type instances
	 */
	private void initBothMapInstances() {
		echo("Init both maps");
		bothMapsPanel = new BothMapsPanel(template, config);

		initMiddlePanel();

		initImagePanel();

		echo("Init both maps end");
	}

	private void initMiddlePanel() {
		echo("Init middle panel");

		this.predicatePanel = new VerticalPanel();
		HTML predicateHeader = new HTML("<b>Predicate</b>");
		String predicateString = "<i>Predicate</i>";
		predicateHTML = new HTML(predicateString);
		predicatePanel.add(predicateHeader);
		predicatePanel.add(predicateHTML);
		predicatePanel.setStyleName("predicatePanel");

		// ButtonGroup
		HorizontalPanel r1Panel = new HorizontalPanel();
		r1Panel.setHorizontalAlignment(ALIGN_CENTER);
		trueButton = new PushButton(new Image(
				"PeaInvasion/images/verification/trueButton.png"));
		r1Panel.add(trueButton);

		falseButton = new PushButton(new Image(
				"PeaInvasion/images/verification/falseButton.png"));
		HorizontalPanel r2Panel = new HorizontalPanel();
		r2Panel.setHorizontalAlignment(ALIGN_CENTER);
		r2Panel.add(falseButton);

		HorizontalPanel r3Panel = new HorizontalPanel();
		r3Panel.setHorizontalAlignment(ALIGN_CENTER);
		// // r3Panel.add(rdbtnNotSure);
		// Image i3 = new Image("Application/images/verification/unsure.png");
		// // i3.setSize("25px", "25px");
		// Image k3 = new Image("Application/images/verification/key3.png");
		// r3Panel.add(k3);
		// r3Panel.add(i3);
		// r3Panel.setSpacing(2);
		unsureButton = new PushButton(new Image(
				"PeaInvasion/images/verification/unsureButton.png"));
		r3Panel.add(unsureButton);

		VerticalPanel rdbtnPanel = new VerticalPanel();
		rdbtnPanel.setStyleName("RdbtnPanel");
		rdbtnPanel.setVerticalAlignment(ALIGN_MIDDLE);
		rdbtnPanel.add(r1Panel);
		rdbtnPanel.add(r2Panel);
		rdbtnPanel.add(r3Panel);

		rdbtnPanel.setSpacing(5);
		DOM.setStyleAttribute(rdbtnPanel.getElement(), "marginBottom", "3px");
		// verifyButton
		verifyButton = new Button("VERIFY");
		verifyButton.addStyleName("myButton");

		VerticalPanel middlePanel = new VerticalPanel();
		middlePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		if (config.isSimple() == false)
			middlePanel.add(predicatePanel);
		middlePanel.add(rdbtnPanel);
		// middlePanel.add(verifyButton);
		middlePanel.setStyleName("middlePanel");		
		RootPanel.get("verifyButtons").add(middlePanel);
		echo("Init middle panel done");
	}

	private void initImagePanel() {
		echo("Init image panel");
		// subject image
		subjectImagePanel = new VerticalPanel();
		subjectImagePanel.add(new HTML("<img class='image' src='" + noImage
				+ "'/>"));

		// object image
		objectImagePanel = new VerticalPanel();
		objectImagePanel.add(new HTML("<img class='image' src='" + noImage
				+ "'/>"));

		if (!config.isSimple()) {
			RootPanel.get("imageSubject").add(subjectImagePanel);
			RootPanel.get("imageObject").add(objectImagePanel);
		}
		echo("Init image panel done");
	}

	/**
	 * Remove '<' and '>' of an image url
	 * @param image
	 * @return parsed String
	 */
	public String parseImage(String image) {
		System.out.println("PARSE IMAGE: " + image);
		if (image == null || image.length() < 3)
			return null;
		if (image.equals(PropertyConstants.NO_DECLARATION))
			return image;
		String end = image;
		if (image.contains("<"))
			end = image.substring(1, image.length() - 1);
		System.out.println("parsed: " + end);
		return end;
	}

	/**
	 * Scale Image to a fixed size
	 * 
	 * @param image
	 */
	public void scaleImage(Image image) {

		double heightLimit = 150;
		double scaleRatio = 1;
		double newHeight = 120;
		double newWidth = 150;

		if (image.getHeight() == 0) {// getHeight doesn't work
			newHeight = 120;
			newWidth = 120;
			image.setPixelSize(((int) newWidth), ((int) newHeight));
		} else if (image.getHeight() >= heightLimit) {
			scaleRatio = heightLimit / image.getHeight();
			newHeight = image.getHeight() * scaleRatio;
			newWidth = (image.getWidth() * scaleRatio + 20);
			image.setPixelSize(((int) newWidth), ((int) newHeight));
		}

		System.out.println("width " + image.getWidth() + " height "
				+ image.getHeight() + " scale " + scaleRatio + " nuH "
				+ newHeight + " nuW " + newWidth);
	}

	public Button getOkButton() {
		// TODO Auto-generated method stub
		return this.verifyButton;
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

	public PushButton getTrueButton() {
		return trueButton;
	}

	public PushButton getFalseButton() {
		return falseButton;
	}

	public PushButton getUnsureButton() {
		return unsureButton;
	}

	public void enableButtons() {
		this.trueButton.setEnabled(true);
		this.falseButton.setEnabled(true);
		this.unsureButton.setEnabled(true);
		DOM.setStyleAttribute(trueButton.getElement(), "border", "none");
		DOM.setStyleAttribute(falseButton.getElement(), "border", "none");
		DOM.setStyleAttribute(unsureButton.getElement(), "border", "none");
	}

	public void disableButtons() {
		this.trueButton.setEnabled(false);
		this.falseButton.setEnabled(false);
		this.unsureButton.setEnabled(false);
	}
}
