package com.example.wsq.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wsq.android.R;
import com.example.wsq.android.inter.PopupItemListener;
import com.example.wsq.android.tools.RegisterParam;
import com.example.wsq.android.utils.ValidateParam;
import com.example.wsq.android.view.CustomPopup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wsq on 2017/12/11.
 */

public class RegiesterActivity2 extends Activity {

    @BindView(R.id.tv_xueli) TextView tv_xueli;
    @BindView(R.id.tv_juese) TextView tv_juese;
    @BindView(R.id.ll_layout) LinearLayout ll_layout;
    @BindView(R.id.et_bumen) EditText et_bumen;
    @BindView(R.id.et_company) EditText et_company;
    @BindView(R.id.et_location) EditText et_location;
    @BindView(R.id.ll_location) LinearLayout ll_location;
    @BindView(R.id.layout_company) LinearLayout layout_company;
    private CustomPopup popup;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_regiester2);
        ButterKnife.bind(this);
        init();

    }


    public void init() {

        RegisterParam.XUELI = 1;
        RegisterParam.JUESE = 1;
        et_location.setVisibility(View.VISIBLE);
    }
    @OnClick({R.id.btn_next, R.id.iv_back, R.id.tv_xueli, R.id.tv_juese})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_next:
                //判断当选择企业工程师的时候必须要填写公司

                if (RegisterParam.JUESE != 1){
                    if(ValidateParam.validateParamIsNull(et_company.getText().toString())){
                        Toast.makeText(RegiesterActivity2.this, "公司必填参数", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (RegisterParam.JUESE == 1){
                    if(ValidateParam.validateParamIsNull(et_location.getText().toString())){
                        Toast.makeText(RegiesterActivity2.this, "工作地点必须要填写", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                RegisterParam.BUMEN = et_bumen.getText().toString();
                RegisterParam.COMPANY = et_company.getText().toString();
                RegisterParam.DIQU = et_location.getText().toString();
                startActivity(new Intent(RegiesterActivity2.this, RegisterActivity3.class));
                break;
            case R.id.iv_back:

                finish();
                break;
            case  R.id.tv_xueli:
                View view = LayoutInflater.from(RegiesterActivity2.this).inflate(R.layout.layout_default_popup,null);
                List<String> list = new ArrayList<>();

                list.add("高中及以下");
                list.add("专科");
                list.add("本科");
                list.add("研究生");
                list.add("博士以上");
                 popup = new CustomPopup(RegiesterActivity2.this, view, list, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popup.dismiss();
                    }
                }, new PopupItemListener(){
                     @Override
                     public void onClickItemListener(int position, String result) {
                         tv_xueli.setText(result);
                         RegisterParam.XUELI = position+1;
                         popup.dismiss();
                     }
                 });
                popup.showAtLocation(ll_layout, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.tv_juese:


                View view1 = LayoutInflater.from(RegiesterActivity2.this).inflate(R.layout.layout_default_popup,null);
                List<String> list1 = new ArrayList<>();

                list1.add("服务工程师");
                list1.add("企业工程师");
                list1.add("企业管理工程师");
                popup = new CustomPopup(RegiesterActivity2.this, view1, list1, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popup.dismiss();
                    }
                }, new PopupItemListener(){
                    @Override
                    public void onClickItemListener(int position, String result) {
                        tv_juese.setText(result);
                        RegisterParam.JUESE = position+1;
                        popup.dismiss();
                        layout_company.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
                        ll_location.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
                    }
                });
                popup.showAtLocation(ll_layout, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
        }
    }
}
