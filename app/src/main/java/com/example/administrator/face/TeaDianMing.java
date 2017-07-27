package com.example.administrator.face;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.face.bean.Gate;
import com.example.administrator.face.bean.Teacher;
import com.example.administrator.face.net.HttpUtil;
import com.example.administrator.face.tool.Constant;
import com.example.administrator.face.tool.T;
import com.google.gson.Gson;

public class TeaDianMing extends AppCompatActivity implements View.OnClickListener {

    private ImageView gateRegBack;
    private TextView gateRegTid;
    private EditText gateRegSid;
    private EditText gateRegCid;
    private EditText gateRegType;
    private Button gateRegSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tea_dian_ming);
        initView();
    }

    private void initView() {
        gateRegBack = (ImageView) findViewById(R.id.gate_reg_back);
        gateRegBack.setOnClickListener(this);
        gateRegTid = (TextView) findViewById(R.id.gate_reg_tid);
        gateRegSid = (EditText) findViewById(R.id.gate_reg_sid);
        gateRegCid = (EditText) findViewById(R.id.gate_reg_cid);
        gateRegType = (EditText) findViewById(R.id.gate_reg_type);
        gateRegSubmit = (Button) findViewById(R.id.gate_reg_submit);
        gateRegSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.gate_reg_back:
                finish();
                break;
            case R.id.gate_reg_submit:
                if(TextUtils.isEmpty(gateRegTid.getText())){
                    T.show("教师id不能为空");
                    return;
                }else if(TextUtils.isEmpty(gateRegSid.getText())){
                    T.show("学生id不能为空");
                    return;
                }  else if(TextUtils.isEmpty(gateRegCid.getText())){
                    T.show("课程id不能为空");
                    return;
                } else if(TextUtils.isEmpty(gateRegType.getText())){
                    T.show("状态不能为空");
                    return;
                } else{
                    Gate gate = new Gate();
                    gate.setCid(Integer.parseInt(gateRegCid.getText().toString()));
                    gate.setGtype(Integer.parseInt(gateRegType.getText().toString()));
                    gate.setSid(Integer.parseInt(gateRegSid.getText().toString()));
                    gate.setTid(Integer.parseInt(gateRegTid.getText().toString()));
                    Gson gson = new Gson();
                    String g = gson.toJson(gate);
                    zhuCePanDuan(Constant.host+"GateInsertServlet?cid="+gate.getCid()+
                            "&gtype="+gate.getGtype()+
                            "&sid="+gate.getSid()+
                            "&tid="+gate.getTid());
                    Log.e("url",Constant.host+"GateInsertServlet?cid="+gate.getCid()+
                            "&gtype="+gate.getGtype()+
                            "&sid="+gate.getSid()+
                            "&sid="+gate.getSid()+
                            "&tid="+gate.getTid());

                }
                break;
        }
    }

    private void zhuCePanDuan(final String urlpath) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                String str = HttpUtil.doGet(urlpath);
                if("flase".equals(str)){
                    handler.sendEmptyMessage(0x123);
                }else if("true".equals(str)){
                    handler.sendEmptyMessage(0x124);
                }
            }
        }.start();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0x123){
                // Toast.makeText(ZhuCeActivity.this,"注册失败",Toast.LENGTH_LONG).show();
                T.show("注册失败");
            }else if (msg.what == 0x124){
                T.show("注册成功");
                finish();
                // Toast.makeText(ZhuCeActivity.this,"注册成功",Toast.LENGTH_LONG).show();
            }
        }
    };
}
