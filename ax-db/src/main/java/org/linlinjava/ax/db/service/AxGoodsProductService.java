package org.linlinjava.ax.db.service;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.ax.db.dao.GoodsProductMapper;
import org.linlinjava.ax.db.dao.AxGoodsProductMapper;
import org.linlinjava.ax.db.domain.AxGoodsProduct;
import org.linlinjava.ax.db.domain.AxGoodsProductExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AxGoodsProductService {
    @Resource
    private AxGoodsProductMapper axGoodsProductMapper;
    @Resource
    private GoodsProductMapper goodsProductMapper;

    public List<AxGoodsProduct> queryByGid(Integer gid) {
        AxGoodsProductExample example = new AxGoodsProductExample();
        example.or().andGoodsIdEqualTo(gid).andDeletedEqualTo(false);
        return axGoodsProductMapper.selectByExample(example);
    }

    public AxGoodsProduct findById(Integer id) {
        return axGoodsProductMapper.selectByPrimaryKey(id);
    }

    public void deleteById(Integer id) {
        axGoodsProductMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(AxGoodsProduct goodsProduct) {
        goodsProduct.setAddTime(LocalDateTime.now());
        goodsProduct.setUpdateTime(LocalDateTime.now());
        axGoodsProductMapper.insertSelective(goodsProduct);
    }

    public int count() {
        AxGoodsProductExample example = new AxGoodsProductExample();
        example.or().andDeletedEqualTo(false);
        return (int) axGoodsProductMapper.countByExample(example);
    }

    public void deleteByGid(Integer gid) {
        AxGoodsProductExample example = new AxGoodsProductExample();
        example.or().andGoodsIdEqualTo(gid);
        axGoodsProductMapper.logicalDeleteByExample(example);
    }

    public int addStock(Integer id, Short num){
        return goodsProductMapper.addStock(id, num);
    }

    public int reduceStock(Integer id, Short num){
        return goodsProductMapper.reduceStock(id, num);
    }
}