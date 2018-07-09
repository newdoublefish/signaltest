package cn.gdmcmc.www.demo.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.gdmcmc.www.demo.R;
import cn.gdmcmc.www.demo.application.Constants;
import cn.gdmcmc.www.demo.util.SharedPreferencesUtil;
import coder.mylibrary.base.BaseFragment;

public class SettingFragment extends BaseFragment{
    Unbinder unbinder;
    @BindView(R.id.ip)
    TextView ipTextView;
    @BindView(R.id.time)
    TextView timeTextView;
    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        String ipAddress = SharedPreferencesUtil.getInstance().getSaveStringData(Constants.IPADDRESS_PRE,getHoldingActivity().getString(R.string.default_ip));
        ipTextView.setText(ipAddress);
        String timeInterval = SharedPreferencesUtil.getInstance().getSaveStringData(Constants.INTERVAL_PRE,getHoldingActivity().getString(R.string.default_interval));
        timeTextView.setText(timeInterval);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @OnClick({R.id.ip,R.id.time})
    public void onViewClicked(View view) {
        switch (view.getId())
        {
            case R.id.ip:
                AlertDialog.Builder builder = new AlertDialog.Builder(getHoldingActivity());
                //builder.setIcon(R.drawable.ic_launcher);
                builder.setTitle("请输IP地址");
                //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
                View dialogView = LayoutInflater.from(getHoldingActivity()).inflate(R.layout.dialog_ip, null);
                //    设置我们自己定义的布局文件作为弹出框的Content
                builder.setView(dialogView);

                final EditText ipaddr = (EditText)dialogView.findViewById(R.id.ipaddr);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        String a = ipaddr.getText().toString().trim();
                        SharedPreferencesUtil.getInstance().saveData(Constants.IPADDRESS_PRE,a);
                        ipTextView.setText(a);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });
                builder.show();
                break;
            case R.id.time:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getHoldingActivity());
                //builder.setIcon(R.drawable.ic_launcher);
                builder1.setTitle("请输测试间隔时间");
                //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
                View dialogView1 = LayoutInflater.from(getHoldingActivity()).inflate(R.layout.dialog_ip, null);
                //    设置我们自己定义的布局文件作为弹出框的Content
                builder1.setView(dialogView1);

                final EditText time = (EditText)dialogView1.findViewById(R.id.ipaddr);
                builder1.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        String a = time.getText().toString().trim();
                        SharedPreferencesUtil.getInstance().saveData(Constants.INTERVAL_PRE,a);
                        timeTextView.setText(a);
                    }
                });
                builder1.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });
                builder1.show();
                break;
        }
    }
}
