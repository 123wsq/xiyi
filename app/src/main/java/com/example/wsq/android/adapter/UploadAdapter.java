package com.example.wsq.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.wsq.android.R;
import com.example.wsq.android.bean.CameraBean;
import com.example.wsq.android.constant.Constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

        if (convertView== null){

            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_camera_item, parent, false);
            holder = new ViewHolder();
            convertView.setTag(holder);

            holder.iv_pictrue = convertView.findViewById(R.id.iv_pictrue);
            holder.iv_delete = convertView.findViewById(R.id.iv_delete);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        if (mData.get(position).getType() == 1){
            holder.iv_delete.setVisibility(View.GONE);
            holder.iv_pictrue.setImageResource(R.drawable.image_add);
        }else if(mData.get(position).getType() == 3){

            File file = new File(mData.get(position).getFile_path());
            if (file.exists()){
                mRetriever.setDataSource(file.getAbsolutePath());
                Bitmap bitmap=mRetriever.getFrameAtTime();
                holder.iv_pictrue.setImageBitmap(bitmap);
            }
        }else{
            if (mData.get(position).getFile_path().startsWith("http:")){
                Glide.with(mContext).load(mData.get(position).getFile_path()).into(holder.iv_pictrue);

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
                intent.putExtra("filePath",mData.get(position).file_path);
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
    }
}
