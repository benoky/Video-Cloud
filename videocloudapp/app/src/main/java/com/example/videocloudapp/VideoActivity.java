package com.example.videocloudapp;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.videocloudapp.controller.AuthController;
import com.example.videocloudapp.controller.StorageController;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class VideoActivity extends AppCompatActivity {
    public static Context mContext;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageRef;

    //파일을 다운로드 하기 위한 객체
    private long enqueue;
    private DownloadManager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        mContext=this;

        ImageButton ibtDownload=(ImageButton)findViewById(R.id.ibt_download);
        ImageButton ibtDelete=(ImageButton)findViewById(R.id.ibt_delete);
        TextView tvTitle=(TextView)findViewById(R.id.tv_title);
        TextView tvName=(TextView)findViewById(R.id.tv_name);
        TextView tvNickname=(TextView)findViewById(R.id.tv_nickname);
        VideoView vvVideo=(VideoView)findViewById(R.id.vv_video);

        //비디오 뷰를 컨트롤 하기 위한 미디어 컨트롤러 생성, video view 영역을 누르면 하단에 나타난다.
        MediaController controller=new MediaController(this);
        vvVideo.setMediaController(controller);

        //다른 Activity로부터 Extra를 통해 넘겨 받은 값들을 저장하기 위한 변수
        String email=getIntent().getStringExtra("email");
        String title=getIntent().getStringExtra("title");
        String name=getIntent().getStringExtra("name");
        String nickname=getIntent().getStringExtra("nickname");
        boolean share=getIntent().getBooleanExtra("share",false);
        boolean download=getIntent().getBooleanExtra("download",false);

        //텍스트 뷰에 파일 정보 출력
        tvTitle.setText(title);
        tvName.setText(name);
        tvNickname.setText(nickname);

        //Storage를 참조하기 위한 객체 선언
        firebaseStorage=FirebaseStorage.getInstance();
        storageRef=firebaseStorage.getReference().child(name);

        //Storage에서 파일의 uri를 가져와 videoView에 출력한다
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                vvVideo.setVideoURI(uri);
                vvVideo.setVisibility(View.VISIBLE);
                vvVideo.start();
            }
        });

        //화면 상단의 download버튼 선택 시 동작
        ibtDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //해당 영상을 올린 사용자가 현재 로그인한 사용자와 같다면 다운로드 허가 여부와 상관없이 다운로드 가능
                if(email.equals(new AuthController().getEmail())){
                    runDownload(name);
                }else if(download==true){
                    runDownload(name);
                }else{
                    Toast.makeText(mContext,"사용자가 다운로드 허가 하지 않았습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //화면 상단의 delete버튼 선택 시 동작
        ibtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //삭제 버튼 동작 시 해당 동영상이 현재 로그인한 사용자가 올린 동영상일 경우에만 삭제 가능
                if(email.equals(new AuthController().getEmail())){
                    StorageController storageController=new StorageController();
                    storageController.runDelete(nickname,title,name,share,download,email);
                }else{
                    Toast.makeText(mContext,"삭제 권한이 없습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //동영상 파일을 SD카드에 저장하기 위한 권한 요청
        permissionRequest();
    }//onCreate()

    //화면 상단의 delete버튼 선택 시 동작하는 메소드
    private void runDownload(String name) {
        //파일의 Uri를 받아와 다운로드 매니저를 통해 SD카드의 DOWNLOAD파일에 저장
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,name);
                request.setTitle(name);// 다운로드 제목
                request.setDescription("다운로드 중..");// 다운로드 설명
                request.setNotificationVisibility(1);// 상단바에 완료 결과 출력
                enqueue = dm.enqueue(request);
                Toast.makeText(mContext,"다운로드 완료",Toast.LENGTH_SHORT).show();
            }
        });
    }//runDownload()

    //동영상 파일을 SD카드에 저장하기 위한 권한 요청 메소드
    private void permissionRequest(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int PERMISSION_REQUEST_CODE = 1;
            if (ContextCompat.checkSelfPermission(VideoActivity.this, Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(VideoActivity.this, Manifest.permission.MANAGE_EXTERNAL_STORAGE)) {
                } else {
                    ActivityCompat.requestPermissions(VideoActivity.this, new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                }
            }
        }
    }
}//VideoActivityClass
