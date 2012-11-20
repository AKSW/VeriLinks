package org.aksw.verilinks.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.aksw.verilinks.server.msg.Instance;
import org.aksw.verilinks.server.msg.Link;
import org.aksw.verilinks.server.msg.Linkset;
import org.aksw.verilinks.server.msg.Property;
import org.aksw.verilinks.server.msg.Score;
import org.aksw.verilinks.server.msg.Task;
import org.aksw.verilinks.server.msg.TemplateLinkset;
import org.aksw.verilinks.server.msg.User;
import org.aksw.verilinks.server.msg.Userdata;
import org.aksw.verilinks.server.msg.Verification;
import org.aksw.verilinks.server.tools.Balancing;
import org.aksw.verilinks.server.tools.DBTool;
import org.aksw.verilinks.server.tools.Message;
import org.aksw.verilinks.server.tools.PropertyConstants;
import org.aksw.verilinks.server.tools.RDFHandler;
import org.aksw.verilinks.server.tools.XMLTool;
import org.openjena.atlas.json.JSON;
import org.openjena.atlas.json.JsonArray;
import org.openjena.atlas.json.JsonException;
import org.openjena.atlas.json.JsonObject;
import org.openjena.atlas.json.JsonValue;
import org.xml.sax.SAXException;

/**
 * Servlet implementation class Server
 */
