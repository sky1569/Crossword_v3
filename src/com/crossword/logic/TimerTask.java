package com.crossword.logic;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class TimerTask implements Runnable
{
  private Handler handler;
  private String position;
  public TimerTask(String position,Handler handler)
  {
	  this.position = position;
	  this.handler = handler;
  }
  public void run()
  {
	  try {
			  Thread.sleep(5000);//线程暂停10秒，单位毫秒
			//  Log.v("testtime5", "5");
			  Thread.sleep(5000);//线程暂停10秒，单位毫秒
			//  Log.v("testtime5", "10");
			  Message message=new Message();
			  message.what = 0x123;
			  message.obj =this.position ;
			  this.handler.sendMessage(message);//发送消息
		  } 
	  catch (InterruptedException e)
	      {
		  // TODO Auto-generated catch block
		  	e.printStackTrace();
		  }
		 
		  
  }
}
