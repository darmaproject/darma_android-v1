package com.wallet.bean;

import com.google.gson.Gson;

/**
 * Created by Darma Project on 2019/10/14.
 */
public class Ping {
    private int status;
    private int delay;
    private int height;
    private int topo_height;
    private int score;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
    public int getDelay() {
        return delay;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    public int getHeight() {
        return height;
    }

    public void setTopo_height(int topo_height) {
        this.topo_height = topo_height;
    }
    public int getTopo_height() {
        return topo_height;
    }


    public static Ping from(String json){
        return new Gson().fromJson(json,Ping.class);
    }
}
