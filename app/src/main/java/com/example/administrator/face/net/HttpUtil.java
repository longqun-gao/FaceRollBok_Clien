package com.example.administrator.face.net;

import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2017/3/13 0013.
 */

public class HttpUtil {
    public static String doGet(String urlpath){
        StringBuffer sb = new StringBuffer();
        try {
            URL url = new URL(urlpath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            Log.e("请求码",conn.getResponseCode()+"aa");
            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream is = conn.getInputStream();
                int len;
                byte [] b = new byte[1024];
                while((len = is.read(b)) != -1){
                    sb.append(new String(b,0,len));
                }
                is.close();
                conn.disconnect();
            }else{
                Log.e("TAG","请求失败");
            }
        } catch (Exception e) {
            Log.e("TAG","异常");
            e.printStackTrace();
        }
        return sb.toString();
    }


}
