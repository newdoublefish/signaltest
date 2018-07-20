package cn.gdmcmc.www.demo.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import cn.gdmcmc.www.demo.R;
import cn.gdmcmc.www.demo.service.PingService;
import cn.gdmcmc.www.demo.util.LogUtil;

public class SettingPrefFragment extends PreferenceFragment {
    private static String TAG = SettingPrefFragment.class.getSimpleName();
    EditTextPreference ipAddressEditTextPreference;
    EditTextPreference intervalEditTextPreference;
    SharedPreferences sharedpreferences;

    SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener(){
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            LogUtil.d(TAG,"click:"+key);
            if(key.equals(getActivity().getString(R.string.key_address)))
            {
                String address = sharedPreferences.getString(getActivity().getString(R.string.key_address),getActivity().getString(R.string.default_ip));
                ipAddressEditTextPreference.setSummary(address);

            }else if(key.equals(getActivity().getString(R.string.key_interval)))
            {
                String interval = sharedPreferences.getString(getActivity().getString(R.string.key_interval),getActivity().getString(R.string.default_interval));
                LogUtil.d(TAG,"interval:"+interval);
                intervalEditTextPreference.setSummary(interval+"s");
            }
        }
    };

    @SuppressLint("ResourceType")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.preferences);
        initPreferences(getPreferenceManager());

    }

    private void initPreferences(PreferenceManager manager) {
        ipAddressEditTextPreference = (EditTextPreference)findPreference(getActivity().getString(R.string.key_address));
        intervalEditTextPreference = (EditTextPreference)findPreference(getActivity().getString(R.string.key_interval));
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String address = sharedpreferences.getString(getActivity().getString(R.string.key_address),getActivity().getString(R.string.default_ip));
        ipAddressEditTextPreference.setSummary(address);
        String interval = sharedpreferences.getString(getActivity().getString(R.string.key_interval),getActivity().getString(R.string.default_interval));
        intervalEditTextPreference.setSummary(interval+"s");
        /*sharedpreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                LogUtil.d(TAG,"click:"+key);
                if(key.equals(getActivity().getString(R.string.key_address)))
                {
                    String address = sharedPreferences.getString(getActivity().getString(R.string.key_address),getActivity().getString(R.string.default_ip));
                    ipAddressEditTextPreference.setSummary(address);

                }else if(key.equals(getActivity().getString(R.string.key_interval)))
                {
                    String interval = sharedPreferences.getString(getActivity().getString(R.string.key_interval),getActivity().getString(R.string.default_interval));
                    LogUtil.d(TAG,"interval:"+interval);
                    intervalEditTextPreference.setSummary(interval+"s");
                }
            }
        });*/

    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public void onResume() {
        sharedpreferences.registerOnSharedPreferenceChangeListener(listener);
        super.onResume();
        Log.e(TAG, "---onResume---");
    }

    @Override
    public void onPause() {
        sharedpreferences.unregisterOnSharedPreferenceChangeListener(listener);
        super.onPause();
        Log.e(TAG, "---onPause---");
    }
}

