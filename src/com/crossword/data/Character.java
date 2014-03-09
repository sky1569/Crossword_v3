package com.crossword.data;

import java.util.ArrayList;
import java.util.LinkedList;

public class Character {
	
	private String       cap;
	private String       chi;
	private int          x;
	private int          y;
	private int          i;//��־��������ĸ�����
	private int          j;//��־������ڶ�Ӧ���еĵڼ���λ��
	private String       temp;
	private ArrayList<ArrayList<Integer>> indexList = new ArrayList<ArrayList<Integer>>();//�ֵ������б� 
	
	//����ƴ��
	public void setCap(String cap){
		this.cap = cap;
	}
	
	//���ƴ��
	public String getCap(){
		return this.cap;
	}
	
	
	//��������
	public void setChi(String chi){
		this.chi = chi;
	}
	
	//��ȡ����
	public String getChi(){
		return this.chi;
	}
	
    //���ú�����
	
	public void setX(int x){
		this.x = x ;
	}
	
	//��ȡ������
	public int getX(){
		return this.x;
	}
	
	
	public void setI(int i){
		this.i = i;
	}
	
	public int getI(){
		return this.i;
	}
	
	public void setJ(int j){
		this.j = j;
	}
	
	public int getJ(){
		return this.j;
	}
	
	
	
	
    //���ú�����
	
	public void setY(int y){
		this.y = y ;
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
	public boolean isExistInCharacters(LinkedList<Character> characters){
		
		if(characters.isEmpty())//�жϸôʵ������Ƿ�Ϊ��
			return false;
		
        for(Character c:characters)

        	if(c.getX() == this.getX() && c.getY() == this.getY())
        	{
        		c.updateIndexList(this.getI(), this.getJ());
        		if(!this.getCap().contains(c.getCap())){
        		StringBuffer capBuffer = new StringBuffer(c.getCap());
        		capBuffer.append(this.getCap());
        		c.setCap(capBuffer.toString());
        		}
        		return true;
        	}
        
        return false;
	}
	
	

	//�����ֵ������б�
	public void updateIndexList(int i,int j){
		ArrayList<Integer> indexs = new ArrayList<Integer>();

        indexs.add(i);
        indexs.add(j);
		this.indexList.add(indexs);
	}
	
	//��ȡ�ֵ������б�
	public ArrayList<ArrayList<Integer>> getIndexList(){
		
		return this.indexList;
	}
	

	

	


}
