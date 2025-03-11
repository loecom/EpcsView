package com.lchb.epcsview;

public class udpcmdtype{
	public String command;
	public byte cmd;		//0-扫描,1-修改
	public byte dat;		//0
	public short sbh;		//设备号
	public short luh;		//炉号
	public short cuh;		//除尘器号
	public short tdh;		//通道号
	public short dch;		//电场号
	public short can;		//CAN地址
	public short ip1;
	public short ip2;
	public short ip3;
	public short ip4;
	public short mask1;
	public short mask2;
	public short mask3;
	public short mask4;
	public short sbType;	//单相,三相,高频
	public byte macadr1;
	public byte macadr2;
	public byte macadr3;
	public byte macadr4;
	public byte macadr5;
	public byte macadr6;

    public byte[] toByte() {
    	byte[] sb=new byte[48];
    	byte[] comm=new byte[10];
    	comm=this.command.getBytes();
        sb[0]=comm[0];
        sb[1]=comm[1];
        sb[2]=comm[2];
        sb[3]=comm[3];
        sb[4]=comm[4];
        sb[5]=comm[5];
        sb[6]=comm[6];
        sb[7]=comm[7];
        sb[8]=comm[8];
        sb[9]=comm[9];
        sb[10]=this.cmd;
        sb[11]=this.dat;
        sb[12]=shortToBytes(this.sbh,false)[0];
        sb[13]=shortToBytes(this.sbh,false)[1];
        sb[14]=shortToBytes(this.luh,false)[0];
        sb[15]=shortToBytes(this.luh,false)[1];
        sb[16]=shortToBytes(this.cuh,false)[0];
        sb[17]=shortToBytes(this.cuh,false)[1];
        sb[18]=shortToBytes(this.tdh,false)[0];
        sb[19]=shortToBytes(this.tdh,false)[1];
        sb[20]=shortToBytes(this.dch,false)[0];
        sb[21]=shortToBytes(this.dch,false)[1];
        sb[22]=shortToBytes(this.can,false)[0];
        sb[23]=shortToBytes(this.can,false)[1];
        sb[24]=shortToBytes(this.ip1,false)[0];
        sb[25]=shortToBytes(this.ip1,false)[1];
        sb[26]=shortToBytes(this.ip2,false)[0];
        sb[27]=shortToBytes(this.ip2,false)[1];
        sb[28]=shortToBytes(this.ip3,false)[0];
        sb[29]=shortToBytes(this.ip3,false)[1];
        sb[30]=shortToBytes(this.ip4,false)[0];
        sb[31]=shortToBytes(this.ip4,false)[1];
        sb[32]=shortToBytes(this.mask1,false)[0];
        sb[33]=shortToBytes(this.mask1,false)[1];
        sb[34]=shortToBytes(this.mask2,false)[0];
        sb[35]=shortToBytes(this.mask2,false)[1];
        sb[36]=shortToBytes(this.mask3,false)[0];
        sb[37]=shortToBytes(this.mask3,false)[1];
        sb[38]=shortToBytes(this.mask4,false)[0];
        sb[39]=shortToBytes(this.mask4,false)[1];
        sb[40]=shortToBytes(this.sbType,false)[0];
        sb[41]=shortToBytes(this.sbType,false)[1];
        sb[42]=this.macadr1;
        sb[43]=this.macadr2;
        sb[44]=this.macadr3;
        sb[45]=this.macadr4;
        sb[46]=this.macadr5;
        sb[47]=this.macadr6;
        return sb;
    }
    
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append(this.command).append("-")
    	.append(String.format("%1$d:%2$d-",bytesToshort(cmd,(byte)0,false),bytesToshort(dat,(byte)0,false)).toString())
    	.append(String.format("%1$03d:%2$02d:%3$02d:%4$02d:%5$02x:%6$03x-",sbh,luh,cuh,tdh,dch,can).toString())
    	.append(String.format("%1$03d.%2$03d.%3$03d.%4$03d-",ip1,ip2,ip3,ip4).toString())
    	.append(String.format("%1$03d.%2$03d.%3$03d.%4$03d-%5$d-",mask1,mask2,mask3,mask4,sbType).toString())
    	.append(String.format("%1$02x:%2$02x:%3$02x:%4$02x:%5$02x:%6$02x",bytesToshort(macadr1,(byte)0,false),
    			bytesToshort(macadr2,(byte)0,false),bytesToshort(macadr3,(byte)0,false),bytesToshort(macadr4,(byte)0,false),
    			bytesToshort(macadr5,(byte)0,false),bytesToshort(macadr6,(byte)0,false)).toString());
        return sb.toString();
    }
    
    public udpcmdtype(byte[] cmd) {
        this.command=new String(cmd).substring(0, 10);
        this.cmd=cmd[10];
        this.dat=cmd[11];
        this.sbh=bytesToshort(cmd[12],cmd[13],false);
        this.luh=bytesToshort(cmd[14],cmd[15],false);
        this.cuh=bytesToshort(cmd[16],cmd[17],false);
        this.tdh=bytesToshort(cmd[18],cmd[19],false);
        this.dch=bytesToshort(cmd[20],cmd[21],false);
        this.can=bytesToshort(cmd[22],cmd[23],false);
        this.ip1=bytesToshort(cmd[24],cmd[25],false);
        this.ip2=bytesToshort(cmd[26],cmd[27],false);
        this.ip3=bytesToshort(cmd[28],cmd[29],false);
        this.ip4=bytesToshort(cmd[30],cmd[31],false);
        this.mask1=bytesToshort(cmd[32],cmd[33],false);
        this.mask2=bytesToshort(cmd[34],cmd[35],false);
        this.mask3=bytesToshort(cmd[36],cmd[37],false);
        this.mask4=bytesToshort(cmd[38],cmd[39],false);
        this.sbType=bytesToshort(cmd[40],cmd[41],false);
        this.macadr1=cmd[42];
        this.macadr2=cmd[43];
        this.macadr3=cmd[44];
        this.macadr4=cmd[45];
        this.macadr5=cmd[46];
        this.macadr6=cmd[47];
    }

	/**
     * 将int类型的数据转换为byte数组
     * @param n int数据
     * @return 生成的byte数组
     */
    public byte[] shortToBytes(short s, boolean bBigEnding) {
    	byte[] buf = new byte[2];
    	if (bBigEnding)
    		for (int i = buf.length - 1; i >= 0; i--) {
    			buf[i] = (byte) (s & 0x00ff);
    			s >>= 8;
    		}
    	else
    		for (int i = 0; i < buf.length; i++) {
    			buf[i] = (byte) (s & 0x00ff);
    			s >>= 8;
    	}
    	return buf;
    }
    
    /**
     * 将byte数组转换为int数据
     * @param b 字节数组
     * @return 生成的int数据
     */
	public short bytesToshort(byte bufa, byte bufb, boolean bBigEnding){
		short r = 0;
		if (bBigEnding) {
			r <<= 8;
			r |= (bufa & 0x00ff);
			r <<= 8;
			r |= (bufb & 0x00ff);
		}
		else {
			r <<= 8;
			r |= (bufb & 0x00ff);
			r <<= 8;
			r |= (bufa & 0x00ff);
		}
		return r;
	}

    public String getIPaddr(){
		return String.format("%1$d.%2$d.%3$d.%4$d",ip1,ip2,ip3,ip4).toString();
    }
    
    public void setIPaddr(String ipaddr){
    	String temp[]=ipaddr.split("\\.");
    	for (int i=0;i<temp.length;i++){
    		switch(i){
    			case 0:
    				this.ip1=Short.parseShort(temp[i]);
    				break;
    			case 1:
    				this.ip2=Short.parseShort(temp[i]);
    				break;
    			case 2:
    				this.ip3=Short.parseShort(temp[i]);
    				break;
    			case 3:
    				this.ip4=Short.parseShort(temp[i]);
    				break;
    			default:
    				break;
    		}
    	}
    }
    
    public String getmaskaddr(){
		return String.format("%1$d.%2$d.%3$d.%4$d",mask1,mask2,mask3,mask4).toString();
    }
    
    public void setmaskaddr(String maskaddr){
    	String temp[]=maskaddr.split("\\.");
    	for (int i=0;i<temp.length;i++){
    		switch(i){
    			case 0:
    				this.mask1=Short.parseShort(temp[i]);
    				break;
    			case 1:
    				this.mask2=Short.parseShort(temp[i]);
    				break;
    			case 2:
    				this.mask3=Short.parseShort(temp[i]);
    				break;
    			case 3:
    				this.mask4=Short.parseShort(temp[i]);
    				break;
    			default:
    				break;
    		}
    	}
    }
    
    public String getmacaddr(){
		return String.format("%1$02x:%2$02x:%3$02x:%4$02x:%5$02x:%6$02x",bytesToshort(macadr1,(byte)0,false),
    			bytesToshort(macadr2,(byte)0,false),bytesToshort(macadr3,(byte)0,false),bytesToshort(macadr4,(byte)0,false),
    			bytesToshort(macadr5,(byte)0,false),bytesToshort(macadr6,(byte)0,false)).toString();
    }
    
    public void setmacaddr(String macaddr){
    	String temp[]=macaddr.split(":");
    	for (int i=0;i<temp.length;i++){
    		switch(i){
    			case 0:
    				this.macadr1=(byte)Short.parseShort(temp[i],16);
    				break;
    			case 1:
    				this.macadr2=(byte)Short.parseShort(temp[i],16);
    				break;
    			case 2:
    				this.macadr3=(byte)Short.parseShort(temp[i],16);
    				break;
    			case 3:
    				this.macadr4=(byte)Short.parseShort(temp[i],16);
    				break;
    			case 4:
    				this.macadr5=(byte)Short.parseShort(temp[i],16);
    				break;
    			case 5:
    				this.macadr6=(byte)Short.parseShort(temp[i],16);
    				break;
    			default:
    				break;
    		}
    	}
    }
    
    public String getCmd(){
		return this.command;
    }
    
    public String getDevicename(){
		return String.format("LCHB-%1$d-%2$d-%3$02d-%4$02d-%5$03d-%6$d-%7$d.%8$d.%9$d.%10$d",
				luh,cuh,tdh,dch,sbh,sbType,ip1,ip2,ip3,ip4).toString();
    }
    
    public String getdevicename(){
		return String.format("LCHB-%1$d-%2$d-%3$02d-%4$02d-%5$03d-%6$d",
				luh,cuh,tdh,dch,sbh,sbType).toString();
    }
    
    public void setDevicename(String devicename){
    	String temp[]=devicename.split("-");
    	for (int i=0;i<temp.length;i++){
    		switch(i){
    			case 0:
    				break;
    			case 1:
    				this.luh=Short.parseShort(temp[i]);
    				break;
    			case 2:
    				this.cuh=Short.parseShort(temp[i]);
    				break;
    			case 3:
    				this.tdh=Short.parseShort(temp[i]);
    				break;
    			case 4:
    				this.dch=Short.parseShort(temp[i]);
    				break;
    			case 5:
    				this.sbh=Short.parseShort(temp[i]);
    				break;
    			case 6:
    				this.sbType=Short.parseShort(temp[i]);
    				break;
    			default:
    				break;
    		}
    	}
    }
    
    public void setcmd(byte cmd){
    	this.cmd=cmd;
    }

}
