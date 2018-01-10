package com.example.wsq.android.activity;

import android.graphics.Canvas;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wsq.android.R;
import com.example.wsq.android.base.BaseActivity;
import com.example.wsq.android.view.LoddingDialog;
import com.lidong.pdf.PDFView;
import com.lidong.pdf.listener.OnDrawListener;
import com.lidong.pdf.listener.OnLoadCompleteListener;
import com.lidong.pdf.listener.OnPageChangeListener;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wsq on 2018/1/4.
 */

public class PdfReadActivity extends BaseActivity implements OnDrawListener, OnLoadCompleteListener, OnPageChangeListener {

    @BindView(R.id.pdvView) PDFView pdvView;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_page) TextView tv_page;

    private LoddingDialog dialog;

    @Override
    public int getByLayoutId() {
        return R.layout.layout_pdf_reader;
    }

    @Override
    public void init() {


        tv_title.setText(getIntent().getStringExtra("name")+"");

        dialog = new LoddingDialog(this);

        openPdf(getIntent().getStringExtra("url"), getIntent().getStringExtra("name"));
    }

    public void openPdf(String url, String fileName){

        if (TextUtils.isEmpty(url)){
            Toast.makeText(this, "pdf地址为空",     Toast.LENGTH_SHORT).show();
            finish();
            return ;
        }

        dialog.show();

        pdvView.fileFromLocalStorage(this, this,this, url, fileName);
    }


    @OnClick({R.id.iv_back})
    public void onClick(View view){
        switch (view.getId()){
            case  R.id.iv_back:
                finish();
                break;
        }
    }

    //页面绘制的回调
    @Override
    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

    }

    //加载完成的回调
    @Override
    public void loadComplete(int nbPages) {

        if (dialog.isShowing()){
            dialog.dismiss();
        }
    }

    //翻页回调
    @Override
    public void onPageChanged(int page, int pageCount) {

        tv_page.setText(page+" / "+pageCount);
//        Toast.makeText( PdfReadActivity.this , "page= " + page +
//                " pageCount= " + pageCount , Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        pdvView.destroyDrawingCache();
    }
}
