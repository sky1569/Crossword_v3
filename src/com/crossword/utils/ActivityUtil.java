package com.crossword.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;




public class ActivityUtil {
	public static Context packageContext;
	public static Class    className;
	public static Bundle   bundle;
	public static void startActivity(Context context,Class cls,Bundle bundle){//带参数传递的Activity切换
		
		Intent intent = new Intent();
		intent.setClass(context, cls);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}
	
	
	public static void startActivity(Context context,Class cls){//不带参数传递的Activity切换
		
		Intent intent = new Intent();
		intent.setClass(context, cls);
		context.startActivity(intent);
		
	}
	
	
	
	
	
	public static void holdOnActivity(Class cls,Bundle bundle){
		
		ActivityUtil.className = cls;
		ActivityUtil.bundle = bundle;
		
	}
	
	public static void startHoldOnActivity(Context context){
		
	    ActivityUtil.startActivity(context,ActivityUtil.className,ActivityUtil.bundle);
	}
	
	
	

	
	

}
