package com.shaluo.snapbite.repository.mongo;
import com.shaluo.snapbite.model.mongo.Menu;
import org.springframework.data.mongodb.repository.MongoRepository;



public interface MenuRepository extends MongoRepository<Menu, String> {
    Menu findByRestaurantId(Integer restaurantId);
}