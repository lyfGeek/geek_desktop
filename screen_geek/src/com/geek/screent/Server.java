package com.geek.screent;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 远程监控服务类。
 */
public class Server {

    public static void main(String[] args) {
        try {
            // 建立服务端的监听。
            ServerSocket serverSocket = new ServerSocket(10001);
            System.out.println("正在连接服务器。。。");
            Socket client = serverSocket.accept();
            System.out.println("服务器连接成功。");
            OutputStream outputStream = client.getOutputStream();

            // 将文件流转换为二进制数据。
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            // 启动线程。
            ScreenThread screenThread = new ScreenThread(dataOutputStream);
            screenThread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


/**
 * 截图。
 */
class ScreenThread extends Thread {

    private DataOutputStream dataOutputStream;

    public ScreenThread(DataOutputStream dataOutputStream) {
        this.dataOutputStream = dataOutputStream;
    }

    // 开始启动线程。
    @Override
    public void run() {
        // 获取屏幕大小。
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        // 获取屏幕的分辨率。发送。
        try {
            dataOutputStream.writeDouble(screenSize.getHeight());
            dataOutputStream.writeDouble(screenSize.getWidth());
            dataOutputStream.flush();

            // 分享区域的大小。
            Rectangle rectangle = new Rectangle(screenSize);

            Robot robot = new Robot();

            while (true) {
                // 截取图片。
                BufferedImage screenCapture = robot.createScreenCapture(rectangle);
                // 压缩图片。
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                JPEGImageEncoder jpegEncoder = JPEGCodec.createJPEGEncoder(byteArrayOutputStream);
                jpegEncoder.encode(screenCapture);

                // 字节流传输的文件流。
                byte[] data = byteArrayOutputStream.toByteArray();
                dataOutputStream.writeInt(data.length);
                dataOutputStream.write(data);
                dataOutputStream.flush();
                Thread.sleep(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AWTException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
