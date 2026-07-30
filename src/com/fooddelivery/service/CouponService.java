package com.fooddelivery.service;

import com.fooddelivery.dao.ICouponDao;
import com.fooddelivery.dao.CouponDaoImpl;
import com.fooddelivery.model.Coupon;

public class CouponService {
    private ICouponDao couponDao;

    public CouponService() {
        this.couponDao = new CouponDaoImpl();
    }

    public Coupon validateAndGetCoupon(String code, double orderTotal) {
        if (code == null || code.trim().isEmpty()) {
            return null; // No coupon applied
        }
        
        Coupon coupon = couponDao.getCouponByCode(code.toUpperCase());
        if (coupon == null) {
            System.out.println("Invalid or expired coupon.");
            return null;
        }
        
        if (orderTotal < coupon.getMinOrderAmount()) {
            System.out.println("Order total must be at least " + coupon.getMinOrderAmount() + " to use this coupon.");
            return null;
        }
        
        return coupon;
    }
    
    public double calculateDiscount(Coupon coupon, double orderTotal) {
        if (coupon == null) return 0.0;
        double discount = orderTotal * (coupon.getDiscountPercentage() / 100.0);
        if (discount > coupon.getMaxDiscountAmount()) {
            return coupon.getMaxDiscountAmount();
        }
        return discount;
    }

    public void displayActiveCoupons() {
        java.util.List<Coupon> coupons = couponDao.getActiveCoupons();
        if (coupons.isEmpty()) return; // Don't show anything if no coupons are active
        
        System.out.println("--- AVAILABLE COUPONS ---");
        for (Coupon c : coupons) {
            System.out.println("[" + c.getCode() + "] - " + c.getDiscountPercentage() + "% off (Up to " + c.getMaxDiscountAmount() + " INR) - Min Order: " + c.getMinOrderAmount() + " INR");
        }
    }
}
