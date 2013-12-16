package com.crossword.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Iterator;
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
import com.crossword.data.GridforSaved;
import com.crossword.data.Word;


public class JsonUtil {

	//private String  jsonData;//���Դ洢�������json����
	private Context context;
	private JSONObject jObj;//���Խ����ʹ洢��JSONObject����
	private DBManager dbManager;//���ݿ�����Ķ���
	public JsonUtil(Context context){
		
		this.context = context;
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
	
	
	
	public String readJsonFromUrl(String url){
		
		BufferedReader bufferedReader = null;
		StringBuilder sb  = new StringBuilder();
		String s = "";
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
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	//��ȡjson�ļ���ת����String�����ٽ���
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
		}
		return grid;
	}
	

	

	//����ǰGrid��Ϣд��jsonData�У������û�����д��Ϣ�Լ�����
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
			//��ȡgrid�е�word��Ϣ
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
			e.printStackTrace();
		}
		
		return jObj;
	}
	
	
	
}




























