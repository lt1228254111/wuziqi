package com.liutao.gobang.view;

public class Chess {
    private int r;	//行
    private int c;	//列
    public Chess(int r,int c){
        this.r=r;
        this.setC(c);
    }
    public int getR() {
        return r;
    }
    public void setR(int r) {
        this.r = r;
    }
    public int getC() {
        return c;
    }
    public void setC(int c) {
        this.c = c;
    }
}
