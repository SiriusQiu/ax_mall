package org.linlinjava.ax.db.service;

import org.linlinjava.ax.db.dao.AxSystemMapper;
import org.linlinjava.ax.db.domain.AxSystem;
import org.linlinjava.ax.db.domain.AxSystemExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AxSystemConfigService {
    @Resource
    private AxSystemMapper systemMapper;

    public Map<String, String> queryAll() {
        AxSystemExample example = new AxSystemExample();
        example.or().andDeletedEqualTo(false);

        List<AxSystem> systemList = systemMapper.selectByExample(example);
        Map<String, String> systemConfigs = new HashMap<>();
        for (AxSystem item : systemList) {
            systemConfigs.put(item.getKeyName(), item.getKeyValue());
        }

        return systemConfigs;
    }

    public Map<String, String> listMail() {
        AxSystemExample example = new AxSystemExample();
        example.or().andKeyNameLike("ax_mall_%").andDeletedEqualTo(false);
        List<AxSystem> systemList = systemMapper.selectByExample(example);
        Map<String, String> data = new HashMap<>();
        for(AxSystem system : systemList){
            data.put(system.getKeyName(), system.getKeyValue());
        }
        return data;
    }

    public Map<String, String> listWx() {
        AxSystemExample example = new AxSystemExample();
        example.or().andKeyNameLike("ax_wx_%").andDeletedEqualTo(false);
        List<AxSystem> systemList = systemMapper.selectByExample(example);
        Map<String, String> data = new HashMap<>();
        for(AxSystem system : systemList){
            data.put(system.getKeyName(), system.getKeyValue());
        }
        return data;
    }

    public Map<String, String> listOrder() {
        AxSystemExample example = new AxSystemExample();
        example.or().andKeyNameLike("ax_order_%").andDeletedEqualTo(false);
        List<AxSystem> systemList = systemMapper.selectByExample(example);
        Map<String, String> data = new HashMap<>();
        for(AxSystem system : systemList){
            data.put(system.getKeyName(), system.getKeyValue());
        }
        return data;
    }

    public Map<String, String> listExpress() {
        AxSystemExample example = new AxSystemExample();
        example.or().andKeyNameLike("ax_express_%").andDeletedEqualTo(false);
        List<AxSystem> systemList = systemMapper.selectByExample(example);
        Map<String, String> data = new HashMap<>();
        for(AxSystem system : systemList){
            data.put(system.getKeyName(), system.getKeyValue());
        }
        return data;
    }

    public void updateConfig(Map<String, String> data) {
        for (Map.Entry<String, String> entry : data.entrySet()) {
            AxSystemExample example = new AxSystemExample();
            example.or().andKeyNameEqualTo(entry.getKey()).andDeletedEqualTo(false);

            AxSystem system = new AxSystem();
            system.setKeyName(entry.getKey());
            system.setKeyValue(entry.getValue());
            system.setUpdateTime(LocalDateTime.now());
            systemMapper.updateByExampleSelective(system, example);
        }

    }

    public void addConfig(String key, String value) {
        AxSystem system = new AxSystem();
        system.setKeyName(key);
        system.setKeyValue(value);
        system.setAddTime(LocalDateTime.now());
        system.setUpdateTime(LocalDateTime.now());
        systemMapper.insertSelective(system);
    }
}
