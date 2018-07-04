package cn.gdmcmc.www.demo.ping;

public class PingPesultPresenter implements  PingResultContract.Presenter {
    private PingResultContract.View mView;

    public PingPesultPresenter(PingResultContract.View view) {
        mView = view;
    }
    @Override
    public void start() {
        mView.showResult();
    }
}
