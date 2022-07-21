package com.example.gobang;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnRegister;
    private EditText etAccount, etPass, etPassConfirm;
    private CheckBox cbAgree;
    private DBOpenHelper mDBOpenHelper;
    private TextView backLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("用户名或手机号注册");

        initView();
    }

    private void initView() {
        etAccount = findViewById(R.id.et_account_register);
        etPass = findViewById(R.id.et_password_register);
        etPassConfirm = findViewById(R.id.et_password_confirm_register);
        cbAgree = findViewById(R.id.rb_agree);
        btnRegister = findViewById(R.id.btn_register);
        backLogin = findViewById(R.id.tv_backLogin);

        btnRegister.setOnClickListener(this);
        backLogin.setOnClickListener(this);
        mDBOpenHelper = new DBOpenHelper(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                String username = etAccount.getText().toString().trim();
                String password1 = etPass.getText().toString().trim();
                String password2 = etPassConfirm.getText().toString().trim();

                if (!cbAgree.isChecked()) {
                    Toast.makeText(RegisterActivity.this, "请先同意用户协议！", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(RegisterActivity.this, "用户名不能为空！", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(password1)) {
                    Toast.makeText(RegisterActivity.this, "密码不能为空！", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!TextUtils.equals(password1, password2)) {
                    Toast.makeText(RegisterActivity.this, "两次输入密码不一致！", Toast.LENGTH_LONG).show();
                    return;
                }
                if (password1.equals(password2)) {
                    mDBOpenHelper.add(username, password2);
                    Toast.makeText(this, "验证通过,注册成功", Toast.LENGTH_SHORT).show();
                    Intent intentRegister = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intentRegister);

                }
            case R.id.tv_backLogin:
                Intent intentBackLogin = new Intent();
                intentBackLogin.setClass(RegisterActivity.this, LoginActivity.class);
                startActivity(intentBackLogin);
        }
    }
}