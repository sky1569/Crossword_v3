package com.crossword.utils;

import java.util.LinkedList;

import com.crossword.Crossword;
import com.crossword.data.Rank;

public class UserUtil {
	
	private String    account;
	private String    password;
	private String    username;
	public  static   String   currAccount;	//��ǰ��¼���û�
	public static    int      myScore;
	public  static    int      myRank;

	private JsonUtil  jsonUtil;
	public static  int   loginStatus = -1;//��ʾ��¼��״̬,1��ʾ�ѵ�¼��0��ʾ�ο͵�¼��-1��ʾδ��¼
	
	public UserUtil(){
		
		jsonUtil = new JsonUtil();
	}
	
	
	
	
	public void    setAccount(String account){this.account = account;}
	public String  getAccount(){return this.account;}
	
	
	public void    setPassword(String password){this.password = password;}
	public String  getPassword(){return this.password;}
	
	
	public void    setUsername(String username){this.username = username;}
	public String  getUsername(){return this.username;}
	
	//public void    setUserscore() 
	
	
	public String  login(){
		
		String loginUrl = Crossword.LOGIN_ROOT_URL + "id=" + this.account + "&" + "pw=" + this.password;
		loginUrl.replace(" ", "%20");
		String verify = jsonUtil.readJsonFromUrl(loginUrl);
		return verify;
	}

	
	public String register(){
		String registerUrl = Crossword.REGISTER_ROOT_URL + "id=" + this.account + "&"  + 
	                                                       "pw=" + this.password + "&"+
	                                                       "name=" + this.username;
		registerUrl.replace(" ", "%20");
		String verify = jsonUtil.readJsonFromUrl(registerUrl);
		return verify;
	}
	
	
	public String uploadGridScore(int uniqueid,int score){
		
		String uploadScoreUrl = Crossword.UPLOAD_SCORE_ROOT_URL + "user=" + currAccount +"&"
				                                                + "score=" + score+"&" 
				                                                + "id=" + uniqueid;
		String verify = jsonUtil.readJsonFromUrl(uploadScoreUrl);
		
		return verify;
		
	}

	
	public String uploadOfflineScore(int score){
		String uploadOfflineScore = Crossword.UPLOAD_OFFLINE_ROOT_SCORE + "user=" + currAccount + "&"
				                                                          +"score=" + score;
		String verify = jsonUtil.readJsonFromUrl(uploadOfflineScore);
		
		return verify;
	}
	
	
	
	public LinkedList<Rank> getRank(int volNumber){
		
		String rankUrl = Crossword.RANK_ROOT_URL + "user=" + currAccount + "&" + "vol=" + volNumber;
		String jsonData = jsonUtil.readJsonFromUrl(rankUrl);
		return jsonUtil.parseRankJson(jsonData);
	}
	
	
	public LinkedList<Rank> getRank(){
		String rankUrl = Crossword.OFFLINE_RANK_ROOT_URL + "user=" + currAccount;
		String jsonData = jsonUtil.readJsonFromUrl(rankUrl);
		return jsonUtil.parseRankJson(jsonData);
	}
	
	
	
	
	public void setMyScore(int score){this.myScore = score;}
	public int  getMyScore(){return this.myScore;}
	
	

}
