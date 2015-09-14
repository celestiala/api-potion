package com.tmoncorp.mobile.util.spring.version;

import com.tmoncorp.mobile.util.common.version.BuildInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/version")
public class VersionController {

	@Autowired
	private ManifestService manifestService;

	@RequestMapping(method = { RequestMethod.GET }, value = { "/buildInfo" })
	@ResponseBody
	public BuildInfo getBuildInfo(){
		try {
			return manifestService.getBuildInfo();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
