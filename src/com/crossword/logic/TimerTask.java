package com.crossword.logic;

import android.os.Handler;
import android.os.Message;

public class TimerTask implements Runnable
{
  private Handler handler;
  public void run()
  {
	  try {
			  Thread.sleep(10000);//�߳���ͣ10�룬��λ����
			  Message message=new Message();
			  message.what=1;
			  handler.sendMessage(message);//������Ϣ
		  } 
	  catch (InterruptedException e)
	      {
		  // TODO Auto-generated catch block
		  	e.printStackTrace();
		  }
		 
		  
  }
}
