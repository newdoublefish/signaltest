package cn.gdmcmc.www.demo.home;

import cn.gdmcmc.www.demo.application.BasePresenter;
import cn.gdmcmc.www.demo.application.BaseView;

/**
 * Created by ASIMO on 2017/11/29.
 */

public interface DeviceContract {
    interface View extends BaseView<Presenter>{
        void listDevices();
    }

    interface Presenter extends BasePresenter{

    }
}
