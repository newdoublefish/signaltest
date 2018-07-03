package cn.gdmcmc.www.demo.home;

import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import cn.gdmcmc.www.demo.R;
import cn.gdmcmc.www.demo.dao.Record;

public class RecordViewHolder extends BaseViewHolder<Record> {
    private TextView tv;
    public RecordViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_device);
        tv=$(R.id.tv);
    }

    @Override
    public void setData(Record data) {
        super.setData(data);
        tv.setText(data.getName());
    }
}
