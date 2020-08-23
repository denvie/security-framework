/*
 * Copyright © 2020-2020 尛飛俠（Denvie） All rights reserved.
 */

package cn.denvie.security.gateway.context;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 安全网关Api请求分发器。
 *
 * @author denvie
 * @since 2020/8/23
 */
@WebServlet(name = "securityGatewayServlet", urlPatterns = "/api", loadOnStartup = 2)
public class SecurityGatewayServlet extends HttpServlet {

    private ApplicationContext context;
    private SecurityGatewayHandler apiHandler;

    @Override
    public void init() throws ServletException {
        super.init();
        context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        apiHandler = context.getBean(SecurityGatewayHandler.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        apiHandler.handle(req, resp, "GET");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        apiHandler.handle(request, response, "POST");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        apiHandler.handle(req, resp, "PUT");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        apiHandler.handle(req, resp, "DELETE");
    }
}
