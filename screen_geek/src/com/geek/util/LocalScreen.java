package com.geek.util;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class LocalScreen {

    public static void main(String[] args) {
        // 询问框。
        int showConfirmDialog = JOptionPane.showConfirmDialog(null, "请求控制对方电脑？", "Geek 远程", JOptionPane.YES_NO_CANCEL_OPTION);
        // 判断点击的是什么按钮。~ 否。
        if (showConfirmDialog == JOptionPane.NO_OPTION || JOptionPane.CANCEL_OPTION == showConfirmDialog) {
            return;
        }
        // 输入 ip 地址和端口号。默认值。
        JOptionPane.showInputDialog("请输入您要连接的 ip 和端口号。", "127.0.0.1:10000");
        // 初始化一个窗口。
        JFrame jFrame = new JFrame("Geek 远程桌面。");
        jFrame.setSize(600, 600);
        jFrame.setVisible(true);
        jFrame.setLocationRelativeTo(null);// 居中显示。
        jFrame.setAlwaysOnTop(true);// 置顶。
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 点击关闭按钮退出软件。

        // JLabel 中放图片，JFrame 中放 JLabel。
        JLabel jLabel = new JLabel();
        jFrame.add(jLabel);

        try {
            Robot robot = new Robot();

            // 获取屏幕的大小。
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Dimension screenSize = toolkit.getScreenSize();

//        System.out.println("screenSize = " + screenSize);
            // screenSize = java.awt.Dimension[width=1440,height=960]
//        System.out.println("宽度：" + screenSize.width + "，高度：" + screenSize.height);
//        System.out.println("宽度：" + screenSize.getWidth() + "，高度：" + screenSize.getHeight());
            // 宽度：1440.0高度：960.0

            // 指定分享的区域。
            Rectangle rectangle = new Rectangle(jFrame.getWidth(), 0, (int) screenSize.getWidth() - jFrame.getWidth(), (int) screenSize.getHeight());

            while (true) {
                // 截取图片。
                BufferedImage screenCapture = robot.createScreenCapture(rectangle);

                jLabel.setIcon(new ImageIcon(screenCapture));
            }
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

}
