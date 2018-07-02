package cn.gdmcmc.www.demo.home;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import cn.gdmcmc.www.demo.R;
import cn.gdmcmc.www.demo.bean.DeviceBean;

/**
 * Created by ray on 2017/12/4.
 */

public class DeviceViewHolder extends BaseViewHolder<DeviceBean.DeviceEntity> {
    private TextView tv;
    public DeviceViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_device);
        tv=$(R.id.tv);
    }

    @Override
    public void setData(DeviceBean.DeviceEntity data) {
        super.setData(data);
        tv.setText(data.getName());
    }
}
