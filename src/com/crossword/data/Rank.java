package com.crossword.data;

public class Rank {

	private String userId;
	private int    accumulatePoint;
	private int    rank;
	public static int    myRank;

	public void     setUserId(String userId){this.userId = userId;}
	public String   getUserId(){return this.userId;}
	
	
	public void     setAccumulatePoint(int accumulatePoint){this.accumulatePoint = accumulatePoint;}
	public int      getAccumulatePoint(){return this.accumulatePoint;}
	
	public void     setRank(int rank){this.rank = rank;}
	public int      getRank(){return this.rank;}
	
}
