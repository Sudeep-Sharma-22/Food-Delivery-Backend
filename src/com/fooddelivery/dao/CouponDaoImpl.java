package com.fooddelivery.dao;

import com.fooddelivery.model.Coupon;
import com.fooddelivery.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CouponDaoImpl implements ICouponDao {
    @Override
    public Coupon getCouponByCode(String code) {
        String query = "SELECT * FROM coupons WHERE code = ? AND is_active = TRUE AND current_usage < max_usage";
        Coupon coupon = null;
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, code);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                coupon = new Coupon();
                coupon.setCouponId(rs.getInt("coupon_id"));
                coupon.setCode(rs.getString("code"));
                coupon.setDiscountPercentage(rs.getDouble("discount_percentage"));
                coupon.setMaxDiscountAmount(rs.getDouble("max_discount_amount"));
                coupon.setMinOrderAmount(rs.getDouble("min_order_amount"));
                coupon.setValidFrom(rs.getDate("valid_from"));
                coupon.setValidUntil(rs.getDate("valid_until"));
                coupon.setMaxUsage(rs.getInt("max_usage"));
                coupon.setCurrentUsage(rs.getInt("current_usage"));
                coupon.setActive(rs.getBoolean("is_active"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching coupon: " + e.getMessage());
        }

        return coupon;
    }

    @Override
    public java.util.List<Coupon> getActiveCoupons() {
        java.util.List<Coupon> coupons = new java.util.ArrayList<>();
        String query = "SELECT * FROM coupons WHERE is_active = TRUE AND current_usage < max_usage";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Coupon coupon = new Coupon();
                coupon.setCouponId(rs.getInt("coupon_id"));
                coupon.setCode(rs.getString("code"));
                coupon.setDiscountPercentage(rs.getDouble("discount_percentage"));
                coupon.setMaxDiscountAmount(rs.getDouble("max_discount_amount"));
                coupon.setMinOrderAmount(rs.getDouble("min_order_amount"));
                coupon.setValidFrom(rs.getDate("valid_from"));
                coupon.setValidUntil(rs.getDate("valid_until"));
                coupon.setMaxUsage(rs.getInt("max_usage"));
                coupon.setCurrentUsage(rs.getInt("current_usage"));
                coupon.setActive(rs.getBoolean("is_active"));
                coupons.add(coupon);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching active coupons: " + e.getMessage());
        }

        return coupons;
    }
}
