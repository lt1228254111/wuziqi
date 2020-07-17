package com.liutao.gobang.view;

import com.liutao.gobang.properties.Config;

public class WhoWin {
    private int[][]	exist;
    public WhoWin(int exist[][]){
        this.exist=exist;
    }
    /**
     * 判断输赢的方法
     */
    public int checkWin(){
        if((rowWin()==1)||(columnWin()==1)||(rightSideWin()==1)||(leftSideWin()==1)){
            return 1;
        }else if((rowWin()==-1)||(columnWin()==-1)||(rightSideWin()==-1)||(leftSideWin()==-1)){
            return -1;
        }
        return 0;
    }
    /**
     * 以行的方式赢
     */
    public int rowWin(){
        for(int i=0;i<Config.LINE;i++){
            for(int j=0;j<Config.LINE-4;j++){
                if(exist[i][j]==-1){
                    if(exist[i][j+1]==-1&&exist[i][j+2]==-1&&exist[i][j+3]==-1&&exist[i][j+4]==-1){
                        return -1;
                    }
                }
                if(exist[i][j]==1){
                    if(exist[i][j+1]==1&&exist[i][j+2]==1&&exist[i][j+3]==1&&exist[i][j+4]==1){
                        return 1;
                    }
                }
            }
        }
        return 0;
    }
    /**
     * 以列的方式赢
     */
    public int columnWin(){
        for(int i=0;i<Config.LINE-4;i++){
            for(int j=0;j<Config.LINE;j++){
                if(exist[i][j]==-1){
                    if(exist[i+1][j]==-1&&exist[i+2][j]==-1&&exist[i+3][j]==-1&&exist[i+4][j]==-1){
                        return -1;
                    }
                }
                if(exist[i][j]==1){
                    if(exist[i+1][j]==1&&exist[i+2][j]==1&&exist[i+3][j]==1&&exist[i+4][j]==1){
                        return 1;
                    }
                }
            }
        }
        return 0;
    }
    /**
     * 斜的方式赢
     */
    public int rightSideWin(){  //正斜
        for(int i=0;i<Config.LINE-4;i++){
            for(int j=0;j<Config.LINE-4;j++){
                if(exist[i][j]==-1){
                    if(exist[i+1][j+1]==-1&&exist[i+2][j+2]==-1&&exist[i+3][j+3]==-1&&exist[i+4][j+4]==-1){
                        return -1;
                    }
                }
                if(exist[i][j]==1){
                    if(exist[i+1][j+1]==1&&exist[i+2][j+2]==1&&exist[i+3][j+3]==1&&exist[i+4][j+4]==1){
                        return 1;
                    }
                }
            }
        }
        return 0;
    }
    public int leftSideWin(){   //反斜
        for(int i=4;i<Config.LINE;i++){
            for(int j = 0; j< Config.LINE-4; j++){
                if(exist[i][j]==-1){
                    if(exist[i-1][j+1]==-1&&exist[i-2][j+2]==-1&&exist[i-3][j+3]==-1&&exist[i-4][j+4]==-1){
                        return -1;
                    }
                }
                if(exist[i][j]==1){
                    if(exist[i-1][j+1]==1&&exist[i-2][j+2]==1&&exist[i-3][j+3]==1&&exist[i-4][j+4]==1){
                        return 1;
                    }
                }
            }
        }
        return 0;
    }
}
