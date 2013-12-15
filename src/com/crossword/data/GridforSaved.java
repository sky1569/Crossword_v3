package com.crossword.data;

public class GridforSaved extends Grid{

	//GridforSaved继承自Grid，并且增加所有json数据的成员，便于数据库的存储
	private String jsonData = "";
	
	public void setJsonData(String jsonData){
		this.jsonData = jsonData;
	}
	
	public String getJsonData(){return this.jsonData;}
}
