package com.java.liuyun;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;

public class ShareMethod {
    static ShareFrame shareFrame = null;
    static void initShareImage(Context context, String title, String summary) {
        shareFrame = new ShareFrame(context);
        shareFrame.setTitle(title);
        shareFrame.setSummary(summary);
    }
    static Uri createShareImage(Context context){
        Bitmap bitmap =shareFrame.createImage();

        System.out.println("step1");

        Uri path = saveImage(context,bitmap);

        System.out.println("step2");

        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }

        System.out.println("step3");

        return path;
    }
    static private Uri saveImage(Context context, Bitmap bitmap) {

        File path = context.getCacheDir();

        String fileName = "shareImage.jpeg";

        File file = new File(path, fileName);

        if (file.exists()) {
            file.delete();
        }

        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(file.toString());

        Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
        return uri;
    }
}
