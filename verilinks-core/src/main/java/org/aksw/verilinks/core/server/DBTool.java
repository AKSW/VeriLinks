package org.aksw.verilinks.core.server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;


import java.sql.*;
import java.util.prefs.Preferences;

import org.ini4j.IniPreferences;

import org.aksw.verilinks.core.shared.PropertyConstants;
/**
 * JDBC Tools
 */
public class DBTool{
	
	/**serverName*/
	private String serverName;
	/**portNumber*/
	private String database;
	/**userName*/
	private String userName;
	/**password*/
	private String password;
	/**url Connection data*/
	private String url;
	
	private String iniFilePath;
	
	/**
	 *Constructor
	 *@param newServerName name of the server
	 *@param newDatabase name of the database
	 *@param newUserName username
	 *@param newPassword password
	 */
	public DBTool(String newServerName, String newDatabase, String newUserName, String newPassword) {
		this.serverName = newServerName;
		this.database = newDatabase;
		this.userName = newUserName;
		this.password = newPassword;
		this.createUrl();
	}
	
	/**
   * Constructor for Veri-Links.
   * Change according to your database.    
   */
  public DBTool(String iniFilePath) {
    this.iniFilePath=iniFilePath;
    /*
    this.serverName = "localhost";
    this.database = "veri_links";
    this.userName = "root";
    this.password = "";
    this.createUrl();
    */
    //String iniFile = "db_settings.ini";
    //String iniFile = "d://db_settings.ini";
    try {
      
      Preferences prefs = new IniPreferences(new FileReader(iniFilePath));
      this.serverName = prefs.node("database").get("server", null);
      this.database = prefs.node("database").get("name", null);
      this.userName = prefs.node("database").get("user", null);
      this.password = prefs.node("database").get("pass", null);
  
      Class.forName("com.mysql.jdbc.Driver");
      this.url = "jdbc:mysql://" + serverName + "/" + database;
      
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
    } 
    
  }
  
	/**
	 * Appending serverName and database to url String
	 */
	public void createUrl(){
		this.url = "jdbc:mysql://" + this.serverName + "/" + this.database; 
	}
	
