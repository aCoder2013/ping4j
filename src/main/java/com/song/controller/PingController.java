package com.song.controller;

import com.song.job.PingJob;
import com.song.util.IpUtils;
import com.song.view.Result;

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

    public static final ConcurrentMap<String, LinkedList<String>> contentMap = new ConcurrentHashMap<>();
    public static final ConcurrentMap<String, PingJob> jobMap = new ConcurrentHashMap<>();

    private static final ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @RequestMapping("ping")
    public Result ping(@RequestParam("destIp") String destIp,
                       @RequestParam("custom-param") String customParam,
                       @RequestParam(value = "count", defaultValue = "50") String count) {
        if (StringUtils.isEmpty(destIp)) {
            return Result.create().setData("ip can't be null");
        }
        if (!IpUtils.validIP(destIp)) {
            return Result.create().setData("Ip address is not valid");
        }
        Result result = Result.create();
        result.setSuccess(true);
        if (!jobMap.containsKey(destIp)) {
            LinkedList<String> contents = new LinkedList<>();
            PingJob job = new PingJob(contents, destIp, customParam, count);
            threadPool.submit(job);
            contentMap.put(destIp, contents);
            jobMap.put(destIp, job);
            result.setData("");
        } else {
            LinkedList<String> contents = contentMap.get(destIp);
            if (contents.isEmpty()) {
                result.setData("");
            } else {
                String data = contents.removeFirst();
                if (PingJob.DONE_FLAG_STRING.equals(data)) {
                    result.setSuccess(false);
                    contentMap.remove(destIp);
                    jobMap.remove(destIp);
                } else {
                    result.setData(data);
                }
            }
        }
        return result;
    }


    @RequestMapping(value = "stop")
    public String stop(@RequestParam("destIp") String destIp) {
        if (jobMap.containsKey(destIp)) {
            PingJob job = jobMap.get(destIp);
            job.setStopFlag(true);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("sleep finished");
            contentMap.remove(destIp);
            jobMap.remove(destIp);
            return "success";
        } else {
            return "task doesn't exist";
        }
    }
}
