package com.cas.tcp;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * @author xiang_long
 * @version 1.0
 * @date 2022/4/4 1:03 下午
 * @desc socket最简单的输入输出客户端
 */
public class HttpClient {
    private static final Integer port = 80; // HTTP默认端口

    public static void main(String[] args) throws IOException {

        Socket client = new Socket("localhost", port);
        // 从键盘写入
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        DataOutputStream dos = new DataOutputStream(client.getOutputStream());
        DataInputStream dis = new DataInputStream(client.getInputStream());
        while(true) {
            String re = br.readLine();
            // 输出流
            dos.writeUTF(re);
            dos.flush();

            // 输入流
            String a = dis.readUTF();
            System.out.println(a);
        }

    }

}
