package cn.gdmcmc.www.demo.home;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.stealthcopter.networktools.Ping;
import com.stealthcopter.networktools.ping.PingResult;
import com.stealthcopter.networktools.ping.PingStats;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.gdmcmc.www.demo.R;
import cn.gdmcmc.www.demo.application.MyApplication;
import cn.gdmcmc.www.demo.dao.Record;
import cn.gdmcmc.www.demo.service.OnPingServiceListener;
import cn.gdmcmc.www.demo.service.PingService;
import coder.mylibrary.base.BaseFragment;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class PingFragment extends BaseFragment implements PingContract.View{
    private String TAG = "PingFragment";
    private Unbinder unbinder;
    private PingPresenter pingPresenter;
    @BindView(R.id.pingButton)
    Button startBtn;
    @BindView(R.id.chart)
    LineChartView lineChart;
    @BindView(R.id.editIpAddress)
    EditText editIpAddress;
    @BindView(R.id.text)
    TextView textView;

    private Boolean runFlag = false;
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    private boolean mIsBound;
    private PingService mPingService;

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mPingService = ((PingService.PingBinder)service).getService();
            //回调监听
            // Tell the user about this for our demo.
            Toast.makeText(getHoldingActivity(),"已经链接",
                    Toast.LENGTH_SHORT).show();
            mIsBound = true;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    boolean flag = mPingService.getRecord();
                    if(flag)
                    {
                        startBtn.setText("Stop");
                    }
                }
            });

            mPingService.setOnPingServiceListener(new OnPingServiceListener() {
                @Override
                public void onPingResult(final float data) {
                    //不是在主线程，不能直接操作主UI,切换到主线程
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(data+ " ms");
                        }
                    });
                }
            });


        }

        public void onServiceDisconnected(ComponentName className) {
            mPingService = null;
            Toast.makeText(getHoldingActivity(), "未链接",
                    Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        pingPresenter = new PingPresenter(PingFragment.this);
    }

    @OnClick({R.id.pingButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pingButton:
                if (startBtn.getText().toString().equals("Start")) {

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd HH:mm:ss");
                    Date date = new Date(System.currentTimeMillis());
                    Record record = new Record(null,simpleDateFormat.format(date),editIpAddress.getText().toString());
                    try{
                        MyApplication.getmDaoSession().getRecordDao().insert(record);
                        Toast.makeText(getHoldingActivity(),"插入成功"+record.getId(),Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Toast.makeText(getHoldingActivity(),"插入失败",Toast.LENGTH_SHORT).show();
                    }
                    mPingService.startRecord(record,editIpAddress.getText().toString());
                    runFlag = true;
                    startBtn.setText("Stop");
                    mPointValues.clear();
                    mPointValues.clear();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (runFlag) {
                                try {
                                    //doPing();
                                    Thread.sleep(1000);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    startBtn.setText("Start");
                                }
                            });
                        }
                    }).start();
                } else {
                    mPingService.stopRecord();
                    runFlag = false;
                }
                break;
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ping;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private Viewport initViewPort(float currentX) {
        Viewport port = new Viewport();
        port.top = 100;//Y轴上限，固定(不固定上下限的话，Y轴坐标值可自适应变化)
        port.bottom = 0;//Y轴下限，固定
        if(currentX > 5) {
            port.left = currentX-5;//X轴左边界，变化
            port.right = currentX;//X轴右边界，变化
        }else{
            port.left = 0;//X轴左边界，变化
            port.right = 5;//X轴右边界，变化
        }
        return port;
    }

    private void UpdateChart(float average)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        List<Line> lines = new ArrayList<Line>();
        int postion = mPointValues.size();
        PointValue pv = new PointValue(postion, average);
        pv.setLabel(String.format("%.2f ms", average));
        mPointValues.add(pv);

        mAxisXValues.add(new AxisValue(postion).setLabel(simpleDateFormat.format(date)));
        Line line = new Line(mPointValues).setColor(Color.parseColor("#FFCD41"));  //折线的颜色（橙色）
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
//      line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.GRAY);  //设置字体颜色
        //axisX.setName("date");  //表格名称
        axisX.setTextSize(10);//设置字体大小
        axisX.setMaxLabelChars(5); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        //data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线

        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
        axisY.setName("");//y轴标注
        axisY.setTextSize(10);//设置字体大小
        data.setAxisYLeft(axisY);  //Y轴设置在左边

        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        lineChart.setMaxZoom((float) 2);//最大方法比例
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);

        float xAxisValue = mPointValues.get(postion).getX();
        Viewport port;
        port = initViewPort(xAxisValue);
        lineChart.setCurrentViewport(port);

    }

    private void appendResultsText(final String text) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //resultText.append(text + "\n");
            }
        });
    }

    private void painChart(final float average) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UpdateChart(average);
            }
        });
    }

    private void doPing() throws Exception {
        String ipAddress = editIpAddress.getText().toString();

        if (TextUtils.isEmpty(ipAddress)) {
            appendResultsText("Invalid Ip Address");
            return;
        }

        // Perform a single synchronous ping
        /*PingResult pingResult = Ping.onAddress(ipAddress).setTimeOutMillis(1000).doPing();

        appendResultsText("Pinging Address: " + pingResult.getAddress().getHostAddress());
        appendResultsText("HostName: " + pingResult.getAddress().getHostName());
        appendResultsText(String.format("%.2f ms", pingResult.getTimeTaken()));*/


        // Perform an asynchronous ping
        Ping.onAddress(ipAddress).setTimeOutMillis(1000).setTimes(5).doPing(new Ping.PingListener() {
            @Override
            public void onResult(PingResult pingResult) {
                //appendResultsText(String.format("%.2f ms", pingResult.getTimeTaken()));
            }

            @Override
            public void onFinished(PingStats pingStats) {
//                appendResultsText(String.format("Pings: %d, Packets lost: %d",
//                        pingStats.getNoPings(), pingStats.getPacketsLost()));
//                appendResultsText(String.format("Min/Avg/Max Time: %.2f/%.2f/%.2f ms",
//                        pingStats.getMinTimeTaken(), pingStats.getAverageTimeTaken(), pingStats.getMaxTimeTaken()));
                painChart(pingStats.getAverageTimeTaken());
            }

            @Override
            public void onError(Exception e) {
                // TODO: STUB METHOD
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "---onStart---");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "---onResume---");
        getHoldingActivity().bindService(new Intent(getHoldingActivity(),
                PingService.class), mConnection, Context.BIND_AUTO_CREATE);


    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "---onPause---");

        if (mIsBound) {
            mPingService.setOnPingServiceListener(null);
            getHoldingActivity().unbindService(mConnection);
            mIsBound = false;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "---onStop---");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "---onDestroy---");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(TAG, "---onDetach---");
    }
}
