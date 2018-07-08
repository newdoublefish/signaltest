package cn.gdmcmc.www.demo.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.gdmcmc.www.demo.R;
import cn.gdmcmc.www.demo.service.PingService;
import cn.gdmcmc.www.demo.util.LogUtil;
import coder.mylibrary.base.AppActivity;
import coder.mylibrary.base.BaseFragment;

/**
 * Created by ASIMO on 2017/11/29.
 */

public class HomeActivity extends AppActivity {
    public static String TAG = "HomeActivity";
    @BindView(R.id.btn1)
    Button btn1;
    @BindView(R.id.btn2)
    Button btn2;
    @BindView(R.id.btn3)
    Button btn3;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.home_layout)
    CoordinatorLayout homeLayout;
    private Unbinder unbinder;
    private long exitTime = 0;
    BaseFragment firstFragment;
    BaseFragment secondFragment;
    PingService mPingservice;
    List<Button> btnList=new ArrayList<Button>();
    @Override
    protected int getContentViewId() {
        return R.layout.activity_home;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
        btnList.add(btn1);
        btnList.add(btn2);
        btnList.add(btn3);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        secondFragment = new RecordFragment();

        //btn1.setBackgroundColor(getResources().getColor(R.color.menu_bar_pressed));
        //btn2.setBackgroundColor(getResources().getColor(R.color.menu_bar));
        clickButton(0);

        Intent intent = new Intent(HomeActivity.this,
                PingService.class);
        startService(intent);
    }

    private void clickButton(int index)
    {
        int i=0;
        for (Button btn:btnList
             ) {
            if(i==index)
            {
                btn.setBackgroundColor(getResources().getColor(R.color.menu_bar_pressed));
            }else{
                btn.setBackgroundColor(getResources().getColor(R.color.menu_bar));
            }
            i++;
        }
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.devices_fragment;
    }

    @Override
    protected BaseFragment getFirstFragment() {
        LogUtil.e("-------------getFirstFragment--------------!!!!");
        firstFragment = new PingFragment();
        return firstFragment;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                this.addFragment(firstFragment);
                //btn1.setBackgroundColor(getResources().getColor(R.color.menu_bar_pressed));
                //btn2.setBackgroundColor(getResources().getColor(R.color.menu_bar));
                clickButton(0);
                break;
            case R.id.btn2:
                this.addFragment(secondFragment);
                //btn2.setBackgroundColor(getResources().getColor(R.color.menu_bar_pressed));
                //btn1.setBackgroundColor(getResources().getColor(R.color.menu_bar));
                clickButton(1);
                break;
            case R.id.btn3:
                this.addFragment(secondFragment);
                //btn2.setBackgroundColor(getResources().getColor(R.color.menu_bar_pressed));
                //btn1.setBackgroundColor(getResources().getColor(R.color.menu_bar));
                clickButton(2);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            //两秒之内按返回键就会退出
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Snackbar.make(homeLayout, "再按一次退出程序哦~", Snackbar.LENGTH_LONG).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            //Intent intent = new Intent(HomeActivity.this, AboutActivity.class);
            //startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
