/*
 * Copyright © 2020-2020 尛飛俠（Denvie） All rights reserved.
 */

package cn.denvie.security.api.demo.controller;

import cn.denvie.security.api.annotation.SecurityApi;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Index controller.
 *
 * @author denvie
 * @since 2020/8/23
 */
@RequestMapping
@RestController
public class IndexController {
    @SecurityApi
    @GetMapping("/get")
    public String get(String name) {
        return "get name: " + name;
    }
    @SecurityApi
    @GetMapping("/getPath/{name}")
    public String getPath(@PathVariable String name) {
        return "getPath name: " + name;
    }

    @SecurityApi
    @GetMapping("/getMap")
    public String getMap(@RequestParam Map<String, Object> params) {
        return "getMap params: " + params;
    }

    @SecurityApi
    @PostMapping("/post")
    public String post(String name) {
        return "post name: " + name;
    }

    @SecurityApi
    @PostMapping("/postMap")
    public String postMap(@RequestParam Map<String, Object> params) {
        return "postMap params: " + params;
    }

    @SecurityApi
    @PostMapping("/postBody")
    public String postBody(@RequestBody Map<String, Object> params) {
        return "postBody params: " + params;
    }
}
