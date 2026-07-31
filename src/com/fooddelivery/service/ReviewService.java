package com.fooddelivery.service;

import com.fooddelivery.dao.IReviewDao;
import com.fooddelivery.dao.ReviewDaoImpl;
import com.fooddelivery.model.Review;

import java.util.List;

public class ReviewService {
    private IReviewDao reviewDao;

    public ReviewService() {
        this.reviewDao = new ReviewDaoImpl();
    }

    public boolean leaveReview(int customerId, int restaurantId, int orderId, int rating, String comment) {
        if (rating < 1 || rating > 5) {
            System.err.println("Validation Error: Rating must be between 1 and 5.");
            return false;
        }

        Review review = new Review();
        review.setCustomerId(customerId);
        review.setRestaurantId(restaurantId);
        review.setOrderId(orderId);
        review.setRating(rating);
        review.setComment(comment);

        return reviewDao.addReview(review);
    }

    public void displayRestaurantReviews(int restaurantId) {
        List<Review> reviews = reviewDao.getReviewsByRestaurant(restaurantId);
        if (reviews.isEmpty()) {
            System.out.println("No reviews found for this restaurant.");
            return;
        }

        System.out.println("--- REVIEWS ---");
        for (Review r : reviews) {
            System.out.println(r.getRating() + "/5 Stars - " + r.getComment());
        }
    }
}
