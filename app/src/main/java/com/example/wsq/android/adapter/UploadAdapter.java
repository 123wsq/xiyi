package com.example.wsq.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.wsq.android.R;
import com.example.wsq.android.bean.CameraBean;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.utils.DensityUtil;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;


/**
 * Created by wsq on 2017/12/13.
 */

public class UploadAdapter extends BaseAdapter{

    private Context mContext;
    private List<CameraBean> mData;
    private MediaMetadataRetriever mRetriever;

    public UploadAdapter(Context context, List<CameraBean> data){
        this.mContext = context;
        this.mData = data;
        mRetriever = new MediaMetadataRetriever();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int itemSize = width / Constant.IMAGE_COUNT -10;
        if (convertView== null){

            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_camera_item, parent, false);
            holder = new ViewHolder();
            convertView.setTag(holder);

            holder.iv_pictrue = convertView.findViewById(R.id.iv_pictrue);
            holder.iv_delete = convertView.findViewById(R.id.iv_delete);
            holder.ll_layout = convertView.findViewById(R.id.ll_layout);
            holder.iv_video = convertView.findViewById(R.id.iv_video);
            convertView.setLayoutParams(new RecyclerView.LayoutParams(itemSize, itemSize));
        }else{
            holder = (ViewHolder) convertView.getTag();
        }



        holder.iv_pictrue.setLayoutParams(new RelativeLayout.LayoutParams(itemSize
                - DensityUtil.dp2px(mContext, 40),
                itemSize - DensityUtil.dp2px(mContext, 40)));
        if (mData.get(position).getType() == 1){
            holder.iv_pictrue.setVisibility(View.VISIBLE);
            holder.iv_delete.setVisibility(View.GONE);
            holder.iv_pictrue.setImageResource(R.drawable.image_add);
            holder.iv_video.setVisibility(View.GONE);

        }else if(mData.get(position).getType() == 3){
            holder.iv_video.setVisibility(View.VISIBLE);
            File file = new File(mData.get(position).getFile_path());
            if (file.exists()){
                mRetriever.setDataSource(file.getAbsolutePath());
                Bitmap bitmap=mRetriever.getFrameAtTime();
                holder.iv_pictrue.setImageBitmap(bitmap);

            }else{
                RequestOptions options = new RequestOptions();
                options.error(R.drawable.image_no);
                options.fallback(R.drawable.default_image);
                options.placeholder(R.drawable.default_image);
                Glide.with(mContext)
                        .load(mData.get(position).getFile_path())
                        .apply(options)
                        .into(holder.iv_pictrue);

//                new MyAsyncTask(mData.get(position).getFile_path(), holder.iv_pictrue).execute();
//                holder.iv_pictrue.setImageResource(R.drawable.default_image);

            }
        }else{
            holder.iv_pictrue.setVisibility(View.VISIBLE);
            holder.iv_video.setVisibility(View.GONE);
            if (mData.get(position).getFile_path().startsWith("http:")){
                RequestOptions options = new RequestOptions();
                options.error(R.drawable.image_no);
                options.fallback(R.drawable.default_image);
                options.placeholder(R.drawable.default_image);
                Glide.with(mContext)
                        .load(mData.get(position).getFile_path())
                        .apply(options)
                        .into(holder.iv_pictrue);

            }else{
                holder.iv_pictrue.setImageBitmap(getLoacalBitmap(mData.get(position).getFile_path()));
            }

        }
        if (mData.get(position).isShow()){
            holder.iv_delete.setVisibility(View.VISIBLE);
        }else{
            holder.iv_delete.setVisibility(View.GONE);
        }

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setAction(Constant.ACTION.IMAGE_DELETE);
                intent.putExtra("filePath",mData.get(position).getFile_path());
                mContext.sendBroadcast(intent);
            }
        });

        return convertView;
    }


    /**
     * 加载本地图片
     * http://bbs.3gstdy.com
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    class ViewHolder{
        ImageView iv_pictrue;
        ImageView iv_delete;
        ImageView iv_video;
        RelativeLayout ll_layout;
    }

    class MyAsyncTask extends AsyncTask<String, String,Bitmap>{

        private ImageView imageView;
        private String url;
        MediaMetadataRetriever mRetriever;

        public MyAsyncTask(String url, ImageView imageView){
            this.url = url;
            this.imageView = imageView;
            mRetriever = new MediaMetadataRetriever();
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Logger.d("视频请求地址：  "+ url);
            mRetriever.setDataSource(url, new HashMap<String, String>());
            Bitmap bitmap=mRetriever.getFrameAtTime(1000);
//            createVideoThumbnail(
//                    url, 100, 100)
            return  bitmap;
        }


        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
//            mRetriever.release();
            Logger.d("视频请求结束："+  bitmap);
            if (bitmap != null){
                Logger.d("视频加载完成");
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    private Bitmap createVideoThumbnail(String url, int width, int height) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }
}
