package com.shaluo.snapbite.repository;

import com.shaluo.snapbite.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query("""
        SELECT r FROM Restaurant r
        WHERE (:categories IS NULL OR r.category IN :categories)
          AND (:prices IS NULL OR r.price IN :prices)
          AND (
              :mealFiltersEmpty = TRUE OR
              (r.goodForBreakfast = TRUE AND 'breakfast' IN :meals) OR
              (r.goodForLunch = TRUE AND 'lunch' IN :meals) OR
              (r.goodForDinner = TRUE AND 'dinner' IN :meals) OR
              (r.goodForBrunch = TRUE AND 'brunch' IN :meals)
          )
    """)
    List<Restaurant> filterRestaurants(
            @Param("categories") List<String> categories,
            @Param("prices") List<String> prices,
            @Param("meals") List<String> meals,
            @Param("mealFiltersEmpty") boolean mealFiltersEmpty
    );
}
