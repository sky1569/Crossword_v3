package com.crossword.logic;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class GameLogic extends Activity
{
	public void ConTest(Activity act)
	{
	ConnectivityManager cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE); 
	NetworkInfo info = cwjManager.getActiveNetworkInfo(); 
		if (info != null && info.isAvailable()){ 
		//do nothing 
		} 
		else
		{
		
				Toast.makeText(act,"����������", Toast.LENGTH_SHORT).show();
		}
	}
}
//Ҫ��Ȩ��������<uses-permission android:name=\"android.permission.ACCESS_NETWORK_STATE\"/>