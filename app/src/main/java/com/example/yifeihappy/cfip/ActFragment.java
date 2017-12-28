package com.example.yifeihappy.cfip;

import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.ImageView;
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

public class ActFragment extends Fragment {
    ListView mListView;
    SimpleAdapter actSimpleAdapter;
    List<Map<String, Object>> listItems;
    ActListHandler actListHandler;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.activity_act, container, false);
        mListView = view.findViewById(R.id.act_list);

        listItems = new ArrayList<>();
        actListHandler = new ActListHandler();
        GetActThread getActThread = new GetActThread(actListHandler);
        new Thread(getActThread).start();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> listItem = listItems.get(i);
                Log.d("actname", listItem.get("actname").toString());
                Bundle bundle = new Bundle();
                bundle.putString("act_id", listItem.get("id").toString());
                Intent intent = new Intent(getActivity(), ActDetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return view;

    }

    class ActListHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (0x123 == msg.what) {
                JSONObject actJsonObj = (JSONObject) msg.obj;
                JSONArray jsonArray = null;
                JSONArray imgJsonArray = null;
                try {
                    jsonArray = actJsonObj.getJSONArray("baseData");
                    imgJsonArray = actJsonObj.getJSONArray("imgData");
                    listItems = new ArrayList<>();
                    Log.d("Js", "" + jsonArray.length());
                    try {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObj = null;
                            jsonObj = jsonArray.getJSONObject(i);
                            Map<String, Object> listItem = new HashMap<>();
                            listItem.put("id",jsonObj.optLong("id"));
                            listItem.put("actname", jsonObj.optString("actname"));
                            listItem.put("addr", jsonObj.optString("actaddress"));
                            listItem.put("time", jsonObj.optString("actstart"));
                            listItem.put("sponser", jsonObj.optString("actsponser"));
                            listItem.put("number", ""+jsonObj.optInt("participantnumber")+"|"+jsonObj.optInt("totalnumber"));
                            //listItem.put("img", R.drawable.koala);
                            //listItem.put("img", R.drawable.koala);
                            listItem.put("img", (Bitmap) imgJsonArray.get(i));
                            listItems.add(listItem);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

//                    Log.d("actSimpleAdapter", actSimpleAdapter.toString());
//                    Log.d("getContext()", getContext().toString());
//                    Log.d("listItems", listItems.toString());

                    actSimpleAdapter = new SimpleAdapter(getContext(), listItems, R.layout.act_item,
                            new String[]{"actname", "addr", "time", "sponser", "number", "img"},
                            new int[]{R.id.title, R.id.addr, R.id.time, R.id.sponser, R.id.number, R.id.act_img});

                    actSimpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {

                        public boolean setViewValue(View view, Object data,
                                                    String textRepresentation) {
                            //判断是否为我们要处理的对象
                            if (view instanceof ImageView && data instanceof Bitmap) {
                                ImageView iv = (ImageView) view;
                                Log.d("img", "set img view");
                                iv.setImageBitmap((Bitmap) data);
                                return true;
                            } else
                                return false;
                        }
                    });

                    mListView.setAdapter(actSimpleAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
