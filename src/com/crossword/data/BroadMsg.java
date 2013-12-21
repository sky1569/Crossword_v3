package com.crossword.data;

import java.io.Serializable;

public class BroadMsg implements Serializable{

	private boolean  isbroad ;
	private int[]   unlockNumber;
	private int   volNumber;
	private int amountOfLevels;
	
	public void setIsbroad ( String value)	{this.isbroad = value.equals("YES")? true : false;}
	public boolean getIsbroad(){return this.isbroad;}
	
	public void setUnlockNumber (int[] value){this.unlockNumber = value; this.setAmountOfLevels(unlockNumber.length); }
	public int[] getUnlockNumber () 	{return this.unlockNumber;}
	
	public void setVolNumber(int value) {this.volNumber = value;}
	public int getVolNumber()			{return this.volNumber;}
	
	public void    setAmountOfLevels(int levels){this.amountOfLevels = levels;}
	public Integer getAmountOfLevels(){return this.amountOfLevels;}
}
