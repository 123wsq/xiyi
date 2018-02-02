package com.example.medialib.video;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.medialib.R;
import com.example.medialib.adapter.VideoAdapter;
import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.NiceVideoPlayerManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/2/1 0001.
 */

public class VideoPlayListActvity extends AppCompatActivity implements RecyclerView.RecyclerListener {

    private RecyclerView rv_RecyclerView;
    private String url = "http://play.g3proxy.lecloud.com/vod/v2/MjUxLzE2LzgvbGV0di11dHMvMTQvdmVyXzAwXzIyLTExMDc2NDEzODctYXZjLTE5OTgxOS1hYWMtNDgwMDAtNTI2MTEwLTE3MDg3NjEzLWY1OGY2YzM1NjkwZTA2ZGFmYjg2MTVlYzc5MjEyZjU4LTE0OTg1NTc2ODY4MjMubXA0?b=259&mmsid=65565355&tm=1499247143&key=f0eadb4f30c404d49ff8ebad673d3742&platid=3&splatid=345&playid=0&tss=no&vtype=21&cvid=2026135183914&payff=0&pip=08cc52f8b09acd3eff8bf31688ddeced&format=0&sign=mb&dname=mobile&expect=1&tag=mobile&xformat=super";
    private List<String> mData;
    private VideoAdapter mAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_video_list);

        initView();
    }
    public void initView(){
        rv_RecyclerView = findViewById(R.id.rv_RecyclerView);
        mData = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mData.add(url);
        }
//        rv_RecyclerView.addItemDecoration(new RecyclerViewDivider(
//                this, LinearLayoutManager.HORIZONTAL, 2,
//                ContextCompat.getColor(this, R.color.default_backgroud_color)));
        rv_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rv_RecyclerView.setHasFixedSize(true);

        rv_RecyclerView.setRecyclerListener(this);

        mAdapter = new VideoAdapter(this, mData);
        rv_RecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        NiceVideoPlayer niceVideoPlayer = ((VideoAdapter.ViewHolder) holder).nice_video_player;
        if (niceVideoPlayer == NiceVideoPlayerManager.instance().getCurrentNiceVideoPlayer()) {
            NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
        }
    }
}
