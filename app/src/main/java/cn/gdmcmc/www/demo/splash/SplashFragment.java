package cn.gdmcmc.www.demo.splash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.gdmcmc.www.demo.R;
import cn.gdmcmc.www.demo.home.HomeActivity;
import cn.gdmcmc.www.demo.util.LogUtil;
import coder.mylibrary.base.ActivityManager;
import coder.mylibrary.base.BaseFragment;

/**
 * Created by ASIMO on 2017/11/29.
 */

public class SplashFragment extends BaseFragment implements SplashContract.View {
    @BindView(R.id.splash)
    ImageView mSplashImg;
    private SplashPresenter splashPresenter;
    private Unbinder unbinder;
    private static String TAG=SplashFragment.class.getName();
    private ScaleAnimation scaleAnimation;
    @Override
    public void showPic() {
        Glide.with(getActivity())
                .load(R.drawable.splash)
                .animate(scaleAnimation)
                .into(mSplashImg);
    }

    public static SplashFragment getInstance() {
        LogUtil.d("----------SplashFragment getInstance-----------");
        SplashFragment splashFragment = new SplashFragment();
        return splashFragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        splashPresenter = new SplashPresenter(SplashFragment.this);
        initAnim();
    }

    private void initAnim() {
        scaleAnimation = new ScaleAnimation(1.0f, 1.1f, 1.0f, 1.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(2500);
        //mSplashImg.startAnimation(scaleAnimation);

        //缩放动画监听
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(getActivity(), HomeActivity.class));
                ActivityManager.getInstance().finishActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_splash;
    }

    @Override
    public void onResume() {
        super.onResume();
        splashPresenter.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
