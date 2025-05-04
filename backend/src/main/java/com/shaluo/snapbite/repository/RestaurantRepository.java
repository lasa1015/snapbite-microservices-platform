package com.shaluo.snapbite.repository;

import com.shaluo.snapbite.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    // 根据用户前端传来的 分类（category）、价格（price） 和 餐段（meal time，比如早餐、午餐等），去筛选符合条件的餐厅。
    //  JPQL 查询语句
    // 尽量把筛选逻辑写在查询语句（JPQL / SQL）里，而不是写在 Java 层
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
