package com.example.foodreview;

public class Comment {
    private int index;
    private String text;
    private byte[] image;
    private String name;

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Comment(int index, String name, String text, byte[] image) {
        this.index = index;
        this.name = name;
        this.text = text;
        this.image = image;
    }

}
