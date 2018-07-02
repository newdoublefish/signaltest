package cn.gdmcmc.www.demo.splash;

/**
 * Created by ASIMO on 2017/11/29.
 */

public class SplashPresenter implements SplashContract.Presenter {

    private SplashContract.View mView;

    public SplashPresenter(SplashContract.View view) {
        mView = view;
    }

    @Override
    public void start() {
        mView.showPic();
    }
}
