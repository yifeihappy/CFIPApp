package com.example.yifeihappy.cfip;


import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yifeihappy on 2017/12/24.
 */

public class WallFragment extends Fragment {
    ListView mListView;
    SimpleAdapter simpleAdapter;
    List<Map<String, Object>> listItems;
    WallListHandler wallListHandler;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.activity_wall, container, false);
        mListView = view.findViewById(R.id.wall_list);

        listItems = new ArrayList<>();
        wallListHandler = new WallListHandler(simpleAdapter);
        GetWallThread getWallThread = new GetWallThread(wallListHandler);
        new Thread(getWallThread).start();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String,Object> listItem = listItems.get(i);
                Log.d("wall_id",listItem.get("title").toString());
            }
        });
        return view;

    }
    class WallListHandler extends Handler
    {
        SimpleAdapter simpleAdapter;
        public WallListHandler(SimpleAdapter simpleAdapter) {
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                simpleAdapter = new SimpleAdapter(getActivity(), listItems, R.layout.wall_item, new String[]{"title",  "content", "time"},
                        new int[]{R.id.title, R.id.content, R.id.time});

                mListView.setAdapter(simpleAdapter);
            }
        }
    }
}
