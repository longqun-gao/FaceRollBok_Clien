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

import com.example.administrator.face.bean.Gate;
import com.example.administrator.face.net.HttpUtil;
import com.example.administrator.face.tool.Constant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SelGate extends AppCompatActivity implements View.OnClickListener {

    private ImageView selGateBack;
    private ListView stugateListview;
    private List<Gate> list = new ArrayList<>();
    private String urlpath = null;
    Gate gate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sel_gate);
        initView();
        urlpath = "GateGetAllServlet";
        getdata(urlpath);
    }

    private void initView() {
        selGateBack = (ImageView) findViewById(R.id.sel_gate_back);
        stugateListview = (ListView) findViewById(R.id.stugate_listview);
        selGateBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        finish();
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
                        gate = new Gate();
                        gate.setGid(jsonObject.getInt("gid"));
                        gate.setTid(jsonObject.getInt("tid"));
                        gate.setSid(jsonObject.getInt("sid"));
                        gate.setGtime(jsonObject.getString("gtime"));
                        gate.setCid(jsonObject.getInt("cid"));
                        gate.setGtype(jsonObject.getInt("gtype"));
                        list.add(gate);
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
                stugateListview.setAdapter(myadapter);
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
                view = getLayoutInflater().inflate(R.layout.gate_itrm,null);
                holder.gid = (TextView) view.findViewById(R.id.gate_item_gid);
                holder.tid = (TextView) view.findViewById(R.id.gate_item_tid);
                holder.sid = (TextView) view.findViewById(R.id.gate_item_sid);
                holder.cid = (TextView) view.findViewById(R.id.gate_item_cid);
                holder.gtime = (TextView) view.findViewById(R.id.gate_item_gtime);
                holder.gtype = (TextView) view.findViewById(R.id.gate_item_gtype);
                view.setTag(holder);
            }else{
                holder = (ViewHolder) view.getTag();
            }
            holder.gid.setText(list.get(i).getGid()+"");
            holder.tid.setText(list.get(i).getTid()+"");
            holder.sid.setText(list.get(i).getSid()+"");
            holder.cid.setText(list.get(i).getCid()+"");
            holder.gtime.setText(list.get(i).getGtime());
            holder.gtype.setText(list.get(i).getGtype()+"");

            return view;
        }

        class ViewHolder{
            TextView gid,tid,sid,cid,gtime,gtype;
        }

    }
}
