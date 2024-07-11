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
import phishingnet.contentAnalysis.models.risks.Requirement
import phishingnet.contentAnalysis.models.risks.Risk
import phishingnet.contentAnalysis.models.risks.RiskLevel
import phishingnet.contentAnalysis.models.warnings.Warning
import phishingnet.contentAnalysis.modules.*
import phishingnet.contentAnalysis.modules.experimental_modules.LLMModule
import java.time.Instant
import javax.sql.DataSource


@SpringBootApplication
class PhishingNetApplication {

    @Bean
    fun saltPepperEncoder() = SaltPepperEncoder()

    @Bean
    fun symmetricEncoder() = SymmetricEncoder()

    @get:Bean
    val processor: Processor = Processor(
        listOf(
            FromHistoryModule(),
            HeaderAuthModule(),
            ReturnPathModule(),
            IbanDetectionModule(),
            LanguageToolModule(),
            UrgencyModule(),
            AttachmentExtensionModule(),
            BlackListedTinyUrlModule(),
            GoogleSafeBrowsingApi(),
            LLMModule()
        ), listOf(
            Risk(
                "O remetente pode se estar a tentar passar por outra pessoa",
                "O remetente do email é diferente do caminho de retorno, " +
                        "pode ser indicativo de uma tentativa de falsificação, " +
                        "mas é também uma prática comum em emails empresariais e em particular emails comerciais.",
                RiskLevel.B,
                warningRequirements = mutableMapOf(
                    Warning.FROM_DISTINCT_RETURN_PATH to Requirement(exact = 1)
                ),
            ), Risk(
                "O remetente pode se estar a tentar passar por outra pessoa",
                "Os protocolos de autenticação DKIM e/ou SPF falharam.",
                RiskLevel.D,
                warningRequirements = mutableMapOf(
                    Warning.HEADER_CERTIFICATES_AUTH_FAILED to Requirement(minimum = 1)
                ),
            ), Risk(
                "O remetente pode se estar a tentar passar por outra pessoa",
                "Os protocolos de autenticação DKIM e/ou SPF falharam, " +
                        "e o remetente de email é diferente do caminho de retorno.",
                RiskLevel.E,
                warningRequirements = mutableMapOf(
                    Warning.FROM_DISTINCT_RETURN_PATH to Requirement(exact = 1),
                    Warning.HEADER_CERTIFICATES_AUTH_FAILED to Requirement(minimum = 1)
                ),
            ), Risk(
                "O email pode ter sido comprometido no caminho entre o remetente e o recetor",
                "Os protocolo de autenticação DKIM falhou.",
                RiskLevel.C,
                warningRequirements = mutableMapOf(
                    Warning.DKIM_AUTH_FAILED to Requirement(exact = 1)
                ),
            ),Risk(
                "Possível burla financeira",
                "Este remetente não tem um historial de troca de emails consigo e contém um IBAN, " +
                        "informe-se antes de efetuar pagamentos.",
                RiskLevel.C,
                warningRequirements = mutableMapOf(
                    Warning.ASKS_FOR_IBAN to Requirement(minimum = 1),
                    Warning.PAST_EMAILS_SENT to Requirement(maximum = 3)
                )
            ), Risk(
                "Possível burla financeira",
                "O email está marcado como urgente e contém um IBAN, potencialmente de forma a o apressar " +
                        "a efetuar um pagamento informe-se antes de efetuar pagamentos.",
                RiskLevel.E,
                warningRequirements = mutableMapOf(
                    Warning.ASKS_FOR_IBAN to Requirement(minimum = 1),
                    Warning.URGENCY to Requirement(exact = 1)
                )
            ), Risk(
                "Possível burla financeira",
                "Os protocolos de autenticação DKIM e/ou SPF falharam e contém um IBAN.",
                RiskLevel.F,
                warningRequirements = mutableMapOf(
                    Warning.ASKS_FOR_IBAN to Requirement(minimum = 1),
                    Warning.HEADER_CERTIFICATES_AUTH_FAILED to Requirement(minimum = 1)
                )
            ), Risk(
                "Erros gramaticais",
                "Vários casos de erros gramaticais, no entanto, isto pode se dever à formatação do email.",
                RiskLevel.B,
                warningRequirements = mutableMapOf(Warning.BAD_GRAMMAR to Requirement(minimum = 5))
            ), Risk(
                "Anexo potencialmente malicioso detetado",
                "Foi detetada a existência de anexos executáveis.",
                RiskLevel.D,
                warningRequirements = mutableMapOf(Warning.FILE_ATTACHED_CAN_BE_DANGEROUS to Requirement(minimum = 1))
            ), Risk(
                "Existem links encurtados no email",
                "Os links presentes no email têm um destino deconhecido, e por isso potencialmente perigoso",
                RiskLevel.D,
                warningRequirements = mutableMapOf(Warning.URL_SHORTENED to Requirement(minimum = 1))
            ), Risk(
                "Existem links maliciosos no email",
                "Os links presentes no email foram detetados como sendo maliciosos pela Google Safe API.",
                RiskLevel.F,
                warningRequirements = mutableMapOf(Warning.MALICIOUS_URL to Requirement(minimum = 1))
            ), Risk(
                "O modelo de Inteligência Artificial detetou uma possível ameaça de phishing",
                "O modelo de Inteligência Artificial detetou que o email analisado se assemelha a um email de phishing",
                RiskLevel.E,
                warningRequirements = mutableMapOf(Warning.LLM_TRIGGERED to Requirement(exact = 1))
            ),
        )
    )

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
