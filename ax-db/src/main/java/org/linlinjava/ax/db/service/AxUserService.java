package org.linlinjava.ax.db.service;

import com.github.pagehelper.PageHelper;
import org.linlinjava.ax.db.dao.AxUserMapper;
import org.linlinjava.ax.db.domain.AxUser;
import org.linlinjava.ax.db.domain.AxUserExample;
import org.linlinjava.ax.db.domain.UserVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AxUserService {
    @Resource
    private AxUserMapper userMapper;

    public AxUser findById(Integer userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    public UserVo findUserVoById(Integer userId) {
        AxUser user = findById(userId);
        UserVo userVo = new UserVo();
        userVo.setNickname(user.getNickname());
        userVo.setAvatar(user.getAvatar());
        return userVo;
    }

    public AxUser queryByOid(String openId) {
        AxUserExample example = new AxUserExample();
        example.or().andWeixinOpenidEqualTo(openId).andDeletedEqualTo(false);
        return userMapper.selectOneByExample(example);
    }

    public void add(AxUser user) {
        user.setAddTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.insertSelective(user);
    }

    public int updateById(AxUser user) {
        user.setUpdateTime(LocalDateTime.now());
        return userMapper.updateByPrimaryKeySelective(user);
    }

    public List<AxUser> querySelective(String username, String mobile, Integer page, Integer size, String sort, String order) {
        AxUserExample example = new AxUserExample();
        AxUserExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(username)) {
            criteria.andUsernameLike("%" + username + "%");
        }
        if (!StringUtils.isEmpty(mobile)) {
            criteria.andMobileEqualTo(mobile);
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return userMapper.selectByExample(example);
    }

    public int count() {
        AxUserExample example = new AxUserExample();
        example.or().andDeletedEqualTo(false);

        return (int) userMapper.countByExample(example);
    }

    public List<AxUser> queryByUsername(String username) {
        AxUserExample example = new AxUserExample();
        example.or().andUsernameEqualTo(username).andDeletedEqualTo(false);
        return userMapper.selectByExample(example);
    }

    public boolean checkByUsername(String username) {
        AxUserExample example = new AxUserExample();
        example.or().andUsernameEqualTo(username).andDeletedEqualTo(false);
        return userMapper.countByExample(example) != 0;
    }

    public List<AxUser> queryByMobile(String mobile) {
        AxUserExample example = new AxUserExample();
        example.or().andMobileEqualTo(mobile).andDeletedEqualTo(false);
        return userMapper.selectByExample(example);
    }

    public List<AxUser> queryByOpenid(String openid) {
        AxUserExample example = new AxUserExample();
        example.or().andWeixinOpenidEqualTo(openid).andDeletedEqualTo(false);
        return userMapper.selectByExample(example);
    }

    public void deleteById(Integer id) {
        userMapper.logicalDeleteByPrimaryKey(id);
    }
}
