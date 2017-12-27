package com.example.yifeihappy.cfip;

import android.content.Intent;
import android.media.ImageReader;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    TextView responseTxt;
    Button btnGet;
    Button btnPost;
    Button btnWallActivity;
    Button btnActActivity;
    String responseData = "";
    Button btnGetImg;
    Button btnBarActivity;
   // Button btnBaoMing;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123) {
                responseTxt.setText(msg.obj.toString());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        responseTxt = (TextView) findViewById(R.id.responseTxt);
        btnGet = (Button) findViewById(R.id.btnGet);
        btnPost = (Button) findViewById(R.id.btnPost);
        btnWallActivity = (Button) findViewById(R.id.btnWallActivity);
        btnActActivity = (Button) findViewById(R.id.btnActActivity);
        btnGetImg = (Button) findViewById(R.id.btnGetImg);
        btnBarActivity = (Button) findViewById(R.id.btnBarActivity);
      //  btnBaoMing = (Button) findViewById(R.id.btnBaoMing);

//        btnBaoMing.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                    JSONObject jsonObject = new JSONObject();
//                try {
//                    jsonObject.put("userid",LoginActivity.userID);
//                    jsonObject.put("actid",1513472664404L);
//                    jsonObject.put("applytime","2017-12-26T20:46");
//                    jsonObject.put("acttype","4");
//
//                    ApplyThread applyThread = new ApplyThread(handler, jsonObject);
//                    applyThread.start();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });



        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("JS", "btnGet.click");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String urlStr = "http://112.74.35.75:8080/Entity/U3d616b41047817/CFIP/Message/";
                        String jsonStr = HttpJsonUtils.getHttp(urlStr);
                        JSONObject jsonObject = null;//把String对象转为JSONObject对象
                        try {
                            jsonObject = new JSONObject(jsonStr);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            JSONArray jsonArray = jsonObject.getJSONArray("Message");
                            Log.d("JS", "" + jsonArray.length());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("JS", "btnPost.click");
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        String urlStr = "http://112.74.35.75:8080/Entity/U3d616b41047817/CFPIA/Topic/";
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("title", "This is Topic");
                            jsonObject.put("authorname", "this si author name");

                            String jsonStr = HttpJsonUtils.getHttp(urlStr);
                            jsonObject = new JSONObject(jsonStr);//把String对象转为JSONObject对象
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

        btnWallActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WallActivity.class);
                startActivity(intent);
                //结束当前activity
                //finish();
            }
        });

        btnActActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ActActivity.class);
                startActivity(intent);
            }
        });

        btnGetImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ImgActivity.class);
                startActivity(intent);
            }
        });

        btnBarActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BarActivity.class);
                startActivity(intent);
            }
        });

    }
}
