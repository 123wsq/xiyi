package com.example.wsq.android.activity.order;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.wsq.android.R;
import com.example.wsq.android.tools.AppStatus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by wsq on 2017/12/27.
 */

public class VideoPlayActivity extends Activity {

//    @BindView(R.id.video_player)
//    NiceVideoPlayer video_player;
    @BindView(R.id.vv_VideoView)
    VideoView vv_VideoView;
    String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Vitamio.isInitialized(this);
        setContentView(R.layout.layout_video_play);
        AppStatus.onSetStates(this);
        ButterKnife.bind(this);
        init();
    }

    public void init(){

        //添加播放控制条,还是自定义好点
//        video_player.setMediaController(new MediaController(this));

        url = getIntent().getStringExtra("URL");
        if (TextUtils.isEmpty(url)){

            Toast.makeText(this, "视频地址不能为空", Toast.LENGTH_SHORT).show();
            finish();
        }

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        vv_VideoView.setVideoPath(url);
        vv_VideoView.setMediaController(new MediaController(this));
//        vv_VideoView.set
        vv_VideoView.requestFocus();

        vv_VideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                // optional need Vitamio 4.0
                mediaPlayer.setPlaybackSpeed(1.0f);
            }
        });
        vv_VideoView.start();


//        video_player.setPlayerType(NiceVideoPlayer.TYPE_IJK); // or NiceVideoPlayer.TYPE_NATIVE
//        video_player.setUp(url, null);
//        video_player.isFullScreen();
//        TxVideoPlayerController controller = new TxVideoPlayerController(this);
//        controller.setTitle("蜥蜴");
//        video_player.setController(controller);

    }

    @Override
    protected void onStop() {
        super.onStop();
//        PictureFileUtils.deleteCacheDirFile(VideoPlayActivity.this);
        // 在onStop时释放掉播放器
//        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
    }

    @OnClick({R.id.iv_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
