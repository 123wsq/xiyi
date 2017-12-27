package com.example.wsq.android.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.wsq.android.R;
import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.NiceVideoPlayerManager;
import com.xiao.nicevideoplayer.TxVideoPlayerController;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wsq on 2017/12/27.
 */

public class VideoPlayActivity extends AppCompatActivity{

    @BindView(R.id.video_player)
    NiceVideoPlayer video_player;
    String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_video_play);

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

//        RelativeLayout.LayoutParams layoutParams=
//                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
//                        RelativeLayout.LayoutParams.FILL_PARENT);
//        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//        video_player.setLayoutParams(layoutParams);


        video_player.setPlayerType(NiceVideoPlayer.TYPE_IJK); // or NiceVideoPlayer.TYPE_NATIVE
        video_player.setUp(url, null);

        TxVideoPlayerController controller = new TxVideoPlayerController(this);
        controller.setTitle("蜥蜴");
//        controller.setImage(mImageUrl);
        video_player.setController(controller);

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

    @OnClick({R.id.iv_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
