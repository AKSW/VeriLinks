package org.aksw.verilinks.core.server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aksw.verilinks.core.shared.Balancing;
import org.aksw.verilinks.core.shared.Message;
import org.aksw.verilinks.core.shared.PropertyConstants;
import org.aksw.verilinks.core.shared.msg.Instance;
import org.aksw.verilinks.core.shared.msg.Link;
import org.aksw.verilinks.core.shared.msg.Linkset;
import org.aksw.verilinks.core.shared.msg.Property;
import org.aksw.verilinks.core.shared.msg.Score;
import org.aksw.verilinks.core.shared.msg.User;
import org.aksw.verilinks.core.shared.msg.Userdata;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openjena.atlas.json.JSON;
import org.openjena.atlas.json.JsonObject;
import org.openjena.atlas.json.JsonValue;

public class RestServlet extends HttpServlet {

	/** User List */
	private HashMap<String, User> userList;
	
	private String GET_USER_SCORE = "getUserScore";
	private String GET_HIGHSCORE = "getHighscore";
	private String GET_USERDATA = "getUserdata"; // connect method
	private String GET_LINK = "getLink";
	private String GET_LINKSETS = "getLinksets";
	private String GET_TEMPLATE = "getTemplate";
	
	private String POST_COMMIT_VERIFICATIONS = "commitVerifications";
	private String POST_SCORE = "postScore";
	private String POST_LEVEL_STATS = "postLevelStats";
	
	
	/** Resource path */
	private String resourcePath = null;

	private final String dbIniFile = "db_settings.ini";
	private final String templateFile = "LinkTemplates.xml";
	private final String prefixFile = "prefix.csv";

	private static final double EVAL_POSITIVE = 1;
	private static final double EVAL_NONE = 0;
	private static final double EVAL_NEGATIVE = -1;
	private static final double EVAL_FIRST = -2;

	private static final double VALID = 1;
	private static final double NOT_VALID = 0;
	private static final double UNSURE = -1;

	/** Prefix Map **/
	private HashMap<String, String> prefixMap;

	@Override
	public void init() throws ServletException {
		super.init();
		
		//users
		userList = new HashMap<String,User>();
		
		initPath();
		initPrefix();
	}

	public void initPath() {
		echo("####Server: Init Path ####");
		String prefix = getServletContext().getRealPath("");
		if (!prefix.endsWith("/")) {
			prefix += '/';
		}
		resourcePath = prefix + "Application/";
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
			response = postScore(req.getParameter("userId"),req.getParameter("userName"),req.getParameter("score"),req.getParameter("game"));
		if (service.equals(POST_LEVEL_STATS)) // Post score
			response = postLevelStats(req);
		
		
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
		if (service.equals(GET_USERDATA)) // Userdata
			response = getUserdata(req.getParameter("userId"), req.getParameter("userName")).toString();
		else if (service.equals(GET_HIGHSCORE)) // Highscore
			response = getHighscore(req.getParameter("game")).toString();
		else if (service.equals(GET_LINK)) // Link
			response = getLink(req.getParameter("userId"), req.getParameter("userName"),  
					req.getParameter("linkset"), req.getParameter("nextLink"), 
					req.getParameter("verifiedLinks"),
					req.getParameter("curLink"), req.getParameter("verification"))
					.toString();
		else if (service.equals(GET_LINKSETS)) // Linkset
			response = getLinkset().toString();
		else if (service.equals(GET_TEMPLATE))
			response = getTemplate();

		// CORS
//		resp.addHeader("Access-Control-Allow-Origin", "http://localhost:8080");
		resp.addHeader("Access-Control-Allow-Origin", "*");

		resp.setContentType("application/json");

		if (isJSONPRequest(req)) {
			response = getCallback(req) + "(" + response + ");";
			resp.setContentType("text/javascript");
		}

		resp.getWriter().write(response);
	}

	


