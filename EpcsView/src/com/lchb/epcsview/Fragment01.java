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
				.setTitle("�豸����")
				.setCancelable(false)
			    .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() { 
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
			    .setNegativeButton("ȡ��", new DialogInterface.OnClickListener() { 
			        @Override 
			        public void onClick(DialogInterface dialog, int which) {} 
			    });
				switch (v.getId()){
				case R.id.btnStart:
					dialog.setMessage("ȷ��Ҫ������?").show();
					break;
				case R.id.btnStop:
					dialog.setMessage("ȷ��Ҫͣ����?").show();
					break;
				case R.id.btnUp:
					dialog.setMessage("ȷ��Ҫ�ָ���?").show();
					break;
				case R.id.btnPause:
					dialog.setMessage("ȷ��Ҫ��ֹ��?").show();
					break;
				case R.id.btnDown:
					dialog.setMessage("ȷ��Ҫ��ѹ��?").show();
					break;
				case R.id.btnRstAlarm:
					dialog.setMessage("ȷ��Ҫ���������?").show();
					break;
				case R.id.btnReset:
					dialog.setMessage("ȷ��Ҫ�����豸��?").show();
					break;
				case R.id.btnSaveSetting:
					dialog.setMessage("ȷ��Ҫ����������?").show();
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
	        		.setTitle("�豸����")
	        		.setMessage("����ʧ��")
	        		.setCancelable(false)
	        		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() { 
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
			txt[0].setValue(String.valueOf(v40000[2])+"#¯"+String.valueOf(v40000[3])+"#������"+
					String.valueOf(v40000[4])+"#ͨ��"+String.valueOf(v40000[5])+"#�糡"+String.valueOf(v40000[1])+"#�豸");
			txt[0].settxtItem1("");txt[0].settxtItem2("");txt[0].settxtItem3("");txt[0].setindex(0);
			
			txt[1].setTitle("");txt[1].setisWrite(false);
			txt[1].settxtItem1("");txt[1].settxtItem2("");txt[1].settxtItem3("");txt[1].setindex(0);
			switch (v40000[15]){
			case 1:txt[1].setValue("����:��Ƶ����");break;
			case 2:txt[1].setValue("����:��Ƶ����");break;
			case 3:txt[1].setValue("����:��Ƶ��Դ");break;
			default:txt[1].setValue("����:δ֪�豸");break;}
			
			txt[2].setTitle("");txt[2].setisWrite(false);
			txt[2].settxtItem1("");txt[2].settxtItem2("");txt[2].settxtItem3("");txt[2].setindex(0);
			switch (v30000[24]){
			case 1:txt[2].setValue("����");break;
			case 2:txt[2].setValue("��ֹ");break;
			case 3:txt[2].setValue("�½�");break;
			default:txt[2].setValue("ͣ��");break;}
			
			txt[3].setTitle("");
			txt[3].settxtItem1("");txt[3].settxtItem2("");txt[3].settxtItem3("");txt[3].setindex(0);
			switch (v30000[23]){
			case 1:txt[3].setValue("ͬ������");break;
			case 2:txt[3].setValue("���뿪·");break;
			case 3:txt[3].setValue("�������");break;
			case 4:txt[3].setValue("�����·");break;
			case 5:txt[3].setValue("�����·");break;
			case 6:txt[3].setValue("���Ƿѹ");break;
			case 7:txt[3].setValue("ƫ����");break;
			case 8:txt[3].setValue("����˹");break;
			case 9:txt[3].setValue("����˹");break;
			case 10:txt[3].setValue("�����ź�");break;
			case 11:txt[3].setValue("��բ�ź�");break;
			case 12:txt[3].setValue("�ٽ�����");break;
			case 13:txt[3].setValue("Σ������");break;
			case 14:txt[3].setValue("CPU�쳣");break;
			case 15:txt[3].setValue("���಻ƽ��");break;
			case 16:txt[3].setValue("ͬ������");break;
			case 17:txt[3].setValue("�޶��ε���");break;
			case 18:txt[3].setValue("���ε����쳣");break;
			case 19:txt[3].setValue("ĸ�ߵ�ѹ��");break;
			case 20:txt[3].setValue("�Ӵ���δ�պ�");break;
			case 21:txt[3].setValue("�Ӵ���δ�Ͽ�");break;
			case 22:txt[3].setValue("��ȴ��·�쳣");break;
			case 23:txt[3].setValue("IGBT����");break;
            case 24:txt[3].setValue("ĸ�ߵ�ѹ��");break;
			default:txt[3].setValue("�豸����");break;}
			
			txt[4].setTitle("");txt[4].setisWrite(false);
			txt[4].settxtItem1("");txt[4].settxtItem2("");txt[4].settxtItem3("");txt[4].setindex(0);
			switch (v30000[22]){
			case 1:txt[4].setValue("��ͨ������");break;
			case 2:txt[4].setValue("����Ƶ������");break;
			case 3:txt[4].setValue("һ�ε�ѹ����");break;
			case 4:txt[4].setValue("һ�ε�������");break;
			case 5:txt[4].setValue("ĸ�ߵ�ѹ����");break;
			case 6:txt[4].setValue("г���������");break;
			case 7:txt[4].setValue("���ε�ѹ����");break;
			case 8:txt[4].setValue("���ε�������");break;
			case 9:txt[4].setValue("�糡��������");break;
			case 10:txt[4].setValue("��������");break;
			case 11:txt[4].setValue("IGBT�¶�����");break;
			case 12:txt[4].setValue("����迹����");break;
			default:txt[4].setValue("�޲�������");break;}
			
			txt[5].setTitle("");txt[5].setisWrite(false);
			txt[5].settxtItem1("");txt[5].settxtItem2("");txt[5].settxtItem3("");txt[5].setindex(0);
			switch (v30000[25]){
			case 1:txt[5].setValue("DCS����");break;
			case 2:txt[5].setValue("ͨѶ����");break;
			default:txt[5].setValue("�ֶ�����");break;}
			
			txt[6].setTitle("�������");txt[6].setValue("�������");
			txt[6].settxtItem1("");txt[6].settxtItem2("");txt[6].settxtItem3("");txt[6].setindex(9);
			if (v10000[9]){
				txt[6].setisWrite(true);
			}else{
				txt[6].setisWrite(false);
			}
			
			txt[7].setTitle("�������");txt[7].setisWrite(true);txt[7].setValue("�������");
			txt[7].settxtItem1("");txt[7].settxtItem2("");txt[7].settxtItem3("");txt[7].setindex(6);
			
			txt[8].setTitle("����");txt[8].setisWrite(true);txt[8].setValue("����");
			txt[8].settxtItem1("");txt[8].settxtItem2("");txt[8].settxtItem3("");txt[8].setindex(1);
			
			txt[9].setTitle("ͣ��");txt[9].setisWrite(true);txt[9].setValue("ͣ��");
			txt[9].settxtItem1("");txt[9].settxtItem2("");txt[9].settxtItem3("");txt[9].setindex(2);
			
			txt[10].setTitle("�ָ�");txt[10].setisWrite(true);txt[10].setValue("�ָ�");
			txt[10].settxtItem1("");txt[10].settxtItem2("");txt[10].settxtItem3("");txt[10].setindex(3);
			
			txt[11].setTitle("��ֹ");txt[11].setisWrite(true);txt[11].setValue("��ֹ");
			txt[11].settxtItem1("");txt[11].settxtItem2("");txt[11].settxtItem3("");txt[11].setindex(4);
			
			txt[12].setTitle("��ѹ");txt[12].setisWrite(true);txt[12].setValue("��ѹ");
			txt[12].settxtItem1("");txt[12].settxtItem2("");txt[12].settxtItem3("");txt[12].setindex(5);
			
			txt[13].setTitle("�����豸");txt[13].setisWrite(true);txt[13].setValue("����");
			txt[13].settxtItem1("");txt[13].settxtItem2("");txt[13].settxtItem3("");txt[13].setindex(6);
			
			txt[14].setTitle("����");txt[14].setisWrite(false);txt[14].setValue("����");
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
			txt.setTitle("");txt.setisWrite(false);txt.setValue("ʵʱֵ");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("����ֵ");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("��ֵ");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("�޶�ֵ");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("��ͨ��(��)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("��ͨ��ʵʱֵ");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[1]));
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
			txt.setTitle("��ͨ���޶�ֵ(��)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[40]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(40);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("һ�ε�ѹ(V)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("һ�ε�ѹʵʱֵ");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[3]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("һ�ε�ѹ����ֵ");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[13]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("һ�ε�ѹ�޶�ֵ(V)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[42]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(42);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("һ�ε���(A)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("һ�ε���ʵʱֵ");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[4]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("һ�ε�������ֵ");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[14]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("һ�ε����޶�ֵ(%)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[43])+"%");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(43);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("���ε�ѹ(kV)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("���ε�ѹʵʱֵ");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[7]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("���ε�ѹ����ֵ");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[17]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("���ε�ѹ��ֵ");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[9]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("���ε�ѹ�޶�ֵ(kV)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[46]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(46);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("���ε���(mA)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("���ε���ʵʱֵ");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[8]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("���ε�������ֵ");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[18]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("���ε�����ֵ");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[11]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("���ε����޶�ֵ(%)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[47])+"%");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(47);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("����(��)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("����ʵʱֵ");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[19]));
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
			txt.setTitle("�����޶�ֵ(��)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[48]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(48);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("����ģ����");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("����ģ����ʵʱֵ");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[20]));
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
			txt.setTitle("");txt.setisWrite(false);txt.setValue("����Ƶ��(spm)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("����Ƶ��ʵʱֵ");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[21]));
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
			txt.setTitle("����Ƶ���޶�ֵ(spm)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[49]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(49);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("�������(pls)");
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
			txt.setTitle("��������޶�ֵ(pls)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[50]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(50);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("����(pls)");
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
			txt.setTitle("�����޶�ֵ(pls)");txt.setisWrite(true);txt.setValue("1:"+String.valueOf(v40000[51]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(51);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("��ѹ����(kV)");
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
			txt.setTitle("��ѹ�����޶�ֵ(kV)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[54]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(54);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("��������(%)");
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
			txt.setTitle("���������޶�ֵ(%)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[55]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(55);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("�����½�(��)");
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
			txt.setTitle("�����½��޶�ֵ(��)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[56]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(56);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("ƫ����(%)");
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
			txt.setTitle("ƫ�����޶�ֵ(%)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[57]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(57);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("���Ƿѹ(kV)");
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
			txt.setTitle("���Ƿѹ�޶�ֵ(kV)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[58]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(58);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("�����·(kV)");
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
			txt.setTitle("�����·�޶�ֵ(kV)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[59]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(59);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("����ֵ");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("�ֵ");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("У׼ֵ");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("У׼ֵ");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("һ�ε�ѹ(V)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("һ�ε�ѹ����ֵ(V)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[17]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(17);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("һ�ε�ѹ�ֵ(V)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[34]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(34);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("һ�ε�ѹУ׼ֵ(��)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[24])+"��");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(24);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("һ�ε���(A)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("һ�ε�������ֵ(A)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[18]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(18);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("һ�ε����ֵ(A)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[35]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(35);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("һ�ε���У׼ֵ(��)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[25])+"��");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(25);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("���ε�ѹ(kV)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("���ε�ѹ����ֵ(kV)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[21]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(21);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("���ε�ѹ�ֵ(kV)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[38]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(38);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("���ε�ѹУ׼ֵ(��)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[28])+"��");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(28);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("���ε���(mA)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("���ε�������ֵ(mA)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[22]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(22);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("���ε����ֵ(mA)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[39]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(39);
			txtmap[2]=txt;
			
			txt=new txtItemType();
            if (v10000[35]==true){
                txt.setTitle("�������0-65535");txt.setisWrite(true);txt.setValue(String.valueOf(v30000[154]));
                txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(60);
            }
            else{
                txt.setTitle("");txt.setisWrite(false);txt.setValue("");
                txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
            }
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("���ε���У׼ֵ(��)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[29])+"��");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(29);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("����(��)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("��������ֵ(��)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[23]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(23);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("��������У׼ֵ(��)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[30])+"��");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(30);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("��������У׼ֵ(��)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[31])+"��");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(31);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("����ģ����");
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
			txt.setTitle("����ģ��������У׼ֵ(��)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[32])+"��");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(32);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("����ģ��������У׼ֵ(��)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[33])+"��");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(33);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("ͨ������");
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
			txt.setTitle("");txt.setisWrite(false);txt.setValue("�豸��");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("¯��");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[2])+"#¯");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(2);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("��������");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[3])+"#������");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(3);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("ͨ����");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[4])+"#ͨ��");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(4);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("�糡��");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[5])+"#�糡");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(5);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("�豸��");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("�豸��");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[1]));
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
			txt.setTitle("");txt.setisWrite(false);txt.setValue("CAN��ַ");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("CAN��ַ");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[6]));
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
			txt.setTitle("");txt.setisWrite(false);txt.setValue("IP��ַ");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("IP��ַ1");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[7]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(7);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("IP��ַ2");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[8]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(8);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("IP��ַ3");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[9]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(9);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("IP��ַ4");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[10]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(10);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("MASK��ַ");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("MASK��ַ1");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[11]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(11);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("MASK��ַ2");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[12]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(12);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("MASK��ַ3");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[13]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(13);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("MASK��ַ4");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[14]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(14);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("�豸����");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			switch (v40000[15]){
			case 1:
				txt.setValue("��Ƶ����");
				break;
			case 2:
				txt.setValue("��Ƶ����");
				break;
			case 3:
				txt.setValue("��Ƶ��Դ");
				break;
			default:
				txt.setValue("δ֪�豸");
				break;
			}
			txt.setTitle("�豸����");txt.setisWrite(true);
			txt.settxtItem1("��Ƶ����");txt.settxtItem2("��Ƶ����");txt.settxtItem3("��Ƶ��Դ");txt.setindex(15);
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
			txt.setTitle("");txt.setisWrite(false);txt.setValue("DCS���Ʒ�ʽ");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			switch (v40000[16]){
			case 1:
				txt.setValue("DCS����");
				break;
			default:
				txt.setValue("ͨѶ����");
				break;
			}
			txt.setTitle("DCS���Ʒ�ʽ");txt.setisWrite(true);
			txt.settxtItem1("ͨѶ����");txt.settxtItem2("DCS����");txt.settxtItem3("");txt.setindex(16);
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
			txt.setTitle("���������");txt.setisWrite(true);txt.setValue("���������");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(8);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("ʵʱֵ");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("����ֵ");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("��ֵ");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("�޶�ֵ");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("����Ƶ��(Hz)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("����Ƶ��ʵʱֵ");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[2]));
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
			txt.setTitle("����Ƶ���޶�ֵ(Hz)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[41]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(41);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("ĸ�ߵ�ѹ(V)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("ĸ�ߵ�ѹʵʱֵ");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[5]));
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
			txt.setTitle("ĸ�ߵ�ѹ�޶�ֵ(V)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[44]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(44);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("һ�ε���(A)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("һ�ε���ʵʱֵ");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[4]));
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
			txt.setTitle("һ�ε����޶�ֵ(%)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[43])+"%");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(43);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("���ε�ѹ(kV)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("���ε�ѹʵʱֵ");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[7]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("���ε�ѹ����ֵ");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[17]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("���ε�ѹ��ֵ");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[9]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("���ε�ѹ�޶�ֵ(kV)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[46]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(46);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("���ε���(mA)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("���ε���ʵʱֵ");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[8]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("���ε�������ֵ");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[18]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("���ε�����ֵ");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[11]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("���ε����޶�ֵ(%)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[47])+"%");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(47);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("����(��)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("����ʵʱֵ");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[19]));
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
			txt.setTitle("�����޶�ֵ(��)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[48]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(48);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("����Ƶ��(spm)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("����Ƶ��ʵʱֵ");txt.setisWrite(false);txt.setValue(String.valueOf(v30000[21]));
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
			txt.setTitle("����Ƶ���޶�ֵ(spm)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[49]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(49);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("�������(ms)");
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
			txt.setTitle("��������޶�ֵ(ms)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[50]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(50);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("����(ms)");
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
			txt.setTitle("���ȸ����޶�ֵ(ms)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[52]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(52);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("���ȵ����޶�ֵ(ms)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[53]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(53);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("�����½�(%)");
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
			txt.setTitle("�����½��޶�ֵ(%)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[56]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(56);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("�����ж�(%)");
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
			txt.setTitle("�����ж��޶�ֵ(%)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[57]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(57);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("���Ƿѹ(kV)");
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
			txt.setTitle("���Ƿѹ�޶�ֵ(kV)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[58]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(58);
			txtmap[4]=txt;
			
			map.add(txtmap);

            txtmap=new txtItemType[5];

            txt=new txtItemType();
            txt.setTitle("");txt.setisWrite(false);txt.setValue("�����·(kV)");
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
            txt.setTitle("�����·�޶�ֵ(kV)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[59]));
            txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(59);
            txtmap[4]=txt;

            map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("����ֵ");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("�ֵ");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("У׼ֵ");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("У׼ֵ");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("ĸ�ߵ�ѹ(V)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("ĸ�ߵ�ѹ����ֵ(V)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[19]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(19);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("ĸ�ߵ�ѹ�ֵ(V)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[36]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(36);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("ĸ�ߵ�ѹУ׼ֵ(��)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[26])+"��");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(26);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("һ�ε���(A)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("һ�ε�������ֵ(A)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[18]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(18);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("һ�ε����ֵ(A)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[35]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(35);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("һ�ε���У׼ֵ(��)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[25])+"��");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(25);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("���ε�ѹ(kV)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("���ε�ѹ����ֵ(kV)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[21]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(21);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("���ε�ѹ�ֵ(kV)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[38]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(38);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("���ε�ѹУ׼ֵ(��)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[28])+"��");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(28);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("���ε���(mA)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("���ε�������ֵ(mA)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[22]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(22);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("���ε����ֵ(mA)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[39]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(39);
			txtmap[2]=txt;
			
			txt=new txtItemType();
            if (v10000[35]==true){
                txt.setTitle("�������0-65535");txt.setisWrite(true);txt.setValue(String.valueOf(v30000[154]));
                txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(60);
            }
            else{
                txt.setTitle("");txt.setisWrite(false);txt.setValue("");
                txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
            }
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("���ε���У׼ֵ(��)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[29])+"��");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(29);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("����(��)");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("��������ֵ(��)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[23]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(23);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("��������У׼ֵ(��)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[30])+"��");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(30);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("��������У׼ֵ(��)");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[31])+"��");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(31);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("ͨ������");
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
			txt.setTitle("");txt.setisWrite(false);txt.setValue("�豸��");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("¯��");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[2])+"#¯");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(2);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("��������");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[3])+"#������");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(3);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("ͨ����");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[4])+"#ͨ��");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(4);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("�糡��");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[5])+"#�糡");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(5);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("�豸��");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("�豸��");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[1]));
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
			txt.setTitle("");txt.setisWrite(false);txt.setValue("CAN��ַ");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("CAN��ַ");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[6]));
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
			txt.setTitle("");txt.setisWrite(false);txt.setValue("IP��ַ");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("IP��ַ1");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[7]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(7);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("IP��ַ2");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[8]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(8);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("IP��ַ3");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[9]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(9);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("IP��ַ4");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[10]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(10);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("MASK��ַ");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			txt.setTitle("MASK��ַ1");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[11]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(11);
			txtmap[1]=txt;
			
			txt=new txtItemType();
			txt.setTitle("MASK��ַ2");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[12]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(12);
			txtmap[2]=txt;
			
			txt=new txtItemType();
			txt.setTitle("MASK��ַ3");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[13]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(13);
			txtmap[3]=txt;
			
			txt=new txtItemType();
			txt.setTitle("MASK��ַ4");txt.setisWrite(true);txt.setValue(String.valueOf(v40000[14]));
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(14);
			txtmap[4]=txt;
			
			map.add(txtmap);

			txtmap=new txtItemType[5];
			
			txt=new txtItemType();
			txt.setTitle("");txt.setisWrite(false);txt.setValue("�豸����");
			txt.settxtItem1("");txt.settxtItem2("");txt.settxtItem3("");txt.setindex(0);
			txtmap[0]=txt;
			
			txt=new txtItemType();
			switch (v40000[15]){
			case 1:
				txt.setValue("��Ƶ����");
				break;
			case 2:
				txt.setValue("��Ƶ����");
				break;
			case 3:
				txt.setValue("��Ƶ��Դ");
				break;
			default:
				txt.setValue("δ֪�豸");
				break;
			}
			txt.setTitle("�豸����");txt.setisWrite(true);
			txt.settxtItem1("��Ƶ����");txt.settxtItem2("��Ƶ����");txt.settxtItem3("��Ƶ��Դ");txt.setindex(15);
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
