package org.linlinjava.ax.db.service;

import com.github.pagehelper.PageHelper;
import org.linlinjava.ax.db.dao.AxAddressMapper;
import org.linlinjava.ax.db.domain.AxAddress;
import org.linlinjava.ax.db.domain.AxAddressExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AxAddressService {
    @Resource
    private AxAddressMapper addressMapper;

    public List<AxAddress> queryByUid(Integer uid) {
        AxAddressExample example = new AxAddressExample();
        example.or().andUserIdEqualTo(uid).andDeletedEqualTo(false);
        return addressMapper.selectByExample(example);
    }

    public AxAddress query(Integer userId, Integer id) {
        AxAddressExample example = new AxAddressExample();
        example.or().andIdEqualTo(id).andUserIdEqualTo(userId).andDeletedEqualTo(false);
        return addressMapper.selectOneByExample(example);
    }

    public int add(AxAddress address) {
        address.setAddTime(LocalDateTime.now());
        address.setUpdateTime(LocalDateTime.now());
        return addressMapper.insertSelective(address);
    }

    public int update(AxAddress address) {
        address.setUpdateTime(LocalDateTime.now());
        return addressMapper.updateByPrimaryKeySelective(address);
    }

    public void delete(Integer id) {
        addressMapper.logicalDeleteByPrimaryKey(id);
    }

    public AxAddress findDefault(Integer userId) {
        AxAddressExample example = new AxAddressExample();
        example.or().andUserIdEqualTo(userId).andIsDefaultEqualTo(true).andDeletedEqualTo(false);
        return addressMapper.selectOneByExample(example);
    }

    public void resetDefault(Integer userId) {
        AxAddress address = new AxAddress();
        address.setIsDefault(false);
        address.setUpdateTime(LocalDateTime.now());
        AxAddressExample example = new AxAddressExample();
        example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        addressMapper.updateByExampleSelective(address, example);
    }

    public List<AxAddress> querySelective(Integer userId, String name, Integer page, Integer limit, String sort, String order) {
        AxAddressExample example = new AxAddressExample();
        AxAddressExample.Criteria criteria = example.createCriteria();

        if (userId != null) {
            criteria.andUserIdEqualTo(userId);
        }
        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return addressMapper.selectByExample(example);
    }
}
