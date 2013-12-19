package com.crossword.data;

public class Vol {

	private String name ;
	private String openDate;
	private int    amountOfLevels;
	private int    volNumber;
	private int    score = 0;
	
	public void    setVolName(String name){this.name = name;}
	public String  getVolName(){return this.name;}
	
	public void    setOpenDate(String date){this.openDate = date;}
	public String  getOpenDate(){return this.openDate;}
	
	public void    setAmountOfLevels(int levels){this.amountOfLevels = levels;}
	public int     getAmountOfLevels(){return this.amountOfLevels;}
	
	public void    setVolNumber(int volNumber){this.volNumber = volNumber;}
	public int     getVolNumber(){return this.volNumber;}
	
	public void    setScore(int score){this.score = score;}
	public int     getScore(){return this.score;}
}
