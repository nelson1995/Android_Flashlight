/*
 * Copyright (C) 2017 Nishant Srivastava
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.theedge.nelsongkatale.android_flashlight;

import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;

import com.theedge.nelsongkatale.android_flashlight.controller.FlashLightController;

/**
 * The type Pre marshmallow.
 */
@SuppressWarnings("deprecation")
class PreMarshmallow implements FlashLightController {

    private Camera camera;
    private Camera.Parameters parameters;

    public PreMarshmallow() {
        openCamera();
    }

    private void openCamera(){
        camera = Camera.open(getCameraId());
        parameters = camera.getParameters();
    }

    protected void onStop(){
        if(camera != null){
            camera.release();
            camera = null;
        }
    }

    public void on(){
        try{
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameters);
            camera.startPreview();

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void off(){
        try{
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);
            camera.stopPreview();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private int getCameraId() {
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
                return i;
            }
        }
        return 0;
    }
}
