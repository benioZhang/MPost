package com.benio.mpost.interf.impl;

import com.benio.mpost.util.AKLog;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UploadBatchListener;

/**
 * Created by benio on 2015/10/22.
 */
public abstract class UploadFilesListener implements UploadBatchListener {

    private boolean isDone;

    @Override
    public final void onSuccess(List<BmobFile> files, List<String> urls) {
        //这样做的原因是因为，该接口每成功上传一个文件，就会调用一次onSuccess()方法
        //而我需要的是所有都上传完后才调用
        //所以可以用isDone来控制了
        onSuccess(files, urls, isDone);
    }

    public abstract void onSuccess(List<BmobFile> files, List<String> urls, boolean isDone);

    @Override
    public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
        if (curIndex >= total) {
            isDone = true;
        }
        AKLog.d(UploadFilesListener.class.getSimpleName(), "curIndex：" + curIndex + ",curPercent：" + curPercent + ",total: " + total + ",totalPercent: " + totalPercent);
    }
}
