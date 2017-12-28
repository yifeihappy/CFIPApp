package com.example.yifeihappy.cfip;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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

public class TopicFragment extends Fragment {
    ListView mListView;
    SimpleAdapter topicSimpleAdapter;
    List<Map<String, Object>> listItems;
    TopicListHandler topicListHandler;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.activity_topic, container, false);
        mListView = view.findViewById(R.id.topic_list);

        listItems = new ArrayList<>();
        topicListHandler = new TopicListHandler();
        GetTopicThread getTopicThread = new GetTopicThread(topicListHandler);
        new Thread(getTopicThread).start();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> listItem = listItems.get(i);
                Log.d("title", listItem.get("title").toString());
                Bundle bundle = new Bundle();
                bundle.putString("topic_id", listItem.get("id").toString());
                Intent intent = new Intent(getActivity(), TopicDetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return view;

    }

    class TopicListHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (0x123 == msg.what) {
                JSONObject topicJsonObj = (JSONObject) msg.obj;
                JSONArray jsonArray = null;
                JSONArray imgJsonArray = null;
                try {
                    jsonArray = topicJsonObj.getJSONArray("baseData");
                    imgJsonArray = topicJsonObj.getJSONArray("imgData");
                    listItems = new ArrayList<>();
                    Log.d("Js", "" + jsonArray.length());
                    try {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObj = null;
                            jsonObj = jsonArray.getJSONObject(i);
                            Map<String, Object> listItem = new HashMap<>();
                            listItem.put("id",jsonObj.optLong("id"));
                            listItem.put("title", jsonObj.optString("title"));
                            listItem.put("authorname", jsonObj.optString("authorname"));
                            listItem.put("brief", jsonObj.optString("brief"));
                            listItem.put("publishtime", jsonObj.optString("publishtime"));
                            listItem.put("commentnumber", jsonObj.optString("commentnumber"));
                            listItem.put("img", (Bitmap) imgJsonArray.get(i));
                            listItems.add(listItem);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

//                    Log.d("topicSimpleAdapter", topicSimpleAdapter.toString());
//                    Log.d("getContext()", getContext().toString());
//                    Log.d("listItems", listItems.toString());

                    topicSimpleAdapter = new SimpleAdapter(getContext(), listItems, R.layout.topic_item,
                            new String[]{"title", "authorname", "brief", "publishtime", "commentnumber", "img"},
                            new int[]{R.id.title, R.id.authorname, R.id.brief, R.id.publishtime, R.id.commentnumber, R.id.topic_img});
                    topicSimpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {

                        public boolean setViewValue(View view, Object data,
                                                    String textRepresentation) {
                            //判断是否为我们要处理的对象
                            if (view instanceof ImageView && data instanceof Bitmap) {
                                ImageView iv = (ImageView) view;
                                Log.d("img topic", "set img view");
                                iv.setImageBitmap((Bitmap) data);
                                return true;
                            } else
                                return false;
                        }
                    });

                    mListView.setAdapter(topicSimpleAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
