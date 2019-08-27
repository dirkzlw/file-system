package com.zlw.fsys.view;

import com.zlw.fsys.exception.ReaderWriterException;
import com.zlw.fsys.utils.IOUtils;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Ranger
 * @create 2019-08-25 21:39
 */
public class ViFile {
    private JFrame frame;
    private JPanel panel;
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private JTextArea textVi;
    //鼠标样式
    private Cursor cursor = new Cursor(Cursor.HAND_CURSOR);

    public ViFile(File vif, JTextArea textIn) {
        try {
            // 设置外观
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        frame = new JFrame();
        //系统分辨率
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        //窗口距离，宽高
        int frameX = screenWidth / 5;
        int frameY = screenHeight / 11;
        int frameWidth = screenWidth * 5 / 8;
        int frameHeight = screenHeight * 4 / 5;
        //设置屏幕初始化位置和宽高
        frame.setBounds(frameX, frameY, frameWidth, frameHeight);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //设置图标
        frame.setIconImage(new ImageIcon("img/logo.png").getImage());
        frame.setResizable(false);

        panel = new JPanel();

        //初始化输入区
        textVi = new JTextArea();
        textVi.setFont(Zface.font);
        //初始化文件文本
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(vif));
            String line = "";
            while ((line=br.readLine())!=null){
                textVi.append(line+"\n");
            }
        } catch (Exception e) {
            e=new ReaderWriterException();
            System.out.println(e.getMessage());
        } finally {
            IOUtils.closeIO(br);
        }

        // 添加滚动条
        JScrollPane scrollPanel = new JScrollPane(textVi);
        scrollPanel.setBounds(0, 0, frameWidth - 5, frameHeight - 85);
//        scrollPanel2.setBorder(null); // 去除边框
        DefaultCaret caret2 = (DefaultCaret) textVi.getCaret(); // 滚动条保持在最下部
        caret2.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        panel.add(scrollPanel);

        //退出按钮
        JButton quitBtn = new JButton("退 出");
        quitBtn.setBounds(frameWidth - 350, frameHeight - 78, 80, 35);
        quitBtn.setCursor(cursor);
        quitBtn.setToolTipText("建议先保存再退出！");
        quitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textIn.setEditable(true);
                frame.dispose();
            }
        });
        frame.add(quitBtn);

        //保存按钮
        JButton saveBtn = new JButton("保 存");
        saveBtn.setBounds(frameWidth - 250, frameHeight - 78, 80, 35);
        saveBtn.setCursor(cursor);
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BufferedWriter bw = null;
                try {
                    bw = new BufferedWriter(new FileWriter(vif));
                    bw.write(textVi.getText());
                    bw.flush();
                } catch (Exception e1) {
                    e1.printStackTrace();
                } finally {
                    try {
                        if (bw != null);{
                            bw.close();
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } finally {
                        IOUtils.closeIO(bw);
                    }
                }
            }
        });
        frame.add(saveBtn);

        //保存并退出按钮
        JButton saveAndQuitBtn = new JButton("保存并退出");
        saveAndQuitBtn.setBounds(frameWidth - 150, frameHeight - 78, 120, 35);
        saveAndQuitBtn.setCursor(cursor);
        saveAndQuitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BufferedWriter bw = null;
                try {
                    bw = new BufferedWriter(new FileWriter(vif));
                    bw.write(textVi.getText());
                    bw.flush();
                } catch (Exception e1) {
                    e1.printStackTrace();
                } finally {
                    IOUtils.closeIO(bw);
                }
                System.out.println("bw = " + bw.getClass());
                textIn.setEditable(true);
                frame.dispose();
            }
        });
        frame.add(saveAndQuitBtn);

        panel.setLayout(null);
        frame.add(panel);
        //监听窗口关闭事件
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                textIn.setEditable(true);
            }
        });
        frame.setVisible(true);
    }
}
