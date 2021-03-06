package cn.gdmcmc.www.demo.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.stealthcopter.networktools.Ping;
import com.stealthcopter.networktools.ping.PingResult;
import com.stealthcopter.networktools.ping.PingStats;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cn.gdmcmc.www.demo.R;
import cn.gdmcmc.www.demo.application.Constants;
import cn.gdmcmc.www.demo.application.MyApplication;
import cn.gdmcmc.www.demo.dao.Record;
import cn.gdmcmc.www.demo.dao.RecordItem;
import cn.gdmcmc.www.demo.util.LogUtil;
import cn.gdmcmc.www.demo.util.SharedPreferencesUtil;

//https://www.cnblogs.com/zhujiabin/p/5404771.html 线程池问题
//https://blog.csdn.net/u013078044/article/details/64123663 休眠运行问题
//https://blog.csdn.net/u013334392/article/details/76187594
public class PingService extends Service {
    private static final String TAG = PingService.class.getSimpleName();
    private final IBinder mBinder = new PingBinder();
    private Boolean runFlag = false;
    private ScheduledExecutorService mThreadPool;//定时器线程池
    private int mCount = 0;
    private OnPingServiceListener mOnDataArrivedListener;
    private String ipAddress;
    private Record record;
    private TelephonyManager mTelephonyManager;
    private PhoneStatListener mListener;
    public int mGsmSignalStrength;
    private PowerManager.WakeLock wakeLock = null;
    private static final String CHANNEL_ID = "11111";
    private static final String CHANNEL_NAME = "ForegroundServiceChannel";
    private String vendor;


    private boolean isFastMobileNetwork() {
        if (mTelephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_LTE) {
            //这里只简单区分两种类型网络，认为4G网络为快速，但最终还需要参考信号值
            return true;
        }
        return false;
    }

