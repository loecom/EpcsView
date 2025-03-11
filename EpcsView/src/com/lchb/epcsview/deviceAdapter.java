package com.lchb.epcsview;

import java.util.List;  

import android.content.Context;  
import android.graphics.Color;
import android.view.LayoutInflater;  
import android.view.View;  
import android.view.ViewGroup;  
import android.widget.BaseAdapter;  
import android.widget.TextView;

public class deviceAdapter extends BaseAdapter {  
	  
    private List<udpcmdtype> devicelist; //����  
    private int resource;   //item�Ĳ���  
    private Context context;  
    private LayoutInflater inflator;  
    private TextView deviceTextView;
    
    /** 
     *  
     * @param context mainActivity
     * @param persons   ��ʾ������ 
     * @param resource  һ��Item�Ĳ��� 
     */  
    public deviceAdapter(Context context,List<udpcmdtype>devicelist,int resource){  
        this.context = context;
        this.devicelist = devicelist;
        this.resource = resource;
    }
    
    public void refresh(List<udpcmdtype> list) {
    	this.devicelist = list;
    	this.notifyDataSetChanged();
    }
    
    /* 
     * ����������� 
     * */  
    @Override  
    public int getCount() {
    	int size;
    	size=devicelist.size();
        return size;
    }  
    /* 
     * ��������Ϊposition������ 
     * */  
    @Override  
    public Object getItem(int position) {
        return devicelist.get(position);
    }  
    /* 
     * ��������ֵ���Item��Id 
     * */  
    @Override  
    public long getItemId(int position) {
        return position;
    }  
    /*
     *ͨ������ֵposition������ӳ�䵽��ͼ 
     *convertView���л��湦�ܣ��ڵ�һҳʱΪnull���ڵڶ�����....ҳʱ��Ϊnull 
     * */  
    
    @Override  
    public View getView(int position, View convertView, ViewGroup parent) {
        inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflator.inflate(resource, null);
        deviceTextView = (TextView)convertView.findViewById(R.id.ItemText);
        udpcmdtype list = devicelist.get(position);
		GlobalVar globalvar=(GlobalVar)context.getApplicationContext();
		ModbusTcpMaster mbtcp=globalvar.getmbtcp();
		if (mbtcp!=null){
			if (mbtcp.isConnected()){
    			if (mbtcp.getslaveipaddr().equals(list.getIPaddr())){
    	        	deviceTextView.setTextColor(Color.RED);
    			}
			}
		}
        else{
        	deviceTextView.setTextColor(Color.rgb(0, 0, 0));
        }
        deviceTextView.setTag(list);
        deviceTextView.setText(list.getDevicename());
        return convertView;
    }

}