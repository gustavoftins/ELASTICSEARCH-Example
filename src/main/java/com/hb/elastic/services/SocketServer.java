package com.hb.elastic.services;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

@Service
public class SocketServer {

    public static final int SOCKET_PORT = 5501;

    public void streamFile(String log) throws IOException {

        Socket sock = new Socket("127.0.0.1", SOCKET_PORT);

        byte[] logBytes = log.getBytes();
        InputStream is = IOUtils.toInputStream(log, "UTF-8");
        BufferedInputStream bis = new BufferedInputStream(is);
        bis.read(logBytes, 0, logBytes.length);
        OutputStream os = sock.getOutputStream();
        os.write(logBytes, 0, logBytes.length);
        os.flush();
        os.close();
    }
}
