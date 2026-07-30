package com.fooddelivery.model;

import java.sql.Date;

public class Coupon {
    private int couponId;
    private String code;
    private double discountPercentage;
    private double maxDiscountAmount;
    private double minOrderAmount;
    private Date validFrom;
    private Date validUntil;
    private int maxUsage;
    private int currentUsage;
    private boolean isActive;

    public Coupon() {}

    public int getCouponId() { return couponId; }
    public void setCouponId(int couponId) { this.couponId = couponId; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public double getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(double discountPercentage) { this.discountPercentage = discountPercentage; }

    public double getMaxDiscountAmount() { return maxDiscountAmount; }
    public void setMaxDiscountAmount(double maxDiscountAmount) { this.maxDiscountAmount = maxDiscountAmount; }

    public double getMinOrderAmount() { return minOrderAmount; }
    public void setMinOrderAmount(double minOrderAmount) { this.minOrderAmount = minOrderAmount; }

    public Date getValidFrom() { return validFrom; }
    public void setValidFrom(Date validFrom) { this.validFrom = validFrom; }

    public Date getValidUntil() { return validUntil; }
    public void setValidUntil(Date validUntil) { this.validUntil = validUntil; }

    public int getMaxUsage() { return maxUsage; }
    public void setMaxUsage(int maxUsage) { this.maxUsage = maxUsage; }

    public int getCurrentUsage() { return currentUsage; }
    public void setCurrentUsage(int currentUsage) { this.currentUsage = currentUsage; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    @Override
    public String toString() {
        return "Coupon{" +
                "code='" + code + '\'' +
                ", discount=" + discountPercentage + "%" +
                '}';
    }
}
