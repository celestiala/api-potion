package com.tmoncorp.mobile.util.spring.cache;

import com.tmoncorp.mobile.util.common.cache.CacheMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cache")
public class ApiCacheController {

	@Autowired
	private MCacheInterceptor cacheInterceptor;

	@RequestMapping(method = { RequestMethod.GET }, value = { "/mode/{mode}" })
	@ResponseBody
	public String setCacheMode(@PathVariable(value = "mode") CacheMode mode){
		cacheInterceptor.setMode(mode);
		return cacheInterceptor.getMode().toString();
	}

	@RequestMapping(method = { RequestMethod.GET }, value = { "/prefix" })
	@ResponseBody
	public String changeCacheKey(@RequestParam(value = "key", required = false) String prefix){
		cacheInterceptor.setPrefix(prefix);
		return cacheInterceptor.getPrefix();
	}
}
