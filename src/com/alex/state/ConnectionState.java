package com.alex.state;

/**
 * Created by Alex on 11.02.2018.
 */
public interface ConnectionState {
    void autorization(String ip, int port, String username, String password);
    void goToDir(String string);
    void upLoadFile(String pathToFile);
    void downLoadFile(String fileName);
    void deleteFile(String fileName);
    void goUpDir();
    void closeConnection();
}
