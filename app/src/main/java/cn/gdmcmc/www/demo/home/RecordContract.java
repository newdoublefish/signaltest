package cn.gdmcmc.www.demo.home;

import cn.gdmcmc.www.demo.application.BasePresenter;
import cn.gdmcmc.www.demo.application.BaseView;

public interface RecordContract {
    interface View extends BaseView<Presenter> {
        void listRecords();
    }

    interface Presenter extends BasePresenter {

    }
}
