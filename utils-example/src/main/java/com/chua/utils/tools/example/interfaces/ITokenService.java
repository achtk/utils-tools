package com.chua.utils.tools.example.interfaces;

import com.chua.utils.tools.entity.ReturnBean;
import com.chua.utils.tools.entity.ReturnBeanBuilder;
import feign.Headers;
import feign.RequestLine;
import org.springframework.web.bind.annotation.GetMapping;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 * token服务接口
 * @author CH
 * @date 2020-09-29
 */
public interface ITokenService {

    @RequestLine("GET /test")
    @Headers({"X-Ping: {token}"})
    public String ssoUser(@Named("token") String token);

    @RequestLine("GET /test1")
    @Headers({"X-Ping: {token}"})
    public ReturnBean ssoUser1();
}
