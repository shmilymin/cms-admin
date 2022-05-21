package com.mm.common.handler;

import cn.hutool.http.ContentType;
import cn.hutool.json.JSONUtil;
import com.mm.common.util.R;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 权限错误
 *
 * @author lwl
 */
@Component
public class AuthFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response
            , AuthenticationException e) throws IOException {
        response.setContentType(ContentType.JSON.getValue());
        try (PrintWriter writer = response.getWriter()) {
            writer.write(JSONUtil.toJsonStr(R.error(e.getMessage())));
        }
    }
}
