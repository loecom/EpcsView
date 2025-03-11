package com.lchb.epcsview;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

	/**
	 * A placeholder fragment containing a simple view.
	 */
	@SuppressLint("ValidFragment")
	public class MainFragment extends Fragment {
		private ListView wifiList;
		private ListView DeviceList;
		private List<ScanResult> list;//扫描结果列表
		private List<udpcmdtype> devicelist;
		private wifiAdapter adapter;
		private deviceAdapter deviceadapter;
		private boolean updatewifiRunning = false;
		private boolean updatedeviceRunning = false;
		private static final int UPDATEWIFILIST=0;
	    private static final int UPDATEDEVICELIST=1;
	    private static final int SETDEVICEOK=2;
	    private static final int SETDEVICENOK=3;
	    private static final int UPDATEDEVICEFIRST=4;
		private static final int POPKEYBORAD=5;
		private static final int WAITMBTCPEND=6;
		private static final int SETMHANDLER=0;
		private static final int ENDTHREAD=1;
	    private static final byte[] EPCSCMDBYTE={(byte)0x45,(byte)0x50,(byte)0x43,(byte)0x53,(byte)0x2d,(byte)0x56,(byte)0x2d,(byte)0x43,(byte)0x4d,(byte)0x44,
				 (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
				 (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
				 (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
				 (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00};
	    private static final udpcmdtype SERVER_CMDBYTE=new udpcmdtype(EPCSCMDBYTE);
	    private Handler mainActivityHandler1;
	    private Handler mainActivityHandler2;
	    private String ipaddr;
	    private int UnitID;
	    private String wifissid;
	    private String wifipassword;
	    private int wifitype;
		
		public MainFragment(Handler mainActivityHandler1,Handler mainActivityHandler2) {
			this.mainActivityHandler1=mainActivityHandler1;
			this.mainActivityHandler2=mainActivityHandler2;
		}
		
		public void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, null);
			return rootView;
		}
		
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			init();
		}
		
		@Override
		public void onStart() {
			super.onStart();
		}
		
		@Override
		public void onResume() {
			super.onResume();
		}
		
		@Override
		public void onPause() {
			super.onPause();
		}
		
		@Override
		public void onStop() {
			super.onStop();
		}
		
		@Override
		public void onDestroy() {
			super.onDestroy();
			updatewifiRunning = false;
			updatedeviceRunning=false;
		}
		
		@SuppressLint("HandlerLeak")
        @SuppressWarnings("unchecked")
		Handler mHandler = new Handler() {
			@Override  
	        public void handleMessage(Message msg) {  
	            switch (msg.what){
	            	case UPDATEWIFILIST:
	            		if ((List<ScanResult>)msg.obj!=null) adapter.refresh((List<ScanResult>)msg.obj);
	            		break;
	            	case UPDATEDEVICELIST:
                		if ((List<udpcmdtype>)msg.obj!=null) deviceadapter.refresh((List<udpcmdtype>)msg.obj);
	            		break;
	            	case SETDEVICEOK:
        				new AlertDialog.Builder(getActivity())
        				.setTitle("修改成功").setMessage("设备信息修改成功!")
        				.setCancelable(false)
        			    .setPositiveButton("确定", null).show();
	            		break;
	            	case SETDEVICENOK:
        				new AlertDialog.Builder(getActivity())
        				.setTitle("修改失败").setMessage("设备信息修改失败,请重新修改!")
        				.setCancelable(false)
        			    .setPositiveButton("确定", null).show();
	            		break;
	            	case UPDATEDEVICEFIRST:
                		deviceadapter=new deviceAdapter(getActivity(),(List<udpcmdtype>)msg.obj,R.layout.listitem);
              			DeviceList.setAdapter(deviceadapter);
              			updatedeviceRunning = true;
                  		new updatedevicelist().start();
	            		break;
		        	case POPKEYBORAD:
	                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
	                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		        		break;
		        	case WAITMBTCPEND:
		        		switch (wifitype){
		        		case 1:
		        		    WifiAdmin.getWifiAdmin().addNetwork(WifiAdmin.getWifiAdmin().CreateWifiInfo(wifissid, wifipassword, wifitype));
				    		new sendhandlermsgthread(ipaddr, UnitID).start();
		        		    wifissid="";
		        		    wifipassword="";
		        		    wifitype=0;
		        			break;
		        		case 2:
		        		case 3:
		        		    WifiAdmin.getWifiAdmin().addNetwork(WifiAdmin.getWifiAdmin().CreateWifiInfo(wifissid, wifipassword, wifitype));
		        		    wifissid="";
		        		    wifipassword="";
		        		    wifitype=0;
		        			break;
		        		case 4:
				    		new sendhandlermsgthread(ipaddr, UnitID).start();
		        		    wifitype=0;
		        		default:
		        			break;
		        		}
		        		break;
	            	default:
	            		break;
	            }
	            super.handleMessage(msg);  
	        }  
	    };
		
	    class updatewifilist extends Thread{  
            @Override  
            public void run() {
            	while(updatewifiRunning){
            		try {
                		WifiAdmin.getWifiAdmin().startScan();
        				list=WifiAdmin.getWifiAdmin().getWifiList();
        				list=removeListElement2(list);
        				Collections.sort(list, new Comparator<ScanResult>() {
        					@Override
        					public int compare(ScanResult lhs, ScanResult rhs) {
        						int level1,level2;
        						level1=WifiManager.calculateSignalLevel(lhs.level, 101);
        						level2=WifiManager.calculateSignalLevel(rhs.level, 101);
        						if (level1<level2)
        							return 1;
        						else if (level1==level2)
        							return 0;
        						else if (level1>level2)
        							return -1;
        						return 0; 
        					}
        		        });
    					mHandler.sendMessage(mHandler.obtainMessage(UPDATEWIFILIST, list));
            			Thread.sleep(2000);
            		} catch (InterruptedException e) {
            			e.printStackTrace();
            		}
            	}
            }
        }
        
	    class updatedevicelist extends Thread{
            @Override
            public void run() {
            	while(updatedeviceRunning){
            		try {
                		devicelist=UpdServer("255.255.255.255",6798,6799,SERVER_CMDBYTE);
    					mHandler.sendMessage(mHandler.obtainMessage(UPDATEDEVICELIST, devicelist));
            			Thread.sleep(1);
            		} catch (InterruptedException e) {
            			e.printStackTrace();
            		}
            	}
            }
        }
	    
	   class setdevicethread extends Thread{
	    	private String ipaddr;
	    	private int serverport;
	    	private int clientport;
	    	private udpcmdtype SERVER_CMDBYTE;
	    	private List<udpcmdtype> returnlist;

            public void run() {
            	returnlist=UpdServer(ipaddr,serverport,clientport,SERVER_CMDBYTE);
            	String cmd="";
            	int len=returnlist.size();
            	String server_cmdtype=SERVER_CMDBYTE.toString();
            	if (len>0){
                	cmd=returnlist.get(0).toString();
                	if (cmd.equals(server_cmdtype)){
                		Message msg = mHandler.obtainMessage();  
                		msg.what = SETDEVICEOK;
                		msg.sendToTarget();
                	}
            	}
            	else{
            		Message msg = mHandler.obtainMessage();  
            		msg.what = SETDEVICENOK;
            		msg.sendToTarget();
            	}
            }
            public setdevicethread(String ipaddr, int serverport, int clientport, udpcmdtype SERVER_CMDBYTE){
	    		this.ipaddr=ipaddr;
	    		this.serverport=serverport;
	    		this.clientport=clientport;
	    		this.SERVER_CMDBYTE=SERVER_CMDBYTE;
            }
        }
	    
	   class sendhandlermsgthread extends Thread{
	    	private String ipaddr;
	    	private int UnitID;

	    	public void run() {
				try {
					Thread.sleep(2000);
		    		while (!WifiAdmin.getWifiAdmin().getwifiswitch()||!WifiAdmin.getWifiAdmin().getnetworkstate()){
						Thread.sleep(10);
					}
					mainActivityHandler2.sendMessage(mainActivityHandler2.obtainMessage(SETMHANDLER, mHandler));
	    			if (WifiAdmin.getWifiAdmin().getwifiswitch()&&WifiAdmin.getWifiAdmin().getnetworkstate()){
	    				mainActivityHandler1.sendMessage(mainActivityHandler1.obtainMessage(UnitID, ipaddr));
			    		ipaddr="";
			    		UnitID=0;
	    			}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	    	}
	    	
	    	public sendhandlermsgthread(String ipaddr, int UnitId){
	    		this.ipaddr=ipaddr;
	    		this.UnitID = UnitId;
	    	}
       }
        
		public void init(){
			wifiList=(ListView)getActivity().findViewById(R.id.wifiList);
			DeviceList=(ListView)getActivity().findViewById(R.id.DeviceList);
			wifiList.setOnItemClickListener(new wifilistItemListener());
			DeviceList.setOnItemClickListener(new devicelistItemListener());
		    WifiAdmin.getWifiAdmin().startScan();
      		list=WifiAdmin.getWifiAdmin().getWifiList();
			list=removeListElement2(list);
	        Collections.sort(list, new Comparator<ScanResult>() {
				@Override
				public int compare(ScanResult lhs, ScanResult rhs) {
					int level1,level2;
					level1=WifiManager.calculateSignalLevel(lhs.level, 101);
					level2=WifiManager.calculateSignalLevel(rhs.level, 101);
					if (level1<level2)
						return 1;
					else if (level1==level2)
						return 0;
					else if (level1>level2)
						return -1;
					return 0; 
				}
	        });
      		adapter=new wifiAdapter(getActivity(),list,R.layout.listitem);
      		wifiList.setAdapter(adapter);
			updatewifiRunning=true;
      		new updatewifilist().start();
      		
			new Thread(new Runnable(){
				public void run(){
					try{
						Thread.sleep(2000);
						devicelist=UpdServer("255.255.255.255",6798,6799,SERVER_CMDBYTE);
						mHandler.sendMessage(mHandler.obtainMessage(UPDATEDEVICEFIRST, devicelist));
					} 
					catch (InterruptedException e){
						e.printStackTrace();
					}
				}
			}).start();
		}
		
		public List<udpcmdtype> UpdServer(String ipaddr, int serverport, int clientport, udpcmdtype SERVER_CMDBYTE){
    		List<udpcmdtype> devicelist=new ArrayList<udpcmdtype>();
        	DatagramSocket updSocket = null;
        	InetAddress SERVER_IP = null;
        	try{
        		updSocket=new DatagramSocket(null);
        		updSocket.setReuseAddress(true);
        		updSocket.bind(new InetSocketAddress(serverport));
            	updSocket.setSoTimeout(3000);
				try {
                    SERVER_IP = InetAddress.getByName(ipaddr);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
	           	DatagramPacket sendPacket = new DatagramPacket(SERVER_CMDBYTE.toByte(), SERVER_CMDBYTE.toByte().length, SERVER_IP, clientport);
	           	byte buff[]=new byte[1024];
	           	DatagramPacket receivePacket = new DatagramPacket(buff,buff.length);
        		updSocket.send(sendPacket);
        		//Log.e("UDP test", "s e n d:"+msg.toString());
        		devicelist.clear();
        		for(int i=0;i<=updSocket.getReceiveBufferSize();i++){
        			updSocket.receive(receivePacket);
        			if (receivePacket!=null){
                        udpcmdtype udpmsg=new udpcmdtype(receivePacket.getData());
                        devicelist.add(udpmsg);
                    }
        		}
    	    } catch (IOException e) {
    	        e.printStackTrace();
    	        Collections.sort(devicelist, new Comparator<udpcmdtype>() {
					@Override
					public int compare(udpcmdtype lhs, udpcmdtype rhs) {
						int result;
						String ipaddr1,ipaddr2;
						String devicename1,devicename2;
						ipaddr1=lhs.getIPaddr();
						ipaddr2=rhs.getIPaddr();
						devicename1=lhs.getdevicename();
						devicename2=rhs.getdevicename();
						result=ipaddr1.compareTo(ipaddr2);
						if (result==0){
							result=devicename1.compareTo(devicename2);
						}
						return result;
					}
    	         });
    	    }
        	updSocket.disconnect();
        	updSocket.close();
        	updSocket=null;
			return devicelist;
		}
		
	    public class wifilistItemListener implements OnItemClickListener{
			@Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	            // TODO Auto-generated method stub
	        	final TextView content=(TextView)view.findViewById(R.id.ItemText);
	        	final EditText input = new EditText(getActivity());
		        final ScanResult wifiinfo=(ScanResult)content.getTag();
	        	final String ssid=wifiinfo.SSID.toString();
	            Builder dialog=new AlertDialog.Builder(getActivity()).setTitle("建立连接")
		        	.setCancelable(false)
	            	.setNegativeButton("取消", null)
	    		    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
		        		public void onClick(DialogInterface dialog, int which) {
		        			// 点击“确认”后的操作
		        			String password="";
		                	String[] SSID=ssid.toString().split("-");
			            	if ((wifiinfo.capabilities.toString().indexOf("WPA")>0)||(wifiinfo.capabilities.toString().indexOf("WEP")>0)){
			        			password=input.getText().toString();
			            	}
			            	else if ((wifiinfo.capabilities.toString().indexOf("WPA")<0)&&(wifiinfo.capabilities.toString().indexOf("WEP")<0)){
			        			password="llllllll";
			            	}
		        			if (password.equals("")){
		        				new AlertDialog.Builder(getActivity())
		        				.setTitle("密码错误").setMessage("密码为空请重新连接!")
		    	        		.setCancelable(false)
		        			    .setPositiveButton("确定", null).show();
		        			}
		        			else{
		        				if (wifiinfo.capabilities.toString().indexOf("WPA")>0){
		        					mainActivityHandler2.sendMessage(mainActivityHandler2.obtainMessage(SETMHANDLER, mHandler));
		    						mainActivityHandler2.sendMessage(mainActivityHandler2.obtainMessage(ENDTHREAD));
		    						wifitype=3;
		    						wifissid=ssid;
		    						wifipassword=password;
		        				}
		        				else if (wifiinfo.capabilities.toString().indexOf("WEP")>0){
		        					mainActivityHandler2.sendMessage(mainActivityHandler2.obtainMessage(SETMHANDLER, mHandler));
		    						mainActivityHandler2.sendMessage(mainActivityHandler2.obtainMessage(ENDTHREAD));
		    						wifitype=2;
		    						wifissid=ssid;
		    						wifipassword=password;
		        				}
		        				else if ((wifiinfo.capabilities.toString().indexOf("WPA")<0)&&(wifiinfo.capabilities.toString().indexOf("WEP")<0)){
		        					mainActivityHandler2.sendMessage(mainActivityHandler2.obtainMessage(SETMHANDLER, mHandler));
		    						mainActivityHandler2.sendMessage(mainActivityHandler2.obtainMessage(ENDTHREAD));
		    						wifitype=1;
		    						wifissid=ssid;
		    						wifipassword="";
				            		if (SSID.length==7){
				            			if (SSID[5].equals("000")){}
				            			else{
				            				ipaddr="192.168.0.1";
				            				UnitID=Integer.parseInt(SSID[5]);
				            			}
				            		}
		        				}
		        			}
		        		}
		        	});
	            if ((wifiinfo.capabilities.toString().indexOf("WPA")>0)||(wifiinfo.capabilities.toString().indexOf("WEP")>0)){
	            	dialog.setMessage("输入密码并确认连接吗?").setView(input).show();
	        		Message msg = mHandler.obtainMessage();
	        		msg.what = POPKEYBORAD;
	        		msg.sendToTarget();
	            }
	            else if ((wifiinfo.capabilities.toString().indexOf("WPA")<0)&&(wifiinfo.capabilities.toString().indexOf("WEP")<0)){
	            	dialog.setMessage("确认连接吗?").show();
	            }
	        }
	    }
	   
	    public class devicelistItemListener implements OnItemClickListener{
			@Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		        // TODO Auto-generated method stub
		        final TextView content=(TextView)view.findViewById(R.id.ItemText);
		        LayoutInflater factory=LayoutInflater.from(getActivity());
		        final View deviceview=factory.inflate(R.layout.devicedialog, null);
		        final EditText devicename=(EditText)deviceview.findViewById(R.id.editdevicename);
		        final EditText txtipaddr=(EditText)deviceview.findViewById(R.id.editipaddr);
		        final EditText maskaddr=(EditText)deviceview.findViewById(R.id.editmaskaddr);
		        final EditText macaddr=(EditText)deviceview.findViewById(R.id.editmacaddr);
		        final udpcmdtype device=(udpcmdtype)content.getTag();
		        devicename.setText(device.getdevicename());
		        txtipaddr.setText(device.getIPaddr());
		        maskaddr.setText(device.getmaskaddr());
		        macaddr.setText(device.getmacaddr());
		        new AlertDialog.Builder(getActivity())
			    .setTitle("设备信息")
			    .setView(deviceview)
        		.setCancelable(false)
			    .setPositiveButton("连接", new DialogInterface.OnClickListener() {
			        @Override
			        public void onClick(DialogInterface dialog, int which) {
			        	// 点击“确认”后的操作
						String devicename=device.getdevicename().toString();
						String[] namelist=devicename.split("-");
			    		if (namelist.length==7){
			    			if (namelist[5].equals("000")){}
			    			else{
	        					mainActivityHandler2.sendMessage(mainActivityHandler2.obtainMessage(SETMHANDLER, mHandler));
	    						mainActivityHandler2.sendMessage(mainActivityHandler2.obtainMessage(ENDTHREAD));
	    						wifitype=4;
					    		ipaddr=device.getIPaddr();
	            				UnitID=Integer.parseInt(namelist[5]);
			    			}
			    		}
			        }
			    })
			    .setNegativeButton("取消", null)
				.setNeutralButton("修改", new DialogInterface.OnClickListener() {
				    @Override
				    public void onClick(DialogInterface dialog, int which) {
				    	// 点击“修改”后的操作
				    	boolean devicestatus=checkdevice(devicename.getText().toString(),txtipaddr.getText().toString(),maskaddr.getText().toString(),macaddr.getText().toString());
				    	if (devicestatus){
				    		device.setcmd(Byte.parseByte("1"));
					    	device.setDevicename(devicename.getText().toString());
					    	device.setIPaddr(txtipaddr.getText().toString());
					    	device.setmaskaddr(maskaddr.getText().toString());
					    	device.setmacaddr(macaddr.getText().toString());
					    	Log.e("command:",device.toString());
					    	new setdevicethread(txtipaddr.getText().toString(), 6797, 6799, device).start();
				    	}
				    }
			    }).show();
	        }
	    }
	    
	    public List<ScanResult> removeListElement2(List<ScanResult> list) {
	    	for(int i=0;i<list.size();i++) {
	    		String ssid=list.get(i).SSID;
	    		if (ssid.length()>=5){
		        	if(("LCHB-".equals(ssid.toString().substring(0, 5)))||("TP-LINK_LCHBmian".equals(ssid))){}
		        	else{
		            	list.remove(i);
		            	--i;//删除了元素，迭代的下标也跟着改变
		        	}
	    		}
	    		else{
	            	list.remove(i);
	            	--i;//删除了元素，迭代的下标也跟着改变
	    		}
	    	}
			return list;
	    }
	    
	    public boolean checkdevice(String devicename, String ipaddr, String maskaddr, String macaddr){
			// TODO 自动生成的方法存根
    		Builder dialog=new AlertDialog.Builder(getActivity()).setTitle("修改错误")
	        .setCancelable(false)
    	    .setPositiveButton("确定", null);
    		boolean result=false;
			String temp1[]=new String[7];
			String temp2[]=new String[4];
			String temp3[]=new String[4];
			String temp4[]=new String[6];
	    	temp1=devicename.toString().split("-");
	    	temp2=ipaddr.toString().split("\\.");
	    	temp3=maskaddr.toString().split("\\.");
	    	temp4=macaddr.toString().split(":");
	    	if (temp1.length==7){
		    	for (int i=0;i<temp1.length;i++){
		    		switch(i){
		    			case 0:
		    				if ("LCHB".equals(temp1[i])){
		    					result=true;
		    				}
		    				else{
		    					result=false;
		    		    		dialog.setMessage("设备名称输入错误,请重新修改!").show();
		    		    		return result;
		    				}
		    				break;
		    			case 1:
		    			case 2:
		    			case 3:
		    			case 4:
		    			case 5:
		    			case 6:
		    		    	try{
		    		    		Short.parseShort(temp1[i]);
		    					result=true;
		    		    	}
		    		    	catch(NumberFormatException e){
		    					result=false;
		    		    		dialog.setMessage("设备名称输入错误,请重新修改!").show();
		    		    		return result;
		    		    	}
		    				break;
		    			default:
	    					result=false;
	    		    		dialog.setMessage("设备名称输入错误,请重新修改!").show();
	    		    		return result;
		    		}
		    	}
	    	}
	    	else{
				result=false;
	    		dialog.setMessage("设备名称输入错误,请重新修改!").show();
	    		return result;
	    	}
	    	if (temp2.length==4){
		    	for (int i=0;i<temp2.length;i++){
		    		switch(i){
		    			case 0:
		    			case 1:
		    			case 2:
		    			case 3:
		    		    	try{
		    		    		Short.parseShort(temp2[i]);
		    					result=true;
		    		    	}
		    		    	catch(NumberFormatException e){
		    					result=false;
		    		    		dialog.setMessage("IP地址输入错误,请重新修改!").show();
		    		    		return result;
		    		    	}
		    				break;
		    			default:
	    					result=false;
	    		    		dialog.setMessage("IP地址输入错误,请重新修改!").show();
	    		    		return result;
		    		}
		    	}
	    	}
	    	else{
				result=false;
	    		dialog.setMessage("IP地址输入错误,请重新修改!").show();
	    		return result;
	    	}
	    	if (temp3.length==4){
		    	for (int i=0;i<temp3.length;i++){
		    		switch(i){
		    			case 0:
		    			case 1:
		    			case 2:
		    			case 3:
		    		    	try{
		    		    		Short.parseShort(temp3[i]);
		    					result=true;
		    		    	}
		    		    	catch(NumberFormatException e){
		    					result=false;
		    		    		dialog.setMessage("MASK地址输入错误,请重新修改!").show();
		    		    		return result;
		    		    	}
		    				break;
		    			default:
	    					result=false;
	    		    		dialog.setMessage("MASK地址输入错误,请重新修改!").show();
	    		    		return result;
		    		}
		    	}
	    	}
	    	else{
				result=false;
	    		dialog.setMessage("MASK地址输入错误,请重新修改!").show();
	    		return result;
	    	}
	    	if (temp4.length==6){
		    	for (int i=0;i<temp4.length;i++){
		    		switch(i){
		    			case 0:
		    			case 1:
		    			case 2:
		    			case 3:
		    			case 4:
		    			case 5:
		    		    	try{
		    		    		Short.parseShort(temp4[i],16);
		    					result=true;
		    		    	}
		    		    	catch(NumberFormatException e){
		    					result=false;
		    		    		dialog.setMessage("MAC地址输入错误,请重新修改!").show();
		    		    		return result;
		    		    	}
		    				break;
		    			default:
	    					result=false;
	    		    		dialog.setMessage("MAC地址输入错误,请重新修改!").show();
	    		    		return result;
		    		}
		    	}
	    	}
	    	else{
				result=false;
	    		dialog.setMessage("MAC地址输入错误,请重新修改!").show();
	    		return result;
	    	}
			return result;
	    }
		
	}
