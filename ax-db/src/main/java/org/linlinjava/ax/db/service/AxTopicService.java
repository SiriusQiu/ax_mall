package org.linlinjava.ax.db.service;

import com.github.pagehelper.PageHelper;
import org.linlinjava.ax.db.dao.AxTopicMapper;
import org.linlinjava.ax.db.domain.AxTopic;
import org.linlinjava.ax.db.domain.AxTopic.Column;
import org.linlinjava.ax.db.domain.AxTopicExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AxTopicService {
    @Resource
    private AxTopicMapper topicMapper;
    private Column[] columns = new Column[]{Column.id, Column.title, Column.subtitle, Column.price, Column.picUrl, Column.readCount};

    public List<AxTopic> queryList(int offset, int limit) {
        return queryList(offset, limit, "add_time", "desc");
    }

    public List<AxTopic> queryList(int offset, int limit, String sort, String order) {
        AxTopicExample example = new AxTopicExample();
        example.or().andDeletedEqualTo(false);
        example.setOrderByClause(sort + " " + order);
        PageHelper.startPage(offset, limit);
        return topicMapper.selectByExampleSelective(example, columns);
    }

    public int queryTotal() {
        AxTopicExample example = new AxTopicExample();
        example.or().andDeletedEqualTo(false);
        return (int) topicMapper.countByExample(example);
    }

    public AxTopic findById(Integer id) {
        AxTopicExample example = new AxTopicExample();
        example.or().andIdEqualTo(id).andDeletedEqualTo(false);
        return topicMapper.selectOneByExampleWithBLOBs(example);
    }

    public List<AxTopic> queryRelatedList(Integer id, int offset, int limit) {
        AxTopicExample example = new AxTopicExample();
        example.or().andIdEqualTo(id).andDeletedEqualTo(false);
        List<AxTopic> topics = topicMapper.selectByExample(example);
        if (topics.size() == 0) {
            return queryList(offset, limit, "add_time", "desc");
        }
        AxTopic topic = topics.get(0);

        example = new AxTopicExample();
        example.or().andIdNotEqualTo(topic.getId()).andDeletedEqualTo(false);
        PageHelper.startPage(offset, limit);
        List<AxTopic> relateds = topicMapper.selectByExampleWithBLOBs(example);
        if (relateds.size() != 0) {
            return relateds;
        }

        return queryList(offset, limit, "add_time", "desc");
    }

    public List<AxTopic> querySelective(String title, String subtitle, Integer page, Integer limit, String sort, String order) {
        AxTopicExample example = new AxTopicExample();
        AxTopicExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(title)) {
            criteria.andTitleLike("%" + title + "%");
        }
        if (!StringUtils.isEmpty(subtitle)) {
            criteria.andSubtitleLike("%" + subtitle + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return topicMapper.selectByExampleWithBLOBs(example);
    }

    public int updateById(AxTopic topic) {
        topic.setUpdateTime(LocalDateTime.now());
        AxTopicExample example = new AxTopicExample();
        example.or().andIdEqualTo(topic.getId());
        return topicMapper.updateByExampleSelective(topic, example);
    }

    public void deleteById(Integer id) {
        topicMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(AxTopic topic) {
        topic.setAddTime(LocalDateTime.now());
        topic.setUpdateTime(LocalDateTime.now());
        topicMapper.insertSelective(topic);
    }


}
