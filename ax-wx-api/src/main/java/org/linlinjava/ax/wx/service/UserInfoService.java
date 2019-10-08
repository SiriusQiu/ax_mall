package org.linlinjava.ax.wx.service;

import org.linlinjava.ax.db.domain.AxUser;
import org.linlinjava.ax.db.service.AxUserService;
import org.linlinjava.ax.wx.dto.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class UserInfoService {
    @Autowired
    private AxUserService userService;


    public UserInfo getInfo(Integer userId) {
        AxUser user = userService.findById(userId);
        Assert.state(user != null, "用户不存在");
        UserInfo userInfo = new UserInfo();
        userInfo.setNickName(user.getNickname());
        userInfo.setAvatarUrl(user.getAvatar());
        return userInfo;
    }
}
