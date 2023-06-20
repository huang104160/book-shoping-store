package com.xuxu.bookstore.filter;

import com.xuxu.bookstore.common.R;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否已登录
 */
@Slf4j
// 登录拦截器，拦截所有路径
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    // 路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();



    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("我是LoginCheckFilter, 我应该第一个执行");
        // 向下转型
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 1. 获取本次请求的URI
        String requestURI = request.getRequestURI();
        // 定义不需要处理的拦截路径
        String[] urls = new String[]{
                "/",
                "/user/login",
                "/user/logout",
                "/user/register",
                "/backend/**",  // 放行所有的资源文件
                "/front/**",
                "/front/index.html",
                "/category/list",
                "/book/page/*/*",
                "/book/search/*/*",
                "/category/*",
                "/common/**",
                // 放行 swagger 的资源文件，不需要登录即可访问
                "/doc.html",
                "/webjars/**",
                "/swagger-resources",
                "/v2/api-docs"
        };
        // 2. 判断本次请求是否需要登录才可以访问
        boolean check = check(urls, requestURI);
        // 3. 如果不需要，则直接放行
        if (check) {
            log.info("本次请求 {} 不需要处理", requestURI);
            filterChain.doFilter(request, response);
            return;
        }
        // 4. 判断登录状态，如果已登录，则直接放行
        if (request.getSession().getAttribute("userId") != null) {

            String userId = request.getSession().getAttribute("userId").toString();
            log.info("用户已登录，用户id为: {}", userId);
            filterChain.doFilter(request, response);
            return;
        }
        log.info("用户未登录");
        // 5. 如果未登录则返回未登录结果，通过输出流的方式向客户端页面响应数据
        // 设置响应的类型并设置字符编码
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(R.error("请先登录")));
    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     *
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
