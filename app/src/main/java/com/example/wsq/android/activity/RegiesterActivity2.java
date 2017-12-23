package com.example.wsq.android.activity;

import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wsq.android.R;
import com.example.wsq.android.base.BaseActivity;
import com.example.wsq.android.inter.PopupItemListener;
import com.example.wsq.android.tools.RegisterParam;
import com.example.wsq.android.utils.ValidateParam;
import com.example.wsq.android.view.CustomPopup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wsq on 2017/12/11.
 */

public class RegiesterActivity2 extends BaseActivity implements View.OnClickListener {

    private Button btn_next;
    private ImageView iv_back;
    private TextView tv_xueli, tv_juese;
    private LinearLayout ll_layout;
    private EditText et_bumen, et_company;

    private CustomPopup popup;

    private LinearLayout layout_company;


    @Override
    public int getByLayoutId() {
        return R.layout.layout_regiester2;
    }

    @Override
    public void init() {

        RegisterParam.XUELI = 1;
        RegisterParam.JUESE = 1;
    }

    @Override
    public void initView() {

        btn_next = this.findViewById(R.id.btn_next);
        iv_back = this.findViewById(R.id.iv_back);
        tv_xueli = this.findViewById(R.id.tv_xueli);
        tv_juese = this.findViewById(R.id.tv_juese);
        ll_layout = this.findViewById(R.id.ll_layout);
        et_bumen = this.findViewById(R.id.et_bumen);
        et_company = this.findViewById(R.id.et_company);
        layout_company = this.findViewById(R.id.layout_company);

        btn_next.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_xueli.setOnClickListener(this);
        tv_juese.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_next:
                //判断当选择企业工程师的时候必须要填写公司

                if (RegisterParam.JUESE==2){
                    if(ValidateParam.validateParamIsNull(et_company.getText().toString())){
                        Toast.makeText(RegiesterActivity2.this, "公司必填参数", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                RegisterParam.BUMEN = et_bumen.getText().toString();
                RegisterParam.COMPANY = et_company.getText().toString();
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
                        layout_company.setVisibility(position ==1 ? View.VISIBLE : View.GONE);
                    }
                });
                popup.showAtLocation(ll_layout, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
        }
    }
}
