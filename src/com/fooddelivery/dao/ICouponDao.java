package com.fooddelivery.dao;

import com.fooddelivery.model.Coupon;
import java.util.List;

public interface ICouponDao {
    Coupon getCouponByCode(String code);
    List<Coupon> getActiveCoupons();
}
