package com.shaluo.orderservice;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.shaluo.orderservice.client")
@SpringBootApplication
public class OrderServiceApplication {

	public static void main(String[] args) {

		// 检查系统环境变量DB_HOST 是否存在
		if (System.getenv("DB_HOST") == null) {

			// 如果系统变量DB_HOST没有，说明是本地开发，需要手动加载 .env 文件
			// 如果存在，是生产环境，依靠docker compose up文件注入
			Dotenv dotenv = Dotenv.configure()
					.directory("backend/order-service") // 指定本地 .env 文件所在目录
					.filename(".env.dev-local")
					.load();

			// 将变量设置到系统属性中，Spring Boot 才能识别 ${}

			// postgres
			System.setProperty("DB_HOST", dotenv.get("DB_HOST"));
			System.setProperty("DB_PORT", dotenv.get("DB_PORT"));
			System.setProperty("DB_NAME", dotenv.get("DB_NAME"));
			System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
			System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));

			// jwt secret
			System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));

			// mongo db
			System.setProperty("MONGO_HOST", dotenv.get("MONGO_HOST"));
			System.setProperty("MONGO_PORT", dotenv.get("MONGO_PORT"));
			System.setProperty("MONGO_DB", dotenv.get("MONGO_DB"));

			System.setProperty("USER_SERVICE_URL", dotenv.get("USER_SERVICE_URL"));
			System.setProperty("RESTAURANT_SERVICE_URL", dotenv.get("RESTAURANT_SERVICE_URL"));
			System.setProperty("CART_SERVICE_URL", dotenv.get("CART_SERVICE_URL"));

			System.setProperty("EUREKA_CLIENT_SERVICEURL_DEFAULTZONE", dotenv.get("EUREKA_CLIENT_SERVICEURL_DEFAULTZONE"));

			// RabbitMQ
			System.setProperty("RABBITMQ_HOST", dotenv.get("RABBITMQ_HOST"));
			System.setProperty("RABBITMQ_PORT", dotenv.get("RABBITMQ_PORT"));
			System.setProperty("RABBITMQ_USER", dotenv.get("RABBITMQ_USER"));
			System.setProperty("RABBITMQ_PASSWORD", dotenv.get("RABBITMQ_PASSWORD"));



		}

		SpringApplication.run(OrderServiceApplication.class, args);
	}

}
