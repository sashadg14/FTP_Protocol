package com.alex;

import com.alex.ConnectionHandler;
import com.alex.observers.ComandSenderObserver;
import com.alex.observers.Observable;
import com.alex.observers.Observer;

import javax.swing.*;
import java.io.*;
import java.net.Socket;


/**
 * Created by Alex on 06.02.2018.
 */
public class FTPRequests {
    ConnectionHandler connectionHandler;
    Observable observable=new Observable();

    public FTPRequests() {
        this.connectionHandler=new ConnectionHandler();
        observable.addObserver( new ComandSenderObserver(connectionHandler));
    }

    public void addObserverToObservable(Observer observer){
        observable.addObserver(observer);
    }

    public ConnectionHandler getConnectionHandler() {
        return connectionHandler;
    }

    public String sendUSER(String userName) throws IOException {
        String response="";
        observable.sendCommand("USER " + userName);
        response+=connectionHandler.readLine() + "\n";
        response+=connectionHandler.readLine() + "\n";
        return response;
    }

    public String sendPASSWORD(String password) throws IOException {
        String response="";
        observable.sendCommand("PASS " + password);
        response+=connectionHandler.readLine() + "\n";
        return response;
    }

    public String sendPASV() throws IOException {
        observable.sendCommand("PASV");
        String response = connectionHandler.readLine()+"\n";
        return response;
    }

    public String sendLIST() throws IOException {
        observable.sendCommand("LIST");
        String response = "";
        return response;
    }

    public String sendPWD() throws IOException {
        observable.sendCommand("PWD");
        return connectionHandler.readLine()+"\n";
    }

    public String getResponseLine() throws IOException {
        return connectionHandler.readLine()+"\n";
    }
    public String getAllFiles(String ip, int port) throws IOException {
        return connectionHandler.getStrFilesFromServer(ip,port);
    }

    public String sendCWD(String dir) throws IOException {
        observable.sendCommand("CWD "+dir);
        return connectionHandler.readLine();
    }

    public void sendRETR(String filename) throws IOException {
        observable.sendCommand("RETR "+filename);
    }

    public void sendSTOR(String filename) throws IOException {
        observable.sendCommand("STOR "+filename);
    }

    public String sendDELE(String filename) throws IOException {
        observable.sendCommand("DELE "+filename);
        return connectionHandler.readLine();
    }
    public String sendCDUP() throws IOException {
        observable.sendCommand("CDUP");
        return connectionHandler.readLine();
    }
    public String sendTYPE_A() throws IOException {
        observable.sendCommand("TYPE A");
        return connectionHandler.readLine();
    }
    public String sendQUIT() throws IOException {
        observable.sendCommand("QUIT");
        return connectionHandler.readLine()+"\n";
    }

    public void downloadFile(String ip, int port, String fileName) throws IOException {
        connectionHandler.downloadFile(new Socket(ip,port),fileName);
    }

    public void uploadFile(String ip, int port,String path) throws IOException {
        File file=new File(path);
        connectionHandler.sendFile(new Socket(ip,port).getOutputStream(), new FileInputStream(file), file.getName());
    }
}
