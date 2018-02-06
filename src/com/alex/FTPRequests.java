package com.alex;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Alex on 06.02.2018.
 */
public class FTPRequests {
    ConnectionHandler connectionHandler = new ConnectionHandler();
    JTextArea textArea;

    public void setTextArea(JTextArea textArea) {
        this.textArea = textArea;
    }

    public synchronized void connect(String host, int port, String userName, String password) {
        if (connectionHandler.createConnection(host, port)) {
            try {
                connectionHandler.sendLine("USER " + userName);
                textArea.append("<- USER " + userName + "\n");
                textArea.append("-> " + connectionHandler.readLine() + "\n");
                textArea.append("->" + connectionHandler.readLine() + "\n");
                connectionHandler.sendLine("PASS " + password);
                textArea.append("<- PASS " + password + "\n");
                textArea.append("->" + connectionHandler.readLine() + "\n");
            } catch (IOException e) {
                System.err.println("Ошибка при авторизации");
            }
        } else System.err.println("Ошибка создания нового соединения");
    }

    public synchronized String list() {
        String filesStr = "";
        if (connectionHandler.checkConnection())
            try {
                connectionHandler.sendLine("PASV");
                textArea.append("<- PASV\n");
                String response = connectionHandler.readLine();
                textArea.append("->" + response + "\n");
                connectionHandler.sendLine("LIST");
                textArea.append("<- LIST\n");
                filesStr = getAllFiles(connectionHandler.createDataConnectionFromPASV(response));
                textArea.append(filesStr);
                textArea.append("->" + connectionHandler.readLine() + "\n");
                textArea.append("->" + connectionHandler.readLine() + "\n");
            } catch (IOException e) {
                System.out.println("Ошибка получения списка файлов");
            }
        return filesStr;
    }

    private String getAllFiles(Socket socket) throws IOException {
        byte[] buffer = new byte[4096];
        String buf = "";
        BufferedInputStream input = new BufferedInputStream(socket.getInputStream());
        while ((input.read(buffer)) != -1) {
            buf += new String(buffer) + "\n";
        }
        input.close();
        return buf;
    }

    public synchronized String pwd() {
        String dir = "";
        try {
            connectionHandler.sendLine("PWD");
            textArea.append("<- PWD\n");
            String response = connectionHandler.readLine();
            textArea.append("->" + response + "\n");
            dir = extractDirFromResponse(response);
        } catch (IOException e) {
            System.out.println("Ошибка получения текущей директории");
        }
        return dir;
    }

    private String extractDirFromResponse(String response) {
        String dir = "";
        if (response.startsWith("257 ")) {
            int firstQ = response.indexOf('\"');
            int secondQ = response.indexOf('\"', firstQ + 1);
            if (secondQ > 0) {
                dir = response.substring(firstQ + 1, secondQ);
            }
        }
        return dir;
    }

    public void disconnect() {
        if (connectionHandler.checkConnection())
            try {
                connectionHandler.sendLine("QUIT");
                textArea.append("<- QUIT\n");
            } catch (IOException e) {
                System.err.println("Ошибка при отключении");
            } finally {
                connectionHandler.closeConnection();
            }
    }

    public void setAscii() {
        if (connectionHandler.checkConnection())
            try {
                connectionHandler.sendLine("TYPE A");
                textArea.append("<- TYPE A\n");
                textArea.append("-> " + connectionHandler.readLine());
            } catch (IOException e) {
                System.out.println("Ошибка установки типа файла");
            }
    }

    public void retr(String filename) {
        if (connectionHandler.checkConnection())
            try {
                connectionHandler.sendLine("PASV");
                textArea.append("<- PASV\n");
                String response = connectionHandler.readLine();
                textArea.append("->" + response + "\n");
                connectionHandler.sendLine("RETR " + filename);
                textArea.append("<- RETR " + filename + "\n");
                connectionHandler.downloadFile(connectionHandler.createDataConnectionFromPASV(response), filename);
                textArea.append("-> " + connectionHandler.readLine() + "\n");
                textArea.append("-> " + connectionHandler.readLine() + "\n");
            } catch (IOException e) {
                System.out.println("Ошибка при получении файлa");
            }
    }

    public void cdUp() throws IOException {
        if (connectionHandler.checkConnection())
            try {
                connectionHandler.sendLine("CDUP");
                textArea.append("<- CDUP\n");
                textArea.append("-> " + connectionHandler.readLine() + "\n");
            } catch (IOException e) {
                System.out.println("Ошибка перехода на директорию выше");
            }
    }

    public void cwd(String dir) {
        if (connectionHandler.checkConnection())
            try {
                connectionHandler.sendLine("CWD " + dir);
                textArea.append("<- CWD " + dir + "\n");
                textArea.append("-> " + connectionHandler.readLine() + "\n");
            } catch (IOException e) {
                System.out.println("Ошибка при переходе в директорию " + dir);
            }
    }

    public void delete(String filename) {
        if (connectionHandler.checkConnection())
            try {
                connectionHandler.sendLine("DELE " + filename);
                textArea.append("<- DELE " + filename + "\n");
                textArea.append("-> " + connectionHandler.readLine() + "\n");
            } catch (IOException e) {
                System.out.println("Ошибка при удалении файла " + filename);
            }
    }

    public synchronized void stor(File file) {
        String filename = file.getName();
        if (connectionHandler.checkConnection())
            try {
                connectionHandler.sendLine("PASV");
                textArea.append("<- PASV\n");
                String response = connectionHandler.readLine();
                textArea.append("->" + response + "\n");
                connectionHandler.sendLine("STOR " + filename);
                textArea.append("<- STOR " + filename + "\n");
                Socket socket = connectionHandler.createDataConnectionFromPASV(response);
                connectionHandler.sendFile(socket.getOutputStream(), new FileInputStream(file), filename);
                textArea.append("-> " + connectionHandler.readLine() + "\n");
                textArea.append("-> " + connectionHandler.readLine() + "\n");
            } catch (IOException e) {
                System.out.println("Ошибка при отправке файлa");
            }
    }
}
