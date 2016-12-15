package com.song.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by song on 2016/12/15.
 */
@Controller
public class IndexController {

    @RequestMapping(value = "")
    public String index() {
        return "index";
    }
}
