package cn.gdmcmc.www.demo.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.Toast;

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
import cn.gdmcmc.www.demo.dao.RecordItem;
import cn.gdmcmc.www.demo.ping.PingResultActivity;
import cn.gdmcmc.www.demo.util.LogUtil;
import coder.mylibrary.base.BaseFragment;
//TODO:https://blog.csdn.net/qq_34414005/article/details/53448048 长按删除
//
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

    public void showPopMenu(View view,final int pos){
        PopupMenu popupMenu = new PopupMenu(getHoldingActivity(),view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_record,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                recordAdapter.removeItem(pos);
                try{
                    for (RecordItem recordItem:records.get(pos).getItems())
                    {
                        MyApplication.getmDaoSession().getRecordItemDao().delete(recordItem);
                    }
                    MyApplication.getmDaoSession().getRecordDao().delete(records.get(pos));
                    records.remove(pos);
                    recordAdapter.clear();
                    recordAdapter.addAll(records);
                }catch (Exception e){
                    e.printStackTrace();
                }
                return false;
            }
        });
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                //Toast.makeText(getHoldingActivity(), "关闭PopupMenu", Toast.LENGTH_SHORT).show();
            }
        });
        popupMenu.show();
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

            @Override
            public void onItemLongClick(int position, BaseViewHolder holder) {
                LogUtil.d("-------onItemLongClick-----------------");
                showPopMenu(holder.itemView,position);

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
