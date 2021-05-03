package com.example.videocloudapp.controller;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DBController {
    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private Context context;

    private String nickname;

    //LoginActivity의 Context정보를 생성자로 받아온다.
    public DBController(Context context){
        this.context=context;
    }

    DBController(){

    }

    //사용자가 입력한 회원가입에 관한 모든 데이터가 조건에 부합 한다면 DB의 root노드의 하위노드로 UserList를 생성하고 해당 노드의 하위에 입력된 닉네임으로 노드를 생성하고 해당 노드의 값에 이메일을 값으로 저장한다.
    public void upLoadUserInfo(String newEmail, String newNickname){
        DatabaseReference root=firebaseDatabase.getReference();
        DatabaseReference userListNode=root.child("UserList");
        userListNode.child(newNickname).setValue(newEmail);
    }//upLoadUserInfo()

    public void upLoadVideoInfo(String title, String filename,boolean share, boolean download){
        DatabaseReference root=firebaseDatabase.getReference();
        DatabaseReference videoListNode=root.child("VideoList");
        DatabaseReference userListNode=root.child("UserList");

        //현재 로그인한 사용자의 이메일을 가지고 DB에서 닉네임을 찾아온다.
        userListNode.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String email=new AuthController().getEmail();
                if(snapshot.getValue().toString().equals(email)){
                    nickname=snapshot.getKey();
                    //닉네임을 노드의 값으로 정하고 그 값에 동영상의 정보를 저장한다.
                    videoListNode.push().setValue(nickname+"!_#@#_!"+filename+"!_#@#_!"+title+"!_#@#_!"+share+"!_#@#_!"+download+"!_#@#_!"+email);
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
    }//upLoadVideoInfo()

    //Storage에서 파일 삭제 시 DB에 저장된 파일의 정보를 삭제하는 메소드
    public void deleteVideoInfo(String nickname2, String title, String filename,boolean share, boolean download, String email){
        DatabaseReference root=firebaseDatabase.getReference();
        DatabaseReference videoListNode=root.child("VideoList");

        videoListNode.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue().toString().equals(nickname2+"!_#@#_!"+filename+"!_#@#_!"+title+"!_#@#_!"+share+"!_#@#_!"+download+"!_#@#_!"+email)){
                    videoListNode.child(snapshot.getKey()).setValue(null);
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
    }
}//DBController()
