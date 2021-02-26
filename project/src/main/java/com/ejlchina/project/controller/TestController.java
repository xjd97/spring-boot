package com.ejlchina.project.controller;

import com.ejlchina.json.JSONKit;
import com.ejlchina.okhttps.HTTP;
import com.ejlchina.project.entity.PlatformBean;
import com.ejlchina.project.entity.PlatformEntity;
import com.ejlchina.project.annotation.RequestTime;
import com.ejlchina.project.entity.IDName;
import com.ejlchina.project.entity.Platform;
import com.ejlchina.project.service.TestService;
import com.ejlchina.project.utils.RedisUtil;
import com.ejlchina.searcher.SearchResult;
import com.ejlchina.searcher.Searcher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class TestController {

    @Resource
    private Searcher searcher;

    @Resource
    private TestService testService;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private PlatformEntity platformEntity;

    @Resource
    private HTTP http;

    @GetMapping("/search")
    @RequestTime
    public SearchResult<PlatformBean> test(HttpServletRequest request) {
        Map<String, Object> map = testService.buildParamMap(request);
        Long id = (Long) request.getAttribute("userId");
        map.put("id", id);
        return searcher.search(PlatformBean.class, map);
    }

    @GetMapping("/test4")
    @RequestTime
    public List<Platform> test4() {
        return platformEntity.findAllByIdIn(Arrays.asList(1L, 17L));
    }

    @GetMapping("/test")
    @RequestTime
    public Map test2(HttpServletRequest request) {
        return testService.buildParamMap(request);
    }

    @GetMapping("/platforms")
    @RequestTime
    public List<Platform> platforms() {
        return testService.getPlatforms();
    }

    @GetMapping("/platform")
    @RequestTime
    public Platform platform(Long id) {
        return testService.getPlatform(id);
    }

    @PostMapping("/test1")
    public Map<String, Object> respMap(@RequestBody IDName obj) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", obj.getId());
        map.put("name", obj.getName());
        return map;
    }

    @GetMapping("/test2")
    public JsonNode resp() throws JsonProcessingException {
        testService.testAsync();
        String result = http.sync("http://localhost:9090/adm-card/index").get().getBody().toString();
        return new ObjectMapper().readTree(result);
    }

    @GetMapping("/test3")
    public Platform testRedis() {
        Platform platform = new Platform();
        platform.setId(1L);
        platform.setName("df");
        platform.setPhone("1366");
        redisUtil.set("test:redis", platform, 3600);
        return (Platform) redisUtil.get("test:redis");
    }


    private static Thread t1;
    private static Thread t2;

    public static void main(String[] args) {
        List<Integer> strList = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
        Map<Integer, Integer> collect = strList.stream()
                .filter(p -> p > 2)
                .collect(Collectors.toMap(strList::indexOf, k -> k));
        List<Integer> list = new ArrayList<>();
        strList.forEach(p -> list.add(p + 1));
        System.out.println(list);
        collect.forEach((key, value) -> System.out.println(key + ":" + value));
        char[] num = "1234567".toCharArray();
        char[] str = "ABCDEFG".toCharArray();
        t1 = new Thread(() -> {
            for (char n : num) {
                System.out.println(n);
                LockSupport.unpark(t2);
                LockSupport.park();
            }
        });

        t2 = new Thread(() -> {
            for (char s : str) {
                LockSupport.park();
                System.out.println(s);
                LockSupport.unpark(t1);
            }
        });
        t1.start();
        t2.start();
        List<Integer> numList = new ArrayList<>();
        numList.add(1);
        numList.remove(0);
        System.out.println(numList.get(0));
        JSONKit.toJson(numList);
    }
}
