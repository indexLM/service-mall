package org.indexlm.frame.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.indexlm.frame.bean.common.AuthCode;
import org.indexlm.frame.bean.common.exception.LogErrorException;
import org.indexlm.frame.bean.common.response.RespRes;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 这个是在token判定不通过的时候调用的方法
 *
 * @author LiuMing
 * @since 2021/2/27
 */
@Component
public class AuthExceptionEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            RespRes result = RespRes.code(AuthCode.TOKEN_EXPIRE);
            String resultStr = objectMapper.writeValueAsString(result);
            httpServletResponse.setContentType("application/json;charset=utf-8");
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpServletResponse.getWriter().write(resultStr);
        } catch (Exception ee) {
            LogErrorException.cast(AuthCode.TOKEN_EXPIRE);
        }
    }
}
