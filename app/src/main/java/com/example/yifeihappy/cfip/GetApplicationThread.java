package com.example.yifeihappy.cfip;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by yifeihappy on 2017/12/27.
 */

public class GetApplicationThread extends Thread {
    Handler handler;
    Long userID;
    Long actID;
    public GetApplicationThread(Handler handler, Long userID, Long actID) {
        this.handler = handler;
        this.userID = userID;
        this.actID = actID;
    }

    @Override
    public void run() {
        super.run();
        String urlStr = "http://112.74.35.75:8080/Entity/U3d616b41047817/CFIP/Applicationform/?Applicationform.userid="+userID+"&Applicationform.actid="+actID;
        Log.d("urlStr=",urlStr);
        String jsonStr = HttpJsonUtils.getHttp(urlStr);
        Log.d("hasbaoming",jsonStr);
        if(!"{}".equals(jsonStr)){
            Message msg = new Message();
            msg.what = 0x124;
            handler.sendMessage(msg);
        }
    }
}
