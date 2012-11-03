package org.aksw.verilinks.games.peaInvasion.shared;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import org.aksw.verilinks.games.peaInvasion.client.PeaInvasion;

public class VerificationStatistics implements IsSerializable{

	private ArrayList<Verification> list;
	private ArrayList<Verification> completeList;
	private int countAgreed;
	private int countDisagreed;
	private int countPenalty;
	private int countAgreedComplete;
	private int countDisagreedComplete;
	private int countPenaltyComplete;
	private int countUnsure;
	private int countUnsureComplete;
	
	public VerificationStatistics(){
		list = new ArrayList<Verification>();
		completeList = new ArrayList<Verification>();
		countAgreed=0;
		countDisagreed=0;
		countPenalty = 0;
		countAgreedComplete=0;
		countDisagreedComplete=0;
		countPenaltyComplete = 0;
		countUnsure=0;
		countUnsureComplete=0;
	}
	
	/** Add Verification to lists*/
	public void addVerification(Verification ver){
		this.list.add(ver);
		this.completeList.add(ver);
	}

	/** Add Verification Evaluation to latest added verification*/
	public void addEvaluation(int bonus, boolean notSure){
		System.out.println("##VerificationStats: Add Eval. Bonus = "+bonus+" , notSure = "+notSure);
		if (bonus==GameConstants.BONUS_MEDIUM || bonus==GameConstants.BONUS_HUGE){
			countAgreed++;
			countAgreedComplete++;
		}else if ((bonus==GameConstants.BONUS_NONE) && (notSure==false)){
			countDisagreed++;
			countDisagreedComplete++;
		}
		else if (bonus==GameConstants.BONUS_PENALTY){
			countPenalty++;
			countPenaltyComplete++;
		}
		else if ((bonus==GameConstants.BONUS_NONE) && (notSure==true)){
			countUnsure++;
			countUnsureComplete++;
		}
		for (int i=0;i<list.size();i++)
			System.out.println(list.get(i).getId()+" , "+list.get(i).getSelection());
	}
	
	public void reset(){
		System.out.println("Before: VerifyStats list size: "+list.size()+ " , completeListSize: "+completeList.size());
		list=new ArrayList<Verification>();
		countAgreed=0;
		countDisagreed=0;
		countPenalty=0;
		countUnsure=0;
		System.out.println("After: VerifyStats list size: "+list.size()+ " , completeListSize: "+completeList.size());
		
	}
	
	public ArrayList<Verification> getList() {
		return this.list;
	}

	public ArrayList<Verification> getCompleteList() {
		return this.completeList;
	}
	
	public int getCountAgreed() {
		return countAgreed;
	}

	public int getCountDisagreed() {
		return countDisagreed;
	}

	public int getCountPenalty(){
		return this.countPenalty;
	}

	public int getCountAgreedComplete() {
		return countAgreedComplete;
	}

	public int getCountDisagreedComplete() {
		return countDisagreedComplete;
	}

	public int getCountPenaltyComplete() {
		return countPenaltyComplete;
	}

	public int getCountUnsure() {
		// TODO Auto-generated method stub
		return this.countUnsure;
	}
}
