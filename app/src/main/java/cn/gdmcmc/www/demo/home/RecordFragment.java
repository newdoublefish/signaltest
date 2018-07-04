package cn.gdmcmc.www.demo.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewStub;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.gdmcmc.www.demo.R;
import cn.gdmcmc.www.demo.application.MyApplication;
import cn.gdmcmc.www.demo.dao.Record;
import cn.gdmcmc.www.demo.ping.PingResultActivity;
import cn.gdmcmc.www.demo.util.LogUtil;
import coder.mylibrary.base.BaseFragment;

public class RecordFragment extends BaseFragment implements RecordContract.View,SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener {
    public static String TAG = "RecordFragment";
    RecordContract.Presenter presenter;
    Unbinder unbinder;
    @BindView(R.id.network_error_layout)
    ViewStub networkErrorLayout;
    @BindView(R.id.devices_recycler_view)
    EasyRecyclerView devicesRecyclerView;

    private RecordAdapter recordAdapter;
    private List<Record> records;
    @Override
    public void onRefresh() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_device;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        LogUtil.e("---------initView-------------------");
        unbinder = ButterKnife.bind(this, view);
        presenter = new RecordPresenter(this);

        initRecyclerView();
        presenter.start();
    }

    private void initRecyclerView(){
        RecyclerView.LayoutManager staggerdGridLayoutManager;
        staggerdGridLayoutManager=new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        devicesRecyclerView.setLayoutManager(staggerdGridLayoutManager);
        recordAdapter=new RecordAdapter(getContext());
        devicesRecyclerView.setAdapter(recordAdapter);
        recordAdapter.setMore(R.layout.load_more_layout,this);
        recordAdapter.setNoMore(R.layout.no_more_layout);
        recordAdapter.setError(R.layout.network_error);
        recordAdapter.setOnMyItemClickListener(new DeviceAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(int position, BaseViewHolder holder) {
                LogUtil.d("-------onItemClick-----------------");
                Intent intent = new Intent(getHoldingActivity(), PingResultActivity.class);

                intent.putExtra("current", records.get(position).getId());
                LogUtil.d("onitem click record id:"+records.get(position).getId());
                startActivity(intent);

            }
        });
        devicesRecyclerView.setRefreshListener(this);
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void listRecords() {
        LogUtil.e("------------listDevices------------");
        initData();
        recordAdapter.addAll(records);
    }
    private void initData(){
        records = MyApplication.getmDaoSession().getRecordDao().loadAll();
    }

}
