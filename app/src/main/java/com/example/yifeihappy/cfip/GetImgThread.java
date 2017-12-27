package com.example.yifeihappy.cfip;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.example.yifeihappy.cfip.ActActivity.imageUrl;

/**
 * Created by yifeihappy on 2017/12/23.
 */

public class GetImgThread extends Thread {
    String id;
    String baseUrl;
    Handler handler;
    String imageUrl;
    public GetImgThread(String baseUrl, String id, Handler handler) {
        this.id = id;
        this.baseUrl = baseUrl;
        this.handler = handler;
        // = "http://112.74.35.75:8080/file/U3d616b41047817/CFIP/Dengshen/"+this.id;
        imageUrl = baseUrl+this.id;
    }
    @Override
    public void run() {
        super.run();
        Bitmap mBitmap = null;

        try {
            URL url = new URL(imageUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream is = conn.getInputStream();
            mBitmap = BitmapFactory.decodeStream(is);
            Message msg = new Message();
            msg.what = 0x123;
            msg.obj = mBitmap;
            handler.sendMessage(msg);
            is.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
