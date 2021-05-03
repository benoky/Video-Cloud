package com.example.videocloudapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.videocloudapp.fragment.MyFragment;
import com.example.videocloudapp.fragment.OtherFragment;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    public static Context mContext;

    //프래그먼트를 컨트롤하기 위한 객체
    private OtherFragment otherFragment;
    private MyFragment myFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext=this;

        otherFragment=new OtherFragment();
        myFragment=new MyFragment();

        //최초 메인 화면이 호출되면 화면에 otherFragment를 출력한다.
        getSupportFragmentManager().beginTransaction().replace(R.id.container,  otherFragment).commit();

        TabLayout tabLayout=(TabLayout)findViewById(R.id.tablayout);

        //메인 화면 하단에 있는 탭 버튼
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==0){
                    //OTHER버튼 선택 시 otherFragment출력
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,  otherFragment).commit();
                }else if(tab.getPosition()==1){
                    //MY버튼 선택 시 myFragment출력
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,  myFragment).commit();
                }else{
                    //가장 우측 업로드 버튼 선택 시 UploadActivity호출
                    startActivity(new Intent(mContext,UploadActivity.class));
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }//onCreate()
}//MainActivityClass
