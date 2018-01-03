package com.example.blackpearl.foldscope.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.example.blackpearl.foldscope.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by BlackPearl on 18-Nov-17.
 */
public class Utility {

    public static boolean isOnline(Context context) {

        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                return cm.getActiveNetworkInfo().isConnected();
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean ValidateEmail(String email) {

        if (email
                .matches("\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")
                && email.length() > 0) {
            return true;
        }
        return false;
    }

    public static boolean IsAlphanumeric(String email) {

        if (email
                .matches("^[a-zA-Z0-9]*$")
                && email.length() > 0) {
            return true;
        }
        return false;
    }

    public static boolean IsNumericValid(String numeric) {
        // TODO Auto-generated method stub
        boolean isContactNo = false;
        String strExpression = "^(0|[1-9][0-9]*)$";
        CharSequence inputStr = numeric;
        Pattern objPattern = Pattern.compile(strExpression,
                Pattern.CASE_INSENSITIVE);
        Matcher objMatcher = objPattern.matcher(inputStr);
        if (objMatcher.matches()) {
            isContactNo = true;
        }
        return isContactNo;
    }

    public static ProgressDialog ShowProgressDialog(Context context, String message) {

        ProgressDialog m_Dialog = new ProgressDialog(context, R.style.CustomDialog);
        m_Dialog.setMessage(message);
        m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_Dialog.setCancelable(false);
        m_Dialog.show();
        return m_Dialog;
    }

    public static void LogD(String Tag, String Message) {

        Log.d(Tag, Message);
    }

    public static void LogE(String Tag, String Message) {

        Log.e(Tag, Message);
    }

    public static void LogW(String Tag, String Message) {

        Log.d(Tag, Message);
    }

    public static void LogI(String Tag, String Message) {

        Log.i(Tag, Message);
    }

    // Copy to sdcard for debug use
    public static void CopyDatabase(Context c, String DATABASE_NAME) {

        String databasePath = c.getDatabasePath(DATABASE_NAME).getPath();
        File f = new File(databasePath);
        OutputStream myOutput = null;
        InputStream myInput = null;
        if (f.exists()) {
            try {
                File directory = new File("/mnt/sdcard/DB_DEBUG");
                if (!directory.exists())
                    directory.mkdir();
                myOutput = new FileOutputStream(directory.getAbsolutePath()
                        + "/" + DATABASE_NAME);
                myInput = new FileInputStream(databasePath);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }
                myOutput.flush();
            } catch (Exception e) {
            } finally {
                try {
                    if (myOutput != null) {
                        myOutput.close();
                        myOutput = null;
                    }
                    if (myInput != null) {
                        myInput.close();
                        myInput = null;
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    public static String DownloadImage(String url1, String path) {
        // initilize the default HTTP client object
        String TAG = "Download Image";
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String fileUrl = url1;
            URL url = new URL(fileUrl.replaceAll(" ", "%20"));
            URLConnection ucon = url.openConnection();
            LogI(TAG, "Download File----->" + fileUrl);
            ucon.setReadTimeout(5000);
            ucon.setConnectTimeout(30000);
            ucon.connect();
            InputStream is = ucon.getInputStream();
            BufferedInputStream inStream = new BufferedInputStream(is, 1024 * 5);
            FileOutputStream outStream = new FileOutputStream(path);
            byte[] buff = new byte[5 * 1024];
            int len;
            while ((len = inStream.read(buff)) != -1) {
                outStream.write(buff, 0, len);
            }
            outStream.flush();
            outStream.close();
            inStream.close();
        } catch (Exception e) {
            LogE(TAG, "Download Error----->" + e.toString());
        }
        return path;
    }

    public static String Filename(File file, String Title) {

        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;
    }

    public static void DeleteFile(String path) {

        File file = new File(path);
        if (file.exists()) {
            String deleteCmd = "rm -r " + path;
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
            } catch (IOException e) {
            }
        }
    }

    public static void HideSoftKeyboard(Activity activity) {

        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static boolean CheckAndRequestPermissions(Context context,android.app.Activity activity ) {

        final int REQUEST_ID_MULTIPLE_PERMISSIONS = 110;
        int permissionREAD_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionWRITE_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCamera = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionWRITE_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionREAD_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

}
