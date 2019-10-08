package org.linlinjava.ax.db.service;

import com.github.pagehelper.PageHelper;
import org.linlinjava.ax.db.dao.AxIssueMapper;
import org.linlinjava.ax.db.domain.AxIssue;
import org.linlinjava.ax.db.domain.AxIssueExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AxIssueService {
    @Resource
    private AxIssueMapper issueMapper;

    public void deleteById(Integer id) {
        issueMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(AxIssue issue) {
        issue.setAddTime(LocalDateTime.now());
        issue.setUpdateTime(LocalDateTime.now());
        issueMapper.insertSelective(issue);
    }

    public List<AxIssue> querySelective(String question, Integer page, Integer limit, String sort, String order) {
        AxIssueExample example = new AxIssueExample();
        AxIssueExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(question)) {
            criteria.andQuestionLike("%" + question + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return issueMapper.selectByExample(example);
    }

    public int updateById(AxIssue issue) {
        issue.setUpdateTime(LocalDateTime.now());
        return issueMapper.updateByPrimaryKeySelective(issue);
    }

    public AxIssue findById(Integer id) {
        return issueMapper.selectByPrimaryKey(id);
    }
}
