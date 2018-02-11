package com.alex.observers;


import com.alex.ConnectionHandler;

import java.io.IOException;

/**
 * Created by Alex on 10.02.2018.
 */
public class ComandSenderObserver implements Observer{
    ConnectionHandler connectionHandler;

    public ComandSenderObserver(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }

    @Override
    public void sendCommand(String command) {
        try {
            connectionHandler.sendLine(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
