package com.example.domain.review

import com.example.domain.utils.Resource

interface ReviewRepository {
    suspend fun getAllReviews(): Resource<List<Review>>

    suspend fun getReviewsByUser(userId: String): Resource<List<Review>>

    suspend fun getReviewsByRestaurant(restaurantId: String): Resource<List<Review>>

    suspend fun createReview(
        userId: Int,
        restaurantId: Int,
        idRestaurant: Int? = null,
        idUser: Int? = null,
        review: String,
        rating: Int,
    ): Resource<String>

    suspend fun updateReview(
        userId: Int,
        restaurantId: Int,
        reviewId: String,
        idRestaurant: Int? = null,
        idUser: Int? = null,
        review: String? = null,
        rating: Int? = null,
    ): Resource<Review>

    suspend fun deleteReview(reviewId: String): Resource<String>
}