package testutils

import com.google.gson.Gson
import io.ktor.config.MapApplicationConfig
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.TestApplicationRequest
import io.ktor.server.testing.withTestApplication
import org.codingforanimals.veganacademy.server.config.plugins.AppConfig
import org.codingforanimals.veganacademy.server.config.plugins.DatabaseConfig
import org.codingforanimals.veganacademy.server.config.plugins.ServerConfig
import org.codingforanimals.veganacademy.server.database.DatabaseFactory
import org.codingforanimals.veganacademy.server.database.DatabaseFactoryForServerTest
import org.codingforanimals.veganacademy.server.features.model.data.source.RecipeSource
import org.codingforanimals.veganacademy.server.features.model.data.source.RememberMeDataSource
import org.codingforanimals.veganacademy.server.features.model.data.source.UserSource
import org.codingforanimals.veganacademy.server.features.model.data.source.impl.RecipeSourceImpl
import org.codingforanimals.veganacademy.server.features.model.data.source.impl.RememberMeDataSourceImpl
import org.codingforanimals.veganacademy.server.features.model.data.source.impl.UserSourceImpl
import org.codingforanimals.veganacademy.server.features.model.repository.RecipeRepository
import org.codingforanimals.veganacademy.server.features.model.repository.RememberMeRepository
import org.codingforanimals.veganacademy.server.features.model.repository.UserRepository
import org.codingforanimals.veganacademy.server.features.model.repository.impl.RecipeRepositoryImpl
import org.codingforanimals.veganacademy.server.features.model.repository.impl.RememberMeRepositoryImpl
import org.codingforanimals.veganacademy.server.features.model.repository.impl.UserRepositoryImpl
import org.codingforanimals.veganacademy.server.features.model.service.RecipeService
import org.codingforanimals.veganacademy.server.features.model.service.RememberMeService
import org.codingforanimals.veganacademy.server.features.model.service.UserService
import org.codingforanimals.veganacademy.server.features.model.service.impl.RecipeServiceImpl
import org.codingforanimals.veganacademy.server.features.model.service.impl.RememberMeServiceImpl
import org.codingforanimals.veganacademy.server.features.model.service.impl.UserServiceImpl
import org.codingforanimals.veganacademy.server.run
import org.codingforanimals.veganacademy.server.utils.UserUtils
import org.koin.core.module.Module
import org.koin.dsl.module

fun MapApplicationConfig.createConfigForTesting() {
    // Server config
    put("ktor.server.isProd", "false")

    // Database Config
    put("ktor.database.jdbcDriver", "org.h2.Driver")
    put("ktor.database.jdbcDatabaseUrl", "jdbc:h2:mem:;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE")
    put("ktor.database.dbUser", "root")
    put("ktor.database.dbPassword", "password")
    put("ktor.database.maxPoolSize", "1")

    // JWT config
    put("ktor.jwt.issuer", "issuer")
    put("ktor.jwt.secret", "secret")
}

fun getAppConfigForUnitTest(): AppConfig {

    return AppConfig().apply {
        databaseConfig = DatabaseConfig(
            jdbcDriver = "org.h2.Driver",
            jdbcDatabaseUrl = "jdbc:h2:mem:;DATABASE_TO_UPPER=false;MODE=MYSQL",
            dbUser = "root",
            dbPassword = "password",
            maxPoolSize = 1
        )
        serverConfig = ServerConfig(isProd = false)
    }
}

@KtorExperimentalLocationsAPI
fun withTestServer(koinModules: List<Module> = listOf(appTestModule), block: TestApplicationEngine.() -> Unit) {
    withTestApplication(
        {
            (environment.config as MapApplicationConfig).apply {
                createConfigForTesting()
            }
            run(isProd = false, koinModules = koinModules)
        }, block
    )
}

fun setContentTypeFormUrlEncoded(request: TestApplicationRequest) =
    request.addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())

fun setContentTypeText(request: TestApplicationRequest) =
    request.addHeader(HttpHeaders.ContentType, ContentType.Text.Plain.toString())

val appTestModule = module {
    single { Gson() }
    single { getAppConfigForUnitTest() }
    single<DatabaseFactory> { DatabaseFactoryForServerTest(get()) }

    single { UserUtils() }

    single<RememberMeDataSource> { RememberMeDataSourceImpl() }
    single<RememberMeRepository> { RememberMeRepositoryImpl(get()) }
    single<RememberMeService> { RememberMeServiceImpl(get()) }

    single<UserSource> { UserSourceImpl() }
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<UserService> { UserServiceImpl(get(), get()) }

    single<RecipeSource> { RecipeSourceImpl() }
    single<RecipeRepository> { RecipeRepositoryImpl(get()) }
    single<RecipeService> { RecipeServiceImpl(get()) }
}

fun buildRequestBody(json: String) = mapOf("content" to json).toString()