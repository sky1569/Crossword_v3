package com.crossword.data;

public class Description {

	private String desc1;//一级提示
	private String desc2;//二级提示
	private int    to;//标记该描述属于第几个词
	
	
	public void setDesc1(String desc1){this.desc1 = desc1;}
	public String getDesc1(){return this.desc1;}
	
	public void setDesc2(String desc2){this.desc2 = desc2;}
	public String getDesc2(){return this.desc2;}
	
	public void setTo(int to){this.to = to;}
	public int getTo(){return this.to;}

	
	}
