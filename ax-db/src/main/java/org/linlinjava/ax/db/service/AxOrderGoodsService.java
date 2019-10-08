package org.linlinjava.ax.db.service;

import org.linlinjava.ax.db.dao.AxOrderGoodsMapper;
import org.linlinjava.ax.db.domain.AxOrderGoods;
import org.linlinjava.ax.db.domain.AxOrderGoodsExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AxOrderGoodsService {
    @Resource
    private AxOrderGoodsMapper orderGoodsMapper;

    public int add(AxOrderGoods orderGoods) {
        orderGoods.setAddTime(LocalDateTime.now());
        orderGoods.setUpdateTime(LocalDateTime.now());
        return orderGoodsMapper.insertSelective(orderGoods);
    }

    public List<AxOrderGoods> queryByOid(Integer orderId) {
        AxOrderGoodsExample example = new AxOrderGoodsExample();
        example.or().andOrderIdEqualTo(orderId).andDeletedEqualTo(false);
        return orderGoodsMapper.selectByExample(example);
    }

    public List<AxOrderGoods> findByOidAndGid(Integer orderId, Integer goodsId) {
        AxOrderGoodsExample example = new AxOrderGoodsExample();
        example.or().andOrderIdEqualTo(orderId).andGoodsIdEqualTo(goodsId).andDeletedEqualTo(false);
        return orderGoodsMapper.selectByExample(example);
    }

    public AxOrderGoods findById(Integer id) {
        return orderGoodsMapper.selectByPrimaryKey(id);
    }

    public void updateById(AxOrderGoods orderGoods) {
        orderGoods.setUpdateTime(LocalDateTime.now());
        orderGoodsMapper.updateByPrimaryKeySelective(orderGoods);
    }

    public Short getComments(Integer orderId) {
        AxOrderGoodsExample example = new AxOrderGoodsExample();
        example.or().andOrderIdEqualTo(orderId).andDeletedEqualTo(false);
        long count = orderGoodsMapper.countByExample(example);
        return (short) count;
    }

    public boolean checkExist(Integer goodsId) {
        AxOrderGoodsExample example = new AxOrderGoodsExample();
        example.or().andGoodsIdEqualTo(goodsId).andDeletedEqualTo(false);
        return orderGoodsMapper.countByExample(example) != 0;
    }
}
