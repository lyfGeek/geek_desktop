package com.geek.screent;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * 远程桌面监控客户端。
 */
public class Client {

    public static void main(String[] args) {
        // 询问框。
        int showConfirmDialog = JOptionPane.showConfirmDialog(null, "请求控制对方电脑？", "Geek 远程", JOptionPane.YES_NO_CANCEL_OPTION);
        // 判断点击的是什么按钮。~ 否。
        if (showConfirmDialog == JOptionPane.NO_OPTION || JOptionPane.CANCEL_OPTION == showConfirmDialog) {
            return;
        }
        // 输入 ip 地址和端口号。默认值。
        String inputDialog = JOptionPane.showInputDialog("请输入您要连接的 ip 和端口号。", "192.168.142.154:10001");
        // 获取服务器的 ip 和端口号。
        String host = inputDialog.substring(0, inputDialog.indexOf(":"));
        String port = inputDialog.substring(inputDialog.indexOf(":") + 1);
        // 连接。
        try {
            Socket client = new Socket(host, Integer.parseInt(port));

            // 输入流。
            DataInputStream dataInputStream = new DataInputStream(client.getInputStream());

            // 创建显示的面板。
            JFrame jFrame = new JFrame();
            jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            jFrame.setTitle("Geek 远程桌面。");
            jFrame.setSize(2160, 1440);

            // 读取服务端的分辨率。
            double height = dataInputStream.readDouble();
            double width = dataInputStream.readDouble();
            Dimension dimension = new Dimension((int) width, (int) height);
            jFrame.setSize(dimension);

            // 面板。
            JLabel jLabel = new JLabel();// 背景。
            JPanel jPanel = new JPanel();

            // 设置滚动条。
            JScrollPane jScrollPane = new JScrollPane(jPanel);
            jPanel.setLayout(new FlowLayout());
            jPanel.add(jLabel);
            jFrame.add(jScrollPane);

            jFrame.setVisible(true);
            jFrame.setLocationRelativeTo(null);// 居中显示。
//            jFrame.setAlwaysOnTop(true);// 置顶。
//            jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 点击关闭按钮退出软件。

            while (true) {
                // 获取流的长度。
                int length = dataInputStream.readInt();
                byte[] imageData = new byte[length];
                dataInputStream.readFully(imageData);

                ImageIcon imageIcon = new ImageIcon(imageData);
                jLabel.setIcon(imageIcon);

                // 重新绘制。否则图片叠加。
                jFrame.repaint();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
