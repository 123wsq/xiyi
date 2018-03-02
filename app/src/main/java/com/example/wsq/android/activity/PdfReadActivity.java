package com.example.wsq.android.activity;

import android.graphics.Canvas;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wsq.android.R;
import com.example.wsq.android.base.BaseActivity;
import com.example.wsq.android.inter.OnDownloadListener;
import com.example.wsq.android.service.UserService;
import com.example.wsq.android.service.impl.UserServiceImpl;
import com.example.wsq.android.utils.ToastUtis;
import com.example.wsq.android.view.LoddingDialog;
import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnLoadCompleteListener;
import com.joanzapata.pdfview.listener.OnPageChangeListener;
import com.orhanobut.logger.Logger;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wsq on 2018/1/4.
 */
public class PdfReadActivity extends BaseActivity {
//public class PdfReadActivity extends BaseActivity implements OnDrawListener, OnLoadCompleteListener, OnPageChangeListener {

//    @BindView(R.id.pdvView) PDFView pdvView;
    @BindView(R.id.pdfview) PDFView pdfview;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_page) TextView tv_page;
    @BindView(R.id.rl_layout) RelativeLayout rl_layout;

    private LoddingDialog dialog;
    private UserService userService;

    @Override
    public int getByLayoutId() {
        return R.layout.layout_pdf_reader;
    }

    @Override
    public void init() {

        userService = new UserServiceImpl();
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

        onDownLoadPdf(url);



//        pdfview.fromAsset(fileName).
//                .pages(0, 2, 1, 3, 3, 3)
//                .defaultPage(1)
//                .showMinimap(false)
//                .enableSwipe(true)
//                .onDraw(onDrawListener)
//                .onLoad(onLoadCompleteListener)
//                .onPageChange(onPageChangeListener)
//                .load();

//        pdvView.fileFromLocalStorage(this, this,this, url, fileName);
    }


    @OnClick({R.id.iv_back})
    public void onClick(View view){
        switch (view.getId()){
            case  R.id.iv_back:
                finish();
                break;
        }
    }

//    //页面绘制的回调
//    @Override
//    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {
//
//    }
//
//    //加载完成的回调
//    @Override
//    public void loadComplete(int nbPages) {
//
//        if (dialog.isShowing()){
//            dialog.dismiss();
//        }
//    }
//
//    //翻页回调
//    @Override
//    public void onPageChanged(int page, int pageCount) {
//
//        tv_page.setText(page+" / "+pageCount);
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        pdvView.destroyDrawingCache();
//    }



    public void onDownLoadPdf(String url){
        dialog.show();
        Map<String, String> param = new HashMap<>();

        userService.onDownloadPDF(this, url, param, new OnDownloadListener() {
            @Override
            public void onSuccess(File file) {
                if (dialog.isShowing())dialog.dismiss();
//                ToastUtis.onToast("下载成功： "+ file.getAbsolutePathePath());

                pdfview.fromFile(file)
//                .pages(0, 2, 1, 3, 3, 3)
                .defaultPage(1)
                .showMinimap(false)
                .enableSwipe(true)
//                .onDraw(onDrawListener)
                .onLoad(onLoadCompleteListener)
                .onPageChange(onPageChangeListener)
                .load();

            }

            @Override
            public void onProgress(float progress, long total) {
                dialog.setUpdateProgress((int) progress);
            }

            @Override
            public void onFail() {

                if (dialog.isShowing())dialog.dismiss();
            }
        });
    }
    OnLoadCompleteListener onLoadCompleteListener = new OnLoadCompleteListener() {
        @Override
        public void loadComplete(int nbPages) {

        }
    };

    OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageChanged(int page, int pageCount) {
            tv_page.setText(page+" / "+pageCount);
        }
    };
}
