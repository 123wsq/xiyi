package com.example.wsq.android.tools;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.example.wsq.android.utils.BitmapUtils;
import com.example.wsq.android.utils.DensityUtil;
import com.example.wsq.android.utils.SelectorUtils;

/**
 * Created by Administrator on 2018/1/30 0030.
 */

public class AppImageView {

    /**
     * 设置RadioButton的图片
     * @param context
     * @param radioButton
     * @param fileName
     */
    public static void onRadioButton(Context context, RadioButton radioButton, String fileName){
        Drawable drawable = new BitmapDrawable(BitmapUtils.onAssetsImages(context, fileName));
        drawable.setBounds(0, 0, DensityUtil.dp2px(context, 30), DensityUtil.dp2px(context, 30));
        if (drawable != null){
            radioButton.setCompoundDrawables(null, drawable, null, null);
        }

    }

    /**
     * ImageView设置显示的图片
     * @param context
     * @param imageView
     * @param fileName
     */
    public static void onImageView (Context context, ImageView imageView, String fileName){

        imageView.setImageBitmap(BitmapUtils.onAssetsImages(context, fileName));
    }

    /**
     * ImageView动态设置选择器
     * @param context
     * @param imageView
     * @param normalImage
     * @param selectImage
     */
    public static void onImageSelect(Context context, ImageView imageView, String normalImage, String selectImage){
        StateListDrawable drawable = SelectorUtils.newSelector(context,
                BitmapUtils.onAssetsImages(context, normalImage),
                BitmapUtils.onAssetsImages(context, selectImage));

        LayoutParams params = imageView.getLayoutParams();
        params.width = LayoutParams.WRAP_CONTENT;
        params.height =LayoutParams.WRAP_CONTENT;
        imageView.setLayoutParams(params);
        imageView.setBackground(drawable);
//       imageView.setImageDrawable(drawable);

    }
}
