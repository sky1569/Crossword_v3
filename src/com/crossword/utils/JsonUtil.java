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

import com.crossword.Crossword;
import com.crossword.data.Grid;
import com.crossword.data.Rank;
import com.crossword.data.Vol;
import com.crossword.data.Word;
import com.crossword.data.Character;

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
			if(entity != null)
			{
				bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(),"UTF-8"));
				while((s = bufferedReader.readLine()) != null){
					sb.append(s);
				}				
			}
				
		}catch(Exception e){
			
			
		//	Log.v("hh2", "sd");
			e.printStackTrace();
			//return s = "wrong";
			return null;
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
		LinkedList<Word> entities = new LinkedList<Word>();
		JSONObject jsonObject;
		JSONArray  jsonWordArray;
		try {
		    jsonObject = new JSONObject(jsonData);
		    //解析JSON数据中包含的所有的Word信息
		    jsonWordArray = jsonObject.getJSONArray("words");
		    for(int i = 0;i < jsonWordArray.length();i++){
				
		    	Word entity = new Word();
		    	JSONObject jObj = jsonWordArray.getJSONObject(i);
		    	entity.setDesc(jObj.getString("desc"));
		    	entity.setDesc2(jObj.getString("desc2"));
		    	//entity.setTmp(jObj.getString("tmp"));
		    	//entity.setHoriz(jObj.getInt("horiz") == 1?true:false);
		    	//entity.setX(jObj.getInt("x"));
		    	//entity.setY(jObj.getInt("y"));
		    	entity.setLength(jObj.getInt("len"));
		    	entity.setWordSign(jObj.getInt("sign"));
		    	//解析每个word中的所有Word并组成链表
		    	JSONArray jsonCharacterArray = jObj.getJSONArray("info");
		    	
		    	LinkedList<Character> characterEntities = new LinkedList<Character>();
		    	for(int j = 0;j <jsonCharacterArray.length();j++){
		    		Character character = new Character();
		    		JSONObject jCharacterObj = jsonCharacterArray.getJSONObject(j);
		    		character.setCap(jCharacterObj.getString("cap"));
		    		character.setChi(jCharacterObj.getString("chi"));
		    		character.setX(jCharacterObj.getInt("x"));
		    		character.setY(jCharacterObj.getInt("y"));
		    		character.setTemp(jCharacterObj.getString("temp"));
		    		character.updateIndexList(entity.getWordSign());
		    		
		    		for(Word w:entities){//遍历已有的所有词，判断当前解析的字是否已经存在，并在isInWord更新字的索引
		    			
		    			if(character.isInWord(w)){	    				
		    				continue;
		    			}else{//如果不是，就把当前解析的字加入字组中
		    				characterEntities.add(character);
		    			}
		    		}
		    		
		    	}
		    	entity.setEntities(characterEntities);
		    	//entity.setCap(jObj.getString("cap"));
		    	//entity.setChi(jObj.getString("chi"));
		    	//entity.setMask(jObj.getString("mask"));
		    	entities.add(entity);	
			}
		    grid.setEntities(entities);
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
			Log.v("读入直播错误", "11");
			e.printStackTrace();
		}
		return vol;
	}
	

	public LinkedList<Vol> parseVolJson(String jsonData){
		
		LinkedList<Vol> entities = new LinkedList<Vol>();
		try {
			 Log.v(" parseVolJson", "执行了parseVolJson");
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
			LinkedList<Word> entities = grid.getEntities();
			for(Word entity:entities){
			   
				JSONObject jObjj = new JSONObject();
				jObjj.put("desc", entity.getDesc());
				jObjj.put("tmp", entity.getTmp());
				jObjj.put("horiz", entity.getHoriz() == true?1:0);
				jObjj.put("x", entity.getX());
				jObjj.put("y", entity.getY());
				jObjj.put("len", entity.getLength());
				jObjj.put("chi", entity.getChi());
				jObjj.put("cap", entity.getCap());
				jObjj.put("mask", entity.getMask());
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





























