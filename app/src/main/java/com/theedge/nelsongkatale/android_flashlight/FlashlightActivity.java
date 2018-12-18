package com.theedge.nelsongkatale.android_flashlight;
/**
 * Author :Nelson G katale
 * Description:A simple android flashlight utility.
 */

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


public class FlashlightActivity extends AppCompatActivity {

    private Camera camera;
    private boolean buttontoggle=false;
    private ImageButton flashLightButton;
    boolean isflash=false;

    final int CAMERA_PERMISSION_CODE=1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        flashLightButton = (ImageButton) findViewById(R.id.button);
            flashLightButton.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    int permission=PermissionChecker.checkSelfPermission(FlashlightActivity.this, Manifest.permission.CAMERA);
                    if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= 23 && permission!= PermissionChecker.PERMISSION_GRANTED){
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, 0);

                    }else{
                        int buildSdkVersion=Build.VERSION.SDK_INT;
                        if (!isflash) {
                            if (!buttontoggle) {
                                if (buildSdkVersion >= Build.VERSION_CODES.LOLLIPOP) {
                                    toggleOn();
                                }
                            } else {
                                toggleOff();
                            }
                        } else {
                            flashHardware();
                        }
                    }
                }
            });

    //settings toolbar
        Toolbar settings_toolbar=(Toolbar)findViewById(R.id.settings_toolbar);
        setSupportActionBar(settings_toolbar);
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
            case R.id.about_app:
                Intent intent=new Intent(this,AboutActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**Toggles flashlight on*/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void toggleOn(){
        CameraManager cameraManager=(CameraManager)getSystemService(Context.CAMERA_SERVICE);

        try {
            String flashID=cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(flashID,true);
            buttontoggle=true;
            flashLightButton.setImageResource(R.drawable.lighton);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /* Toggles flashlight off*/
    private void toggleOff(){
        CameraManager cameraManager=(CameraManager)getSystemService(Context.CAMERA_SERVICE);

        try {
            String flashID=cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(flashID,false);
            buttontoggle=false;
            flashLightButton.setImageResource(R.drawable.lightoff);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**release camera resources on application exit*/
    protected  void onStop(){
        super.onStop();
        if(camera!=null){
            camera.release();
            camera=null;
        }
    }

    /**
     * display's dialog box when device doesn't support flash hardware
     * @return true when device doesn't have flash
     */
    private boolean flashHardware(){
        AlertDialog.Builder builder=new AlertDialog.Builder(FlashlightActivity.this);
        builder.setTitle("Error!");
        builder.setMessage("Device doesn't support flash");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.show();

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==CAMERA_PERMISSION_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                Toast.makeText(FlashlightActivity.this,"Permission Granted",Toast.LENGTH_SHORT).show();

            }else{

                Toast.makeText(FlashlightActivity.this,"Permission Denied ! ",Toast.LENGTH_SHORT).show();
            }

        }
    }
}