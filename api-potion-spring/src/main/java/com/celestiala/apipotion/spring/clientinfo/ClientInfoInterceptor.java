package com.celestiala.apipotion.spring.clientinfo;

import com.celestiala.apipotion.core.clientinfo.ClientInfo;
import com.celestiala.apipotion.core.clientinfo.ClientInfoProvider;
import com.celestiala.apipotion.core.clientinfo.ClientPlatform;
import com.celestiala.apipotion.core.clientinfo.HeaderNames;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ClientInfoInterceptor extends HandlerInterceptorAdapter {

    public static final String CLIENT_PLATFORM = HeaderNames.CLIENT_PLATFORM;

    private void setClientInfo(HttpServletRequest request) {
        ClientInfo info = new ClientInfo();
        String platform = request.getHeader(CLIENT_PLATFORM);
        if (platform != null && !"".equals(platform))
            info.setPlatform(ClientPlatform.valueOf(platform));
        else
            info.setPlatform(ClientInfoProvider.getInfo().getPlatform());
        info.setVersion(request.getHeader(HeaderNames.API_VERSION));
        //TODO check header name
        info.setUserAgent(request.getHeader(HeaderNames.USER_AGENT));
        ClientInfoService.setInfo(info);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        setClientInfo(request);
        if (!super.preHandle(request, response, handler))
            return false;
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        ClientInfoService.clean();
        super.postHandle(request, response, handler, modelAndView);
    }

}
