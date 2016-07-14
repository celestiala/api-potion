package com.tmoncorp.mobile;

import com.tmoncorp.core.controller.BaseApiController;
import com.tmoncorp.apipotion.spring.http.ApiResponse;
import com.tmoncorp.apipotion.spring.http.RestClientTemplate;
import com.tmoncorp.apipotion.spring.http.SpringHttpClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/async")
public class AsyncTestController extends BaseApiController {

    private AsyncRestTemplate asyncRestTemplate;

    @Autowired
    private RestClientTemplate restClientTemplate;


    @Autowired
    private SpringHttpClientFactory springHttpClientFactory;

    private static final Logger LOG = LoggerFactory.getLogger(AsyncTestController.class);

    @PostConstruct
    public void init(){
        HttpComponentsAsyncClientHttpRequestFactory asyncClientHttpRequestFactory=new HttpComponentsAsyncClientHttpRequestFactory(springHttpClientFactory.getAsyncClient());
        asyncRestTemplate=new AsyncRestTemplate(asyncClientHttpRequestFactory,restClientTemplate);

    }

    @RequestMapping(method = RequestMethod.GET, value = "/test")
    @ResponseBody
    public String test() {
        ListenableFuture<ResponseEntity<ApiResponse>> future=asyncRestTemplate.getForEntity("http://localhost:8088/api/async/slow",ApiResponse.class);
        return "OK";
    }
    @RequestMapping(method = RequestMethod.GET, value = "/slow")
    @ResponseBody
    public String slow(){
        try {
            Thread.sleep(900);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOG.debug("=============== O_O ==============");
        return "OK";
    }
}
