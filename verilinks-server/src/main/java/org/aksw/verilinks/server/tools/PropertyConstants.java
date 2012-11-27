package org.aksw.verilinks.server.tools;

public class PropertyConstants {

  /**Ontology name*/
  public final static String NAME ="name";
	public static String ONTOLOGY="ontology";
  /**Endpoint*/
  public final static String ENDPOINT ="endpoint";
  /**Number of displayed properties*/
  public static String NR_OF_PROPERTIES = "#properties"; 
  /**rdfs:label*/
  public static String PROP0 = "property0";
  /**rdf:type*/
  public static String PROP1 = "property1";
  /**ontology specific*/
  public static String PROP2 = "property2";
  /**ontology specific 2*/
  public static String PROP3 = "property3";
  /**image*/
  public static final String IMAGE = "image";
  
  public static final String TYPE = "type";

  
	public static final String TEMPLATE_TYPE_NORMAL = "normalType";
	public static final String TEMPLATE_TYPE_MAP = "mapType";
  public static final String LONGITUDE_VALUE = "<http://www.w3.org/2003/01/geo/wgs84_pos#long>";
	public static final String LONGITUDE = "Longitude";
  public static final String LATITUDE_VALUE = "<http://www.w3.org/2003/01/geo/wgs84_pos#lat>";
	public static final String LATITUDE = "Latitude";
  // Confidence
  public static final int CONFIDENCE_POSITIVE= 1;
  public static final int CONFIDENCE_NEGATIVE= -2;
  public static final int CONFIDENCE_ERROR= -1;
  public static final int CONFIDENCE_NOT_SPECIFIED= 0;
  
  //Limits
  public static final int LIMIT_NUMBER_OF_TYPES = 3;
  public static final int LIMIT_NUMBER_OF_PROPERTIES = 7;
  
  // Separator
  public static final String SEPERATOR_PROPERTY_VALUE = " ; ";
  
  // Pre-defined properties
  /**rdfs:label*/
  public static String ATTR_RDFS_LABEL = "rdfs:label";
  /**rdf:type*/
  public static String ATTR_RDF_TYPE = "rdf:type";
  /**rdf:type*/
  public static String ATTR_DB_ABSTRACT = "db:abstract";

  // SPARQL queries
  public final static  String NO_DECLARATION = "Declaration missing";
  
  // MySQL database property names
  public final static String DB_TABLE_NAME_HIGHSCORES = "highscores";
  public final static String DB_TABLE_NAME_INSTANCES = "instances";
  public final static String DB_TABLE_NAME_LINKS = "links";
  public final static String DB_TABLE_NAME_TEMPLATES = "templates";
  // deprecated
  public final static String DB_TABLE_NAME_LINKEDONTOLOGIES = "linked_ontologies";
  public final static String DB_TABLE_NAME_LINKSET = "linkset";
  public final static String DB_TABLE_NAME_USER = "user";
  public final static String DB_TABLE_NAME_POSITIVE = "positive";
  public final static String DB_TABLE_NAME_NEGATIVE = "negative";
  
  public final static String DB_TABLE_LINKS_PROPERTY_ID = "ID";
  public final static String DB_TABLE_LINKS_PROPERTY_SUBJECT = "Subject";
  public final static String DB_TABLE_LINKS_PROPERTY_PREDICATE = "Predicate";
  public final static String DB_TABLE_LINKS_PROPERTY_OBJECT = "Object";
  // deprecated
  public final static String DB_TABLE_LINKS_PROPERTY_LINKED_ONTOLOGIES = "linkedOntologies";
  public final static String DB_TABLE_LINKS_PROPERTY_LINKSET = "Linkset";
  public final static String DB_TABLE_LINKS_PROPERTY_CONFIDENCE = "Confidence";
  public final static String DB_TABLE_LINKS_PROPERTY_COUNTER = "Counter";
  public final static String DB_TABLE_LINKS_PROPERTY_POSITIVE = "Positive";
  public final static String DB_TABLE_LINKS_PROPERTY_NEGATIVE = "Negative";
  
  public final static String DB_TABLE_INSTANCES_PROPERTY_URI = "URI";
	public static final String DB_TABLE_INSTANCES_PROPERTY_NAME = "Property";
	public static final String DB_TABLE_INSTANCES_PROPERTY_VALUE = "Value";
	
