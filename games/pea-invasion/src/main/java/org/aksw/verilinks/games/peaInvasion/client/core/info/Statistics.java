package org.aksw.verilinks.games.peaInvasion.client.core.info;

import org.aksw.verilinks.games.peaInvasion.shared.User;
import org.aksw.verilinks.games.peaInvasion.shared.VerificationStatistics;

public class Statistics {

	private User user;
	private int score;
	private int money;
	private int level;
	private int time;
	private VerificationStatistics verification;
	
	public Statistics(){
	
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public VerificationStatistics getVerification() {
		return verification;
	}

	public void setVerification(VerificationStatistics verification) {
		this.verification = verification;
	}

}
