package com.lchb.epcsview;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public class Fragment01 extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private ListView gridview;
		private TextView devicename;
		private TextView devicetype;
		private Button btnstart;
		private Button btnstop;
		private Button btnreset;
		private Button btndown;
		private Button btnpause;
		private Button btnup;
		private Button btnrstalarm;
		private Button btnsavesetting;
		private TextView txtStatus;
		private TextView txtAlarm;
		private TextView txtRemote;
		private TextView txtLimit;
		private List<txtItemType[]> listitem;
		private txtItemType[] updateviewdate;
		private datelistAdapter saitems;
		private GlobalVar globalvar;
		private boolean[] v10000;
		private short[] v30000;
		private short[] v40000;
		private boolean mRunning = false;
		private static final int POPKEYBORAD=0;
	    private static final int UPDATELIST=1;
	    private static final int UPDATELISTVIEW=2;
	    private static final int WRITEERROR=3;

		public Fragment01() {
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment01, container,false);
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
			mRunning = false;
		}
		
		public void init(){
			globalvar=(GlobalVar)getActivity().getApplication();
			gridview=(ListView)getActivity().findViewById(R.id.gridView);
			devicename=(TextView)getActivity().findViewById(R.id.txtDeviceName);
			devicetype=(TextView)getActivity().findViewById(R.id.txtDeviceType);
			btnstart=(Button)getActivity().findViewById(R.id.btnStart);
			btnstop=(Button)getActivity().findViewById(R.id.btnStop);
			btnreset=(Button)getActivity().findViewById(R.id.btnReset);
			btndown=(Button)getActivity().findViewById(R.id.btnDown);
			btnpause=(Button)getActivity().findViewById(R.id.btnPause);
			btnup=(Button)getActivity().findViewById(R.id.btnUp);
			btnrstalarm=(Button)getActivity().findViewById(R.id.btnRstAlarm);
			btnsavesetting=(Button)getActivity().findViewById(R.id.btnSaveSetting);
			btnstart.setOnClickListener(listener);
			btnstop.setOnClickListener(listener);
			btnreset.setOnClickListener(listener);
			btndown.setOnClickListener(listener);
			btnpause.setOnClickListener(listener);
			btnup.setOnClickListener(listener);
			btnrstalarm.setOnClickListener(listener);
			btnsavesetting.setOnClickListener(listener);
			txtStatus=(TextView)getActivity().findViewById(R.id.txtStatus);
			txtAlarm=(TextView)getActivity().findViewById(R.id.txtAlarm);
			txtRemote=(TextView)getActivity().findViewById(R.id.txtRemote);
			txtLimit=(TextView)getActivity().findViewById(R.id.txtLimit);
			v40000=globalvar.getv40000();
			if (v40000[15]==1||v40000[15]==2)
				listitem=updatetxthv();
			else
				listitem=updatetxthf();
            saitems=new datelistAdapter(getActivity(),listitem,R.layout.listitem5,mHandler);
            saitems.setListView(gridview);
            gridview.setAdapter(saitems);
            mRunning=true;
            new updateview().start();
		}
		
		OnClickListener listener=new OnClickListener(){
			public void onClick(final View v){
				final Builder dialog=new Builder(getActivity())
				.setTitle("设备操作")
				.setCancelable(false)
			    .setPositiveButton("确定", new DialogInterface.OnClickListener() { 
			        @Override 
			        public void onClick(DialogInterface dialog, int which) {
						switch (v.getId()){
						case R.id.btnStart:
					    	new mbWriteThread(getActivity(), mHandler, 0, 0, 1).start();
							break;
						case R.id.btnStop:
					    	new mbWriteThread(getActivity(), mHandler, 0, 1, 1).start();
							break;
						case R.id.btnUp:
					    	new mbWriteThread(getActivity(), mHandler, 0, 2, 1).start();
							break;
						case R.id.btnPause:
					    	new mbWriteThread(getActivity(), mHandler, 0, 3, 1).start();
							break;
						case R.id.btnDown:
					    	new mbWriteThread(getActivity(), mHandler, 0, 4, 1).start();
							break;
						case R.id.btnRstAlarm:
					    	new mbWriteThread(getActivity(), mHandler, 0, 5, 1).start();
							break;
						case R.id.btnReset:
					    	new mbWriteThread(getActivity(), mHandler, 0, 6, 1).start();
							break;
						case R.id.btnSaveSetting:
					    	new mbWriteThread(getActivity(), mHandler, 0, 9, 1).start();
							break;
						default:
							break;
						}
			        } 
			    }) 
			    .setNegativeButton("取消", new DialogInterface.OnClickListener() { 
			        @Override 
			        public void onClick(DialogInterface dialog, int which) {} 
			    });
				switch (v.getId()){
				case R.id.btnStart:
					dialog.setMessage("确定要开机吗?").show();
					break;
				case R.id.btnStop:
					dialog.setMessage("确定要停机吗?").show();
					break;
				case R.id.btnUp:
					dialog.setMessage("确定要恢复吗?").show();
					break;
				case R.id.btnPause:
					dialog.setMessage("确定要中止吗?").show();
					break;
				case R.id.btnDown:
					dialog.setMessage("确定要降压吗?").show();
					break;
				case R.id.btnRstAlarm:
					dialog.setMessage("确定要清除故障吗?").show();
					break;
				case R.id.btnReset:
					dialog.setMessage("确定要重启设备吗?").show();
					break;
				case R.id.btnSaveSetting:
					dialog.setMessage("确定要保存数据吗?").show();
					break;
				default:
					break;
				}
	        }
		};
		
		@SuppressLint("HandlerLeak")
		Handler mHandler = new Handler() {
	        @Override  
	        public void handleMessage(Message msg) {
	        	switch (msg.what){
	        	case POPKEYBORAD:
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	        		break;
	        	case UPDATELIST:
	        		updateview(updateviewdate);
	        		break;
	        	case UPDATELISTVIEW:
	        		saitems.refresh(listitem);
	        		break;
	        	case WRITEERROR:
	        		new Builder(getActivity())
	        		.setTitle("设备操作")
	        		.setMessage("操作失败")
	        		.setCancelable(false)
	        		.setPositiveButton("确定", new DialogInterface.OnClickListener() { 
	        			@Override 
	        			public void onClick(DialogInterface dialog, int which) {
			        } 
	        		}).show();
	        		break;
	        	default:
	        		break;
	        	}
	            super.handleMessage(msg);
	        }  
	    };
	    
	    class updateview extends Thread{  
            @Override  
            public void run() {
            	while(mRunning){
            		try {
            			Thread.sleep(500);
            		} catch (InterruptedException e) {
            			e.printStackTrace();
            		}
            		List<txtItemType[]> list;
	    			v40000=globalvar.getv40000();
	    			if (v40000[15]==1||v40000[15]==2)
	    				list=updatetxthv();
	    			else
	    				list=updatetxthf();
	    			if (listitem.size()==list.size()){
	            		Message msg2 = saitems.getHandler().obtainMessage();
	            		msg2.obj = list;
	            		msg2.sendToTarget();
	    			}
	    			else{
	    				listitem=list;
	            		Message msg3 = mHandler.obtainMessage();
	            		msg3.what = UPDATELISTVIEW;
	            		msg3.sendToTarget();
	    			}
            		Message msg1 = mHandler.obtainMessage();
            		msg1.what = UPDATELIST;
            		msg1.sendToTarget();
	    			updateviewdate=updatetxtview();
            	}
            }
        }
	    
	    public void updateview(txtItemType[] txt){
			v10000=globalvar.getv10000();
			v30000=globalvar.getv30000();
			v40000=globalvar.getv40000();
			
			devicename.setTag(txt[0]);
			devicename.setText(txt[0].getValue());
			
			devicetype.setTag(txt[1]);
			devicetype.setText(txt[1].getValue());
			
			switch (v30000[24]){
			case 1:txtStatus.setBackgroundColor(Color.RED);break;
			case 2:txtStatus.setBackgroundColor(Color.RED);break;
			case 3:txtStatus.setBackgroundColor(Color.RED);break;
			default:txtStatus.setBackgroundColor(Color.GREEN);break;}
			txtStatus.setTag(txt[2]);
			txtStatus.setText(txt[2].getValue());
			
			if (v30000[23]!=0){
				if ((txtItemType)txtAlarm.getTag()!=null){
					if (((txtItemType)txtAlarm.getTag()).isWrite()){
						txtAlarm.setBackgroundColor(Color.YELLOW);
						txt[3].setisWrite(false);
					}
					else{
						txtAlarm.setBackgroundColor(Color.WHITE);
						txt[3].setisWrite(true);
					}
				}
				else{
					txtAlarm.setBackgroundColor(Color.YELLOW);
					txt[3].setisWrite(false);
				}
			}
			else{
				txtAlarm.setBackgroundColor(Color.WHITE);
				txt[3].setisWrite(false);
			}
			txtAlarm.setTag(txt[3]);
			txtAlarm.setText(txt[3].getValue());
			
			txtLimit.setTag(txt[4]);
			txtLimit.setText(txt[4].getValue());
			
			txtRemote.setTag(txt[5]);
			txtRemote.setText(txt[5].getValue());
			
			txtRemote.setTag(txt[5]);
			txtRemote.setText(txt[5].getValue());
			
			btnsavesetting.setTag(txt[6]);
			btnsavesetting.setEnabled(txt[6].isWrite);
			btnrstalarm.setTag(txt[7]);
			btnstart.setTag(txt[8]);
			btnstop.setTag(txt[9]);
			btnup.setTag(txt[10]);
			btnpause.setTag(txt[11]);
			btndown.setTag(txt[12]);
			btnreset.setTag(txt[13]);
	    	
	    }
	    
	    public txtItemType[] updatetxtview(){
			txtItemType[] txt=new txtItemType[15];
			for (int i=0;i<txt.length;i++){
				txt[i]=new txtItemType();
			}
			v10000=globalvar.getv10000();
			v30000=globalvar.getv30000();
			v40000=globalvar.getv40000();
			
			txt[0].setTitle("");txt[0].setisWrite(false);
			txt[0].setValue(String.valueOf(v40000[2])+"#炉"+String.valueOf(v40000[3])+"#除尘器"+
					String.valueOf(v40000[4])+"#通道"+String.valueOf(v40000[5])+"#电场"+String.valueOf(v40000[1])+"#设备");
			txt[0].settxtItem1("");txt[0].settxtItem2("");txt[0].settxtItem3("");txt[0].setindex(0);
			
			txt[1].setTitle("");txt[1].setisWrite(false);
			txt[1].settxtItem1("");txt[1].settxtItem2("");txt[1].settxtItem3("");txt[1].setindex(0);
			switch (v40000[15]){
			case 1:txt[1].setValue("类型:工频单相");break;
			case 2:txt[1].setValue("类型:工频三相");break;
			case 3:txt[1].setValue("类型:高频电源");break;
			default:txt[1].setValue("类型:未知设备");break;}
			
			txt[2].setTitle("");txt[2].setisWrite(false);
			txt[2].settxtItem1("");txt[2].settxtItem2("");txt[2].settxtItem3("");txt[2].setindex(0);
			switch (v30000[24]){
			case 1:txt[2].setValue("运行");break;
			case 2:txt[2].setValue("中止");break;
			case 3:txt[2].setValue("下降");break;
			default:txt[2].setValue("停机");break;}
			
			txt[3].setTitle("");
			txt[3].settxtItem1("");txt[3].settxtItem2("");txt[3].settxtItem3("");txt[3].setindex(0);
			switch (v30000[23]){
			case 1:txt[3].setValue("同步故障");break;
			case 2:txt[3].setValue("输入开路");break;
			case 3:txt[3].setValue("输入过流");break;
			case 4:txt[3].setValue("输出开路");break;
			case 5:txt[3].setValue("输出短路");break;
			case 6:txt[3].setValue("输出欠压");break;
			case 7:txt[3].setValue("偏励磁");break;
			case 8:txt[3].setValue("轻瓦斯");break;
			case 9:txt[3].setValue("重瓦斯");break;
			case 10:txt[3].setValue("报警信号");break;
			case 11:txt[3].setValue("跳闸信号");break;
			case 12:txt[3].setValue("临界油温");break;
			case 13:txt[3].setValue("危险油温");break;
			case 14:txt[3].setValue("CPU异常");break;
			case 15:txt[3].setValue("三相不平衡");break;
			case 16:txt[3].setValue("同步干扰");break;
			case 17:txt[3].setValue("无二次电流");break;
			case 18:txt[3].setValue("二次电流异常");break;
			case 19:txt[3].setValue("母线电压低");break;
			case 20:txt[3].setValue("接触器未闭合");break;
			case 21:txt[3].setValue("接触器未断开");break;
			case 22:txt[3].setValue("冷却回路异常");break;
			case 23:txt[3].setValue("IGBT故障");break;
            case 24:txt[3].setValue("母线电压高");break;
			default:txt[3].setValue("设备正常");break;}
			
			txt[4].setTitle("");txt[4].setisWrite(false);
			txt[4].settxtItem1("");txt[4].settxtItem2("");txt[4].settxtItem3("");txt[4].setindex(0);
			switch (v30000[22]){
			case 1:txt[4].setValue("导通角限制");break;
			case 2:txt[4].setValue("控制频率限制");break;
			case 3:txt[4].setValue("一次电压限制");break;
			case 4:txt[4].setValue("一次电流限制");break;
			case 5:txt[4].setValue("母线电压限制");break;
			case 6:txt[4].setValue("谐振电流限制");break;
			case 7:txt[4].setValue("二次电压限制");break;
			case 8:txt[4].setValue("二次电流限制");break;
			case 9:txt[4].setValue("电场闪络限制");break;
			case 10:txt[4].setValue("油温限制");break;
			case 11:txt[4].setValue("IGBT温度限制");break;
			case 12:txt[4].setValue("输出阻抗限制");break;
			default:txt[4].setValue("无参数限制");break;}
			
			txt[5].setTitle("");txt[5].setisWrite(false);
			txt[5].settxtItem1("");txt[5].settxtItem2("");txt[5].settxtItem3("");txt[5].setindex(0);
			switch (v30000[25]){
			case 1:txt[5].setValue("DCS控制");break;
			case 2:txt[5].setValue("通讯控制");break;
			default:txt[5].setValue("手动控制");break;}
			
			txt[6].setTitle("保存参数");txt[6].setValue("保存参数");
			txt[6].settxtItem1("");txt[6].settxtItem2("");txt[6].settxtItem3("");txt[6].setindex(9);
			if (v10000[9]){
				txt[6].setisWrite(true);
			}else{
				txt[6].setisWrite(false);
			}
			
			txt[7].setTitle("清除故障");txt[7].setisWrite(true);txt[7].setValue("清除故障");
			txt[7].settxtItem1("");txt[7].settxtItem2("");txt[7].settxtItem3("");txt[7].setindex(6);
			
			txt[8].setTitle("开机");txt[8].setisWrite(true);txt[8].setValue("开机");
			txt[8].settxtItem1("");txt[8].settxtItem2("");txt[8].settxtItem3("");txt[8].setindex(1);
			
			txt[9].setTitle("停机");txt[9].setisWrite(true);txt[9].setValue("停机");
			txt[9].settxtItem1("");txt[9].settxtItem2("");txt[9].settxtItem3("");txt[9].setindex(2);
			
			txt[10].setTitle("恢复");txt[10].setisWrite(true);txt[10].setValue("恢复");
			txt[10].settxtItem1("");txt[10].settxtItem2("");txt[10].settxtItem3("");txt[10].setindex(3);
			
			txt[11].setTitle("中止");txt[11].setisWrite(true);txt[11].setValue("中止");
			txt[11].settxtItem1("");txt[11].settxtItem2("");txt[11].settxtItem3("");txt[11].setindex(4);
			
			txt[12].setTitle("降压");txt[12].setisWrite(true);txt[12].setValue("降压");
			txt[12].settxtItem1("");txt[12].settxtItem2("");txt[12].settxtItem3("");txt[12].setindex(5);
			
			txt[13].setTitle("重启设备");txt[13].setisWrite(true);txt[13].setValue("重启");
			txt[13].settxtItem1("");txt[13].settxtItem2("");txt[13].settxtItem3("");txt[13].setindex(6);
			
			txt[14].setTitle("保留");txt[14].setisWrite(false);txt[14].setValue("保留");
			txt[14].settxtItem1("");txt[14].settxtItem2("");txt[14].settxtItem3("");txt[14].setindex(0);
			
			return txt;
	    }
	    
	    public List<txtItemType[]> updatetxthv(){
			List<txtItemType[]> map=new ArrayList<txtItemType[]>();
			txtItemType[] txtmap;
			txtItemType txt;
			v10000=globalvar.getv10000();
			v30000=globalvar.getv30000();
			v40000=globalvar.getv40000();
			map.clear();

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("实时值");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("脉冲值");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("峰值");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("限定值");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("导通角(°)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("导通角实时值");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[1]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("导通角限定值(°)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[40]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(40);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("一次电压(V)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("一次电压实时值");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[3]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("一次电压脉冲值");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[13]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("一次电压限定值(V)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[42]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(42);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("一次电流(A)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("一次电流实时值");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[4]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("一次电流脉冲值");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[14]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("一次电流限定值(%)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[43])+"%");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(43);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("二次电压(kV)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("二次电压实时值");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[7]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("二次电压脉冲值");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[17]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("二次电压峰值");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[9]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("二次电压限定值(kV)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[46]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(46);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("二次电流(mA)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("二次电流实时值");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[8]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("二次电流脉冲值");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[18]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("二次电流峰值");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[11]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("二次电流限定值(%)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[47])+"%");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(47);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("油温(℃)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("油温实时值");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[19]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("油温限定值(℃)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[48]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(48);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("辅助模拟量");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("辅助模拟量实时值");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[20]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(true);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("闪络频率(spm)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("闪络频率实时值");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[21]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("闪络频率限定值(spm)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[49]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(49);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("闪络封锁(pls)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("闪络封锁限定值(pls)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[50]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(50);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("充电比(pls)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("充电比限定值(pls)");txt.setisWrite(true);txt.setValue("1:"+String.valueOf(v40000[51]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(51);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("电压闪络(kV)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("电压闪络限定值(kV)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[54]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(54);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("电流闪络(%)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("电流闪络限定值(%)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[55]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(55);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("闪络下降(°)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("闪络下降限定值(°)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[56]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(56);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("偏励磁(%)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("偏励磁限定值(%)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[57]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(57);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("输出欠压(kV)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("输出欠压限定值(kV)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[58]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(58);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("输出开路(kV)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("输出开路限定值(kV)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[59]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(59);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("量程值");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("额定值");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("校准值");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("校准值");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("一次电压(V)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("一次电压量程值(V)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[17]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(17);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("一次电压额定值(V)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[34]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(34);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("一次电压校准值(‰)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[24])+"‰");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(24);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("一次电流(A)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("一次电流量程值(A)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[18]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(18);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("一次电流额定值(A)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[35]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(35);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("一次电流校准值(‰)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[25])+"‰");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(25);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("二次电压(kV)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("二次电压量程值(kV)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[21]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(21);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("二次电压额定值(kV)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[38]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(38);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("二次电压校准值(‰)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[28])+"‰");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(28);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("二次电流(mA)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("二次电流量程值(mA)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[22]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(22);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("二次电流额定值(mA)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[39]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(39);
			txtmap[2]=txt;
			
			txt=new txtItemType();
            if (v10000[35]==true){
                txt.setTitle("解除代码0-65535");txt.setisWrite(true);txt.setValue(String.valueOf(v30000[154]));
                txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(60);
            }
            else{
                txt.setTitle("");txt.setisWrite(false);txt.setValue("");
                txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
            }
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("二次电流校准值(‰)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[29])+"‰");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(29);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("油温(℃)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("油温量程值(℃)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[23]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(23);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("油温下限校准值(‰)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[30])+"‰");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(30);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("油温上限校准值(‰)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[31])+"‰");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(31);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("辅助模拟量");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("辅助模拟量下限校准值(‰)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[32])+"‰");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(32);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("辅助模拟量上限校准值(‰)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[33])+"‰");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(33);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("通信设置");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("设备名");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("炉号");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[2])+"#炉");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(2);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("除尘器号");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[3])+"#除尘器");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(3);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("通道号");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[4])+"#通道");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(4);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("电场号");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[5])+"#电场");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(5);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("设备号");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("设备号");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[1]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(1);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("CAN地址");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("CAN地址");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[6]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(6);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("IP地址");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("IP地址1");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[7]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(7);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("IP地址2");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[8]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(8);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("IP地址3");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[9]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(9);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("IP地址4");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[10]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(10);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("MASK地址");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("MASK地址1");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[11]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(11);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("MASK地址2");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[12]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(12);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("MASK地址3");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[13]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(13);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("MASK地址4");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[14]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(14);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("设备类型");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			switch (v40000[15]){
			case 1:
				txt.setValue("工频单相");
				break;
			case 2:
				txt.setValue("工频三相");
				break;
			case 3:
				txt.setValue("高频电源");
				break;
			default:
				txt.setValue("未知设备");
				break;
			}
			txt.setTitle("设备类型");txt.setisWrite(true);
			txt.settxtItem1("工频单相");txt.settxtItem2("工频三相");txt.settxtItem3("高频电源");txt.setindex(15);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("DCS控制方式");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			switch (v40000[16]){
			case 1:
				txt.setValue("DCS控制");
				break;
			default:
				txt.setValue("通讯控制");
				break;
			}
			txt.setTitle("DCS控制方式");txt.setisWrite(true);
			txt.settxtItem1("通讯控制");txt.settxtItem2("DCS控制");txt.settxtItem3("");txt.setindex(16);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[4]=txt;
			
			map.add(txtmap);
			
			return map;
	    }
	    
	    public List<txtItemType[]> updatetxthf(){
			List<txtItemType[]> map=new ArrayList<txtItemType[]>();
			txtItemType[] txtmap;
			txtItemType txt;
			v10000=globalvar.getv10000();
			v30000=globalvar.getv30000();
			v40000=globalvar.getv40000();
			map.clear();

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("单脉冲测试");txt.setisWrite(true);txt.setValue("单脉冲测试");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(8);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("实时值");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("脉冲值");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("峰值");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("限定值");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("控制频率(Hz)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("控制频率实时值");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[2]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("控制频率限定值(Hz)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[41]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(41);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("母线电压(V)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("母线电压实时值");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[5]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("母线电压限定值(V)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[44]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(44);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("一次电流(A)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("一次电流实时值");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[4]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("一次电流限定值(%)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[43])+"%");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(43);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("二次电压(kV)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("二次电压实时值");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[7]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("二次电压脉冲值");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[17]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("二次电压峰值");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[9]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("二次电压限定值(kV)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[46]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(46);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("二次电流(mA)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("二次电流实时值");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[8]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("二次电流脉冲值");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[18]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("二次电流峰值");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[11]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("二次电流限定值(%)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[47])+"%");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(47);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("油温(℃)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("油温实时值");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[19]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("油温限定值(℃)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[48]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(48);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("闪络频率(spm)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("闪络频率实时值");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[21]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("闪络频率限定值(spm)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[49]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(49);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("闪络封锁(ms)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("闪络封锁限定值(ms)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[50]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(50);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("充电比(ms)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("充电比高能限定值(ms)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[52]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(52);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("充电比低能限定值(ms)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[53]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(53);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("闪络下降(%)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("闪络下降限定值(%)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[56]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(56);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("闪络判断(%)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("闪络判断限定值(%)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[57]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(57);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("输出欠压(kV)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("输出欠压限定值(kV)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[58]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(58);
			txtmap[4]=txt;
			
			map.add(txtmap);

            txtmap=new txtItemType[5];

            txt=new txtItemType();
            txt.setTitle("");txt.setisWrite(false);txt.setValue("输出开路(kV)");
            txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
            txtmap[0]=txt;

            txt=new txtItemType();
            txt.setTitle("");txt.setisWrite(false);txt.setValue("");
            txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
            txtmap[1]=txt;

            txt=new txtItemType();
            txt.setTitle("");txt.setisWrite(false);txt.setValue("");
            txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
            txtmap[2]=txt;

            txt=new txtItemType();
            txt.setTitle("");txt.setisWrite(false);txt.setValue("");
            txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
            txtmap[3]=txt;

            txt=new txtItemType();
            txt.setTitle("输出开路限定值(kV)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[59]));
            txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(59);
            txtmap[4]=txt;

            map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("量程值");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("额定值");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("校准值");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("校准值");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("母线电压(V)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("母线电压量程值(V)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[19]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(19);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("母线电压额定值(V)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[36]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(36);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("母线电压校准值(‰)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[26])+"‰");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(26);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("一次电流(A)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("一次电流量程值(A)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[18]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(18);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("一次电流额定值(A)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[35]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(35);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("一次电流校准值(‰)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[25])+"‰");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(25);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("二次电压(kV)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("二次电压量程值(kV)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[21]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(21);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("二次电压额定值(kV)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[38]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(38);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("二次电压校准值(‰)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[28])+"‰");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(28);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("二次电流(mA)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("二次电流量程值(mA)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[22]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(22);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("二次电流额定值(mA)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[39]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(39);
			txtmap[2]=txt;
			
			txt=new txtItemType();
            if (v10000[35]==true){
                txt.setTitle("解除代码0-65535");txt.setisWrite(true);txt.setValue(String.valueOf(v30000[154]));
                txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(60);
            }
            else{
                txt.setTitle("");txt.setisWrite(false);txt.setValue("");
                txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
            }
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("二次电流校准值(‰)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[29])+"‰");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(29);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("油温(℃)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("油温量程值(℃)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[23]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(23);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("油温下限校准值(‰)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[30])+"‰");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(30);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("油温上限校准值(‰)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[31])+"‰");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(31);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("通信设置");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("设备名");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("炉号");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[2])+"#炉");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(2);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("除尘器号");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[3])+"#除尘器");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(3);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("通道号");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[4])+"#通道");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(4);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("电场号");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[5])+"#电场");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(5);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("设备号");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("设备号");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[1]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(1);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("CAN地址");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("CAN地址");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[6]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(6);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("IP地址");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("IP地址1");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[7]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(7);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("IP地址2");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[8]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(8);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("IP地址3");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[9]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(9);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("IP地址4");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[10]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(10);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("MASK地址");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("MASK地址1");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[11]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(11);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("MASK地址2");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[12]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(12);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("MASK地址3");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[13]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(13);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("MASK地址4");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[14]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(14);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("设备类型");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			switch (v40000[15]){
			case 1:
				txt.setValue("工频单相");
				break;
			case 2:
				txt.setValue("工频三相");
				break;
			case 3:
				txt.setValue("高频电源");
				break;
			default:
				txt.setValue("未知设备");
				break;
			}
			txt.setTitle("设备类型");txt.setisWrite(true);
			txt.settxtItem1("工频单相");txt.settxtItem2("工频三相");txt.settxtItem3("高频电源");txt.setindex(15);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[4]=txt;
			
			map.add(txtmap);
			
			return map;
	    }
				
	}
