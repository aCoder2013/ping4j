package com.song.job;

import com.song.util.LogUtils;

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
    private String customParam;
    private String count = "50";
    public static final String DONE_FLAG_STRING = "It's done!";



    public PingJob(LinkedList<String> contents, String ip, String customParam, String count) {
        this.contents = contents;
        this.ip = ip;
        this.customParam = customParam;
        this.count = count;
    }

    @Override
    public void run() {
        String pingCmd = "ping -c " + count + " " + (customParam == null ? "" : customParam) + " " + ip;
        try {
            Runtime r = Runtime.getRuntime();
            Process p = r.exec(pingCmd);
            BufferedReader in = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                contents.add(inputLine);
            }
            in.close();
        } catch (IOException e) {
            //ignore
        } finally {
            contents.add(DONE_FLAG_STRING);
            LogUtils.log("ping " + ip + " job finished");
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
