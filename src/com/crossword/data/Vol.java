package com.crossword.data;

import java.io.Serializable;

public class Vol implements Serializable{

	public static final long serialVersionUID = -7060210544600464481L;
	private String name ;
	private String openDate;
	private int    amountOfLevels;
	private int    volNumber;
	private int    score = 0;
	private boolean isbroad =false;
//	private int[]    unlockNumber;
	private int 	curLevel;
	
	public void    setVolName(String name){this.name = name;}
	public String  getVolName(){return this.name;}
	
	public void    setOpenDate(String date){this.openDate = date;}
	public String  getOpenDate(){return this.openDate;}
	
	public void    setAmountOfLevels(int levels){this.amountOfLevels = levels;}
	public Integer getAmountOfLevels(){return this.amountOfLevels;}
	
	public void    setVolNumber(int volNumber){this.volNumber = volNumber;}
	public Integer getVolNumber(){return this.volNumber;}
	
	public void    setScore(int score){this.score = score;}
	public Integer getScore(){return this.score;}
	
	public void setIsbroad ( String value)	{this.isbroad = value.equals("YES")? true : false;}
	public boolean getIsbroad(){return this.isbroad;}
	
	public void  setCurLevel( int value) {this.curLevel =value;}
	public int   getCurLevel(){ return this.curLevel;}
	
//	public void setUnlockNumber (int[] value){this.unlockNumber = value; this.setAmountOfLevels(unlockNumber.length); }
	//public int[] getUnlockNumber () 	{return this.unlockNumber;}
}
