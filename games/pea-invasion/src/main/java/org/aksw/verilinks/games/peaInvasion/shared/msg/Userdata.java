package org.aksw.verilinks.games.peaInvasion.shared.msg;

public class Userdata {

	private String name=null;
	private String id=null;
	private int highscore=0;
	private String strength=null;
	private int numVeri=0;
	private int numAgree=0;
	private int numDisagree=0;
	private int numUnsure=0;
	private int numPenalty=0;
	
	public Userdata(String id, String name, int highscore, String strength,
			int numVeri, int numAgree, int numDisagree, int numUnsure, int numPenalty){
		this.id=id;
		this.name=name;
		this.highscore=highscore;
		this.strength = strength;
		this.numVeri=numVeri;
		this.numAgree=numAgree;
		this.numDisagree=numDisagree;
		this.numUnsure = numUnsure;
		this.numPenalty=numPenalty;
		
	}

	public Userdata() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getHighscore() {
		return highscore;
	}

	public void setHighscore(int highscore) {
		this.highscore = highscore;
	}

	public String getStrength() {
		return strength;
	}

	public void setStrength(String strength) {
		this.strength = strength;
	}

	public int getNumVeri() {
		return numVeri;
	}

	public void setNumVeri(int numVeri) {
		this.numVeri = numVeri;
	}

	public int getNumAgree() {
		return numAgree;
	}

	public void setNumAgree(int numAgree) {
		this.numAgree = numAgree;
	}

	public int getNumDisagree() {
		return numDisagree;
	}

	public void setNumDisagree(int numDisagree) {
		this.numDisagree = numDisagree;
	}

	public int getNumUnsure() {
		return numUnsure;
	}

	public void setNumUnsure(int numUnsure) {
		this.numUnsure = numUnsure;
	}

	public int getNumPenalty() {
		return numPenalty;
	}

	public void setNumPenalty(int numPenalty) {
		this.numPenalty = numPenalty;
	}
}
