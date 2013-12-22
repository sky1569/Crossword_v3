package com.crossword.utils;

import com.crossword.Crossword;

public class UserUtil {
	
	private String    account;
	private String    password;
	private String    username;
	private JsonUtil  jsonUtil;
	public static  boolean   loginStatus;//±íÊ¾µÇÂ¼µÄ×´Ì¬
	
	public UserUtil(){
		
		jsonUtil = new JsonUtil();
	}
	
	public void    setAccount(String account){this.account = account;}
	public String  getAccount(){return this.account;}
	
	
	public void    setPassword(String password){this.password = password;}
	public String  getPassword(){return this.password;}
	
	
	public void    setUsername(String username){this.username = username;}
	public String  getUsername(){return this.username;}
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
	

}
