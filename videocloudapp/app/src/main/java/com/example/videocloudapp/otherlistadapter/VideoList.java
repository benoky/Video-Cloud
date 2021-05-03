package com.example.videocloudapp.otherlistadapter;

public class VideoList {
    //private Bitmap thumbnail;
    private String otherTitle;
    private String otherName;
    private String otherNickname;
    private boolean share;
    private boolean download;
    private String email;

    public VideoList(String otherTitle, String otherName, String otherNickname, boolean share, boolean download, String email){
        //this.thumbnail=thumbnail;
        this.otherTitle=otherTitle;
        this.otherName=otherName;
        this.otherNickname=otherNickname;
        this.share=share;
        this.download=download;
        this.email=email;
    }
    //public Bitmap getThumbnail(){return  thumbnail;}
    public String getOtherTitle(){
        return otherTitle;
    }
    public String getOtherName(){
        return otherName;
    }
    public String getOtherNickname(){
        return otherNickname;
    }
    public boolean getShare(){
        return share;
    }
    public boolean getDownload() {return download;}
    public String getEmail() {return  email;}
}
