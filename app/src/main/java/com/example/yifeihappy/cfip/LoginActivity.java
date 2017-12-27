package com.example.yifeihappy.cfip;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class LoginActivity extends AppCompatActivity {

    Button btn;
    EditText editText;
    EditText editPswd;
    public static Long userID;
    Handler handler;
    TextView errorTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn = (Button)findViewById(R.id.btnLogin);
        editText = (EditText)findViewById(R.id.editNumber);
        editPswd = (EditText)findViewById(R.id.editPassword);
        final Handler handler = new LoginHandler();
        errorTxt = (TextView)findViewById(R.id.errortTxt);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String studentNumber = editText.getText().toString();
                Log.d("studentNumber", studentNumber);
                Thread thread = new LoginThread(studentNumber, handler, editPswd.getText().toString());
                thread.start();
            }
        });

//        java.text.SimpleDateFormat dateformat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
//        String t=dateformat.format(new Date());
//        Log.e("msg", t);
    }

    class LoginThread extends Thread{
        Handler handler;
        String stuNumber;
        String password;
        public LoginThread(String stuNumber, Handler handler, String password){
            this.stuNumber = stuNumber;
            this.handler = handler;
            this.password = password;
        }
        @Override
        public void run() {
            super.run();
            String url = "http://112.74.35.75:8080/Entity/U3d616b41047817/CFIP/Student/?Student.studentnumber="+stuNumber;
            String jsonStr = HttpJsonUtils.getHttp(url);
            Log.d("jsonStr",jsonStr);
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                JSONArray jsonArray = jsonObject.getJSONArray("Student");
                Log.d("datalength", "" + jsonArray.length());
                Log.d("pswd", jsonArray.getJSONObject(0).optString("password"));
                Log.d("pswd_edit", password);
                if(jsonArray.getJSONObject(0).optString("password").equals(password)){
                    Message msg = new Message();
                    msg.what = 0x001;
                    handler.sendMessage(msg);
                    LoginActivity.userID = jsonArray.getJSONObject(0).optLong("id");
                } else {
                    Log.d("TF","not equal");
                    Message msg = new Message();
                    msg.what = 0x000;
                    handler.sendMessage(msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class LoginHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d("msg",String.valueOf(msg.what));
            if(0x000 == msg.what) {
                errorTxt.setVisibility(View.VISIBLE);
            } else if(0x001 == msg.what) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }
}
