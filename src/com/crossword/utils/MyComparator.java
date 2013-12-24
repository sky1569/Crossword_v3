package com.crossword.utils;
import java.util.*;

import com.crossword.data.Vol;

public class MyComparator implements Comparator{

	@Override
	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		
		Vol v1 = (Vol)o1;
		Vol v2 = (Vol)o2;
		
		if(v1.getVolNumber() < v2.getVolNumber()){
			return -1;
		}else{
			return 1;
		}
	

	}

}
