package org.aksw.verilinks.games.peaInvasion.shared.msg;

import java.util.ArrayList;

import com.google.gwt.dev.json.JsonObject;

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
		JsonObject json = new JsonObject();
		
		JsonObject jUser = new JsonObject();
		jUser.put("id", user.getId());
		jUser.put("name", user.getName());
		
		JsonObject jVeriList = null;
		JsonObject jVeri = null;
		Verification v = null;
		for(int i = 0; i<this.verification.size();i++){
			v =this.verification.get(i);
			jVeri = new JsonObject();
			jVeri.put("id", v.getId());
			jVeri.put("veri", v.getVerification());
			jVeriList.put("verification", jVeri);
		}
		
		json.put("User", jUser);
		json.put("Verification", jVeriList);
		
		return null;
		
	}
	
}
