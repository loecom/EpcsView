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
	  
    private List<udpcmdtype> devicelist; //数据  
    private int resource;   //item的布局  
    private Context context;  
    private LayoutInflater inflator;  
    private TextView deviceTextView;
    
    /** 
     *  
     * @param context mainActivity
     * @param persons   显示的数据 
     * @param resource  一个Item的布局 
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
     * 获得数据总数 
     * */  
    @Override  
    public int getCount() {
    	int size;
    	size=devicelist.size();
        return size;
    }  
    /* 
     * 根据索引为position的数据 
     * */  
    @Override  
    public Object getItem(int position) {
        return devicelist.get(position);
    }  
    /* 
     * 根据索引值获得Item的Id 
     * */  
    @Override  
    public long getItemId(int position) {
        return position;
    }  
    /*
     *通过索引值position将数据映射到视图 
     *convertView具有缓存功能，在第一页时为null，在第二第三....页时不为null 
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