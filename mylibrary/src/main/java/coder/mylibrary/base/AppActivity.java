package coder.mylibrary.base;

import android.content.Intent;
import android.os.Bundle;


/**
 * Created by renlei on 2016/5/23.
 */
public abstract class AppActivity extends BaseActivity {

    //获取第一个fragment
    protected abstract BaseFragment getFirstFragment();

    //获取Intent
    protected void handleIntent(Intent intent) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        System.out.println("-----onCreate----2");

        if (null != getIntent()) {
            handleIntent(getIntent());
        }
        //避免重复添加Fragment
        //System.out.println("fragments size:"+getSupportFragmentManager().getFragments().size());
        if (0 == getSupportFragmentManager().getFragments().size()) {
            System.out.println("-----onCreate----3");
            BaseFragment firstFragment = getFirstFragment();
            if (null != firstFragment) {
                addFragment(firstFragment);
            }
        }

        ActivityManager.getInstance().addActivity(this);
    }

    @Override
    protected int getContentViewId() {
        return 0;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().finishActivity(this);
    }
}
