package cn.gdmcmc.www.demo.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by ray on 2017/12/4.
 */

public class DeviceBean {
    private String message;
    private List<DeviceEntity> deviceEntities;
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DeviceEntity> getDeviceEntities() {
        return deviceEntities;
    }

    public void setDeviceEntities(List<DeviceEntity> deviceEntities) {
        this.deviceEntities = deviceEntities;
    }

    public static class DeviceEntity implements Parcelable {
        public DeviceEntity(){}

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int get__v() {
            return __v;
        }

        public void set__v(int __v) {
            this.__v = __v;
        }

        private String _id;
        private String name;
        private String username;
        private String password;
        private int __v;

        protected DeviceEntity(Parcel in) {
            this._id=in.readString();
            this.name=in.readString();
            this.username=in.readString();
            this.password=in.readString();
            this.__v=in.readInt();
        }


        public static final Creator<DeviceEntity> CREATOR = new Creator<DeviceEntity>() {
            @Override
            public DeviceEntity createFromParcel(Parcel in) {
                return new DeviceEntity(in);
            }

            @Override
            public DeviceEntity[] newArray(int size) {
                return new DeviceEntity[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this._id);
            dest.writeString(this.name);
            dest.writeString(this.username);
            dest.writeString(this.password);
            dest.writeInt(this.__v);
        }
    }
}
