package com.theedge.nelsongkatale.android_flashlight;

import android.app.Activity;
import android.app.AlertDialog;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.content.DialogInterface;
import android.os.Handler;

import com.theedge.nelsongkatale.android_flashlight.controller.FlashLightController;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

public class FlashLight implements LifecycleObserver {

    private FlashLightController flashLightController = null;
    private Utilities utilities = null;
    private boolean isFlashOn=false;
    private Handler handler;
    long pulseTime = 1000;
    private static WeakReference<Activity> activityWeakRef = null;

    private final Runnable pulseRunnable = new Runnable() {
        @Override
        public void run() {
            enableTorch(!isFlashOn);
            handler.postDelayed(pulseRunnable, pulseTime);
        }
    };

    public FlashLight(){}

    public FlashLight(Activity activity){
        this.utilities = new Utilities();
        this.activityWeakRef = new WeakReference<>(activity);
        handler = new Handler();
    }

    public void enableTorch(boolean enabled){
        if (activityWeakRef != null) {
            if (enabled) {
                if (isFlashOn && utilities.checkForCameraPermissions(activityWeakRef.get().getApplicationContext())) {
                    flashLightController.off();
                    isFlashOn = false;
                }
            } else {
                if (!isFlashOn && utilities.checkForCameraPermissions(activityWeakRef.get().getApplicationContext())) {
                    flashLightController.on();
                    isFlashOn = true;
                }
            }
        } else {
            flashLightController.off();
            isFlashOn = false;
        }
    }

    public boolean initTorchMode(){
        if(activityWeakRef != null){
            if(!utilities.checkForFlashHardware(activityWeakRef.get().getApplicationContext())){

                showNoFlashAlert();

            }else if(utilities.checkForFlashHardware(activityWeakRef.get().getApplicationContext()) &&
                    utilities.checkForCameraPermissions(activityWeakRef.get().getApplicationContext())){

                if(utilities.isMarshMallowAndUp()){

                    flashLightController = new PostMarshmallow(activityWeakRef.get().getApplicationContext());

                }else{

                    flashLightController = new PreMarshmallow();
                }

                return true;
            }
        }
        return false;
    }

    private void showNoFlashAlert() {
        new AlertDialog.Builder(activityWeakRef.get().getApplicationContext())
                .setMessage(R.string.ErrorMessage)
                .setIcon(android.R.drawable.ic_dialog_alert).setTitle("Info")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    public FlashLight observeLifecycle(LifecycleOwner lifecycleOwner) {
        // Subscribe to listening lifecycle
        lifecycleOwner.getLifecycle().addObserver(this);

        return this;
    }

    public FlashLight pulse(boolean enabled) {
        if (enabled) {
            handler.postDelayed(pulseRunnable, pulseTime);
        } else {
            handler.removeCallbacks(pulseRunnable);
        }
        return this;
    }

    public FlashLight withDelay(long time, final TimeUnit timeUnit) {
        this.pulseTime = TimeUnit.MILLISECONDS.convert(time, timeUnit);
        return this;
    }

}
