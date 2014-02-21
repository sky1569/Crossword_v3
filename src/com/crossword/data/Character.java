package com.crossword.data;

import java.util.ArrayList;
import java.util.LinkedList;

public class Character {
	
	private String       cap;
	private String       chi;
	private int          x;
	private int          y;
	private String       temp;
	private ArrayList<Integer> indexList = new ArrayList<Integer>();//�ֵ������б�
	
	//����ƴ��
	public void setCap(String cap){
		this.cap = cap;
	}
	
	//���ƴ��
	public String getCap(){
		return this.cap;
	}
	
	
	//����������
	public void setChi(String chi){
		this.chi = chi;
	}
	
	//��ȡ����
	public String getChi(){
		return this.chi;
	}
	
    //���ú�����
	
	public void setX(int x){
		this.x = x;
	}
	
	//��ȡ������
	public int getX(){
		return this.x;
	}
	
	
	
    //���ú�����
	
	public void setY(int y){
		this.y = y;
	}
	
	//��ȡ������
	public int getY(){
		return this.y;
	}
	
	
	//����temp��Ϣ
	public void setTemp(String temp){
		this.temp = temp;
	}
	//��ȡtemp��Ϣ
	public String getTemp(){
		return this.temp;
	}

	//��ȡĳ�����Ƿ���ĳ������
	public boolean isInWord(Word word){
		
		ArrayList<Integer> indexs = new ArrayList<Integer>();
		LinkedList<Character> entities  = word.getEntities();
		if(entities.isEmpty()){//�жϸôʵ������Ƿ�Ϊ��
			return false;
		}
        for(Character c:entities){
        	if(c.getX() == this.getX() && c.getY() == this.getY()){
        		c.updateIndexList(word.getIndex());
        		return true;
        	}
        }
        
        return false;
	}
	
	

	//�����ֵ������б�
	public void updateIndexList(int index){
		
		this.indexList.add(index);
	}
	
	//��ȡ�ֵ������б�
	public ArrayList<Integer> getIndexList(){
		
		return this.indexList;
	}
	
	

	


}
