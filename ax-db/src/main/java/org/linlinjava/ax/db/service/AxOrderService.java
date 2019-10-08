package org.linlinjava.ax.db.service;

import com.github.pagehelper.PageHelper;
import org.linlinjava.ax.db.dao.AxOrderMapper;
import org.linlinjava.ax.db.dao.OrderMapper;
import org.linlinjava.ax.db.domain.AxOrder;
import org.linlinjava.ax.db.domain.AxOrderExample;
import org.linlinjava.ax.db.util.OrderUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class AxOrderService {
    @Resource
    private AxOrderMapper axOrderMapper;
    @Resource
    private OrderMapper orderMapper;

    public int add(AxOrder order) {
        order.setAddTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        return axOrderMapper.insertSelective(order);
    }

    public int count(Integer userId) {
        AxOrderExample example = new AxOrderExample();
        example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        return (int) axOrderMapper.countByExample(example);
    }

    public AxOrder findById(Integer orderId) {
        return axOrderMapper.selectByPrimaryKey(orderId);
    }

    private String getRandomNum(Integer num) {
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < num; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public int countByOrderSn(Integer userId, String orderSn) {
        AxOrderExample example = new AxOrderExample();
        example.or().andUserIdEqualTo(userId).andOrderSnEqualTo(orderSn).andDeletedEqualTo(false);
        return (int) axOrderMapper.countByExample(example);
    }

    // TODO 这里应该产生一个唯一的订单，但是实际上这里仍然存在两个订单相同的可能性
    public String generateOrderSn(Integer userId) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
        String now = df.format(LocalDate.now());
        String orderSn = now + getRandomNum(6);
        while (countByOrderSn(userId, orderSn) != 0) {
            orderSn = now + getRandomNum(6);
        }
        return orderSn;
    }

    public List<AxOrder> queryByOrderStatus(Integer userId, List<Short> orderStatus, Integer page, Integer limit, String sort, String order) {
        AxOrderExample example = new AxOrderExample();
        example.setOrderByClause(AxOrder.Column.addTime.desc());
        AxOrderExample.Criteria criteria = example.or();
        criteria.andUserIdEqualTo(userId);
        if (orderStatus != null) {
            criteria.andOrderStatusIn(orderStatus);
        }
        criteria.andDeletedEqualTo(false);
        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return axOrderMapper.selectByExample(example);
    }

    public List<AxOrder> querySelective(Integer userId, String orderSn, List<Short> orderStatusArray, Integer page, Integer limit, String sort, String order) {
        AxOrderExample example = new AxOrderExample();
        AxOrderExample.Criteria criteria = example.createCriteria();

        if (userId != null) {
            criteria.andUserIdEqualTo(userId);
        }
        if (!StringUtils.isEmpty(orderSn)) {
            criteria.andOrderSnEqualTo(orderSn);
        }
        if (orderStatusArray != null && orderStatusArray.size() != 0) {
            criteria.andOrderStatusIn(orderStatusArray);
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return axOrderMapper.selectByExample(example);
    }

    public int updateWithOptimisticLocker(AxOrder order) {
        LocalDateTime preUpdateTime = order.getUpdateTime();
        order.setUpdateTime(LocalDateTime.now());
        return orderMapper.updateWithOptimisticLocker(preUpdateTime, order);
    }

    public void deleteById(Integer id) {
        axOrderMapper.logicalDeleteByPrimaryKey(id);
    }

    public int count() {
        AxOrderExample example = new AxOrderExample();
        example.or().andDeletedEqualTo(false);
        return (int) axOrderMapper.countByExample(example);
    }

    public List<AxOrder> queryUnpaid(int minutes) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expired = now.minusMinutes(minutes);
        AxOrderExample example = new AxOrderExample();
        example.or().andOrderStatusEqualTo(OrderUtil.STATUS_CREATE).andAddTimeLessThan(expired).andDeletedEqualTo(false);
        return axOrderMapper.selectByExample(example);
    }

    public List<AxOrder> queryUnconfirm(int days) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expired = now.minusDays(days);
        AxOrderExample example = new AxOrderExample();
        example.or().andOrderStatusEqualTo(OrderUtil.STATUS_SHIP).andShipTimeLessThan(expired).andDeletedEqualTo(false);
        return axOrderMapper.selectByExample(example);
    }

    public AxOrder findBySn(String orderSn) {
        AxOrderExample example = new AxOrderExample();
        example.or().andOrderSnEqualTo(orderSn).andDeletedEqualTo(false);
        return axOrderMapper.selectOneByExample(example);
    }

    public Map<Object, Object> orderInfo(Integer userId) {
        AxOrderExample example = new AxOrderExample();
        example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        List<AxOrder> orders = axOrderMapper.selectByExampleSelective(example, AxOrder.Column.orderStatus, AxOrder.Column.comments);

        int unpaid = 0;
        int unship = 0;
        int unrecv = 0;
        int uncomment = 0;
        for (AxOrder order : orders) {
            if (OrderUtil.isCreateStatus(order)) {
                unpaid++;
            } else if (OrderUtil.isPayStatus(order)) {
                unship++;
            } else if (OrderUtil.isShipStatus(order)) {
                unrecv++;
            } else if (OrderUtil.isConfirmStatus(order) || OrderUtil.isAutoConfirmStatus(order)) {
                uncomment += order.getComments();
            } else {
                // do nothing
            }
        }

        Map<Object, Object> orderInfo = new HashMap<Object, Object>();
        orderInfo.put("unpaid", unpaid);
        orderInfo.put("unship", unship);
        orderInfo.put("unrecv", unrecv);
        orderInfo.put("uncomment", uncomment);
        return orderInfo;

    }

    public List<AxOrder> queryComment(int days) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expired = now.minusDays(days);
        AxOrderExample example = new AxOrderExample();
        example.or().andCommentsGreaterThan((short) 0).andConfirmTimeLessThan(expired).andDeletedEqualTo(false);
        return axOrderMapper.selectByExample(example);
    }
}
