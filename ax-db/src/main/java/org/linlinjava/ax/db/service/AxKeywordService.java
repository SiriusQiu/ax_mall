package org.linlinjava.ax.db.service;

import com.github.pagehelper.PageHelper;
import org.linlinjava.ax.db.dao.AxKeywordMapper;
import org.linlinjava.ax.db.domain.AxKeyword;
import org.linlinjava.ax.db.domain.AxKeywordExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AxKeywordService {
    @Resource
    private AxKeywordMapper keywordsMapper;

    public AxKeyword queryDefault() {
        AxKeywordExample example = new AxKeywordExample();
        example.or().andIsDefaultEqualTo(true).andDeletedEqualTo(false);
        return keywordsMapper.selectOneByExample(example);
    }

    public List<AxKeyword> queryHots() {
        AxKeywordExample example = new AxKeywordExample();
        example.or().andIsHotEqualTo(true).andDeletedEqualTo(false);
        return keywordsMapper.selectByExample(example);
    }

    public List<AxKeyword> queryByKeyword(String keyword, Integer page, Integer limit) {
        AxKeywordExample example = new AxKeywordExample();
        example.setDistinct(true);
        example.or().andKeywordLike("%" + keyword + "%").andDeletedEqualTo(false);
        PageHelper.startPage(page, limit);
        return keywordsMapper.selectByExampleSelective(example, AxKeyword.Column.keyword);
    }

    public List<AxKeyword> querySelective(String keyword, String url, Integer page, Integer limit, String sort, String order) {
        AxKeywordExample example = new AxKeywordExample();
        AxKeywordExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(keyword)) {
            criteria.andKeywordLike("%" + keyword + "%");
        }
        if (!StringUtils.isEmpty(url)) {
            criteria.andUrlLike("%" + url + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return keywordsMapper.selectByExample(example);
    }

    public void add(AxKeyword keywords) {
        keywords.setAddTime(LocalDateTime.now());
        keywords.setUpdateTime(LocalDateTime.now());
        keywordsMapper.insertSelective(keywords);
    }

    public AxKeyword findById(Integer id) {
        return keywordsMapper.selectByPrimaryKey(id);
    }

    public int updateById(AxKeyword keywords) {
        keywords.setUpdateTime(LocalDateTime.now());
        return keywordsMapper.updateByPrimaryKeySelective(keywords);
    }

    public void deleteById(Integer id) {
        keywordsMapper.logicalDeleteByPrimaryKey(id);
    }
}
