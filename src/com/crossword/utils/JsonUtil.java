package com.crossword.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.crossword.data.Grid;
import com.crossword.data.Rank;
import com.crossword.data.Vol;
import com.crossword.data.Word;


public class JsonUtil {

	//private String  jsonData;//用以存储解析后的json数据
	private Context context;
	private JSONObject jObj;//用以解析和存储的JSONObject对象
	private DBManager dbManager;//数据库管理的对象
	public JsonUtil(Context context){
		
		this.context = context;
	}
	
	public JsonUtil(){
		
	}
	
	public String readJsonDataFromFile(String filename){
		String res = "";
		try{
		FileInputStream in = this.context.openFileInput(filename);
		ByteArrayBuffer bb = new ByteArrayBuffer(in.available());
		int current = 0;
		while((current = in.read())!=-1){
			bb.append(current);
		}
	    res = EncodingUtils.getString(bb.toByteArray(), "UTF-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		return res;

	}
	
/*	
	public String readJsonDataFromAssets(String filename){
		
				String res = "";
				try{
				//FileInputStream in = this.context.openFileInput(filename);
				InputStream in =  this.context.getAssets().open(filename);
	
				ByteArrayBuffer bb = new ByteArrayBuffer(in.available());
				int current = 0;
				while((current = in.read())!=-1){
					bb.append(current);
				}
			    res = EncodingUtils.getString(bb.toByteArray(), "UTF-8");
				}catch(Exception e){
					e.printStackTrace();
				}
				return res;


	}
	
	*/
	
	public String readJsonFromUrl(String url){
		
		BufferedReader bufferedReader = null;
		StringBuilder sb  = new StringBuilder();
		String s = "";
		//Log.v("hh1", "sd");
		try{
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(new HttpGet(url));
			HttpParams httpParams = client.getParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
			HttpConnectionParams.setSoTimeout(httpParams, 5000);
			HttpEntity entity = response.getEntity();
			if(entity != null){
				bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(),"UTF-8"));
				while((s = bufferedReader.readLine()) != null){
					sb.append(s);
				}
			}
		}catch(Exception e){
			
			
		//	Log.v("hh2", "sd");
			e.printStackTrace();
			//return s = "wrong";
		}
		String str = sb.toString();
		
		if(str != null && str.startsWith("\ufeff")){
			str = str.substring(1);
		}
		//Log.v("sb", str);
		return str;
	}
	//读取json文件，转换成String变量再解析
	public Grid parseGridJson(String jsonData){
		
		Grid grid = new Grid();
		LinkedList<Word> entries = new LinkedList<Word>();
		JSONObject jsonObject;
		JSONArray  jsonArray;
		try {
		    jsonObject = new JSONObject(jsonData);
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
			}
		    grid.setEntries(entries);
		    grid.setFilename(jsonObject.getString("file"));
		    grid.setUniqueid(jsonObject.getInt("uniqueid"));
		    grid.setVol(jsonObject.getInt("volNumber"));
		    grid.setLevel(jsonObject.getInt("level"));
		    grid.setDegree(jsonObject.getInt("degree"));
		    grid.setCategory(jsonObject.getString("category"));
		    grid.setIslocked(jsonObject.getInt("islocked"));
		    grid.setStar(jsonObject.getInt("star"));
		    grid.setScore(jsonObject.getInt("score"));
		    grid.setDate(jsonObject.getString("date"));
		    grid.setGamename(jsonObject.getString("gamename"));
		    grid.setAuthor(jsonObject.getString("author"));
		    grid.setWidth(jsonObject.getInt("width"));
		    grid.setHeight(jsonObject.getInt("height"));
		    grid.setJsonData(jsonData);
		    
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.v("解析异常", "sdsd");
		}
		return grid;
	}
	

	
	
	
	//获取排名信息
	public LinkedList<Rank> parseRankJson(String jsonData){
		LinkedList<Rank> rankedList = new LinkedList<Rank>();
		try {
			JSONObject jObj = new JSONObject(jsonData);
			JSONArray  jArr = jObj.getJSONArray("top");
			for(int i = 0;i<jArr.length();i++){
				
				JSONObject jObjj = jArr.getJSONObject(i);
				Rank rank = new Rank();
				rank.setUserId(jObjj.getString("ID"));
				rank.setAccumulatePoint(jObjj.getInt("SCORE"));
				rank.setRank(i+1);
				rankedList.add(rank);
			}
			int myRank = jObj.getInt("rank");
			UserUtil.myRank = myRank;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rankedList;
	}
	
	


	public Vol parseBroad(String jsonData)
	{
		JSONObject jsonObject;
		JSONArray  jsonArray;
		Vol vol =new Vol();
		
		try
		{
			 jsonObject = new JSONObject(jsonData);
			 vol.setIsbroad(jsonObject.getString("broad"));
			// vol.setVolName(jsonObject.getString("name"));
			 JSONObject jsonObj = jsonObject.getJSONObject("broad_vol");
			 vol.setVolName(jsonObj.getString("name"));
			 vol.setAmountOfLevels(jsonObj.getInt("amount_of_levels"));
			 vol.setCurLevel(jsonObj.getInt("cur_level"));
			 vol.setOpenDate(jsonObj.getString("open_date"));
			 vol.setVolNumber(jsonObj.getInt("vol_no"));
		}
		catch(Exception e)
		{
			Log.v("读入直博错误", "11");
			e.printStackTrace();
		}
		return vol;
	}
	

	public LinkedList<Vol> parseVolJson(String jsonData){
		
		LinkedList<Vol> entities = new LinkedList<Vol>();
		try {
			JSONArray  jArr = new JSONArray(jsonData);
			for(int i = 0;i < jArr.length();i++){
				JSONObject jObj = jArr.getJSONObject(i);
				Vol vol = new Vol();
				vol.setVolName(jObj.getString("name"));
				vol.setOpenDate(jObj.getString("open_date"));
				vol.setAmountOfLevels(jObj.getInt("amount_of_levels"));
				vol.setVolNumber(jObj.getInt("vol_no"));
				entities.add(vol);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return entities;
	}

	
	
	
	//将当前Grid信息写入jsonData中，包含用户的填写信息以及分数
	public JSONObject writeToJson(Grid grid){
		
		JSONObject jObj = new JSONObject();
		JSONArray  jArry = new JSONArray();
		try {
			jObj.put("file", grid.getFilename());
			jObj.put("uniqueid", grid.getUniqueid());
			jObj.put("volNumber", grid.getVol());
			jObj.put("level", grid.getLevel());
			jObj.put("degree", grid.getDegree());
			jObj.put("category", grid.getCategory());
			jObj.put("islocked", grid.getIslocked());
			jObj.put("star",grid.getStar());
			//获取grid中的word信息
			LinkedList<Word> entries = grid.getEntries();
			for(Word entry:entries){
			   
				JSONObject jObjj = new JSONObject();
				jObjj.put("desc", entry.getDesc());
				jObjj.put("tmp", entry.getTmp());
				jObjj.put("horiz", entry.getHoriz() == true?1:0);
				jObjj.put("x", entry.getX());
				jObjj.put("y", entry.getY());
				jObjj.put("len", entry.getLength());
				jObjj.put("chi", entry.getChi());
				jObjj.put("cap", entry.getCap());
				jObjj.put("mask", entry.getMask());
				jArry.put(jObjj);
			}
			jObj.put("words", jArry);
			jObj.put("score", grid.getScore());
			jObj.put("date", grid.getDate());
			jObj.put("gamename", grid.getGamename());
			jObj.put("author", grid.getAunthor());
			jObj.put("width", grid.getWidth());
			jObj.put("height", grid.getHeight());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.v("写入错误", ""+grid.getScore());
			e.printStackTrace();
		}
		
		return jObj;
	}
	
	
	
}





























