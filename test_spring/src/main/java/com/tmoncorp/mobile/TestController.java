package com.tmoncorp.mobile;

import com.tmoncorp.core.controller.BaseApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class TestController extends BaseApiController {
    //	@Autowired
    //	CategoryService service;

    @RequestMapping(method = RequestMethod.GET, value = "/test1")
    @ResponseBody
    public String getAll() {
        return "";
    }

}
