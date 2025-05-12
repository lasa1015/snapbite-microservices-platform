package com.shaluo.gatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class GatewayServiceApplication {

	public static void main(String[] args) {

		if (System.getenv("EUREKA_CLIENT_SERVICEURL_DEFAULTZONE") == null) {

			Dotenv dotenv = Dotenv.configure()
					.directory("./gateway-service") // 指定本地 .env 文件所在目录
					.filename(".env.dev-local")
					.load();

			// 将变量设置到系统属性中，Spring Boot 才能识别 ${}


			System.setProperty("EUREKA_CLIENT_SERVICEURL_DEFAULTZONE", dotenv.get("EUREKA_CLIENT_SERVICEURL_DEFAULTZONE"));
		}

		SpringApplication.run(GatewayServiceApplication.class, args);
	}
}
