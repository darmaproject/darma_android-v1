package com.darma.wallet.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import com.darma.wallet.MyApplication;
import com.darma.wallet.R;
import com.wallet.utils.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Darma Project on 2019/10/8.
 */
public class ShareUtils {


    public static void share(String imagePath, Context context) {
        if (imagePath != null) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            File file = new File(imagePath);
            intent.putExtra(Intent.EXTRA_STREAM,FileUtils.getUriFromAppFile(context,file));
            intent.setType("image/*");
            Intent chooser = Intent.createChooser(intent, context.getResources().getString(R.string.str_share));
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context. startActivity(chooser);
            }

            insertSystemAlbumAndRefresh(file,context);
        }
    }

    public static void shareFile(String filePath, Context context) {
        if (filePath != null) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            File file = new File(filePath);
            intent.putExtra(Intent.EXTRA_STREAM,FileUtils.getUriFromAppFile(context,file));
            intent.setType("*/*");
            Intent chooser = Intent.createChooser(intent, context.getResources().getString(R.string.str_share));
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context. startActivity(chooser);
            }

        }
    }

    public static void shareUrl(String url,Context context) {
        if (url != null) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, url);//
            intent.setType("text/plain");
            Intent chooser = Intent.createChooser(intent, context.getResources().getString(R.string.str_share));
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context. startActivity(chooser);
            }

        }
    }


    public static void share(Bitmap bitmap, Context context) throws IOException {

//        String imagePath = Environment.getExternalStorageDirectory().getPath() + File.separator+"Share"+ File.separator +System.currentTimeMillis()+ ".png";
        String imagePath = FileUtils.getDCIMFilePath();
            imagePath =imagePath+File.separator + System.currentTimeMillis()+ ".png";
        if (bitmap != null) {
                File file = new File(imagePath);
                FileOutputStream os = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.flush();
                os.close();
                share(imagePath,context);
        }
    }



    /**
     * Insert into the system album and refresh the system album
     */
    private static void insertSystemAlbumAndRefresh(File imgFile,Context context) {
        String imageUri = null;
        try {
            imageUri = MediaStore.Images.Media.insertImage(context.getContentResolver(),imgFile.getAbsolutePath(), imgFile.getName(), "pic: " + imgFile.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("_stone_", "insertSystemAlbumAndRefresh-subscribe: imageUri=" + imageUri);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(imgFile)));

    }

}
