package cn.gdmcmc.www.demo.application;

import android.app.Application;
import android.content.Context;

import org.greenrobot.greendao.database.Database;

import java.util.concurrent.TimeUnit;

import cn.gdmcmc.www.demo.application.exception.LocalFileHandler;
import cn.gdmcmc.www.demo.dao.DaoMaster;
import cn.gdmcmc.www.demo.dao.DaoSession;
import cn.gdmcmc.www.demo.util.LogUtil;
import cn.gdmcmc.www.demo.util.SharedPreferencesUtil;
import cn.gdmcmc.www.demo.util.ToastUtil;
import okhttp3.OkHttpClient;

/**
 * Created by ASIMO on 2017/11/28.
 */

public class MyApplication extends Application {
    private static DaoSession mDaoSession;
    private static MyApplication mApplication;
    private static final String DATA_BASE_NAME="record";
    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.isDebug=true;

        //配置是否显示log
        LogUtil.isDebug = true;

        //配置时候显示toast
        ToastUtil.isShow = true;

        setupDatabase(this);

        SharedPreferencesUtil.init(this);

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

    private void setupDatabase(Context context)
    {
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(context,DATA_BASE_NAME);
        Database db = openHelper.getWritableDb();
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();
    }

    public static DaoSession getmDaoSession(){
        return mDaoSession;
    }


}