	private String postScore(String id,
			String userName, String score, String game) {	
		Connection con = null;
		try {
			DBTool db = new DBTool(resourcePath + dbIniFile);
			con = db.getConnection();
			String query = "INSERT IGNORE INTO highscore VAUES ('"+id+"' , '"+userName+"' ,"+score+")";
			Statement stmt = con.createStatement();
			stmt.executeUpdate(query);
			con.close();
		}catch (SQLException e) {
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
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			echo("SQL ERROR: " + e.getMessage());
			return e.getMessage();
		} 
		return "Post level stats successfully!";
	}

	
	private String commitVerification(HttpServletRequest req) {
		StringBuffer jb = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = req.getReader();
			while ((line = reader.readLine()) != null){
				jb.append(line);
				JsonObject j = JSON.parse(line);
				JsonValue eval = j.get("verifiTest");
				System.out.println("EVAL: "+eval.toString());
			}
		} catch (Exception e) { /* report an error */
		}

		echo("POST data: " + jb.toString());
		return jb.toString();
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
	 *          already verified links
	 * @return link
	 */
	private JSONObject getLink(String userId, String userName, String linkset,
			String nextLink, String verifiedLinks, String curLink, String verification) {
		echo("rawLinks: " + verifiedLinks);

		// Connect to db
		DBTool db = new DBTool(resourcePath + dbIniFile);
		Connection con = db.getConnection();

		// Evaluate verification
		double eval = evaluateVerification(curLink, verification, con);

		// Get link
		Link link = getNewLink(userName, userId, linkset, nextLink,
				verifiedLinks);

		// Create JSON
		JSONObject linkJson = new JSONObject();
		try {
			JSONObject subject = new JSONObject();
			JSONObject object = new JSONObject();
			
			JSONObject prop = null;
			JSONArray propArray = new JSONArray();
			
			// Subject
			subject.put("uri", link.getSubject().getUri());
//			subject.put("ontology", link.getSubject().getOntology());
			List<Property> subjectProp = link.getSubject().getProperties();
			for(int i=0;i<subjectProp.size();i++){
//				subject.put(subjectProp.get(i).getProperty(), subjectProp.get(i).getValue());
				prop = new JSONObject();
				prop.put("property", subjectProp.get(i).getProperty());
				prop.put("value", subjectProp.get(i).getValue());
				propArray.put(prop);
			}
			// Property names
			subject.put("properties", propArray);
	
			// Object
			object.put("uri", link.getObject().getUri());
			List<Property> objectProp = link.getObject().getProperties();
			for(int i=0;i<objectProp.size();i++){
//				object.put(objectProp.get(i).getProperty(), objectProp.get(i).getValue());
				prop=new JSONObject();
				prop.put("property", objectProp.get(i).getProperty());
				prop.put("value", objectProp.get(i).getValue());
				propArray.put(prop);
			}
			// Property names
			object.put("properties", propArray);
			
//			// Properties
//			JSONObject obProp = new JSONObject();
//			// for(){
//			// properties.put(arg0, arg1);
//			// }
//			JSONArray obPropNames = new JSONArray();
//			obPropNames.put("http://www.w3.org/2000/01/rdf-schema#label");
//			obPropNames.put("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
//			obPropNames.put("http://dbpedia.org/ontology/abstract");
//			
//			obProp.put("label", link.getObject().getLabel());
//			obProp.put("type", link.getObject().getType());
//			object.put("properties", obProp);
//			object.put("propNames", obPropNames);
			
			// Link
			linkJson.put("subject", subject);
			linkJson.put("object", object);
			linkJson.put("predicate", link.getPredicate());
			linkJson.put("prevLinkEval", eval);

		} catch (JSONException e) {
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
	 * @param userName
	 * @param userId
	 * @param linkset
	 * @param nextLinkType evaluationLink or normal
	 * @param verifiedLinks from user verified links
	 * @return
	 */
	private Link getNewLink(String userName, String userId,
			String linkset, String nextLinkType, String verifiedLinks) {
		echo("Get new statement for client '" + linkset + "'");

		// Only get statements, which were not shown yet
		String[] verifications = verifiedLinks.split(" ");
		String notIn = getNotInQuery(verifications);

		// 1.connect to db
		DBTool db = new DBTool(resourcePath + dbIniFile);
		Connection con = db.getConnection();

		boolean nextLink = Boolean.parseBoolean(nextLinkType);

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
						rs.getString("linkedOntologies"), rs.getInt("Counter"), notSure); 
				echo("link: " + rs.getInt("ID") + " , " + rs.getString("Subject")
						+ " , " + rs.getString("Predicate") + " , "
						+ rs.getString("Object") + " , " + rs.getDouble("Confidence")
						+ " , " + rs.getInt("Counter") + " , " + rs.getDouble("Difficulty"));
				statement.setDifficulty(rs.getDouble("Difficulty"));
			} else { // If no result, query again
				echo("Get new statement fail. Query again!");
				SQLqueryLinks = "select * from links, difficulty where links.ID=difficulty.ID AND links.linkedOntologies='"
						+ linkset
						+ "' and links.Confidence not in (1,-1,-2) "
						+ notIn
						+ " order by links.Counter limit 1";
				echo("SQLqueryLinks: " + SQLqueryLinks);
				rs = stmt.executeQuery(SQLqueryLinks);
				if (rs.next()) {
					echo("Got Statement " + rs.getInt("ID"));
					int notSure = rs.getInt("Counter") - rs.getInt("Positive")
							- rs.getInt("Negative");
					statement = new Link(rs.getInt("ID"),
							rs.getString("Subject"), rs.getString("Predicate"),
							rs.getString("Object"), rs.getString("linkedOntologies"),
							rs.getInt("Counter"), notSure); // TODO: change linkedOntologies
																							// into linkset
					echo("link: " + rs.getInt("ID") + " , " + rs.getString("Subject")
							+ " , " + rs.getString("Predicate") + " , "
							+ rs.getString("Object") + " , " + rs.getDouble("Confidence")
							+ " , " + rs.getInt("Counter") + " , "
							+ rs.getDouble("Difficulty"));
					statement.setDifficulty(rs.getDouble("Difficulty"));
				} else {
					echo("Query after previous error... still error..");
					SQLqueryLinks = "select * from links, difficulty where links.ID = difficulty.ID and links.linkedOntologies='"
							+ linkset
							+ "' and links.Confidence not in (-1) "
							+ notIn
							+ " order by rand() limit 1";
					echo("SQLqueryLinks: " + SQLqueryLinks);
					rs = stmt.executeQuery(SQLqueryLinks);
					if (rs.next()) {
						echo("Got Statement " + rs.getInt("ID"));
						int notSure = rs.getInt("Counter") - rs.getInt("Positive")
								- rs.getInt("Negative");
						statement = new Link(rs.getInt("ID"),
								rs.getString("Subject"), rs.getString("Predicate"),
								rs.getString("Object"), rs.getString("linkedOntologies"),
								rs.getInt("Counter"), notSure); // TODO: change linkedOntologies
																								// into linkset
						echo("link: " + rs.getInt("ID") + " , " + rs.getString("Subject")
								+ " , " + rs.getString("Predicate") + " , "
								+ rs.getString("Object") + " , " + rs.getDouble("Confidence")
								+ " , " + rs.getInt("Counter") + " , "
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
		echo("##Server: Get new statement with id = " + id + " for client from "
				+ linkset + " done ##");

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
			subjectProp.add(new Property(prop,val));
			echo(i+".property of subject instance: "+prop+" >> "+val);
			i++;
		}
		echo("property size: "+subjectProp.size());
		if(subjectProp.size()==0)
				echo("Query subject instance failed!");
		// Object
		i=0;
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
			objectProp.add(new Property(prop,val));
			echo(i+".property of subject instance: "+prop+" >> "+val);
			i++;
		} 
		echo("property size: "+objectProp.size());
		if(objectProp.size()==0)
			echo("Query object instance failed!");
		
		Instance subjectInstance = new Instance(statement.getSubjectUri(),subjectProp);
		Instance objectInstance = new Instance(statement.getObjectUri(),objectProp);
		statement.setSubject(subjectInstance);
		statement.setObject(objectInstance);
		
		return statement;
	}

	private String generateLinkQuery(String userName, String userId,
			String linkset, String nextLinkType, String verifiedLinks, Connection con) {
		String SQLqueryLinks = null;

		// Only get statements, which were not shown yet
		String[] verifications = verifiedLinks.split(" ");
		String notIn = getNotInQuery(verifications);

		boolean nextLink = Boolean.parseBoolean(nextLinkType);

		// normal
		if (verifications.length < 3) {
			echo("Get Easy Link! Number of verifications: " + verifications.length);
			SQLqueryLinks = "SELECT * FROM easy_questions,links WHERE easy_questions.ID=links.ID and easy_questions.linkedOntologies='"
					+ linkset + "' " + notIn + " order by easy_questions.Counter limit 1";
		} else if (nextLink == Message.NORMAL_LINK) {
			boolean isVerifiedLink = isVerifiedLink();
			if (isVerifiedLink == false) {// Minimum
				echo("Get Minimum Link!");
				SQLqueryLinks = "select * from links, difficulty where links.ID = difficulty.ID AND links.linkedOntologies='"
						+ linkset
						+ "' and links.Confidence not in (1,-1,-2) "
						+ notIn
						+ " order by links.Counter limit 1";
			} else { // Already validated
				echo("Get Verified Link!");
				// take: link Difficulty, user strength, not already verified links
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
					echo("Server ERROR: Get already verified link: " + e.getMessage());
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

	private double getLinkDifficulty(String id, Statement dbStmt)
			throws SQLException {
		// Get LinkDifficulty
		double difficulty = 0;
		String sqlQuery = "Select * from difficulty where ID = " + id;
		echo("Difficulty query: " + sqlQuery);
		ResultSet rs = dbStmt.executeQuery(sqlQuery);
		if (rs.next()) {
			difficulty = rs.getDouble("Difficulty");
			echo("Query Difficulty success: id = " + rs.getInt("ID")
					+ " Difficulty: " + difficulty);
		} else
			echo("Server MYSQL Error: Couldn't retrieve difficulty for id = " + id);
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

	private double getUserStrength(String userName, String userId, Connection con)
			throws SQLException {
		double userStrength = 0;
		String sqlUserStrength = "(Select strength " + "from user_strength "
				+ "where UserName='" + userName + "' " + "And UserID= '" + userId
				+ "' )";
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
						+ " Threshold : " + threshold + " Confidence: " + confidence);
				echo("What kind of link should be queried?");
				if (confidence == 1 || confidence == -2) { // manually verified links
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
					if (threshold == 0) // db value = null means stmt not verified yet
															// (counter = 0)
						eval = EVAL_FIRST;
					else
						eval = threshold;
				}
				echo("Eval: " + eval);
			} else
				echo("Server MYSQL Error: Couldn't retrieve threshold for ID = "
						+ curLink);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			echo("Server SQL Error: " + e.getMessage());
		}
		return eval;
	}

	/**
	 * Connect user and get userdata
	 * @param id
	 * @param userName
	 * @param linkset
	 * @return user
	 */
	private JSONObject getUserdata(String id, String userName) {
		echo("getUserdata ==> " + userName);
		JSONObject userJson = null;
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
						rs.getInt("Agreement"), rs.getInt("Disagreement"),rs.getInt("Unsure"),rs.getInt("Penalty"));
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
				// JSON
				userJson = new JSONObject();
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
		} catch (JSONException e) {
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
					+ PropertyConstants.DB_TABLE_NAME_USER + "` VALUES ('" + id + "' , '"
					+ userName + "', 15,5,0,0,0," + // agree, disagree, unsure, penalty,
																					// allVerif (init a:15,d:5 => 15/20
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

	private JSONObject test(String game) {
		echo("####Server: Highscore Request####");
		echo("game = "+game);
		JSONObject hScoreJson = new JSONObject();

		JSONObject sBuff = null;
		int i = 0;
		try {
			JSONArray hScoreArray = new JSONArray();
			Score s = null;
				s = new Score("test", "id", 2);
				sBuff = new JSONObject();
				sBuff.put("name", s.getName());
				sBuff.put("id", "blaa");
				sBuff.put("score", s.getScore());
							
			echo("####Server: Highscore Request Done####\n");
		
			

		}  catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			echo("JSON error####\n");
		}
		return sBuff;
	}
	
	private JSONObject getHighscore(String game) {
		echo("####Server: Highscore Request####");
		echo("game = "+game);
		JSONObject hScoreJson = new JSONObject();
		int i = 0;
		try {
			JSONArray hScoreArray = new JSONArray();
			Score s = null;
			JSONObject sBuff = null;
			DBTool db = new DBTool(resourcePath + dbIniFile);
			Connection con = db.getConnection();
			String query = "SELECT * FROM `highscores` ORDER BY `Score` DESC LIMIT 0,10";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				s = new Score(rs.getString(1), "id", rs.getInt(2));
				sBuff = new JSONObject();
				sBuff.put("name", s.getName());
				if(s.getId()==null)
					sBuff.put("id", "null");
				else
				sBuff.put("id", s.getId());
				sBuff.put("score", s.getScore());
				hScoreArray.put(sBuff);
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

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			echo("JSON error####\n");
		}
		return hScoreJson;
	}

	private JSONObject getLinkset() {
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
				linkset
						.setDescription(rs
								.getString(PropertyConstants.DB_TABLE_LINKEDONTOLOGIES_DESCRIPTION));
				linkset.setDifficulty(rs
						.getString(PropertyConstants.DB_TABLE_LINKEDONTOLOGIES_DIFFICULTY));
				linkset.setId(rs.getString(PropertyConstants.DB_TABLE_LINKEDONTOLOGIES_ID));
				tmp.add(linkset);
				i++;
				// echo(tmp.get(i)+" i "+i);
			}
			echo("Numbers of Linked Ontologies: " + tmp.size());
			echo("####Server: Get Linked Ontologies Done####\n");
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
		JSONObject linksetJSON = new JSONObject();
		JSONArray lSetArray = new JSONArray();
		JSONObject lSet = null;
		Linkset lBuff = null;
		try {
			for (int i = 0; i < tmp.size(); i++) {
				lBuff = tmp.get(i);
				lSet = new JSONObject();
				lSet.put("subject", lBuff.getSubject());
				lSet.put("object", lBuff.getObject());
				lSet.put("predicate", lBuff.getPredicate());
				lSet.put("description", lBuff.getDescription());
				lSet.put("difficulty", lBuff.getDifficulty());
				lSetArray.put(lSet);
			}
			// Out
			linksetJSON.put("linksets", lSetArray);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return linksetJSON;
	}

	private boolean isVerifiedLink() {
		Random gen = new Random();
		double r = gen.nextDouble();
		echo("isVerifiedLink? r= " + r + " eFactor=" + Balancing.EXPLORATION_FACTOR);
		if (r < Balancing.EXPLORATION_FACTOR)
			return false;
		else
			return true;
	}

	private String getTemplate() {
		String file ="";
		try{
		  FileInputStream fstream = new FileInputStream(this.resourcePath+this.templateFile);
		  // Get the object of DataInputStream
		  DataInputStream in = new DataInputStream(fstream);
		  BufferedReader br = new BufferedReader(new InputStreamReader(in));
		  String strLine;
		  //Read File Line By Line
		  while ((strLine = br.readLine()) != null)   {
		  // Print the content on the console
		  	file = file +strLine;
		  }
		  //Close the input stream
		  in.close();
		    }catch (Exception e){//Catch exception if any
		  System.err.println("Error: " + e.getMessage());
		  }
		  return file;
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

		if (removedBrackets.contains(PropertyConstants.SEPERATOR_PROPERTY_VALUE)) {
			int numberOfTypes = s.split(" ; ").length;
			String split[] = new String[numberOfTypes];
			// split
			split = removedBrackets.split(PropertyConstants.SEPERATOR_PROPERTY_VALUE);
			for (int i = 0; i < numberOfTypes; i++) { // For every type set prefix
				// echo("Split :"+i+" "+split[i]);
				split[i] = setPrefix(split[i]);
			}
			// Concat to 1 string
			for (int j = 0; j < split.length; j++) {
				if (j != split.length - 1)
					parsed += split[j] + PropertyConstants.SEPERATOR_PROPERTY_VALUE; // add
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
				prefixMap.put(split[1], split[0]); // Switch key and value (key=url,
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
			if (s.contains("#")) { // separator = '#' like 'http://purl.org/NET/dady#'
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

	private long getCurTime(){
		return System.currentTimeMillis();
	}
	
	private void echo(String s) {
		System.out.println("REST Servlet: " + s);
	}

}
