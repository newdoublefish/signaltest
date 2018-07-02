package cn.gdmcmc.www.demo.home;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import cn.gdmcmc.www.demo.bean.DeviceBean;

/**
 * Created by ray on 2017/12/4.
 */

public class DeviceAdapter extends RecyclerArrayAdapter<DeviceBean.DeviceEntity> {
    public OnMyItemClickListener onMyItemClickListener;

    public DeviceAdapter(Context context) {
        super(context);
    }
    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new DeviceViewHolder(parent);
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

    public void setOnMyItemClickListener(OnMyItemClickListener listener){
        this.onMyItemClickListener=listener;
    }
}
