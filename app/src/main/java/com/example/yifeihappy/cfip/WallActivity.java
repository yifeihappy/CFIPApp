package com.example.yifeihappy.cfip;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WallActivity extends AppCompatActivity {
    ListView mListView;
    SimpleAdapter simpleAdapter;
    List<Map<String, Object>> listItems;
    WallListHandler wallListHandler;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);

        mListView = (ListView)findViewById(R.id.wall_list);
        listItems = new ArrayList<>();

        wallListHandler = new WallListHandler(simpleAdapter, this);

        GetWallThread getWallThread = new GetWallThread(wallListHandler);
        new Thread(getWallThread).start();

//        for(int i=0;i<10;i++) {
//            Map<String, Object> listItem = new HashMap<>();
//            listItem.put("title","开学啦");
//            listItem.put("id","1234234325234");
//            listItem.put("content","我希望收到一个圣诞礼物！");
//            listItem.put("time","2017-12-22T12:00");
//
//            listItems.add(listItem);
//        }
//
//        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.wall_item, new String[]{"title", "id", "content", "time"},
//                new int[]{R.id.title, R.id.id, R.id.content, R.id.time});
//
//        mListView.setAdapter(simpleAdapter);


    }

    class WallListHandler extends  Handler
    {
        WallActivity wallActivity;
        SimpleAdapter simpleAdapter;
        public WallListHandler(SimpleAdapter simpleAdapter, WallActivity wallActivity) {
            this.wallActivity = wallActivity;
            this.simpleAdapter = simpleAdapter;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(0x123 == msg.what) {
                Bundle bundle = msg.getData();
                JSONArray jsonArray = (JSONArray)msg.obj;
                for(int i=0;i<jsonArray.length();i++) {
                    try {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        Map<String, Object> listItem = new HashMap<>();
                        listItem.put("title", jsonObj.optString("title"));
                        listItem.put("id",jsonObj.optLong("id"));
                        listItem.put("content", jsonObj.optString("content"));
                        listItem.put("time", jsonObj.optString("publishtime"));

                        listItems.add(listItem);
                        simpleAdapter = new SimpleAdapter(this.wallActivity, listItems, R.layout.wall_item, new String[]{"title", "content", "time"},
                                new int[]{R.id.title,  R.id.content, R.id.time});

                        mListView.setAdapter(simpleAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

//    ListView mListView;
//    View mView;
//    public static final String imageUrl = "http://www.google.com//ig/images/weather/mostly_sunny.gif";
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_wall);
//
//        mView = LayoutInflater.from(this).inflate(R.layout.list_item,null);
//        mListView = (ListView) findViewById(R.id.wall_list);
//        List<HashMap<String,Object>> mListData = getListData();
//
//        SimpleAdapter adapter = new SimpleAdapter(this, mListData, R.layout.list_item,
//                new String[]{"icon"}, new int[]{R.id.img});
//
//        adapter.setViewBinder(new ViewBinder() {
//
//            public boolean setViewValue(View view, Object data,
//                                        String textRepresentation) {
//                //判断是否为我们要处理的对象
//                if(view instanceof ImageView  && data instanceof Bitmap){
//                    ImageView iv = (ImageView) view;
//
//                    iv.setImageBitmap((Bitmap) data);
//                    return true;
//                }else
//                    return false;
//            }
//        });
//
//        mListView.setAdapter(adapter);
//    }
//    public List<HashMap<String,Object>> getListData(){
//        List<HashMap<String,Object>> list = new ArrayList<>();
//        HashMap<String,Object> map = null;
//        for(int i=0;i<5;i++){
//            map = new HashMap<>();
//            map.put("icon",getBitmap());
//            list.add(map);
//        }
//        return list;
//    }
//
//    public Bitmap getBitmap(){
//        Bitmap mBitmap = null;
//        try {
//            URL url = new URL(imageUrl);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            InputStream is = conn.getInputStream();
//            mBitmap = BitmapFactory.decodeStream(is);
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return mBitmap;
//    }
