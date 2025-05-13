package com.shaluo.notificationservice;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NotificationServiceApplication {

	public static void main(String[] args) {


		// 检查系统环境变量 是否存在
		if (System.getenv("RABBITMQ_HOST") == null) {

			Dotenv dotenv = Dotenv.configure()
					.directory("backend/notification-service") // 指定本地 .env 文件所在目录
					.filename(".env.dev-local")
					.load();

			// RabbitMQ
			System.setProperty("RABBITMQ_HOST", dotenv.get("RABBITMQ_HOST"));
			System.setProperty("RABBITMQ_PORT", dotenv.get("RABBITMQ_PORT"));
			System.setProperty("RABBITMQ_USER", dotenv.get("RABBITMQ_USER"));
			System.setProperty("RABBITMQ_PASSWORD", dotenv.get("RABBITMQ_PASSWORD"));



		}


		SpringApplication.run(NotificationServiceApplication.class, args);
	}

}
