package com.theedge.nelsongkatale.flashlight;

import com.theedge.nelsongkatale.android_flashlight.FlashLight;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class FlashLightUnitTest {

    @Test public void initTorchTest(){

        FlashLight flashLight = new FlashLight();

        boolean state = flashLight.initTorchMode();
        assertNotEquals(state,true);

    }

}