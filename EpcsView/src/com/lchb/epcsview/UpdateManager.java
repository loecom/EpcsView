package com.lchb.epcsview;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;
import android.app.AlertDialog.Builder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by 农夫 on 2014/8/4.
 */
public class UpdateManager {
    public ProgressDialog pBar;
    private Handler handler = new Handler();
    private int newVerCode = 0;
    private String newVerName = "";
    private Context context;
    private static String UPDATE_APKNAME = "EpcsView-debug.apk";
    private static final String TAG = "Update";
    private static final String UPDATE_SERVER = "http://112.12.3.153/";
    private static final String UPDATE_VERJSON = "ver.json";
    private static final String UPDATE_SAVENAME = "updateapksamples.apk";

    public UpdateManager(Context context){
        this.context = context;
    }

    /**
     * 检测软件更新
     */
    public void checkUpdate(){
        new GetServerVerCode().execute();
    }

    private class GetServerVerCode extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                String verjson = getContent(UPDATE_SERVER + UPDATE_VERJSON);
                JSONArray array = new JSONArray(verjson);
                if (array.length() > 0) {
                    JSONObject obj = array.getJSONObject(0);
                    try {
                        newVerCode = Integer.parseInt(obj.getString("verCode"));
                        newVerName = obj.getString("verName");
                        UPDATE_APKNAME = obj.getString("apkname");
                    } catch (Exception e) {
                        newVerCode = -1;
                        newVerName = "";
                        return false;
                    }
                }
            } catch (Exception e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                int vercode = getVerCode();
                if (newVerCode > vercode) {
                    doNewVersionUpdate();
                } else {
                    //notNewVersionShow();
                }
            }
        }
    }

    private void notNewVersionShow() {
        int verCode = getVerCode();
        String verName = getVerName();
        StringBuffer sb = new StringBuffer();
        sb.append("当前版本:");
        sb.append(verName);
        //sb.append(" Code:");
        //sb.append(verCode);
        sb.append(",\n已是最新版,无需更新!");
        Dialog dialog = new AlertDialog.Builder(context)
                .setTitle("软件更新").setMessage(sb.toString())// 设置内容
                .setCancelable(false)
                .setPositiveButton("确定", null).create();// 创建
        // 显示对话框
        dialog.show();
    }

    private void doNewVersionUpdate() {
        int verCode = getVerCode();
        String verName = getVerName();
        StringBuffer sb = new StringBuffer();
        sb.append("当前版本:");
        sb.append(verName);
        //sb.append(" Code:");
        //sb.append(verCode);
        sb.append(", 发现新版本:");
        sb.append(newVerName);
        //sb.append(" Code:");
        //sb.append(newVerCode);
        sb.append(", 是否更新?");
        Dialog dialog = new AlertDialog.Builder(context)
                .setTitle("软件更新")
                .setMessage(sb.toString())
                .setCancelable(false)
                        // 设置内容
                .setPositiveButton("更新",// 设置确定按钮
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                pBar = new ProgressDialog(context);
                                pBar.setTitle("正在下载");
                                pBar.setMessage("请稍候...");
                                pBar.setCancelable(false);
                                pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                downFile(UPDATE_SERVER + UPDATE_APKNAME);
                            }
                        })
                .setNegativeButton("暂不更新",null).create();// 创建
        // 显示对话框
        dialog.show();
    }

    void downFile(final String url) {
        pBar.show();
        new Thread() {
            public void run() {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(url);
                HttpResponse response;
                try {
                    response = client.execute(get);
                    HttpEntity entity = response.getEntity();
                    long length = entity.getContentLength();
                    InputStream is = entity.getContent();
                    FileOutputStream fileOutputStream = null;
                    if (is != null) {
                        File file = new File(
                                Environment.getExternalStorageDirectory(),UPDATE_SAVENAME);
                        fileOutputStream = new FileOutputStream(file);
                        byte[] buf = new byte[1024];
                        int ch = -1;
                        int count = 0;
                        while ((ch = is.read(buf)) != -1) {
                            fileOutputStream.write(buf, 0, ch);
                            count += ch;
                            if (length > 0) {
                            }
                        }
                    }
                    fileOutputStream.flush();
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    down();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    void down() {
        handler.post(new Runnable() {
            public void run() {
                pBar.cancel();
                update();
            }
        });
    }

    void update() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), UPDATE_SAVENAME)),"application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public String getContent(String url) throws Exception{
        StringBuilder sb = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpParams httpParams = client.getParams();
        //设置网络超时参数
        HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
        HttpConnectionParams.setSoTimeout(httpParams, 5000);
        HttpGet httpGet=new HttpGet(url);
        HttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"), 8192);
            String line = null;
            while ((line = reader.readLine())!= null){
                sb.append(line + "\n");
            }
            reader.close();
        }
        return sb.toString();
    }

    public int getVerCode() {
        int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo("com.lchb.epcsview", 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        return verCode;
    }

    public String getVerName() {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo( "com.lchb.epcsview", 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        return verName;
    }

    public String getAppName() {
        String verName = context.getResources().getText(R.string.app_name).toString();
        return verName;
    }
}
