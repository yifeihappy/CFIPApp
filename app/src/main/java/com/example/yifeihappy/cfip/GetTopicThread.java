package com.example.yifeihappy.cfip;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by yifeihappy on 2017/12/24.
 */

public class GetTopicThread implements Runnable {

    Handler handler;
    public GetTopicThread(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        String url = "http://112.74.35.75:8080/Entity/U3d616b41047817/CFIP/Topic/";
        String jsonStr = HttpJsonUtils.getHttp(url);
        Log.d("jsonStr:",jsonStr);
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray jsonArray = jsonObject.getJSONArray("Topic");
            Log.d("datalength", "" + jsonArray.length());
            Bundle bundle = new Bundle();

            JSONArray imgJsonArray = new JSONArray();
            Message msg = new Message();
            msg.what = 0x123;

            for(int i=0;i<jsonArray.length();i++){
                try {
                    URL urlImg = new URL("http://112.74.35.75:8080/file/U3d616b41047817/CFIP/Topic/"+String.valueOf(jsonArray.getJSONObject(i).optLong("id")));
                    HttpURLConnection connImg = (HttpURLConnection) urlImg.openConnection();
                    InputStream is = connImg.getInputStream();
                    Bitmap mBitmap = BitmapFactory.decodeStream(is);
                    imgJsonArray.put(i,mBitmap);
                    is.close();
                    Log.d("get bitmap","img:"+i);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            JSONObject actJsonObj = new JSONObject();
            actJsonObj.put("baseData", jsonArray);
            actJsonObj.put("imgData", imgJsonArray);

            msg.obj = actJsonObj;
            handler.sendMessage(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
