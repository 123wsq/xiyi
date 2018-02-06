package com.example.medialib.tools;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.example.medialib.bean.MediaFileBean;
import com.example.medialib.picture.ImageSelectorActvity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/2/6 0006.
 */

public class MediaSelector {

    public MediaSelector(){

    }

    public static class Builder{
        private Activity mContext;               //上下文对象
        private boolean isSelect = false;       //是否选择 默认不可选择
        private boolean isCrop = false;         //是否可裁剪 默认不可裁剪
        //
//        MediaStore.Video.Media.EXTERNAL_CONTENT_URI;  视频
//        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;  音频
        private Uri type = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;//类型
        private int selectMaxCount = 10;        //最多选择个数，默认是10
        private int selectMinCount = 0;         //最少选择数量，默认为0
        private int imageSpanCount = 3;         //每行显示个数
        private boolean selectionMode = true;   //是否多选， 默认为多选
        private boolean previewImage = true;    //是否可以预览图片
        private boolean previewVideo = true;    //是否可以预览视频
        private boolean isCamera = true;        //是否显示拍照按钮
        private String imageFormat = "jpg";     //保存图片的后缀， 默认为jpg
        private boolean isCompress = false;     //是否压缩 ， 默认不压缩
        private boolean circleDimmedLayer = false;//是否圆形裁剪  默认不是
        private int minCompressSize = 200;      //最小压缩大小
        private int videoMaxSecond = 60;        //视频录制时长， 默认60s
        private int resultCode = Activity.RESULT_OK;//返回值
        private List<MediaFileBean> mImageList;  //预览图、默认选中等, 该参数只支持图片

        private Bundle bundle;

        public Builder(Activity context){
            this.mContext = context;
        }

        public  Builder setSelect(boolean isSelect){
            this.isSelect = isSelect;
            return this;
        }
        public  Builder setCrop(boolean isCrop){
            this.isCrop = isCrop;
            return this;
        }
        public  Builder setMediaType(Uri uri){
            this.type = uri;
            return this;
        }

        public  Builder setMaxSelectCount(int maxCount){
            this.selectMaxCount = maxCount;
            return this;
        }
        public  Builder setMinSelectCount(int minCount){
            this.selectMinCount = minCount;
            return this;
        }
        public  Builder setSpanCount(int spanCount){
            this.imageSpanCount = spanCount;
            return this;
        }
        public  Builder setSelectMode(boolean selectionMode){
            this.selectionMode = selectionMode;
            return this;
        }

        public  Builder setPreviewImage(boolean previewImage){
            this.previewImage = previewImage;
            return this;
        }

        public  Builder setPreViewVideo(boolean previewVideo){
            this.previewVideo = previewVideo;
            return this;
        }

        public  Builder setIsCamera(boolean isCamera){
            this.isCamera = isCamera;
            return this;
        }
        public  Builder setImageFormat(String imageFormat){
            this.imageFormat = imageFormat;
            return this;
        }

        public Builder setIsCompress(boolean isCompress){
            this.isCompress = isCompress;
            return this;
        }

        public Builder setCircleDimmedLayer(boolean circleDimmedLayer){
            this.circleDimmedLayer = circleDimmedLayer;
            return this;
        }

        public Builder setMinCompressSize(int minCompressSize){
            this.minCompressSize = minCompressSize;
            return this;
        }
        public Builder setVideoMaxSecond(int videoMaxSecond){
            this.videoMaxSecond = videoMaxSecond;
            return this;
        }
        public Builder setImageList(List<MediaFileBean> list){
                this.mImageList = list;
            return this;
        }

        public Builder setResultCode(int resultCode){
            this.resultCode = resultCode;
            return this;
        }

        public Builder create(){
            bundle = new Bundle();
            bundle.putBoolean("isSelect", isSelect);
            bundle.putBoolean("isCrop", isCrop);
            bundle.putString("type", type.toString());
            bundle.putInt("selectMaxCount", selectMaxCount);
            bundle.putInt("selectMinCount", selectMinCount);
            bundle.putInt("imageSpanCount", imageSpanCount);
            bundle.putInt("minCompressSize", minCompressSize);
            bundle.putInt("videoMaxSecond", videoMaxSecond);
            bundle.putString("imageFormat", imageFormat);
            bundle.putBoolean("selectionMode", selectionMode);
            bundle.putBoolean("previewImage", previewImage);
            bundle.putBoolean("previewVideo", previewVideo);
            bundle.putBoolean("isCamera", isCamera);
            bundle.putBoolean("isCompress", isCompress);
            bundle.putBoolean("circleDimmedLayer", circleDimmedLayer);
            bundle.putInt("resultCode", resultCode);
            bundle.putSerializable("list", (Serializable) mImageList);
            return this;
        }
        /**
         * 打开照相机
         * @return
         */
        public Builder openCamera(){

        return this;
        }

        /**
         * 打开相册选择文件
         * @return
         */
        public Builder openPhotoAlbum(){

            Intent intent =  new Intent(mContext, ImageSelectorActvity.class);
            if (bundle != null){
                intent.putExtras(bundle);
            }else{
                try {
                    throw new Exception("未执行 create() ");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return this;
            }

            mContext.startActivityForResult(intent, resultCode);
            return this;
        }

        /**
         * 打开摄像机
         */
        public Builder openCameraShooting(){

            return this;
        }

        /**
         * 打开视频选择
         */
        public Builder openVideoAlbum(){

            return this;
        }
    }
}
