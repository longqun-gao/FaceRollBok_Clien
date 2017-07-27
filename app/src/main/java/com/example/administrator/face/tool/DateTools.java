package com.example.administrator.face.tool;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/3/21 0021.
 */

public class DateTools {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm ss");
    public static String getDate(long i){
        return sdf.format(new Date(i));
    }
}
