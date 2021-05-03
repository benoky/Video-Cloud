package com.example.videocloudapp.controller;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;

import com.example.videocloudapp.UploadActivity;
import com.example.videocloudapp.VideoActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class StorageController {
    private final String STORAGEURI="gs://videocloud2.appspot.com";

    private Context context;

    private FirebaseStorage firebaseStorage;

    public StorageController(Context context){
        this.context=context;
    }

    public StorageController(){

    }

    //Stroage에 파일을 저장하는 메소드
    public void runSave(String title, String filename, Uri uri,boolean share, boolean download){
        firebaseStorage=FirebaseStorage.getInstance();
        StorageReference storageRef=firebaseStorage.getReferenceFromUrl(STORAGEURI);

        storageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for(StorageReference item:listResult.getItems()){
                    File file=new File(item.getPath());
                    if(!file.getName().equals(filename)){
                        Toast.makeText(context.getApplicationContext(),"파일 저장 완료",Toast.LENGTH_LONG).show();
                        //storage에 파일을 저장한다.
                        UploadTask uploadTask=storageRef.child(filename).putFile(uri);

                        //DB에 동영상 파일의 정보를 저장한다.
                        DBController dbController=new DBController();
                        dbController.upLoadVideoInfo(title, filename, share, download);

                        break;
                    }else {
                        Toast.makeText(context.getApplicationContext(),"이미 같은 이름의 파일이 있습니다. 파일 이름을 변경해 주세요.",Toast.LENGTH_LONG).show();
                        break;
                    }
                }
            }
        });//addOnSuccessListener()
    }//runSave()

    public void runDelete(String nickname2, String title, String filename,boolean share, boolean download, String email){

        firebaseStorage=FirebaseStorage.getInstance();
        StorageReference storageRef=firebaseStorage.getReferenceFromUrl(STORAGEURI).child(filename);

        storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                new DBController().deleteVideoInfo(nickname2,title, filename, share, download, email);
                Toast.makeText(VideoActivity.mContext,"삭제 완료",Toast.LENGTH_SHORT).show();
                ((VideoActivity) VideoActivity.mContext).finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }//runDelete()



}//StorageControllerClass
