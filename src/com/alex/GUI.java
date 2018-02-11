package com.alex;

import com.alex.observers.TextAreaObserver;
import com.alex.state.AlredyConnectState;
import com.alex.state.ConnectionState;
import com.alex.state.NotConnectState;

import javax.swing.*;

/**
 * Created by Alex on 06.02.2018.
 */

public class GUI {
    private JFrame jFrame = new JFrame("FTP Client");
    private JTextField loginField = new JTextField();
    private JTextField passwordField = new JTextField();
    private JTextField ipField = new JTextField();
    private JTextField portField = new JTextField();

    private JLabel loginLabel = new JLabel("Login");
    private JLabel passwordLabel = new JLabel("Password");
    private JLabel ipLabel = new JLabel("IP");
    private JLabel portLabel = new JLabel("Port");

    private JButton connectButton = new JButton("Connect");
    private JButton exitButton = new JButton("Exit");
    private JButton goToDirBtn = new JButton("Go to dir: ");
    private JButton goToThePreviousDirBtn = new JButton("Up dir");
    private JButton upLoad = new JButton("Upload");
    private JButton downLoadBtn = new JButton("Download");
    private JButton delete = new JButton("Delete");

    private JTextField upLoadfield = new JTextField();
    private JTextField downLoadfield = new JTextField();
    private JTextField goToDirField = new JTextField();
    private JTextField deleteFilefield = new JTextField();
    private String curDirString;
    private int loginW = 120, loginH = 25, loginX = 90, loginY = 30, loginLabelX = 10;

    FTPRequests ftpRequests;
    private ConnectionState connectionState;
    JTextArea mainTextArea;
    //ComandExecutor comandExecutor=new ComandExecutor();
    JTextArea displayDir = new JTextArea();
    JLabel curDirArea = new JLabel();

    public void addStrintToLog(String s){
        mainTextArea.append("-> "+s);
    }
    public void setFilesStr(String str){
        displayDir.setText(str);
    }

    public void setCurrendDirectory(String dir){
        curDirString=dir;
        curDirArea.setText("current directory: " + curDirString);
    }

    AlredyConnectState alredyConnectState;
    NotConnectState notConnectState;

    public void changeState(){
        if(connectionState instanceof NotConnectState)
            connectionState =alredyConnectState;
        else if (connectionState instanceof AlredyConnectState)
            connectionState =notConnectState;
    }

    public GUI(FTPRequests ftpRequests) {
        alredyConnectState=new AlredyConnectState(this,ftpRequests);
        notConnectState=new NotConnectState(this,ftpRequests);
        this.ftpRequests=ftpRequests;
        jFrame.setLayout(null);
        createGUIitemsForConnecting();
        createGUIitemsForWorkingWihtFiles();
        addListnersToButtons();
        mainTextArea=createLogTextArea();
        jFrame.setBounds(400, 0, 800, 800);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        ftpRequests.addObserverToObservable(new TextAreaObserver(mainTextArea));
        connectionState =new NotConnectState(this,ftpRequests);
    }

    private void createGUIitemsForConnecting() {
        jFrame.add(loginField);
        jFrame.add(passwordField);
        jFrame.add(ipField);
        jFrame.add(portField);

        loginField.setText("culturied");
        passwordField.setText("min4ik97");
        ipField.setText("66.220.9.50");
        portField.setText("21");

        loginField.setBounds(loginX, loginY, loginW, loginH);
        passwordField.setBounds(loginX, loginY + loginH * 2, loginW, loginH);
        ipField.setBounds(loginX, loginY + loginH * 4, loginW, loginH);
        portField.setBounds(loginX, loginY + loginH * 6, loginW, loginH);

        jFrame.add(loginLabel);
        jFrame.add(passwordLabel);
        jFrame.add(ipLabel);
        jFrame.add(portLabel);

        loginLabel.setBounds(loginLabelX, loginY, loginW, loginH);
        passwordLabel.setBounds(loginLabelX, loginY + loginH * 2, loginW, loginH);
        ipLabel.setBounds(loginLabelX, loginY + loginH * 4, loginW, loginH);
        portLabel.setBounds(loginLabelX, loginY + loginH * 6, loginW, loginH);

        jFrame.add(connectButton);
        connectButton.setBounds(loginX + loginW + 40, loginY, loginW, loginH);

        jFrame.add(exitButton);
        exitButton.setBounds(loginX + loginW + 40, loginY + loginH * 2, loginW, loginH);
    }

