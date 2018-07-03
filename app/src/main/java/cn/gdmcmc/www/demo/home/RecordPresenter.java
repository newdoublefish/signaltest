package cn.gdmcmc.www.demo.home;

public class RecordPresenter implements RecordContract.Presenter {
    RecordContract.View mView;

    public RecordPresenter(RecordContract.View view)
    {
        mView = view;
    }

    @Override
    public void start() {
        mView.listRecords();
    }
}
