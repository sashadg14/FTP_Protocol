package com.alex;

import java.util.StringTokenizer;

/**
 * Created by Alex on 11.02.2018.
 */
public class ResponsesUtils {
    public String getCurrentDir(String response) {
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

    public String getIpFromResponse(String response){
        String ip = null;
        int opening = response.indexOf('(');
        int closing = response.indexOf(')', opening + 1);
        if (closing > 0) {
            String dataLink = response.substring(opening + 1, closing);
            StringTokenizer tokenizer = new StringTokenizer(dataLink, ",");
            ip = tokenizer.nextToken() + "." + tokenizer.nextToken() + "."
                    + tokenizer.nextToken() + "." + tokenizer.nextToken();

        }
        return ip;
    }

    public int getPortFromResponse(String response){
        String ip = null;
        int port = 0;
        int opening = response.indexOf('(');
        int closing = response.indexOf(')', opening + 1);
        if (closing > 0) {
            String dataLink = response.substring(opening + 1, closing);
            StringTokenizer tokenizer = new StringTokenizer(dataLink, ",");
           tokenizer.nextToken();
           tokenizer.nextToken();
           tokenizer.nextToken();
           tokenizer.nextToken();
            port = Integer.parseInt(tokenizer.nextToken()) * 256
                    + Integer.parseInt(tokenizer.nextToken());
        }
        return port;
    }
}
