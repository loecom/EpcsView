package com.lchb.epcsview;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;  
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.Gravity;
import android.view.LayoutInflater;  
import android.view.View;  
import android.view.View.OnClickListener;
import android.view.ViewGroup;  
import android.widget.BaseAdapter;  
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

public class datelistAdapter extends BaseAdapter {  
	  
    private List<txtItemType[]> datelist; //����  
    private int resource;   //item�Ĳ���  
    private Context context;
    private LayoutInflater inflator;
    private Handler mHandler;
	private static final int POPKEYBORAD=0;
    private ListView mListView;
    
    /** 
     *  
     * @param context mainActivity
     * @param persons   ��ʾ������ 
     * @param resource  һ��Item�Ĳ��� 
     */  
    public datelistAdapter(Context context,List<txtItemType[]> datelist,int resource,Handler mHandler){
    	this.context=context;
        this.datelist = datelist;
        this.resource = resource;
        this.mHandler=mHandler;
        inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    public void refresh(List<txtItemType[]> list) {
    	this.datelist = list;
    	this.notifyDataSetChanged();
    }
    
    /* 
     * ����������� 
     * */  
    @Override  
    public int getCount() {
    	int size;
    	size=datelist.size();
        return size;
    }  
    /* 
     * ��������Ϊposition������ 
     * */  
    @Override  
    public Object getItem(int position) {
        return datelist.get(position);
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
    	ViewHolder holder;
    	if (convertView==null){
    		holder=new ViewHolder();
            convertView = inflator.inflate(resource, null);
            holder.textView[0]=(TextView)convertView.findViewById(R.id.ItemText1);
            holder.textView[1]=(TextView)convertView.findViewById(R.id.ItemText2);
            holder.textView[2]=(TextView)convertView.findViewById(R.id.ItemText3);
            holder.textView[3]=(TextView)convertView.findViewById(R.id.ItemText4);
            holder.textView[4]=(TextView)convertView.findViewById(R.id.ItemText5);
            convertView.setTag(holder);
    	}
    	else{
    		holder=(ViewHolder)convertView.getTag();
    	}
        final txtItemType[] list = datelist.get(position);
        final int pos=position;
		final short[] v40000=((GlobalVar)context.getApplicationContext()).getv40000();
        for (int i=0;i<holder.textView.length;i++){
    		holder.textView[i].setTag(list[i]);
    		holder.textView[i].setText(list[i].getValue());
    		if (v40000[15]==1||v40000[15]==2){
    	        if(position<17){
	            	if (i!=4)
	            		holder.textView[i].setBackgroundColor(Color.rgb(183, 222, 232));
	            	else
	            		holder.textView[i].setBackgroundColor(Color.rgb(141, 180, 226));
    	        }
    	        else if(position>=17&&position<=23){
    	        	holder.textView[i].setBackgroundColor(Color.rgb(141, 180, 226));
    	        }
    	        else if(position>23){
    	        	holder.textView[i].setBackgroundColor(Color.rgb(0, 176, 80));
    	        }
    		}
    		else{
    			if(position==0&&i==0){
    				holder.textView[i].setBackgroundColor(Color.rgb(221, 221, 221));
    				holder.textView[i].setGravity(Gravity.CENTER);
    			}
    			else if(position<14){
    				if (position==9&&i==3){
    	            	holder.textView[i].setBackgroundColor(Color.rgb(141, 180, 226));
    				}
    				else if (i!=4)
    	            	holder.textView[i].setBackgroundColor(Color.rgb(183, 222, 232));
    	            else
    	            	holder.textView[i].setBackgroundColor(Color.rgb(141, 180, 226));
    	        }
    	        else if(position>=14&&position<=19){
    	        	holder.textView[i].setBackgroundColor(Color.rgb(141, 180, 226));
    	        }
    	        else if(position>19){
    	        	holder.textView[i].setBackgroundColor(Color.rgb(0, 176, 80));
    	        }
    		}
        	if (list[i].isWrite){
        		holder.textView[i].setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						final txtItemType item=(txtItemType)v.getTag();
						if (item.getindex()==8&&item.isWrite&&pos==0){
							v.setBackgroundColor(Color.CYAN);
					        new AlertDialog.Builder(context)
							.setTitle("���������")
							.setMessage(item.getTitle())
							.setCancelable(false)
							.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() { 
							    @Override 
							    public void onClick(DialogInterface dialog, int which) {
							    	new mbWriteThread(context, mHandler, 0, item.getindex()-1, 1).start();
							    } 
							})
							.setNegativeButton("ȡ��", null).show();
						}
						else if (item.getindex()==15&&item.isWrite){
							v.setBackgroundColor(Color.CYAN);
					        LayoutInflater factory=LayoutInflater.from(context);
					        View view=factory.inflate(R.layout.sbtypedialog, null);
					        final RadioButton[] rbtnsbType=new RadioButton[4];
					        rbtnsbType[0]=(RadioButton)view.findViewById(R.id.rbtnUnknown);
					        rbtnsbType[1]=(RadioButton)view.findViewById(R.id.rbtnHvSingle);
					        rbtnsbType[2]=(RadioButton)view.findViewById(R.id.rbtnHvThree);
					        rbtnsbType[3]=(RadioButton)view.findViewById(R.id.rbtnHf);
				        	rbtnsbType[v40000[item.getindex()]].setChecked(true);
					        new AlertDialog.Builder(context)
							.setTitle("�޸Ĳ���")
							.setMessage(item.getTitle())
							.setView(view)
							.setCancelable(false)
							.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() { 
							    @Override 
							    public void onClick(DialogInterface dialog, int which) {
							    	int j=0;
							    	for (int i=0;i<rbtnsbType.length;i++){
							    		if (rbtnsbType[i].isChecked())
							    			j=i;
							    	}
							    	new mbWriteThread(context, mHandler, 1, item.getindex()-1, j).start();
							    } 
							})
							.setNegativeButton("ȡ��", null).show();
						}
						else if (item.getindex()==16&&item.isWrite){
							v.setBackgroundColor(Color.CYAN);
					        LayoutInflater factory=LayoutInflater.from(context);
					        View view=factory.inflate(R.layout.dcstypedialog, null);
					        final RadioButton[] rbtnDcsType=new RadioButton[2];
					        rbtnDcsType[0]=(RadioButton)view.findViewById(R.id.rbtnCommCtrl);
					        rbtnDcsType[1]=(RadioButton)view.findViewById(R.id.rbtnDcsCtrl);
					        rbtnDcsType[v40000[item.getindex()]].setChecked(true);
					        new AlertDialog.Builder(context)
							.setTitle("�޸Ĳ���")
							.setMessage(item.getTitle())
							.setView(view)
							.setCancelable(false)
							.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() { 
							    @Override 
							    public void onClick(DialogInterface dialog, int which) {
							    	int j = 0;
							    	for (int i=0;i<rbtnDcsType.length;i++){
							    		if (rbtnDcsType[i].isChecked())
							    			j=i;
							    	}
							    	new mbWriteThread(context, mHandler, 1, item.getindex()-1, j).start();
							    }
							})
							.setNegativeButton("ȡ��", null).show();
						}
						else if(item.getindex()!=0&&item.isWrite){
							v.setBackgroundColor(Color.CYAN);
				    		final EditText input=new EditText(context);
				    		input.setInputType(InputType.TYPE_CLASS_NUMBER);
				    		input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
				    		input.setKeyListener(new DigitsKeyListener());
			        		input.setText(String.valueOf(v40000[item.getindex()]));
			        		input.setSelectAllOnFocus(true);
							new AlertDialog.Builder(context)
							.setTitle("�޸Ĳ���")
							.setMessage(item.getTitle())
							.setView(input)
							.setCancelable(false)
							.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() { 
							    @Override 
							    public void onClick(DialogInterface dialog, int which) {
							    	if (input.getText().toString().equals("")){}
							    	else
							    		new mbWriteThread(context, mHandler, 1, item.getindex()-1, Integer.parseInt(input.getText().toString())).start();
							    } 
							})
							.setNegativeButton("ȡ��", null).show();
			        		Message msg = mHandler.obtainMessage();
			        		msg.what = POPKEYBORAD;
			        		msg.sendToTarget();
						}
					}
        		});
        	}
        }
        return convertView;
    }
    
    public class ViewHolder {
    	public TextView[] textView=new TextView[5];
    }
    
	public void setListView(ListView listView){
        this.mListView = listView;
    }
    
    /**
     * ListView��������
     * @param menu
     */
    private void updateSingleRow(List<txtItemType[]> list){
    	this.datelist = list;
        if(mListView!=null){
            int start = mListView.getFirstVisiblePosition();
            for(int i=start, j=mListView.getLastVisiblePosition();i<=j;i++){
            	Object obj=mListView.getItemAtPosition(i);
            	txtItemType[] list2=(txtItemType[])obj;
            	txtItemType[] list1=list.get(i);
                if(list1==list2){
                    View view = mListView.getChildAt(i-start);
                    getView(i, view, mListView);
                }
            }
        }
    }
    
    /**
     * ������Ϣ֪ͨ�����½���
     */
	@SuppressLint("HandlerLeak")
	@SuppressWarnings("unchecked")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg){
            updateSingleRow((List<txtItemType[]>)msg.obj);
        }
    };
    

    public Handler getHandler(){
        return this.handler;
    }
}