public class Server extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Server() {
        super();
        // TODO Auto-generated constructor stub
    }


	/** User List */
	private HashMap<String, User> userList;

	private String GET_USER_SCORE = "getUserScore";
	private String GET_HIGHSCORE = "getHighscore";
	private String GET_USERDATA = "getUserdata"; // connect method
	private String GET_LINK = "getLink";
	private String GET_LINKSETS = "getLinksets";
	private String GET_TEMPLATE = "getTemplate";
	private String GET_TASKS = "getTasks";
	private String PERFORM_TASKS = "performTasks";
	private String CHECK_STATUS = "checkStatus";

	private String DISCONNECT_USER = "disconnectUser";

	private String POST_COMMIT_VERIFICATIONS = "commitVerifications";
	private String POST_SCORE = "postScore";
	private String POST_LEVEL_STATS = "postLevelStats";

	
	private static final int VALID = 1;
	private static final int NOT_VALID = 0;
	private static final int UNSURE = -1;
	private static final int SURE_POSITIVE = 1;
	private static final int SURE_NEGATIVE = -2;

	
	/** Resource path */
	private String resourcePath = null;

	private final String dbIniFile = "db_settings.ini";
	private final String templateFile = "LinkTemplates.xml";
	private final String prefixFile = "prefix.csv";

	private static final double EVAL_POSITIVE = 1;
	private static final double EVAL_NONE = 0;
	private static final double EVAL_NEGATIVE = -1;
	private static final double EVAL_FIRST = -2;


	private boolean taskSuccess;

	/** Prefix Map **/
	private HashMap<String, String> prefixMap;

	@Override
	public void init() throws ServletException {
		super.init();
		echo("Init Server");

		initPath();

		// users
		userList = new HashMap<String, User>();

		// Check if necessary files exist
		if (!checkFiles()) {
			echo("####Server: Initialize Server failed. System files missing");
			return;
		}

		// Check if database exist
		if (!checkDbExist()) {
			createDatabase();
		}

		// Check if MySQL Server is running
		if (!checkMySqlServer()) {
			return;
		} else {
			// Init data
			restart();
		}

	}

	private void restart() {
		initPrefix();
		// this.running = true;
	}

	private boolean checkFiles() {
		echo("##Check if files exist##");
		boolean exist = true;
		// dbIniFile
		if (!(new File(resourcePath + dbIniFile).isFile())) {
			exist = false;
			echo("Error: " + dbIniFile + " not found! ");
		} else
			echo("Found " + dbIniFile);
		// templateFile
		if (!(new File(resourcePath + templateFile).isFile())) {
			exist = false;
			echo("Error: " + templateFile + " not found! ");
		} else
			echo("Found " + templateFile);
		// prefixFile
		if (!(new File(resourcePath + prefixFile).isFile())) {
			exist = false;
			echo("Error: " + prefixFile + " not found! ");
		} else
			echo("Found " + prefixFile);
		echo("##Check if files exist done##\n");
		return exist;
	}

	private boolean checkMySqlServer() {
		echo("##Server: Check if MySQL-Server running##");
		DBTool db = new DBTool(resourcePath + dbIniFile);
		boolean b;
		if (db.getConnection() == null) {
			echo("##Server: ERROR MySQL-Server not running##");
			b = false;
		} else {
			echo("##Server: MySQL-Server is running##");
			b = true;
		}
		try {
			db.getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b;
	}

	private boolean checkDbExist() {
		echo("##Server: Check if veri-links database exists##");
		boolean exist = false;
		// 1.connect to db
		DBTool db = new DBTool(resourcePath + dbIniFile);
		String dbExistQuery = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '"
				+ db.getDatabase() + "'";
		echo("Query: " + dbExistQuery);
		db.setDatabase("");
		db.createUrl();
		Connection con = db.getConnection();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(dbExistQuery);
			if (rs.next()) {
				echo("Veri-Links db exists");
				exist = true;
			} else {
				echo("Veri-Links db doesn't exists");
				exist = false;
			}
		} catch (Exception e) {
			echo("Server Error: Couldn't check whether database exists or not! Check iniFile.");
		}
		echo("##Server: Check if veri-links database exists done##\n");
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return exist;
	}

	private void createDatabase() {
		echo("##Server: Create Database##");
		// 1.connect to db
		DBTool db = new DBTool(resourcePath + dbIniFile);
		try {
			db.createDatabase();
			echo("##Server: Create Database Success##\n");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			echo("##Server: ERROR Create Database Failed##\n");
		}
	}

	public void initPath() {
		echo("####Server: Init Path ####");
		String prefix = getServletContext().getRealPath("");
		if (!prefix.endsWith("/")) {
			prefix += '/';
		}
		resourcePath = prefix + "WEB-INF/resources/";
		echo("####Server: Init Path : '" + resourcePath + "' Done ####\n");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		echo("Processing POST");
		String service = req.getParameter("service");
		echo("Req: " + service);
		String response = null;
		if (service.equals(POST_COMMIT_VERIFICATIONS)) // Commit Verification
			response = commitVerification(req);
		if (service.equals(POST_SCORE)) // Post score
			response = postScore(req.getParameter("userId"),
					req.getParameter("userName"), req.getParameter("score"),
					req.getParameter("game"));
		if (service.equals(POST_LEVEL_STATS)) // Post score
			response = postLevelStats(req);
		resp.addHeader("Access-Control-Allow-Origin", "*");
		resp.getWriter().write(response);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// super.doGet(req, resp);

		echo("Processing GET");
		String service = req.getParameter("service");
		echo("Req: " + service);
		String response = null;
		if(service == null)
			response = "Server running!";
		else if (service.equals(GET_USERDATA)) // Userdata
			response = getUserdata(req.getParameter("userId"),
					req.getParameter("userName")).toString();
		else if (service.equals(GET_HIGHSCORE)) // Highscore
			response = getHighscore(req.getParameter("game")).toString();
		else if (service.equals(GET_LINK)) // Link
			response = getLink(req.getParameter("userId"),
					req.getParameter("userName"), req.getParameter("linkset"),
					req.getParameter("nextLink"),
					req.getParameter("verifiedLinks"),
					req.getParameter("curLink"),
					req.getParameter("verification")).toString();
		else if (service.equals(GET_LINKSETS)) // Linkset
			response = getLinkset().toString();
		else if (service.equals(GET_TEMPLATE))
			response = getTemplate();
		else if (service.equals(GET_TASKS))
			response = getTasks().toString();
		else if (service.equals(PERFORM_TASKS))
			response = taskPerform();
		else if (service.equals(DISCONNECT_USER))
			response = disconnectUser(req.getParameter("userName"),req.getParameter("userId"),req.getParameter("time"));
		else if (service.equals(CHECK_STATUS))
			response = "Server running!";

		// CORS
		// resp.addHeader("Access-Control-Allow-Origin",
		// "http://localhost:8080");
		resp.addHeader("Access-Control-Allow-Origin", "*");

		resp.setContentType("application/json");

		if (isJSONPRequest(req)) {
			response = getCallback(req) + "(" + response + ");";
			resp.setContentType("text/javascript");
		}
		echo(response);
		resp.getWriter().write(response);
//		resp.getWriter().write("bla");
	}

	private String postScore(String id, String userName, String score,
			String game) {
		Connection con = null;
		try {
			DBTool db = new DBTool(resourcePath + dbIniFile);
			con = db.getConnection();
			String query = "INSERT IGNORE INTO highscore VAUES ('" + id
					+ "' , '" + userName + "' ," + score + ")";
			Statement stmt = con.createStatement();
			stmt.executeUpdate(query);
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			echo("SQL ERROR: " + e.getMessage());
			return e.getMessage();
		}
		return "Post score successfully!";
	}

	private String postLevelStats(HttpServletRequest req) {
		// Parse stats

		Connection con = null;
		try {
			DBTool db = new DBTool(resourcePath + dbIniFile);
			con = db.getConnection();
			String query = "INSERT IGNORE INTO highscore VAUES ()";
			Statement stmt = con.createStatement();
			stmt.executeUpdate(query);
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			echo("SQL ERROR: " + e.getMessage());
			return e.getMessage();
		}
		return "Post level stats successfully!";
	}

	private String commitVerification(HttpServletRequest req) {
		echo("CommitVerification: " + req);
		if (req.getParameterMap().size() != 2) {
			echo("param: " + req.getParameterMap().size());
			return "Error: Invalid request parameters!";
		}
		
		// ParseJson
		ArrayList<Verification> verifications =null;
		User user = new User();
		List<String> requestParameterNames = Collections
				.list((Enumeration<String>) req.getParameterNames());
		for (String parameterName : requestParameterNames) {
			echo("param Name: " + parameterName);
			if (!parameterName.equals("service")){
				// veri
				verifications = parseJson(parameterName);
				// user
				JsonObject j = JSON.parse(parameterName);
				JsonObject jUser = j.get("user").getAsObject();
				
				String userId = jUser.get("id").toString();
				String userName = jUser.get("name").toString();
				user.setId(userId);
				user.setName(userName);
				user.setCredible(true);
			}
		}
		
	
		
		// Connect to db
		DBTool db = new DBTool(resourcePath + dbIniFile);
		Connection con = db.getConnection();

		// add into db
		updateDB(con, verifications, 0, 0, 0, 0, user, user.isCredible());

//		// Get user strength
//		String userStrength = getUserStrength(con,user);

		// Close db connection
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		echo("Commitverification done: ");
		return "done";
	}

	private ArrayList<Verification> parseJson(String data) {
		echo("Parse Json: " + data);
		ArrayList<Verification> arr = new ArrayList<Verification>();
		
		try {
			JsonObject j = JSON.parse(data);
//			String userId = j.get("userId").toString();
//			String userName = j.get("userName").toString();
			JsonValue veriValue = j.get("verification");
			echo("verivalue: " + veriValue);

			JsonArray veriArray = veriValue.getAsArray();
			echo("veriArray size: " + veriArray.size());
			for (int i = 0; i < veriArray.size(); i++) {
				JsonObject c = JSON.parse(veriArray.get(i).toString());
				String verification = c.get("veri").toString();
				String id = c.get("id").toString();

				echo("i: " + id);
				echo("v: " + verification);
				Verification v = new Verification();
				v.setId(Integer.parseInt(id));
				v.setSelection(Integer.parseInt(verification));
				arr.add(v);
			}
		} catch (Exception e) {
			echo("error:" + e.getMessage());
		}
		return arr;
	}

	

	/**
	 * Update db
	 * @param con
	 * @param verifications currentList
	 * @param agree
	 * @param disagree
	 * @param unsure
	 * @param penalty
	 * @param user
	 * @return userStrength
	 */
	private void updateDB(Connection con,
			ArrayList<Verification> verifications, int agree, int disagree, int unsure, int penalty, User user, boolean credible) {

		echo("##Server: Commit Verifications of User " + user.getName() + "####");
		

		// Links statistics
		updateLinksStatistics(con, verifications, user, credible);
		
		// User statistics
//		updateUserStatistics(con, verifications, agree, disagree, unsure, penalty, user);
		
		
//		return msg;
	}

	private void updateUserStatistics(Connection con, ArrayList<Verification> verifications, int agree,  int disagree, int unsure, int penalty, User user) {
		String sqlQuery=null;
		Statement dbStmt=null;
		
		// Gaming stats
		if(user.getCurrentLevel()<4){
			try {
				echo("\n>>>>Update GameStats for user '" + user.getName() + "'");
				// Level cleared
				sqlQuery = "UPDATE user SET `Level"
						+ user.getCurrentLevel() + "Cleared` = (`Level"+user.getCurrentLevel()+"Cleared` +1) "
						+ "WHERE `" + PropertyConstants.DB_TABLE_USER_ID +"` = '"+user.getId()+"' AND `"
						+ PropertyConstants.DB_TABLE_USER_NAME +"` = '"+user.getName()+"' ";
				dbStmt = con.createStatement();
				dbStmt.executeUpdate(sqlQuery);
				echo("Update level cleared query: "+sqlQuery);
				// Duration for level
				double time = user.getCurrentLevelTime()/1000;
				sqlQuery = "UPDATE user SET `Level"
					+ user.getCurrentLevel() + "Time` = (`Level"+user.getCurrentLevel()+"Time` +"+ time+") "
					+ "WHERE `" + PropertyConstants.DB_TABLE_USER_ID +"` = '"+user.getId()+"' AND `"
					+ PropertyConstants.DB_TABLE_USER_NAME +"` = '"+user.getName()+"' ";
				echo("Update level cleared TIME query: "+sqlQuery);
				dbStmt.executeUpdate(sqlQuery);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// Verification stats
		try {
			// Agree
			String sqlAgree = "update user set Agreement = (Agreement + "+agree+") where UserName= '"+user.getName()+"' AND UserID='"+user.getId()+"'";
			echo(sqlAgree);
			// Disagree
			String sqlDisagree = "update user set Disagreement = (Disagreement + "+disagree+") where UserName= '"+user.getName()+"' AND UserID='"+user.getId()+"'";
			echo(sqlDisagree);
			// Unsure
			String sqlUnsure = "update user set Unsure = (Unsure + "+unsure+") where UserName= '"+user.getName()+"' AND UserID='"+user.getId()+"'";
			echo(sqlUnsure);
			// Penalty
			String sqlPenalty = "update user set Penalty = (Penalty + "+penalty+") where UserName= '"+user.getName()+"' AND UserID='"+user.getId()+"'";
			echo(sqlPenalty);
			// All
			String sqlAll = "update user set Verified = (Verified + "+verifications.size()+") where UserName= '"+user.getName()+"' AND UserID='"+user.getId()+"'";
			echo(sqlAll);
			dbStmt = con.createStatement();
			if(agree!=0)
					dbStmt.executeUpdate(sqlAgree);
			if(disagree!=0)
				dbStmt.executeUpdate(sqlDisagree);
			if(unsure!=0)
				dbStmt.executeUpdate(sqlUnsure);
			if(penalty!=0)
				dbStmt.executeUpdate(sqlPenalty);
			dbStmt.executeUpdate(sqlAll);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}


	private void updateLinksStatistics(Connection con, ArrayList<Verification> verifications, User user, boolean credible) {
		if (credible== false){
			echo("User: "+user.getName()+" had too many false verifications! Don't save decisions!");
			return;
		}
		String msg = null;
		String sqlQuery, sqlQueryVerify;
		Statement dbStmt;
		Link rdfStmt;
		Verification veri;
		int id;
		int selection;

		for (int i = 0; i < verifications.size(); i++) {
			veri = verifications.get(i);
			id = veri.getId();
			selection = veri.getSelection();
			// update counter in db
			try {
				echo("\n>>>>Update statements with ID '" + id + "'");
				sqlQuery = "UPDATE links SET `"
						+ PropertyConstants.DB_TABLE_LINKS_PROPERTY_COUNTER + "`= `"
						+ PropertyConstants.DB_TABLE_LINKS_PROPERTY_COUNTER
						+ "` + 1 WHERE `" + PropertyConstants.DB_TABLE_LINKS_PROPERTY_ID
						+ "` = " + id;
				dbStmt = con.createStatement();
				dbStmt.executeUpdate(sqlQuery);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// update selection
			if (selection != UNSURE) {
				try {

					if (selection == VALID) {
						sqlQuery = "UPDATE links SET `"
								+ PropertyConstants.DB_TABLE_LINKS_PROPERTY_POSITIVE + "`= `"
								+ PropertyConstants.DB_TABLE_LINKS_PROPERTY_POSITIVE
								+ "` + 1 WHERE `"
								+ PropertyConstants.DB_TABLE_LINKS_PROPERTY_ID + "` = " + id;
						sqlQueryVerify = "INSERT INTO `"
								+ PropertyConstants.DB_TABLE_NAME_POSITIVE + "` VALUES ('" + id
								+ "' , '" + user.getId() + "', '"+user.getName()+"')";
					} else {
						sqlQuery = "UPDATE links SET `"
								+ PropertyConstants.DB_TABLE_LINKS_PROPERTY_NEGATIVE + "`= `"
								+ PropertyConstants.DB_TABLE_LINKS_PROPERTY_NEGATIVE
								+ "` + 1 WHERE `"
								+ PropertyConstants.DB_TABLE_LINKS_PROPERTY_ID + "` = " + id;
						sqlQueryVerify = "INSERT INTO `"
								+ PropertyConstants.DB_TABLE_NAME_NEGATIVE + "` VALUES ('" + id
								+ "' , '" + user.getId() + "','"+user.getName()+"')";
					}
					dbStmt = con.createStatement();
					dbStmt.executeUpdate(sqlQuery);
					dbStmt.executeUpdate(sqlQueryVerify);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else{ //if not sure do nothing in db, just inc norSureCounter of statement
				echo("Evaluation: Unsure(-1)  ---> Do Nothing in links table");
			}
			msg = "Commit verifications of user " + user.getName() + " successful";
		}
		if (msg == null){
			msg = "Commit verifications of user " + user.getName() + " failed";
		}
		echo(msg);
	}
	
	private boolean isJSONPRequest(HttpServletRequest httpRequest) {
		String callbackMethod = httpRequest.getParameter("callback");
		return (callbackMethod != null && callbackMethod.length() > 0);
	}

	private String getCallback(HttpServletRequest httpRequest) {
		return httpRequest.getParameter("callback");
	}

	/**
	 * Get link
	 * 
	 * @param rawLinks
	 *            already verified links
	 * @return link
	 */
	private JsonObject getLink(String userId, String userName, String linkset,
			String nextLink, String verifiedLinks, String curLink,
			String verification) {
		echo("rawLinks: " + verifiedLinks);

		// Connect to db
		DBTool db = new DBTool(resourcePath + dbIniFile);
		Connection con = db.getConnection();

		// Evaluate verification
		double eval = 0;
		if(verification != null)
			eval = evaluateVerification(curLink, verification, con);
		// Get difficulty
		double diff = 0;
		if(curLink!=null)
			try {
				diff = getLinkDifficulty(curLink,con);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		// Get link
		Link link = getNewLink(userName, userId, linkset, nextLink,
				verifiedLinks);

		// Create Json
		JsonObject linkJson = new JsonObject();
		try {
			JsonObject subject = new JsonObject();
			JsonObject object = new JsonObject();

			JsonObject prop = null;
			JsonArray propArray = new JsonArray();

			// Subject
			subject.put("uri", link.getSubject().getUri());
			// subject.put("ontology", link.getSubject().getOntology());
			List<Property> subjectProp = link.getSubject().getProperties();
			for (int i = 0; i < subjectProp.size(); i++) {
				// subject.put(subjectProp.get(i).getProperty(),
				// subjectProp.get(i).getValue());
				prop = new JsonObject();
				prop.put("property", subjectProp.get(i).getProperty());
				prop.put("value", subjectProp.get(i).getValue());
				echo("s: " + prop.toString());
				propArray.add(prop);
			}
			echo("subjectProp size: " + subjectProp.size());
			// Property names
			subject.put("properties", propArray);

			// Object
			propArray = new JsonArray();
			object.put("uri", link.getObject().getUri());
			List<Property> objectProp = link.getObject().getProperties();
			for (int i = 0; i < objectProp.size(); i++) {
				// object.put(objectProp.get(i).getProperty(),
				// objectProp.get(i).getValue());
				prop = new JsonObject();
				prop.put("property", objectProp.get(i).getProperty());
				prop.put("value", objectProp.get(i).getValue());
				echo("o: " + prop.toString());
				propArray.add(prop);
			}
			echo("objectProp size: " + subjectProp.size());
			// Property names
			object.put("properties", propArray);;

			// Link
			linkJson.put("id", link.getId());
			linkJson.put("subject", subject);
			linkJson.put("object", object);
			linkJson.put("predicate", link.getPredicate());
			linkJson.put("prevLinkEval", Double.toString(eval));
			linkJson.put("difficulty", Double.toString(diff));
			
		} catch (JsonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Close db connection
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return linkJson;
	}

	/**
	 * Get new link from db
	 * 
	 * @param userName
	 * @param userId
	 * @param linkset
	 * @param nextLinkType
	 *            evaluationLink or normal
	 * @param verifiedLinks
	 *            from user verified links
	 * @return
	 */
	private Link getNewLink(String userName, String userId, String linkset,
			String nextLinkType, String verifiedLinks) {
		echo("Get new statement for client '" + linkset + "'");
		// Only get statements, which were not shown yet
		echo("verifiedLinks: " + verifiedLinks);
		String notIn = "";
		if (verifiedLinks != null) {
			String[] verifications = null;
			if (verifiedLinks.contains(" ")) {
				echo("contains +");
				verifications = verifiedLinks.split(" ");
			} else {
				verifications = new String[1];
				verifications[0] = verifiedLinks;
				echo("solo");
			}
			notIn = getNotInQuery(verifications);
		}
		echo("Connect DB");
		// 1.connect to db
		DBTool db = new DBTool(resourcePath + dbIniFile);
		Connection con = db.getConnection();
		echo("nextLink: ");
		boolean nextLink = Boolean.parseBoolean(nextLinkType);
		echo("generate slq");
		int id = 0;
		Link statement = null;
		Statement stmt = null;
		ResultSet rs = null;

		// Generate SQL query
		String SQLqueryLinks = generateLinkQuery(userName, userId, linkset,
				nextLinkType, verifiedLinks, con);
		echo("SQLqueryLinks: " + SQLqueryLinks);

		// Execute Query
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(SQLqueryLinks);

			if (rs.next()) {
				echo("Got Statement " + rs.getInt("ID"));
				int notSure = rs.getInt("Counter") - rs.getInt("Positive")
						- rs.getInt("Negative");
				// TODO: change linkedOntologies into linkset
				statement = new Link(rs.getInt("ID"), rs.getString("Subject"),
						rs.getString("Predicate"), rs.getString("Object"),
						rs.getString("linkedOntologies"), rs.getInt("Counter"),
						notSure);
				echo("link: " + rs.getInt("ID") + " , "
						+ rs.getString("Subject") + " , "
						+ rs.getString("Predicate") + " , "
						+ rs.getString("Object") + " , "
						+ rs.getDouble("Confidence") + " , "
						+ rs.getInt("Counter") + " , "
						+ rs.getDouble("Difficulty"));
				statement.setDifficulty(rs.getDouble("Difficulty"));
			} else { // If no result, query again
				echo("Get new statement fail. Query again!");
				SQLqueryLinks = "select * from links, difficulty where links.ID=difficulty.ID AND links.linkedOntologies='"
						+ linkset
						+ "' and links.Confidence not in (1,-1,-2) "
						+ notIn + " order by links.Counter limit 1";
				echo("SQLqueryLinks: " + SQLqueryLinks);
				rs = stmt.executeQuery(SQLqueryLinks);
				if (rs.next()) {
					echo("Got Statement " + rs.getInt("ID"));
					int notSure = rs.getInt("Counter") - rs.getInt("Positive")
							- rs.getInt("Negative");
					statement = new Link(rs.getInt("ID"),
							rs.getString("Subject"), rs.getString("Predicate"),
							rs.getString("Object"),
							rs.getString("linkedOntologies"),
							rs.getInt("Counter"), notSure); // TODO: change
															// linkedOntologies
															// into linkset
					echo("link: " + rs.getInt("ID") + " , "
							+ rs.getString("Subject") + " , "
							+ rs.getString("Predicate") + " , "
							+ rs.getString("Object") + " , "
							+ rs.getDouble("Confidence") + " , "
							+ rs.getInt("Counter") + " , "
							+ rs.getDouble("Difficulty"));
					statement.setDifficulty(rs.getDouble("Difficulty"));
				} else {
					echo("Query after previous error... still error..");
					SQLqueryLinks = "select * from links, difficulty where links.ID = difficulty.ID and links.linkedOntologies='"
							+ linkset
							+ "' and links.Confidence not in (-1) "
							+ notIn + " order by rand() limit 1";
					echo("SQLqueryLinks: " + SQLqueryLinks);
					rs = stmt.executeQuery(SQLqueryLinks);
					if (rs.next()) {
						echo("Got Statement " + rs.getInt("ID"));
						int notSure = rs.getInt("Counter")
								- rs.getInt("Positive") - rs.getInt("Negative");
						statement = new Link(rs.getInt("ID"),
								rs.getString("Subject"),
								rs.getString("Predicate"),
								rs.getString("Object"),
								rs.getString("linkedOntologies"),
								rs.getInt("Counter"), notSure); // TODO: change
																// linkedOntologies
																// into linkset
						echo("link: " + rs.getInt("ID") + " , "
								+ rs.getString("Subject") + " , "
								+ rs.getString("Predicate") + " , "
								+ rs.getString("Object") + " , "
								+ rs.getDouble("Confidence") + " , "
								+ rs.getInt("Counter") + " , "
								+ rs.getDouble("Difficulty"));
						statement.setDifficulty(rs.getDouble("Difficulty"));
					}
				}
			}
			id = rs.getInt("ID");

			// Query Instances
			statement = getInstances(statement, stmt);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			echo("Server ERROR: Get new statement: " + e.getMessage());
		}
		echo("##Server: Get new statement with id = " + id
				+ " for client from " + linkset + " done ##");

		return highlight(statement);
	}

	private Link highlight(Link statement) {
		// TODO Auto-generated method stub
		return statement;
	}

	
	
	
	private Link getInstances(Link statement, Statement stmt)
			throws SQLException {
		String sqlInstanceQuery = null;
		String prop = null;
		String val = null;
		int i = 0;
		List<Property> subjectProp = new ArrayList<Property>();
		List<Property> objectProp = new ArrayList<Property>();

		// Subject
		echo("Query subject instance: ");
		sqlInstanceQuery = "SELECT * FROM "
				+ PropertyConstants.DB_TABLE_NAME_INSTANCES + " WHERE "
				+ PropertyConstants.DB_TABLE_INSTANCES_PROPERTY_URI + " = '"
				+ statement.getSubjectUri() + "'";
		echo("Subject instance query: " + sqlInstanceQuery);
		ResultSet rs = stmt.executeQuery(sqlInstanceQuery);
		while (rs.next()) {
			prop = rs.getString("Property");
			val = parse(rs.getString("Value"));
			subjectProp.add(new Property(prop, val));
			echo(i + ".property of subject instance: " + prop + " >> " + val);
			i++;
		}
		echo("property size: " + subjectProp.size());
		if (subjectProp.size() == 0)
			echo("Query subject instance failed!");
		// Object
		i = 0;
		echo("Query object instance: ");
		sqlInstanceQuery = "SELECT * FROM "
				+ PropertyConstants.DB_TABLE_NAME_INSTANCES + " WHERE "
				+ PropertyConstants.DB_TABLE_INSTANCES_PROPERTY_URI + " = '"
				+ statement.getObjectUri() + "'";
		echo("object instance query: " + sqlInstanceQuery);
		rs = stmt.executeQuery(sqlInstanceQuery);
		while (rs.next()) {
			prop = rs.getString("Property");
			val = parse(rs.getString("Value"));
			objectProp.add(new Property(prop, val));
			echo(i + ".property of subject instance: " + prop + " >> " + val);
			i++;
		}
		echo("property size: " + objectProp.size());
		if (objectProp.size() == 0)
			echo("Query object instance failed!");

		Instance subjectInstance = new Instance(statement.getSubjectUri(),
				subjectProp);
		Instance objectInstance = new Instance(statement.getObjectUri(),
				objectProp);
		statement.setSubject(subjectInstance);
		statement.setObject(objectInstance);

		return statement;
	}

	private String generateLinkQuery(String userName, String userId,
			String linkset, String nextLinkType, String verifiedLinks,
			Connection con) {
		String SQLqueryLinks = null;

		// Only get statements, which were not shown yet
		String notIn = "";
		String[] verifications;
		if (verifiedLinks != null) {
			verifications = verifiedLinks.split(" ");

			notIn = getNotInQuery(verifications);
		} else
			verifications = new String[1];
		boolean nextLink = Boolean.parseBoolean(nextLinkType);
		echo("generateLink nextLink");
		// normal
		if (verifications.length < 3) {
			echo("Get Easy Link! Number of verifications: "
					+ verifications.length);
			SQLqueryLinks = "SELECT * FROM easy_questions,links WHERE easy_questions.ID=links.ID and easy_questions.linkedOntologies='"
					+ linkset
					+ "' "
					+ notIn
					+ " order by easy_questions.Counter limit 1";
		} else if (nextLink == Message.NORMAL_LINK) {
			boolean isVerifiedLink = isVerifiedLink();
			if (isVerifiedLink == false) {// Minimum
				echo("Get Minimum Link!");
				SQLqueryLinks = "select * from links, difficulty where links.ID = difficulty.ID AND links.linkedOntologies='"
						+ linkset
						+ "' and links.Confidence not in (1,-1,-2) "
						+ notIn + " order by links.Counter limit 1";
			} else { // Already validated
				echo("Get Verified Link!");
				// take: link Difficulty, user strength, not already verified
				// links
				try {
					// Get user strength
					double userStrength = getUserStrength(userName, userId, con);

					// Create Link query
					SQLqueryLinks = "SELECT * " + "FROM difficulty, links "
							+ "where linkedOntologies='" + linkset + "' "
							+ "AND links.ID=difficulty.ID " + notIn
							+ "AND Difficulty is not null "
							+ "ORDER BY ABS(difficulty.Difficulty - "
							+ Balancing.getUserStrength(userStrength) + " )";

				} catch (SQLException e) {
					e.printStackTrace();
					echo("Server ERROR: Get already verified link: "
							+ e.getMessage());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}// eval
		else if (nextLink == Message.EVAL_LINK) {
			echo("Get Evaluation link!");
			SQLqueryLinks = "select * from links, difficulty where links.ID = difficulty.ID AND links.linkedOntologies='"
					+ linkset
					+ "' and links.Confidence in (1,-2) "
					+ notIn
					+ " order by links.Counter limit 1";
		}
		return SQLqueryLinks;
	}

	private double getLinkDifficulty(String id, Connection con)
			throws SQLException {
		// Get LinkDifficulty
		double difficulty = 0;
		String sqlQuery = "Select * from difficulty where ID = " + id;
		echo("Difficulty query: " + sqlQuery);
		Statement dbStmt = con.createStatement();
		ResultSet rs = dbStmt.executeQuery(sqlQuery);
		if (rs.next()) {
			difficulty = rs.getDouble("Difficulty");
			echo("Query Difficulty success: id = " + rs.getInt("ID")
					+ " Difficulty: " + difficulty);
		} else
			echo("Server MYSQL Error: Couldn't retrieve difficulty for id = "
					+ id);
		return difficulty;
	}

	/**
	 * Get all the ids which were already verified
	 * 
	 * @param completeList
	 **/
	private String getNotInQuery(String[] verifications) {
		if (verifications.length < 1)
			return "";
		String notIn = "and links.ID not in ( ";

		for (int i = 0; i < verifications.length; i++) {
			notIn += verifications[i];
			if (i != verifications.length - 1)
				notIn += " , ";
		}
		notIn += ") ";
		return notIn;
	}

	private double getUserStrength(String userName, String userId,
			Connection con) throws SQLException {
		double userStrength = 0;
		String sqlUserStrength = "(Select strength " + "from user_strength "
				+ "where UserName='" + userName + "' " + "And UserID= '"
				+ userId + "' )";
		echo("sqlUserStrength: " + sqlUserStrength);
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(sqlUserStrength);
		if (rs.next()) {
			userStrength = rs.getDouble("strength");
			echo("Got userStrength: " + userStrength);
		}
		return userStrength;
	}

	private double evaluateVerification(String curLink, String verify,
			Connection con) {
		echo("##Evaluate player's verification##");
		double eval = 0;
		// Query verificationInformation

		Statement dbStmt = null;
		String sqlQuery = null;
		ResultSet rs = null;

		int verification = Integer.parseInt(verify);
		if (verification == VALID)
			sqlQuery = "SELECT * from evaluate_positive WHERE ID=" + curLink;
		else if (verification == NOT_VALID)
			sqlQuery = "SELECT * from evaluate_negative WHERE ID=" + curLink;
		else if (verification == UNSURE)
			return EVAL_NONE;
		echo("Eval query: " + sqlQuery);
		try {
			dbStmt = con.createStatement();
			rs = dbStmt.executeQuery(sqlQuery);
			if (rs.next()) {
				double threshold = rs.getDouble("Threshold");
				double confidence = rs.getDouble("Confidence");
				// Calculate bonus
				echo("Query Threshold success: id = " + rs.getInt("ID")
						+ " Threshold : " + threshold + " Confidence: "
						+ confidence);
				echo("What kind of link should be queried?");
				if (confidence == 1 || confidence == -2) { // manually verified
															// links
					echo("-> manual verified links with Confidence = {1,-2}");
					if ((confidence == 1 && verification == VALID)
							|| (confidence == -2 && verification == NOT_VALID))
						eval = EVAL_POSITIVE;
					else
						eval = EVAL_NEGATIVE; // Penalty
				} else { // normal links
					echo("-> normal links with Confidence != {1,-2}");
					// if(0.3<=threshold && threshold<0.7) // medium reward
					// bonus = GameConstants.BONUS_MEDIUM;
					// else if(0.7<=threshold) // huge reward
					// bonus = GameConstants.BONUS_HUGE;
					if (threshold == 0) // db value = null means stmt not
										// verified yet
										// (counter = 0)
						eval = EVAL_FIRST;
					else
						eval = threshold;
				}
				echo("Eval: " + eval);
			} else {
				echo("Server MYSQL Error: Couldn't retrieve threshold for ID = "
						+ curLink);
				return -1;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			echo("Server SQL Error: " + e.getMessage());
		}
		return eval;
	}

//	private double getLinkDifficulty(String id){
//		// Get LinkDifficulty
//		double difficulty=0;
//		String sqlQuery = "Select * from difficulty where ID = "+id;
//		echo("Difficulty query: "+sqlQuery);
//		Statement dbStmt=null;
//		ResultSet rs = null; 
//		try {
//			rs = dbStmt.executeQuery(sqlQuery);
//		if(rs.next()){
//			difficulty = rs.getDouble("Difficulty");
//			echo("Query Difficulty success: id = " + rs.getInt("ID") + " Difficulty: "+difficulty);
//		}else
//			echo("Server MYSQL Error: Couldn't retrieve difficulty for id = "+id);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return difficulty;
//	}
	
	
	/**
	 * Connect user and get userdata
	 * 
	 * @param id
	 * @param userName
	 * @param linkset
	 * @return user
	 */
	private JsonObject getUserdata(String id, String userName) {
		echo("getUserdata ==> " + userName);
		JsonObject userJson = new JsonObject();
		try {
			DBTool db = new DBTool(resourcePath + dbIniFile);
			Connection con = db.getConnection();
			String query = "SELECT * FROM `user` WHERE `UserID` = '" + id
					+ "' and `UserName` = '" + userName + "'";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if (rs.next()) {
				Userdata u = new Userdata(rs.getString("UserID"),
						rs.getString("UserName"), 0, // hScore
						null, // user strength
						rs.getInt("Verified"), // numVeri
						rs.getInt("Agreement"), rs.getInt("Disagreement"),
						rs.getInt("Unsure"), rs.getInt("Penalty"));
				// Highscore
				// String hScoreQuery =
				// "SELECT * FROM `highscores` WHERE `UserID` = '"+id+"' and `UserName` = '"+userName+"'";
				String hScoreQuery = "SELECT * FROM `highscores` WHERE `Player` = '"
						+ userName + "' order by Score desc limit 0,1";
				rs = stmt.executeQuery(hScoreQuery);
				if (rs.next())
					u.setHighscore(rs.getInt("Score"));
				// User strength
				String uStrengthQuery = "SELECT * FROM `user_strength` WHERE `UserID` = '"
						+ id + "' and `UserName` = '" + userName + "'";
				rs = stmt.executeQuery(uStrengthQuery);
				if (rs.next())
					u.setStrength(rs.getString("Strength"));
				// Json
				userJson = new JsonObject();
				userJson.put("userId", u.getId());
				userJson.put("userName", u.getName());
				userJson.put("highscore", u.getHighscore());
				userJson.put("strength", u.getStrength());
				userJson.put("#veri", u.getNumVeri());
				userJson.put("#agree", u.getNumAgree());
				userJson.put("#disagree", u.getNumDisagree());
				userJson.put("#unsure", u.getNumUnsure());
				userJson.put("#penalty", u.getNumPenalty());

			} else
				// Create new db entry
				insertNewPlayer(id, userName);
			con.close();
		} catch (JsonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			echo("SQL ERROR: " + e.getMessage());
		}
		echo("getUserdata ==> " + userName + " done.");
		return userJson;
	}

	private void insertNewPlayer(String id, String userName) {
		echo("##Add User##");
		User user = new User();
		user.setId(id);
		user.setName(userName);
		userList.put(id, user);
		String strength = null;
		try {
			DBTool db = new DBTool(resourcePath + dbIniFile);
			Connection con = db.getConnection();
			String sqlQuery = "INSERT IGNORE INTO `"
					+ PropertyConstants.DB_TABLE_NAME_USER + "` VALUES ('" + id
					+ "' , '" + userName + "', 15,5,0,0,0," + // agree,
																// disagree,
																// unsure,
																// penalty,
																// allVerif
																// (init
																// a:15,d:5 =>
																// 15/20
																// richtigen)
					"0,0," + // gamesplayed, gametime
					"0,0,0,0,0,0)"; // level
			echo("Query: " + sqlQuery);
			Statement stmt = con.createStatement();
			stmt.executeUpdate(sqlQuery);
			con.close();
		} catch (Exception e) {
			echo("Server Error: Couldn't insert User into db.");
		}
	}

	private JsonObject test(String game) {
		echo("####Server: Highscore Request####");
		echo("game = " + game);
		JsonObject hScoreJson = new JsonObject();

		JsonObject sBuff = null;
		int i = 0;
		try {
			JsonArray hScoreArray = new JsonArray();
			Score s = null;
			s = new Score("test", "id", 2);
			sBuff = new JsonObject();
			sBuff.put("name", s.getName());
			sBuff.put("id", "blaa");
			sBuff.put("score", s.getScore());

			echo("####Server: Highscore Request Done####\n");

		} catch (JsonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			echo("Json error####\n");
		}
		return sBuff;
	}

	private JsonObject getHighscore(String game) {
		echo("####Server: Highscore Request####");
		echo("game = " + game);
		JsonObject hScoreJson = new JsonObject();
		int i = 0;
		try {
			JsonArray hScoreArray = new JsonArray();
			Score s = null;
			JsonObject sBuff = null;
			DBTool db = new DBTool(resourcePath + dbIniFile);
			Connection con = db.getConnection();
			String query = "SELECT * FROM `highscores` ORDER BY `Score` DESC LIMIT 0,10";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				s = new Score(rs.getString(1), "id", rs.getInt(2));
				sBuff = new JsonObject();
				sBuff.put("name", s.getName());
				if (s.getId() == null)
					sBuff.put("id", "null");
				else
					sBuff.put("id", s.getId());
				sBuff.put("score", s.getScore());
				hScoreArray.add(sBuff);
				i++;
				echo("i " + i);
			}
			echo("####Server: Highscore Request Done####\n");
			con.close();
			// Out
			hScoreJson.put("highscore", hScoreArray);
			hScoreJson.put("bla", "test");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			echo("####Server: Highscore Request failed####\n");

		} catch (JsonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			echo("Json error####\n");
		}
		return hScoreJson;
	}

	private JsonObject getLinkset() {
		echo("Get Linkset");

		ArrayList<Linkset> tmp = new ArrayList<Linkset>();

		String query = "SELECT * FROM "
				+ PropertyConstants.DB_TABLE_NAME_LINKEDONTOLOGIES + " WHERE "
				+ PropertyConstants.DB_TABLE_LINKEDONTOLOGIES_READY + " = 1";

		// db connect
		DBTool db = new DBTool(resourcePath + dbIniFile);
		Connection con = db.getConnection(); // establish connection
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			Linkset linkset;
			int i = 0;
			while (rs.next()) {
				echo(rs.getString(1) + " : " + rs.getString(3) + " : " + i);
				linkset = new Linkset();
				linkset.setSubject(rs
						.getString(PropertyConstants.DB_TABLE_LINKEDONTOLOGIES_SUBJECT));
				linkset.setObject(rs
						.getString(PropertyConstants.DB_TABLE_LINKEDONTOLOGIES_OBJECT));
				linkset.setPredicate(rs
						.getString(PropertyConstants.DB_TABLE_LINKEDONTOLOGIES_PREDICATE));
				linkset.setDescription(rs
						.getString(PropertyConstants.DB_TABLE_LINKEDONTOLOGIES_DESCRIPTION));
				linkset.setDifficulty(rs
						.getString(PropertyConstants.DB_TABLE_LINKEDONTOLOGIES_DIFFICULTY));
				linkset.setId(rs
						.getString(PropertyConstants.DB_TABLE_LINKEDONTOLOGIES_ID));
				tmp.add(linkset);
				i++;
				// echo(tmp.get(i)+" i "+i);
			}
			echo("Numbers of Linked Ontologies: " + tmp.size());
			echo("####Server: Get Linkset Done####\n");
		} catch (Exception e) {
			echo("Server: ERROR getting linked Ontologies: " + e.getMessage());
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		echo("Parse Linkset to JSON");
		JsonObject linksetJson = new JsonObject();
		JsonArray lSetArray = new JsonArray();
		JsonObject lSet = null;
		Linkset lBuff = null;
		try {
			for (int i = 0; i < tmp.size(); i++) {
				lBuff = tmp.get(i);
				lSet = new JsonObject();
				lSet.put("subject", lBuff.getSubject());
				lSet.put("object", lBuff.getObject());
				lSet.put("predicate", lBuff.getPredicate());
				lSet.put("description", lBuff.getDescription());
				lSet.put("difficulty", lBuff.getDifficulty());
				lSetArray.add(lSet);
			}
			// Out
			linksetJson.put("linksets", lSetArray);
		} catch (JsonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return linksetJson;
	}

	private boolean isVerifiedLink() {
		Random gen = new Random();
		double r = gen.nextDouble();
		echo("isVerifiedLink? r= " + r + " eFactor="
				+ Balancing.EXPLORATION_FACTOR);
		if (r < Balancing.EXPLORATION_FACTOR)
			return false;
		else
			return true;
	}

	private String getTemplate() {
		String file = "";
		try {
			String templateFile = "template.json";
			FileInputStream fstream = new FileInputStream(this.resourcePath
					+ templateFile);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				// Print the content on the console
				file = file + strLine;
			}
			// Close the input stream
			in.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		return file;
	}
	

	private String disconnectUser(String id, String name,
			String playTime) {
		echo("##Disconect User##");
		String msg="User "+name +"Disconnected!";
		if(userList.remove("id") != null){
			echo(msg);

			// Inc times played counter
			String sqlQuery = "UPDATE `"+ PropertyConstants.DB_TABLE_NAME_USER + 
					"` SET  `" +PropertyConstants.DB_TABLE_USER_GAMESPLAYED+"` = (`"+PropertyConstants.DB_TABLE_USER_GAMESPLAYED+"` + 1) "+  
					"WHERE `"+PropertyConstants.DB_TABLE_USER_ID+"` = '"+id+"' AND "+
					"`"+PropertyConstants.DB_TABLE_USER_NAME+"` = '"+name+"'";
			echo("Times played Query: " + sqlQuery);
			
			// Set duration of game
			double time =  Integer.valueOf(playTime)/1000;
			echo("User "+name+ " played "+time+" seconds!");
			String sqlQuery2 = "UPDATE `"+ PropertyConstants.DB_TABLE_NAME_USER + 
					"` SET  `" +PropertyConstants.DB_TABLE_USER_PLAYTIME+"` = (`"+PropertyConstants.DB_TABLE_USER_PLAYTIME+"` + " + time+") "+  
					"WHERE `"+PropertyConstants.DB_TABLE_USER_ID+"` = '"+id+"' AND "+
					"`"+PropertyConstants.DB_TABLE_USER_NAME+"` = '"+name+"'";
			echo("PlayTime Query: " + sqlQuery2);
			
			DBTool db = new DBTool(resourcePath + dbIniFile);
			Connection con = db.getConnection();
			try {
				Statement stmt = con.createStatement();
				stmt.executeUpdate(sqlQuery);
				stmt.executeUpdate(sqlQuery2);
			} catch (Exception e) {
				echo("SQL Server Error: Couldn't disconnect user!.");
			} finally {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else{
			msg = "Server Error: User = "+name+" wasn't found! Couldn't calculate play time!";
			echo(msg);
		}
		
		return msg;
	}

	private JsonObject getTasks() {
		echo("getTasks ==> ");
		JsonObject tJson = null;
		JsonArray tasks = new JsonArray();
		JsonObject tasksJson = new JsonObject();

		BufferedReader br = null;
		String buffer;
		String[] split = new String[Task.SIZE + 1];

		try {
			br = new BufferedReader(new FileReader(resourcePath + "linkFiles/"
					+ "task.tk"));
			while ((buffer = br.readLine()) != null) {
				split = buffer.split(Task.SEPERATOR);
				// For debugging
				for (int i = 0; i < split.length; i++)
					echo("Split: " + split[i]);

				if (split[Task.SIZE].equals("1")
						|| split[0].equals("[subject]"))
					// Only Send tasks with done-value=0
					continue;

				Task t = new Task();
				t.setTask(split);

				tJson = new JsonObject();
				tJson.put("subject", t.getSubject());
				tJson.put("object", t.getObject());
				tJson.put("predicate", t.getPredicate());
				tJson.put("description", t.getDescription());
				tJson.put("difficulty", t.getDifficulty());
				tJson.put("date", t.getDate());
				tJson.put("file", t.getFile());
				tJson.put("linkset", t.getLinkset());

				tasks.add(tJson);
			}
			// all tasks
			tasksJson.put("tasks", tasks);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		echo("getTasks => Done\n");
		return tasksJson;
	}

	/**
	 * Communication method. Read task-file "task.tk" and search for
	 * "to-do tasks" (done-value = 0). Create temporary task-file "task.tmp" to
	 * write modifications. At the end delete original task-file and rename
	 * temporary task-file to "task.tk".
	 */
	public String taskPerform() {
		echo(" Perform Task");
		String msg = "Error Performing Task";
		taskSuccess = false;
		String originalTaskFilePath = resourcePath + "linkFiles/" + "task.tk";
		String tempTaskFilePath = resourcePath + "linkFiles/" + "task.tmp";
		BufferedReader br = null;
		BufferedWriter brWrite = null;
		String buffer;
		String[] split = new String[Task.SIZE + 1];
		int i = 0;
		try {
			// Open file to read
			br = new BufferedReader(new FileReader(originalTaskFilePath));
			// Create tempFile for writing
			brWrite = new BufferedWriter(new FileWriter(tempTaskFilePath));

			// Read lines
			while ((buffer = br.readLine()) != null) {
				split = buffer.split(Task.SEPERATOR);
				// /*Test method
				echo("{Test} Show split:");
				for (int j = 0; j < split.length; j++)
					echo("split[" + j + "]: " + split[j]);
				echo("buffer: " + buffer + "\n");
				// */
				if ((split[Task.SIZE].equals("1"))
						|| (split[0].equals("[subject]"))) {
					// If task-file has to-do task
					brWrite.write(buffer);
					brWrite.newLine();
					continue;
				}
				Task t = new Task();
				t.setTask(split);
				echo("\n<<<<<< " + i + ".Task, Subject: " + t.getSubject()
						+ ", Object: " + t.getObject() + ", File: "
						+ t.getFile() + " >>>>>>\n");
				if (insertLinksIntoDB(t)) {
					// Write to tempFile
					brWrite.write(split[0] + Task.SEPERATOR + split[1]
							+ Task.SEPERATOR + split[2] + Task.SEPERATOR
							+ split[3] + Task.SEPERATOR + split[4]
							+ Task.SEPERATOR + split[5] + Task.SEPERATOR
							+ split[6] + Task.SEPERATOR + split[7]
							+ Task.SEPERATOR + "1");
					taskSuccess = true;
				} else
					brWrite.write(split[0] + Task.SEPERATOR + split[1]
							+ Task.SEPERATOR + split[2] + Task.SEPERATOR
							+ split[3] + Task.SEPERATOR + split[4]
							+ Task.SEPERATOR + split[5] + Task.SEPERATOR
							+ split[6] + Task.SEPERATOR + split[7]
							+ Task.SEPERATOR + "0");

				brWrite.newLine();
			}

			msg = "Server: Performing Tasks successfully!";
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				// Close Reader and Writer
				if (br != null)
					br.close();
				if (brWrite != null)
					brWrite.close();

				// Delete original task-file
				File fDelete = new File(originalTaskFilePath);
				boolean delete = fDelete.delete();
				if (delete)
					echo("Delete template file success");
				else
					echo("Delete template file failed");
				// Rename tempTaskFile
				File fRename = new File(tempTaskFilePath);
				boolean rename = fRename
						.renameTo(new File(originalTaskFilePath));
				if (rename)
					echo("Rename template file success");
				else
					echo("Rename template file failed");
				/*
				 * if (taskSuccess) restart();
				 */
				restart();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		echo("####Server: Performing Tasks successfully!####\n");
		return msg;
	}

	// TODO insert another table-column for deleting/logging links
	/**
	 * Returns true if Querying rdf-statements successful
	 */
	private boolean insertLinksIntoDB(Task t) {
		echo("##Server: Insert links into database##");
		String msg = null;
		boolean status = false;
		RDFHandler rdf = new RDFHandler(resourcePath + "linkFiles/"
				+ t.getFile(), resourcePath + "db_settings.ini", resourcePath
				+ templateFile, t);
		echo("Path: " + t.getFile() + "\nxmlPath: " + resourcePath
				+ templateFile);
		XMLTool xml = new XMLTool(resourcePath + templateFile);
		try {
			xml.readTemplateFile();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TemplateLinkset template = xml.getLinkset(t.getLinkset());

		// echo("Names: "+name+" , "+name_2);
		if (t.getFile().endsWith(".xml"))
			msg = rdf.start(template, RDFHandler.formatXML);
		else
			msg = rdf.start(template, RDFHandler.formatNT);
		echo("Status of querying  and adding links: " + msg + " ");
		echo("##Server: Insert links into database done##\n");
		status = true;
		return status;
	}

	/**
	 * Parse method. Remove brackets and replace prefix with abbreviation
	 */
	private String parse(String s) {
		// Remove '<' and '>'
		// echo("##Server: Parse "+s+"##");
		String removedBrackets = s.replaceAll("<", "").replaceAll(">", "");
		// Set Prefix
		String buffer;
		String parsed = "";

		if (removedBrackets
				.contains(PropertyConstants.SEPERATOR_PROPERTY_VALUE)) {
			int numberOfTypes = s.split(" ; ").length;
			String split[] = new String[numberOfTypes];
			// split
			split = removedBrackets
					.split(PropertyConstants.SEPERATOR_PROPERTY_VALUE);
			for (int i = 0; i < numberOfTypes; i++) { // For every type set
														// prefix
				// echo("Split :"+i+" "+split[i]);
				split[i] = setPrefix(split[i]);
			}
			// Concat to 1 string
			for (int j = 0; j < split.length; j++) {
				if (j != split.length - 1)
					parsed += split[j]
							+ PropertyConstants.SEPERATOR_PROPERTY_VALUE; // add
																			// \n
																			// ??
				else
					parsed += split[j];
				// echo(j+".parsed split: "+parsed);
			}
		} else {
			parsed = setPrefix(removedBrackets);
		}
		// echo("Parsed: "+parsed);
		// echo("##Server: Parse "+s+" done##");
		return parsed;

	}

	private void initPrefix() {
		echo("##Server: Init Prefixes##");
		prefixMap = new HashMap<String, String>();
		fillPrefixMap();
		echo("##Server: Init Prefixes done##\n");
	}

	private void fillPrefixMap() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(resourcePath + prefixFile));
			String[] split = new String[2];
			String line = null;
			while ((line = br.readLine()) != null) {
				split = line.split(",");
				prefixMap.put(split[1], split[0]); // Switch key and value
													// (key=url,
													// val=abbr.)

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

	}

	private String setPrefix(String s) {
		int limit = ("http://").length();
		int position = s.length();
		String shorten = "";
		String prefix = "";
		String ending = "";
		int endingPosition = 0;
		// string contains prefix?
		if (s.contains("http://")) {
			if (s.contains("#")) { // separator = '#' like
									// 'http://purl.org/NET/dady#'
				position = s.lastIndexOf('#') + 1;
				shorten = s.substring(0, position);
				prefix = replaceWithPrefix(shorten);
				if (prefix.isEmpty()) {
					endingPosition = s.lastIndexOf("/") + 1;
					ending = s.substring(endingPosition);
					prefix = setPrefix(s.substring(0, endingPosition));
					position = endingPosition;
				}
			} else { // separator = '/' like 'http://data.totl.net/tarot/card/'
				shorten = s;
				String buffer;
				while (position > limit) {
					position = shorten.lastIndexOf('/') + 1;
					shorten = shorten.substring(0, position);
					buffer = shorten.substring(0, position - 1);
					shorten = replaceWithPrefix(shorten);
					if (shorten.isEmpty()) {
						shorten = buffer;
						continue;
					} else {
						break;
					}
				}
				prefix = shorten;
			}
			String parsed = prefix + s.substring(position);
			return parsed;
		} else
			return s;
	}

	private String replaceWithPrefix(String s) {
		if (prefixMap.containsKey(s)) {
			return prefixMap.get(s) + ":";
		} else
			return "";
	}

	private long getCurTime() {
		return System.currentTimeMillis();
	}

	private void echo(String s) {
		System.out.println("[Server]: " + s);
	}


}
