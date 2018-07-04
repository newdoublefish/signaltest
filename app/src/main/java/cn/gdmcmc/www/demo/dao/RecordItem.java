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
    private float value;
    private Long recordId;
    @Generated(hash = 872452715)
    public RecordItem(Long id, String date, float value, Long recordId) {
        this.id = id;
        this.date = date;
        this.value = value;
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
    public float getValue() {
        return this.value;
    }
    public void setValue(float value) {
        this.value = value;
    }
    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }
    public Long getRecordId() {
        return this.recordId;
    }
}
