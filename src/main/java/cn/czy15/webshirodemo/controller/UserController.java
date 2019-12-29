package cn.czy15.webshirodemo.controller;
import cn.czy15.webshirodemo.constant.Constant;
import cn.czy15.webshirodemo.entity.SysUser;
import cn.czy15.webshirodemo.exception.code.BaseResponseCode;
import cn.czy15.webshirodemo.service.UserService;
import cn.czy15.webshirodemo.service.impl.UserServiceImpl;
import cn.czy15.webshirodemo.utils.DataResult;
import cn.czy15.webshirodemo.vo.req.LoginReqVO;
import cn.czy15.webshirodemo.vo.req.UserPageReqVO;
import cn.czy15.webshirodemo.vo.resp.LoginRespVO;
import cn.czy15.webshirodemo.vo.resp.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @ClassName: UserController
 * TODO:类文件简单描述
 */
@RestController
@RequestMapping("/api")
@Api(tags = "用户模块相关接口")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/user/login")
    @ApiOperation(value = "用户登录接口")
    public DataResult<LoginRespVO> login(@RequestBody @Valid LoginReqVO vo){
        DataResult result=DataResult.success();
        result.setData(userService.login(vo));
        return result;
    }

    @GetMapping("/user/logout")
    @ApiOperation(value = "用户登出接口")
    public DataResult logout(HttpServletRequest request){
        String accessToken=request.getHeader(Constant.ACCESS_TOKEN);
        String refreshToken=request.getHeader(Constant.REFRESH_TOKEN);
        userService.logout(accessToken,refreshToken);
        return DataResult.success();
    }

    @GetMapping("/user/unLogin")
    @ApiOperation(value = "引导客户端去登录")
    public DataResult unLogin(){
        DataResult result= DataResult.getResult(BaseResponseCode.TOKEN_ERROR);
        return result;
    }

    @PostMapping("/users")
    @ApiOperation(value = "分页查询用户接口")
    @RequiresPermissions("sys:user:list")
    public DataResult<PageVO<SysUser>> pageInfo(@RequestBody UserPageReqVO vo){
        DataResult result=DataResult.success();
        result.setData(userService.pageInfo(vo));
        return result;
    }

}
