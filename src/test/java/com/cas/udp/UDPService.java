package com.cas.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * @author xiang_long
 * @version 1.0
 * @date 2022/4/4 1:27 下午
 * @desc 简单的UDP输出输入服务端
 */
public class UDPService {

    /**
     * Sever端程序
     *
     * 一，调用DatagramSocket(int port)创建一个数据报套接字，绑定在指定端口上；
     * 二，调用DatagramPacket(byte[] buf,int length),建立一个字节数组来接收UDP包；
     * 三，调用DatagramSocket.receive()；
     * 四，最后关闭数据报套接字。
     * @param args
     * @throws SocketException
     */
    public static void main(String[] args) throws IOException {

        // 1.构建方法，创建数据报套接字并将其绑定到本地主机上的指定端口
        DatagramSocket socket = new DatagramSocket(8800);

        // 2.创建数据报，用于接收客户端发送的数据
        byte[] data = new byte[1024]; // 创建字节数组，指定接收的数据包的大小
        DatagramPacket packet = new DatagramPacket(data, data.length);

        // 3.接收客户端发送的数据
        System.out.println("****服务器端已经启动，等待客户端发送数据");
        socket.receive(packet);

        // 4.读取数据
        String info = new String(data, 0, packet.getLength());
        System.out.println("我是服务器，客户端说：" + info);

        /**
         * 向客户端响应数据
         */
        // 1.定义客户端的地址，端口号，数据
        InetAddress address = packet.getAddress();
        int port = packet.getPort();
        byte[] data2 = "欢迎你！".getBytes();

        // 2.创建数据报，包含响应的数据信息
        DatagramPacket packet1 = new DatagramPacket(data2, data2.length, address, port);

        // 3.响应客户端
        socket.send(packet1);

        // 4.关闭资源
        socket.close();

    }

}
