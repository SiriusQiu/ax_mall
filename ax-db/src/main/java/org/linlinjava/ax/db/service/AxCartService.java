package org.linlinjava.ax.db.service;

import com.github.pagehelper.PageHelper;
import org.linlinjava.ax.db.dao.AxCartMapper;
import org.linlinjava.ax.db.domain.AxCart;
import org.linlinjava.ax.db.domain.AxCartExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AxCartService {
    @Resource
    private AxCartMapper cartMapper;

    public AxCart queryExist(Integer goodsId, Integer productId, Integer userId) {
        AxCartExample example = new AxCartExample();
        example.or().andGoodsIdEqualTo(goodsId).andProductIdEqualTo(productId).andUserIdEqualTo(userId).andDeletedEqualTo(false);
        return cartMapper.selectOneByExample(example);
    }

    public void add(AxCart cart) {
        cart.setAddTime(LocalDateTime.now());
        cart.setUpdateTime(LocalDateTime.now());
        cartMapper.insertSelective(cart);
    }

    public int updateById(AxCart cart) {
        cart.setUpdateTime(LocalDateTime.now());
        return cartMapper.updateByPrimaryKeySelective(cart);
    }

    public List<AxCart> queryByUid(int userId) {
        AxCartExample example = new AxCartExample();
        example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        return cartMapper.selectByExample(example);
    }


    public List<AxCart> queryByUidAndChecked(Integer userId) {
        AxCartExample example = new AxCartExample();
        example.or().andUserIdEqualTo(userId).andCheckedEqualTo(true).andDeletedEqualTo(false);
        return cartMapper.selectByExample(example);
    }

    public int delete(List<Integer> productIdList, int userId) {
        AxCartExample example = new AxCartExample();
        example.or().andUserIdEqualTo(userId).andProductIdIn(productIdList);
        return cartMapper.logicalDeleteByExample(example);
    }

    public AxCart findById(Integer id) {
        return cartMapper.selectByPrimaryKey(id);
    }

    public int updateCheck(Integer userId, List<Integer> idsList, Boolean checked) {
        AxCartExample example = new AxCartExample();
        example.or().andUserIdEqualTo(userId).andProductIdIn(idsList).andDeletedEqualTo(false);
        AxCart cart = new AxCart();
        cart.setChecked(checked);
        cart.setUpdateTime(LocalDateTime.now());
        return cartMapper.updateByExampleSelective(cart, example);
    }

    public void clearGoods(Integer userId) {
        AxCartExample example = new AxCartExample();
        example.or().andUserIdEqualTo(userId).andCheckedEqualTo(true);
        AxCart cart = new AxCart();
        cart.setDeleted(true);
        cartMapper.updateByExampleSelective(cart, example);
    }

    public List<AxCart> querySelective(Integer userId, Integer goodsId, Integer page, Integer limit, String sort, String order) {
        AxCartExample example = new AxCartExample();
        AxCartExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(userId)) {
            criteria.andUserIdEqualTo(userId);
        }
        if (!StringUtils.isEmpty(goodsId)) {
            criteria.andGoodsIdEqualTo(goodsId);
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return cartMapper.selectByExample(example);
    }

    public void deleteById(Integer id) {
        cartMapper.logicalDeleteByPrimaryKey(id);
    }

    public boolean checkExist(Integer goodsId) {
        AxCartExample example = new AxCartExample();
        example.or().andGoodsIdEqualTo(goodsId).andCheckedEqualTo(true).andDeletedEqualTo(false);
        return cartMapper.countByExample(example) != 0;
    }
}
