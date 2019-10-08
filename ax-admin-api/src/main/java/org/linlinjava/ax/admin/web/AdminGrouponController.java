package org.linlinjava.ax.admin.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.linlinjava.ax.admin.annotation.RequiresPermissionsDesc;
import org.linlinjava.ax.core.util.ResponseUtil;
import org.linlinjava.ax.core.validator.Order;
import org.linlinjava.ax.core.validator.Sort;
import org.linlinjava.ax.db.domain.AxGoods;
import org.linlinjava.ax.db.domain.AxGroupon;
import org.linlinjava.ax.db.domain.AxGrouponRules;
import org.linlinjava.ax.db.service.AxGoodsService;
import org.linlinjava.ax.db.service.AxGrouponRulesService;
import org.linlinjava.ax.db.service.AxGrouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/groupon")
@Validated
public class AdminGrouponController {
    private final Log logger = LogFactory.getLog(AdminGrouponController.class);

    @Autowired
    private AxGrouponRulesService rulesService;
    @Autowired
    private AxGoodsService goodsService;
    @Autowired
    private AxGrouponService grouponService;

    @RequiresPermissions("admin:groupon:read")
    @RequiresPermissionsDesc(menu = {"推广管理", "团购管理"}, button = "详情")
    @GetMapping("/listRecord")
    public Object listRecord(String grouponId,
                             @RequestParam(defaultValue = "1") Integer page,
                             @RequestParam(defaultValue = "10") Integer limit,
                             @Sort @RequestParam(defaultValue = "add_time") String sort,
                             @Order @RequestParam(defaultValue = "desc") String order) {
        List<AxGroupon> grouponList = grouponService.querySelective(grouponId, page, limit, sort, order);

        List<Map<String, Object>> groupons = new ArrayList<>();
        for (AxGroupon groupon : grouponList) {
            try {
                Map<String, Object> recordData = new HashMap<>();
                List<AxGroupon> subGrouponList = grouponService.queryJoinRecord(groupon.getId());
                AxGrouponRules rules = rulesService.queryById(groupon.getRulesId());
                AxGoods goods = goodsService.findById(rules.getGoodsId());

                recordData.put("groupon", groupon);
                recordData.put("subGroupons", subGrouponList);
                recordData.put("rules", rules);
                recordData.put("goods", goods);

                groupons.add(recordData);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        return ResponseUtil.okList(groupons, grouponList);
    }

    @RequiresPermissions("admin:groupon:list")
    @RequiresPermissionsDesc(menu = {"推广管理", "团购管理"}, button = "查询")
    @GetMapping("/list")
    public Object list(String goodsId,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        List<AxGrouponRules> rulesList = rulesService.querySelective(goodsId, page, limit, sort, order);
        return ResponseUtil.okList(rulesList);
    }

    private Object validate(AxGrouponRules grouponRules) {
        Integer goodsId = grouponRules.getGoodsId();
        if (goodsId == null) {
            return ResponseUtil.badArgument();
        }
        BigDecimal discount = grouponRules.getDiscount();
        if (discount == null) {
            return ResponseUtil.badArgument();
        }
        Integer discountMember = grouponRules.getDiscountMember();
        if (discountMember == null) {
            return ResponseUtil.badArgument();
        }
        LocalDateTime expireTime = grouponRules.getExpireTime();
        if (expireTime == null) {
            return ResponseUtil.badArgument();
        }

        return null;
    }

    @RequiresPermissions("admin:groupon:update")
    @RequiresPermissionsDesc(menu = {"推广管理", "团购管理"}, button = "编辑")
    @PostMapping("/update")
    public Object update(@RequestBody AxGrouponRules grouponRules) {
        Object error = validate(grouponRules);
        if (error != null) {
            return error;
        }

        Integer goodsId = grouponRules.getGoodsId();
        AxGoods goods = goodsService.findById(goodsId);
        if (goods == null) {
            return ResponseUtil.badArgumentValue();
        }

        grouponRules.setGoodsName(goods.getName());
        grouponRules.setPicUrl(goods.getPicUrl());

        if (rulesService.updateById(grouponRules) == 0) {
            return ResponseUtil.updatedDataFailed();
        }

        return ResponseUtil.ok();
    }

    @RequiresPermissions("admin:groupon:create")
    @RequiresPermissionsDesc(menu = {"推广管理", "团购管理"}, button = "添加")
    @PostMapping("/create")
    public Object create(@RequestBody AxGrouponRules grouponRules) {
        Object error = validate(grouponRules);
        if (error != null) {
            return error;
        }

        Integer goodsId = grouponRules.getGoodsId();
        AxGoods goods = goodsService.findById(goodsId);
        if (goods == null) {
            return ResponseUtil.badArgumentValue();
        }

        grouponRules.setGoodsName(goods.getName());
        grouponRules.setPicUrl(goods.getPicUrl());

        rulesService.createRules(grouponRules);

        return ResponseUtil.ok(grouponRules);
    }

    @RequiresPermissions("admin:groupon:delete")
    @RequiresPermissionsDesc(menu = {"推广管理", "团购管理"}, button = "删除")
    @PostMapping("/delete")
    public Object delete(@RequestBody AxGrouponRules grouponRules) {
        Integer id = grouponRules.getId();
        if (id == null) {
            return ResponseUtil.badArgument();
        }

        rulesService.delete(id);
        return ResponseUtil.ok();
    }
}
