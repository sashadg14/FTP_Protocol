package com.alex.state;

import com.alex.FTPRequests;
import com.alex.GUI;
import com.alex.ResponsesUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Alex on 11.02.2018.
 */
public class AlredyConnectState implements ConnectionState {
    GUI gui;
    FTPRequests ftpRequests;
    ResponsesUtils responsesUtils;

    public AlredyConnectState(GUI gui, FTPRequests ftpRequests) {
        this.gui = gui;
        responsesUtils = new ResponsesUtils();
        this.ftpRequests = ftpRequests;
    }

    @Override
    public void autorization(String ip, int port, String username, String password) {
        JOptionPane.showMessageDialog(null, "Вы уже подключены");
    }

    @Override
    public void goToDir(String dir) {
        try {
            gui.addStrintToLog(ftpRequests.sendCWD(dir));
            updateDir();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void updateDir() throws IOException {
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
    }
    @Override
    public void upLoadFile(String pathToFile) {
        try {
            gui.addStrintToLog(ftpRequests.sendTYPE_A());

            String response=ftpRequests.sendPASV();
            gui.addStrintToLog(response);
            ftpRequests.sendSTOR(new File(pathToFile).getName());

            String ip=responsesUtils.getIpFromResponse(response);
            int port=responsesUtils.getPortFromResponse(response);

            ftpRequests.uploadFile(ip,port,pathToFile);
            gui.addStrintToLog(ftpRequests.getResponseLine());
            gui.addStrintToLog(ftpRequests.getResponseLine());

            updateDir();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Ошибка при загрузке файла");
        }
    }

    @Override
    public void downLoadFile(String fileName) {
            try {
                gui.addStrintToLog(ftpRequests.sendTYPE_A());

                String response=ftpRequests.sendPASV();
                gui.addStrintToLog(response);
                ftpRequests.sendRETR(fileName);

                String ip=responsesUtils.getIpFromResponse(response);
                int port=responsesUtils.getPortFromResponse(response);

                ftpRequests.downloadFile(ip,port,fileName);
                gui.addStrintToLog(ftpRequests.getResponseLine());
                gui.addStrintToLog(ftpRequests.getResponseLine());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Ошибка при загрузке файла");
            }
    }

    @Override
    public void deleteFile(String fileName) {
        try {
            gui.addStrintToLog(ftpRequests.sendDELE(fileName));
            updateDir();
        }catch (IOException e){
            JOptionPane.showMessageDialog(null, "Ошибка при удалении файла");
        }
    }

    @Override
    public void goUpDir() {
        try {
            ftpRequests.sendCDUP();
            updateDir();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeConnection() {
        try {
            ftpRequests.sendQUIT();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ftpRequests.getConnectionHandler().closeConnection();
            gui.changeState();
        }
    }

    private String getFiles(String response) throws IOException {
        String ip = responsesUtils.getIpFromResponse(response);
        int port = responsesUtils.getPortFromResponse(response);
        return ftpRequests.getAllFiles(ip, port);
    }


}
