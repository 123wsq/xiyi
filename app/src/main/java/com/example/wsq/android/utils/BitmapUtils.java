package com.example.wsq.android.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.example.wsq.android.constant.Constant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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


    /**
     * 给一个Bitmap添加水印
     * @param context
     * @param path
     * @return
     */
    public static String addBitmapWatermark(Context context, String path){

        Bitmap bitmap = ImageUtil.drawTextToLeftBottom(
                context,
                BitmapUtils.getLocalImage(path),
                DateUtil.onDateFormat(DateUtil.DATA_FORMAT),
                Color.BLACK,
                15,
                0,
                0);

        File file = saveImage(bitmap);
        return file.getAbsolutePath();
    }
}
