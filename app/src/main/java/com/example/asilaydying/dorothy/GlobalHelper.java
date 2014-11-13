package com.example.asilaydying.dorothy;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by asilaydying on 11/12/2014.
 */
public class GlobalHelper {
    public static String KategoriaKepLink = "http://dorothy.hu/img/Kategoriak/";
    public static String Website = "http://dorothy.hu/";

    public static Bitmap CheckFile(String id, String size, String path, Context context) {
        try {
            File file = context.getFileStreamPath(id + ".jpg");
            //File file = new File(id + ".jpg");
            FileInputStream fis = null;
            if (file.exists())//létezik a fájl
            {
                if (Long.parseLong(size) == file.length())//ugyanaz a fájl
                {
                    fis= new FileInputStream(file);

                    byte[] buffer = new byte[(int) file.length()];
                    int len = 0;
                    while ((len = fis.read(buffer)) != -1)
                    {
                    }

                    Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
                    fis.close();
                    return bitmap;
                } else {
                    return DownloadImage(id, path, context);
                }
            } else//nem létezik a fájl
            {
                return DownloadImage(id, path, context);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap DownloadImage(String id, String path, Context context) {

        Bitmap bmp = null;
        try {
            //bmp = BitmapFactory.decodeStream(new URL(path).openConnection().getInputStream());
            File malkFile = context.getFileStreamPath(id + ".jpg");
            URLConnection ucon = new URL(path).openConnection();
            InputStream is = ucon.getInputStream();
            FileOutputStream fos = new FileOutputStream(malkFile);
            byte[] buffer = new byte[ucon.getContentLength()];
            int len = 0;
            while ((len = is.read(buffer)) != -1)
                fos.write(buffer, 0, len);
            fos.close();
            is.close();

            bmp = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
            return bmp;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bmp;
    }

}
