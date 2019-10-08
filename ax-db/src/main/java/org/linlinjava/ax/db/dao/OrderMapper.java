package org.linlinjava.ax.db.dao;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.ax.db.domain.AxOrder;

import java.time.LocalDateTime;

public interface OrderMapper {
    int updateWithOptimisticLocker(@Param("lastUpdateTime") LocalDateTime lastUpdateTime, @Param("order") AxOrder order);
}