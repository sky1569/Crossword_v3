package com.ming.crossword.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.crossword.data.Grid;
import com.crossword.data.Word;


public class JsonUtil {

	private String  jsonData;
	private Context context;
	public JsonUtil(Context context){
		
		this.context = context;
	}
	
	
	public String readJsonDataFromAssets(String filename){
		
		
		String res = "";
		try{
		InputStream in = this.context.getResources().getAssets().open(filename);
		//int length = in.available();
		ByteArrayBuffer bb = new ByteArrayBuffer(in.available());
		int current = 0;
		while((current = in.read())!=-1){
			bb.append(current);
		}
		//byte[] buffer = new byte[length];
		//in.read(buffer);
	    res = EncodingUtils.getString(bb.toByteArray(), "UTF-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		return res;
		/*InputStream in = null;
		InputStreamReader inputStreamReader = null;
		try{
			in = this.context.getResources().getAssets().open(filename);
			inputStreamReader = new InputStreamReader(in,"utf-8");
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}catch(Exception e1){
			e1.printStackTrace();
		}
		
		BufferedReader reader = new BufferedReader(inputStreamReader);
		StringBuffer sb = new StringBuffer("");
		String line;
		
		try{
			while((line = reader.readLine()) != null){
				sb.append(line);
				sb.append("\n");
			}
		}catch(IOException e){
			e.printStackTrace();
		}*/
		//return sb.toString();
	}
	
	
	public Grid parseGridJson(String fileName){
		
		Grid grid = new Grid();
		LinkedList<Word> entries = new LinkedList<Word>();
		String res = readJsonDataFromAssets(fileName);
		JSONObject jsonObject;
		JSONArray  jsonArray;
		try {
		    jsonObject = new JSONObject(res);
		    jsonArray = jsonObject.getJSONArray("words");
		    for(int i = 0;i < jsonArray.length();i++){
				
		    	Word entry = new Word();
		    	JSONObject jObj = jsonArray.getJSONObject(i);
		    	entry.setDesc(jObj.getString("desc"));
		    	entry.setTmp(jObj.getString("tmp"));
		    	entry.setHoriz(jObj.getInt("horiz") == 1?true:false);
		    	entry.setX(jObj.getInt("x"));
		    	entry.setY(jObj.getInt("y"));
		    	entry.setLength(jObj.getInt("len"));
		    	entry.setCap(jObj.getString("cap"));
		    	entry.setChi(jObj.getString("chi"));
		    	entry.setMask(jObj.getString("mask"));
		    	entries.add(entry);
		    	//String desc = jsonArray.getJSONObject(i).getString("desp");	    	
			}
		    grid.setEntries(entries);
		    grid.setFilename(jsonObject.getString("file"));
		    grid.setUniqueid(jsonObject.getInt("uniqueid"));
		    grid.setVol(jsonObject.getInt("vol"));
		    grid.setLevel(jsonObject.getInt("level"));
		    grid.setCategory(jsonObject.getString("category"));
		    grid.setScore(jsonObject.getInt("score"));
		    grid.setDate(jsonObject.getString("date"));
		    grid.setGamename(jsonObject.getString("gamename"));
		    grid.setAuthor(jsonObject.getString("author"));
		    grid.setWidth(jsonObject.getInt("width"));
		    grid.setHeight(jsonObject.getInt("height"));
		    
		    
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//½âÎöJSONÊý×é
		
		return grid;
	}
	
	
	
	
	
}





























