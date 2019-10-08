package org.linlinjava.ax.wx.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.ax.core.util.ResponseUtil;
import org.linlinjava.ax.db.domain.AxUser;
import org.linlinjava.ax.db.domain.AxUserFormid;
import org.linlinjava.ax.db.service.AxUserFormIdService;
import org.linlinjava.ax.db.service.AxUserService;
import org.linlinjava.ax.wx.annotation.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/ax/formid")
@Validated
public class WxUserFormId {
    private final Log logger = LogFactory.getLog(WxUserFormId.class);

    @Autowired
    private AxUserService userService;

    @Autowired
    private AxUserFormIdService formIdService;

    @GetMapping("create")
    public Object create(@LoginUser Integer userId, @NotNull String formId) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }

        AxUser user = userService.findById(userId);
        AxUserFormid userFormid = new AxUserFormid();
        userFormid.setOpenid(user.getWeixinOpenid());
        userFormid.setFormid(formId);
        userFormid.setIsprepay(false);
        userFormid.setUseamount(1);
        userFormid.setExpireTime(LocalDateTime.now().plusDays(7));
        formIdService.addUserFormid(userFormid);

        return ResponseUtil.ok();
    }
}
