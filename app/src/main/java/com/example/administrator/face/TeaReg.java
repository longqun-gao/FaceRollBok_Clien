package com.example.administrator.face;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.administrator.face.bean.Teacher;
import com.example.administrator.face.net.HttpUtil;
import com.example.administrator.face.tool.Constant;
import com.example.administrator.face.tool.T;
import com.google.gson.Gson;

public class TeaReg extends AppCompatActivity implements View.OnClickListener {

    private ImageView mTeacherRegBack;
    private EditText mTeacherZhuceName;
    private EditText mTeacherZhucePwd;
    private EditText mTeacherZhucePhone;
    private EditText mTeacherZhuceSex;
    /**
     * 提交注册
     */
    private Button mTeacherZhuceSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tea_reg);
        initView();
    }

    private void initView() {
        mTeacherRegBack = (ImageView) findViewById(R.id.teacher_reg_back);
        mTeacherZhuceName = (EditText) findViewById(R.id.teacher_zhuce_name);
        mTeacherZhucePwd = (EditText) findViewById(R.id.teacher_zhuce_pwd);
        mTeacherZhuceSex = (EditText) findViewById(R.id.teacher_zhuce_sex);
        mTeacherZhucePhone = (EditText) findViewById(R.id.teacher_zhuce_phone);
        mTeacherZhuceSubmit = (Button) findViewById(R.id.teacher_zhuce_submit);
        mTeacherZhuceSubmit.setOnClickListener(this);
        mTeacherRegBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.teacher_reg_back:
                finish();
                break;
            case R.id.teacher_zhuce_submit:
                if(TextUtils.isEmpty(mTeacherZhuceName.getText())){
                    T.show("用户名不能为空");
                    return;
                }else if(TextUtils.isEmpty(mTeacherZhucePwd.getText())){
                    T.show("密码不能为空");
                    return;
                }  else if(TextUtils.isEmpty(mTeacherZhuceSex.getText())){
                    T.show("性别不能为空");
                    return;
                }  else if(TextUtils.isEmpty(mTeacherZhucePhone.getText())){
                    T.show("电话不能为空");
                    return;
                }  else{
                    Teacher teacher = new Teacher();
                    teacher.setTname(mTeacherZhuceName.getText().toString());
                    teacher.setTpassword(mTeacherZhucePwd.getText().toString());
                    teacher.setTphone(mTeacherZhucePhone.getText().toString());
                    teacher.setTsex(mTeacherZhuceSex.getText().toString());
                    Gson gson = new Gson();
                    String tea = gson.toJson(teacher);
                    String flag = "1";
                    zhuCePanDuan(Constant.host+"RegServlet?flag="+flag+"&username="+teacher.getTname()+
                            "&password="+teacher.getTpassword()+"&phone="+teacher.getTphone()+
                            "&sex="+teacher.getTsex());

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
                if("0".equals(str)){
                    handler.sendEmptyMessage(0x123);
                }else if("1".equals(str)){
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
