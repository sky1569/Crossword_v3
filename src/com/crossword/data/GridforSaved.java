package com.crossword.data;

public class GridforSaved extends Grid{

	//GridforSaved�̳���Grid��������������json���ݵĳ�Ա���������ݿ�Ĵ洢
	private String jsonData = "";
	
	public void setJsonData(String jsonData){
		this.jsonData = jsonData;
	}
	
	public String getJsonData(){return this.jsonData;}
}
