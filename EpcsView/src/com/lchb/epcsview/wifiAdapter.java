package com.lchb.epcsview;

import java.util.List;  

import android.content.Context;  
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.view.LayoutInflater;  
import android.view.View;  
import android.view.ViewGroup;  
import android.widget.BaseAdapter;  
import android.widget.TextView;

public class wifiAdapter extends BaseAdapter {  
	  
    private List<ScanResult> wifilist; //数据  
    private int resource;   //item的布局  
    private Context context;  
    private LayoutInflater inflator;  
    private TextView wifiTextView;  
    
    /** 
     *  
     * @param context mainActivity 
     * @param persons   显示的数据 
     * @param resource  一个Item的布局 
     */  
    public wifiAdapter(Context context,List<ScanResult>wifilist,int resource){  
        this.context = context;
        this.wifilist = wifilist;
        this.resource = resource;
    }
    
    public void refresh(List<ScanResult> list) {
    	this.wifilist = list;
    	this.notifyDataSetChanged();
    }
    
    /* 
     * 获得数据总数 
     * */  
    @Override  
    public int getCount() {  
        return wifilist.size();
    }  
    /* 
     * 根据索引为position的数据 
     * */  
    @Override  
    public Object getItem(int position) {  
        return wifilist.get(position);
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
        wifiTextView = (TextView)convertView.findViewById(R.id.ItemText);
        ScanResult list = wifilist.get(position);
		GlobalVar globalvar=(GlobalVar)context.getApplicationContext();
		ModbusTcpMaster mbtcp=globalvar.getmbtcp();
        if(list.SSID.equals(getnowssid())){
        	wifiTextView.setTextColor(Color.BLUE);
    		if (mbtcp!=null){
    			if (mbtcp.isConnected()){
        			if (mbtcp.getslaveipaddr().equals("192.168.0.1")){
        	        	wifiTextView.setTextColor(Color.RED);
        			}
    			}
    		}
        }
        else{
        	wifiTextView.setTextColor(Color.BLACK);
        }
        wifiTextView.setTag(list);
        wifiTextView.setText(WifiManager.calculateSignalLevel(list.level, 101)+"%/"+list.SSID);
        return convertView;
    }
    
    public String getnowssid() {  
    	WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);  
    	WifiInfo info = wifi.getConnectionInfo();
    	//String maxText = info.getMacAddress();
    	//String ipText = intToIp(info.getIpAddress());
    	@SuppressWarnings("unused")
		String status = "";
    	if (wifi.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
    		status = "WIFI_STATE_ENABLED";
    	}
    	int start,end;
    	try{
    		start=info.getSSID().indexOf("\"")+1;
    		end=info.getSSID().length()-1;
    	}
    	catch(NullPointerException e){
    		start=0;
    		end=info.getSSID().length();
    	}
    	String ssid = info.getSSID().substring(start, end);
    	//int networkID = info.getNetworkId();
    	//int speed = info.getLinkSpeed();
    	return ssid;
    	/*return "mac：" + maxText + "\n\r"
    	+ "ip：" + ipText + "\n\r"
    	+ "wifi status :" + status + "\n\r"
    	+ "ssid :" + ssid + "\n\r"
    	+ "net work id :" + networkID + "\n\r"
    	+ "connection speed:" + speed + "\n\r"
    	;*/
    }
}