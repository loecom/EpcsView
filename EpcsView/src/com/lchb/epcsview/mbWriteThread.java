package com.lchb.epcsview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.procimg.Register;

public class mbWriteThread extends Thread{
	private int mbtype;
	private int ref;
	private boolean state;
	private Register[] register;
	private ModbusTcpMaster mbtcp;
	private Handler mHandler;
	private int value;
    private static final int WRITEERROR=3;

    public void run() {
		if (mbtcp!=null){
			try {
				if (mbtcp.isConnected()){
			    	switch (mbtype){
			    	case 0:
			    		try {
							mbtcp.writeCoil(ref, state);
						} catch (ModbusException e) {
							e.printStackTrace();
			        		Message msg = mHandler.obtainMessage();
			        		msg.what = WRITEERROR;
			        		msg.sendToTarget();
						}
			    		break;
			    	case 1:
			    		try {
			    			register=mbtcp.readMultipleRegisters(ref, 1);
			    			register[0].setValue(value);
							mbtcp.writeSingleRegister(ref, register[0]);
						} catch (ModbusException e) {
							e.printStackTrace();
			        		Message msg = mHandler.obtainMessage();
			        		msg.what = WRITEERROR;
			        		msg.sendToTarget();
						}
			    		break;
			    	default:
			    		break;
			    	}
				}
				else{
	        		Message msg = mHandler.obtainMessage();
	        		msg.what = WRITEERROR;
	        		msg.sendToTarget();
				}
			} catch (Exception e) {
				e.printStackTrace();
        		Message msg = mHandler.obtainMessage();
        		msg.what = WRITEERROR;
        		msg.sendToTarget();
			}
		}
		else{
    		Message msg = mHandler.obtainMessage();
    		msg.what = WRITEERROR;
    		msg.sendToTarget();
		}
    }
    
    public mbWriteThread(Context context, Handler mHandler, int mbtype, int ref, int value){
		GlobalVar globalvar=(GlobalVar)context.getApplicationContext();
    	this.mHandler=mHandler;
		this.mbtype=mbtype;
		this.ref=ref;
		if (value==0){
			this.state=false;
		}
		else{
			this.state=true;
		}
		this.value=value;
		this.mbtcp=globalvar.getmbtcp();
    }
}
