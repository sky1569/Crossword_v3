package com.crossword.data;

import java.util.ArrayList;
import java.util.LinkedList;

public class Character {
	
	private String       cap;
	private String       chi;
	private int          x;
	private int          y;
	private int          i;//标志这个字在哪个词中
	private int          j;//标志这个字在对应词中的第几个位置
	private String       temp;
	private ArrayList<ArrayList<Integer>> indexList = new ArrayList<ArrayList<Integer>>();//字的索引列表 
	
	//设置拼音
	public void setCap(String cap){
		this.cap = cap;
	}
	
	//获得拼音
	public String getCap(){
		return this.cap;
	}
	
	
	//设置中文
	public void setChi(String chi){
		this.chi = chi;
	}
	
	//获取中文
	public String getChi(){
		return this.chi;
	}
	
    //设置横坐标
	
	public void setX(int x){
		this.x = x ;
	}
	
	//获取横坐标
	public int getX(){
		return this.x;
	}
	
	
	public void setI(int i){
		this.i = i;
	}
	
	public int getI(){
		return this.i;
	}
	
	public void setJ(int j){
		this.j = j;
	}
	
	public int getJ(){
		return this.j;
	}
	
	
	
	
    //设置横坐标
	
	public void setY(int y){
		this.y = y ;
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
	public boolean isExistInCharacters(LinkedList<Character> characters){
		
		if(characters.isEmpty())//判断该词的字组是否为空
			return false;
		
        for(Character c:characters)

        	if(c.getX() == this.getX() && c.getY() == this.getY())
        	{
        		c.updateIndexList(this.getI(), this.getJ());
        		if(!this.getCap().contains(c.getCap())){
        		StringBuffer capBuffer = new StringBuffer(c.getCap());
        		capBuffer.append(this.getCap());
        		c.setCap(capBuffer.toString());
        		}
        		return true;
        	}
        
        return false;
	}
	
	

	//更新字的索引列表
	public void updateIndexList(int i,int j){
		ArrayList<Integer> indexs = new ArrayList<Integer>();

        indexs.add(i);
        indexs.add(j);
		this.indexList.add(indexs);
	}
	
	//获取字的索引列表
	public ArrayList<ArrayList<Integer>> getIndexList(){
		
		return this.indexList;
	}
	

	

	


}
