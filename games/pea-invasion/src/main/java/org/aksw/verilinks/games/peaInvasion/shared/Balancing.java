package org.aksw.verilinks.games.peaInvasion.shared;

import java.util.Random;

public class Balancing {
	public static final String EASY="easy";
	public static final String MEDIUM="medium";
	public static final String HARD="hard";
	public static final String VERY_HARD="very hard";
	public static final String ULTIMATE="ultimate";
	public static final String UNKNWON = "unknown";
	
	public static final String NOVICE="novice";
	public static final String APPRENTICE="apprentice";
	public static final String VETERAN="veteran";
	public static final String MASTER="master";
	public static final String HERO="hero";
	public static final String LEGEND = "legend";
	public static final String ETERNAL = "eternal";
	
	
	public static final double EXPLORATION_FACTOR = 0.6;
	
	public static double getLinkDifficulty(double difficulty) {
		if(0<=difficulty && difficulty <0.2)
//			return EASY;
			return 0.1;
		else if(0.2<=difficulty && difficulty <0.5)
//			return MEDIUM;
			return 0.2;
		else if(0.5<=difficulty && difficulty <0.7)
//			return HARD;
			return 0.4;
		else if(0.7<=difficulty && difficulty <0.9)
//			return VERY_HARD;
			return 1;
		else if(0.9<=difficulty && difficulty <1.0)
//			return ULTIMATE;
			return 5;
		else
//			return "difficultyError";
			return -1;
	}
	
	public static double getUserStrength(double str){
		// Novice
		if(str<=0.60)
			return random(0,0.3);
		else if(0.61<=str && str<0.71)
			return random(0,0.6);
		else if(0.72<=str && str<0.76)
			return random(0,0.75);
		else if(0.77<=str && str <0.81)
			return random(0,0.9);
		else if(0.82<=str && str <0.95)
			return random(0,1);
		else if(0.96<=str && str <0.9)
			return random(0.2,1);
		else if(str == 1)
			return random(0.5,1);
		else
			return -1;
	}

	public static String getStringUserStrength(double str) {
		// Novice
			if(str<=0.60)
				return NOVICE;
			else if(0.61<=str && str<0.71)
				return APPRENTICE;
			else if(0.72<=str && str<0.76)
				return VETERAN;
			else if(0.77<=str && str <0.81)
				return MASTER;
			else if(0.82<=str && str <0.95)
				return HERO;
			else if(0.96<=str && str <0.9)
				return LEGEND;
			else if(str == 1)
				return ETERNAL;
			else
				return "unknwon";
		}
	
//	public static double getUserStrength(double str){
//		// Novice
//		if(str<=0.76)
//			return random(0,0.3);
//		else if(0.77<=str && str<0.84)
//			return random(0,0.6);
//		else if(0.85<=str && str<0.9)
//			return random(0,0.75);
//		else if(0.91<=str && str <0.94)
//			return random(0,0.9);
//		else if(0.95<=str && str <0.97)
//			return random(0,1);
//		else if(0.98<=str && str <0.9)
//			return random(0.2,1);
//		else if(str == 1)
//			return random(0.5,1);
//		else
//			return -1;
//	}
	
//	
//	public static String getStringUserStrength(double str) {
//		// Novice
//			if(str<=0.76)
//				return NOVICE;
//			else if(0.77<=str && str<0.84)
//				return APPRENTICE;
//			else if(0.85<=str && str<0.9)
//				return VETERAN;
//			else if(0.91<=str && str <0.94)
//				return MASTER;
//			else if(0.95<=str && str <0.97)
//				return HERO;
//			else if(0.98<=str && str <0.9)
//				return LEGEND;
//			else if(str == 1)
//				return ETERNAL;
//			else
//				return "unknwon";
//		}
//	
	
	
	private static double random(double minValue, double maxValue) {
		double min = minValue *100;
		double max = (maxValue-0.01) *100;
		double r = min + (double)(Math.random() * ((max - min) + 1));
		r = Math.round(r);
		return r/100;
	}

	public static int getBonus(int bonus, double difficulty) {
		if (bonus == GameConstants.BONUS_NEGATIVE)
			return bonus;
		double diff = getLinkDifficulty(difficulty);			
		return (int) (bonus*diff);
	}

	public static String getStringLinkDifficulty(double difficulty) {
		if(difficulty==0)
			return UNKNWON;
		else if(0<difficulty && difficulty <0.2)
			return EASY;
		else if(0.2<=difficulty && difficulty <0.5)
			return MEDIUM;
		else if(0.5<=difficulty && difficulty <0.7)
			return HARD;
		else if(0.7<=difficulty && difficulty <0.9)
			return VERY_HARD;
		else if(0.9<=difficulty && difficulty <1.0)
			return ULTIMATE;
		else
			return "difficultyError";

	}


}
