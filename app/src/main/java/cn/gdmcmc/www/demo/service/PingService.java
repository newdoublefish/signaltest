package cn.gdmcmc.www.demo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.stealthcopter.networktools.Ping;
import com.stealthcopter.networktools.ping.PingResult;
import com.stealthcopter.networktools.ping.PingStats;

import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PingService extends Service {
    private static final String TAG = PingService.class.getSimpleName();
    private final IBinder mBinder = new PingBinder();
    private Boolean runFlag = false;
    private ScheduledExecutorService mThreadPool;//定时器线程池
    private int mCount = 0;
    private OnPingServiceListener mOnDataArrivedListener;
    private String ipAddress;


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

                if (mOnDataArrivedListener != null) {
                    mOnDataArrivedListener.onPingResult(pingStats.getAverageTimeTaken());
                }
            }

            @Override
            public void onError(Exception e) {
                // TODO: STUB METHOD
            }
        });

    }


    final TimerTask task = new TimerTask() {

        @Override
        public void run() {
            try {
                doPing(ipAddress);
            }catch (Exception e){

            }

        }
    };


    public class PingBinder extends Binder{
        public PingService getService(){
            return PingService.this;
        }
    }

    @Override
    public void onCreate() {
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

    public void startRecord(String ipAddress)
    {
        this.ipAddress = ipAddress;
        runFlag = true;
        mCount = 0;
        mThreadPool = Executors.newScheduledThreadPool(1);
        mThreadPool.scheduleAtFixedRate(task, 0, 2000, TimeUnit.MILLISECONDS);
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
    }


}
