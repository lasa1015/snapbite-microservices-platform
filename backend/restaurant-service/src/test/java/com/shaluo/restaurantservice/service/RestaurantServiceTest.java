package com.shaluo.restaurantservice.service;

import com.shaluo.restaurantservice.dto.request.RestaurantFilterRequest;
import com.shaluo.restaurantservice.dto.response.RestaurantResponse;
import com.shaluo.restaurantservice.model.postgres.Restaurant;
import com.shaluo.restaurantservice.repository.postgres.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestaurantServiceTest {

    private RestaurantRepository restaurantRepository;
    private RestaurantService restaurantService;

    @BeforeEach
    void setUp() {
        restaurantRepository = mock(RestaurantRepository.class);
        // 推荐用构造函数注入
        restaurantService = new RestaurantService(restaurantRepository);
    }

    @Test
    void testGetAllRestaurants() {
        // 构造全英文假数据，所有字段与你的实体类型顺序严格对应
        Restaurant r1 = buildRestaurant(
                1L, "Wok House", "https://yelp.com/wok-house", "wokhouse.jpg",
                "Chinese", 4.7, "$$", 53.3498, -6.2603, "+353-123-4567",
                "10 Main St, Dublin",
                "08:00-22:00","08:00-22:00","08:00-22:00","08:00-22:00","08:00-22:00","10:00-23:00","10:00-23:00",
                true, false, true, true, "Authentic Sichuan cuisine", 1001L
        );
        Restaurant r2 = buildRestaurant(
                2L, "Seoul Kitchen", "https://yelp.com/seoul-kitchen", "seoul.jpg",
                "Korean", 4.4, "$$", 53.3501, -6.2652, "+353-234-5678",
                "22 Abbey St, Dublin",
                "09:00-21:00","09:00-21:00","09:00-21:00","09:00-21:00","09:00-21:00","10:00-22:00","10:00-22:00",
                true, true, true, true, "Traditional Korean BBQ", 1002L
        );
        List<Restaurant> list = Arrays.asList(r1, r2);

        when(restaurantRepository.findAll()).thenReturn(list);

        List<RestaurantResponse> result = restaurantService.getAllRestaurants();

        assertEquals(2, result.size());
        assertEquals("Wok House", result.get(0).getName());
        assertEquals("Chinese", result.get(0).getCategory());
        assertEquals("$$", result.get(0).getPrice());
        assertEquals("10 Main St, Dublin", result.get(0).getDisplayAddress());
        assertEquals("Seoul Kitchen", result.get(1).getName());
    }

    @Test
    void testFilterRestaurants_sortDesc() {
        Restaurant r1 = buildRestaurant(
                1L, "La Piazza", "https://yelp.com/la-piazza", "lapiazza.jpg",
                "Italian", 4.9, "$$$", 53.3438, -6.2417, "+353-345-6789",
                "55 Temple Bar, Dublin",
                "12:00-23:00","12:00-23:00","12:00-23:00","12:00-23:00","12:00-23:00","13:00-23:30","13:00-23:30",
                false, false, true, true, "Fresh pasta and wood-fired pizza", 1003L
        );
        Restaurant r2 = buildRestaurant(
                2L, "Pizza Planet", "https://yelp.com/pizza-planet", "pizzaplanet.jpg",
                "Pizza", 4.2, "$", 53.3384, -6.2326, "+353-456-7890",
                "7 South Lotts Rd, Dublin",
                "11:00-23:00","11:00-23:00","11:00-23:00","11:00-23:00","11:00-23:00","11:00-23:30","11:00-23:30",
                false, false, false, true, "Gourmet pizzas", 1004L
        );
        List<Restaurant> list = Arrays.asList(r1, r2);

        RestaurantFilterRequest filter = new RestaurantFilterRequest();
        filter.setCategories(Arrays.asList("Italian", "Pizza"));
        filter.setPrices(Arrays.asList("$$$", "$"));
        filter.setMeals(Collections.emptyList());
        filter.setSortOrder("desc");

        when(restaurantRepository.filterRestaurants(
                anyList(), anyList(), anyList(), eq(true)
        )).thenReturn(list);

        List<RestaurantResponse> result = restaurantService.filterRestaurants(filter);

        assertEquals(2, result.size());
        // desc，分数高的排前面
        assertEquals("La Piazza", result.get(0).getName());
        assertEquals("Pizza Planet", result.get(1).getName());
    }

    @Test
    void testFilterRestaurants_sortAsc() {
        Restaurant r1 = buildRestaurant(
                1L, "Mediterraneo", "https://yelp.com/mediterraneo", "mediterraneo.jpg",
                "Mediterranean", 4.6, "$$$", 53.3438, -6.2417, "+353-567-8901",
                "120 City Quay, Dublin",
                "11:00-23:00","11:00-23:00","11:00-23:00","11:00-23:00","11:00-23:00","12:00-23:30","12:00-23:30",
                false, true, true, true, "Seafood and Mediterranean dishes", 1005L
        );
        Restaurant r2 = buildRestaurant(
                2L, "Burger Hub", "https://yelp.com/burger-hub", "burgerhub.jpg",
                "American", 3.9, "$", 53.3377, -6.2289, "+353-678-9012",
                "80 Camden St, Dublin",
                "11:00-22:00","11:00-22:00","11:00-22:00","11:00-22:00","11:00-22:00","12:00-23:00","12:00-23:00",
                true, false, true, true, "Classic burgers and fries", 1006L
        );
        List<Restaurant> list = Arrays.asList(r1, r2);

        RestaurantFilterRequest filter = new RestaurantFilterRequest();
        filter.setCategories(Arrays.asList("Mediterranean", "American"));
        filter.setPrices(Arrays.asList("$$$", "$"));
        filter.setMeals(Collections.emptyList());
        filter.setSortOrder("asc");

        when(restaurantRepository.filterRestaurants(
                anyList(), anyList(), anyList(), eq(true)
        )).thenReturn(list);

        List<RestaurantResponse> result = restaurantService.filterRestaurants(filter);

        assertEquals(2, result.size());
        // asc，分数低的排前面
        assertEquals("Burger Hub", result.get(0).getName());
        assertEquals("Mediterraneo", result.get(1).getName());
    }

    @Test
    void testGetRestaurantByOwnerId_found() {
        Restaurant r = buildRestaurant(
                1L, "Wok House", "https://yelp.com/wok-house", "wokhouse.jpg",
                "Chinese", 4.7, "$$", 53.3498, -6.2603, "+353-123-4567",
                "10 Main St, Dublin",
                "08:00-22:00","08:00-22:00","08:00-22:00","08:00-22:00","08:00-22:00","10:00-23:00","10:00-23:00",
                true, false, true, true, "Authentic Sichuan cuisine", 1001L
        );
        when(restaurantRepository.findByOwnerId(1001L)).thenReturn(Optional.of(r));

        RestaurantResponse resp = restaurantService.getRestaurantByOwnerId(1001L);
        assertEquals("Wok House", resp.getName());
        assertEquals("Chinese", resp.getCategory());
    }

    @Test
    void testGetRestaurantByOwnerId_notFound() {
        when(restaurantRepository.findByOwnerId(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                restaurantService.getRestaurantByOwnerId(999L)
        );
        assertEquals("未找到该商户对应的餐厅", ex.getMessage());
    }

    /**
     * 辅助方法，防止构造器太长看不清
     */
    private Restaurant buildRestaurant(Long id, String name, String yelpUrl, String imgUrl,
                                       String category, Double rating, String price, Double latitude,
                                       Double longitude, String phone, String address,
                                       String mondayHours, String tuesdayHours, String wednesdayHours,
                                       String thursdayHours, String fridayHours, String saturdayHours,
                                       String sundayHours,
                                       Boolean goodForBreakfast, Boolean goodForBrunch, Boolean goodForLunch, Boolean goodForDinner,
                                       String description, Long ownerId) {
        Restaurant r = new Restaurant();
        r.setId(id);
        r.setName(name);
        r.setYelpUrl(yelpUrl);
        r.setImgUrl(imgUrl);
        r.setCategory(category);
        r.setRating(rating);
        r.setPrice(price);
        r.setLatitude(latitude);
        r.setLongitude(longitude);
        r.setPhone(phone);
        r.setAddress(address);
        r.setMondayHours(mondayHours);
        r.setTuesdayHours(tuesdayHours);
        r.setWednesdayHours(wednesdayHours);
        r.setThursdayHours(thursdayHours);
        r.setFridayHours(fridayHours);
        r.setSaturdayHours(saturdayHours);
        r.setSundayHours(sundayHours);
        r.setGoodForBreakfast(goodForBreakfast);
        r.setGoodForBrunch(goodForBrunch);
        r.setGoodForLunch(goodForLunch);
        r.setGoodForDinner(goodForDinner);
        r.setDescription(description);
        r.setOwnerId(ownerId);
        return r;
    }
}
