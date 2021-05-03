package com.example.videocloudapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.videocloudapp.controller.AuthController;

public class LoginActivity extends AppCompatActivity {
    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext=this;

        EditText etvEmail=(EditText)findViewById(R.id.etv_email);
        EditText etvPassword=(EditText)findViewById(R.id.etv_password);
        Button btLogin=(Button)findViewById(R.id.bt_login);
        Button btRegister=(Button)findViewById(R.id.bt_register);

        //로그인 버튼 선택 시 동작작
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runLogin(etvEmail.getText().toString(),etvPassword.getText().toString());
            }
        });

        //회원가입 버튼 선택 시 동작
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //회원가입 화면으로 이동
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });
    }//onCreate()

    //로그인 버튼 선택 시 AuthController클래스의 runLogin메소드를 호출하는 메소드,
    private void runLogin(String email, String password){
        //AuthController클래스의 runLogin메소드를 통해 로그인 진행
        AuthController authController=new AuthController(getApplicationContext());
        authController.runLogin(email,password);
    }//runLogin()

}//LoginActivityClass