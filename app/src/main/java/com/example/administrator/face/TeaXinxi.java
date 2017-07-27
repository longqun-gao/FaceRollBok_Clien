package com.example.administrator.face;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.face.net.HttpUtil;
import com.example.administrator.face.tool.Constant;
import com.example.administrator.face.tool.T;

public class TeaXinxi extends AppCompatActivity implements View.OnClickListener {

    private ImageView tracherXinxiBack;
    private TextView updateTeacherName;
    private TextView updateTeacherMima;
    private TextView updateTeacherPhone;
    private TextView updateTeacherSex;
    private TextView updateTeacherTid;
    private String flag;
    private String input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tea_xinxi);
        initView();
        Intent in = getIntent();
        flag=in.getStringExtra("flag");
    }

    private void initView() {
        tracherXinxiBack = (ImageView) findViewById(R.id.tracher_xinxi_back);
        tracherXinxiBack.setOnClickListener(this);
        updateTeacherTid = (TextView) findViewById(R.id.update_teacher_tid);
        updateTeacherName = (TextView) findViewById(R.id.update_teacher_name);
        updateTeacherMima = (TextView) findViewById(R.id.update_teacher_mima);
        updateTeacherPhone = (TextView) findViewById(R.id.update_teacher_phone);
        updateTeacherSex = (TextView) findViewById(R.id.update_teacher_sex);

        //设置个人信息的值
        updateTeacherName.setText(MyApp.teacher.getTname());
        updateTeacherMima.setText(MyApp.teacher.getTpassword());
        updateTeacherPhone.setText(MyApp.teacher.getTphone());
        updateTeacherSex.setText(MyApp.teacher.getTsex());
        updateTeacherTid.setText(MyApp.teacher.getTid()+"");
        //点击可修改密码
        updateTeacherMima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1();
            }
        });
    }

    //提示是否修改
    private void dialog1() {
        final EditText et = new EditText(this);

        new AlertDialog.Builder(this).setTitle("请输入新的密码")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        input = et.getText().toString().trim();
                        Log.e("pass",input+"");
                        if (input.equals("")) {
                            T.show("内容不能为空！");
                        }else{
                            getupdate(Constant.host+"StuUpdateServlet?id="
                                    +updateTeacherTid.getText().toString().trim()
                                    +"&password="+input+"&flag=1");
                            Log.e("url",Constant.host+"StuUpdateServlet?id="
                                    +updateTeacherTid.getText().toString().trim()
                                    +"&password="+input+"&flag=1");
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }private void getupdate(final String path){
        new Thread() {
            @Override
            public void run() {
                super.run();
                String str = HttpUtil.doGet(path);
                if("flase".equals(str)){
                    //失败
                    handler.sendEmptyMessage(0x124);
                }else if("true".equals(str)) {
                    //成功
                    handler.sendEmptyMessage(0x123);

                }
            }
        }.start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123) {
                T.show("修改成功 ");
                MyApp.teacher.setTpassword(input);
                updateTeacherMima.setText(input);
                finish();

            }else if(msg.what == 0x124){
                T.show("修改失败");
            }
        }
    };


    @Override
    public void onClick(View view) {
        finish();
    }
}
