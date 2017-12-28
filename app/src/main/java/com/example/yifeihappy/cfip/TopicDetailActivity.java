package com.example.yifeihappy.cfip;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TopicDetailActivity extends AppCompatActivity {

    ImageView topicImg;
    TextView title;
    TextView brief;
    TextView author;
    TextView time;
    EditText commentEdit;
    TextView successTxt;
    TextView errorTxt;
    Button btnOK;
    Long topicID;
    Integer topicType;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);

        topicImg = (ImageView) findViewById(R.id.topic_img);
        title = (TextView) findViewById(R.id.title);
        brief = (TextView) findViewById(R.id.brief);
        author = (TextView) findViewById(R.id.authorname);
        time = (TextView) findViewById(R.id.time);
        commentEdit = (EditText) findViewById(R.id.commentEdit);
        btnOK = (Button) findViewById(R.id.btnOK);
        successTxt = (TextView) findViewById(R.id.successTxt);
        errorTxt = (TextView) findViewById(R.id.errortTxt);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String topic_id = bundle.get("topic_id").toString();
        handler = new MyHandler();
        TopicDetailThread topicDetailThread = new TopicDetailThread(topic_id, handler);
        topicDetailThread.start();

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("commnet", "comment click");
                JSONObject jsonObject = new JSONObject();
                Date date = new Date();
                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                String sDateSuffix = dateformat.format(date);
                try {
                    jsonObject.put("userid",LoginActivity.userID);
                    jsonObject.put("username", LoginActivity.userName);
                    jsonObject.put("brief", commentEdit.getText());
                    jsonObject.put("commentstime", sDateSuffix);
                    jsonObject.put("topicid", topicID);
                    jsonObject.put("topictype", topicType);

                    AddCommentThread addCommentThread = new AddCommentThread(handler, jsonObject);
                    addCommentThread.start();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (0x123 == msg.what) {
                JSONObject actDataJson = (JSONObject) msg.obj;
                java.text.SimpleDateFormat dateformat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                String t = dateformat.format(new Date());
                Log.d("curTime", t);

                try {
                    JSONObject actBaseData = actDataJson.getJSONObject("baseData");
                    topicImg.setImageBitmap((Bitmap) (actDataJson.get("topicImg")));
                    title.setText(actDataJson.getJSONObject("baseData").get("title").toString());
                    brief.setText(actDataJson.getJSONObject("baseData").get("brief").toString());
                    author.setText(actDataJson.getJSONObject("baseData").get("authorname").toString());
                    time.setText(actDataJson.getJSONObject("baseData").get("publishtime").toString());
                    topicID = Long.parseLong(actDataJson.getJSONObject("baseData").get("id").toString());
                    topicType = Integer.parseInt(actDataJson.getJSONObject("baseData").get("topictype").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (0x124 == msg.what) {
                successTxt.setVisibility(View.VISIBLE);
                //errorTxt.setVisibility(View.INVISIBLE);
            } else if (0x224 == msg.what) {
                //successTxt.setVisibility(View.INVISIBLE);
                errorTxt.setVisibility(View.VISIBLE);
            }
        }
    }

    class TopicDetailThread extends Thread {
        String topic_id;
        Handler handler;

        public TopicDetailThread(String str, Handler handler) {
            topic_id = str;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();

            String urlStr = "http://112.74.35.75:8080/Entity/U3d616b41047817/CFIP/Topic/" + topic_id;
            Log.d("urlStr=", urlStr);
            String jsonStr = HttpJsonUtils.getHttp(urlStr);
            JSONObject topicDataJson = new JSONObject();
            JSONObject jsonObject = null;//把String对象转为JSONObject对象
            try {
                jsonObject = new JSONObject(jsonStr);
                topicDataJson.put("baseData", jsonObject);

                URL urlImg = new URL("http://112.74.35.75:8080/file/U3d616b41047817/CFIP/Topic/" + topic_id);
                HttpURLConnection connImg = (HttpURLConnection) urlImg.openConnection();
                InputStream is = connImg.getInputStream();
                Bitmap mBitmap = BitmapFactory.decodeStream(is);

                topicDataJson.put("topicImg", mBitmap);

                Message msg = new Message();
                msg.what = 0x123;
                msg.obj = topicDataJson;
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
