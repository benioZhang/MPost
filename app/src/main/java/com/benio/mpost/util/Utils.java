package com.benio.mpost.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.benio.mpost.app.AppConfig;
import com.benio.mpost.bean.ForbiddenUser;
import com.benio.mpost.bean.MPost;
import com.benio.mpost.bean.MUser;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by shau-lok on 11/4/15.
 */
public class Utils {

    public static int getScreenWidth(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static boolean checkListEmpty(List<?> list) {
        return list == null || list.size() < 1;
    }

    /**
     * 检查post是否存在list中
     *
     * @param post
     * @param list
     * @return
     */
    public static boolean isPostInList(MPost post, List<MPost> list) {
        if (list == null || list.isEmpty()) return false;
        for (MPost mPost : list) {
            if (TextUtils.equals(mPost.getObjectId(), post.getObjectId())) return true;
        }
        return false;
    }


    /**
     * 检查user是否存在list中
     *
     * @param user
     * @param list
     * @return
     */
    public static boolean isUserInList(MUser user, List<MUser> list) {
        if (list != null) {
            for (MUser u : list) {
                if (u.equals(user)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isForbiddenUser(MUser user, List<ForbiddenUser> list) {
        if (list != null) {
            for (ForbiddenUser u : list) {
                MUser tmp = u.getUser();
                if (tmp != null && tmp.equals(user)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 压缩图片
     *
     * @param ImgPath
     * @return
     */
    public static String getCompressImage(String ImgPath) {
        Bitmap bitmap;
        File outputFile = new File(ImgPath);
        long fileSize = outputFile.length();
        final long fileMaxSize = 30 * 1024;
        if (fileSize >= fileMaxSize) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(ImgPath, options);
            int height = options.outHeight;
            int width = options.outWidth;

            double scale = Math.sqrt((float) fileSize / fileMaxSize);
            options.outHeight = (int) (height / scale);
            options.outWidth = (int) (width / scale);
            options.inSampleSize = (int) (scale + 0.5);
            options.inJustDecodeBounds = false;

            bitmap = BitmapFactory.decodeFile(ImgPath);
        } else {
            bitmap = BitmapFactory.decodeFile(ImgPath);
        }
        try {
            outputFile = new File(getOutputMediaFileUri().getPath());
            FileOutputStream fos = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }

        return outputFile.getAbsolutePath();

    }

    public static Uri getOutputMediaFileUri() {
        String mediaStorageDir = AppConfig.DEFAULT_SAVE_IMAGE_PATH + File.separator + "camera";
        File mediaStorage = new File(mediaStorageDir);
        if (!mediaStorage.exists()) {
            if (!mediaStorage.mkdirs()) {
                AKLog.e("mkdirs error");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir + File.separator + "IMG_" + timeStamp
                + ".jpg");

        return Uri.fromFile(mediaFile);
    }

    /**
     * 获取图片的绝对地址
     *
     * @param contentURI
     * @return
     */
    public static String getRealPathFromURI(Context context, Uri contentURI) {
        String result;
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(contentURI, projection, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

}
