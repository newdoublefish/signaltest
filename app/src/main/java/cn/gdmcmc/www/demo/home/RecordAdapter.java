package cn.gdmcmc.www.demo.home;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import cn.gdmcmc.www.demo.dao.Record;

public class RecordAdapter extends RecyclerArrayAdapter<Record> {
    public DeviceAdapter.OnMyItemClickListener onMyItemClickListener;

    public RecordAdapter(Context context) {
        super(context);
    }
    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecordViewHolder(parent);
    }
    @Override
    public void OnBindViewHolder(final BaseViewHolder holder, final int position) {
        super.OnBindViewHolder(holder, position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onMyItemClickListener!=null){
                    onMyItemClickListener.onItemClick(position,holder);
                }
            }
        });
    }

    public interface OnMyItemClickListener{
        void onItemClick(int position,BaseViewHolder holder);
    }

    public void setOnMyItemClickListener(DeviceAdapter.OnMyItemClickListener listener){
        this.onMyItemClickListener=listener;
    }
}
