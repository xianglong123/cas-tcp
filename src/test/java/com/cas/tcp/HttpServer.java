package com.cas.tcp;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author xiang_long
 * @version 1.0
 * @date 2022/4/3 8:05 下午
 * @desc socket最简单的输入输出服务端
 */
public class HttpServer {
    private static final Integer port = 80; // HTTP默认端口

    public static void main(String[] args) {
        ServerSocket serverSocket;

        try {
            // 建立服务器socket，监听客户端请求
            serverSocket = new ServerSocket(port);
            Socket client = serverSocket.accept();
            while (true) {
                // 输入流
                DataInputStream dis = new DataInputStream(client.getInputStream());
                String a = dis.readUTF();
                // 输出流
                DataOutputStream dos = new DataOutputStream(client.getOutputStream());
                dos.writeUTF("服务器》: " + a);
                dos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
