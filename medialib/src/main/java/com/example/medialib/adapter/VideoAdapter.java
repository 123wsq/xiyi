package com.example.medialib.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.medialib.R;
import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.TxVideoPlayerController;

import java.util.List;
import java.util.Map;

/**
 * Created by wsq on 2017/12/19.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder>{

    private Context mContext;
    private List<String> mData;
    public VideoAdapter(Context context, List<String> list){
        this.mContext = context;
        this.mData = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_video_item, parent, false);
        TxVideoPlayerController controller = new TxVideoPlayerController(mContext);
        ViewHolder holder = new ViewHolder(view);
        holder.setController(controller);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

//        holder.tv_name.setText(mData.get(position).get(ResponseKey.TITLE)+"");
//        holder.iv_next.setVisibility(View.VISIBLE);

        holder.bindData(mData.get(position));

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public NiceVideoPlayer nice_video_player;
        public TxVideoPlayerController mController;
        public ViewHolder(View itemView) {
            super(itemView);
            nice_video_player = itemView.findViewById(R.id.nice_video_player);
        }

        public void setController(TxVideoPlayerController controller) {
            mController = controller;
            nice_video_player.setController(mController);
        }

        public void bindData(String videoUrl) {
            mController.setTitle("111");
//            mController.setLenght(v);
//            Glide.with(itemView.getContext())
//                    .load(video.getImageUrl())
//                    .placeholder(R.drawable.img_default)
//                    .crossFade()
//                    .into(mController.imageView());
            nice_video_player.setUp(videoUrl, null);
        }

    }
}
