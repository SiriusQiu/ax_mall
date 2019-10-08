package org.linlinjava.ax.db.service;

import com.github.pagehelper.PageHelper;
import org.linlinjava.ax.db.dao.AxCategoryMapper;
import org.linlinjava.ax.db.domain.AxCategory;
import org.linlinjava.ax.db.domain.AxCategoryExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AxCategoryService {
    @Resource
    private AxCategoryMapper categoryMapper;
    private AxCategory.Column[] CHANNEL = {AxCategory.Column.id, AxCategory.Column.name, AxCategory.Column.iconUrl};

    public List<AxCategory> queryL1WithoutRecommend(int offset, int limit) {
        AxCategoryExample example = new AxCategoryExample();
        example.or().andLevelEqualTo("L1").andNameNotEqualTo("推荐").andDeletedEqualTo(false);
        PageHelper.startPage(offset, limit);
        return categoryMapper.selectByExample(example);
    }

    public List<AxCategory> queryL1(int offset, int limit) {
        AxCategoryExample example = new AxCategoryExample();
        example.or().andLevelEqualTo("L1").andDeletedEqualTo(false);
        PageHelper.startPage(offset, limit);
        return categoryMapper.selectByExample(example);
    }

    public List<AxCategory> queryL1() {
        AxCategoryExample example = new AxCategoryExample();
        example.or().andLevelEqualTo("L1").andDeletedEqualTo(false);
        return categoryMapper.selectByExample(example);
    }

    public List<AxCategory> queryByPid(Integer pid) {
        AxCategoryExample example = new AxCategoryExample();
        example.or().andPidEqualTo(pid).andDeletedEqualTo(false);
        return categoryMapper.selectByExample(example);
    }

    public List<AxCategory> queryL2ByIds(List<Integer> ids) {
        AxCategoryExample example = new AxCategoryExample();
        example.or().andIdIn(ids).andLevelEqualTo("L2").andDeletedEqualTo(false);
        return categoryMapper.selectByExample(example);
    }

    public AxCategory findById(Integer id) {
        return categoryMapper.selectByPrimaryKey(id);
    }

    public List<AxCategory> querySelective(String id, String name, Integer page, Integer size, String sort, String order) {
        AxCategoryExample example = new AxCategoryExample();
        AxCategoryExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(id)) {
            criteria.andIdEqualTo(Integer.valueOf(id));
        }
        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return categoryMapper.selectByExample(example);
    }

    public int updateById(AxCategory category) {
        category.setUpdateTime(LocalDateTime.now());
        return categoryMapper.updateByPrimaryKeySelective(category);
    }

    public void deleteById(Integer id) {
        categoryMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(AxCategory category) {
        category.setAddTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        categoryMapper.insertSelective(category);
    }

    public List<AxCategory> queryChannel() {
        AxCategoryExample example = new AxCategoryExample();
        example.or().andLevelEqualTo("L1").andDeletedEqualTo(false);
        return categoryMapper.selectByExampleSelective(example, CHANNEL);
    }
}
