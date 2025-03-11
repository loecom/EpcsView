package com.lchb.epcsview;

import android.app.Application;

public class GlobalVar extends Application{
	
	private boolean wifiswitch=false;
	public boolean getwifiswitch(){
        return wifiswitch;
    }
	public void setwifiswitch(boolean wifiswitch){
        this.wifiswitch=wifiswitch;
    }
	
	private ModbusTcpMaster mbtcp=null;
	public ModbusTcpMaster getmbtcp(){
        return mbtcp;
    }
	public void setmbtcp(ModbusTcpMaster mbtcp){
        this.mbtcp=mbtcp;
    }

	private boolean[] v10000=new boolean[36];
	public boolean getv10000(int index){
        return v10000[index];
    }
	public boolean[] getv10000(){
        return v10000;
    }
	public void setv10000(boolean[] v10000){
        this.v10000=v10000;
    }
	
	private short[] v30000 = new short[155];
	public short getv30000(int index){
        return v30000[index];
    }
	public short[] getv30000(){
        return v30000;
    }
	public void setv30000(short[] v30000){
        this.v30000=v30000;
    }
	
	private short[] v40000 = new short[61];
	public short getv40000(int index){
        return v40000[index];
    }
	public short[] getv40000(){
        return v40000;
    }
	public void setv40000(short[] v40000){
        this.v40000=v40000;
    }
	
}
