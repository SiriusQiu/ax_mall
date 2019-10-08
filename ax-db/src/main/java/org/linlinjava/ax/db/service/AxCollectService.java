package org.linlinjava.ax.db.service;

import com.github.pagehelper.PageHelper;
import org.linlinjava.ax.db.dao.AxCollectMapper;
import org.linlinjava.ax.db.domain.AxCollect;
import org.linlinjava.ax.db.domain.AxCollectExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AxCollectService {
    @Resource
    private AxCollectMapper collectMapper;

    public int count(int uid, Integer gid) {
        AxCollectExample example = new AxCollectExample();
        example.or().andUserIdEqualTo(uid).andValueIdEqualTo(gid).andDeletedEqualTo(false);
        return (int) collectMapper.countByExample(example);
    }

    public List<AxCollect> queryByType(Integer userId, Byte type, Integer page, Integer limit, String sort, String order) {
        AxCollectExample example = new AxCollectExample();
        AxCollectExample.Criteria criteria = example.createCriteria();

        if (type != null) {
            criteria.andTypeEqualTo(type);
        }
        criteria.andUserIdEqualTo(userId);
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return collectMapper.selectByExample(example);
    }

    public int countByType(Integer userId, Byte type) {
        AxCollectExample example = new AxCollectExample();
        example.or().andUserIdEqualTo(userId).andTypeEqualTo(type).andDeletedEqualTo(false);
        return (int) collectMapper.countByExample(example);
    }

    public AxCollect queryByTypeAndValue(Integer userId, Byte type, Integer valueId) {
        AxCollectExample example = new AxCollectExample();
        example.or().andUserIdEqualTo(userId).andValueIdEqualTo(valueId).andTypeEqualTo(type).andDeletedEqualTo(false);
        return collectMapper.selectOneByExample(example);
    }

    public void deleteById(Integer id) {
        collectMapper.logicalDeleteByPrimaryKey(id);
    }

    public int add(AxCollect collect) {
        collect.setAddTime(LocalDateTime.now());
        collect.setUpdateTime(LocalDateTime.now());
        return collectMapper.insertSelective(collect);
    }

    public List<AxCollect> querySelective(String userId, String valueId, Integer page, Integer size, String sort, String order) {
        AxCollectExample example = new AxCollectExample();
        AxCollectExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(userId)) {
            criteria.andUserIdEqualTo(Integer.valueOf(userId));
        }
        if (!StringUtils.isEmpty(valueId)) {
            criteria.andValueIdEqualTo(Integer.valueOf(valueId));
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return collectMapper.selectByExample(example);
    }
}
