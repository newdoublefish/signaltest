package cn.gdmcmc.www.demo.home;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.gdmcmc.www.demo.R;
import cn.gdmcmc.www.demo.bean.DeviceBean;
import cn.gdmcmc.www.demo.util.LogUtil;
import coder.mylibrary.base.BaseFragment;

/**
 * Created by ASIMO on 2017/11/29.
 */

public class DeviceFragment extends BaseFragment implements DeviceContract.View, SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener {
    public static String TAG = "DeviceFragment";
    DeviceContract.Presenter presenter;
    Unbinder unbinder;
    //@BindView(R.id.network_error_layout)
    //ViewStub networkErrorLayout;
    @BindView(R.id.devices_recycler_view)
    EasyRecyclerView devicesRecyclerView;

    private DeviceAdapter deviceAdapter;
    private ArrayList<DeviceBean.DeviceEntity> devices;
    

    public void listDevices() {
        LogUtil.e("------------listDevices------------");
        initData();
        deviceAdapter.addAll(devices);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        LogUtil.e("---------initView-------------------");
        unbinder = ButterKnife.bind(this, view);
        presenter = new DevicePresenter(this);

        initRecyclerView();
        presenter.start();

    }

    private void initRecyclerView(){
        devices=new ArrayList<>();
        RecyclerView.LayoutManager staggerdGridLayoutManager;
        staggerdGridLayoutManager=new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        devicesRecyclerView.setLayoutManager(staggerdGridLayoutManager);
        deviceAdapter=new DeviceAdapter(getHoldingActivity());
        devicesRecyclerView.setAdapter(deviceAdapter);
        deviceAdapter.setMore(R.layout.load_more_layout,this);
        deviceAdapter.setNoMore(R.layout.no_more_layout);
        deviceAdapter.setError(R.layout.network_error);
        deviceAdapter.setOnMyItemClickListener(new DeviceAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(int position, BaseViewHolder holder) {
                LogUtil.d("-------onItemClick-----------------");
            }

            @Override
            public void onItemLongClick(int position, BaseViewHolder holder) {

            }
        });
        devicesRecyclerView.setRefreshListener(this);
    }

    private void initData(){
        for(int i=0;i<5;i++)
        {
            DeviceBean.DeviceEntity de=new DeviceBean.DeviceEntity();
            de.set_id("0");
            de.setName("name"+i);
            de.setUsername("username"+i);
            de.setPassword("123");
            de.set__v(1);
            devices.add(de);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_device;
    }

    @Override
    public void onRefresh() {
        LogUtil.e("-----------onRefresh-----------");
        devices.clear();
        initData();
        deviceAdapter.clear();
        deviceAdapter.addAll(devices);
    }

    @Override
    public void onLoadMore() {
        LogUtil.e("-----------onLoadMore------------");
        //deviceAdapter.notifyDataSetChanged();
    }
}
