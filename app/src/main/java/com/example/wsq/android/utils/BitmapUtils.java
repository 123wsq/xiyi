package com.example.wsq.android.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.tools.AppImageLoad;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by wsq on 2017/12/29.
 */

public class BitmapUtils {


    /**
     * 将本地SD卡中的图片转换成Bitmap图
     * @param path
     * @return
     */
    public static Bitmap getLocalImage(String path){

        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inPreferredConfig = Bitmap.Config.ARGB_4444;

        Bitmap img = BitmapFactory.decodeFile(path, options);
        return img;
    }


    /**
     * 将一个Bitmap保存到本地sd卡
     * @param bmp
     * @return
     */
    public static File saveImage(Bitmap bmp) {

        File appDir = new File(Constant.BITMAP_PATH);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


//    /**
//     * 给一个Bitmap添加水印
//     * @param context
//     * @param path
//     * @return
//     */
//    public static String addBitmapWatermark(Context context, String path){
//
//        Bitmap bitmap = ImageUtil.drawTextToLeftBottom(
//                context,
//                BitmapUtils.getLocalImage(path),
//                DateUtil.onDateFormat(DateUtil.DATA_FORMAT),
//                Color.WHITE,
//                15,
//                0,
//                0);
//
//        File file = saveImage(bitmap);
//        return file.getAbsolutePath();
//    }


    /**
     * 读取assets中的图片
     * @param context
     * @param fileName
     * @return
     */
    public static Bitmap onAssetsImages(Context context, String fileName){

        AssetManager manager = context.getAssets();
        InputStream is = null;
        try {
            is = manager.open(AppImageLoad.getPath(context)+fileName);

        } catch (IOException e) {

            //读取本地资源失败
            try {
                is = manager.open(AppImageLoad.defaultPath+ fileName);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);

        return bitmap;
    }

    /**
     * Drawable--->Bitmap
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {

        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
//        System.out.println("Drawable转Bitmap");
        Bitmap.Config config =
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        //注意，下面三行代码要用到，否则在View或者SurfaceView里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);

        return bitmap;
    }
}
