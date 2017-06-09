package com.wl.technology.util;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by wanglin  on 2017/6/7 13:57.
 */

public class FileUtil {

    public static void saveFile(File file) {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(file);
            os = new FileOutputStream(file);
            byte[] bytes = new byte[1024];
            int length = 0;
            while ((length = is.read(bytes)) != -1) {
                os.write(bytes, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) os.close();

                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private static String getFileName(String name) {
        return MD5.Companion.md5(name);
    }

    ///< 写入图片到sdcard
    public static void writeImageInSDCard(Bitmap bitmap, String dir, String name) {
        String tmpName = getFileName(name);
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdir();
        }
        File logoFile = new File(dir, tmpName);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(logoFile);
            fos.write(bitmapdata);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.d(tmpName + "->" + e.getMessage());
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        LogUtil.d("w icon->" + logoFile.getAbsolutePath());
    }
}
