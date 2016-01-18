package com.tmoncorp.mobile;

import com.tmoncorp.core.controller.BaseApiController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/")
public class TestController extends BaseApiController {
//	@Autowired
//	CategoryService service;

	@RequestMapping(method=RequestMethod.GET, value="/test1")
	@ResponseBody
	public String getAll() {
		return "";
	}


}
