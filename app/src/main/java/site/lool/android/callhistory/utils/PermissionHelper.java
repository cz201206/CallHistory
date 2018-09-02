package site.lool.android.callhistory.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

public class PermissionHelper {

    //提示用户授权
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
           // ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,1);
        }
    }
    public static void verifyPermissions(Activity activity,String[]  permissions) {
/*
        String[] p = new String[]{""};
        for (String permission: permissions  ) {
            int permissionResultCode = ActivityCompat.checkSelfPermission(activity, permission);
            if (permissionResultCode != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                p[0] = permission;
                ActivityCompat.requestPermissions(activity, p,1);
            }
        }*/
        if (ActivityCompat.checkSelfPermission(activity, permissions[0])
                != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(activity, permissions[1])
                        != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(activity, permissions[2])
                        != PackageManager.PERMISSION_GRANTED
                ){
            ActivityCompat.requestPermissions(activity,
                    permissions, 1);
        }

    }
}
