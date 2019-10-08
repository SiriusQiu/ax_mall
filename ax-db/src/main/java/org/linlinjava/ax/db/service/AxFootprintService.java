package org.linlinjava.ax.db.service;

import com.github.pagehelper.PageHelper;
import org.linlinjava.ax.db.dao.AxFootprintMapper;
import org.linlinjava.ax.db.domain.AxFootprint;
import org.linlinjava.ax.db.domain.AxFootprintExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AxFootprintService {
    @Resource
    private AxFootprintMapper footprintMapper;

    public List<AxFootprint> queryByAddTime(Integer userId, Integer page, Integer size) {
        AxFootprintExample example = new AxFootprintExample();
        example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        example.setOrderByClause(AxFootprint.Column.addTime.desc());
        PageHelper.startPage(page, size);
        return footprintMapper.selectByExample(example);
    }

    public AxFootprint findById(Integer id) {
        return footprintMapper.selectByPrimaryKey(id);
    }

    public void deleteById(Integer id) {
        footprintMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(AxFootprint footprint) {
        footprint.setAddTime(LocalDateTime.now());
        footprint.setUpdateTime(LocalDateTime.now());
        footprintMapper.insertSelective(footprint);
    }

    public List<AxFootprint> querySelective(String userId, String goodsId, Integer page, Integer size, String sort, String order) {
        AxFootprintExample example = new AxFootprintExample();
        AxFootprintExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(userId)) {
            criteria.andUserIdEqualTo(Integer.valueOf(userId));
        }
        if (!StringUtils.isEmpty(goodsId)) {
            criteria.andGoodsIdEqualTo(Integer.valueOf(goodsId));
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return footprintMapper.selectByExample(example);
    }
}
