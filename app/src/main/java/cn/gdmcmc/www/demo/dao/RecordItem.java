package cn.gdmcmc.www.demo.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity
public class RecordItem {
    @Id(autoincrement = true)
    private Long id;
    private String date;
    private float delay;
    private float signal;
    private Long recordId;
    @Generated(hash = 174789671)
    public RecordItem(Long id, String date, float delay, float signal,
            Long recordId) {
        this.id = id;
        this.date = date;
        this.delay = delay;
        this.signal = signal;
        this.recordId = recordId;
    }
    @Generated(hash = 260571639)
    public RecordItem() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }
    public Long getRecordId() {
        return this.recordId;
    }
    public float getDelay() {
        return this.delay;
    }
    public void setDelay(float delay) {
        this.delay = delay;
    }
    public float getSignal() {
        return this.signal;
    }
    public void setSignal(float signal) {
        this.signal = signal;
    }
}
