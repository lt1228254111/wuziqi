package com.liutao.gobang.view;


import com.liutao.gobang.properties.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


/**
 * 五子棋窗口
 */
public class Frame<jp> extends JPanel {
        private int[][] exist=new int[Config.LINE][Config.LINE];
        private ChessClick mouse;

        public void showUI(){
                // 窗体对象,JFrame默认是边框布局
                JFrame jf = new JFrame();
                jf.setTitle("五子棋");
                jf.setSize(800, 700);
                // 设置退出进程的方法
                jf.setDefaultCloseOperation(3);
                // 设置居中显示
                jf.setLocationRelativeTo(null);
                //设置窗口大小变化
                jf.setResizable(false);
                //设置窗口可见
                jf.setVisible(true);
                mouse=new ChessClick(this);
                //调用面板方法
                CPanel(jf);
                EPanel(jf);
                mouse.setExist(exist);
        }
        /**
         * 面板布局
         */
        public void CPanel(JFrame frame){
                this.setBackground(Color.yellow);
                frame.add(this);
        }
        public void EPanel(JFrame frame){
                //设置背景颜色
                JPanel jp=new JPanel();
                jp.setBackground(new Color(245,245,245));

                //设置左边布局
                FlowLayout fl=new FlowLayout(5,5,60);
                jp.setLayout(fl);
                Dimension di=new Dimension(120,0);
                jp.setPreferredSize(di);

                String[] buttonArray={"开始游戏","悔棋","认输"}; //数组存储     功能按钮命令
                for(int i=0;i<buttonArray.length;i++){			//使用循环实例化按钮对象
                        JButton button=new JButton(buttonArray[i]); //实例化按钮对象
                        button.setPreferredSize(new Dimension(100,50));	//设置大小
                        jp.add(button);							//在面板上添加按钮
                        button.addActionListener(mouse);				//为每一个按钮添加监听
                }
                frame.add(jp,BorderLayout.EAST);		//为窗体(边框布局)添加面板---放置在东侧
        }
        /**
         * 重写面板
         */
        public void paint(Graphics g){
                super.paint(g);
                drawChessTable(g);
                reDrawChess(g);
        }
        /**
         * 绘制棋盘的方法
         * @param g  传入画笔
         */
        public void drawChessTable(Graphics g){
                for(int r=0;r<Config.LINE;r++){    			//行           x 不变    y变
                        g.drawLine(Config.X, Config.Y+r*Config.SIZE,
                                Config.X+(Config.LINE-1)*Config.SIZE, Config.Y+r*Config.SIZE);
                }
                for(int c=0;c<Config.LINE;c++){			//列            x变         y不变
                        g.drawLine(Config.X+Config.SIZE*c,Config.Y,
                                Config.X+Config.SIZE*c, Config.Y+(Config.LINE-1)*Config.SIZE);
                }
        }
        /**
         * 棋子
         * @param g
         */
        public void reDrawChess(Graphics g){
                Graphics2D g2d=(Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                for(int  r=0;r<Config.LINE;r++){			//行
                        for(int c=0;c<Config.LINE;c++){		//列
                                if(exist[r][c]!=0){			//如果该位置不为空
                                        if(exist[r][c]==1){			//该位置是黑子
                                                g2d.setColor(Color.BLACK);
                                                g2d.fillOval(Config.X+c*Config.SIZE-Config.CHESS_SIZE/2,
                                                        Config.Y+r*Config.SIZE-Config.CHESS_SIZE/2 , Config.CHESS_SIZE, Config.CHESS_SIZE);
                                        }else if(exist[r][c]==-1){  //该位置是白子
                                                g2d.setColor(Color.WHITE);
                                                g2d.fillOval(Config.X+c*Config.SIZE-Config.CHESS_SIZE/2,
                                                        Config.Y+r*Config.SIZE-Config.CHESS_SIZE/2 , Config.CHESS_SIZE, Config.CHESS_SIZE);
                                        }
                                }
                        }
                }
        }
        public static void main(String[] args) {
                Frame chess=new Frame();
                chess.showUI();
        }


}

