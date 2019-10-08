package org.linlinjava.ax.admin.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.linlinjava.ax.admin.annotation.RequiresPermissionsDesc;
import org.linlinjava.ax.core.util.ResponseUtil;
import org.linlinjava.ax.core.validator.Order;
import org.linlinjava.ax.core.validator.Sort;
import org.linlinjava.ax.db.domain.AxComment;
import org.linlinjava.ax.db.service.AxCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/comment")
@Validated
public class AdminCommentController {
    private final Log logger = LogFactory.getLog(AdminCommentController.class);

    @Autowired
    private AxCommentService commentService;

    @RequiresPermissions("admin:comment:list")
    @RequiresPermissionsDesc(menu = {"商品管理", "评论管理"}, button = "查询")
    @GetMapping("/list")
    public Object list(String userId, String valueId,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        List<AxComment> commentList = commentService.querySelective(userId, valueId, page, limit, sort, order);
        return ResponseUtil.okList(commentList);
    }

    @RequiresPermissions("admin:comment:delete")
    @RequiresPermissionsDesc(menu = {"商品管理", "评论管理"}, button = "删除")
    @PostMapping("/delete")
    public Object delete(@RequestBody AxComment comment) {
        Integer id = comment.getId();
        if (id == null) {
            return ResponseUtil.badArgument();
        }
        commentService.deleteById(id);
        return ResponseUtil.ok();
    }

}
