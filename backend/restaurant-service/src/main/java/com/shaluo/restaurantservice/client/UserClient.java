package com.shaluo.restaurantservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "user-service", url = "${USER_SERVICE_URL}")
public interface UserClient {

    @GetMapping("/api/users/id/{username}")
    Long getUserId(@PathVariable("username") String username);
}