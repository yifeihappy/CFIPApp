package com.example.yifeihappy.cfip;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yifeihappy on 2017/12/23.
 */

public class GetWallThread implements Runnable {

    Handler handler;
    public GetWallThread(Handler handler) {
        this.handler = handler;
    }
    @Override
    public void run() {
        String urlStr = "http://112.74.35.75:8080/Entity/U3d616b41047817/CFIP/Dengshen/";
        String jsonStr = HttpJsonUtils.getHttp(urlStr);
        JSONObject jsonObject = null;//把String对象转为JSONObject对象
        try {
            jsonObject = new JSONObject(jsonStr);
            JSONArray jsonArray = jsonObject.getJSONArray("Dengshen");
            Log.d("datalength", "" + jsonArray.length());
            Message msg = new Message();
            msg.what = 0x123;
            msg.obj = jsonArray;
            handler.sendMessage(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
