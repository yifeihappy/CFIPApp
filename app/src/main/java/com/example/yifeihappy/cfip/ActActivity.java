package com.example.yifeihappy.cfip;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActActivity extends AppCompatActivity {

    public static final String imageUrl = "http://www.google.com//ig/images/weather/mostly_sunny.gif";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act);

        List<Map<String, Object>> listItems = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            Map<String, Object> listItem = new HashMap<>();
            listItem.put("title", "开学啦");
            listItem.put("addr", "东上院");
            listItem.put("time", "2017-12-22T12:00");
            listItem.put("sponser", "小雪");
            listItem.put("number", "100|1000");
            //listItem.put("img", R.drawable.koala);
            listItem.put("img", R.drawable.koala);
            listItems.add(listItem);
        }


        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.act_item,
                new String[]{"title", "addr", "time", "sponser", "number", "img"},
                new int[]{R.id.title, R.id.addr, R.id.time, R.id.sponser, R.id.number, R.id.act_img});

        ListView listView = (ListView) findViewById(R.id.act_list);
        listView.setAdapter(simpleAdapter);
    }
}
