package org.aksw.verilinks.server.msg;


public class User {
  private String id;
  private String name;
  private String friends;
  private long startTime=0;
	private long playTime=0;
  private int currentLevel=0;
  private long currentLevelTime=0;
  private long currentLevelStartTime=0;
	private String strength="unknown";
	private boolean credible=true;
  
  public User(){
    
  }
  
  public User(String id, String name){
    this.id = id;
    this.name=name;
  }
  
  public User(String id, String name, String friends){
    this.id = id;
    this.name=name;
    this.friends=friends;
  }

  /**
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the friends
   */
  public String getFriends() {
    return friends;
  }

  /**
   * @param friends the friends to set
   */
  public void setFriends(String friends) {
    this.friends = friends;
  }

	public void setGuest() {
		this.name="Guest";
		this.id="Guest#NoID";
		
	}

	public void setStartTime(long startTime) {
		this.startTime=startTime;
	}
	
	public void calcPlayTime(long endTime){
		this.playTime=endTime-startTime;
	}
	
	public long getPlayTime(){
		return this.playTime;
	}


	public int getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}

	public long getCurrentLevelTime() {
		return currentLevelTime;
	}

	public void calcCurrentLevelTime(long time) {
		this.currentLevelTime = time - this.currentLevelStartTime;
		
	}
	public void setCurrentLevelStartTime(long time){
		this.currentLevelStartTime=time;
	}

	public void setStrength(String strength) {
		this.strength=strength;
		
	}
	
	public String getStrength() {
		return this.strength;
		
	}

	public void setCredible(boolean credible) {
		this.credible = credible;
	}

	public boolean isCredible() {
		return credible;
	}
}
