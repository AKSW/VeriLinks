package org.aksw.verilinks.core.shared.msg;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Task implements IsSerializable{

  public static final String SEPERATOR = ";SePeRaToR;";
  public static final int SIZE = 8;
  
	/**
   * HashMap that will always contain strings for both keys and values
   * @gwt.typeArgs <java.lang.String>
   */
  private String[] task;
  
  private String subject;
  private String object;
  private String predicate;
  private String description;
  private String difficulty;
  private String linkFile;
  private String date;
  private String linkset;
  
  
  
  public Task(){
    super();
    task = new String[SIZE];
  }

  /**
   * @return the subject
   */
  public String getSubject() {
    return task[0];
  }

  /**
   * @param subject the subject to set
   */
  public void setSubject(String subject) {
    this.task[0] = subject;
  }

  /**
   * @return the object
   */
  public String getObject() {
    return this.task[1];
  }

  /**
   * @param object the object to set
   */
  public void setObject(String object) {
    this.task[1] = object;
  }


  public void setPredicate(String predicate) {
		// TODO Auto-generated method stub
		this.task[2]=predicate;
	}
	public String getPredicate() {
		// TODO Auto-generated method stub
		return this.task[2];
	}

	 public void setDescription(String descr) {
			// TODO Auto-generated method stub
			this.task[3]=descr;
		}
	 
	public String getDescription() {
		// TODO Auto-generated method stub
		return this.task[3];
	}

  public void setDifficulty(String diff) {
    this.task[4]=diff;
  }
  
	public String getDifficulty() {
		// TODO Auto-generated method stub
		return this.task[4];
	}
  
  /**
   * @return the file
   */
  public String getFile() {
    return this.task[5];
  }

  /**
   * @param file the file to set
   */
  public void setFile(String file) {
    this.task[5] = file;
  }
  
  public void setTask(String[] task) {
    this.task=task;
  }
  
  public String getDate() {
    return this.task[6];
  }
  
  public String getLinkset(){
  	return this.task[7];
  }



//public String[] getTask() {
//	return task;
//}
//
//
//
//public void setTask(String[] task) {
//	this.task = task;
//}
//
//
//
//public String getSubject() {
//	return subject;
//}
//
//
//
//public void setSubject(String subject) {
//	this.subject = subject;
//}
//
//
//
//public String getObject() {
//	return object;
//}
//
//
//
//public void setObject(String object) {
//	this.object = object;
//}
//
//
//
//public String getPredicate() {
//	return predicate;
//}
//
//
//
//public void setPredicate(String predicate) {
//	this.predicate = predicate;
//}
//
//
//
//public String getDescription() {
//	return description;
//}
//
//
//
//public void setDescription(String description) {
//	this.description = description;
//}
//
//
//
//public String getDifficulty() {
//	return difficulty;
//}
//
//
//
//public void setDifficulty(String difficulty) {
//	this.difficulty = difficulty;
//}
//
//
//
//public String getLinkFile() {
//	return linkFile;
//}
//
//
//
//public void setLinkFile(String linkFile) {
//	this.linkFile = linkFile;
//}
//
//
//
//public String getDate() {
//	return date;
//}
//
//
//
//public void setDate(String date) {
//	this.date = date;
//}
//
//
//
//public String getLinkset() {
//	return linkset;
//}
//
//
//
//public void setLinkset(String linkset) {
//	this.linkset = linkset;
//}
//
//
//
//public static String getSeperator() {
//	return SEPERATOR;
//}
//
//
//
//public static int getSize() {
//	return SIZE;
//}


  
	
}
