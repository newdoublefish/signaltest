package cn.gdmcmc.www.demo.ping;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.gdmcmc.www.demo.R;
import cn.gdmcmc.www.demo.application.MyApplication;
import cn.gdmcmc.www.demo.dao.Record;
import cn.gdmcmc.www.demo.dao.RecordItem;
import cn.gdmcmc.www.demo.util.LogUtil;
import coder.mylibrary.base.BaseFragment;
import lecho.lib.hellocharts.animation.ChartAnimationListener;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ViewportChangeListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class PingResultFragment extends BaseFragment implements PingResultContract.View{
    private static final String TAG = PingResultFragment.class.getSimpleName();
    private static PingResultFragment pingResultFragment=null;
    @BindView(R.id.ping_chart)
    LineChartView lineChart;
    private Unbinder unbinder;
    private PingPesultPresenter pingPesultPresenter;
    private static Long recordId;
    private List<PointValue> mPointDelayValues = new ArrayList<PointValue>();
    private List<PointValue> mPointSignalValues = new ArrayList<PointValue>();
    private List<PointValue> mSignalValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        pingPesultPresenter = new PingPesultPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ping_result;
    }

    public static PingResultFragment getInstance(Long id)
    {
        recordId = id;
        LogUtil.d(TAG,"recordId:"+recordId);
        if(pingResultFragment == null)
        {
            pingResultFragment = new PingResultFragment();
        }
        return pingResultFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        pingPesultPresenter.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showResult() {
        mPointDelayValues.clear();
        mPointSignalValues.clear();
        mAxisXValues.clear();
        mSignalValues.clear();
        try{

            Record record = MyApplication.getmDaoSession().getRecordDao().load(recordId);
            LogUtil.d(record.getItems().size()+"");
            int count=0;
            for (RecordItem item :
                    record.getItems()) {
                PointValue pv = new PointValue(count, item.getDelay());
                pv.setLabel(String.format("%.2f ms", item.getDelay()));
                mPointDelayValues.add(pv);

                PointValue signalPv = new PointValue(count,item.getSignal());
                pv.setLabel(String.format("%.1f dbm",item.getSignal()));
                mPointSignalValues.add(signalPv);

                mAxisXValues.add(new AxisValue(count).setLabel(item.getDate()));

                mSignalValues.add(new PointValue(count,-85));
                count++;
            }
            getHoldingActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showChart();
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private Viewport initViewPort() {
        Viewport port = new Viewport();
        port.top = 150;//Y轴上限，固定(不固定上下限的话，Y轴坐标值可自适应变化)
        port.bottom = -150;//Y轴下限，固定
        port.left = 0;//X轴左边界，变化
        port.right = 5;//X轴右边界，变化
        return port;
    }

    private void showChart()
    {
        List<Line> lines = new ArrayList<Line>();
        Line line = new Line(mPointDelayValues).setColor(Color.parseColor("#FFCD41"));  //折线的颜色（橙色）
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line.setFilled(true);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
        line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        //mPointSignalValues
        Line signalLine = new Line(mPointSignalValues).setColor(R.color.black);  //折线的颜色（橙色）
        signalLine.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        signalLine.setCubic(false);//曲线是否平滑，即是曲线还是折线
        signalLine.setFilled(true);//是否填充曲线的面积
        signalLine.setHasLabels(true);//曲线的数据坐标是否加上备注
        signalLine.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        signalLine.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        signalLine.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(signalLine);

        lines.add(line);

        Line signal = new Line(mSignalValues).setColor(Color.parseColor("#FFCD41"));  //折线的颜色（橙色）
        signal.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        signal.setCubic(false);//曲线是否平滑，即是曲线还是折线
        signal.setFilled(true);//是否填充曲线的面积
        signal.setHasLabels(false);//曲线的数据坐标是否加上备注
        signal.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        signal.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        signal.setHasPoints(false);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        //lines.add(signal);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.GRAY);  //设置字体颜色
        //axisX.setName("date");  //表格名称
        axisX.setTextSize(10);//设置字体大小
        axisX.setMaxLabelChars(7); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
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
//        lineChart.setMaxZoom((float) 2);//最大方法比例
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);
        //lineChart.setOnValueTouchListener();

        Viewport port;
        port = initViewPort();
        lineChart.setCurrentViewport(port);
        lineChart.setViewportChangeListener(new ViewportChangeListener() {
            @Override
            public void onViewportChanged(Viewport viewport) {
                LogUtil.d(TAG,viewport.left + "::"+viewport.right);
            }
        });
        //int numValues = mPointValues.size();
        //Viewport viewport =new Viewport(0,  lineChart.getMaximumViewport().height()*1.25f, numValues > 5 ? 5 : numValues, 0);
        //lineChart.setCurrentViewport(viewport);
        //lineChart.moveTo(0, 0);

    }
}
