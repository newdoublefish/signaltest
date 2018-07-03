package cn.gdmcmc.www.demo.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Record {
    @Id(autoincrement = true)
    private Long id;
    private String name;
    private String ipaddress;
    @Generated(hash = 2032758720)
    public Record(Long id, String name, String ipaddress) {
        this.id = id;
        this.name = name;
        this.ipaddress = ipaddress;
    }
    @Generated(hash = 477726293)
    public Record() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getIpaddress() {
        return this.ipaddress;
    }
    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }
}
