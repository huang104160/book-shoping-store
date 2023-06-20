package com.xuxu.bookstore.filter;

/**
 * 3、多个过滤器如何指定执行顺序？
 * 刚才说了，使用@Order注解指定一个int值，越小越先执行。很多博客文章都是这么说的，但你真正的试了吗？
 * 真的可以使用这个注解指定顺序吗？答案是否定的。
 *
 * 经过测试，发现 @Order 注解指定 int 值没有起作用，是无效的。
 * 为啥？因为看源码发现 @WebFilter 修饰的过滤器在加载时，没有使用 @Order 注解，
 * 而是使用的类名来实现自定义Filter顺序，详细的可以参考这篇或者是这篇
 *
 * 所以这种方式下想定义Filter的顺序，就必须限定 Filter 的类名，
 * 比如刚才那个 Filter 叫 ReqResFilter ，加入我们现在新写了一个 Filter 叫 AlibabaFilter ，
 * 那么顺序就是 AlibabaFilter > ReqResFilter 。
 *
 * 所以这种方式虽然实现起来简单，只需要注解，
 * 但自定义顺序就必须要限定类名，使用类名达到排序效果了。
 */


import com.xuxu.bookstore.common.R;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 检查用户角色 ,  (进行判断, admin不能去前台, 他想要访问前台可以自己重新注册一个号, 普通用户不能去后台)
 */
@Slf4j
// 登录拦截器，拦截所有路径
@WebFilter(filterName = "RoleCheckFilter", urlPatterns = "/*")
public class RoleCheckFilter implements Filter {

    @Value("#{'${userRole.adminIds}'.split(',')}") // 获取配置属性并以','分割为列表
    private List<String> adminIds;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("我是RoleCheckFilter, 我应该第二个执行");
        // 向下转型
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 1. 获取本次请求的URI
        String requestURI = request.getRequestURI();
        Object userId = request.getSession().getAttribute("userId");
        if(userId!=null){
            if(adminIds.contains(userId.toString())&&requestURI.indexOf("front")!=-1){
                // 如果登录浏览器中缓存的id是admin里面的,并且将要访问的页面是前台页面, 那么不予通过
                // 设置响应的类型并设置字符编码
                response.setContentType("application/json; charset=utf-8");
                response.getWriter().write(JSON.toJSONString(R.error("admin用户不能进入前台页面")));
                response.sendRedirect("/backend/index.html");
            }else if(!adminIds.contains(userId.toString()) && requestURI.indexOf("backend")!=-1){
                // 如果登录浏览器中缓存的id不是admin里面的,并且将要访问的页面是后台页面, 那么不予通过
                // 设置响应的类型并设置字符编码
                response.setContentType("application/json; charset=utf-8");
                response.getWriter().write(JSON.toJSONString(R.error("普通用户不能进入后台页面")));
                response.sendRedirect("/front/index.html");
            }else {
                filterChain.doFilter(request, response);
                return;
            }
        }else {
            filterChain.doFilter(request, response);
            return;
        }
    }
}
