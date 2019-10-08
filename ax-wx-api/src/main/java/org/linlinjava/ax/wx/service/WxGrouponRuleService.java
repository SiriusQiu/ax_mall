package org.linlinjava.ax.wx.service;

import com.github.pagehelper.Page;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.ax.db.domain.AxGoods;
import org.linlinjava.ax.db.domain.AxGrouponRules;
import org.linlinjava.ax.db.service.AxGoodsService;
import org.linlinjava.ax.db.service.AxGrouponRulesService;
import org.linlinjava.ax.db.service.AxGrouponService;
import org.linlinjava.ax.wx.vo.GrouponRuleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WxGrouponRuleService {
    private final Log logger = LogFactory.getLog(WxGrouponRuleService.class);

    @Autowired
    private AxGrouponRulesService grouponRulesService;
    @Autowired
    private AxGrouponService grouponService;
    @Autowired
    private AxGoodsService goodsService;


    public List<GrouponRuleVo> queryList(Integer page, Integer size) {
        return queryList(page, size, "add_time", "desc");
    }


    public List<GrouponRuleVo> queryList(Integer page, Integer size, String sort, String order) {
        Page<AxGrouponRules> grouponRulesList = (Page)grouponRulesService.queryList(page, size, sort, order);

        Page<GrouponRuleVo> grouponList = new Page<GrouponRuleVo>();
        grouponList.setPages(grouponRulesList.getPages());
        grouponList.setPageNum(grouponRulesList.getPageNum());
        grouponList.setPageSize(grouponRulesList.getPageSize());
        grouponList.setTotal(grouponRulesList.getTotal());

        for (AxGrouponRules rule : grouponRulesList) {
            Integer goodsId = rule.getGoodsId();
            AxGoods goods = goodsService.findById(goodsId);
            if (goods == null)
                continue;

            GrouponRuleVo grouponRuleVo = new GrouponRuleVo();
            grouponRuleVo.setId(goods.getId());
            grouponRuleVo.setName(goods.getName());
            grouponRuleVo.setBrief(goods.getBrief());
            grouponRuleVo.setPicUrl(goods.getPicUrl());
            grouponRuleVo.setCounterPrice(goods.getCounterPrice());
            grouponRuleVo.setRetailPrice(goods.getRetailPrice());
            grouponRuleVo.setGrouponPrice(goods.getRetailPrice().subtract(rule.getDiscount()));
            grouponRuleVo.setGrouponDiscount(rule.getDiscount());
            grouponRuleVo.setGrouponMember(rule.getDiscountMember());
            grouponList.add(grouponRuleVo);
        }

        return grouponList;
    }
}