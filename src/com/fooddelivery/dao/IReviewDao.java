package com.fooddelivery.dao;

import com.fooddelivery.model.Review;
import java.util.List;

public interface IReviewDao {
    boolean addReview(Review review);
    List<Review> getReviewsByRestaurant(int restaurantId);
}
