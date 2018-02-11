package com.alex.observers;

import javax.swing.*;

/**
 * Created by Alex on 10.02.2018.
 */
public class TextAreaObserver implements Observer{
    JTextArea textArea;

    public TextAreaObserver(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void sendCommand(String command) {
        textArea.append("<-" + command + "\n");
    }
}
