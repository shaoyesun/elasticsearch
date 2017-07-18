package com.elasticsearch.controller;

import com.elasticsearch.entity.User;
import com.elasticsearch.service.ElasticsearchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by root on 17-7-11.
 */
@Controller
@RequestMapping(value = "/elastic")
public class ElasticsearchController {

    @Autowired
    private ElasticsearchService elasticsearchService;

    @RequestMapping(value = "/addUser")
    @ResponseBody
    public boolean addUser() {
        File file = new File("/home/bmk/user.txt");
        try {
            FileInputStream in = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(in);
            BufferedReader buffered = new BufferedReader(reader);
            String sLine = "";
            List<User> users = new ArrayList<User>();
            int i = 0;
            while ((sLine = buffered.readLine()) != null) {
                System.out.println(i++);
                String[] u = sLine.split(",");
                User user = new User();
                user.setUuid(u[0]);
                user.setUserName(u[1]);
                user.setRealName(u[2]);
                user.setCompany(u[3]);
                if (u.length > 4) {
                    user.setEmail(u[4]);
                    if (u.length > 5) {
                        user.setPhone(u[5]);
                        if (u.length == 7) {
                            SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            user.setRegisteTime(u[6] == "" ? null : sf.parse(u[6]));
                        }
                    }
                }
                users.add(user);
                if (users.size() % 1000 == 0) {
                    elasticsearchService.insertOrUpdateUser(users);
                    users.clear();
                }
            }
            elasticsearchService.insertOrUpdateUser(users);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @RequestMapping(value = "/query")
    @ResponseBody
    public String queryById() {
        return elasticsearchService.query("北京百迈");
    }

}
