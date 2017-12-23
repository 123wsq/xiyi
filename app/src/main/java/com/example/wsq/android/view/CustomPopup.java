package com.example.wsq.android.view;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.inter.PopupItemListener;
import com.example.wsq.android.utils.DensityUtil;

import java.util.List;

/**
 * Created by wsq on 2017/12/12.
 */

public class CustomPopup extends PopupWindow{

    private Activity mContext;
    private View popupView;
    private List<String> mData;
    private View.OnClickListener onClickListener;
    private PopupItemListener listener;
    public CustomPopup(Activity context, View view, List<String> list, View.OnClickListener clickListener, final PopupItemListener listener){

        this.mContext = context;
        this.popupView = view;
        this.mData = list;
        this.onClickListener = clickListener;
        this.popupView = view;
        this.listener = listener;
        TextView tv_cancel =  popupView.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(onClickListener);
        ListView listview = popupView.findViewById(R.id.listview);
        MyAdapter adapter = new MyAdapter();
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onClickItemListener(position, mData.get(position));
            }
        });

        initPopup();

    }

    public void initPopup(){

        int w = mContext.getResources().getDisplayMetrics().widthPixels;
        int h = mContext.getResources().getDisplayMetrics().heightPixels;
        // 设置按钮监听
        // 设置SelectPicPopupWindow的View
        this.setContentView(popupView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth((int)(w*0.9));
        //
        // 设置SelectPicPopupWindow弹出窗体的高
        int height = 0;
        if (null != mData) {
            if (h / 2 <= DensityUtil.dp2px(mContext, (mData.size() * 50) + 90 )) {
                height = h / 2;
            } else {
                height = DensityUtil.dp2px(mContext, (mData.size() * 50) + 90);
            }
        }else{
            height = h/2;
        }
        this.setHeight(height);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.style_pop);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);
        //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
        this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);

//        this.setOnDismissListener(new PoponDismissListener());

        popupView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub

//					dismiss();

                return false;
            }
        });

    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        backgroundAlpha(0.5f);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        backgroundAlpha(1f);
    }

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        mContext.getWindow().setAttributes(lp);
    }


    class MyAdapter extends BaseAdapter{

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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (convertView == null){

                convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_default_popup_item, null);
                holder = new ViewHolder();
                convertView.setTag(holder);
                holder.tv_popup_name = convertView.findViewById(R.id.tv_popup_name);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_popup_name.setText(mData.get(position)+"");
            return convertView;
        }

        class ViewHolder{

            TextView tv_popup_name;
        }
    }


}
