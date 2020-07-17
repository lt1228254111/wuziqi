package com.liutao.gobang.view;

import com.liutao.gobang.properties.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

import static com.liutao.gobang.properties.Config.LINE;

public class ChessClick extends MouseAdapter implements ActionListener {
        private int x,y;    //记录点击坐标
        private Graphics g;   //存储画笔
        private int count=1;
        private int[][] exist;   //生命棋子数组
        private Graphics2D g2d;   //画笔对象
        private HashMap<String, Integer> map=new HashMap<String,Integer>();//创建集合对象，用途是存储每一种棋子相连对应的权值
        private Frame frame; //生命面板类型的变量
        private WhoWin win;			//生命判断输赢类的对象
        private int flag=0;			//记录下棋的步数
        private ArrayList<Chess> list=new ArrayList<Chess>();    //数组队列   存储的Chess类型的对象
        /**
         * 构造方法
         * @param  frame
         */
	public ChessClick(Frame frame) {
            this.frame=frame;
        }
        /**
         * 设置方法，接收数组
         * @param exist 存储棋盘上棋子的位置
         */
        public void setExist(int[][] exist){
            this.exist=exist;
            win=new WhoWin(exist);
        }
        /**
         * 点击事件处理方法
         */
        public void actionPerformed(ActionEvent e){
            if(e.getSource() instanceof JButton){		//点击按钮
                if(e.getActionCommand().equals("开始游戏")){
                    MouseListener[] mls=frame.getMouseListeners();
                    if(mls.length>0){		//如果还有其他监听  ---移除
                        frame.removeMouseListener(this);
                    }
                    reset();				//调用棋盘回到初始状态的方法
                    frame.addMouseListener(this);		// 为棋盘添加鼠标监听
                }else if(e.getActionCommand().equals("悔棋")){    //悔棋的算法
                        if(list.size()>2){				//至少下了两步
                            Chess chess=list.get(list.size()-2);		//获取倒数第二步的棋子对象
                            int r=chess.getR();
                            int c=chess.getC();
                            exist[r][c]=0;								//令其为空
                            Chess chessAI=list.get(list.size()-1);		//获取最后一步棋子对象
                            r=chessAI.getR();
                            c=chessAI.getC();
                            exist[r][c]=0;								//令其为空
                            list.remove(list.size()-1);					//删除数组队列中后两个对象
                            list.remove(list.size()-1);
                            frame.repaint();						//调用棋子面板的重绘方法

                    }

                }else if(e.getActionCommand().equals("认输")){			//认输的算法
                    if(flag<10){
                        JOptionPane.showMessageDialog(frame, "总步数小于10，不能认输");
                    }else{
                        JOptionPane.showMessageDialog(frame, "白色棋子AI获得胜利");
                        frame.removeMouseListener(this);			//认输后不能在棋盘上上下棋了   所以要移除棋盘上的鼠标监听
                    }
                }
            }
        }
        public void mouseClicked(MouseEvent e){			//鼠标点击事件的处理方法
            x=e.getX();		//获取点击位置的x坐标
            y=e.getY();		//获取点击位置的y坐标
            pvai(x,y);      //调用人机对战的方法

        }
        /**
         * 设置棋盘回到初始状态的方法
         */
        public void reset(){
            count=1;		//默认黑色棋子先下棋
            flag=0;			//下棋步数重置为0
            for(int r=0;r<exist.length;r++){				//遍历二维数组，将所有位置清空
                for(int c=0;c<exist[r].length;c++){
                    exist[r][c]=0;
                }
            }
            frame.repaint();   //调用棋盘重绘的方法
        }
        /**
         * 人机对战的方法
         * @param x  人所下棋子的横坐标
         * @param y	  人所下棋子的纵坐标
         */
        public void pvai(int x,int y){
            flag++;		//步数＋1
            g=frame.getGraphics();
            g2d=(Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            for(int r = 0; r< LINE; r++){
                for(int c = 0; c< LINE; c++){
                    if(exist[r][c]==0){				//判断该位置上是否有棋子
                        if((Math.abs(x-Config.X-c*Config.SIZE)<Config.SIZE/3.0)
                                &&(Math.abs(y-Config.Y-r*Config.SIZE)<Config.SIZE/3.0)){//将棋子放到交叉点上，误差为1/3
                            g2d.setColor(Color.BLACK);
                            exist[r][c]=1;		//记录下了黑色棋子的位置
                            g2d.fillOval(Config.X+c*Config.SIZE-Config.CHESS_SIZE/2,
                                    Config.Y+r*Config.SIZE-Config.CHESS_SIZE/2 ,
                                    Config.CHESS_SIZE, Config.CHESS_SIZE);
                            list.add(new Chess(r,c));	//队列中添加棋子数组对象  存   r   c
                            if(win.checkWin()==1){		//判断黑色棋子是否胜利
                                JOptionPane.showMessageDialog(frame, "黑色棋子获得胜利");
                                frame.removeMouseListener(this);
                                return;
                            }
                            ai(g2d);		//调用ai下棋的方法
                            if(win.checkWin()==-1){		//ai下的是白色棋子，每下完一步，都要判断一次是否获胜
                                JOptionPane.showMessageDialog(frame, "白色棋子获得胜利");
                                frame.removeMouseListener(this);
                                return;
                            }
                        }
                    }
                }
            }
        }
        /**
         * ai 下棋的方法
         * @param g2d
         */
        public void ai(Graphics2D g2d){
            g2d.setColor(Color.WHITE);		//设置ai所下棋子的颜色为白色
            int[][] weightArray=new int[LINE][LINE];//创建一个存储权值的二维数组
            /**
             * 设置每种棋子相连情况下的权值
             */
            map.put("010", 1);
            map.put("0110", 20);
            map.put("01110", 50);
            map.put("011110", 500);
            map.put("0-10", 10);
            map.put("0-1-10", 30);
            map.put("0-1-1-10", 70);
            map.put("0-1-1-1-10", 500);
            map.put("-110", 1);
            map.put("-1110", 10);
            map.put("-11110", 30);
            map.put("-111110", 500);
            map.put("1-10", 1);
            map.put("1-1-10", 10);
            map.put("1-1-1-10", 30);
            map.put("1-1-1-1-10", 500);
            for(int r=0;r<exist.length;r++){	//求出权值  将权值存到数组中
                for(int c=0;c<exist[r].length;c++){
                    if(exist[r][c]==0){			//判断是否是空位
                        String code=countHL(r,c);
                        Integer weight = map.get(code);//获取棋子相连情况对应的权值
                        if(null != weight){//判断权值不为null
                            weightArray[r][c] += weight;//累加权值
                        }
                        code=countVU(r,c);
                        weight = map.get(code);//获取棋子相连情况对应的权值
                        if(null != weight){//判断权值不为null
                            weightArray[r][c] += weight;//累加权值
                        }
                        code=countLLU(r,c);
                        weight = map.get(code);//获取棋子相连情况对应的权值
                        if(null != weight){//判断权值不为null
                            weightArray[r][c] += weight;//累加权值
                        }
                        code=countLRU(r,c);
                        weight = map.get(code);//获取棋子相连情况对应的权值
                        if(null != weight){//判断权值不为null
                            weightArray[r][c] += weight;//累加权值
                        }
                    }
                }
            }
            int max=weightArray[0][0];                    //找出最大的权值
            for(int r=0;r<weightArray.length;r++){
                for(int c=0;c<weightArray[r].length;c++){
                    if(weightArray[r][c]>max){
                        max=weightArray[r][c];
                    }
                }
            }
            for(int r=0;r<weightArray.length;r++){				//随机取最大权值处所在的点
                for(int c=0;c<weightArray[r].length;c++){
                    if(weightArray[r][c]==max&&exist[r][c]==0){	//权值最大且是空位
                        exist[r][c]=-1;
                        g2d.fillOval(Config.X+c*Config.SIZE-Config.CHESS_SIZE/2,
                                Config.Y+r*Config.SIZE-Config.CHESS_SIZE/2 ,
                                Config.CHESS_SIZE, Config.CHESS_SIZE);
                        list.add(new Chess(r,c));
                        return;
                    }
                }
            }
        }
        /**
         * 水平向左方向统计棋子相连的情况
         * @param r   行
         * @param c	    列
         * @return
         */
        private String countHL(int r,int c){
            String code = "0";	//默认记录r,c位置的情况
            int chess = 0;		//记录第一次出现的棋子
            for(int c1=c-1;c1>=0;c1--){		//向左统计
                if(exist[r][c1]==0){		//表示没有棋子的位置
                    if(c1+1==c){			//相邻的空位
                        break;
                    }else{					//空位不相连
                        code = exist[r][c1] + code;//记录棋子相连的情况
                        break;
                    }
                }else{				//表示有棋子
                    if(chess==0){	//判断是否是第一次出现棋子
                        code = exist[r][c1] + code;	//记录棋子相连的情况
                        chess = exist[r][c1];		//记录第一次的棋子的颜色
                    }else if(chess==exist[r][c1]){	//表示之后的棋子和第一次的棋子颜色一致
                        code = exist[r][c1] + code;	//记录棋子相连的情况
                    }else{							//表示之后的棋子和第一次的棋子颜色不同
                        code = exist[r][c1] + code;	//记录棋子相连的情况
                        break;
                    }
                }
            }
            return code;
        }

        /**
         * 垂直向上统计棋子相连的情况
         * @param r	行
         * @param c	列
         * @return
         */
        private String countVU(int r,int c){
            String code = "0";					//默认记录r,c位置的情况
            int chess = 0;						//记录第一次出现的棋子
            for(int r1=r-1;r1>=0;r1--){			//向上统计
                if(exist[r1][c]==0){			//表示没有棋子
                    if(r1+1==r){				//相邻的空位
                        break;
                    }else{						//不相邻的空位
                        code = exist[r1][c] + code;	//记录棋子相连的情况
                        break;
                    }
                }else{				//表示有棋子
                    if(chess==0){	//判断是否是第一次出现棋子
                        code = exist[r1][c] + code;		//记录棋子相连的情况
                        chess = exist[r1][c];			//记录第一次的棋子的颜色
                    }else if(chess==exist[r1][c]){		//表示之后的棋子和第一次的棋子颜色一致
                        code = exist[r1][c] + code;		//记录棋子相连的情况
                    }else{		//表示之后的棋子和第一次的棋子颜色不同
                        code = exist[r1][c] + code;		//记录棋子相连的情况
                        break;
                    }
                }
            }
            return code;
        }
        /**
         * 正斜(\)   棋子相连统计
         * @param r
         * @param c
         * @return
         */
        private String countLLU(int r,int c){
            String code = "0";		//默认记录r,c位置的情况
            int chess = 0;			//记录第一次出现的棋子
            for(int r1=r-1,c1=c-1;r1>=0&&c1>0;r1--,c1--){//向上统计
                if(exist[r1][c1]==0){	//表示没有棋子
                    if(r1+1==r&&c1+1==c){	//相邻的空位
                        break;
                    }else{					//不相邻的空位
                        code = exist[r1][c1] + code;	//记录棋子相连的情况
                        break;
                    }
                }else{		//表示有棋子
                    if(chess==0){	//判断是否是第一次出现棋子
                        code = exist[r1][c1] + code;	//记录棋子相连的情况
                        chess = exist[r1][c1];		//记录第一次的棋子的颜色
                    }else if(chess==exist[r1][c1]){		//表示之后的棋子和第一次的棋子颜色一致
                        code = exist[r1][c1] + code;	//记录棋子相连的情况
                    }else{					//表示之后的棋子和第一次的棋子颜色不同
                        code = exist[r1][c1] + code;	//记录棋子相连的情况
                        break;
                    }
                }
            }
            return code;
        }
        /**
         * 反斜(/)   棋子相连的统计
         * @param r
         * @param c
         * @return
         */
        private String countLRU(int r,int c){
            String code = "0";		//默认记录r,c位置的情况
            int chess = 0;			//记录第一次出现的棋子
            for(int r1 = r-1, c1 = c+1; r1>=0&&c1< LINE; r1--,c1++){
                if(exist[r1][c1]==0){	//表示没有棋子
                    if(r1+1==r&&c1-1==c){	//相邻的空位
                        break;
                    }else{
                        code = exist[r1][c1] + code;	//记录棋子相连的情况
                        break;
                    }
                }else{		//表示有棋子
                    if(chess==0){//判断是否是第一次出现棋子
                        code = exist[r1][c1] + code;		//记录棋子相连的情况
                        chess = exist[r1][c1];			//记录第一次的棋子的颜色
                    }else if(chess==exist[r1][c1]){		//表示之后的棋子和第一次的棋子颜色一致
                        code = exist[r1][c1] + code;	//记录棋子相连的情况
                    }else{					//表示之后的棋子和第一次的棋子颜色不同
                        code = exist[r1][c1] + code;	//记录棋子相连的情况
                        break;
                    }
                }
            }
            return code;
        }
}
