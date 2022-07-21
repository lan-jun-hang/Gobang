package com.example.gobang;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gobang.chessview.MyView;
import com.google.android.material.internal.ToolbarUtils;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin;
    private EditText etAccount, etPassword;
    private TextView tv_clickRegister;
    private CheckBox cb_rmbPsw;
    private SharedPreferences.Editor editor;
    private String userName;

    private Context mContext;

    private DBOpenHelper mDBOpenHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("用户名或手机号登录");

        mContext = LoginActivity.this;

        tv_clickRegister = findViewById(R.id.tv_clickRegister);

        mDBOpenHelper = new DBOpenHelper(this);

        initView();

    }

    private void initView() {
        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        cb_rmbPsw = findViewById(R.id.cb_rmbPsw);

        btnLogin.setOnClickListener(this);
        tv_clickRegister.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_clickRegister:
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_login:
                String account = etAccount.getText().toString();
                String password = etPassword.getText().toString();
                if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(password)) {
                    ArrayList<User> data = mDBOpenHelper.getAllData();
                    for (int i = 0; i < data.size(); i++) {
                        User user = data.get(i);
                        if (account.equals(user.getName()) && password.equals(user.getPassword())) {
                            Toast.makeText(this, "登录成功！", Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent();
                            intent1.setClass(LoginActivity.this, MainActivity.class);
                            startActivity(intent1);
                            return;
                        } else {
                            Toast.makeText(this, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Toast.makeText(this, "请输入用户名和密码！", Toast.LENGTH_SHORT).show();
                }
        }

    }
}