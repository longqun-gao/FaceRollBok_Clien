package com.example.administrator.face;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.face.bean.Course;
import com.example.administrator.face.bean.Gate;
import com.example.administrator.face.net.HttpUtil;
import com.example.administrator.face.tool.Constant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SelCour extends AppCompatActivity implements View.OnClickListener {

    private ImageView selCourBack;
    private ListView courListview;
    private List<Course> list = new ArrayList<>();
    private String urlpath = null;
    Course cou;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sel_cour);
        initView();
        urlpath = "CouGetAll";
        getdata(urlpath);
    }

    private void initView() {
        selCourBack = (ImageView) findViewById(R.id.sel_cour_back);
        courListview = (ListView) findViewById(R.id.cour_listview);
        selCourBack.setOnClickListener(this);
    }

    //获取所有的考勤信息
    public void getdata(final String path){
        new Thread(){
            @Override
            public void run() {
                super.run();
                String str = HttpUtil.doGet(Constant.host+path);
                try {
                    JSONArray array = new JSONArray(str);
                    for (int i =  0;i<array.length();i++){
                        JSONObject jsonObject = array.getJSONObject(i);
                        cou = new Course();
                        cou.setCid(jsonObject.getInt("cid"));
                        cou.setCclass(jsonObject.getString("cclass"));
                        cou.setCname(jsonObject.getString("cname"));
                        cou.setCtime(jsonObject.getString("ctime"));
                        cou.setTid(jsonObject.getInt("tid"));
                        list.add(cou);
                    }
                    handler.sendEmptyMessage(0x123);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0x123){
                Myadapter myadapter = new Myadapter();
                courListview.setAdapter(myadapter);
            }
        }
    };

    /**
     * ListView适配器
     * */
    private class Myadapter extends BaseAdapter {

        @Override
        public int getCount() {
            Log.e("AAAA",list.size()+"");
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if(view == null){
                holder = new ViewHolder();
                view = getLayoutInflater().inflate(R.layout.cour_listview,null);
                holder.cid = (TextView) view.findViewById(R.id.cou_id);
                holder.cname = (TextView) view.findViewById(R.id.cou_name);
                holder.tid = (TextView) view.findViewById(R.id.cou_tea);
                holder.ctime = (TextView) view.findViewById(R.id.cou_time);
                holder.cclas = (TextView) view.findViewById(R.id.cou_class);
                view.setTag(holder);
            }else{
                holder = (ViewHolder) view.getTag();
            }
            holder.cname.setText(list.get(i).getCname()+"");
            holder.tid.setText(list.get(i).getTid()+"");
            holder.cid.setText(list.get(i).getCid()+"");
            holder.ctime.setText(list.get(i).getCtime());
            holder.cclas.setText(list.get(i).getCclass()+"");

            return view;
        }

        class ViewHolder{
            TextView cid,tid,cname,ctime,cclas;
        }

    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
