package com.theedge.nelsongkatale.android_flashlight;
/**
 * Author :Nelson G katale
 * Description:A simple android flashlight utility.
 */

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.theedge.nelsongkatale.android_flashlight.controller.FlashLightController;

import java.util.concurrent.TimeUnit;

public class FlashlightActivity extends AppCompatActivity {

    private Camera camera;

    private Camera.Parameters parameters;

    private ImageButton flashLightButton;

    boolean isFlashLightOn = false;

    private FlashLight flashLight=null;

    int REQUEST_CODE=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        flashLightButton = findViewById(R.id.button);

        flashLightButton.setImageResource(R.drawable.lightoff);

        flashLight = new FlashLight(FlashlightActivity.this).observeLifecycle(this);

        if(!flashLight.initTorchMode()){
            ActivityCompat.requestPermissions(FlashlightActivity.this,new String[]{Manifest.permission.CAMERA},REQUEST_CODE);
        }

        flashLightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { isFlashLightOn = !isFlashLightOn;
                    if (isFlashLightOn) {
                        flashLight.enableTorch(true);
                        flashLight.pulse(true);
                        flashLight.withDelay(1, TimeUnit.SECONDS);
                        flashLightButton.setImageResource(R.drawable.lighton);

                    } else {
                        flashLight.enableTorch(false);
                        flashLight.pulse(false);
                        flashLightButton.setImageResource(R.drawable.lightoff);
                    }
                }
            });
    }

    //toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater=getMenuInflater();

        inflater.inflate(R.menu.about,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.about_app :
                Intent about_intent=new Intent(this,AboutActivity.class);
                startActivity(about_intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(camera != null){
            camera.release();
            camera = null;
        }
    }

//    @Override
//    protected void onDestroy() {
//        if (camera != null) {
//            camera.stopPreview();
//            camera.release();
//            camera = null;
//        }
//        super.onDestroy();
//    }

}
