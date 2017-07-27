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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.face.net.HttpUtil;
import com.example.administrator.face.tool.Constant;
import com.example.administrator.face.tool.T;

public class StuXinxi extends AppCompatActivity implements View.OnClickListener {

    private ImageView stuXinxiBack;
    private TextView updateStudentName;
    private TextView updateStudentPwd;
    private TextView updateStudentPhone;
    private TextView updateStudentSex;
    private TextView updateStudentClass;
    private TextView updateStudentSid;
    private String input;
    private String flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu__xinxi);
        initView();
        Intent in = getIntent();
        flag=in.getStringExtra("flag");
    }

    private void initView() {
        stuXinxiBack = (ImageView) findViewById(R.id.stu_xinxi_back);
        stuXinxiBack.setOnClickListener(this);
        updateStudentSid = (TextView) findViewById(R.id.update_student_sid);
        updateStudentName = (TextView) findViewById(R.id.update_student_name);
        updateStudentPwd = (TextView) findViewById(R.id.update_student_pwd);
        updateStudentPhone= (TextView) findViewById(R.id.update_student_phone);
        updateStudentSex = (TextView) findViewById(R.id.update_student_sex);
        updateStudentClass = (TextView) findViewById(R.id.update_student_class);


        //设置个人信息的值
        updateStudentName.setText(MyApp.student.getSname());
        updateStudentPwd.setText(MyApp.student.getSpassword());
        updateStudentPhone.setText(MyApp.student.getSphone());
        updateStudentClass.setText(MyApp.student.getSclass());
        updateStudentSex.setText(MyApp.student.getSsex());
        updateStudentSid.setText(MyApp.student.getSid()+"");
        //点击修改密码
        updateStudentPwd.setOnClickListener(new View.OnClickListener() {
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
                        if (input.equals("")) {
                            T.show("内容不能为空！");
                        }else{
                            getupdate(Constant.host+"StuUpdateServlet?id="
                                    +updateStudentSid.getText().toString().trim()
                                    +"&password="+input+"&flag=2");
                            Log.e("url",Constant.host+"StuUpdateServlet?id="
                                    +updateStudentSid.getText().toString().trim()
                                    +"&password="+input+"&flag=2");
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
    private void getupdate(final String path){
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
                T.show("密码修改成功 ");
                MyApp.student.setSpassword(input);
                updateStudentPwd.setText(input);
            }else if(msg.what == 0x124){
                T.show("密码修改失败");
            }
        }
    };

    @Override
    public void onClick(View view) {
        finish();
    }

}
