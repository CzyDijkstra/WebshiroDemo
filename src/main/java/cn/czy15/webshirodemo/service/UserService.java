package cn.czy15.webshirodemo.service;

import cn.czy15.webshirodemo.entity.SysUser;
import cn.czy15.webshirodemo.vo.req.LoginReqVO;
import cn.czy15.webshirodemo.vo.req.UserPageReqVO;
import cn.czy15.webshirodemo.vo.resp.LoginRespVO;
import cn.czy15.webshirodemo.vo.resp.PageVO;

/**
 * @author Zhuoyu Chen
 * @date 2019/12/28 0028 - 16:32
 */
public interface UserService {
    LoginRespVO login(LoginReqVO vo);
    void logout(String accessToken,String refreshToken);

    PageVO<SysUser> pageInfo(UserPageReqVO vo);
}
