package com.swust.stylezz.studyinteret;

import android.app.Application;

import com.umeng.commonsdk.UMConfigure;

public class UMApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate ();
        UMConfigure.init ( this,UMConfigure.DEVICE_TYPE_PHONE,"1fe6a20054bcef865eeb0991ee84525b" );
    }
}
