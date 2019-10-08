package org.linlinjava.ax.db.service;

import com.github.pagehelper.PageHelper;
import org.linlinjava.ax.db.dao.AxRegionMapper;
import org.linlinjava.ax.db.domain.AxRegion;
import org.linlinjava.ax.db.domain.AxRegionExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AxRegionService {

    @Resource
    private AxRegionMapper regionMapper;

    public List<AxRegion> getAll(){
        AxRegionExample example = new AxRegionExample();
        byte b = 4;
        example.or().andTypeNotEqualTo(b);
        return regionMapper.selectByExample(example);
    }

    public List<AxRegion> queryByPid(Integer parentId) {
        AxRegionExample example = new AxRegionExample();
        example.or().andPidEqualTo(parentId);
        return regionMapper.selectByExample(example);
    }

    public AxRegion findById(Integer id) {
        return regionMapper.selectByPrimaryKey(id);
    }

    public List<AxRegion> querySelective(String name, Integer code, Integer page, Integer size, String sort, String order) {
        AxRegionExample example = new AxRegionExample();
        AxRegionExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        if (!StringUtils.isEmpty(code)) {
            criteria.andCodeEqualTo(code);
        }

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return regionMapper.selectByExample(example);
    }

}
