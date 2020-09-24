package com.wallet.bean;


import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Darma Project on 2019/9/30.
 */
public class Node implements Serializable {
    private int id;


    private String tag;

    private String ip;
    private String post;
    private String url;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
