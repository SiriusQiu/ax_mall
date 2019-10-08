package org.linlinjava.ax.db.service;

import com.github.pagehelper.PageHelper;
import org.linlinjava.ax.db.dao.AxAdMapper;
import org.linlinjava.ax.db.domain.AxAd;
import org.linlinjava.ax.db.domain.AxAdExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AxAdService {
    @Resource
    private AxAdMapper adMapper;

    public List<AxAd> queryIndex() {
        AxAdExample example = new AxAdExample();
        example.or().andPositionEqualTo((byte) 1).andDeletedEqualTo(false).andEnabledEqualTo(true);
        return adMapper.selectByExample(example);
    }

    public List<AxAd> querySelective(String name, String content, Integer page, Integer limit, String sort, String order) {
        AxAdExample example = new AxAdExample();
        AxAdExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        if (!StringUtils.isEmpty(content)) {
            criteria.andContentLike("%" + content + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return adMapper.selectByExample(example);
    }

    public int updateById(AxAd ad) {
        ad.setUpdateTime(LocalDateTime.now());
        return adMapper.updateByPrimaryKeySelective(ad);
    }

    public void deleteById(Integer id) {
        adMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(AxAd ad) {
        ad.setAddTime(LocalDateTime.now());
        ad.setUpdateTime(LocalDateTime.now());
        adMapper.insertSelective(ad);
    }

    public AxAd findById(Integer id) {
        return adMapper.selectByPrimaryKey(id);
    }
}
