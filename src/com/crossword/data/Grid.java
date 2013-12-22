/*
 * Copyright 2011 Alexis Lauper <alexis.lauper@gmail.com>
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of 
 * the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.crossword.data;

import java.io.Serializable;
import java.util.LinkedList;

import com.crossword.Crossword;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Data class for grid meta information
 * 
 * @author alex
 */
public class Grid implements Comparable<Grid> ,Serializable{
	private String	      fileName;
	private Integer       uniqueid;
	private Integer       vol;
	private Integer       level;
	private Integer       degree;
	private String        category;
	private LinkedList<Word> entries;   
	private Integer          score;
	private String           date;
	private String           gamename;
	private String           author;
	private Integer          width;
	private Integer          height;
	private Integer          islocked;
	private Integer			 star;
	private String           jsonData;
	private Integer			 gameMode;
	
	
	public Grid() {
	}
	
	public void 	   setGameMode(int value) {this.gameMode =value;}
	public Integer	   getGameMode()	{return this.gameMode;}
	
	public void        setFilename(String fileName){this.fileName = fileName;}
	public String      getFilename(){return this.fileName;}
	
	public void        setUniqueid(Integer uniqueid){this.uniqueid = uniqueid;}
	public Integer     getUniqueid(){return this.uniqueid;}
	
	public void        setVol(Integer vol){this.vol = vol;}
    public Integer     getVol(){return this.vol;}
    
    public void        setLevel(Integer level){this.level = level;}
    public Integer     getLevel(){return this.level;}
    
    public void        setDegree(Integer degree){this.degree = degree;}
    public Integer     getDegree(){return this.degree;}
    
    public void        setCategory(String category){this.category = category;}
    public String      getCategory(){return this.category;}
    
    public void        setEntries(LinkedList<Word> entries){this.entries = entries;}
    public LinkedList<Word>  getEntries(){return this.entries;}
    
    public void        setScore(Integer score){this.score = score;}
    public Integer     getScore(){return this.score;}
    
    public void        setDate(String date){this.date = date;}
    public String      getDate(){return this.date;}
    
    public void        setGamename(String gamename){this.gamename = gamename;}
    public String      getGamename(){return this.gamename;}
    
    public void        setAuthor(String author){this.author = author;}
    public String      getAunthor(){return this.author;}
    
    public void        setWidth(Integer width){this.width = width;}
    public Integer     getWidth(){return this.width;}
    
    public void        setHeight(Integer height){this.height = height;}
    public Integer     getHeight(){return this.height;}


    public void        setJsonData(String jsonData){this.jsonData = jsonData;}
    public String      getJsonData(){return this.jsonData;}
    
    public void 	   setIslocked(Integer islocked){this.islocked = islocked;}
    public Integer 		   getIslocked(){return this.islocked;}  
    
    public void 	   setStar(Integer star) {this.star = star;}
    public Integer	   getStar(){return this.star;} 
	@Override
	public int compareTo(Grid arg0) {
		// TODO Auto-generated method stub
		return 0;
	}


//	@Override
	//public int describeContents() {
		// TODO Auto-generated method stub
//		return 0;
//	}


	//@Override
	//public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
//	}
   
}
