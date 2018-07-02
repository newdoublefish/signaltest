package cn.gdmcmc.www.demo.application;

import android.app.Application;

import java.util.concurrent.TimeUnit;

import cn.gdmcmc.www.demo.application.exception.LocalFileHandler;
import cn.gdmcmc.www.demo.util.LogUtil;
import cn.gdmcmc.www.demo.util.ToastUtil;
import okhttp3.OkHttpClient;

/**
 * Created by ASIMO on 2017/11/28.
 */

public class MyApplication extends Application {
    private static MyApplication mApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.isDebug=true;

        //配置是否显示log
        LogUtil.isDebug = true;

        //配置时候显示toast
        ToastUtil.isShow = true;

        //配置程序异常退出处理
        //Thread.setDefaultUncaughtExceptionHandler(new LocalFileHandler(this));
    }

    public static MyApplication getIntstance() {
        return mApplication;
    }

    public static OkHttpClient defaultOkHttpClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .build();
        return client;
    }
}
