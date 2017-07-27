package com.example.administrator.face.tool;

import android.content.Context;
import android.widget.Toast;

/**
 * 吐丝操作
 * Created by Administrator on 2017/3/13 0013.
 */

public class T {
    private static Context context = null;

    public T(Context context){
        this.context=context;
    }

    public static void show(String mess){
        if(context != null){
            Toast.makeText(context,mess,Toast.LENGTH_LONG).show();
        }
    }
}