  public final static String DB_TABLE_INSTANCES_PROPERTY_PROP0 = "Property0";
  public final static String DB_TABLE_INSTANCES_PROPERTY_PROP1 = "Property1";
  public final static String DB_TABLE_INSTANCES_PROPERTY_PROP2 = "Property2";
  public final static String DB_TABLE_INSTANCES_PROPERTY_PROP3 = "Property3";
  public final static String DB_TABLE_INSTANCES_PROPERTY_ONTOLOGY = "Ontology";
  public final static String DB_TABLE_INSTANCES_PROPERTY_IMAGE = "Image";
  public final static String DB_TABLE_INSTANCES_PROPERTY_TYPE = "Type";
  public final static String DB_TABLE_INSTANCES_PROPERTY_LONG = "Longitude";
  public final static String DB_TABLE_INSTANCES_PROPERTY_LAT = "Latitude";

  public final static String DB_TABLE_HIGHSCORES_PLAYER_NAME = "Name";
  public final static String DB_TABLE_HIGHSCORES_PLAYER_ID = "ID";
  public final static String DB_TABLE_HIGHSCORES_SCORE = "Score";
  
  public final static String DB_TABLE_USER_ID = "UserID";
  public final static String DB_TABLE_USER_NAME = "UserName";
	public final static String DB_TABLE_USER_AGREEMENT="Agreement";
	public final static String DB_TABLE_USER_DISAGREEMENT="Disagreement";
	public final static String DB_TABLE_USER_UNSURE="Unsure";
	public final static String DB_TABLE_USER_PENALTY="Penalty";
	public final static String DB_TABLE_USER_VERIFIED="Verified";
	public final static String DB_TABLE_USER_GAMESPLAYED= "GamesPlayed";
	public final static String DB_TABLE_USER_PLAYTIME= "PlayTime";
	public final static String DB_TABLE_USER_LEVEL1CLEARED= "Level1Cleared";
	public final static String DB_TABLE_USER_LEVEL1TIME= "Level1Time";
	public final static String DB_TABLE_USER_LEVEL2CLEARED= "Level2Cleared";
	public final static String DB_TABLE_USER_LEVEL2TIME= "Level2Time";
	public final static String DB_TABLE_USER_LEVEL3CLEARED= "Level3Cleared";
	public final static String DB_TABLE_USER_LEVEL3TIME= "Level3Time";
	
  public final static String DB_TABLE_POSITIVE_LINK_ID = "LinkID";
  public final static String DB_TABLE_POSITIVE_USER_ID = "UserID";
	public final static String DB_TABLE_POSITIVE_USER_NAME="UserName";
	
  public final static String DB_TABLE_NEGATIVE_LINK_ID = "LinkID";
  public final static String DB_TABLE_NEGATIVE_USER_ID = "UserID";
  public final static String DB_TABLE_NEGATIVE_USER_NAME="UserName";
  
  public final static String DB_TABLE_TEMPLATES_PROPERTY_NAME = "Name";
  public final static String DB_TABLE_TEMPLATES_PROPERTY_ENDPOINT = "Endpoint";
  public final static String DB_TABLE_TEMPLATES_PROPERTY_PROP0 = "Property0";
  public final static String DB_TABLE_TEMPLATES_PROPERTY_PROP1 = "Property1";
  public final static String DB_TABLE_TEMPLATES_PROPERTY_PROP2 = "Property2";
  public final static String DB_TABLE_TEMPLATES_PROPERTY_PROP3 = "Property3";
  public final static String DB_TABLE_TEMPLATES_PROPERTY_IMAGE = "Image";
  
  public final static String DB_TABLE_LINKEDONTOLOGIES_SUBJECT = "Subject";
  public final static String DB_TABLE_LINKEDONTOLOGIES_PREDICATE = "Predicate";
  public final static String DB_TABLE_LINKEDONTOLOGIES_OBJECT = "Object";
  public final static String DB_TABLE_LINKEDONTOLOGIES_DESCRIPTION = "Description";
  public final static String DB_TABLE_LINKEDONTOLOGIES_DIFFICULTY = "Difficulty";
  public final static String DB_TABLE_LINKEDONTOLOGIES_READY = "Ready";
	public final static String DB_TABLE_LINKEDONTOLOGIES_ID = "ID";
	
	public static final String ATTR_FILTER = "filter";
	public static final String ATTR_IMPORTANT = "important";
	public static final String ATTR_ZOOM = "zoomFactor";
	public static final String ATTR_LINKEDDATA = "linkedData";
	public static final String ATTR_LIMIT = "limit";
	
	public static final String DIFFICULTY = "difficulty";
	public static final String DIFFICULTY_EASY = "easy";
	public static final String DIFFICULTY_MEDIUM = "medium";
	public static final String DIFFICULTY_HARD = "hard";

	
	public static final String DB_TABLE_NAME_DIFFICULTY = "difficulty";
  public final static String DB_TABLE_DIFFICULTY_DIFFICULTY = "Difficulty";
	public static final String DB_TABLE_DIFFICULTY_LINKEDONTOLOGIES = "linkedOntologies";



}
