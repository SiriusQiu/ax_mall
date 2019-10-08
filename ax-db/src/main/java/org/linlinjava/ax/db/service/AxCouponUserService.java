package org.linlinjava.ax.db.service;

import com.github.pagehelper.PageHelper;
import org.linlinjava.ax.db.dao.AxCouponUserMapper;
import org.linlinjava.ax.db.domain.AxCouponUser;
import org.linlinjava.ax.db.domain.AxCouponUserExample;
import org.linlinjava.ax.db.util.CouponUserConstant;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AxCouponUserService {
    @Resource
    private AxCouponUserMapper couponUserMapper;

    public Integer countCoupon(Integer couponId) {
        AxCouponUserExample example = new AxCouponUserExample();
        example.or().andCouponIdEqualTo(couponId).andDeletedEqualTo(false);
        return (int)couponUserMapper.countByExample(example);
    }

    public Integer countUserAndCoupon(Integer userId, Integer couponId) {
        AxCouponUserExample example = new AxCouponUserExample();
        example.or().andUserIdEqualTo(userId).andCouponIdEqualTo(couponId).andDeletedEqualTo(false);
        return (int)couponUserMapper.countByExample(example);
    }

    public void add(AxCouponUser couponUser) {
        couponUser.setAddTime(LocalDateTime.now());
        couponUser.setUpdateTime(LocalDateTime.now());
        couponUserMapper.insertSelective(couponUser);
    }

    public List<AxCouponUser> queryList(Integer userId, Integer couponId, Short status, Integer page, Integer size, String sort, String order) {
        AxCouponUserExample example = new AxCouponUserExample();
        AxCouponUserExample.Criteria criteria = example.createCriteria();
        if (userId != null) {
            criteria.andUserIdEqualTo(userId);
        }
        if(couponId != null){
            criteria.andCouponIdEqualTo(couponId);
        }
        if (status != null) {
            criteria.andStatusEqualTo(status);
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        if (!StringUtils.isEmpty(page) && !StringUtils.isEmpty(size)) {
            PageHelper.startPage(page, size);
        }

        return couponUserMapper.selectByExample(example);
    }

    public List<AxCouponUser> queryAll(Integer userId, Integer couponId) {
        return queryList(userId, couponId, CouponUserConstant.STATUS_USABLE, null, null, "add_time", "desc");
    }

    public List<AxCouponUser> queryAll(Integer userId) {
        return queryList(userId, null, CouponUserConstant.STATUS_USABLE, null, null, "add_time", "desc");
    }

    public AxCouponUser queryOne(Integer userId, Integer couponId) {
        List<AxCouponUser> couponUserList = queryList(userId, couponId, CouponUserConstant.STATUS_USABLE, 1, 1, "add_time", "desc");
        if(couponUserList.size() == 0){
            return null;
        }
        return couponUserList.get(0);
    }

    public AxCouponUser findById(Integer id) {
        return couponUserMapper.selectByPrimaryKey(id);
    }


    public int update(AxCouponUser couponUser) {
        couponUser.setUpdateTime(LocalDateTime.now());
        return couponUserMapper.updateByPrimaryKeySelective(couponUser);
    }

    public List<AxCouponUser> queryExpired() {
        AxCouponUserExample example = new AxCouponUserExample();
        example.or().andStatusEqualTo(CouponUserConstant.STATUS_USABLE).andEndTimeLessThan(LocalDateTime.now()).andDeletedEqualTo(false);
        return couponUserMapper.selectByExample(example);
    }
}
