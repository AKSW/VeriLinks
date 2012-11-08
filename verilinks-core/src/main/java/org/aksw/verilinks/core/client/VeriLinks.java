package org.aksw.verilinks.core.client;

import java.util.ArrayList;

import org.aksw.verilinks.core.client.gui.TaskPanel;

import org.aksw.verilinks.core.client.gui.AdminPanel;

import org.aksw.verilinks.core.shared.Configuration;
import org.aksw.verilinks.core.shared.jso.JsoTask;
import org.aksw.verilinks.core.shared.jso.JsoTaskArray;
import org.aksw.verilinks.core.shared.msg.Task;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class VeriLinks implements EntryPoint {

	private Configuration config = null;
	private boolean serverRunning = false;

	private ArrayList<Task> taskList = new ArrayList<Task>();
	private TaskPanel taskPanel;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		echo("Start Client");
		this.config = new Configuration();

		// Check if page called from kongregate -> set in configuration
		String urlParam = com.google.gwt.user.client.Window.Location
				.getParameter("kongregate");
		// Window.alert("urlParam: "+urlParam);
		echo("Check URL Pram(s): "+urlParam);
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
		echo("Check Status");
		String url = "/VeriLinks/rest?service=checkStatus";
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
				URL.encode(url));
		try {
			Request request = builder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					// Couldn't connect to server (could be timeout, SOP
					// violation, etc.)
					Window.alert("Couldn't connect to server!");
				}

				public void onResponseReceived(Request request,
						Response response) {
					echo(response.getText());
				}
			});
		} catch (RequestException e) {
			// Couldn't connect to server
			echo("ERROR!");
		}
	}

	private void init() {
		echo("Init");
		Button adminButton = new Button("Admin");
		adminButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent arg0) {
				loadAdmin();
			}
		});
		Button highscoreButton = new Button("Highscores");
		highscoreButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent arg0) {

			}
		});

		RootPanel.get("menue").add(adminButton);
		RootPanel.get("menue").add(highscoreButton);

		Button b = new Button("Rest");
		b.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent arg0) {
				// restTest();
				getTasks();
			}

		});
		RootPanel.get().add(b);
	}

	/** Initialize admin menu */
	private void loadAdmin() {
		HTML head = new HTML("<b>Start Admin Panel</b>");
		Button addLinksButton = new Button("Add Links");
		addLinksButton.addStyleName("myButton");
		addLinksButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent arg0) {
				loadAddLinks();

			}
		});

		Button processTaskButton = new Button("Process Link Tasks");
		processTaskButton.addStyleName("myButton");
		processTaskButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent arg0) {
				getTasks();

			}
		});

		VerticalPanel infoPanel = new VerticalPanel();
		HTML summary = new HTML(
				"You can verify the semantic web links that are already in our database. "
						+ "But you can also upload your own links! Just add a rdf or a n-triple file containing your links to our server");
		summary.setStyleName("startPanel_summary");
		DOM.setStyleAttribute(summary.getElement(), "padding", "3px");
		DOM.setStyleAttribute(summary.getElement(), "border",
				"1px dashed lightgray");
		DOM.setStyleAttribute(summary.getElement(), "border", "1px dashed gray");

		HTML infoAddLinks = new HTML(
				"[Add Link Task]<br>"
						+ "Add a new link task to the Veri-Links database. "
						+ "Upload a rdf or n-triple file containing the semantic web links. "
						+ "And specify the data of the interlinked ontologies");
		HTML infoProcessTasks = new HTML(
				"[Process Link Tasks]<br>"
						+ "Process the pending links tasks, new links will be added to the Veri-Links database.");
		infoPanel.add(summary);
		infoPanel.add(infoAddLinks);
		infoPanel.add(addLinksButton);

		infoPanel.add(infoProcessTasks);
		infoPanel.add(processTaskButton);
		infoPanel.setSpacing(8);
		VerticalPanel adminPanel = new VerticalPanel();
		DOM.setStyleAttribute(adminPanel.getElement(), "backgroundColor",
				"white");
		DOM.setStyleAttribute(adminPanel.getElement(), "padding", "10px");

		// adminPanel.setStyleName("Menu_AdminPanel");
		adminPanel.add(head);
		adminPanel.add(infoPanel);
		adminPanel.setWidth("400px");

		PopupPanel pop = new PopupPanel(true);
		pop.add(adminPanel);
		pop.center();
		pop.show();

	}

	private void loadAddLinks() {
		AdminPanel adminPanel = new AdminPanel();
		final PopupPanel pop = new PopupPanel(false);
		pop.add(adminPanel);
		final PopupPanel adminGlass = new PopupPanel(false);
		adminGlass.setStyleName("rx-glass");
		DOM.setStyleAttribute(adminGlass.getElement(), "width", "100%");
		DOM.setStyleAttribute(adminGlass.getElement(), "height", "100%");
		DOM.setStyleAttribute(adminGlass.getElement(), "backgroundColor",
				"#000");
		adminGlass.add(pop);
		// pop.setStyleName("noBorder");
		adminPanel.setQuitClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				pop.hide();
				adminGlass.hide();
			}
		});
		adminGlass.show();
		pop.center();
	}

	private void loadTasks() {

		taskPanel = new TaskPanel(taskList);

		// glass panel behind popUp
		final PopupPanel adminGlass = new PopupPanel(false);
		adminGlass.setStyleName("rx-glass");
		DOM.setStyleAttribute(adminGlass.getElement(), "width", "100%");
		DOM.setStyleAttribute(adminGlass.getElement(), "height", "100%");
		DOM.setStyleAttribute(adminGlass.getElement(), "backgroundColor",
				"#000");

		final PopupPanel menu2 = new PopupPanel(false);
		menu2.add(taskPanel);
		// menu2.setStyleName("noBorder");
		taskPanel.setQuitClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				adminGlass.hide();
				menu2.hide();
			}

		});

		taskPanel.setOkClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				taskPanel.disableOkButton();
				taskPanel.enableQuitButton(false);
				taskPanel
						.setResult("This action will take a couple minutes or hours. Please have patience.. ");
				performTasks();

			}

		});
		adminGlass.show();
		menu2.center();

	}

	private void getTasks() {
		String url = "/VeriLinks/rest?service=getTasks";

		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
				URL.encode(url));
		// builder.setHeader("Content-Type",
		// "application/x-www-form-urlencoded");
		// builder.setHeader("Access-Control-Allow-Origin", "*");
		echo("REST: " + url);
		try {
			Request request = builder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					// Couldn't connect to server (could be timeout, SOP
					// violation, etc.)
					echo("ERROR rest");
					Window.alert("Error: Couldn't retrieve Task. "
							+ exception.getMessage());
				}

				public void onResponseReceived(Request request,
						Response response) {
					// TODO Auto-generated method stub
					echo("client SUCCESS rest");
					JSONValue jsonValue = JSONParser.parseStrict(response
							.getText());
					System.out.println("jsonValue: " + jsonValue.toString());
					JSONObject jsonObject = jsonValue.isObject(); // assert that
																	// this is
																	// an object
					if (jsonObject == null) {
						System.out
								.println("JSON payload did not describe an object");
						throw new RuntimeException(
								"JSON payload did not describe an object");
					} else
						System.out.println("is object");
					// Cast
					System.out.println("#######SUBJECT: ");
					JsoTaskArray hS = jsonObject.getJavaScriptObject().cast();
					System.out.println("casted");
					System.out.println("hs: " + hS.getTasks().length());
					taskList = parseTasks(hS.getTasks());
					loadTasks();
				}
			});
		} catch (RequestException e) {
			// Couldn't connect to server
			echo("ERROR!");
		}
	}

	private void performTasks() {
		echo("Perform Task");
		String url = "/VeriLinks/rest?service=commitVerifications";
		url = "/VeriLinks/rest?service=performTasks";

		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
				URL.encode(url));
		// builder.setHeader("Content-Type",
		// "application/x-www-form-urlencoded");
		// builder.setHeader("Access-Control-Allow-Origin", "*");
		echo("REST: " + url);
		try {
			Request request = builder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					// Couldn't connect to server (could be timeout, SOP
					// violation, etc.)
					echo("ERROR: " + exception.getMessage());
					Window.alert("ERROR Performing Task: "
							+ exception.getMessage());
					taskPanel.enableQuitButton(true);
					taskPanel.setResult("Error!");
				}

				public void onResponseReceived(Request request,
						Response response) {
					// TODO Auto-generated method stub
					echo("Perform Task success!");
					Window.alert(response.getText());

					taskPanel.enableQuitButton(true);
					taskPanel
							.setResult("All the tasks have completed succesfully! Please refresh your web-browser");
				}
			});
		} catch (RequestException e) {
			// Couldn't connect to server
			echo("ERROR!");
		}
	}

	private void restTest() {
		String url = "/VeriLinks/rest?service=commitVerifications";
		url = "/VeriLinks/rest?service=getHighscore&game=peas";
		url = "/VeriLinks/rest?service=getLink"
				+ "&userName=foo&userId=username-login: only name available"
				+ "&verifiedLinks=55+33+11+1+2" + "&curLink=2"
				+ "&nextLink=false" + "&verification=1"
				+ "&linkset=dbpedia-linkedgeodata";
		url = "/VeriLinks/rest?service=getLinksets";
		url = "/VeriLinks/rest?service=getTasks";

		String data = "{ " + '"' + "verifiTest" + '"' + ":" + '"' + "Test"
				+ '"' + "}";
		echo(data);

		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
				URL.encode(url));
		// builder.setHeader("Content-Type",
		// "application/x-www-form-urlencoded");
		// builder.setHeader("Access-Control-Allow-Origin", "*");
		echo("REST: " + url);
		try {
			Request request = builder.sendRequest(data, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					// Couldn't connect to server (could be timeout, SOP
					// violation, etc.)
					echo("ERROR rest");
				}

				public void onResponseReceived(Request request,
						Response response) {
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

	public ArrayList<Task> parseTasks(JsArray<JsoTask> taskArray) {
		echo("Client: Parse Tasks. Size = " + taskArray.length());
		ArrayList<Task> taskList = new ArrayList<Task>();
		JsoTask t = null;
		Task task = null;
		for (int i = 0; i < taskArray.length(); i++) {
			t = taskArray.get(i);
			task = new Task();
			task.setSubject(t.getSubject());
			task.setObject(t.getObject());
			task.setPredicate(t.getPredicate());
			task.setDescription(t.getDescription());
			task.setDifficulty(t.getDifficulty());
			task.setFile(t.getFile());

			taskList.add(task);
		}
		// Test taskList
		for (int i = 0; i < taskList.size(); i++) {
			Task t1 = taskList.get(i);
			echo(t1.getSubject() + " . " + t1.getObject() + " . "
					+ t1.getFile());
		}

		return taskList;
	}

	private void echo(String m) {
		System.out.println("[Client]: " + m);
	}
}
