package com.ttianjun.common.kit.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by jun on 2015/11/3.
 * socket 发送客户端
 */
public class SocketKit {
    public static final String charset = "UTF-8";
    public static String sendSocket(String msg) throws IOException {

        Socket socket = null;
        OutputStream out = null;
        InputStream in = null;
        try {
            socket = new Socket("localhost", 9019);
            out = socket.getOutputStream();
            in = socket.getInputStream();
            // 请求服务器
            out.write(msg.getBytes(charset));
            out.flush();

         // 获取服务器响应，输出
            byte[] byteArray = new byte[1024];
            int length = 0;
            StringBuffer sb = new StringBuffer();
            while( (length = in.read(byteArray))!=-1){
            	sb.append(new String(byteArray, 0, length, charset));
            	byteArray = new byte[1024];
            }
            return sb.toString();

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            // 关闭连接
            in.close();
            out.close();
            socket.close();
        }
    }
}
