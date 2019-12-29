package cn.czy15.webshirodemo.service.impl;

import cn.czy15.webshirodemo.constant.Constant;
import cn.czy15.webshirodemo.entity.SysUser;
import cn.czy15.webshirodemo.exception.BusinessException;
import cn.czy15.webshirodemo.exception.code.BaseResponseCode;
import cn.czy15.webshirodemo.mapper.SysUserMapper;
import cn.czy15.webshirodemo.service.RedisService;
import cn.czy15.webshirodemo.service.UserService;
import cn.czy15.webshirodemo.utils.JwtTokenUtil;
import cn.czy15.webshirodemo.utils.PageUtil;
import cn.czy15.webshirodemo.utils.PasswordUtils;
import cn.czy15.webshirodemo.vo.req.LoginReqVO;
import cn.czy15.webshirodemo.vo.req.UserPageReqVO;
import cn.czy15.webshirodemo.vo.resp.LoginRespVO;
import cn.czy15.webshirodemo.vo.resp.PageVO;
import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Zhuoyu Chen
 * @date 2019/12/28 0028 - 16:35
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private RedisService redisService;
    @Override
    public LoginRespVO login(LoginReqVO vo) {
        //通过用户名查询用户信息
        //如果查询存在用户
        //就比较它密码是否一样
        SysUser userInfoByName = sysUserMapper.getUserInfoByName(vo.getUsername());
        if(userInfoByName==null){
            throw new BusinessException(BaseResponseCode.ACCOUNT_ERROR);
        }
        System.out.println(111);
        if(userInfoByName.getStatus()==2){
            throw new BusinessException(BaseResponseCode.ACCOUNT_LOCK);
        }

        if(!PasswordUtils.matches(userInfoByName.getSalt(),vo.getPassword(),userInfoByName.getPassword())){
            throw new BusinessException(BaseResponseCode.ACCOUNT_PASSWORD_ERROR);
        }

        LoginRespVO loginRespVO=new LoginRespVO();
        loginRespVO.setPhone(userInfoByName.getPhone());
        loginRespVO.setUsername(userInfoByName.getUsername());
        loginRespVO.setId(userInfoByName.getId());
        Map<String, Object> claims=new HashMap<>();
        claims.put(Constant.JWT_ROLES_KEY,getRolesByUserId(userInfoByName.getId()));
        claims.put(Constant.JWT_PERMISSIONS_KEY,getPermissionsByUserId(userInfoByName.getId()));
        claims.put(Constant.JWT_USER_NAME,userInfoByName.getUsername());
        String accessToken=JwtTokenUtil.getAccessToken(userInfoByName.getId(),claims);
        String refreshToken;
        if(vo.getType().equals("1")){
            refreshToken=JwtTokenUtil.getRefreshToken(userInfoByName.getId(),claims);
        }else {
            refreshToken=JwtTokenUtil.getRefreshAppToken(userInfoByName.getId(),claims);
        }
        loginRespVO.setAccessToken(accessToken);
        loginRespVO.setRefreshToken(refreshToken);
        return loginRespVO;
    }
    /**
     * mock 数据
     * 通过用户id获取该用户所拥有的角色
     * 后期修改为通过操作DB获取
     * @UpdateUser:
     * @Version:     0.0.1
     * @param userId
     * @return       java.util.List<java.lang.String>
     * @throws
     */
    private List<String> getRolesByUserId(String userId){

        List<String> roles=new ArrayList<>();
        if("9a26f5f1-cbd2-473d-82db-1d6dcf4598f8".equals(userId)){
            roles.add("admin");
        }else {
            roles.add("test");
        }
        return roles;
    }
    /**
     * mock 数据
     * 通过用户id获取该用户所拥有的角色
     * 后期通过操作数据获取
     * @Version:     0.0.1
     * @param userId
     * @return       java.util.List<java.lang.String>
     * @throws
     */
    private List<String> getPermissionsByUserId(String userId){
        List<String> permissions=new ArrayList<>();
        if("9a26f5f1-cbd2-473d-82db-1d6dcf4598f8".equals(userId)){
            permissions.add("sys:user:list");
            permissions.add("sys:user:add");
            permissions.add("sys:user:update");
            permissions.add("sys:user:detail");
        }else {
            permissions.add("sys:user:detail");
        }
        return permissions;
    }

    @Override
    public void logout(String accessToken, String refreshToken) {
        if(StringUtils.isEmpty(accessToken)||StringUtils.isEmpty(refreshToken)){
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        Subject subject = SecurityUtils.getSubject();
        log.info("subject.getPrincipals()={}",subject.getPrincipals());
        if (subject.isAuthenticated()) {
            subject.logout();
        }
        String userId=JwtTokenUtil.getUserId(accessToken);
        /**
         * 把token 加入黑名单 禁止再登录
         */
        redisService.set(Constant.JWT_REFRESH_TOKEN_BLACKLIST+accessToken,userId,JwtTokenUtil.getRemainingTime(accessToken), TimeUnit.MILLISECONDS);
        /**
         * 把 refreshToken 加入黑名单 禁止再拿来刷新token
         */
        redisService.set(Constant.JWT_REFRESH_TOKEN_BLACKLIST+refreshToken,userId,JwtTokenUtil.getRemainingTime(refreshToken),TimeUnit.MILLISECONDS);


    }


    @Override
    public PageVO<SysUser> pageInfo(UserPageReqVO vo) {
        PageHelper.startPage(vo.getPageNum(),vo.getPageSize());
        List<SysUser> list=sysUserMapper.selectAll(vo);
        return PageUtil.getPageVO(list);
    }
}