	/**
	 * Connects to database
	 * @return Connection
	 */
	public Connection getConnection(){
		System.out.println("Connecting to database..");
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver"); //JDBC Driver
		} catch(java.lang.ClassNotFoundException e) {
			System.err.print("ClassNotFoundException: ");
			System.err.println(e.getMessage());
		}
		try {
			con = DriverManager.getConnection(this.url, this.userName, this.password);
			//con = DriverManager.getConnection(this.url);
			System.out.println("Connected to database");
		} catch(SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}
		return con;
	}
	
	/**
	 * Executes the given statement, which may be an INSERT, 
	 * UPDATE, or DELETE statement or an SQL statement that returns nothing.
	 * Automatically connect to database.
	 * @param updateStatement String
	 */
	public void queryUpdateOnce( String updateStatement )throws Exception{
		Connection con = this.getConnection(); //establish connection
		try{
			Statement stmt = con.createStatement();
			stmt.executeUpdate(updateStatement);
			stmt.close();
		} catch (SQLException ex){
			System.err.println("SQLException: " + ex.getMessage());
		}
		con.close();
	}
	
	/**
	 * Executes the given statement, which may be an INSERT, 
	 * UPDATE, or DELETE statement or an SQL statement that returns nothing.
	 * Connection to database has to be managed manually.
	 * @param updateStatement String
	 */
	public void queryUpdate( String updateStatement , Connection con){
		try{
			Statement stmt = con.createStatement();
			stmt.executeUpdate(updateStatement);
			stmt.close();
		} catch (SQLException ex){
			System.err.println("SQLException: " + ex.getMessage());
		}
	}
	
	
	
	/**
	 * Executes the given statement, which returns a ResultSet.
	 * Don't forget to close connection
	 * @param resultSetQuery String
	 * @return rs ResultSet
	 */
	public ResultSet queryExecute(String resultSetQuery) throws Exception{
		Connection con = this.getConnection(); //establish connection
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(resultSetQuery);
		return rs;
	}
	
	/**
	 * reads File
	 * @param fileName
	 */
	public void readFile(String fileName){
		File file = new File(fileName);
		FileInputStream fis = null;
	    BufferedInputStream bis = null;
	    DataInputStream dis = null;
	    
	    try{
	    	fis = new FileInputStream(file);
	    	bis = new BufferedInputStream(fis);
	    	dis = new DataInputStream(bis);
	    }
	    catch (FileNotFoundException e){
	    	e.printStackTrace();
	    }
	    catch(IOException e){
	    	e.printStackTrace();
	    }
	}


	 private Connection initDBConnection() {
	   System.out.println("Initialize database connection...");
	   Connection conn = null;
	   try {
         
         conn = DriverManager.getConnection(this.url, this.userName, this.password);
         System.out.println(url);
         

     } catch (SQLException e) {
         e.printStackTrace();
         System.out.println(e.getMessage());
     }
     System.out.println("Connection established.");
     return conn;
 }
	
	/**
   * Method for creating veri_links database and tables.
   * Chacnge connection data according to your database.
	 * @throws SQLException Error creating database
   */
  public void createDatabase() throws SQLException {
    
    System.out.println("##Begin creating '"+this.database+"' database..");
    String createDatabase =      "CREATE DATABASE "+this.database+";";
    String useDatabase=          "USE "+this.database+";";
    
    String createLinksTable =    "CREATE TABLE "+PropertyConstants.DB_TABLE_NAME_LINKS + " ( " +
                               PropertyConstants.DB_TABLE_LINKS_PROPERTY_ID+" INT NOT NULL AUTO_INCREMENT, " +
    		                       PropertyConstants.DB_TABLE_LINKS_PROPERTY_SUBJECT + " VARCHAR(150), "+
    		                       PropertyConstants.DB_TABLE_LINKS_PROPERTY_PREDICATE + " VARCHAR(150), " +
    		                       PropertyConstants.DB_TABLE_LINKS_PROPERTY_OBJECT +" VARCHAR(150), " +
    		                       PropertyConstants.DB_TABLE_LINKS_PROPERTY_LINKED_ONTOLOGIES +" VARCHAR(50), " +
    		                       PropertyConstants.DB_TABLE_LINKS_PROPERTY_CONFIDENCE + " DOUBLE, " +
    		                       PropertyConstants.DB_TABLE_LINKS_PROPERTY_COUNTER + " INT DEFAULT '0', " +
    		                       PropertyConstants.DB_TABLE_LINKS_PROPERTY_POSITIVE + " INT DEFAULT '0', " +
    		                       PropertyConstants.DB_TABLE_LINKS_PROPERTY_NEGATIVE + " INT DEFAULT '0', " +
    		                       		"PRIMARY KEY( "+
	    		                       		PropertyConstants.DB_TABLE_LINKS_PROPERTY_SUBJECT+","+
	    		                       		PropertyConstants.DB_TABLE_LINKS_PROPERTY_PREDICATE+","+
	    		                       		PropertyConstants.DB_TABLE_LINKS_PROPERTY_OBJECT+")," +
    		                       		"UNIQUE("+PropertyConstants.DB_TABLE_LINKS_PROPERTY_ID+"));";
    
    String createInstanceTable = "CREATE TABLE "+PropertyConstants.DB_TABLE_NAME_INSTANCES + " ( " +
                               PropertyConstants.DB_TABLE_INSTANCES_PROPERTY_URI + " VARCHAR(150), " +
                               PropertyConstants.DB_TABLE_INSTANCES_PROPERTY_NAME + " VARCHAR(150), " +
                               PropertyConstants.DB_TABLE_INSTANCES_PROPERTY_VALUE + " VARCHAR(600), " +
                               		"PRIMARY KEY("+PropertyConstants.DB_TABLE_INSTANCES_PROPERTY_URI+","+
                               		PropertyConstants.DB_TABLE_INSTANCES_PROPERTY_NAME+") );";
    
    String createHighscoreTable= "CREATE TABLE "+PropertyConstants.DB_TABLE_NAME_HIGHSCORES + " ( " +
                               PropertyConstants.DB_TABLE_HIGHSCORES_PLAYER + " VARCHAR(15), " +
                               PropertyConstants.DB_TABLE_HIGHSCORES_SCORE + " INT );";
    
    String createUserTable= "CREATE TABLE "+PropertyConstants.DB_TABLE_NAME_USER + " ( " +
                               PropertyConstants.DB_TABLE_USER_ID + " VARCHAR(50), " +
                               PropertyConstants.DB_TABLE_USER_NAME + " VARCHAR(40), " +
                               PropertyConstants.DB_TABLE_USER_AGREEMENT + " INT DEFAULT 0, " +
                               PropertyConstants.DB_TABLE_USER_DISAGREEMENT + " INT DEFAULT 0, " +
                               PropertyConstants.DB_TABLE_USER_UNSURE + " INT DEFAULT 0, " +
                               PropertyConstants.DB_TABLE_USER_PENALTY + " INT DEFAULT 0, " +
                               PropertyConstants.DB_TABLE_USER_VERIFIED + " INT DEFAULT 0, " +
                               PropertyConstants.DB_TABLE_USER_GAMESPLAYED + " INT DEFAULT 0, " +
                               PropertyConstants.DB_TABLE_USER_PLAYTIME + " DOUBLE DEFAULT 0, " +
                               PropertyConstants.DB_TABLE_USER_LEVEL1CLEARED + " INT DEFAULT 0, " +
                               PropertyConstants.DB_TABLE_USER_LEVEL1TIME + " DOUBLE DEFAULT 0, " +
                               PropertyConstants.DB_TABLE_USER_LEVEL2CLEARED + " INT DEFAULT 0, " +
                               PropertyConstants.DB_TABLE_USER_LEVEL2TIME + " DOUBLE DEFAULT 0, " +
                               PropertyConstants.DB_TABLE_USER_LEVEL3CLEARED + " INT DEFAULT 0, " +
                               PropertyConstants.DB_TABLE_USER_LEVEL3TIME + " DOUBLE DEFAULT 0, " +
                  
                               		"PRIMARY KEY ("+ PropertyConstants.DB_TABLE_USER_ID+","+
                               		PropertyConstants.DB_TABLE_USER_NAME+"));";

    String createPositiveTable= "CREATE TABLE "+PropertyConstants.DB_TABLE_NAME_POSITIVE + " ( " +
                               PropertyConstants.DB_TABLE_POSITIVE_LINK_ID + " INT, " +
                               PropertyConstants.DB_TABLE_POSITIVE_USER_ID + " VARCHAR(50), " +
                               PropertyConstants.DB_TABLE_POSITIVE_USER_NAME + " VARCHAR(40), " +
                               		"FOREIGN KEY ("+PropertyConstants.DB_TABLE_POSITIVE_LINK_ID +") " +
                               				"REFERENCES "+PropertyConstants.DB_TABLE_NAME_LINKS+
                               				"("+PropertyConstants.DB_TABLE_LINKS_PROPERTY_ID+") " +
                               						"ON DELETE CASCADE, "+
                                   "FOREIGN KEY ("+PropertyConstants.DB_TABLE_POSITIVE_USER_ID+","+
                                   PropertyConstants.DB_TABLE_POSITIVE_USER_NAME+") "+
                                       "REFERENCES "+PropertyConstants.DB_TABLE_NAME_USER+
                                       " ON DELETE CASCADE "+
                                 ");";
   
    String createNegativeTable= "CREATE TABLE "+PropertyConstants.DB_TABLE_NAME_NEGATIVE + " ( " +
                              PropertyConstants.DB_TABLE_NEGATIVE_LINK_ID + " INT, " +
                              PropertyConstants.DB_TABLE_NEGATIVE_USER_ID + " VARCHAR(50), " +
                              PropertyConstants.DB_TABLE_NEGATIVE_USER_NAME + " VARCHAR(40), " +
                                 "FOREIGN KEY ("+PropertyConstants.DB_TABLE_NEGATIVE_LINK_ID +") " +
                                     "REFERENCES "+PropertyConstants.DB_TABLE_NAME_LINKS+
                                     "("+PropertyConstants.DB_TABLE_LINKS_PROPERTY_ID+") " +
                                         "ON DELETE CASCADE, " +
                                 "FOREIGN KEY ("+PropertyConstants.DB_TABLE_NEGATIVE_USER_ID+", "+
                                 	PropertyConstants.DB_TABLE_NEGATIVE_USER_NAME+") "+
                                     "REFERENCES "+PropertyConstants.DB_TABLE_NAME_USER+
                                     " ON DELETE CASCADE "+
                                ");";
    System.out.println("pos: "+createPositiveTable);
    System.out.println("neg: "+createNegativeTable);
    
    // not needed
    String createTemplatesTable= "CREATE TABLE "+PropertyConstants.DB_TABLE_NAME_TEMPLATES + " ( " +
                              PropertyConstants.DB_TABLE_TEMPLATES_PROPERTY_NAME + " VARCHAR(150), " +
                              PropertyConstants.DB_TABLE_TEMPLATES_PROPERTY_ENDPOINT + " VARCHAR(150), " +
                              PropertyConstants.DB_TABLE_TEMPLATES_PROPERTY_PROP0 + " VARCHAR(150), " +
                              PropertyConstants.DB_TABLE_TEMPLATES_PROPERTY_PROP1 + " VARCHAR(150), " +
                              PropertyConstants.DB_TABLE_TEMPLATES_PROPERTY_PROP2 + " VARCHAR(150), " +
                              PropertyConstants.DB_TABLE_TEMPLATES_PROPERTY_PROP3 + " VARCHAR(150), " +
                              PropertyConstants.DB_TABLE_TEMPLATES_PROPERTY_IMAGE +" VARCHAR(150), " +
                                 "PRIMARY KEY("+PropertyConstants.DB_TABLE_TEMPLATES_PROPERTY_NAME+") );";
    
    String createLinkedOntologiesTable= "CREATE TABLE "+PropertyConstants.DB_TABLE_NAME_LINKEDONTOLOGIES + " ( " +
    													PropertyConstants.DB_TABLE_LINKEDONTOLOGIES_ID+ " VARCHAR(50), " +
                              PropertyConstants.DB_TABLE_LINKEDONTOLOGIES_SUBJECT+ " VARCHAR(50), " +
                              PropertyConstants.DB_TABLE_LINKEDONTOLOGIES_PREDICATE +" VARCHAR(100),"+
                              PropertyConstants.DB_TABLE_LINKEDONTOLOGIES_OBJECT + " VARCHAR(50)," +
                              PropertyConstants.DB_TABLE_LINKEDONTOLOGIES_DESCRIPTION + " VARCHAR(200)," +
                              PropertyConstants.DB_TABLE_LINKEDONTOLOGIES_DIFFICULTY + " VARCHAR(20)," +
                              PropertyConstants.DB_TABLE_LINKEDONTOLOGIES_READY + " TINYINT(1)," +
                              		"PRIMARY KEY ("+PropertyConstants.DB_TABLE_LINKEDONTOLOGIES_ID+") );";

    
  
    
    String createUnsureView = "create view unsure as " +
    		"select ID, (Counter - Positive - Negative) as Unsure from links";
    		
    
    String createLinkDifficultyView = "create view difficulty as " +
			"SELECT links.ID, round(( (unsure.Unsure/Counter + 1.48/(2*Counter))/ (1+1.48/Counter)),2) as Difficulty " +
			"from links, unsure " +
			"where links.ID = unsure.ID";
    
    String createUserStrengthView = "Create View user_strength as " +
    		"select UserID, UserName, if( " +
    			"((Agreement/(Agreement+Disagreement+Penalty)-0.5)/0.5)<0," +
    			"0, " +
    			"round(  ((Agreement/(Agreement+Disagreement+Penalty)-0.5)/0.5)  ,2)) as Strength " +
    		"from user";
    
    String createEvalNegView ="Create view evaluate_negative as " +
    		"SELECT ID, if(Counter = 1, -1,round( ((Negative/Counter + 1.48/(2*Counter))/ (1+1.48/Counter)),2)) as Threshold, Confidence " +
    		"from links";
    
    String createEvalPosView ="Create view evaluate_positive as " +
    		"SELECT ID, if(Counter = -2, -1,round( ((Positive/Counter + 1.48/(2*Counter))/ (1+1.48/Counter)),2)) as Threshold, Confidence " +
    		"from links";
    
    String createEasyQuestions ="create view easy_questions as " +
    		"select links.ID as ID, links.linkedOntologies as linkedOntologies, links.Counter as Counter, difficulty.Difficulty as Difficulty " +
    		"from links, difficulty " +
    		"where links.ID=difficulty.ID and difficulty.Difficulty < 0.2 " +
    		"order by links.Counter";
    
//    String createDifficultyTable= "CREATE TABLE "+PropertyConstants.DB_TABLE_NAME_DIFFICULTY + " ( " +
//    PropertyConstants.DB_TABLE_DIFFICULTY_LINKEDONTOLOGIES +" VARCHAR(100),"+
//    PropertyConstants.DB_TABLE_DIFFICULTY_DIFFICULTY + " VARCHAR(50)," +
//    		"PRIMARY KEY ("+PropertyConstants.DB_TABLE_DIFFICULTY_LINKEDONTOLOGIES+","+
//    		    PropertyConstants.DB_TABLE_DIFFICULTY_DIFFICULTY + ") );";

    
    /*
    try {
      
      Preferences prefs = new IniPreferences(new FileReader(iniFilePath));
      this.serverName = prefs.node("database").get("server", null);
      this.database = "";
      this.userName = prefs.node("database").get("user", null);
      this.password = prefs.node("database").get("pass", null);
  
      Class.forName("com.mysql.jdbc.Driver");
      this.url = "jdbc:mysql://" + serverName + "/" + database;
    }  catch (ClassNotFoundException e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
    } 
    */
      // DBTool Constructor parameters -> (serverName, databaseName, userName, password)
      //DBTool db = new DBTool("localhost","", "root", ""); // <--- Change this
      DBTool db = new DBTool(iniFilePath);
      db.setDatabase("");
      db.createUrl();
      //db.setDatabase("");
      Connection con = db.getConnection();
      db.queryUpdate(createDatabase, con);
      db.queryUpdate(useDatabase, con);
      db.queryUpdate(createLinksTable, con);
      db.queryUpdate(createInstanceTable, con);
      db.queryUpdate(createHighscoreTable, con);
      //db.queryUpdate(createTemplatesTable, con);
      db.queryUpdate(createLinkedOntologiesTable, con);
      db.queryUpdate(createUserTable, con);
      db.queryUpdate(createPositiveTable, con);
      db.queryUpdate(createNegativeTable, con);
      
      db.queryUpdate(createUnsureView, con);
      db.queryUpdate(createLinkDifficultyView, con);
      db.queryUpdate(createUserStrengthView, con);
      db.queryUpdate(createEvalNegView, con);
      db.queryUpdate(createEvalPosView, con);
      db.queryUpdate(createEasyQuestions, con);
//      db.queryUpdate(createDifficultyTable, con);
      con.close();
      System.out.println("##Done");
  
  }
  
  public void setDatabase(String db) {
    this.database=db;
  }
  public String bla(){
    //File inFile = new File("highScore.txt"); 
    //System.out.println(inFile.getAbsolutePath());
    System.out.println(GWT.getModuleBaseURL());
    return GWT.getModuleBaseURL() ;
  }
	
  public String getDatabase() {
    return this.database;
  }
  
	public static void main(String args[]){
		
		DBTool db = new DBTool("d://db_settings.ini");
		//db.createDatabase();
		System.out.println(db.getClass());
		
		//Test current dir
		String dir="user.dir"; // set to current directory
	  try {dir=new File(System.getProperty(dir)).getCanonicalPath();}
	  catch (IOException e1) { }
	  System.out.println ("Current dir : " + dir);
	  
	  //db.bla();
	  //createDatabase();
	  //Constructor
		//RDFHandler test = new RDFHandler("root","" , "localhost","RDFdata");
		//DBTool test = new DBTool("localhost","RDFdata", "root", "");
		

	}
	
}


