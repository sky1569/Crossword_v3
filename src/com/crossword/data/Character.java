package com.crossword.data;

import java.util.ArrayList;
import java.util.LinkedList;

public class Character {
	
	private String       cap;
	private String       chi;
	private int          x;
	private int          y;
	private String       temp;
	private ArrayList<Integer> indexList = new ArrayList<Integer>();//字的索引列表
	
	//设置拼音
	public void setCap(String cap){
		this.cap = cap;
	}
	
	//获得拼音
	public String getCap(){
		return this.cap;
	}
	
	
	//设置中午呢
	public void setChi(String chi){
		this.chi = chi;
	}
	
	//获取中文
	public String getChi(){
		return this.chi;
	}
	
    //设置横坐标
	
	public void setX(int x){
		this.x = x;
	}
	
	//获取横坐标
	public int getX(){
		return this.x;
	}
	
	
	
    //设置横坐标
	
	public void setY(int y){
		this.y = y;
	}
	
	//获取横坐标
	public int getY(){
		return this.y;
	}
	
	
	//设置temp信息
	public void setTemp(String temp){
		this.temp = temp;
	}
	//获取temp信息
	public String getTemp(){
		return this.temp;
	}

	//获取某个字是否在某个词中
	public boolean isInWord(Word word){
		
		ArrayList<Integer> indexs = new ArrayList<Integer>();
		LinkedList<Character> entities  = word.getEntities();
		if(entities.isEmpty()){//判断该词的字组是否为空
			return false;
		}
        for(Character c:entities){
        	if(c.getX() == this.getX() && c.getY() == this.getY()){
        		c.updateIndexList(word.getIndex());
        		return true;
        	}
        }
        
        return false;
	}
	
	

	//更新字的索引列表
	public void updateIndexList(int index){
		
		this.indexList.add(index);
	}
	
	//获取字的索引列表
	public ArrayList<Integer> getIndexList(){
		
		return this.indexList;
	}
	
	

	


}
