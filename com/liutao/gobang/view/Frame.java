package com.liutao.gobang.view;


import com.liutao.gobang.properties.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


/**
 * 五子棋窗口
 */
public class Frame extends JPanel {
        private int[][] exist=new int[Config.ROWS][Config.COLUMNS];
        private ActionListener mouse;

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
                //调用面板方法
                CPanel(jf);
                EPanel(jf);
        }
        /**
         * 面板布局
         */
        public void CPanel(JFrame frame){
                this.setBackground(Color.white);
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

                //设置按钮
                JButton jbu1=new javax.swing.JButton("开始游戏");
                Dimension di1=new Dimension(100,60);
                jbu1.setPreferredSize(di1);
                jp.add(jbu1);

                JButton jbu2=new JButton("人机对战");
                Dimension di2=new Dimension(100,60);
                jbu2.setPreferredSize(di2);
                jp.add(jbu2);

                JButton jbu3=new JButton("悔棋");
                Dimension di3=new Dimension(100,60);
                jbu3.setPreferredSize(di3);
                jp.add(jbu3);

                JButton jbu4=new JButton("重新开始");
                Dimension di4=new Dimension(100,60);
                jbu4.setPreferredSize(di4);
                jp.add(jbu4);

                jbu1.addActionListener(mouse);
                jbu2.addActionListener(mouse);
                jbu3.addActionListener(mouse);
                jbu4.addActionListener(mouse);

                frame.add(jp,BorderLayout.EAST);
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
                for(int r=0;r<Config.ROWS;r++){    			//行           x 不变    y变
                        g.drawLine(Config.X, Config.Y+r*Config.SIZE,
                                Config.X+(Config.COLUMNS-1)*Config.SIZE, Config.Y+r*Config.SIZE);
                }
                for(int c=0;c<Config.COLUMNS;c++){			//列            x变         y不变
                        g.drawLine(Config.X+Config.SIZE*c,Config.Y,
                                Config.X+Config.SIZE*c, Config.Y+(Config.ROWS-1)*Config.SIZE);
                }
        }
        /**
         * 棋子
         * @param g
         */
        public void reDrawChess(Graphics g){
                Graphics2D g2d=(Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                for(int  r=0;r<Config.ROWS;r++){			//行
                        for(int c=0;c<Config.COLUMNS;c++){		//列
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

