package com.example.medialib.video;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;


import com.example.medialib.R;
import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.NiceVideoPlayerManager;
import com.xiao.nicevideoplayer.TxVideoPlayerController;

import java.io.File;


/**
 * Created by wsq on 2017/12/27.
 */

public class VideoPlayActivity extends AppCompatActivity {


    NiceVideoPlayer mNiceVideoPlayer;

    String url;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_video_play);
        init();
    }

    public void init(){

        mNiceVideoPlayer = findViewById(R.id.nice_video_player);

        url = getIntent().getStringExtra("URL");
        if (TextUtils.isEmpty(url)){

            Toast.makeText(this, "视频地址不能为空", Toast.LENGTH_SHORT).show();
            finish();
        }
        File file = new File(url);
        if (file.exists()){
            mNiceVideoPlayer.setPlayerType(NiceVideoPlayer.TYPE_NATIVE); // or NiceVideoPlayer.TYPE_NATIVE
        }else {
            mNiceVideoPlayer.setPlayerType(NiceVideoPlayer.TYPE_IJK); // or NiceVideoPlayer.TYPE_NATIVE
        }

        mNiceVideoPlayer.setUp(url, null);

        TxVideoPlayerController controller = new TxVideoPlayerController(this);
        controller.setTitle("");
        controller.setImage(R.drawable.image_start);
        mNiceVideoPlayer.setController(controller);

    }



    @Override
    protected void onStop() {
        super.onStop();
        // 在onStop时释放掉播放器
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
    }
    @Override
    public void onBackPressed() {
        // 在全屏或者小窗口时按返回键要先退出全屏或小窗口，
        // 所以在Activity中onBackPress要交给NiceVideoPlayer先处理。
        if (NiceVideoPlayerManager.instance().onBackPressd()) return;
        super.onBackPressed();
    }
}
