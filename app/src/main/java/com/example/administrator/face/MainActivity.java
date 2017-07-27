package com.example.administrator.face;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 手动点名系统
     */
    private TextView mMainTeacherDname;
    /**
     * 查看课程
     */
    private TextView mMainTeacherSelCour;
    /**
     * 统计缺勤
     */
    private TextView mMainTeacherSelGateByType;
    /**
     * 个人信息
     */
    private TextView mMainTeacherXinxi;
    /**
     * 切换登录
     */
    private TextView mMainStudentLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mMainTeacherDname = (TextView) findViewById(R.id.main_teacher_dname);
        mMainTeacherDname.setOnClickListener(this);
        mMainTeacherSelCour = (TextView) findViewById(R.id.main_teacher_selCour);
        mMainTeacherSelCour.setOnClickListener(this);
        mMainTeacherSelGateByType = (TextView) findViewById(R.id.main_teacher_selGateByType);
        mMainTeacherSelGateByType.setOnClickListener(this);
        mMainTeacherXinxi = (TextView) findViewById(R.id.main_teacher_xinxi);
        mMainTeacherXinxi.setOnClickListener(this);
        mMainStudentLogin = (TextView) findViewById(R.id.main_student_login);
        mMainStudentLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent in = new Intent();
        switch (v.getId()) {
            case R.id.main_teacher_dname:
                //点名
                in.setClass(MainActivity.this,TeaDianMing.class);
                startActivity(in);
                break;
            case R.id.main_teacher_selCour:
                //查看课程
                in.setClass(MainActivity.this,SelCour.class);
                startActivity(in);
                break;
            case R.id.main_teacher_selGateByType:
                //查看缺勤
                in.setClass(MainActivity.this,StuGateByType.class);
                startActivity(in);
                break;
            case R.id.main_teacher_xinxi:
                //教师信息
                in.setClass(MainActivity.this,TeaXinxi.class);
                startActivity(in);
                break;
            case R.id.main_student_login:
                //切换学生登录
                in.setClass(MainActivity.this,StuLogin.class);
                startActivity(in);
                break;
        }
    }
}
