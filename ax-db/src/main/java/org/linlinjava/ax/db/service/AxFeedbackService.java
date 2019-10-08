package org.linlinjava.ax.db.service;

import com.github.pagehelper.PageHelper;
import org.linlinjava.ax.db.dao.AxFeedbackMapper;
import org.linlinjava.ax.db.domain.AxFeedback;
import org.linlinjava.ax.db.domain.AxFeedbackExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Yogeek
 * @date 2018/8/27 11:39
 */
@Service
public class AxFeedbackService {
    @Autowired
    private AxFeedbackMapper feedbackMapper;

    public Integer add(AxFeedback feedback) {
        feedback.setAddTime(LocalDateTime.now());
        feedback.setUpdateTime(LocalDateTime.now());
        return feedbackMapper.insertSelective(feedback);
    }

    public List<AxFeedback> querySelective(Integer userId, String username, Integer page, Integer limit, String sort, String order) {
        AxFeedbackExample example = new AxFeedbackExample();
        AxFeedbackExample.Criteria criteria = example.createCriteria();

        if (userId != null) {
            criteria.andUserIdEqualTo(userId);
        }
        if (!StringUtils.isEmpty(username)) {
            criteria.andUsernameLike("%" + username + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return feedbackMapper.selectByExample(example);
    }
}
