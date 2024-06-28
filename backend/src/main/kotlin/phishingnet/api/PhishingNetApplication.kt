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
import phishingnet.contentAnalysis.Processor
import phishingnet.contentAnalysis.models.AnalysisModule
import phishingnet.contentAnalysis.models.risks.Requirement
import phishingnet.contentAnalysis.models.risks.Risk
import phishingnet.contentAnalysis.models.risks.RiskLevel
import phishingnet.contentAnalysis.models.warnings.Warning
import phishingnet.contentAnalysis.modules.*
import java.time.Instant
import javax.sql.DataSource


@SpringBootApplication
class PhishingNetApplication {

    @Bean
    fun saltPepperEncoder() = SaltPepperEncoder()

    @Bean
    fun symmetricEncoder() = SymmetricEncoder()

    @Bean
    fun moduleList(): List<AnalysisModule> = listOf(
        FromHistoryModule(),
        HeaderAuthModule(),
        ReturnPathModule(),
        IbanDetectionModule(),
        LanguageToolModule()
    )

    @Bean
    fun riskList(): List<Risk> = listOf(
        Risk(
            "Email sender suspicious",
            "Email sender might be trying to impersonate someone you know.",
            RiskLevel.C,
            warningRequirements = mutableMapOf(
                Warning.HEADER_CERTIFICATES_AUTH_FAILED to Requirement(exact = 1),
                Warning.FROM_DISTINCT_FROM_RETURN_PATH to Requirement(exact = 1)
            )
        ), Risk(
            "Possible financial scam",
            "The email comes from a new contact and contains an IBAN.",
            RiskLevel.E,
            warningRequirements = mutableMapOf(
                Warning.ASKS_FOR_IBAN to Requirement(minimum = 1),
                Warning.PAST_EMAILS_SENT to Requirement(maximum = 3)
            )
        ), Risk(
            "Grammatical errors",
            "Grammatical errors detected",
            RiskLevel.B,
            warningRequirements = mutableMapOf(
                Warning.BAD_GRAMMAR to Requirement(minimum = 3)

            )
        )
    )

    @Bean
    fun processor(moduleList: List<AnalysisModule>, riskList: List<Risk>): Processor = Processor(moduleList, riskList())

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
