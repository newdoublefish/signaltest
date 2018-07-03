package cn.gdmcmc.www.demo.home;

public class PingPresenter implements PingContract.Presenter {
    PingContract.View mView;

    public PingPresenter(PingContract.View view)
    {
        mView = view;
    }

    @Override
    public void start() {

    }
}
