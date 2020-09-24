package com.wallet.bean;

import com.google.gson.Gson;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Darma Project on 2019/10/15.
 */
public class WalletBackupInfo implements Serializable {

    private String name;
    private long ctime;
    private long Height;
    private String seeds;
    private String checksum;

    private String path;


    public String getFileName(){
        return new File(path).getName();
    }
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public long getHeight() {
        return Height;
    }

    public void setHeight(long height) {
        Height = height;
    }

    public String getSeeds() {
        return seeds;
    }

    public void setSeeds(String seeds) {
        this.seeds = seeds;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public static WalletBackupInfo from(String json){
        return new Gson().fromJson(json,WalletBackupInfo.class);
    }
}
