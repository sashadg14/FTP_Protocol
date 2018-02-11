package com.alex;

import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 * Created by Alex on 06.02.2018.
 */
public class ConnectionHandler {
    private Socket socket = null;
    private BufferedReader reader = null;
    private BufferedWriter writer = null;

    public boolean createConnection(String host, int port) {
        if (socket != null) {
            System.err.println("Соединение уже создано");
            return false;
        } else {
            try {
                socket = new Socket(host, port);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            } catch (IOException e) {
                System.err.println("Ошибка соединения");
                return false;
            }
        }
        return true;
    }

    public boolean checkConnection() {
        if (socket != null)
            return true;
        else return false;
    }

    public void closeConnection() {
        if (checkConnection())
            try {
                reader.close();
                writer.close();
                socket.close();
            } catch (IOException e) {
                System.out.println("Ошибка закрытия");
            } finally {
                reader = null;
                writer = null;
                socket = null;
            }
    }

    public void downloadFile(String ip,int port, String fileName) throws IOException {
        Socket socket=new Socket(ip,port);
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        FileOutputStream fos = new FileOutputStream(fileName);
        byte[] buffer = new byte[4096];
        int read = 0;
        while ((read = dis.read(buffer)) > 0) {
            fos.write(buffer, 0, read);
        }
        fos.close();
        dis.close();
    }

    public String getStrFilesFromServer(String ip,int port) throws IOException {
        Socket socket = null;
        try {
            socket = new Socket(ip, port);
        } catch (IOException e) {
            System.err.println("Ошибка создания потока данных от сервера");
        }
        byte[] buffer = new byte[4096];
        String buf = "";
        BufferedInputStream input = new BufferedInputStream(socket.getInputStream());
        while ((input.read(buffer)) != -1) {
            buf += new String(buffer) + "\n";
        }
        input.close();
        return buf;
    }

    public void sendFile(OutputStream output, InputStream inputStream, String filename) throws IOException {
        BufferedOutputStream outputStream = new BufferedOutputStream(output);
        byte[] buffer = new byte[4096];
        int count = 0;
        while ((count = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, count);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    public void sendLine(String line) throws IOException {
        if (checkConnection())
            try {
                writer.write(line + "\r\n");
                writer.flush();
            } catch (IOException e) {
                socket = null;
                throw e;
            }
    }

    public String readLine() throws IOException {
        String line = "";
        if (checkConnection())
            line = reader.readLine();
        return line;
    }
}