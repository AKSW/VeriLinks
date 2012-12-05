package org.aksw.verilinks.games.peaInvasion.shared.msg;

import java.util.ArrayList;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;


public class Commit {

	private User user;
	
	private ArrayList<Verification> verification;
	
	public Commit(){
		this.verification=new ArrayList<Verification>();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ArrayList<Verification> getVerification() {
		return verification;
	}

	public void setVerification(ArrayList<Verification> verification) {
		this.verification = verification;
	}
	
	public void addVerification(Verification verification) {
		if(!this.verification.contains(verification))
			this.verification.add(verification);
	}
	
	public String getJson(){
		JSONObject json = new JSONObject();
		
		JSONObject jUser = new JSONObject();
		jUser.put("id", new JSONString(user.getId()));
		jUser.put("name", new JSONString(user.getName()));
		
		JSONObject jVeriList = null;
		JSONObject jVeri = null;
		Verification v = null;
		for(int i = 0; i<this.verification.size();i++){
			v =this.verification.get(i);
			jVeri = new JSONObject();
			jVeri.put("id", new JSONNumber(v.getId()));
			jVeri.put("veri",  new JSONNumber(v.getVerification()));
			jVeriList.put("verification", jVeri);
		}
		
		json.put("User", jUser);
		json.put("Verification", jVeriList);
		
		return null;
		
	}
	
}
