package cn.gdmcmc.www.demo.home;

/**
 * Created by ASIMO on 2017/11/30.
 */

public class DevicePresenter implements DeviceContract.Presenter {
    private DeviceContract.View mView;

    public DevicePresenter(DeviceContract.View view)
    {
        this.mView=view;
    }

    @Override
    public void start() {
        mView.listDevices();
    }
}
