package org.linlinjava.ax.db.service;

import com.github.pagehelper.PageHelper;
import org.linlinjava.ax.db.dao.AxBrandMapper;
import org.linlinjava.ax.db.domain.AxBrand;
import org.linlinjava.ax.db.domain.AxBrand.Column;
import org.linlinjava.ax.db.domain.AxBrandExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AxBrandService {
    @Resource
    private AxBrandMapper brandMapper;
    private Column[] columns = new Column[]{Column.id, Column.name, Column.desc, Column.picUrl, Column.floorPrice};

    public List<AxBrand> query(Integer page, Integer limit, String sort, String order) {
        AxBrandExample example = new AxBrandExample();
        example.or().andDeletedEqualTo(false);
        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }
        PageHelper.startPage(page, limit);
        return brandMapper.selectByExampleSelective(example, columns);
    }

    public List<AxBrand> query(Integer page, Integer limit) {
        return query(page, limit, null, null);
    }

    public AxBrand findById(Integer id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    public List<AxBrand> querySelective(String id, String name, Integer page, Integer size, String sort, String order) {
        AxBrandExample example = new AxBrandExample();
        AxBrandExample.Criteria criteria = example.createCriteria();

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
        return brandMapper.selectByExample(example);
    }

    public int updateById(AxBrand brand) {
        brand.setUpdateTime(LocalDateTime.now());
        return brandMapper.updateByPrimaryKeySelective(brand);
    }

    public void deleteById(Integer id) {
        brandMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(AxBrand brand) {
        brand.setAddTime(LocalDateTime.now());
        brand.setUpdateTime(LocalDateTime.now());
        brandMapper.insertSelective(brand);
    }

    public List<AxBrand> all() {
        AxBrandExample example = new AxBrandExample();
        example.or().andDeletedEqualTo(false);
        return brandMapper.selectByExample(example);
    }
}
