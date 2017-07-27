package com.example.administrator.face;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.face.bean.Student;
import com.example.administrator.face.bean.Teacher;
import com.example.administrator.face.net.HttpUtil;
import com.example.administrator.face.tool.Constant;
import com.example.administrator.face.tool.T;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class StuLogin extends AppCompatActivity implements View.OnClickListener {

    /**
     * 请输入用户名
     */
    private EditText mLoginindexUsername;
    /**
     * 请输入密码
     */
    private EditText mLoginindexPassword;
    /**
     * 请输入用类型(1教师2学生)
     */
    private EditText mLoginindexType;
    /**
     * 登录
     */
    private Button mLoginindexDenglubtn;
    /**
     * 教师注册
     */
    private TextView mLoginindexTeacherZhuce;
    /**
     * 学生注册
     */
    private TextView mLoginindexStudentZhucebtn;
    String flag  = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_login);
        initView();
    }

    private void initView() {
        mLoginindexUsername = (EditText) findViewById(R.id.loginindex_username);
        mLoginindexPassword = (EditText) findViewById(R.id.loginindex_password);
        mLoginindexType = (EditText) findViewById(R.id.loginindex_type);
        mLoginindexDenglubtn = (Button) findViewById(R.id.loginindex_denglubtn);
        mLoginindexDenglubtn.setOnClickListener(this);
        mLoginindexTeacherZhuce = (TextView) findViewById(R.id.loginindex_teacherZhuce);
        mLoginindexTeacherZhuce.setOnClickListener(this);
        mLoginindexStudentZhucebtn = (TextView) findViewById(R.id.loginindex_student_zhucebtn);
        mLoginindexStudentZhucebtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent in = new Intent();
        switch (v.getId()) {
            case R.id.loginindex_denglubtn:
                loginPanDuan();
                break;
            case R.id.loginindex_teacherZhuce:
                in.setClass(StuLogin.this,TeaReg.class);
                startActivity(in);
                break;
            case R.id.loginindex_student_zhucebtn:
                in.setClass(StuLogin.this,StuReg.class);
                startActivity(in);
                break;
        }
    }

    //登录判断
    private void loginPanDuan(){
        if (TextUtils.isEmpty(mLoginindexUsername.getText().toString())){
            //Toast.makeText(LoginActivity.this,"用户名不能为空",Toast.LENGTH_LONG).show();
            T.show("用户名不能为空");
            return;
        }else if (TextUtils.isEmpty(mLoginindexPassword.getText().toString())){
            T.show("密码不能为空");
            return;
        } else if (TextUtils.isEmpty(mLoginindexType.getText().toString())){
            T.show("类型不能为空");
            return;
        } else {
            //提交数据给服务器
            try {
                loginDengLu(Constant.host+"LoginServlet?flag="
                        + URLEncoder.encode(mLoginindexType.getText().toString().trim(), "utf-8")+"&username="
                        +URLEncoder.encode(mLoginindexUsername.getText().toString(),"utf-8")
                        +"&password="+URLEncoder.encode(mLoginindexPassword.getText().toString(),"utf-8"));//登录操作
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private void loginDengLu(final String urlpath) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                String str = HttpUtil.doGet(urlpath);
                Log.e("str",str+"");
                try {
                    if("false".equals(str)){
                        handler.sendEmptyMessage(0x123);
                    }else{
                        //将接收到的值转化为Josn
                        JSONObject jsonobject = new JSONObject(str);
                        if("1".equals(mLoginindexType.getText().toString().trim())){
                            //gson解析
                            Gson gson = new Gson();
                            MyApp.teacher = gson.fromJson(str,Teacher.class);
                            //json解析
                            int tid = jsonobject.getInt("tid");
                            String tname = jsonobject.getString("tname");
                            Log.e("tname",tname+"");
                            String tpassword = jsonobject.getString("tpassword");
                            String tsex = jsonobject.getString("tsex");
                            String tphone = jsonobject.getString("tphone");
                            Log.e("TAG",tid+":"+tname+":"+tpassword+":"+tsex+":"+tphone);
                            handler.sendEmptyMessage(0x124);
                        }else if("2".equals(mLoginindexType.getText().toString().trim())){
                            //gson解析
                            Gson gson = new Gson();
                            MyApp.student = gson.fromJson(str,Student.class);
                            handler.sendEmptyMessage(0x125);
                        }

                    }
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
            if (msg.what == 0x123){
                T.show("用户名密码类型错误");
            }else if(msg.what == 0x124){
                T.show("教师登录成功");
                flag = mLoginindexType.getText().toString().trim();
                Log.e("type",flag+"type");
                Intent in = new Intent(StuLogin.this,MainActivity.class);
                in.putExtra("flag",flag);
                Log.e("loginflag",flag+"login");
                startActivity(in);
                finish();
            }else if(msg.what == 0x125){
                T.show("学生登录成功");
                flag = mLoginindexType.getText().toString().trim();
                Log.e("type",flag+"type");
                Intent in = new Intent(StuLogin.this,StudentMianActivity.class);
                in.putExtra("flag",flag);
                Log.e("loginflag",flag+"login");
                startActivity(in);
                finish();
            }
        }
    };
}
