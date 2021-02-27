package org.indexlm.frame.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.indexlm.frame.bean.common.AuthCode;
import org.indexlm.frame.bean.common.exception.LogErrorException;
import org.indexlm.frame.bean.common.response.RespRes;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 这个是在权限不足的时候调用的方法
 *
 * @author LiuMing
 * @since 2021/2/27
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            RespRes result = RespRes.code(AuthCode.UN_AUTHORISE);
            String resultStr = objectMapper.writeValueAsString(result);
            httpServletResponse.setContentType("application/json;charset=utf-8");
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpServletResponse.getWriter().write(resultStr);
        } catch (Exception ex) {
           LogErrorException.cast(AuthCode.UN_AUTHORISE);
        }
    }
}
