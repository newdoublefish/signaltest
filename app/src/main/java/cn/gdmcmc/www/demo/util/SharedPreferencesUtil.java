package cn.gdmcmc.www.demo.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {
    public static final String PREFERENCE_NAME = "saveInfo";
    private static SharedPreferences mSharedPreferences;
    private static SharedPreferencesUtil mSharedPrefencesUtil;
    private static SharedPreferences.Editor editor;

    private SharedPreferencesUtil(Context cxt) {
        mSharedPreferences = cxt.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
    }

    public static synchronized void init(Context cxt){
        if(mSharedPrefencesUtil == null){
            mSharedPrefencesUtil = new SharedPreferencesUtil(cxt);
        }
    }

    /**
     * 单例模式，获取instance实例
     *
     * @return
     */
    public synchronized static SharedPreferencesUtil getInstance() {
        if (mSharedPrefencesUtil == null) {
            throw new RuntimeException("please init first!");
        }

        return mSharedPrefencesUtil;
    }
    final public void saveData(String key , String value){
        editor.putString(key, value).commit();
    }

    final public void saveData(String key , int value){
        editor.putInt(key, value).commit();
    }

    final public void saveData(String key , boolean value){
        editor.putBoolean(key, value).commit();
    }

    final public void saveData(String key , float value){
        editor.putFloat(key, value).commit();
    }

    final public void saveData(String key , long value){
        editor.putLong(key, value).commit();
    }

    final public String getSaveStringData(String key , String defaultValue){
        return mSharedPreferences.getString(key, defaultValue);
    }

    final public int getSaveIntData(String key,int defaultValue){
        return mSharedPreferences.getInt(key, defaultValue);
    }

    final public boolean getSaveBooleanData(String key,boolean defaultValue){
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    final public float getSaveFloatData(String key,float defaultValue){
        return mSharedPreferences.getFloat(key, defaultValue);
    }

    final public long getSaveLongData(String key,long defaultValue){
        return mSharedPreferences.getLong(key, defaultValue);
    }
    final public void cleatData(){
        editor.clear().commit();
    }

 }
