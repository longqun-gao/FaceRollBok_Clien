package com.example.administrator.face;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class StudentMianActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mainStudentDname;
    private TextView mainStudentSelgate;
    private TextView mainStudentXsubject;
    private TextView mainStudentXinxi;
    private TextView mainTeacherLogin;
    //记录第一次按下的时间
    private long firstTime = 0;
    private String flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_mian);
        initView();
        Intent intent = getIntent();
        flag=intent.getStringExtra("flag");
    }

    private void initView() {
        mainStudentDname = (TextView) findViewById(R.id.main_student_dname);
        mainStudentSelgate = (TextView) findViewById(R.id.main_student_selgate);
        mainStudentXsubject = (TextView) findViewById(R.id.main_student_xsubject);
        mainStudentXinxi = (TextView) findViewById(R.id.main_student_xinxi);
        mainTeacherLogin = (TextView) findViewById(R.id.main_teacher_login);

        mainStudentDname.setOnClickListener(this);
        mainStudentSelgate.setOnClickListener(this);
        mainStudentXsubject.setOnClickListener(this);
        mainStudentXinxi.setOnClickListener(this);
        mainTeacherLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent in = new Intent();
        switch (view.getId()){
            case R.id.main_student_dname:
                //点名信息
                in.setClass(StudentMianActivity.this,OnlineFaceActivity.class);
                startActivity(in);
                break;
            case R.id.main_student_selgate:
                //查看考勤
                in.setClass(StudentMianActivity.this,SelGate.class);
                startActivity(in);
                break;
            case R.id.main_student_xsubject:
                //查看课程
                in.setClass(StudentMianActivity.this,SelCour.class);
                startActivity(in);
                break;
            case R.id.main_student_xinxi:
                //个人信息
                in.setClass(StudentMianActivity.this,StuXinxi.class);
                in.putExtra("flag",flag);
                startActivity(in);
                break;
            case R.id.main_teacher_login:
                in.setClass(StudentMianActivity.this,StuLogin.class);
                startActivity(in);
                //切换登录
                break;
        }
    }

    /**
     * 双击退出
     * */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {//如果两次按键时间间隔大于800毫秒，则不退出
                Toast.makeText(StudentMianActivity.this, "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                firstTime = secondTime;//更新firstTime
                return true;
            } else {
                System.exit(0);//否则退出程序
            }
        }
        return super.onKeyUp(keyCode, event);
    }
}
