package com.example.wsq.android.activity.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wsq.android.R;
import com.example.wsq.android.base.BaseActivity;
import com.example.wsq.android.utils.FileUtil;
import com.example.wsq.android.view.signature.SignaturePad;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 个人签名
 * Created by wsq on 2017/12/30.
 */

public class SignaturePadActivity extends BaseActivity implements SignaturePad.OnSignedListener {

    @BindView(R.id.signature_pad)  SignaturePad mSignaturePad;
    @BindView(R.id.save_button) Button save_button;
    @BindView(R.id.clear_button) Button clear_button;
    @BindView(R.id.signature_showText) TextView showText;

    private String signPath;

    @Override
    public int getByLayoutId() {
        return R.layout.layout_signature;
    }

    @Override
    public void init() {

        mSignaturePad.setOnSignedListener(this);

    }


    @OnClick({R.id.save_button, R.id.clear_button})
    public void onClick(View view ){
        switch (view.getId()){
            case R.id.save_button:
                Logger.d("点击保存按钮");
                onUpLoadSignPic();
                break;
            case R.id.clear_button:
                Logger.d("点击清除按钮");
                mSignaturePad.clear();


                break;
        }
    }

    public void onUpLoadSignPic(){

        Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();

        if (addSignatureToGallery(signatureBitmap)) {
//            upload();
        } else {
            Toast.makeText(SignaturePadActivity.this, "保存电子签名失败!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 绘制完成
     */
    @Override
    public void onSigned() {

        save_button.setEnabled(true);
        save_button.setEnabled(true);
        save_button.setBackgroundResource(R.drawable.shape_button);
        save_button.setBackgroundResource(R.drawable.shape_button);
        showText.setVisibility(View.GONE);
    }

    /**
     *
     */
    @Override
    public void onClear() {
        save_button.setEnabled(false);
        clear_button.setEnabled(false);
        save_button.setBackgroundResource(R.drawable.shape_button);
        clear_button.setBackgroundResource(R.drawable.shape_button);
        showText.setVisibility(View.VISIBLE);
    }



    public boolean addSignatureToGallery(Bitmap signature) {
        boolean result = false;
        try {
            File photo = new File(FileUtil.getTdPath(this) + String.format("Signature_%d.jpg", System.currentTimeMillis()));
            File temp = new File(FileUtil.getTdPath(this));
            if (!temp.exists()) {
                temp.mkdir();
            }
            saveBitmapToJPG(signature, photo);

            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(photo);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);
            signPath = photo.getAbsolutePath();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }
}
