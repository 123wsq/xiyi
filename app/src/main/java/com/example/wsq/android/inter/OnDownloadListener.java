package com.example.wsq.android.inter;

import java.io.File;

/**
 * Created by Administrator on 2018/1/30 0030.
 */

public interface OnDownloadListener {


    /**
     * 下载成功
     */
    void onSuccess(File file);

    /**
     * 下载进度
     * @param progress
     * @param total
     */
    void onProgress(float progress, long total);

    /**
     * 下载失败
     */
    void onFail();
}
