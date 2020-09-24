package com.darma.wallet.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import com.darma.wallet.BuildConfig;
import com.darma.wallet.R;
import com.darma.wallet.db.WalletDB;
import com.darma.wallet.db.WalletDataBase;
import com.wallet.WalletManager;
import com.wallet.bean.WalletError;
import com.wallet.model.Wallet;
import com.wallet.model.WalletErrorException;

import java.io.*;

/**
 * Created by Darma Project on 2019/10/8.
 */
public class FileUtils {

    public static String SD_PATH = null;


    public static void init(Context context) {
        SD_PATH = context.getExternalCacheDir().getAbsolutePath() + File.separator + "DARMA";
        File file = new File(SD_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        File fileBackUp = new File(getWalletBackUpFilePath(context));
        if (!fileBackUp.exists()) {
            fileBackUp.mkdirs();
        }

        File fileTemp = new File(getTempFilePath(context));
        if (!fileTemp.exists()) {
            fileTemp.mkdirs();
        }


    }

    public static String newScreenFilePath() {
        return getScreenShotFilePath() + File.separator + System.currentTimeMillis() + ".png";
    }

    public static String getCacheFilePath(Context context) {

        return context.getExternalCacheDir().getAbsolutePath();
    }

    public static String getWalletBackUpFilePath(Context context) {

        return SD_PATH + File.separator + "walletbackup";
    }
    public static String getTempFilePath(Context context) {

        return SD_PATH + File.separator + "temp";
    }
    public static Uri getUriFromAppFile(Context context, File file) {

        Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", file);
        Log.v("luo", "uri.toString() = " + uri.toString());


        return uri;
    }


    /**
     * Copying a single file
     *
     * @param oldfile String
     * @param newfile String
     * @return boolean
     */
    public static void copyFile(File oldfile, File newfile) {
        try {
            int bytesum = 0;
            int byteread = 0;
            newfile.delete();
            if (oldfile.exists()) { //
                InputStream inStream = new FileInputStream(oldfile); //
                FileOutputStream fs = new FileOutputStream(newfile);

                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }
    public static void copyFile(Context context, Uri uri, File newfile) {
        try {
            int bytesum = 0;
            int byteread = 0;
            newfile.delete();
            InputStream inStream = context.getContentResolver().openInputStream(uri); //
            FileOutputStream fs = new FileOutputStream(newfile);

            byte[] buffer = new byte[1444];
            int length;
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread; //
                fs.write(buffer, 0, byteread);
            }
            inStream.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    public static String getScreenShotFilePath() {

        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
    }

    public static String getDCIMFilePath() {

        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
    }

    public static String getUrlFileName(String url) {

        return url.substring(url.lastIndexOf("/"));
    }

    /**
     * @param bmp
     * @param picName
     */
    public static String saveBmp2Gallery(Context context, Bitmap bmp, String picName) {

        String fileName = null;
        //System album catalog
        String galleryPath = Environment.getExternalStorageDirectory()
                + File.separator + Environment.DIRECTORY_DCIM
                + File.separator + "Camera" + File.separator;


        File file = null;
        FileOutputStream outStream = null;

        try {
            file = new File(galleryPath, picName + ".jpg");

            fileName = file.toString();
            outStream = new FileOutputStream(fileName);
            if (null != outStream) {
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            }

        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        MediaStore.Images.Media.insertImage(context.getContentResolver(),
                bmp, fileName, null);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);

        return file.getAbsolutePath();

    }


    public static boolean editWalletName(final Context context, final Wallet wallet, final String name) throws WalletErrorException {

        if (wallet == null) {
            throw new WalletErrorException(WalletError.create(0, context.getString(R.string.error_wallet_not_exist)));
        }

        for (Wallet w : WalletManager.getInstance().getWallets()) {
            if (name.equals(w.getName())) {
                throw new WalletErrorException(WalletError.create(0, context.getString(R.string.error_wallet_name_already_exist)));
            }
        }
        final String lastName = wallet.getName();
        if (wallet.getWalletFile().renameTo(WalletManager.getInstance().newWalletFile(name))) {

            ThreadPoolUtils.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    WalletDB walletDB =
                            WalletDataBase.getInstance(context).getWalletDao().getWallet(lastName);
                    if (walletDB != null) {
                        walletDB.setName(name);
                        WalletDataBase.getInstance(context).getWalletDao().update(walletDB);
                    }
                }
            });

        }

        return true;


    }

    public static void deleteWallet(final Context context, Wallet wallet) {
        if (wallet != null && wallet.getWalletFile() != null) {
            final String name = wallet.getName();

            if (wallet.getWalletFile().delete()) {
                ThreadPoolUtils.getInstance().execute(new Runnable() {
                    @Override
                    public void run() {
                        WalletDB walletDB =
                                WalletDataBase.getInstance(context).getWalletDao().getWallet(name);
                        if (walletDB != null) {
                            WalletDataBase.getInstance(context).getWalletDao().delete(walletDB);
                        }
                    }
                });
            }

        }
    }
}