    private void createGUIitemsForWorkingWihtFiles() {

        displayDir.setEditable(false);
        JScrollPane scroll = new JScrollPane(displayDir);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setBounds(loginLabelX, loginY + loginH * 8, loginW * 2, loginW * 2);
        jFrame.add(scroll);

        curDirArea.setBounds(loginLabelX, loginY + loginH * 9 + scroll.getHeight(), loginW * 2, loginH);
        jFrame.add(curDirArea);


        goToDirBtn.setBounds(loginLabelX, loginY + loginH * 11 + scroll.getHeight(), 100, loginH);
        jFrame.add(goToDirBtn);


        goToDirField.setBounds(loginLabelX + 110, loginY + loginH * 11 + scroll.getHeight(), loginW * 2, loginH);
        jFrame.add(goToDirField);


        goToThePreviousDirBtn.setBounds(loginLabelX + goToDirField.getWidth() + 120, loginY + loginH * 11 + scroll.getHeight(), 100, loginH);
        jFrame.add(goToThePreviousDirBtn);


        upLoad.setBounds(loginLabelX, loginY + loginH * 13 + scroll.getHeight(), 100, loginH);
        jFrame.add(upLoad);


        upLoadfield.setBounds(loginLabelX + 110, loginY + loginH * 13 + scroll.getHeight(), loginW * 3, loginH);
        jFrame.add(upLoadfield);
        upLoadfield.setText("D:\\FTPCLIENT\\src\\com\\company\\EntryPoint.java");


        downLoadBtn.setBounds(loginLabelX, loginY + loginH * 15 + scroll.getHeight(), 100, loginH);
        jFrame.add(downLoadBtn);

        downLoadfield.setBounds(loginLabelX + 110, loginY + loginH * 15 + scroll.getHeight(), loginW * 3, loginH);
        jFrame.add(downLoadfield);
        downLoadfield.setText("EntryPoint.java");


        delete.setBounds(loginLabelX, loginY + loginH * 17 + scroll.getHeight(), 100, loginH);
        jFrame.add(delete);

        deleteFilefield.setBounds(loginLabelX + 110, loginY + loginH * 17 + scroll.getHeight(), loginW * 3, loginH);
        jFrame.add(deleteFilefield);
        deleteFilefield.setText("EntryPoint.java");
    }

    private JTextArea createLogTextArea() {
        JTextArea display = new JTextArea();
        display.setEditable(false);
        JScrollPane scroll = new JScrollPane(display);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setBounds(280, 120, 400, 400);
        jFrame.add(scroll);
        return display;
    }

    private void addListnersToButtons() {
        connectButton.addActionListener(a -> {
            connectionState.autorization(ipField.getText(), Integer.parseInt(portField.getText()),loginField.getText(), passwordField.getText());
        });
        exitButton.addActionListener(a -> {
            connectionState.closeConnection();
        });
        downLoadBtn.addActionListener(a -> {
            connectionState.downLoadFile(downLoadfield.getText());
        });
        goToDirBtn.addActionListener(a -> {
            connectionState.goToDir(curDirString + "/" + goToDirField.getText());
        });
        goToThePreviousDirBtn.addActionListener(a -> {
            connectionState.goUpDir();
        });
        delete.addActionListener(a -> {
            connectionState.deleteFile(deleteFilefield.getText());
        });
        upLoad.addActionListener(a -> {
            connectionState.upLoadFile(upLoadfield.getText());
        });
    }
}

