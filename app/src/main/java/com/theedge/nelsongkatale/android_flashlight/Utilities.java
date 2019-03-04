package com.theedge.nelsongkatale.android_flashlight;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

public class Utilities {

    /**
     *
     * @param context
     * @return boolean
     */
    boolean checkForCameraPermissions(Context context){
        return context.checkCallingOrSelfPermission(Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * check for device flash hardware
     * @param context
     * @return boolean
     */

    boolean checkForFlashHardware(Context context){
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }
    /**
     *  check if its android marshmallow and above
     * @return boolean
     */
    static boolean isMarshMallowAndUp(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
