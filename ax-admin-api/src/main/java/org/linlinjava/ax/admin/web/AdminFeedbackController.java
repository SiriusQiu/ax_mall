package org.linlinjava.ax.admin.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.linlinjava.ax.admin.annotation.RequiresPermissionsDesc;
import org.linlinjava.ax.core.util.ResponseUtil;
import org.linlinjava.ax.core.validator.Order;
import org.linlinjava.ax.core.validator.Sort;
import org.linlinjava.ax.db.domain.AxFeedback;
import org.linlinjava.ax.db.service.AxFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Yogeek
 * @date 2018/8/26 1:11
 */
@RestController
@RequestMapping("/admin/feedback")
@Validated
public class AdminFeedbackController {
    private final Log logger = LogFactory.getLog(AdminFeedbackController.class);

    @Autowired
    private AxFeedbackService feedbackService;

    @RequiresPermissions("admin:feedback:list")
    @RequiresPermissionsDesc(menu = {"用户管理", "意见反馈"}, button = "查询")
    @GetMapping("/list")
    public Object list(Integer userId, String username,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        List<AxFeedback> feedbackList = feedbackService.querySelective(userId, username, page, limit, sort,
                order);
        return ResponseUtil.okList(feedbackList);
    }
}
