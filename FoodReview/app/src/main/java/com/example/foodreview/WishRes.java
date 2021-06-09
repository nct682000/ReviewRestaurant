package com.example.foodreview;

import java.sql.Blob;

public class WishRes {
    private int index;
    private String name;
    private String address;
    private String province;
    private String des;
    private float rate;
    private byte[] img;

    public WishRes(int index, String name, String address, String province, String des, float rate, byte[] img) {
        this.index = index;
        this.name = name;
        this.address = address;
        this.province = province;
        this.des = des;
        this.rate = rate;
        this.img = img;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }
}
