package com.example.videocloudapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.videocloudapp.controller.StorageController;

import java.io.File;

public class UploadActivity extends AppCompatActivity {
    public static Context mContext;
    private final int VIDEO_SELECT_REQUEST=2;

    private TextView tvNewFilename;

    //파일 탐색기에서 선택한 파일의 이름과 경로를 저장하기 위한 객체
    private String filename;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        mContext=this;

        tvNewFilename=(TextView)findViewById(R.id.tv_newfilename);
        TextView etvNewTitle=(TextView)findViewById(R.id.etv_newtitle);
        Switch swbShare=(Switch)findViewById(R.id.swb_share);
        Switch swbDownload=(Switch)findViewById(R.id.swb_download);
        Button btSave=(Button)findViewById(R.id.bt_save);


        //파일의 이름이 출력되는 텍스트 뷰를 선택 시 동작
        tvNewFilename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoSelect();
            }
        });

        //저장 버튼 선택 시 동작작
       btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runSave(etvNewTitle.getText().toString(), swbShare.isChecked(), swbDownload.isChecked());
            }
        });
    }//onCreate()

    //파일의 이름이 출력되는 텍스트 뷰를 선택 시 파일 탐색기를 호출하는 메소드
    private void videoSelect(){
        startActivityForResult(new Intent().setType("video/*").setAction(Intent.ACTION_GET_CONTENT),VIDEO_SELECT_REQUEST);
    }//videoSelect()

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VIDEO_SELECT_REQUEST) {
            if (resultCode == RESULT_OK) {
                try {
                    //Intent로부터 선택한 파일의 경로를 가져와 저장한다.
                    uri=data.getData();
                    //파일의 이름을 가져오기 위해 파일의 경로로 file객체를 생성한다.
                    File file=new File(uri.toString());
                    //file객체로부터 파일의 이름을 가져와 저장한다.
                    String[] tmpStr=file.getName().split("2F");
                    filename=tmpStr[tmpStr.length-1];

                    tvNewFilename.setText(filename);
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "동영상 선택 취소", Toast.LENGTH_SHORT).show();
            }
        }
    }//onActivityResult()

    //저장 버튼 선택 시 동작하는 메소드
    private void runSave(String title, boolean share, boolean download){
        StorageController storageController=new StorageController(getApplicationContext());
        storageController.runSave(title, filename, uri, share, download);
    }//runSave()
}//UploadActivityClass
