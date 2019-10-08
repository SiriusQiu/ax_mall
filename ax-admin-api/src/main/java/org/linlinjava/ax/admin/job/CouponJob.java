package org.linlinjava.ax.admin.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.ax.db.domain.AxCoupon;
import org.linlinjava.ax.db.domain.AxCouponUser;
import org.linlinjava.ax.db.service.AxCouponService;
import org.linlinjava.ax.db.service.AxCouponUserService;
import org.linlinjava.ax.db.util.CouponConstant;
import org.linlinjava.ax.db.util.CouponUserConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 检测优惠券过期情况
 */
@Component
public class CouponJob {
    private final Log logger = LogFactory.getLog(CouponJob.class);

    @Autowired
    private AxCouponService couponService;
    @Autowired
    private AxCouponUserService couponUserService;

    /**
     * 每隔一个小时检查
     * TODO
     * 注意，因为是相隔一个小时检查，因此导致优惠券真正超时时间可能比设定时间延迟1个小时
     */
    @Scheduled(fixedDelay = 60 * 60 * 1000)
    public void checkCouponExpired() {
        logger.info("系统开启任务检查优惠券是否已经过期");

        List<AxCoupon> couponList = couponService.queryExpired();
        for (AxCoupon coupon : couponList) {
            coupon.setStatus(CouponConstant.STATUS_EXPIRED);
            couponService.updateById(coupon);
        }

        List<AxCouponUser> couponUserList = couponUserService.queryExpired();
        for (AxCouponUser couponUser : couponUserList) {
            couponUser.setStatus(CouponUserConstant.STATUS_EXPIRED);
            couponUserService.update(couponUser);
        }
    }

}
