package com.tokyonth.carnet.bean;

public class DataListBean {

    private String time;
    private String data;

    public DataListBean(String time,String data){
        this.time = time;
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
