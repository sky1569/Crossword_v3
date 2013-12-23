package com.crossword.utils;

import java.util.LinkedList;

import com.crossword.Crossword;
import com.crossword.data.Rank;

public class UserUtil {
	
	private String    account;
	private String    password;
	private String    username;
	public  static   String   currAccount;	//当前登录的用户
	public static    int      myScore;
	public  static    int      myRank;

	private JsonUtil  jsonUtil;
	public static  boolean   loginStatus;//表示登录的状态
	
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
	
	
	public String uploadScore(int uniqueid,int score){
		
		String uploadScoreUrl = Crossword.UPLOAD_SCORE_ROOT_URL + "user=" + currAccount +"&"
				                                                + "score=" + score+"&" 
				                                                + "id=" + uniqueid;
		String verify = jsonUtil.readJsonFromUrl(uploadScoreUrl);
		
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
