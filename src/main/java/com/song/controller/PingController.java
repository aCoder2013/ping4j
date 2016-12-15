package com.song.controller;

import com.song.job.PingJob;
import com.song.util.IpUtils;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by song on 2016/12/15.
 */
@RestController
public class PingController {

    private static final ConcurrentMap<String, LinkedList<String>> contentMap = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, PingJob> jobMap = new ConcurrentHashMap<>();

    private static final ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @RequestMapping("ping")
    public String ping(@RequestParam("destIp") String destIp) {
        if (StringUtils.isEmpty(destIp)) {
            return "ip can't be null";
        }
        if (!IpUtils.validIP(destIp)) {
            return "Ip address is not valid";
        }
        if (!jobMap.containsKey(destIp)) {
            LinkedList<String> contents = new LinkedList<>();
            PingJob job = new PingJob(contents, destIp);
            threadPool.submit(job);
            contentMap.put(destIp, contents);
            jobMap.put(destIp,job);
        } else {
            LinkedList<String> contents = contentMap.get(destIp);
            if (contents.isEmpty()) {
                return "";
            }
            return contents.removeFirst();
        }
        return "success";
    }


    @RequestMapping(value = "stop")
    public String stop(@RequestParam("destIp") String destIp){
        if(jobMap.containsKey(destIp)){
            PingJob job = jobMap.get(destIp);
            job.setStopFlag(true);
            contentMap.remove(destIp);
            jobMap.remove(destIp);
            return "success";
        }else {
            return "task doesn't exist";
        }
    }
}
