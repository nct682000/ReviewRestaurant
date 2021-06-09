package com.example.foodreview;

public class Res {
    private int index;
    private String name;
    private byte[] img;
//    private String img;

    public Res(int i, String n, byte[] im){
        index = i;
        name = n;
        img = im;
    }

    public byte[] getImg() {
        return img;
    }

//    public String getImg() {
//        return img;
//    }

    public void setImg(byte[] img) {
        this.img = img;
    }

//    public void setImg(String img) {
//        this.img = img;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
