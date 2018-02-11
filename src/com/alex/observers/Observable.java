package com.alex.observers;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alex on 10.02.2018.
 */
public class Observable {
    List<Observer> observers=new LinkedList<>();

    public void addObserver(Observer observer){
        observers.add(observer);
    }

    public void sendCommand(String comand){
        for(Observer observer:observers)
            observer.sendCommand(comand);
    }
}
