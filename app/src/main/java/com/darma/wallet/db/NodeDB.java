package com.darma.wallet.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import com.wallet.bean.Node;
import com.wallet.utils.StringUtils;

import java.io.Serializable;

/**
 * Created by Darma Project on 2019/9/30.
 */
@Entity
public class NodeDB implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;


    private String tag;

    private String ip;
    private String post;
    private String url;

    private boolean isDefault=false;

    private String username;
    private String userpwd;

    private String desc;
    private long createTime;

    private long updateTime;


    public NodeDB(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpwd() {
        return userpwd;
    }

    public void setUserpwd(String userpwd) {
        this.userpwd = userpwd;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl(){
        return url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public Node toNode(){
        Node node=new Node();
        node.setId(id);
        node.setIp(ip);
        node.setPost(post);
        node.setTag(tag);
        node.setUrl(getUrl());
        return node;
    }
}
