package cn.gdmcmc.www.demo.splash;

import android.view.View;

import cn.gdmcmc.www.demo.R;
import coder.mylibrary.base.AppActivity;
import coder.mylibrary.base.BaseFragment;

/**
 * Created by ASIMO on 2017/11/29.
 */

public class SplashActivity extends AppActivity {
    @Override
    protected BaseFragment getFirstFragment() {
        return SplashFragment.getInstance();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.splash_fragment;
    }

    @Override
    public void onClick(View v) {

    }
}
