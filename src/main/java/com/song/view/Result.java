package com.song.view;

/**
 * Created by song on 2016/12/17.
 */
public class Result {
    private boolean success;

    private String data;

    public static Result create(){
        return new Result();
    }

    public boolean isSuccess() {
        return success;
    }

    public Result setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getData() {
        return data;
    }

    public Result setData(String data) {
        this.data = data;
        return this;
    }
}
