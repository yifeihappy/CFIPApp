package com.example.yifeihappy.cfip;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by yifeihappy on 2017/12/22.
 */

public class HttpJsonUtils {
    public static String getHttp(String urlStr) {
        //String u = "http://112.74.35.75:8080/Entity/U3d616b41047817/CFIP/Message/";
        HttpURLConnection conn = null;
        StringBuilder sb = new StringBuilder();
        //JSONObject jsonObj = null;
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                //面对获取的输入流进行读取
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            }
            Log.d("data", sb.toString());
            //jsonObj = new JSONObject(sb.toString());//把String对象转为JSONObject对象

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sb.toString();
        //return jsonObj;
    }

    public static boolean postHttp(String urlStr, JSONObject paramData) {
        URL url = null;
        //JSONObject jsonObj = null;
        StringBuilder sb = new StringBuilder();
        try {
            //创建连接
            url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type","application/json");
            connection.connect();

            //POST请求
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());

            out.writeBytes(paramData.toString());
            out.flush();
            out.close();

            if (connection.getResponseCode() == 200) {
                InputStream is = connection.getInputStream();
                //面对获取的输入流进行读取
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            } else{
                return false;
            }
            Log.d("data","data="+ sb.toString());
            //jsonObj = new JSONObject(sb.toString());//把String对象转为JSONObject对象

            // 断开连接
            connection.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        //return jsonObj;
        return true;
    }


}
