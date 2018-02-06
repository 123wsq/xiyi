package com.example.medialib.picture;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.medialib.R;
import com.example.medialib.adapter.ImageAdapter;
import com.example.medialib.bean.MediaFileBean;
import com.example.medialib.tools.MediaType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2018/2/6 0006.
 */

public class ImageSelectorActvity extends Activity{

    RecyclerView rv_RecyclerView;

    private ImageAdapter mAdapter;
    private List<MediaFileBean> mData;
    private long startTime;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_image_list);

        initView();
    }

    private void initView() {
        rv_RecyclerView = this.findViewById(R.id.rv_RecyclerView);

        rv_RecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        rv_RecyclerView.setHasFixedSize(true);

        mData = new ArrayList<>();
        mAdapter = new ImageAdapter(this, mData);
        rv_RecyclerView.setAdapter(mAdapter);

        rv_RecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {


                Log.d("状态：", newState+"");
                if (newState == RecyclerView.SCROLL_STATE_IDLE) { // 滚动静止时才加载图片资源，极大提升流畅度
                    mAdapter.setScrolling(false);
                    mAdapter.notifyDataSetChanged(); // notify调用后onBindViewHolder会响应调用
                } else
                    mAdapter.setScrolling(true);
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        handler.postDelayed(thread, 1 * 1000);
    }




    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Log.d("finish", "加载完成");
            Log.d("finish", "用时 ： "+(System.currentTimeMillis()- startTime)+" ms");
            mAdapter.notifyDataSetChanged();
        }
    };

    Runnable thread = new Runnable() {
        @Override
        public void run() {

            startTime = System.currentTimeMillis();
            Log.d("start", "开始加载");
            onGetSdImage();
        }
    };

    public void onGetSdImage(){

        //音频  Audio.Media.EXTERNAL_CONTENT_URI
        //视频  Video.Media.EXTERNAL_CONTENT_URI
        Cursor cursor = getContentResolver().query(Uri.parse(getIntent().getStringExtra("type")), null, null, null, null);

        Log.d("获取的数据库格式", Arrays.toString(cursor.getColumnNames()));
        while (cursor.moveToNext()){

            //缩略图
            int id  = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));
//            Bitmap thumbnail = MediaStore.Images.Thumbnails.getThumbnail(getContentResolver(), id, MediaStore.Images.Thumbnails.MICRO_KIND, null);
            MediaFileBean bean = new MediaFileBean();
            bean.setName(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)));
            bean.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
//            bean.setThumbnail(thumbnail);
            bean.setDesc(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DESCRIPTION)));
            bean.setSize( cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.SIZE)));
            mData.add(bean);
        }
        cursor.close();
        Log.d("本地图片个数", mData.size()+"");
        handler.sendMessage(new Message());
    }

}
