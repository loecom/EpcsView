package com.lchb.epcsview;

import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.procimg.InputRegister;
import net.wimpi.modbus.procimg.Register;
import net.wimpi.modbus.util.BitVector;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements OnClickListener, OnGestureListener {

	private GestureDetector mGestureDetector;
    
    @SuppressLint("HandlerLeak")
	Handler mainActivityHandler1=new Handler(){
        public void handleMessage(Message msg) {
        	mRunning=true;
        	new updateslavedate((String)msg.obj, msg.what).start();
        	setFragment(1);
        }
    };

    @SuppressLint("HandlerLeak")
	Handler mainActivityHandler2=new Handler(){
        public void handleMessage(Message msg) {
        	switch (msg.what){
        	case GETMHANDLER:
            	mHandler=(Handler)msg.obj;
            	break;
        	case ENDTHREAD:
        		if (mRunning){
        			endthread=true;
        		}
        		else{
        			endthread=false;
        			mHandler.sendMessage(mHandler.obtainMessage(WAITMBTCPEND));
        		}
            	break;
        	case MBTCPERR:
    			//new AlertDialog.Builder(MainActivity.this)
    			//.setTitle("通讯错误")
    			//.setMessage("Modbus TCP通讯错误!")
        		//.setCancelable(false)
    		    //.setPositiveButton("确定", null).show();
        		break;
            default:
            	break;
        	}
        }
    };
    
	private Fragment[] frags={new MainFragment(mainActivityHandler1,mainActivityHandler2), new Fragment01(), new Fragment02(), new AboutFragment()};
	
	private int pIndex=0;
	private int nIndex;
	private Handler mHandler;
	private boolean mRunning=false;
	private boolean endthread=false;
	private static final int WAITMBTCPEND=6;
	private static final int GETMHANDLER=0;
	private static final int ENDTHREAD=1;
	private static final int MBTCPERR=2;
	private Button btnPrv;
	private Button btnNext;
	private TextView txtTitle;
	private UpdateManager updatemanager;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebtn);
		mGestureDetector=new GestureDetector(this);
    	final EditText input = new EditText(MainActivity.this);
    	new AlertDialog.Builder(MainActivity.this)
		.setTitle("输入密码")
		.setMessage("请输入修改权限密码!")
		.setView(input)
		.setCancelable(false)
	    .setPositiveButton("确定", new DialogInterface.OnClickListener() { 
	        @Override 
	        public void onClick(DialogInterface dialog, int which) { 
	        	// 点击“确认”后的操作
	        	String password=input.getText().toString();
	        	if (password.equals("4388")){
	        	}
	        	else{
    				new AlertDialog.Builder(MainActivity.this)
    				.setTitle("密码错误")
    				.setMessage("密码不正确即将退出程序!")
    				.setCancelable(false)
    			    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
    			        @Override 
    			        public void onClick(DialogInterface dialog, int which) {
    			    		GlobalVar globalvar=(GlobalVar)getApplication();
    			    		if (globalvar.getwifiswitch()==false)
    			    			WifiAdmin.getWifiAdmin().closeWifi();
    			        	android.os.Process.killProcess(android.os.Process.myPid());
    			        	System.exit(0);
    			        }
    			    }).show();
	        	}
	        } 
	    }).show();
		inittag();
		init();
		WifiAdmin.getWifiAdmin().setmContext(MainActivity.this);
		WifiAdmin.getWifiAdmin().getWifiMeathod();
		GlobalVar globalvar=(GlobalVar)getApplication();
		globalvar.setwifiswitch(WifiAdmin.getWifiAdmin().getwifiswitch());
		if (globalvar.getwifiswitch()==false)
			WifiAdmin.getWifiAdmin().openWifi();
		new UpdateManager(MainActivity.this).checkUpdate();
	}

    @Override
	protected void onResume() {
		 super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	public void init(){
		GlobalVar globalvar=(GlobalVar)getApplication();
		boolean[] result1xs=new boolean[36];
		short[] result3xs = new short[155];
		short[] result4xs = new short[61];
		for (int i=0;i<result1xs.length;i++)
			result1xs[i]=false;
		globalvar.setv10000(result1xs);
		for (int i=0;i<result3xs.length;i++)
			result3xs[i]=0;
		globalvar.setv30000(result3xs);
		for (int i=0;i<result4xs.length;i++)
			result4xs[i]=0;
		globalvar.setv40000(result4xs);
	}
	
	public void inittag(){
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.ll_fragment, frags[0]);
		fragmentTransaction.commit();
		btnPrv=(Button)findViewById(R.id.btnPrv);
		btnNext=(Button)findViewById(R.id.btnNext);
		txtTitle=(TextView)findViewById(R.id.txtTitle);
		btnPrv.setOnClickListener(this);
		btnNext.setOnClickListener(this);
		txtTitle.setText("主页");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		GlobalVar globalvar=(GlobalVar)getApplication();
		if (globalvar.getwifiswitch()==false)
			WifiAdmin.getWifiAdmin().closeWifi();
	}
	
	@Override 
	public void onBackPressed() {
		if (pIndex>0){
			GlobalVar globalvar=(GlobalVar)getApplication();
			if (pIndex==3 && globalvar.getv40000(15)!=3) setFragment(pIndex-2);
			else setFragment(pIndex-1);
		}
		else{
			new AlertDialog.Builder(this)
			.setTitle("退出程序")
			.setMessage("确定要退出吗?")
			.setCancelable(false)
		    .setPositiveButton("确定", new DialogInterface.OnClickListener() { 
		        @Override 
		        public void onClick(DialogInterface dialog, int which) {
		    		GlobalVar globalvar=(GlobalVar)getApplication();
		    		if (globalvar.getwifiswitch()==false)
		    			WifiAdmin.getWifiAdmin().closeWifi();
		        	android.os.Process.killProcess(android.os.Process.myPid());
		        	System.exit(0);
		        } 
		    }) 
		    .setNegativeButton("取消", null).show();
		}
	}

	class updateslavedate extends Thread{ 
		ModbusTcpMaster mbtcp;
		GlobalVar globalvar=(GlobalVar)getApplication();
		boolean[] result1xs=new boolean[36];
		short[] result3xs = new short[155];
		short[] result4xs = new short[61];
		
        public void run() {
        	while (mRunning){
    			if (mbtcp!=null){
    				try {
    					if (!mbtcp.isConnected()){
    						mbtcp.connect();
    						Thread.sleep(200);
    					}
    					if (mbtcp.isConnected()&&WifiAdmin.getWifiAdmin().getwifiswitch()&&WifiAdmin.getWifiAdmin().getnetworkstate()){
    						globalvar.setmbtcp(mbtcp);
    						ModbusTcpMaster(mbtcp);
    					}
    					else{
    						mainActivityHandler2.sendMessage(mainActivityHandler2.obtainMessage(MBTCPERR));
    						for (int i=0;i<result1xs.length;i++)
    							result1xs[i]=false;
    						globalvar.setv10000(result1xs);
    						for (int i=0;i<result3xs.length;i++)
    							result3xs[i]=0;
    						globalvar.setv30000(result3xs);
    						for (int i=0;i<result4xs.length;i++)
    							result4xs[i]=0;
    						globalvar.setv40000(result4xs);
    					}
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
						mainActivityHandler2.sendMessage(mainActivityHandler2.obtainMessage(MBTCPERR));
						for (int i=0;i<result1xs.length;i++)
							result1xs[i]=false;
						globalvar.setv10000(result1xs);
						for (int i=0;i<result3xs.length;i++)
							result3xs[i]=0;
						globalvar.setv30000(result3xs);
						for (int i=0;i<result4xs.length;i++)
							result4xs[i]=0;
						globalvar.setv40000(result4xs);
					}
    			}
    			else{
					mainActivityHandler2.sendMessage(mainActivityHandler2.obtainMessage(MBTCPERR));
					for (int i=0;i<result1xs.length;i++)
						result1xs[i]=false;
					globalvar.setv10000(result1xs);
					for (int i=0;i<result3xs.length;i++)
						result3xs[i]=0;
					globalvar.setv30000(result3xs);
					for (int i=0;i<result4xs.length;i++)
						result4xs[i]=0;
					globalvar.setv40000(result4xs);
					globalvar.setmbtcp(mbtcp);
    			}
    			if (endthread){
    				mRunning=false;
					for (int i=0;i<result1xs.length;i++)
						result1xs[i]=false;
					globalvar.setv10000(result1xs);
					for (int i=0;i<result3xs.length;i++)
						result3xs[i]=0;
					globalvar.setv30000(result3xs);
					for (int i=0;i<result4xs.length;i++)
						result4xs[i]=0;
					globalvar.setv40000(result4xs);
    				mbtcp.disconnect();
        			mbtcp=null;
					globalvar.setmbtcp(mbtcp);
        			mHandler.sendMessage(mHandler.obtainMessage(WAITMBTCPEND));
    				endthread=false;
    			}
        	}
        }
    	
    	public updateslavedate(String ipaddr, int UnitId){
			mbtcp=null;
    		mbtcp=new ModbusTcpMaster(ipaddr, UnitId);
    	}
    }
	
	public void ModbusTcpMaster(ModbusTcpMaster mbtcp){
		GlobalVar globalvar=(GlobalVar)getApplication();
		BitVector result1x=new BitVector(36);
		boolean[] result1xs=new boolean[36];
		InputRegister[] result3xl;
		InputRegister[] result3xh;
		short[] result3xs = new short[155];
		Register[] result4x;
		short[] result4xs = new short[61];
		if (mbtcp!=null&&mbtcp.isConnected()&&WifiAdmin.getWifiAdmin().getwifiswitch()&&WifiAdmin.getWifiAdmin().getnetworkstate()){
			try {
		        new mbWriteThread(this, hHandler, 0, 8, 1).start();
				result1x=mbtcp.readInputDiscretes(0, 35);
				for (int i=1;i<=result1x.size();i++)
					result1xs[i]=result1x.getBit(i-1);
				globalvar.setv10000(result1xs);
			} catch (ModbusException e) {
				e.printStackTrace();
				mbtcp.disconnect();
				for (int i=0;i<result1xs.length;i++)
					result1xs[i]=false;
				globalvar.setv10000(result1xs);
			}
		}
		else{
			for (int i=0;i<result1xs.length;i++)
				result1xs[i]=false;
			globalvar.setv10000(result1xs);
		}
		try {
			Thread.sleep(200);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		if (mbtcp!=null&&mbtcp.isConnected()&&WifiAdmin.getWifiAdmin().getwifiswitch()&&WifiAdmin.getWifiAdmin().getnetworkstate()){
			try {
				result3xl=mbtcp.readInputRegisters(0, 77);
				for (int i=1;i<=result3xl.length;i++)
					result3xs[i]=result3xl[i-1].toShort();
			} catch (ModbusException e) {
				e.printStackTrace();
				mbtcp.disconnect();
				for (int i=0;i<result3xs.length;i++)
					result3xs[i]=0;
				globalvar.setv30000(result3xs);
			}
		}
		else{
			for (int i=0;i<result3xs.length;i++)
				result3xs[i]=0;
			globalvar.setv30000(result3xs);
		}
		try {
			Thread.sleep(200);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		if (mbtcp!=null&&mbtcp.isConnected()&&WifiAdmin.getWifiAdmin().getwifiswitch()&&WifiAdmin.getWifiAdmin().getnetworkstate()){
			try {
				result3xh=mbtcp.readInputRegisters(77, 77);
				for (int i=1;i<=result3xh.length;i++)
					result3xs[i+77]=result3xh[i-1].toShort();
				globalvar.setv30000(result3xs);
			} catch (ModbusException e) {
				e.printStackTrace();
				mbtcp.disconnect();
				for (int i=0;i<result3xs.length;i++)
					result3xs[i]=0;
				globalvar.setv30000(result3xs);
			}
		}
		else{
			for (int i=0;i<result3xs.length;i++)
				result3xs[i]=0;
			globalvar.setv30000(result3xs);
		}
		try {
			Thread.sleep(200);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		if (mbtcp!=null&&mbtcp.isConnected()&&WifiAdmin.getWifiAdmin().getwifiswitch()&&WifiAdmin.getWifiAdmin().getnetworkstate()){
			try {
				result4x=mbtcp.readMultipleRegisters(0, 60);
				for (int i=1;i<=result4x.length;i++)
					result4xs[i]=result4x[i-1].toShort();
				globalvar.setv40000(result4xs);
			} catch (ModbusException e) {
				e.printStackTrace();
				mbtcp.disconnect();
				for (int i=0;i<result4xs.length;i++)
					result4xs[i]=0;
				globalvar.setv40000(result4xs);
			}
		}
		else{
			for (int i=0;i<result4xs.length;i++)
				result4xs[i]=0;
			globalvar.setv40000(result4xs);
		}
	}
	
    @SuppressLint("HandlerLeak")
	Handler hHandler = new Handler() {
        @Override  
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }  
    };

	@Override
	public void onClick(View v) {
		GlobalVar globalvar=(GlobalVar)getApplication();
		int index=0;
		switch (v.getId()) {
		case R.id.btnPrv:
			index=nIndex-1;
			if (index<0)index=frags.length-1;
			if (index==2 && globalvar.getv40000(15)==0) index=1;
			setFragment(index);
			break;
		case R.id.btnNext:
			index=nIndex+1;
			if (index>=frags.length)index=0;
			if (index==2 && globalvar.getv40000(15)==0) index=3;
			setFragment(index);
			break;
		default:
			break;
		}
	}
	
	public void setFragment(int index){
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		nIndex = index;
		if(pIndex==frags.length-1&&nIndex==0){
			fragmentTransaction.setCustomAnimations(R.anim.move_in, R.anim.move_out);
		}
		else if(pIndex==0&&nIndex==frags.length-1){
			fragmentTransaction.setCustomAnimations(R.anim.move_in_2, R.anim.move_out_2);
		}
		else if(pIndex < nIndex){
			fragmentTransaction.setCustomAnimations(R.anim.move_in, R.anim.move_out);
		}
		else{
			fragmentTransaction.setCustomAnimations(R.anim.move_in_2, R.anim.move_out_2);
		}
		pIndex = index;
		if (index<frags.length&&index>=0){
			fragmentTransaction.replace(R.id.ll_fragment, frags[index]);
			fragmentTransaction.commit();
		}
		switch (nIndex){
		case 0:
			txtTitle.setText("主页");
			break;
		case 1:
			txtTitle.setText("调试");
			break;
		case 2:
			txtTitle.setText("波形");
			break;
		case 3:
			txtTitle.setText("关于连成");
			break;
		default:
			txtTitle.setText("连成环保");
			break;
		}
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		GlobalVar globalvar=(GlobalVar)getApplication();
		int index=0;
		if(e1.getX()-e2.getX()>=255 && Math.abs(velocityX)>0) {//move to left
			index=nIndex+1;
			if (index>=frags.length)index=0;
			if (index==2 && globalvar.getv40000(15)==0) index=3;
			setFragment(index);
		}else if(e2.getX()-e1.getX()>=255 && Math.abs(velocityX)>0) {
			index=nIndex-1;
			if (index<0)index=frags.length-1;
			if (index==2 && globalvar.getv40000(15)==0) index=1;
			setFragment(index);
		}else {  
			return false;  
		}  
		return true;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,float distanceY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {}
	
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
    	if(mGestureDetector.onTouchEvent(event))
    		event.setAction(MotionEvent.ACTION_CANCEL);  
    	return super.dispatchTouchEvent(event);
    }
}
