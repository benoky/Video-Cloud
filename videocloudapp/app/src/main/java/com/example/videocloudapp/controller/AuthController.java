package com.example.videocloudapp.controller;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.videocloudapp.LoginActivity;
import com.example.videocloudapp.MainActivity;
import com.example.videocloudapp.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthController {
    private FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    private Context context;

    DBController dbController=new DBController(context);


    //LoginActivity의 Context정보를 생성자로 받아온다.
    public AuthController(Context context){
        this.context=context;
    }

    public AuthController(){

    }

    //firebase의 authentication을 사용하여 로그인을 수행하는 메소드
    public void runLogin(String email, String password){
        //firebase에 해당 문자열들이 일치하는지 검사 요청
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) { //task에 로그인의 성공 여부가저장 되어 있음
                    context.startActivity(new Intent(context.getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)); //로그인 결과가 true이면 MainActivity화면을 호출한다.
                    ((LoginActivity) LoginActivity.mContext).finish();//로그인에 성공하면 LoginActivity화면을 종료한다.

                } else {
                    Log.d("asd",task.toString());
                    //로그인 결과가 false이면 LoginActivity의 화면에 Toast메시지를 출력한다.
                    Toast.makeText(context.getApplicationContext(),"Email 또는 Password를 다시 확인해 주세요.",Toast.LENGTH_LONG).show();
                }
            }//onComplete()
        });
    }//runLogin()

    public void runRegister(String newEmail, String newPassword, String newNickname){
        firebaseAuth.createUserWithEmailAndPassword(newEmail,newPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                //계정 정보가 성공적으로 등록 되었는지 판단
                if(task.isSuccessful()){
                    Toast.makeText(context.getApplicationContext(),"회원가입 성공",Toast.LENGTH_LONG).show();

                    dbController.upLoadUserInfo(newEmail,newNickname);

                    ((RegisterActivity) context).finish(); //회원가입에 성공하면 RegisterActivity화면을 종료한다.

                }else{
                    Toast.makeText(context.getApplicationContext(),"이메일 또는 비밀번호를 확인해 주세요.",Toast.LENGTH_LONG).show();
                }
            }
        });//createUserWithEmailAndPassword()
    }//runRegister()

    public String getEmail(){
        return firebaseAuth.getCurrentUser().getEmail();
    }

}//AuthControllerClass
