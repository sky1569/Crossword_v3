package com.crossword.utils;

import android.os.Handler;
import android.os.Message;

public class HandleMessage {
		
		Handler handler = new Handler() {
		public void handleMessage(Message msg,Module m) {
		//要做的事情
	      if(msg.what==1)
	      {
	    	 // m.setDisValue(x, y, value);
	    	  
	    	  
	      }
		}
		};
}
