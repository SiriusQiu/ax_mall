package org.linlinjava.ax.db.service;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import org.linlinjava.ax.db.dao.AxGoodsMapper;
import org.linlinjava.ax.db.dao.AxGrouponRulesMapper;
import org.linlinjava.ax.db.domain.AxGoods;
import org.linlinjava.ax.db.domain.AxGrouponRules;
import org.linlinjava.ax.db.domain.AxGrouponRulesExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AxGrouponRulesService {
    @Resource
    private AxGrouponRulesMapper mapper;
    @Resource
    private AxGoodsMapper goodsMapper;
    private AxGoods.Column[] goodsColumns = new AxGoods.Column[]{AxGoods.Column.id, AxGoods.Column.name, AxGoods.Column.brief, AxGoods.Column.picUrl, AxGoods.Column.counterPrice, AxGoods.Column.retailPrice};

    public int createRules(AxGrouponRules rules) {
        rules.setAddTime(LocalDateTime.now());
        rules.setUpdateTime(LocalDateTime.now());
        return mapper.insertSelective(rules);
    }

    /**
     * 根据ID查找对应团购项
     *
     * @param id
     * @return
     */
    public AxGrouponRules queryById(Integer id) {
        AxGrouponRulesExample example = new AxGrouponRulesExample();
        example.or().andIdEqualTo(id).andDeletedEqualTo(false);
        return mapper.selectOneByExample(example);
    }

    /**
     * 查询某个商品关联的团购规则
     *
     * @param goodsId
     * @return
     */
    public List<AxGrouponRules> queryByGoodsId(Integer goodsId) {
        AxGrouponRulesExample example = new AxGrouponRulesExample();
        example.or().andGoodsIdEqualTo(goodsId).andDeletedEqualTo(false);
        return mapper.selectByExample(example);
    }

    /**
     * 获取首页团购活动列表
     *
     * @param page
     * @param limit
     * @return
     */
    public List<AxGrouponRules> queryList(Integer page, Integer limit) {
        return queryList(page, limit, "add_time", "desc");
    }

    public List<AxGrouponRules> queryList(Integer page, Integer limit, String sort, String order) {
        AxGrouponRulesExample example = new AxGrouponRulesExample();
        example.or().andDeletedEqualTo(false);
        example.setOrderByClause(sort + " " + order);
        PageHelper.startPage(page, limit);
        return mapper.selectByExample(example);
    }

    /**
     * 判断某个团购活动是否已经过期
     *
     * @return
     */
    public boolean isExpired(AxGrouponRules rules) {
        return (rules == null || rules.getExpireTime().isBefore(LocalDateTime.now()));
    }

    /**
     * 获取团购活动列表
     *
     * @param goodsId
     * @param page
     * @param size
     * @param sort
     * @param order
     * @return
     */
    public List<AxGrouponRules> querySelective(String goodsId, Integer page, Integer size, String sort, String order) {
        AxGrouponRulesExample example = new AxGrouponRulesExample();
        example.setOrderByClause(sort + " " + order);

        AxGrouponRulesExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(goodsId)) {
            criteria.andGoodsIdEqualTo(Integer.parseInt(goodsId));
        }
        criteria.andDeletedEqualTo(false);

        PageHelper.startPage(page, size);
        return mapper.selectByExample(example);
    }

    public void delete(Integer id) {
        mapper.logicalDeleteByPrimaryKey(id);
    }

    public int updateById(AxGrouponRules grouponRules) {
        grouponRules.setUpdateTime(LocalDateTime.now());
        return mapper.updateByPrimaryKeySelective(grouponRules);
    }
}