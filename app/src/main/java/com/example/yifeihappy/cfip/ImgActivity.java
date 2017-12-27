package com.example.yifeihappy.cfip;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class ImgActivity extends AppCompatActivity {

    ImageView imgView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img);
        imgView = (ImageView)findViewById(R.id.imageView);

        ImgHandler imgHandler = new ImgHandler();
        GetImgThread getImgThread = new GetImgThread("http://112.74.35.75:8080/file/U3d616b41047817/CFIP/Dengshen/","1513930948797",imgHandler);
        getImgThread.start();
    }
    class ImgHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(0x123 == msg.what) {
                imgView.setImageBitmap((Bitmap)msg.obj);
            }
        }
    }
}
