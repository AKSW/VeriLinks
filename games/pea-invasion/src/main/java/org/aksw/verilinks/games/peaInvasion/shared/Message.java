package org.aksw.verilinks.games.peaInvasion.shared;

public class Message {

  // Server
  public final static String MSG_RUNNING = "Server is running :)";
  public final static String MSG_NOT_RUNNING = "Server is not running :(";
  
  public static final String STATUS_GET_EVALUATED_STATEMENT="Get evaluated statement";
  public static final String STATUS_GET_NORMAL_STATEMENT="Get normal statement";
  public static final String STATUS_WAIT_FOR_EVALUATED_STATEMENT="Wait for evaluated statement";
  
  public static final String SEND_EVALUATED_STATEMENT="Send evaluated statement";
  public static final String SEND_NORMAL_STATEMENT="Send normal statement";
  
  public static final boolean NORMAL_LINK = false;
  public static final boolean EVAL_LINK = true;
	public static String USERNAME_LOGIN= "username-login: only name available";
}
