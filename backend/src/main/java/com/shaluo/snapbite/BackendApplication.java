package com.shaluo.snapbite;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {

		// 检查系统环境变量DB_HOST 是否存在
		if (System.getenv("DB_HOST") == null) {

			// 如果系统变量DB_HOST没有，说明是本地开发，需要手动加载 .env 文件
			// 如果存在，是生产环境，依靠docker compose up文件注入
			Dotenv dotenv = Dotenv.configure()
					.directory("./backend") // 指定本地 .env 文件所在目录
					.filename(".env.dev-local")
					.load();

			// 将变量设置到系统属性中，Spring Boot 才能识别 ${}
			System.setProperty("DB_HOST", dotenv.get("DB_HOST"));
			System.setProperty("DB_PORT", dotenv.get("DB_PORT"));
			System.setProperty("DB_NAME", dotenv.get("DB_NAME"));
			System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
			System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));

		}

		SpringApplication.run(BackendApplication.class, args);
	}

}
