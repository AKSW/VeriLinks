package org.aksw.verilinks.games.peaInvasion.shared.msg;

public class Verification {
	private int id;
	private int verification;
	
	public Verification(int id, int verification){
		this.id=id;
		this.verification=verification;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVerification() {
		return verification;
	}

	public void setVerification(int verification) {
		this.verification = verification;
	}
	

}
