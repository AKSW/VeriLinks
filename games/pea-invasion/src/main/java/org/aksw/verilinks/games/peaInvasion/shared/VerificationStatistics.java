package org.aksw.verilinks.games.peaInvasion.shared;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.rpc.IsSerializable;

import org.aksw.verilinks.games.peaInvasion.client.PeaInvasion;

public class VerificationStatistics implements IsSerializable{

	private User user;
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
		this.user = new User();
	}
	
	/** Add Verification to lists*/
	public void addVerification(Verification ver){
		this.list.add(ver);
		this.completeList.add(ver);
	}

	/** Add Verification Evaluation to latest added verification*/
	public void addEvaluation(int bonus, boolean notSure){
		System.out.println("##VerificationStats: Add Eval. Bonus = "+bonus+" , notSure = "+notSure);
		if (bonus==GameConstants.BONUS_AGREE || bonus==GameConstants.BONUS_POSITIVE){
			countAgreed++;
			countAgreedComplete++;
			System.out.println("##VerificationStats: agree");
			
		}else if ((bonus==GameConstants.BONUS_NONE) && (notSure==false)){
			countDisagreed++;
			countDisagreedComplete++;
			System.out.println("##VerificationStats: disagree");
		}
		else if (bonus==GameConstants.BONUS_NEGATIVE){
			countPenalty++;
			countPenaltyComplete++;
			System.out.println("##VerificationStats: penalty");
		}
		else if ((bonus==GameConstants.BONUS_NONE) && (notSure==true)){
			countUnsure++;
			countUnsureComplete++;
			System.out.println("##VerificationStats: unsure");
		}
//		for (int i=0;i<list.size();i++)
//			System.out.println(list.get(i).getId()+" , "+list.get(i).getSelection());
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

	public String getVerifiedLinks() {
		ArrayList<Integer> unique = new ArrayList<Integer>();
		String verified = "";
		for(int i=0;i<this.list.size();i++){
			if(!unique.contains(list.get(i).getId()))
				unique.add(list.get(i).getId());
		}
		for(int i=0;i<unique.size();i++){
			verified+=unique.get(i);
			if(i!=unique.size()-1)
				verified+="+";
		}
		return verified;
	}
	
	public String getJson(){
		
		JSONObject jUser = new JSONObject();
		jUser.put("id", new JSONString(user.getId()));
		jUser.put("name",  new JSONString(user.getName()));
		
		JSONArray jVeriArr = new JSONArray();
		JSONObject jV = null;
		
		for(int i =0;i<list.size();i++){
			jV = new JSONObject();
			jV.put("id", new JSONNumber(list.get(i).getId()));
			jV.put("veri", new JSONNumber(list.get(i).getSelection()));
			jVeriArr.set(i,jV);
		}
		
		JSONObject jData = new JSONObject();
		jData.put("user", jUser);
		jData.put("verification", jVeriArr);
		
		return jData.toString();
	}

	public void setUser(User u) {
		this.user=u;
	}
}
