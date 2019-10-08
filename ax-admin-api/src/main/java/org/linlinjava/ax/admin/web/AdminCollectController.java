package org.linlinjava.ax.admin.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.linlinjava.ax.admin.annotation.RequiresPermissionsDesc;
import org.linlinjava.ax.core.util.ResponseUtil;
import org.linlinjava.ax.core.validator.Order;
import org.linlinjava.ax.core.validator.Sort;
import org.linlinjava.ax.db.domain.AxCollect;
import org.linlinjava.ax.db.service.AxCollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/collect")
@Validated
public class AdminCollectController {
    private final Log logger = LogFactory.getLog(AdminCollectController.class);

    @Autowired
    private AxCollectService collectService;


    @RequiresPermissions("admin:collect:list")
    @RequiresPermissionsDesc(menu = {"用户管理", "用户收藏"}, button = "查询")
    @GetMapping("/list")
    public Object list(String userId, String valueId,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        List<AxCollect> collectList = collectService.querySelective(userId, valueId, page, limit, sort, order);
        return ResponseUtil.okList(collectList);
    }
}
