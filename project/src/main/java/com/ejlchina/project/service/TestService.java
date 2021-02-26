package com.ejlchina.project.service;

import com.ejlchina.project.dao.PlatformDao;
import com.ejlchina.project.entity.Platform;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TestService {

    @Resource
    private PlatformDao platformDao;

    public List<Platform> getPlatforms() {
        return platformDao.getPlatforms();
    }

    public Platform getPlatform(Long id) {
        return platformDao.getPlatform(id);
    }

    public Map<String, Object> buildParamMap(HttpServletRequest request) {
        Map<String, String[]> properties = request.getParameterMap();
        Map<String, Object> newMap = new HashMap<>();
        for (Map.Entry<String, String[]> entry : properties.entrySet()) {
            newMap.put(entry.getKey(), entry.getValue()[0]);
        }
        return newMap;
    }

    @Async("asyncServiceExecutor")
    public void testAsync() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException ignored) {
        }
    }

}
