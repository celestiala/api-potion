package com.celestiala.mobile;

import com.celestiala.core.controller.BaseApiController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class CacheTestController extends BaseApiController {


    @Autowired
    private CacheTestService testService;

    @RequestMapping(method = RequestMethod.GET, value = "/test1")
    @ResponseBody
    public String getAll() {
        return "";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/cachedValue")
    @ResponseBody
    public long getCache() {
        return testService.getCachedTime(false);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/invalidate")
    @ResponseBody
    public long getCacheInvalidate() {
        return testService.getCachedTime(true);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/cachedValue2")
    @ResponseBody
    public long getCache2() {
        return testService.getCachedTime2(false);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/invalidate2")
    @ResponseBody
    public long getCacheInvalidate2() {
        return testService.getCachedTime2(true);
    }

}
