package com.example.yifeihappy.cfip;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActDetailActivity extends AppCompatActivity {

    ImageView imgView;
    TextView titleTxt;
    TextView addrTxt;
    TextView timeTxt;
    TextView sponserTxt;
    TextView numberTxt;
    TextView successTxt;
    TextView errorTxt;
    TextView peopleTxt;
    Button btnOK;
    Handler handler;
    Long actID;
    Integer actType;
    TextView timeOutTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_detail);
        imgView = (ImageView)findViewById(R.id.act_img);
        titleTxt = (TextView)findViewById(R.id.title);
        addrTxt = (TextView)findViewById(R.id.addr);
        timeTxt = (TextView)findViewById(R.id.time);
        sponserTxt = (TextView)findViewById(R.id.sponser);
        numberTxt = (TextView)findViewById(R.id.number);
        successTxt = (TextView)findViewById(R.id.successTxt);
        peopleTxt = (TextView)findViewById(R.id.peopleTxt);
        btnOK = (Button)findViewById(R.id.btnOK);
        errorTxt = (TextView)findViewById(R.id.errorTxt);
        timeOutTxt = (TextView)findViewById(R.id.timeOutTxt);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String act_id = (String)bundle.get("act_id");
        Log.d("act_id:",act_id);
        handler = new MyHandler();
        ActDetailThread actDetailThread = new ActDetailThread(act_id, handler);
        actDetailThread.start();
        GetApplicationThread getApplicationThread = new GetApplicationThread(handler,LoginActivity.userID, Long.parseLong(act_id));
        getApplicationThread.start();
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObject = new JSONObject();
                Date date = new Date();
                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                String sDateSuffix = dateformat.format(date);
                try {
                    jsonObject.put("userid",LoginActivity.userID);
                    jsonObject.put("actid",actID);
                    jsonObject.put("applytime",sDateSuffix);
                    jsonObject.put("acttype",actType);

                    ApplyThread applyThread = new ApplyThread(handler, jsonObject);
                    applyThread.start();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(0x123 == msg.what){
                JSONObject actDataJson = (JSONObject)msg.obj;
                java.text.SimpleDateFormat dateformat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                String t = dateformat.format(new Date());
                Log.d("curTime", t);

                try {
                    JSONObject actBaseData = actDataJson.getJSONObject("baseData");
                    imgView.setImageBitmap((Bitmap)(actDataJson.get("actImg")));
                    titleTxt.setText(actDataJson.getJSONObject("baseData").get("actname").toString());
                    addrTxt.setText(actDataJson.getJSONObject("baseData").get("actaddress").toString());
                    timeTxt.setText(actDataJson.getJSONObject("baseData").get("actstart").toString());
                    sponserTxt.setText(actDataJson.getJSONObject("baseData").get("actsponser").toString());
                    numberTxt.setText(actDataJson.getJSONObject("baseData").get("totalnumber").toString());
                    peopleTxt.setText(actDataJson.getJSONObject("baseData").get("participantnumber").toString());

                    actID = Long.parseLong(actDataJson.getJSONObject("baseData").get("id").toString());
                    actType = Integer.parseInt(actDataJson.getJSONObject("baseData").get("acttype").toString());
                    if(t.compareTo(actDataJson.getJSONObject("baseData").get("applyend").toString())>0)//活动过期
                    {
                        timeOutTxt.setVisibility(View.VISIBLE);
                    } else {
                        btnOK.setVisibility(View.VISIBLE);//加载图片后显示报名按钮
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if(0x124 == msg.what) {//报名成功
                btnOK.setEnabled(false);
                successTxt.setVisibility(View.VISIBLE);
                errorTxt.setVisibility(View.INVISIBLE);
            } else if(0x224 == msg.what) {//报名失败
                errorTxt.setVisibility(View.VISIBLE);
            }
        }
    }
    class ActDetailThread extends Thread{
        String act_id;
        Handler handler;
        public ActDetailThread(String str, Handler handler){
            act_id = str;
            this.handler = handler;
        }
        @Override
        public void run() {
            super.run();
            {
                String urlStr = "http://112.74.35.75:8080/Entity/U3d616b41047817/CFIP/Activity/"+act_id;
                Log.d("urlStr=",urlStr);
                String jsonStr = HttpJsonUtils.getHttp(urlStr);
                JSONObject actDataJson = new JSONObject();
                JSONObject jsonObject = null;//把String对象转为JSONObject对象
                try {
                    jsonObject = new JSONObject(jsonStr);
//
                    actDataJson.put("baseData", jsonObject);
//                    JSONArray jsonArray = jsonObject.getJSONArray("Dengshen");
//                    Log.d("datalength", "" + jsonArray.length());

                    URL urlImg = new URL("http://112.74.35.75:8080/file/U3d616b41047817/CFIP/Activity/"+act_id);
                    HttpURLConnection connImg = (HttpURLConnection) urlImg.openConnection();
                    InputStream is = connImg.getInputStream();
                    Bitmap mBitmap = BitmapFactory.decodeStream(is);

                    actDataJson.put("actImg", mBitmap);

                    Message msg = new Message();
                    msg.what = 0x123;
                    msg.obj = actDataJson;
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
