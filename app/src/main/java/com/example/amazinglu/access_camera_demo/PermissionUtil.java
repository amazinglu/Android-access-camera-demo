package com.example.amazinglu.access_camera_demo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionUtil {
    public static final int REQ_CODE_READ_EXTERNAL_STORAGE = 200;
    public static final int REQ_CODE_WRITE_EXTERNAL_STORAGE = 202;
    public static final int REQ_CODE_CAMERA = 199;

    public static boolean checkPermission(Context context, String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermission(Activity activity, String[] permissions, int reqCode) {
        ActivityCompat.requestPermissions(activity, permissions, reqCode);
    }

    public static void requestReadExternalStoragePermission(Activity activity) {
        requestPermission(activity, new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE},
                REQ_CODE_READ_EXTERNAL_STORAGE);
    }

    public static void requestWriteExternalStoragePermission(Activity activity) {
        requestPermission(activity, new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQ_CODE_WRITE_EXTERNAL_STORAGE);
    }

    public static void requestCameraPermission(Activity activity) {
        requestPermission(activity, new String[] {Manifest.permission.CAMERA},
                REQ_CODE_CAMERA);
    }
}
