package com.shaluo.snapbite.repository.mongo;
import com.shaluo.snapbite.model.mongo.Menu;
import org.springframework.data.mongodb.repository.MongoRepository;


// 这是一个操作 MongoDB 的 Repository
// 操作 MongoDB 中的 Menu 实体，实体主键 id 是 String 类型
// MongoRepository 是 Spring Data Mongo 提供的一个接口，只要继承了就会有很多可以用的方法！
public interface MenuRepository extends MongoRepository<Menu, String> {

    // 根据 餐厅id 找到餐厅实体
    Menu findByRestaurantId(Integer restaurantId);
}