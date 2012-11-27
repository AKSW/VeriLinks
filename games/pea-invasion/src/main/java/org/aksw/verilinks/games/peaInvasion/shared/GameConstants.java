package org.aksw.verilinks.games.peaInvasion.shared;

public class GameConstants {

  public static final String MODE_EASY ="easy";
  public static final String MODE_MEDIUM ="medium";
  public static final String MODE_HARD ="hard";
  
	public static final int TRUE = 1;
	public static final int FALSE = 0;
	public static final int UNSURE = -1;
//  
//  public static final int VALID = 1;
//  public static final int NOT_VALID = 0;
//  public static final int NOT_SURE = 2;
  
  public static final int COST_PEA =10;
  public static final int COST_PEA_BIG =25;
  
  public static final int REWARD_ENEMY_PEA =25;
  public static final int REWARD_ENEMY_SHOOTER =25;
  public static final int REWARD_ENEMY_BOSS =25;
  
  	public static final int BONUS_POSITIVE = 100;
	public static final int BONUS_NEGATIVE = -400;
	public static final int BONUS_DISAGREE = 0;
	public static final int BONUS_AGREE = 20;
	public static final int BONUS_FIRST = 5;
	public static final int BONUS_UNSURE = 0;
	
	public static final int BONUS_HUGE = 300;
	public static final int BONUS_MEDIUM = 100;
	public static final int BONUS_NONE = 0;
	
	public static final double EVAL_POSITIVE = 1;
	public static final double EVAL_UNSURE = 0;
	public static final double EVAL_NEGATIVE = -1;
	public static final double EVAL_FIRST = -2;
	public static final double EVAL_ERROR = -1111;
	public static final double EVAL_THRESHOLD = 0.3;
	
}
