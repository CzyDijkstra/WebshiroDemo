package cn.czy15.webshirodemo.mapper;

import cn.czy15.webshirodemo.entity.SysUser;
import cn.czy15.webshirodemo.entity.SysUserExample;
import java.util.List;

import cn.czy15.webshirodemo.vo.req.UserPageReqVO;
import org.apache.ibatis.annotations.Param;
public interface SysUserMapper {
    int deleteByPrimaryKey(String id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    SysUser getUserInfoByName(String username);

    List<SysUser> selectAll(UserPageReqVO vo);

}