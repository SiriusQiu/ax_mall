package org.linlinjava.ax.db.service;

import org.linlinjava.ax.db.dao.AxPermissionMapper;
import org.linlinjava.ax.db.dao.AxRoleMapper;
import org.linlinjava.ax.db.domain.AxPermission;
import org.linlinjava.ax.db.domain.AxPermissionExample;
import org.linlinjava.ax.db.domain.AxRole;
import org.linlinjava.ax.db.domain.AxRoleExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AxPermissionService {
    @Resource
    private AxPermissionMapper permissionMapper;

    public Set<String> queryByRoleIds(Integer[] roleIds) {
        Set<String> permissions = new HashSet<String>();
        if(roleIds.length == 0){
            return permissions;
        }

        AxPermissionExample example = new AxPermissionExample();
        example.or().andRoleIdIn(Arrays.asList(roleIds)).andDeletedEqualTo(false);
        List<AxPermission> permissionList = permissionMapper.selectByExample(example);

        for(AxPermission permission : permissionList){
            permissions.add(permission.getPermission());
        }

        return permissions;
    }


    public Set<String> queryByRoleId(Integer roleId) {
        Set<String> permissions = new HashSet<String>();
        if(roleId == null){
            return permissions;
        }

        AxPermissionExample example = new AxPermissionExample();
        example.or().andRoleIdEqualTo(roleId).andDeletedEqualTo(false);
        List<AxPermission> permissionList = permissionMapper.selectByExample(example);

        for(AxPermission permission : permissionList){
            permissions.add(permission.getPermission());
        }

        return permissions;
    }

    public boolean checkSuperPermission(Integer roleId) {
        if(roleId == null){
            return false;
        }

        AxPermissionExample example = new AxPermissionExample();
        example.or().andRoleIdEqualTo(roleId).andPermissionEqualTo("*").andDeletedEqualTo(false);
        return permissionMapper.countByExample(example) != 0;
    }

    public void deleteByRoleId(Integer roleId) {
        AxPermissionExample example = new AxPermissionExample();
        example.or().andRoleIdEqualTo(roleId).andDeletedEqualTo(false);
        permissionMapper.logicalDeleteByExample(example);
    }

    public void add(AxPermission axPermission) {
        axPermission.setAddTime(LocalDateTime.now());
        axPermission.setUpdateTime(LocalDateTime.now());
        permissionMapper.insertSelective(axPermission);
    }
}
