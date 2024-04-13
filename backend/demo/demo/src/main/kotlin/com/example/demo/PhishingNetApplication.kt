package com.example.demo

import com.example.demo.utils.Clock
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import java.time.Instant
import javax.sql.DataSource

@SpringBootApplication
class PhishingNetApplication{

	@Bean
	fun clock() = object : Clock {
		override fun now() = Instant.now()
	}
}

@Configuration
@Component
class setDataSource {
	@ConfigurationProperties(prefix = "spring.datasource")
	@Bean
	@Primary
	fun dataSource(): DataSource? {
		val url = System.getenv("JDBC_DATABASE_URL")
			?: "jdbc:postgresql://localhost:5433/phishingnet?user=postgres&password=admin"

		return DataSourceBuilder
			.create()
			.url(url)
			.build()
	}
}

fun main(args: Array<String>) {
	runApplication<PhishingNetApplication>(*args)
}
