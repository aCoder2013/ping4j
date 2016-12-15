package com.song.job;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

/**
 * Created by song on 2016/12/15.
 */
public class PingJob implements Runnable {

    private LinkedList<String> contents = null;

    private volatile boolean stopFlag = false;
    private String ip;


    public PingJob(LinkedList<String> contents, String ip) {
        this.contents = contents;
        this.ip = ip;
    }

    @Override
    public void run() {
        int totalCount = 10000;
        String pingCmd = "ping " + ip;
        try {
            Runtime r = Runtime.getRuntime();
            Process p = r.exec(pingCmd);

            BufferedReader in = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (stopFlag && totalCount-- > 0) {
                    break;
                }
                contents.add(inputLine);
            }
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isStopFlag() {
        return stopFlag;
    }

    public PingJob setStopFlag(boolean stopFlag) {
        this.stopFlag = stopFlag;
        return this;
    }
}