    private void getNetWorkInfo() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            switch (info.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    //wifi
                    WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    WifiInfo connectionInfo = manager.getConnectionInfo();
                    int rssi = connectionInfo.getRssi();
                    LogUtil.d(TAG,"当前为wifi网络，信号强度=" + rssi);
                    break;
                case ConnectivityManager.TYPE_MOBILE:
                    //移动网络,可以通过TelephonyManager来获取具体细化的网络类型
                    String netWorkStatus = isFastMobileNetwork() ? "4G网络" : "2G网络";
                    LogUtil.d(TAG,"当前为" + netWorkStatus + "，信号强度=" + mGsmSignalStrength);
                    //mTextView.setText("当前为" + netWorkStatus + "，信号强度=" + mGsmSignalStrength);
                    break;
            }
        } else {
            //mTextView.setText("没有可用网络");
            LogUtil.d(TAG,"没有可用网络");
        }
    }

    private String getVendor()//得到当前电话卡的归属运营商
    {
        //中国移动的是 46000
        //中国联通的是 46001
        //中国电信的是 46003
        String STRNetworkOperator[] = { "46000", "46001", "46003" };
        String vendor[]={"中国移动","中国联通","中国电信"};
        String strNetworkOperator = mTelephonyManager.getNetworkOperator();
        LogUtil.d(TAG,strNetworkOperator);
        if (strNetworkOperator != null) {
            for (int i = 0; i < 3; i++) {
                if (strNetworkOperator.equals(STRNetworkOperator[i])) {
                    return vendor[i];
                }
            }
        }
        return "未知运营商";
    }

    private String getOperatorName() {
        String operator = mTelephonyManager.getSimOperator();
        LogUtil.e(operator);
        if (operator != null) {
            if (operator.equals("46000") || operator.equals("46002")) {
                // operatorName="中国移动";
                //signalTextView.setText("中国移动");
                vendor = "中国移动";
                // Toast.makeText(this, "此卡属于(中国移动)",
                // Toast.LENGTH_SHORT).show();
            } else if (operator.equals("46001")) {
                // operatorName="中国联通";
                vendor = "中国联通";
                // Toast.makeText(this, "此卡属于(中国联通)",
                // Toast.LENGTH_SHORT).show();
            } else if (operator.equals("46003")) {
                // operatorName="中国电信";
                vendor = "中国电信";
                // Toast.makeText(this, "此卡属于(中国电信)",
                // Toast.LENGTH_SHORT).show();
            }else {
                vendor = "未知";
            }
        }
        return vendor;
    }

    private String getYys()
    {
        String operator = mTelephonyManager.getSimOperator();

        if(operator!=null){

            if(operator.equals("46000") || operator.equals("46002") || operator.equals("46004") || operator.equals("46007")){

                vendor = "中国移动";

            }else if(operator.equals("46001") || operator.equals("46006") || operator.equals("46009")){

                vendor = "中国联通";

            }else if(operator.equals("46003") || operator.equals("46005") || operator.equals("46011")){

                vendor = "中国电信";
            }else {
                vendor = "未知";
            }

        }
        return vendor;

    }


    private class PhoneStatListener extends PhoneStateListener {
        //获取信号强度

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
//            super.onSignalStrengthsChanged(signalStrength);
//            mGsmSignalStrength = signalStrength.getGsmSignalStrength();
//            getNetWorkInfo();
            super.onSignalStrengthsChanged(signalStrength);
            String signalInfo = signalStrength.toString();
            String[] params = signalInfo.split(" ");

            LogUtil.d(TAG,signalInfo);
            if(mTelephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_LTE){
                //4G网络 最佳范围   >-90dBm 越大越好
                mGsmSignalStrength = Integer.parseInt(params[9]);
                String yys = getYys();
                if (yys=="中国电信") {
                    int asu = signalStrength.getGsmSignalStrength();
                    mGsmSignalStrength = -113 + 2*asu;
                    LogUtil.d(TAG,mGsmSignalStrength+"");
                }
                LogUtil.d(TAG,"vendor:"+yys+" dbm:"+mGsmSignalStrength+"");
            }else if(mTelephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSDPA ||
                    mTelephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSPA ||
                    mTelephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSUPA ||
                    mTelephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS){
                        //3G网络最佳范围  >-90dBm  越大越好  ps:中国移动3G获取不到  返回的无效dbm值是正数（85dbm）
                        //在这个范围的已经确定是3G，但不同运营商的3G有不同的获取方法，故在此需做判断 判断运营商与网络类型的工具类在最下方
                        String yys = getYys();//获取当前运营商
                        if (yys=="中国移动") {
                            LogUtil.d(TAG,0+"");//中国移动3G不可获取，故在此返回0
                        }else if (yys=="中国联通") {
                            mGsmSignalStrength = signalStrength.getCdmaDbm();
                            LogUtil.d(TAG,mGsmSignalStrength+"");
                        }else if (yys=="中国电信") {
                            mGsmSignalStrength = signalStrength.getEvdoDbm();
                            LogUtil.d(TAG,mGsmSignalStrength+"");
                        }else{
                            int asu = signalStrength.getGsmSignalStrength();
                            mGsmSignalStrength = -113 + 2*asu;
                            LogUtil.d(TAG,mGsmSignalStrength+"");
                        }
            }else{
                        //2G网络最佳范围>-90dBm 越大越好
                        int asu = signalStrength.getGsmSignalStrength();
                        mGsmSignalStrength = -113 + 2*asu;
                         LogUtil.d(TAG,mGsmSignalStrength+"");
            }
        }
    }

    @Override
    public void onCreate() {
        LogUtil.d(TAG,"-------onCreate----------");
        /*if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);

            Notification notification = new Notification.Builder(getApplicationContext(), CHANNEL_ID).build();
            startForeground(1, notification);
        }*/

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, PingService.class.getName());
        wakeLock.acquire();
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mListener=new PhoneStatListener();
        mTelephonyManager.listen(mListener, PhoneStatListener.LISTEN_SIGNAL_STRENGTHS);
    }



    public void setOnPingServiceListener(OnPingServiceListener listener)
    {
        this.mOnDataArrivedListener = listener;
    }

    private void doPing(String ipAddress) throws Exception {
        // Perform an asynchronous ping
        Ping.onAddress(ipAddress).setTimeOutMillis(1000).setTimes(5).doPing(new Ping.PingListener() {
            @Override
            public void onResult(PingResult pingResult) {
                //appendResultsText(String.format("%.2f ms", pingResult.getTimeTaken()));
            }

            @Override
            public void onFinished(PingStats pingStats) {
                LogUtil.e(TAG,"onFinished");
                if(mGsmSignalStrength>0) //有时会出现非常大的正值，原因是无法识别到是哪个运营商
                {
                    mGsmSignalStrength = -200;
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd HH:mm:ss");
                Date date = new Date(System.currentTimeMillis());
                if (mOnDataArrivedListener != null) {
                    mOnDataArrivedListener.onPingResult(pingStats.getAverageTimeTaken(),mGsmSignalStrength,vendor);
                }


                RecordItem item = new RecordItem(null,simpleDateFormat.format(date),pingStats.getAverageTimeTaken(),mGsmSignalStrength,record.getId());
                try{
                    MyApplication.getmDaoSession().getRecordItemDao().insert(item);
                    LogUtil.d(TAG,"insert record item success:"+item.getId()+":"+item.getDate()+":"+item.getDelay()+":"+item.getSignal()+":"+item.getRecordId());
                }catch (Exception e){
                    LogUtil.e(TAG,"insert record item error:");
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                // TODO: STUB METHOD
                LogUtil.e(TAG,"onError");
            }
        });

    }

    private void doPingSync(String ipAddress) throws Exception {
        // Perform an asynchronous ping
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        PingResult pingResult = Ping.onAddress(ipAddress).setTimeOutMillis(500).doPing();
        if (mOnDataArrivedListener != null) {
            mOnDataArrivedListener.onPingResult(pingResult.getTimeTaken(),mGsmSignalStrength,vendor);
        }
        RecordItem item = new RecordItem(null,simpleDateFormat.format(date),pingResult.getTimeTaken(),mGsmSignalStrength,record.getId());
        try{
            MyApplication.getmDaoSession().getRecordItemDao().insert(item);
            LogUtil.d(TAG,"insert record item success:"+item.getId()+":"+item.getDate()+":"+item.getDelay()+":"+item.getSignal()+":"+item.getRecordId());
        }catch (Exception e){
            LogUtil.e(TAG,"insert record item error:");
            e.printStackTrace();
        }

    }


    final TimerTask task = new TimerTask() {

        @Override
        public void run() {
            try {
                    //doPingSync(ipAddress);
                doPing(ipAddress);
                LogUtil.d("after invoke doping");
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    };


    public class PingBinder extends Binder{
        public PingService getService(){
            return PingService.this;
        }
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "ping service destroy!!", Toast.LENGTH_SHORT).show();
        if (wakeLock != null) {
            wakeLock.release();
            wakeLock = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        //开启定时任务
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        //关闭定时器
        return true;
    }

    public void startRecord(Record record,String ipAddress,int interval)
    {
        this.ipAddress = ipAddress;
        this.record = record;
        runFlag = true;
        mCount = 0;
        mThreadPool = Executors.newScheduledThreadPool(1);
        //String timeinterval = SharedPreferencesUtil.getInstance().getSaveStringData(Constants.INTERVAL_PRE,this.getString(R.string.default_interval));
        //int interval = Integer.parseInt(timeinterval) * 1000;
        mThreadPool.scheduleAtFixedRate(task, 0, interval, TimeUnit.MILLISECONDS);
    }

    public boolean getRecord()
    {
        return runFlag;
    }

    public void stopRecord()
    {
        runFlag = false;
        if (mThreadPool != null) {
            mThreadPool.shutdownNow();
            mCount = 0;
        }
        mOnDataArrivedListener.onPingStatusChange(runFlag);

    }
}
