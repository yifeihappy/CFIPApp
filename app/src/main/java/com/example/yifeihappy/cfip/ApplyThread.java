package com.example.yifeihappy.cfip;

import android.os.Handler;
import android.os.Message;

import org.json.JSONObject;

/**
 * Created by yifeihappy on 2017/12/27.
 */

public class ApplyThread extends Thread {
    Handler handler;
    JSONObject jsonObject;
    public ApplyThread(Handler handler, JSONObject jsonObject){
        this.handler = handler;
        this.jsonObject = jsonObject;
    }

    @Override
    public void run() {
        super.run();
        boolean b = HttpJsonUtils.postHttp("http://112.74.35.75:8080/Entity/U3d616b41047817/CFIP/Applicationform/", jsonObject);
        Message msg = new Message();
        if(b) {
            msg.what = 0x124;
        } else {
            msg.what = 0x224;
        }
        handler.sendMessage(msg);
    }
}
