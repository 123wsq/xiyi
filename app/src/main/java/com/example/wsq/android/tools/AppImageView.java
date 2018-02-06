package com.example.wsq.android.tools;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wsq.android.R;
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

    public static void onRadioButton(Context context, RadioButton radioButton, String fileName, int width, int height){
        Drawable drawable = new BitmapDrawable(BitmapUtils.onAssetsImages(context, fileName));
        drawable.setBounds(0, 0, DensityUtil.dp2px(context, width), DensityUtil.dp2px(context, height));
        if (drawable != null){
            radioButton.setCompoundDrawables(null, drawable, null, null);
        }

    }

    public static void onRadioButton(Context context, RadioButton radioButton, int drawableID, int width, int height){
       Drawable drawable =  context.getResources().getDrawable(drawableID);
        drawable.setBounds(0, 0, DensityUtil.dp2px(context, width), DensityUtil.dp2px(context, height));
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
     * 动态设置layout的背景色
     * @param context
     * @param layout
     * @param fileName
     */
    public static void onLayoutBackgroundImage (Context context, LinearLayout layout, String fileName){

        layout.setBackground(new BitmapDrawable(BitmapUtils.onAssetsImages(context, fileName)));
    }
    /**
     * 动态设置layout的背景色
     * @param context
     * @param layout
     * @param fileName
     */
    public static void onLayoutBackgroundImage (Context context, RelativeLayout layout, String fileName){

        layout.setBackground(new BitmapDrawable(BitmapUtils.onAssetsImages(context, fileName)));
    }

    /**
     * 给TextView设置背景色
     * @param context
     * @param text
     * @param color
     */
    public static void onLayoutBackgroundImage (Context context, TextView text, String color){

        GradientDrawable drawable = (GradientDrawable) text.getBackground();
        drawable.setCornerRadius(DensityUtil.dp2px(context, 5));
        drawable.setColor(Color.parseColor(color));
        text.setBackground(drawable);
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

    /**
     * RadioButton动态选择器
     * @param context
     * @param radioButton
     * @param mormalImage
     * @param checkedImage
     */
    public static void onRadioButtonSelect(Context context, RadioButton radioButton, String mormalImage, String checkedImage){

        StateListDrawable drawable = SelectorUtils.newSelector(context,
                BitmapUtils.onAssetsImages(context, mormalImage),
                BitmapUtils.onAssetsImages(context, checkedImage));
        drawable.setBounds(0, 0, DensityUtil.dp2px(context, 30), DensityUtil.dp2px(context, 30));

        if (drawable != null){
            radioButton.setCompoundDrawables(null, drawable, null, null);
        }
    }
}
