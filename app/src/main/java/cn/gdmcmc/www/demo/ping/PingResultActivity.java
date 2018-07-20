package cn.gdmcmc.www.demo.ping;

import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.View;

import cn.gdmcmc.www.demo.R;
import coder.mylibrary.base.AppActivity;
import coder.mylibrary.base.BaseFragment;

public class PingResultActivity extends AppActivity {
    @Override
    protected BaseFragment getFirstFragment() {
        return PingResultFragment.getInstance(getIntent().getLongExtra("current", 0));
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_ping_result;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fragment;
    }

    @Override
    public void onClick(View v) {

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();
        return super.onKeyDown(keyCode, event);
    }

}
