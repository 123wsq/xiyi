package com.example.medialib.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.medialib.R;
import com.example.medialib.bean.MediaFileBean;
import com.xiao.nicevideoplayer.TxVideoPlayerController;

import java.util.List;

/**
 * Created by wsq on 2017/12/19.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>{

    private Context mContext;
    private List<MediaFileBean> mData;
    protected boolean isScrolling = false;


    public ImageAdapter(Context context, List<MediaFileBean> list){
        this.mContext = context;
        this.mData = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_image_item, parent, false);
        ViewHolder holder  = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int itemSize = width / 4;

        holder.rl_layout.setLayoutParams(new LinearLayout.LayoutParams(itemSize-2,
                itemSize -20));

        if (isScrolling){
            holder.iv_image.setImageResource(R.drawable.default_image);
        }else {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap bitmap = BitmapFactory.decodeFile(mData.get(position).getPath(), options);
            holder.iv_image.setImageBitmap(bitmap);
        }
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    public void setScrolling(boolean scrolling) {
        isScrolling = scrolling;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv_image;
        private CheckBox cb_CheckBox;
        private RelativeLayout rl_layout;
        public ViewHolder(View itemView) {
            super(itemView);
            iv_image = itemView.findViewById(R.id.iv_image);
            cb_CheckBox = itemView.findViewById(R.id.cb_CheckBox);
            rl_layout = itemView.findViewById(R.id.rl_layout);
        }

    }

    public static int dp2px(Context context, float dpValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
