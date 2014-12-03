package hu.uniobuda.nik.dorothy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by asilaydying on 11/12/2014.
 */
public class GlobalHelper {
    public static String KategoriaKepLink = "http://dorothy.hu/img/Kategoriak/";
    public static String Website = "http://dorothy.hu/";
    public static String BaseAndroidURL = "http://dorothy.hu/Android/";
    public static String PrefFileUserData = "username";

    public static Bitmap CheckFile(String id, String size, String path, Context context) {
        try {
            File file = context.getFileStreamPath(id + ".jpg");
            //File file = new File(id + ".jpg");
            FileInputStream fis = null;
            if (file.exists())//létezik a fájl
            {
                if (Long.parseLong(size) == file.length())//ugyanaz a fájl
                {
                    fis = new FileInputStream(file);

                    byte[] buffer = new byte[(int) file.length()];
                    int len = 0;
                    while ((len = fis.read(buffer)) != -1) {
                    }

                    Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
                    fis.close();
                    return bitmap;
                } else {
                    DownloadImage(id, path, context);
                    return CheckFile(id, size, path, context);
                }
            } else//nem létezik a fájl
            {
                DownloadImage(id, path, context);
                return CheckFile(id, size, path, context);
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
            BitmapFactory.Options option = new BitmapFactory.Options();
            option.inJustDecodeBounds = false;
            bmp = BitmapFactory.decodeByteArray(buffer, 0, buffer.length,option);
            return bmp;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bmp;
    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getCurrentUser() {
        return CurrentUser;
    }

    private static String CurrentUser;

    public static void CheckLogin(Context ctx) {
        SharedPreferences settings = ctx.getSharedPreferences(GlobalHelper.PrefFileUserData, 0);
        CurrentUser = settings.getString("username", null);
    }

    public static void LogOut(Context ctx) {
        SharedPreferences settings = ctx.getSharedPreferences(GlobalHelper.PrefFileUserData, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove("username");
        editor.commit();

        Intent intent = new Intent(ctx, MyActivity.class);
        ctx.startActivity(intent);
        ((Activity) ctx).finish();
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        if (email.matches(emailRegex)) {
            return true;
        }
        return false;
    }

    public static void DisableButton(View v) {
        v.setBackgroundColor(Color.parseColor("#808080"));
        v.setEnabled(false);
    }
    public static void EnableButton(View v) {
        v.setBackgroundColor(Color.parseColor("#fff4bf3b"));
        v.setEnabled(true);
    }

}
