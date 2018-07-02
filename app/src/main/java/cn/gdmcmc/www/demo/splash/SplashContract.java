package cn.gdmcmc.www.demo.splash;

import cn.gdmcmc.www.demo.application.BasePresenter;
import cn.gdmcmc.www.demo.application.BaseView;

/**
 * Created by ASIMO on 2017/11/29.
 */

public interface SplashContract {
    interface View extends BaseView<Presenter>{
        void showPic();
    }

    interface Presenter extends BasePresenter{

    }
}
