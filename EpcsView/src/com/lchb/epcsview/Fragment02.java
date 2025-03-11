package com.lchb.epcsview;

import java.math.BigDecimal;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

	/**
	 * A placeholder fragment containing a simple view.
	 */
	@SuppressLint("NewApi")
	public class Fragment02 extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private GraphicalView GviewA;
		private LinearLayout chartA;
		private XYMultipleSeriesDataset mDatasetA;
		private XYSeries secVoltageSeriesA;
		private XYSeries secCurrentSeriesA;
		private XYMultipleSeriesRenderer mRendererA;
		private GraphicalView GviewB;
		private LinearLayout chartB;
		private XYMultipleSeriesDataset mDatasetB;
		private XYSeries secVoltageSeriesB;
		private XYSeries secCurrentSeriesB;
		private XYMultipleSeriesRenderer mRendererB;
		private GlobalVar globalvar;
		private boolean mRunning = false;
		private Button btnSinglePulseTest;
		private TextView txtCtrlFrequency;
        private TextView txtCtrlFrequencyTitle;
		private TextView txtCtrlFrequencySet;
		private TextView txtChargeRatioHigh;
		private TextView txtChargeRatioLow;
		private static final int POPKEYBORAD=0;
	    private static final int WRITEERROR=3;
        
		public Fragment02() {
		}
				
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment02, container,false);
			return rootView;
		}

	    @SuppressLint("HandlerLeak")
		public void onActivityCreated(Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);
	        chartA=(LinearLayout)getActivity().findViewById(R.id.chartA);
	        chartB=(LinearLayout)getActivity().findViewById(R.id.chartB);
	        btnSinglePulseTest=(Button)getActivity().findViewById(R.id.btnSinglePulseTest);
            txtCtrlFrequencyTitle=(TextView)getActivity().findViewById(R.id.txtCtrlFrequencyTitle);
	        txtCtrlFrequency=(TextView)getActivity().findViewById(R.id.txtCtrlFrequency);
	        txtCtrlFrequencySet=(TextView)getActivity().findViewById(R.id.txtCtrlFrequencySet);
	        txtChargeRatioHigh=(TextView)getActivity().findViewById(R.id.txtChargeRatioHigh);
	        txtChargeRatioLow=(TextView)getActivity().findViewById(R.id.txtChargeRatioLow);
	        btnSinglePulseTest.setOnClickListener(onclick);
	        txtCtrlFrequencySet.setOnClickListener(onclick);
	        txtChargeRatioHigh.setOnClickListener(onclick);
	        txtChargeRatioLow.setOnClickListener(onclick);
	        globalvar=(GlobalVar)getActivity().getApplication();
	        InitlineViewA();
	        InitlineViewB();
	        GviewA.post(new Runnable(){
				@Override
				public void run() {
		            int width=GviewA.getWidth();
		            int height=GviewA.getHeight();
		            int newwidth=height;
		            int newheight=width;
		            Float x=((width-newwidth)/2f);
		            Float y=((height-newheight)/2f);
		            float xx=new BigDecimal(x.toString()).setScale(0, BigDecimal.ROUND_HALF_UP).floatValue();
		            float yy=new BigDecimal(y.toString()).setScale(0, BigDecimal.ROUND_HALF_UP).floatValue();
		            GviewA.setRotation(90);
		            LinearLayout.LayoutParams linearParams=(LinearLayout.LayoutParams)GviewA.getLayoutParams();
		            linearParams.width=newwidth;
		            linearParams.height=newheight;
		            GviewA.setLayoutParams(linearParams);
		            GviewA.setX(xx);
		            GviewA.setY(yy);
				}
	        });
	        GviewB.post(new Runnable(){
				@Override
				public void run() {
		            int width=GviewB.getWidth();
		            int height=GviewB.getHeight();
		            int newwidth=height;
		            int newheight=width;
		            Float x=((width-newwidth)/2f);
		            Float y=((height-newheight)/2f);
		            float xx=new BigDecimal(x.toString()).setScale(0, BigDecimal.ROUND_HALF_UP).floatValue();
		            float yy=new BigDecimal(y.toString()).setScale(0, BigDecimal.ROUND_HALF_UP).floatValue();
		            GviewB.setRotation(90);
		            LinearLayout.LayoutParams linearParams=(LinearLayout.LayoutParams)GviewB.getLayoutParams();
		            linearParams.width=newwidth;
		            linearParams.height=newheight;
		            GviewB.setLayoutParams(linearParams);
		            GviewB.setX(xx);
		            GviewB.setY(yy);
				}
	        });
            mRunning=true;
            new updateview().start();
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
            mRunning=false;
		}

		OnClickListener onclick=new OnClickListener() {
			public void onClick(View v){
	    		final EditText input=new EditText(getActivity());
	    		input.setInputType(InputType.TYPE_CLASS_NUMBER);
	    		input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
	    		input.setKeyListener(new DigitsKeyListener());
        		input.setSelectAllOnFocus(true);
        		Message msg = mHandler.obtainMessage();
        		msg.what = POPKEYBORAD;
				switch (v.getId()) {
				case R.id.btnSinglePulseTest:
                    if (globalvar.getv40000(15)!=1&&globalvar.getv40000(15)!=2){
                        new AlertDialog.Builder(getActivity())
                                .setTitle("���������")
                                .setMessage("���������")
                                .setCancelable(false)
                                .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        new mbWriteThread(getActivity(), mHandler, 0, 7, 1).start();
                                    }
                                })
                                .setNegativeButton("ȡ��", null).show();
                    }
					break;
				case R.id.txtCtrlFrequencySet:
					v.setBackgroundColor(Color.CYAN);
                    if (globalvar.getv40000(15)==1||globalvar.getv40000(15)==2){
                        input.setText(String.valueOf(globalvar.getv40000(40)));
                        new AlertDialog.Builder(getActivity())
                                .setTitle("�޸Ĳ���")
                                .setMessage("��ͨ���޶�ֵ(��)")
                                .setView(input)
                                .setCancelable(false)
                                .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (input.getText().toString().equals("")){}
                                        else
                                            new mbWriteThread(getActivity(), mHandler, 1, 39, Integer.parseInt(input.getText().toString())).start();
                                    }
                                })
                                .setNegativeButton("ȡ��", null).show();
                    }
                    else{
                        input.setText(String.valueOf(globalvar.getv40000(41)));
                        new AlertDialog.Builder(getActivity())
                                .setTitle("�޸Ĳ���")
                                .setMessage("����Ƶ���޶�ֵ(Hz)")
                                .setView(input)
                                .setCancelable(false)
                                .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (input.getText().toString().equals("")){}
                                        else
                                            new mbWriteThread(getActivity(), mHandler, 1, 40, Integer.parseInt(input.getText().toString())).start();
                                    }
                                })
                                .setNegativeButton("ȡ��", null).show();
                    }
	        		msg.sendToTarget();
					break;
				case R.id.txtChargeRatioHigh:
                    if (globalvar.getv40000(15)!=1&&globalvar.getv40000(15)!=2){
                        v.setBackgroundColor(Color.CYAN);
                        input.setText(String.valueOf(globalvar.getv40000(52)));
                        new AlertDialog.Builder(getActivity())
                                .setTitle("�޸Ĳ���")
                                .setMessage("���ȸ����޶�ֵ(ms)")
                                .setView(input)
                                .setCancelable(false)
                                .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (input.getText().toString().equals("")){}
                                        else
                                            new mbWriteThread(getActivity(), mHandler, 1, 51, Integer.parseInt(input.getText().toString())).start();
                                    }
                                })
                                .setNegativeButton("ȡ��", null).show();
                        msg.sendToTarget();
                    }
					break;
				case R.id.txtChargeRatioLow:
					v.setBackgroundColor(Color.CYAN);
                    if (globalvar.getv40000(15)==1||globalvar.getv40000(15)==2){
                        input.setText(String.valueOf(globalvar.getv40000(51)));
                        new AlertDialog.Builder(getActivity())
                                .setTitle("�޸Ĳ���")
                                .setMessage("���ȵ����޶�ֵ(pls)")
                                .setView(input)
                                .setCancelable(false)
                                .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (input.getText().toString().equals("")){}
                                        else
                                            new mbWriteThread(getActivity(), mHandler, 1, 50, Integer.parseInt(input.getText().toString())).start();
                                    }
                                })
                                .setNegativeButton("ȡ��", null).show();
                    }
                    else{
                        input.setText(String.valueOf(globalvar.getv40000(53)));
                        new AlertDialog.Builder(getActivity())
                                .setTitle("�޸Ĳ���")
                                .setMessage("���ȵ����޶�ֵ(ms)")
                                .setView(input)
                                .setCancelable(false)
                                .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (input.getText().toString().equals("")){}
                                        else
                                            new mbWriteThread(getActivity(), mHandler, 1, 52, Integer.parseInt(input.getText().toString())).start();
                                    }
                                })
                                .setNegativeButton("ȡ��", null).show();
                    }
	        		msg.sendToTarget();
					break;
				default:
					break;
				}
			}
		};
		
		@SuppressLint("HandlerLeak")
		Handler mHandler=new Handler(){
            @Override
            public void handleMessage(Message msg){
            	switch (msg.what){
            	case POPKEYBORAD:
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
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
            	default:
            		break;
            	}
            	super.handleMessage(msg);
            }
        };
        
        //�����Handlerʵ������������Timerʵ������ɶ�ʱ����ͼ��Ĺ���
	    @SuppressLint("HandlerLeak")
		Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg){
            	//ˢ��ͼ��
    	        mDatasetA.removeSeries(secVoltageSeriesA);
    	        mDatasetA.removeSeries(secCurrentSeriesA);
    	    	secVoltageSeriesA.clear();
    	    	secCurrentSeriesA.clear();
                if (globalvar.getv40000(15)==1||globalvar.getv40000(15)==2) {
                    for (int i = 0; i < 64; i++) {
                        secVoltageSeriesA.add((double) 40 / 63 * i, (double) globalvar.getv30000(40 + i));
                    }
                    for (int i = 0; i < 44; i++) {
                        secCurrentSeriesA.add((double) 40 / 43 * i, (double) globalvar.getv30000(104 + i) * 2);
                    }
                }
    	        else {
                    for (int i = 0; i < 64; i++) {
                        secVoltageSeriesA.add((double) 100 / 63 * i, (double) globalvar.getv30000(40 + i) * 5);
                    }
                    for (int i = 0; i < 44; i++) {
                        secCurrentSeriesA.add((double) 100 / 43 * i, (double) globalvar.getv30000(104 + i));
                    }
                }
                mDatasetA.addSeries(secVoltageSeriesA);
    	        mDatasetA.addSeries(secCurrentSeriesA);
    	        GviewA.repaint();

    	        mDatasetB.removeSeries(secVoltageSeriesB);
    	        mDatasetB.removeSeries(secCurrentSeriesB);
    	    	secVoltageSeriesB.clear();
    	    	secCurrentSeriesB.clear();
                if (globalvar.getv40000(15)==1||globalvar.getv40000(15)==2){
                    for (int i = 0; i < 64; i++) {
                        secVoltageSeriesB.add((double) 40 / 63 * i, (double) globalvar.getv30000(40 + i));
                    }
                    for (int i = 0; i < 44; i++) {
                        secCurrentSeriesB.add((double) 40 / 43 * i, (double) globalvar.getv30000(104 + i) * 2);
                    }
                }
                else {
                    for (int i = 0; i < 64; i++) {
                        secVoltageSeriesB.add((double) 100 / 63 * i, (double) globalvar.getv30000(40 + i) * 5);
                    }
                    for (int i = 0; i < 44; i++) {
                        secCurrentSeriesB.add((double) 100 / 43 * i, (double) globalvar.getv30000(104 + i));
                    }
                }
                mDatasetB.addSeries(secVoltageSeriesB);
    	        mDatasetB.addSeries(secCurrentSeriesB);
    	        GviewB.repaint();
            	super.handleMessage(msg);
                if (globalvar.getv40000(15)==1||globalvar.getv40000(15)==2){
                    btnSinglePulseTest.setEnabled(false);
                    txtCtrlFrequencyTitle.setText("��ͨ��");
                    txtCtrlFrequency.setText(String.valueOf(globalvar.getv30000(1)) + "��");
                    txtCtrlFrequencySet.setText(String.valueOf(globalvar.getv40000(40)) + "��");
                    txtChargeRatioHigh.setText("1:");
                    txtChargeRatioLow.setText(String.valueOf(globalvar.getv40000(51)));
                    txtChargeRatioHigh.setBackgroundColor(Color.rgb(183, 222, 232));
                }
                else{
                    btnSinglePulseTest.setEnabled(true);
                    txtCtrlFrequencyTitle.setText("����Ƶ��");
                    txtCtrlFrequency.setText(String.valueOf(globalvar.getv30000(2))+"Hz");
                    txtCtrlFrequencySet.setText(String.valueOf(globalvar.getv40000(41)) + "Hz");
                    txtChargeRatioHigh.setText(String.valueOf(globalvar.getv40000(52))+"ms");
                    txtChargeRatioLow.setText(String.valueOf(globalvar.getv40000(53)) + "ms");
                    txtChargeRatioHigh.setBackgroundColor(Color.rgb(141, 180, 226));
                }
                txtCtrlFrequency.setBackgroundColor(Color.rgb(183, 222, 232));
                txtCtrlFrequencySet.setBackgroundColor(Color.rgb(141, 180, 226));
                txtChargeRatioLow.setBackgroundColor(Color.rgb(141, 180, 226));
            }
        };
        
	    public void InitlineViewA(){
	        //ͬ������Ҫ����dataset����ͼ��Ⱦ��renderer
			mDatasetA=new XYMultipleSeriesDataset();
            mRendererA=new XYMultipleSeriesRenderer();
            if (globalvar.getv40000(15)==1||globalvar.getv40000(15)==2) {
                secVoltageSeriesA = new XYSeries("���ε���(0~200%)");
                secCurrentSeriesA = new XYSeries("���ε�ѹ(0~100kV)");
                for (int i = 0; i < 64; i++) {
                    secVoltageSeriesA.add((double) 40 / 63 * i, (double) globalvar.getv30000(40 + i));
                }
                for (int i = 0; i < 44; i++) {
                    secCurrentSeriesA.add((double) 40 / 43 * i, (double) globalvar.getv30000(104 + i) * 2);
                }
                mRendererA.setXTitle("(ms)");//����ΪX��ı���
                mRendererA.setYTitle("(%/kV)");//����y��ı���
                mRendererA.setChartTitle("  ");//����ͼ�����
                mRendererA.setYAxisMax(200);
                mRendererA.addYTextLabel(0,"0 0");
                mRendererA.addYTextLabel(20,"20 10");
                mRendererA.addYTextLabel(40,"40 20");
                mRendererA.addYTextLabel(60,"50 30");
                mRendererA.addYTextLabel(80,"80 40");
                mRendererA.addYTextLabel(100,"100 050");
                mRendererA.addYTextLabel(120,"120 060");
                mRendererA.addYTextLabel(140,"140 070");
                mRendererA.addYTextLabel(160,"160 080");
                mRendererA.addYTextLabel(180,"180 090");
                mRendererA.addYTextLabel(200,"200 100");
                mRendererA.setXAxisMin(0);
                mRendererA.setXAxisMax(20);
                mRendererA.setPanLimits(new double[]{0, 20, 0, 200}); //�����϶�ʱX��Y����������ֵ��Сֵ.
                mRendererA.setZoomLimits(new double[]{0, 20, 0, 200});//���÷Ŵ���СʱX��Y������������Сֵ.
            }
            else{
                secVoltageSeriesA = new XYSeries("���ε�ѹ(0~100kV)");
                secCurrentSeriesA = new XYSeries("���ε���(0~500%)");
                for (int i = 0; i < 64; i++) {
                    secVoltageSeriesA.add((double) 100 / 63 * i, (double) globalvar.getv30000(40 + i) * 5);
                }
                for (int i = 0; i < 44; i++) {
                    secCurrentSeriesA.add((double) 100 / 43 * i, (double) globalvar.getv30000(104 + i));
                }
                mRendererA.setXTitle("(us)");//����ΪX��ı���
                mRendererA.setYTitle("(kV/%)");//����y��ı���
                mRendererA.setChartTitle("����A�鲨��");//����ͼ�����
                mRendererA.setYAxisMax(500);
                mRendererA.addYTextLabel(0,"0 0");
                mRendererA.addYTextLabel(50,"10 50");
                mRendererA.addYTextLabel(100,"020 100");
                mRendererA.addYTextLabel(150,"030 150");
                mRendererA.addYTextLabel(200,"040 200");
                mRendererA.addYTextLabel(250,"050 250");
                mRendererA.addYTextLabel(300,"060 300");
                mRendererA.addYTextLabel(350,"070 350");
                mRendererA.addYTextLabel(400,"080 400");
                mRendererA.addYTextLabel(450,"090 450");
                mRendererA.addYTextLabel(500,"100 500");
                mRendererA.setXAxisMin(0);
                mRendererA.setXAxisMax(50);
                mRendererA.setPanLimits(new double[]{0, 50, 0, 500}); //�����϶�ʱX��Y����������ֵ��Сֵ.
                mRendererA.setZoomLimits(new double[]{0, 50, 0, 500});//���÷Ŵ���СʱX��Y������������Сֵ.
            }
            mDatasetA.addSeries(secVoltageSeriesA);
            mDatasetA.addSeries(secCurrentSeriesA);
            //����ͼ���X��ĵ�ǰ����
            mRendererA.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
            mRendererA.setAxisTitleTextSize(30);//����������ı���С
            mRendererA.setChartTitleTextSize(40);//����ͼ��������ֵĴ�С
            mRendererA.setLabelsTextSize(20);//���ñ�ǩ�����ִ�С
            mRendererA.setLegendTextSize(20);//����ͼ���ı���С
            mRendererA.setLegendHeight(40);
            mRendererA.setPointSize(5);//���õ�Ĵ�С
            mRendererA.setYAxisMin(0);//����y����Сֵ��0
            mRendererA.setYLabels(11);//����Y��̶ȸ�����ò�Ʋ�̫׼ȷ��
            mRendererA.setYLabelsAlign(Align.CENTER);
	        mRendererA.setXLabels(11);//����X��̶ȸ�����ò�Ʋ�̫׼ȷ��
	        mRendererA.setShowGrid(true);//��ʾ����
	        mRendererA.setGridColor(Color.GRAY);
	        mRendererA.setZoomButtonsVisible(false);//�Ƿ���ʾ�Ŵ���С��ť
	        mRendererA.setMargins(new int[]{50,70,60,0});//������ͼλ��
	        
			XYSeriesRenderer secVoltageR=new XYSeriesRenderer();//(������һ���߶���)
            secVoltageR.setColor(Color.GREEN);//������ɫ
            secVoltageR.setPointStyle(PointStyle.CIRCLE);//���õ����ʽ
            secVoltageR.setFillPoints(true);//���㣨��ʾ�ĵ��ǿ��Ļ���ʵ�ģ�
            secVoltageR.setDisplayChartValues(false);//�����ֵ��ʾ����
            secVoltageR.setChartValuesSpacing(10);//��ʾ�ĵ��ֵ��ͼ�ľ���
            secVoltageR.setChartValuesTextSize(25);//���ֵ�����ִ�С
            secVoltageR.setLineWidth(5);//�����߿�
            mRendererA.addSeriesRenderer(secVoltageR);

			XYSeriesRenderer secCurrentR=new XYSeriesRenderer();//(������һ���߶���)
            secCurrentR.setColor(Color.RED);//������ɫ
            secCurrentR.setPointStyle(PointStyle.CIRCLE);//���õ����ʽ
            secCurrentR.setFillPoints(true);//���㣨��ʾ�ĵ��ǿ��Ļ���ʵ�ģ�
            secCurrentR.setDisplayChartValues(false);//�����ֵ��ʾ����
            secCurrentR.setChartValuesSpacing(10);//��ʾ�ĵ��ֵ��ͼ�ľ���
            secCurrentR.setChartValuesTextSize(25);//���ֵ�����ִ�С
            secCurrentR.setLineWidth(5);//�����߿�
            mRendererA.addSeriesRenderer(secCurrentR);
	       
            GviewA=ChartFactory.getLineChartView(getActivity(),mDatasetA,mRendererA);
            GviewA.setBackgroundColor(Color.BLACK);
	        //�Ƴ�ԭ�е�LinearLayout�е���ͼ�ؼ�
	        chartA.removeAllViewsInLayout();
	        chartA.addView(GviewA);
	    }
        
	    public void InitlineViewB(){
	        //ͬ������Ҫ����dataset����ͼ��Ⱦ��renderer
			mDatasetB=new XYMultipleSeriesDataset();
            mRendererB=new XYMultipleSeriesRenderer();
            if (globalvar.getv40000(15)==1||globalvar.getv40000(15)==2) {
                secVoltageSeriesB = new XYSeries("���ε���(0~200%)");
                secCurrentSeriesB = new XYSeries("���ε�ѹ(0~100kV)");
                for (int i = 0; i < 64; i++) {
                    secVoltageSeriesB.add((double) 40 / 63 * i, (double) globalvar.getv30000(40 + i));
                }
                for (int i = 0; i < 44; i++) {
                    secCurrentSeriesB.add((double) 40 / 43 * i, (double) globalvar.getv30000(104 + i) * 2);
                }
                mRendererB.setXTitle("(ms)");//����ΪX��ı���
                mRendererB.setChartTitle("  ");//����ͼ�����
                mRendererB.setYAxisMax(200);
                mRendererB.setXAxisMin(20);
                mRendererB.setXAxisMax(40);
                mRendererB.setPanLimits(new double[]{20, 40, 0, 200}); //�����϶�ʱX��Y����������ֵ��Сֵ.
                mRendererB.setZoomLimits(new double[]{20, 40, 0, 2000});//���÷Ŵ���СʱX��Y������������Сֵ.
            }
            else{
                secVoltageSeriesB = new XYSeries("���ε�ѹ(0~100kV)");
                secCurrentSeriesB = new XYSeries("���ε���(0~500%)");
                for (int i = 0; i < 64; i++) {
                    secVoltageSeriesB.add((double) 100 / 63 * i, (double) globalvar.getv30000(40 + i) * 5);
                }
                for (int i = 0; i < 44; i++) {
                    secCurrentSeriesB.add((double) 100 / 43 * i, (double) globalvar.getv30000(104 + i));
                }
                mRendererB.setXTitle("(us)");//����ΪX��ı���
                mRendererB.setChartTitle("����B�鲨��");//����ͼ�����
                mRendererB.setYAxisMax(500);
                mRendererB.setXAxisMin(50);
                mRendererB.setXAxisMax(100);
                mRendererB.setPanLimits(new double[]{50, 100, 0, 500}); //�����϶�ʱX��Y����������ֵ��Сֵ.
                mRendererB.setZoomLimits(new double[]{50, 100, 0, 500});//���÷Ŵ���СʱX��Y������������Сֵ.
            }
            mDatasetB.addSeries(secVoltageSeriesB);
            mDatasetB.addSeries(secCurrentSeriesB);
            //����ͼ���X��ĵ�ǰ����
            mRendererB.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
            mRendererB.setAxisTitleTextSize(30);//����������ı���С
            mRendererB.setChartTitleTextSize(40);//����ͼ��������ֵĴ�С
            mRendererB.setLabelsTextSize(20);//���ñ�ǩ�����ִ�С
            mRendererB.setLegendTextSize(20);//����ͼ���ı���С
            mRendererB.setLegendHeight(40);
            mRendererB.setPointSize(5);//���õ�Ĵ�С
            mRendererB.setYAxisMin(0);//����y����Сֵ��0
            mRendererB.setYLabels(11);//����Y��̶ȸ�����ò�Ʋ�̫׼ȷ��
            mRendererB.setYLabelsAlign(Align.RIGHT);
            mRendererB.setXLabels(11);//����X��̶ȸ�����ò�Ʋ�̫׼ȷ��
	        mRendererB.setShowGrid(true);//��ʾ����
	        mRendererB.setGridColor(Color.GRAY);
	        mRendererB.setZoomButtonsVisible(false);//�Ƿ���ʾ�Ŵ���С��ť
	        mRendererB.setMargins(new int[]{50,0,60,70});//������ͼλ��
	        
			XYSeriesRenderer secVoltageR=new XYSeriesRenderer();//(������һ���߶���)
            secVoltageR.setColor(Color.GREEN);//������ɫ
            secVoltageR.setPointStyle(PointStyle.CIRCLE);//���õ����ʽ
            secVoltageR.setFillPoints(true);//���㣨��ʾ�ĵ��ǿ��Ļ���ʵ�ģ�
            secVoltageR.setDisplayChartValues(false);//�����ֵ��ʾ����
            secVoltageR.setChartValuesSpacing(10);//��ʾ�ĵ��ֵ��ͼ�ľ���
            secVoltageR.setChartValuesTextSize(25);//���ֵ�����ִ�С
            secVoltageR.setLineWidth(5);//�����߿�
            mRendererB.addSeriesRenderer(secVoltageR);

			XYSeriesRenderer secCurrentR=new XYSeriesRenderer();//(������һ���߶���)
            secCurrentR.setColor(Color.RED);//������ɫ
            secCurrentR.setPointStyle(PointStyle.CIRCLE);//���õ����ʽ
            secCurrentR.setFillPoints(true);//���㣨��ʾ�ĵ��ǿ��Ļ���ʵ�ģ�
            secCurrentR.setDisplayChartValues(false);//�����ֵ��ʾ����
            secCurrentR.setChartValuesSpacing(10);//��ʾ�ĵ��ֵ��ͼ�ľ���
            secCurrentR.setChartValuesTextSize(25);//���ֵ�����ִ�С
            secCurrentR.setLineWidth(5);//�����߿�
            mRendererB.addSeriesRenderer(secCurrentR);
	       
            GviewB=ChartFactory.getLineChartView(getActivity(),mDatasetB,mRendererB);
            GviewB.setBackgroundColor(Color.BLACK);
	        //�Ƴ�ԭ�е�LinearLayout�е���ͼ�ؼ�
	        chartB.removeAllViewsInLayout();
	        chartB.addView(GviewB);
	    }

	    class updateview extends Thread{  
            @Override  
            public void run() {
            	while(mRunning){
            		try {
            			Thread.sleep(500);
            		} catch (InterruptedException e) {
            			e.printStackTrace();
            		}
                	Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
            	}
            }
        }
	}
