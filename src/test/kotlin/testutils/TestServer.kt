package testutils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.ktor.config.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.server.testing.*
import org.codingforanimals.veganacademy.config.plugins.AppConfig
import org.codingforanimals.veganacademy.config.plugins.DatabaseConfig
import org.codingforanimals.veganacademy.config.plugins.ServerConfig
import org.codingforanimals.veganacademy.database.DatabaseFactory
import org.codingforanimals.veganacademy.database.DatabaseFactoryForServerTest
import org.codingforanimals.veganacademy.features.model.data.source.RecipeSource
import org.codingforanimals.veganacademy.features.model.data.source.UserSource
import org.codingforanimals.veganacademy.features.model.data.source.impl.RecipeSourceImpl
import org.codingforanimals.veganacademy.features.model.data.source.impl.UserSourceImpl
import org.codingforanimals.veganacademy.features.model.repository.RecipeRepository
import org.codingforanimals.veganacademy.features.model.repository.UserRepository
import org.codingforanimals.veganacademy.features.model.repository.impl.RecipeRepositoryImpl
import org.codingforanimals.veganacademy.features.model.repository.impl.UserRepositoryImpl
import org.codingforanimals.veganacademy.run
import org.koin.core.module.Module
import org.koin.dsl.module

val gson: Gson = GsonBuilder().create()

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
            run(testing = true, koinModules = koinModules)
        }, block
    )
}

fun setContentTypeFormUrlEncoded(request: TestApplicationRequest) =
    request.addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())

fun setContentTypeText(request: TestApplicationRequest) =
    request.addHeader(HttpHeaders.ContentType, ContentType.Text.Plain.toString())

val appTestModule = module {
    single { getAppConfigForUnitTest() }
    single { JwtService(get()) }
    single<DatabaseFactory> { DatabaseFactoryForServerTest(get()) }

    single<UserSource> { UserSourceImpl() }
    single<UserRepository> { UserRepositoryImpl(get()) }

    single<RecipeSource> { RecipeSourceImpl() }
    single<RecipeRepository> { RecipeRepositoryImpl(get()) }
}

fun buildRequestBody(json: String) = mapOf("content" to json).toString()