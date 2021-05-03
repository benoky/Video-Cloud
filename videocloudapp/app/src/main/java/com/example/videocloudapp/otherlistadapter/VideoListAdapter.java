package com.example.videocloudapp.otherlistadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.videocloudapp.R;

import java.util.ArrayList;

public class VideoListAdapter extends BaseAdapter {
    private ArrayList<VideoList> videoLists;
    private LayoutInflater inflater;

    public VideoListAdapter(ArrayList<VideoList> videoLists, LayoutInflater inflater){
        this.videoLists = videoLists;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return videoLists.size();
    }

    @Override
    public Object getItem(int i) {
        return videoLists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        VideoList videoList = videoLists.get(i);
        View itemView = inflater.inflate(R.layout.list_video, viewGroup, false);

        TextView tvOtherTitle=itemView.findViewById(R.id.tv_videotitle);
        TextView tvOtherName=itemView.findViewById(R.id.tv_videoname);
        TextView tvOtherNickname=itemView.findViewById(R.id.tv_videonickname);


        tvOtherTitle.setText(videoList.getOtherTitle());
        tvOtherName.setText(videoList.getOtherName());
        tvOtherNickname.setText(videoList.getOtherNickname());

        return itemView;
    }
}

