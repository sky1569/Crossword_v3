package com.crossword.utils;
import java.util.Comparator;

import com.crossword.data.Grid;
import com.crossword.data.Vol;

public class MyComparator implements Comparator{

	@Override
	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		if(o1 instanceof Vol){
		Vol v1 = (Vol)o1;
		Vol v2 = (Vol)o2;
		
		if(v1.getVolNumber() < v2.getVolNumber()){
			return -1;
		}else{
			return 1;
		}
		}
		else
		{
			Grid g1 =(Grid)o1;
			Grid g2 =(Grid)o2;
			
			if(g1.getLevel() < g2.getLevel()){
				return -1;
			}else{
				return 1;
			}
		}

	}

	
}
