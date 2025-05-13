package com.shaluo.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", contextId = "userClient", url = "${USER_SERVICE_URL}")
public interface UserClient {

    @GetMapping("/api/users/id/{username}")
    Long getUserId(@PathVariable("username") String username);
}
