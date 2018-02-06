package com.alex;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Alex on 06.02.2018.
 */

public class GUI {
    JFrame jFrame = new JFrame("FTP Client");
    JTextField loginField = new JTextField();
    JTextField passwordField = new JTextField();
    JTextField ipField = new JTextField();
    JTextField portField = new JTextField();

    JLabel loginLabel = new JLabel("Login");
    JLabel passwordLabel = new JLabel("Password");
    JLabel ipLabel = new JLabel("IP");
    JLabel portLabel = new JLabel("Port");

    JButton connectButton = new JButton("Connect");
    JButton exitButton = new JButton("Exit");
    JButton goToDirBtn = new JButton("Go to dir: ");
    JButton goToThePreviousDirBtn = new JButton("Up dir");
    JButton upLoad = new JButton("Upload");
    JButton downLoadBtn = new JButton("Download");
    JButton delete = new JButton("Delete");

    JTextField upLoadfield = new JTextField();
    JTextField downLoadfield = new JTextField();
    JTextField goToDirField = new JTextField();
    JTextField deleteFilefield = new JTextField();
    String curDirString;
    int loginW = 120, loginH = 25, loginX = 90, loginY = 30, loginLabelX = 10;

    FTPRequests ftpRequests = new FTPRequests();

    JTextArea displayDir = new JTextArea();
    JLabel curDirArea = new JLabel();

    public GUI() {
        jFrame.setLayout(null);
        createGUIitemsForConnecting();
        createGUIitemsForWorkingWihtFiles();
        addListnersToButtons();
        ftpRequests.setTextArea(createLogTextArea());
        jFrame.setBounds(400, 0, 800, 800);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //    jFrame.update(jFrame.getGraphics());
        //  jFrame.update(jFrame.getGraphics());
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

        // display.setBounds(300,200,400,400);
        scroll.setBounds(280, 120, 400, 400);
        jFrame.add(scroll);
        return display;
    }

    private void addListnersToButtons() {
        connectButton.addActionListener(a -> {
            new Thread(() -> {
                ftpRequests.connect(ipField.getText(), Integer.parseInt(portField.getText()), loginField.getText(), passwordField.getText());
                displayDir.setText(ftpRequests.list());
                curDirString = ftpRequests.pwd();
                curDirArea.setText("current directory: " + curDirString);
            }).start();
        });

        exitButton.addActionListener(a -> {
            ftpRequests.disconnect();
        });

        downLoadBtn.addActionListener(a -> {
            ftpRequests.setAscii();
            ftpRequests.retr(downLoadfield.getText());
        });
        goToDirBtn.addActionListener(a -> {
            ftpRequests.cwd(curDirString + "/" + goToDirField.getText());
            curDirString = ftpRequests.pwd();
            curDirArea.setText("current directory: " + curDirString);
            displayDir.setText(ftpRequests.list());
        });

        goToThePreviousDirBtn.addActionListener(a -> {
            try {
                ftpRequests.cdUp();
                curDirString = ftpRequests.pwd();
                curDirArea.setText("current directory: " + curDirString);
                displayDir.setText(ftpRequests.list());
            } catch (IOException e) {
                System.err.println("Ошибка подключения");
            }
        });

        delete.addActionListener(a -> {
            ftpRequests.delete(deleteFilefield.getText());
        });
        upLoad.addActionListener(a -> {
            ftpRequests.setAscii();
            ftpRequests.stor(new File(upLoadfield.getText()));
        });
    }
}

