package com.zlw.view;

import com.zlw.service.KeywordService;
import com.zlw.utils.Variates;
import com.zlw.utils.ZfaceUtils;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * 主界面
 *
 * @author Ranger
 * @create 2019-08-24 21:42
 */
public class Zface {

    //主窗口
    private JFrame frame;
    //主面板
    private JPanel panel;
    //获取屏幕尺寸
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    //代码文本框
    private JTextArea textIn;
    static Font font = new Font("Consolas", Font.PLAIN, 25);

    public Zface() {
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
        int frameX = screenWidth / 8;
        int frameY = screenHeight / 11;
        int frameWidth = screenWidth * 3 / 4;
        int frameHeight = screenHeight * 4 / 5;
        //设置屏幕初始化位置和宽高
        frame.setBounds(frameX, frameY, frameWidth, frameHeight);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //设置图标
        frame.setIconImage(new ImageIcon("img/logo.png").getImage());
        frame.setResizable(false);

        panel = new JPanel();

        //初始化输入区
        textIn = new JTextArea();
        ZfaceUtils.addRootTextFirst(textIn);
        //设置光标位置
        textIn.setCaretPosition(textIn.getText().length());
        textIn.setFont(font);
        //监听回车事件
        textIn.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (textIn.isEditable() && (char) e.getKeyChar() == KeyEvent.VK_ENTER) {
                    String command = textIn.getText().split("\n")[textIn.getLineCount() - 2].substring(Variates.dirStr.length());
                    System.out.println("command = " + command);

                    //减去回车的字符
                    KeywordService.dealWithKetword(textIn, command);
                }
                //解决退格问题
                if (textIn.isEditable() && (char) e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
                    String[] spt = textIn.getText().toString().split("\n");
                    if(Variates.dirStr.substring(0,Variates.dirStr.length()-1 ).equals(spt[spt.length-1])){
                        textIn.append(" ");
                    }

                }
            }
        });

        // 添加滚动条
        JScrollPane scrollPanel = new JScrollPane(textIn);
        scrollPanel.setBounds(0, 0, frameWidth - 5, frameHeight - 35);
//        scrollPanel2.setBorder(null); // 去除边框
        DefaultCaret caret2 = (DefaultCaret) textIn.getCaret(); // 滚动条保持在最下部
        caret2.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        panel.add(scrollPanel);

        panel.setLayout(null);
        frame.add(panel);
        frame.setVisible(true);
    }
}
