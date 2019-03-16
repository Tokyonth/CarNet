package com.tokyonth.carnet.base;

import android.app.Application;

public class IWalkApplication extends Application {

    private static IWalkApplication mInstance;

    public static IWalkApplication getInstance(){
        if(mInstance == null){
            mInstance = new IWalkApplication();
        }
        return mInstance;
    }
}
