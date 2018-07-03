package cn.gdmcmc.www.demo.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity
public class RecordItem {
    private Long id;
    private String date;
    private float value;
    @ToOne(joinProperty = "id")
    private Record recordId;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1357763500)
    private transient RecordItemDao myDao;
    @Generated(hash = 72282044)
    public RecordItem(Long id, String date, float value) {
        this.id = id;
        this.date = date;
        this.value = value;
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
    @Generated(hash = 1744215759)
    private transient Long recordId__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 490838327)
    public Record getRecordId() {
        Long __key = this.id;
        if (recordId__resolvedKey == null || !recordId__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RecordDao targetDao = daoSession.getRecordDao();
            Record recordIdNew = targetDao.load(__key);
            synchronized (this) {
                recordId = recordIdNew;
                recordId__resolvedKey = __key;
            }
        }
        return recordId;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2086241915)
    public void setRecordId(Record recordId) {
        synchronized (this) {
            this.recordId = recordId;
            id = recordId == null ? null : recordId.getId();
            recordId__resolvedKey = id;
        }
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1597298436)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRecordItemDao() : null;
    }
}
