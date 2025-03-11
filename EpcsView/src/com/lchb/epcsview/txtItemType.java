package com.lchb.epcsview;

public class txtItemType {
	public String Title;
	public boolean isWrite;
	public String Value;
	public String txtItem1;
	public String txtItem2;
	public String txtItem3;
	public int index;
	
	public void setTitle(String Title){
		this.Title=Title;
	}
	
	public String getTitle(){
		return this.Title;
	}
	
	public void settxtItem1(String txtItem1){
		this.txtItem1=txtItem1;
	}
	
	public String gettxtItem1(){
		return this.txtItem1;
	}
	
	public void settxtItem2(String txtItem2){
		this.txtItem2=txtItem2;
	}
	
	public String gettxtItem2(){
		return this.txtItem2;
	}
	
	public void settxtItem3(String txtItem3){
		this.txtItem3=txtItem3;
	}
	
	public String gettxtItem3(){
		return this.txtItem3;
	}
	
	public void setisWrite(boolean isWrite){
		this.isWrite=isWrite;
	}
	
	public boolean isWrite(){
		return this.isWrite;
	}
	
	public void setValue(String Value){
		this.Value=Value;
	}
	
	public String getValue(){
		return this.Value;
	}
	
	public void setindex(int index){
		this.index=index;
	}
	
	public int getindex(){
		return this.index;
	}

}
