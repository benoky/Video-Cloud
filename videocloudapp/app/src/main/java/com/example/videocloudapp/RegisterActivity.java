package com.example.videocloudapp;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.videocloudapp.controller.AuthController;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    public static Context mContext;

    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mContext=this;

        EditText etvNewEmail=(EditText)findViewById(R.id.etv_newemail);
        EditText etvNewPassword=(EditText)findViewById(R.id.etv_newpassword);
        EditText etvNewNickname=(EditText)findViewById(R.id.etv_newnickname);
        Button btComplete=(Button) findViewById(R.id.bt_complete);

        //완료 버튼 선택 시 동작
        btComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runRegister(etvNewEmail.getText().toString(), etvNewPassword.getText().toString(), etvNewNickname.getText().toString());
            }
        });
    }//onCreate()

    //완료 버튼 선택 시 동작하는 메소드
    private void runRegister(String newEmail, String newPassword, String newNickname){
        firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference root=firebaseDatabase.getReference();
        DatabaseReference userListNode=root.child("UserList");

        //회원가입이 진행 되기 전 입력된 데이터를 검사한다.
        //비밀번호가 2글자 이상, 15글자 이하인지 검사
        if(newPassword.length()<=15 && newPassword.length()>=2 && !newEmail.equals("")){
            //닉네임이 2글자 이상, 9글자 이하인지 검사
            if(newNickname.length()<=9 && newNickname.length()>=2){
                //닉네임이 이미 존재하는지 검사
                userListNode.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        //userListNode의 하위노드로 사용자가 입력한 닉네임이 있는지 검사
                        if(snapshot.getKey().equals(newNickname)){
                            Toast.makeText(getApplicationContext(),"닉네임을 다시 확인해 주세요.",Toast.LENGTH_LONG).show();
                        }else{
                            AuthController authController=new AuthController(getApplicationContext());
                            authController.runRegister(newEmail,newPassword,newNickname);
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }else{
                Toast.makeText(this,"닉네임을 다시 확인해 주세요.",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this,"이메일 또는 비밀번호를 확인해 주세요.",Toast.LENGTH_LONG).show();
        }
    }//runRegister()
}//RegisterActivityClass
