package org.linlinjava.ax.db.service;

import com.github.pagehelper.PageHelper;
import org.linlinjava.ax.db.dao.AxSearchHistoryMapper;
import org.linlinjava.ax.db.domain.AxSearchHistory;
import org.linlinjava.ax.db.domain.AxSearchHistoryExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AxSearchHistoryService {
    @Resource
    private AxSearchHistoryMapper searchHistoryMapper;

    public void save(AxSearchHistory searchHistory) {
        searchHistory.setAddTime(LocalDateTime.now());
        searchHistory.setUpdateTime(LocalDateTime.now());
        searchHistoryMapper.insertSelective(searchHistory);
    }

    public List<AxSearchHistory> queryByUid(int uid) {
        AxSearchHistoryExample example = new AxSearchHistoryExample();
        example.or().andUserIdEqualTo(uid).andDeletedEqualTo(false);
        example.setDistinct(true);
        return searchHistoryMapper.selectByExampleSelective(example, AxSearchHistory.Column.keyword);
    }

    public void deleteByUid(int uid) {
        AxSearchHistoryExample example = new AxSearchHistoryExample();
        example.or().andUserIdEqualTo(uid);
        searchHistoryMapper.logicalDeleteByExample(example);
    }

    public List<AxSearchHistory> querySelective(String userId, String keyword, Integer page, Integer size, String sort, String order) {
        AxSearchHistoryExample example = new AxSearchHistoryExample();
        AxSearchHistoryExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(userId)) {
            criteria.andUserIdEqualTo(Integer.valueOf(userId));
        }
        if (!StringUtils.isEmpty(keyword)) {
            criteria.andKeywordLike("%" + keyword + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return searchHistoryMapper.selectByExample(example);
    }
}
