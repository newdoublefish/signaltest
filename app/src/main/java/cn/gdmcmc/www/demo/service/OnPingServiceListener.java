package cn.gdmcmc.www.demo.service;

public interface OnPingServiceListener {
    void onPingResult(float data,int signal,String vendor);
    void onPingStatusChange(boolean status);
}
