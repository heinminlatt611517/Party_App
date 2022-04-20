/*
 * Developed by Md Rayhan Uddin (rayhanuddin55@gmail.com)
 */

package com.parallax.partyapp.views;

import android.content.pm.ActivityInfo;
import android.os.Build;

import com.akexorcist.localizationactivity.ui.LocalizationActivity;

public class BaseActivity extends LocalizationActivity {

    @Override
    protected void onStart() {
        super.onStart();

        if (Build.VERSION.SDK_INT == 26) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
}
