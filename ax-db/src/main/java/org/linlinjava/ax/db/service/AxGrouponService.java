package org.linlinjava.ax.db.service;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import org.linlinjava.ax.db.dao.AxGrouponMapper;
import org.linlinjava.ax.db.domain.AxGroupon;
import org.linlinjava.ax.db.domain.AxGrouponExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AxGrouponService {
    @Resource
    private AxGrouponMapper mapper;

    /**
     * 获取用户发起的团购记录
     *
     * @param userId
     * @return
     */
    public List<AxGroupon> queryMyGroupon(Integer userId) {
        AxGrouponExample example = new AxGrouponExample();
        example.or().andUserIdEqualTo(userId).andCreatorUserIdEqualTo(userId).andGrouponIdEqualTo(0).andDeletedEqualTo(false).andPayedEqualTo(true);
        example.orderBy("add_time desc");
        return mapper.selectByExample(example);
    }

    /**
     * 获取用户参与的团购记录
     *
     * @param userId
     * @return
     */
    public List<AxGroupon> queryMyJoinGroupon(Integer userId) {
        AxGrouponExample example = new AxGrouponExample();
        example.or().andUserIdEqualTo(userId).andGrouponIdNotEqualTo(0).andDeletedEqualTo(false).andPayedEqualTo(true);
        example.orderBy("add_time desc");
        return mapper.selectByExample(example);
    }

    /**
     * 根据OrderId查询团购记录
     *
     * @param orderId
     * @return
     */
    public AxGroupon queryByOrderId(Integer orderId) {
        AxGrouponExample example = new AxGrouponExample();
        example.or().andOrderIdEqualTo(orderId).andDeletedEqualTo(false);
        return mapper.selectOneByExample(example);
    }

    /**
     * 获取某个团购活动参与的记录
     *
     * @param id
     * @return
     */
    public List<AxGroupon> queryJoinRecord(Integer id) {
        AxGrouponExample example = new AxGrouponExample();
        example.or().andGrouponIdEqualTo(id).andDeletedEqualTo(false).andPayedEqualTo(true);
        example.orderBy("add_time desc");
        return mapper.selectByExample(example);
    }

    /**
     * 根据ID查询记录
     *
     * @param id
     * @return
     */
    public AxGroupon queryById(Integer id) {
        AxGrouponExample example = new AxGrouponExample();
        example.or().andIdEqualTo(id).andDeletedEqualTo(false).andPayedEqualTo(true);
        return mapper.selectOneByExample(example);
    }

    /**
     * 返回某个发起的团购参与人数
     *
     * @param grouponId
     * @return
     */
    public int countGroupon(Integer grouponId) {
        AxGrouponExample example = new AxGrouponExample();
        example.or().andGrouponIdEqualTo(grouponId).andDeletedEqualTo(false).andPayedEqualTo(true);
        return (int) mapper.countByExample(example);
    }

    public int updateById(AxGroupon groupon) {
        groupon.setUpdateTime(LocalDateTime.now());
        return mapper.updateByPrimaryKeySelective(groupon);
    }

    /**
     * 创建或参与一个团购
     *
     * @param groupon
     * @return
     */
    public int createGroupon(AxGroupon groupon) {
        groupon.setAddTime(LocalDateTime.now());
        groupon.setUpdateTime(LocalDateTime.now());
        return mapper.insertSelective(groupon);
    }


    /**
     * 查询所有发起的团购记录
     *
     * @param rulesId
     * @param page
     * @param size
     * @param sort
     * @param order
     * @return
     */
    public List<AxGroupon> querySelective(String rulesId, Integer page, Integer size, String sort, String order) {
        AxGrouponExample example = new AxGrouponExample();
        AxGrouponExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(rulesId)) {
            criteria.andRulesIdEqualTo(Integer.parseInt(rulesId));
        }
        criteria.andDeletedEqualTo(false);
        criteria.andPayedEqualTo(true);
        criteria.andGrouponIdEqualTo(0);

        PageHelper.startPage(page, size);
        return mapper.selectByExample(example);
    }
}
