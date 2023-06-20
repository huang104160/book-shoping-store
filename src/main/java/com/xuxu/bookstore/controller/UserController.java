package com.xuxu.bookstore.controller;

import com.xuxu.bookstore.common.CustomException;
import com.xuxu.bookstore.common.R;
import com.xuxu.bookstore.dto.UserDto;
import com.xuxu.bookstore.entity.User;
import com.xuxu.bookstore.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户管理控制器
 */

@Api(tags = "用户接口")
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/isLogin")
    public R<String> isLogin(HttpServletRequest request) {
        if (request.getSession().getAttribute("userId") != null) {
            return R.successMsg("logined");
        }
        return R.success("noLogin");
    }

    /**
     * 用户登录
     *
     * @param request
     * @param user
     * @return
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public R<User> login(HttpServletRequest request, @RequestBody User user, String isAdmin) {
        String encryptPwd = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        // 如果isAdmin不等于1就说明是普通用户登录网站前台, 管理员isAdmin等于1但是他不能登录网站前台, 只能登录后台
        if ("1".equals(isAdmin)) {
            queryWrapper.eq(User::getIsAdmin, 1);
        } else {
            queryWrapper.eq(User::getIsAdmin, 0);
        }
        // SQL: select * from user where username = username;
        queryWrapper.eq(User::getUsername, user.getUsername()); // username已经设计成键, 保证唯一性
        User loginUser = userService.getOne(queryWrapper);
        if (loginUser == null) {
            return R.error("用户名错误");
        }

        if (!loginUser.getPassword().equals(encryptPwd)) {
            return R.error("密码错误");
        }

        if (loginUser.getStatus() == 0) {
            return R.error("该账号已被禁用，请联系管理员解除!");
        }
        // 设置session存活的时间为20分钟，当客户端无任何操作后，20分钟后清除session
//        request.getSession().setMaxInactiveInterval(60 * 20);
        //TODO 开发期间 , 永不过期
        request.getSession().setMaxInactiveInterval(-1);
        request.getSession().setAttribute("userId", loginUser.getId());
        return R.success(loginUser);
    }

    @PostMapping("/register")
    public R<User> register(HttpServletRequest request, @RequestBody User user) {
        // 1. 设置新注册的用户账号可用
        user.setStatus(1);
        // 2. 加密用户的密码
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        // 3. 注册用户信息
        userService.save(user);
        // 4. 从数据库查询插入的用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, user.getUsername());
        User loginUser = userService.getOne(queryWrapper);
        // 5. 将用户信息添加到会话域
        request.getSession().setAttribute("userId", loginUser.getId());
        // 6. 将用户信息返回给前端
        return R.success(loginUser);
    }

    /**
     * 退出登录
     *
     * @param request
     * @return
     */
    @ApiOperation("退出登录")
    @GetMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        //清理Session中保存的当前登录员工的id
        request.getSession().removeAttribute("userId");
        return R.successMsg("退出成功");
    }

    /**
     * 修改用户密码
     *
     * @param userDto
     * @return
     */
    @PostMapping("/pwd")
    public R<String> updatePwd(@RequestBody UserDto userDto) {
        Long userId = userDto.getId();
        // 用户输入的密码
        String password = userDto.getPassword();
        // 新密码
        String newPassword = userDto.getNewPassword();
        // 确认密码
        String confirmPassword = userDto.getConfirmPassword();
        // 1. 新密码和确认密码不一致
        if (!newPassword.equals(confirmPassword)) {
            throw new CustomException("两次输入的密码不一致");
        }
        // 2. 用户输入密码与数据库密码不一致
        User user = userService.getById(userId);
        String rightPwd = user.getPassword();
        // 将用户输入的密码进行加密后和数据库中的密码进行对比
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equalsIgnoreCase(rightPwd)) {
            throw new CustomException("您输入原密码不正确");
        }
        // 3. 旧密码和数据库密码一致则将数据库密码修改为新密码
        user.setPassword(DigestUtils.md5DigestAsHex(newPassword.getBytes()));
        userService.updateById(user);
        return R.successMsg("密码修改成功");
    }

    /**
     * 重置用户密码
     *
     * @param user
     * @return
     */
    @PostMapping("/reset")
    public R<String> resetPwd(@RequestBody User user) {
        // 将密码初始化为123456
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId, user.getId());
        String initPwd = DigestUtils.md5DigestAsHex("123456".getBytes());
        updateWrapper.set(User::getPassword, initPwd);
        userService.update(updateWrapper);
        return R.successMsg("密码重置成功，初始密码为: 123456");
    }

    /**
     * 保存用户信息
     *
     * @param user 用户对象
     * @return
     */
    @ApiOperation("新增用户")
    @PostMapping
    public R<String> save(@RequestBody User user) {
        // 创建的账号默认为可用的
        user.setStatus(1);
        // 设置用户的初始密码为 123456
        user.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        userService.save(user);
        return R.successMsg("新增用户成功");
    }

    /**
     * 根据 id 删除一个或批量删除多个用户
     *
     * @param ids id列表
     * @return
     */
    @ApiOperation("删除用户信息")
    @DeleteMapping
    @ApiImplicitParam(name = "ids", value = "id列表", required = true, dataTypeClass = String.class)
    public R<String> delete(@RequestParam List<Long> ids) {
        userService.removeByIds(ids);
        return R.successMsg("删除用户成功");
    }

    /**
     * 根据用户id修改用户信息
     *
     * @param user 用户对象
     * @return
     */
    @ApiOperation("更改用户信息")
    @PutMapping
    public R<String> update(@RequestBody User user) {
        userService.updateById(user);
        return R.successMsg("修改用户成功");
    }

    /**
     * 根据 id 修改一个或批量修改多个用户状态
     *
     * @param status 用户状态
     * @param ids    id列表
     * @return
     */
    @ApiOperation("修改用户状态")
    @PutMapping("/status/{status}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "状态[1正常 0禁用]", dataTypeClass = String.class),
            @ApiImplicitParam(name = "ids", value = "id列表", dataTypeClass = String.class)
    })
    public R<String> status(@PathVariable("status") Integer status, @RequestParam List<Long> ids) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        // SQL: update user set status = status where id in (ids);
        updateWrapper.in(User::getId, ids);
        updateWrapper.set(User::getStatus, status);
        userService.update(updateWrapper);
        return R.success("修改用户状态成功");
    }

    /**
     * 根据 id 获取用户信息
     *
     * @param id 用户id
     * @return
     */
    @ApiOperation("根据id获取用户信息")
    @GetMapping("/{id}")
    public R<User> getById(@PathVariable("id") Long id) {
        User user = userService.getById(id);
        return R.success(user);
    }

    /**
     * 分页查询用户信息
     *
     * @param page     当前页数
     * @param pageSize 一页的数据量
     * @return
     */

    @ApiOperation("分页查询用户信息")
    @GetMapping("/page/{page}/{pageSize}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "pageSize", value = "每页数据量", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "username", value = "用户名", required = false, dataTypeClass = String.class)
    })
    public R<Page<User>> page(
            @PathVariable("page") Integer page,
            @PathVariable("pageSize") Integer pageSize,
            String username
    ) {
        Page<User> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        // SQL: select * from user where username like %username% order by update_time desc;
        queryWrapper.like(username != null, User::getUsername, username);
        queryWrapper.eq(User::getIsAdmin, 0);
        queryWrapper.orderByDesc(User::getUpdateTime);
        userService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 用户在商城页面修改个人信息
     *
     * @param user
     * @return
     */
    @ApiOperation("用户在商城页面修改个人信息")
    @PostMapping("/costumUpgrade")
    public R<User> login(@RequestBody User user) {

        User loginUser = userService.getById(user.getId());
        LambdaQueryWrapper<User> uqw = new LambdaQueryWrapper<>();
        uqw.eq(User::getUsername, user.getUsername());
        User userSearched = userService.getOne(uqw);
        if(userSearched.getId() != userSearched.getId()){
            return R.error("该账户名已被使用, 请修改");
        }
        if (loginUser == null) {
            return R.error("用户名或密码错误");
        }
        if (loginUser.getStatus() == 0) {
            return R.error("该账号已被禁用，请联系管理员解除!");
        }
        String encryptPwd = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(encryptPwd);
        userService.updateById(user);
        return R.success(user);
    }
}
