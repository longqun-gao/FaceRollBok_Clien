package com.example.administrator.face.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.administrator.face.R;
import com.example.administrator.face.StuLogin;


/**
 * Created by Administrator on 2017/3/27 0027.
 */

public class StartActivity extends Activity {
    private Button btn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        btn = (Button) findViewById(R.id.tiyan);
        // 立即体验单击事件
        btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
				/*startActivity(new Intent(Activity_first.this,
						Activity_login.class));*/
                startActivity(new Intent(StartActivity.this,
                        StuLogin.class));
                finish();
            }
        });
    }
}
