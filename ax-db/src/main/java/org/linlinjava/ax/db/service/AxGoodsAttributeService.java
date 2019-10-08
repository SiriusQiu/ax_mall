package org.linlinjava.ax.db.service;

import org.linlinjava.ax.db.dao.AxGoodsAttributeMapper;
import org.linlinjava.ax.db.domain.AxGoodsAttribute;
import org.linlinjava.ax.db.domain.AxGoodsAttributeExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AxGoodsAttributeService {
    @Resource
    private AxGoodsAttributeMapper goodsAttributeMapper;

    public List<AxGoodsAttribute> queryByGid(Integer goodsId) {
        AxGoodsAttributeExample example = new AxGoodsAttributeExample();
        example.or().andGoodsIdEqualTo(goodsId).andDeletedEqualTo(false);
        return goodsAttributeMapper.selectByExample(example);
    }

    public void add(AxGoodsAttribute goodsAttribute) {
        goodsAttribute.setAddTime(LocalDateTime.now());
        goodsAttribute.setUpdateTime(LocalDateTime.now());
        goodsAttributeMapper.insertSelective(goodsAttribute);
    }

    public AxGoodsAttribute findById(Integer id) {
        return goodsAttributeMapper.selectByPrimaryKey(id);
    }

    public void deleteByGid(Integer gid) {
        AxGoodsAttributeExample example = new AxGoodsAttributeExample();
        example.or().andGoodsIdEqualTo(gid);
        goodsAttributeMapper.logicalDeleteByExample(example);
    }
}
