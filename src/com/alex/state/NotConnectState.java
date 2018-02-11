package com.alex.state;


import com.alex.FTPRequests;
import com.alex.GUI;
import com.alex.ResponsesUtils;

import javax.swing.*;
import java.io.IOException;

/**
 * Created by Alex on 11.02.2018.
 */
public class NotConnectState implements ConnectionState {
    FTPRequests ftpRequests;
    GUI gui;
    ResponsesUtils responsesUtils;

    public NotConnectState(GUI gui, FTPRequests ftpRequests) {
        this.gui = gui;
        responsesUtils = new ResponsesUtils();
        this.ftpRequests = ftpRequests;
    }

    @Override
    public void autorization(String ip, int port, String username, String password) {
        boolean bool = ftpRequests.getConnectionHandler().createConnection(ip, port);
        if (bool) {
            try {
                gui.addStrintToLog(ftpRequests.sendUSER(username));
                gui.addStrintToLog(ftpRequests.sendPASSWORD(password));
                String s = ftpRequests.sendPASV();
                gui.addStrintToLog(s);
                gui.addStrintToLog(ftpRequests.sendLIST());
                String files = getFiles(s);
                gui.addStrintToLog(files);
                gui.addStrintToLog(ftpRequests.getResponseLine());
                gui.addStrintToLog(ftpRequests.getResponseLine());
                gui.setFilesStr(files);
                String curDirResp = ftpRequests.sendPWD();
                gui.addStrintToLog(curDirResp);
                gui.setCurrendDirectory(responsesUtils.getCurrentDir(curDirResp));
                gui.changeState();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getFiles(String response) throws IOException {
        String ip = responsesUtils.getIpFromResponse(response);
        int port = responsesUtils.getPortFromResponse(response);
        return ftpRequests.getAllFiles(ip, port);
    }

    @Override
    public void goToDir(String string) {
        showMessage();
    }

    @Override
    public void upLoadFile(String pathToFile) {
        showMessage();
    }

    @Override
    public void downLoadFile(String fileName) {
        showMessage();
    }

    @Override
    public void deleteFile(String fileName) {
        showMessage();
    }

    @Override
    public void goUpDir() {
        showMessage();
    }

    @Override
    public void closeConnection() {
        showMessage();
    }

    private void showMessage() {
        JOptionPane.showMessageDialog(null, "Вы не авторизованы");
    }
}
