package phishingnet.api

import phishingnet.api.http.pipeline.AuthenticationInterceptor
import phishingnet.api.http.pipeline.UserArgumentResolver
import phishingnet.api.security.SaltPepperEncoder
import phishingnet.api.security.SymmetricEncoder
import phishingnet.api.utils.Clock
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.time.Instant
import javax.sql.DataSource


@SpringBootApplication
class PhishingNetApplication{

	@Bean
	fun saltPepperEncoder() = SaltPepperEncoder()

	@Bean
	fun symmetricEncoder() = SymmetricEncoder()

	@Bean
	fun clock() = object : Clock {
		override fun now() = Instant.now()
	}
}

@Configuration
@Component
class SetDataSource {
	@ConfigurationProperties(prefix = "spring.datasource")
	@Bean
	@Primary
	fun dataSource(): DataSource? {
		val url = System.getenv("DB_URL")

		return DataSourceBuilder
			.create()
			.url(url)
			.build()
	}
}

@Configuration
class WebConfig : WebMvcConfigurer {
	override fun addCorsMappings(registry: CorsRegistry) {
		registry.addMapping("/**")
			.allowedOrigins("*") // Troque para a origem da sua extensão
			.allowedMethods("GET", "POST", "PUT", "DELETE")
			.allowedHeaders("*") // Permitir todos os headers necessários
	}
}

@Configuration
class PipelineConfigurer(
	val authenticationInterceptor: AuthenticationInterceptor,
	val userArgumentResolver: UserArgumentResolver,
) : WebMvcConfigurer {

	override fun addInterceptors(registry: InterceptorRegistry) {
		registry.addInterceptor(authenticationInterceptor)
	}

	override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
		resolvers.add(userArgumentResolver)
	}
}

fun main(args: Array<String>) {
	runApplication<PhishingNetApplication>(*args)
}
