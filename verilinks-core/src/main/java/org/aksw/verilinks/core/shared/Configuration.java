package org.aksw.verilinks.core.shared;

public class Configuration {
	
	/**
	 *  User has semantic web knowledge.
	 *  Show uri, properties namespace etc. 
	 */
	public final static String KNOWLEDGE_EXPERT = "knowledge expert";
	
	/**
	 *  User has no semantic web knowledge.
	 *  Only show necessaray and intuitiv informatio.n
	 */
	public final static String KNOWLEDGE_NORMAL = "knowledge normal";
	
	
	private String knowledgeMode;
	private boolean loginNeeded;
	private boolean kongregate;
	private boolean simple;
	
	
	public Configuration(){
		this.knowledgeMode=KNOWLEDGE_EXPERT;
//		this.knowledgeMode=KNOWLEDGE_NORMAL;
		this.loginNeeded = true;
		this.kongregate=false;
		this.simple=true;
	}
	
	public Configuration(String mode, boolean login){
		this.knowledgeMode=mode;
		this.loginNeeded=login;
		this.kongregate=!login;
	}

	public String getKnowledgeMode(){
		return this.knowledgeMode;
	}
	public void setKnowledgeMode(String mode){
		this.knowledgeMode=mode;
	}
	// here
	public boolean getLoginNeeded(){
		return this.loginNeeded;
//		return true;
	}

	public boolean isKongregate(){
		return this.kongregate;
	}
	/** If called from kongregate page, no login needed*/
	public void setKongregate(boolean b) {
		this.kongregate=b;
		this.loginNeeded=!b;
	}
	
	public boolean isSimple(){
		return this.simple;
	}
	/** If called from kongregate page, no login needed*/
	public void setSimple(boolean b) {
		this.simple=b;
		this.loginNeeded=b;
	}
	
}
