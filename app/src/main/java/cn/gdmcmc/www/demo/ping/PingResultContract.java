package cn.gdmcmc.www.demo.ping;

import cn.gdmcmc.www.demo.application.BasePresenter;
import cn.gdmcmc.www.demo.application.BaseView;


public interface PingResultContract {
    interface View extends BaseView<PingResultContract.Presenter> {
        void showResult();
    }

    interface Presenter extends BasePresenter {

    }
}
