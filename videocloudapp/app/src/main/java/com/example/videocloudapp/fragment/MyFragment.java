package com.example.videocloudapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.videocloudapp.MainActivity;
import com.example.videocloudapp.R;
import com.example.videocloudapp.VideoActivity;
import com.example.videocloudapp.controller.AuthController;
import com.example.videocloudapp.otherlistadapter.VideoList;
import com.example.videocloudapp.otherlistadapter.VideoListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyFragment extends Fragment {
    private MainActivity mainActivity;

    String name;
    String title;
    String nickname;
    boolean share;
    boolean download;
    String email;

    ArrayList<VideoList> videoLists;
    VideoListAdapter videoListAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //이제 더이상 엑티비티 참초가안됨
        mainActivity = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        videoLists =new ArrayList<>();
        videoListAdapter =new VideoListAdapter(videoLists, getLayoutInflater());
        //프래그먼트 메인을 인플레이트해주고 컨테이너에 붙여달라는 의미
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_my , container, false);

        ListView lvOtherList=rootView.findViewById(R.id.lv_mylist);


        lvOtherList.setAdapter(videoListAdapter);

        //DB에서 값을 가져오기 위한 객체 생성
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference root=firebaseDatabase.getReference();
        DatabaseReference videoListNode=root.child("VideoList");

        videoListNode.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String[] str=snapshot.getValue().toString().split("!_#@#_!");

                nickname=str[0];
                name=str[1];
                title=str[2];
                share=Boolean.parseBoolean(str[3]);
                download=Boolean.parseBoolean(str[4]);
                email=str[5];

                //Storage에 업로드 되어있는 파일이 현재 로그인한 사용자가 업로드한 파일인지 확인한다
                if(email.equals(new AuthController().getEmail())){
                    videoLists.add(new VideoList(title, name, nickname, share, download, email));
                    videoListAdapter.notifyDataSetChanged();
                }

                /*FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();

                StorageReference storageRef= firebaseStorage.getReference().child(name);

                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //Bitmap bitmap = ThumbnailUtils.createVideoThumbnail("/sdcard/Movies/creek_rock.mp4", MediaStore.Images.Thumbnails.MINI_KIND);
                        //Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bitmap, 115, 115);

                        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                        mediaMetadataRetriever.setDataSource("/sdcard/Movies/creek_rock.mp4");
                        Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime(1000000);//6초 영상 추출
                        Log.d("asdasd123",bitmap.toString());
                        videoLists.add(new VideoList(bitmap, title, name, nickname, share));
                        videoListAdapter.notifyDataSetChanged();
                    }
                });*/


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

        //리스트 뷰의 각 항목은 선택 했을때 동작
        lvOtherList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getActivity(), VideoActivity.class).
                        //현재 선택 한 항목의 정보를 VideoActivity에 넘겨줌
                                putExtra("email",videoLists.get(i).getEmail()).
                                putExtra("title", videoLists.get(i).getOtherTitle()).
                                putExtra("name", videoLists.get(i).getOtherName()).
                                putExtra("nickname", videoLists.get(i).getOtherNickname()).
                                putExtra("share", videoLists.get(i).getShare()).
                                putExtra("download", videoLists.get(i).getDownload())
                );
            }
        });

        return rootView;
    }
}
