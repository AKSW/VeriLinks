package org.aksw.verilinks.games.peaInvasion.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Bonus implements IsSerializable{ 

	private int bonus;
	private double difficulty;
	
	public Bonus(){};
	
	public Bonus(int bonus, double difficulty){
		this.bonus=bonus;
		this.difficulty=difficulty;
	}

	public int getBonus() {
		return bonus;
	}

	public void setBonus(int bonus) {
		this.bonus = bonus;
	}

	public double getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(double difficulty) {
		this.difficulty = difficulty;
	}
	
}
