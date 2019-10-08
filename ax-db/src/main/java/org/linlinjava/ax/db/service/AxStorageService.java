package org.linlinjava.ax.db.service;

import com.github.pagehelper.PageHelper;
import org.linlinjava.ax.db.dao.AxStorageMapper;
import org.linlinjava.ax.db.domain.AxStorage;
import org.linlinjava.ax.db.domain.AxStorageExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AxStorageService {
    @Autowired
    private AxStorageMapper storageMapper;

    public void deleteByKey(String key) {
        AxStorageExample example = new AxStorageExample();
        example.or().andKeyEqualTo(key);
        storageMapper.logicalDeleteByExample(example);
    }

    public void add(AxStorage storageInfo) {
        storageInfo.setAddTime(LocalDateTime.now());
        storageInfo.setUpdateTime(LocalDateTime.now());
        storageMapper.insertSelective(storageInfo);
    }

    public AxStorage findByKey(String key) {
        AxStorageExample example = new AxStorageExample();
        example.or().andKeyEqualTo(key).andDeletedEqualTo(false);
        return storageMapper.selectOneByExample(example);
    }

    public int update(AxStorage storageInfo) {
        storageInfo.setUpdateTime(LocalDateTime.now());
        return storageMapper.updateByPrimaryKeySelective(storageInfo);
    }

    public AxStorage findById(Integer id) {
        return storageMapper.selectByPrimaryKey(id);
    }

    public List<AxStorage> querySelective(String key, String name, Integer page, Integer limit, String sort, String order) {
        AxStorageExample example = new AxStorageExample();
        AxStorageExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(key)) {
            criteria.andKeyEqualTo(key);
        }
        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return storageMapper.selectByExample(example);
    }
}
