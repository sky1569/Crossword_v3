package com.crossword.logic;

import android.os.Handler;
import android.os.Message;

public class TimerTask implements Runnable
{
  private Handler handler;
  public void run()
  {
	  try {
			  Thread.sleep(10000);//线程暂停10秒，单位毫秒
			  Message message=new Message();
			  message.what=1;
			  handler.sendMessage(message);//发送消息
		  } 
	  catch (InterruptedException e)
	      {
		  // TODO Auto-generated catch block
		  	e.printStackTrace();
		  }
		 
		  
  }
}
